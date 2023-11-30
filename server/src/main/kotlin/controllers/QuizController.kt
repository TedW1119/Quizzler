package controllers

import com.aallam.openai.api.LegacyOpenAI
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import services.QuizService
import util.DataModels.Question
import util.DataModels.Quiz

class QuizController {
    private val questionController: QuestionController = QuestionController()
    private val noteController: NoteController = NoteController()
    private val quizService: QuizService = QuizService()

    @OptIn(LegacyOpenAI::class)
    suspend fun generateQuiz(quiz: Quiz) {
        // TODO: THIS WORKS FOR MCQ and T/F Only
        val note = noteController.getNote(quiz.noteId)

        if (note == null) {
            // throw
            return
        }
        val text = note.text

        // Select difficulty
        val difficultyPrompt = when (quiz.difficulty) {
            "Easy" -> "Also, write me this question as if I was in Grade 1, as in elementary school, so make it easy difficulty."
            "Medium" -> "Also, write me this question as if I was in Grade 9, as in high school, so make it medium difficulty."
            else -> "Also, write me this question as if I was in University Year One, as in university or college, so make it hard difficulty."
        }
        val difficultyLength = when (quiz.difficulty) {
            "Easy" -> "10"
            "Medium" -> "15"
            else -> "20"
        }

        val openAI = OpenAI("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")

        // Attempt to Generate Question:
        try {
            val questions: List<Question>

            // Result is stored as empty string
            var result = ""

            // 1. Multiple Choice Questions
            if (quiz.questionType == "MCQ" ) {
                // Complete Request Using OpenAPI
                val completionRequest = CompletionRequest(
                    model = ModelId("text-davinci-003"),
                    prompt = "Given the sample text: \n" + text + "\n \n" +
                            "Now, create ${quiz.totalQuestions} multiple choice question(s). Store them in a JSON of the following type: data class Question( val _id: String, val question: String, val type: String, val options: List<String>, val hint: String,  marks: Int, val answer: String ) Only worry about the question, options, and answer fields, and keep the question and options as concise as possible (less than $difficultyLength words each). Follow this example for ${quiz.totalQuestions} multiple choice questions, but an example of one multiple choice question stored in the JSON (remember to append the JSON correctly for more than one question) is:" + """ [ { "_id": "1", "question": "What is the primary role of an operating system?", "type": "MCQ", "options": ["Run applications", "Manage hardware resources", "Create virtual CPUs", "Handle file operations"], "hint": "", "marks": 0, "answer": "Manage hardware resources" } ] $difficultyPrompt""",
                    maxTokens = 300,
                    temperature = 0.02
                )
                // Complete and Decode from String
                val completion: TextCompletion = openAI.completion(completionRequest)
                result = completion.choices[0].text.trim()

            // 2. T/F Questions
            } else if (quiz.questionType == "T/F") {

                // Complete Request Using OpenAPI
                val completionRequest = CompletionRequest(
                    model = ModelId("text-davinci-003"),
                    prompt = "Given the sample text: \n" + text + "\n \n" +
                            "Now, create ${quiz.totalQuestions} true and false question(s). Store them in a JSON of the following type: data class Question( val _id: String, val question: String, val type: String, val options: List<String>, val hint: String,  marks: Int, val answer: String ) Only worry about the question, options, and answer fields, and keep the question and options as concise as possible (less than $difficultyLength words each). Follow this example for ${quiz.totalQuestions} true and false questions, but an example of one true and false question stored in the JSON (remember to append the JSON correctly for more than one question) is:" + """ [ { "_id": "1", "question": "An operating system is responsible for running applications.", "type": "T/F", "options": ["True", "False"], "hint": "", "marks": 0, "answer": "False" } ] $difficultyPrompt""",
                    maxTokens = 300,
                    temperature = 0.02
                )
                // Complete and Decode from String
                val completion: TextCompletion = openAI.completion(completionRequest)
                result = completion.choices[0].text.trim()

            } else if (quiz.questionType == "Mix") {

                // Complete Request Using OpenAPI
                val completionRequest = CompletionRequest(
                    model = ModelId("text-davinci-003"),
                    prompt = "Given the sample text: \n" + text + "\n \n" +
                            "Now, create a total ${quiz.totalQuestions} multiple choice and true/false question(s). Store them in a JSON of the following type: data class Question( val _id: String, val question: String, val type: String, val options: List<String>, val hint: String,  marks: Int, val answer: String ) Only worry about the question, options, and answer fields, and keep the question and options as concise as possible (less than $difficultyLength words each). Follow this example for ${quiz.totalQuestions} multiple choice and true/false questions, but an example of one multiple choice question and one true/false question stored in the JSON together (remember to append the JSON correctly for more than one question) is:" + """ [ { "_id": "1", "question": "An operating system is responsible for running applications.", "type": "T/F", "options": ["True", "False"], "hint": "", "marks": 0, "answer": "False" }, { "_id": "2", "question": "An operating system is responsible for running applications.", "type": "T/F", "options": ["True", "False"], "hint": "", "marks": 0, "answer": "False" } ] Remember, that was an example of the structure, I need a JSON of length ${quiz.totalQuestions} and I need a mix of both types of questions. $difficultyPrompt""",
                    maxTokens = 500,
                    temperature = 0.02
                )
                // Complete and Decode from String
                val completion: TextCompletion = openAI.completion(completionRequest)
                result = completion.choices[0].text.trim()

            }

            // Error Handling Check
            if (result[0] == '{') {
                result = "[$result]"
            }
            questions = Json.decodeFromString(result)

            // Make correct updates for Each Question
            val (finalQuestions, finalQuestionIds) = questions.map { question ->
                val newId = ObjectId().toString()
                question.copy(_id = newId) to newId
            }.unzip()

            // Print the modified questions and Upload each question to the quiz
            finalQuestions.forEach {
                questionController.createQuestion(it)
                println(it)
            }

            // Copy Result into Quiz
            val finalQuiz = quiz.copy(questionIds = finalQuestionIds)

            // Create Quiz
            createQuiz(finalQuiz)
        } catch (e: Exception) {
            println("An error occurred: $e")
        }
    }

    fun createQuiz(quiz: Quiz) {
        quizService.createQuiz(quiz)
    }

    fun getQuiz(id: String): Quiz? {
        return quizService.getQuiz(id)
    }

    fun getQuizzesByAccountId(accountId: String): List<Quiz>? {
        return quizService.getQuizzesByAccountId((accountId))
    }


    fun deleteQuiz(id: String): Boolean {
        return quizService.deleteQuiz(id)
    }
}