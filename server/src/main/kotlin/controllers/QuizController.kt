package controllers

import com.aallam.openai.api.LegacyOpenAI
import com.aallam.openai.api.completion.CompletionRequest
import com.aallam.openai.api.completion.TextCompletion
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId
import services.QuizService
import util.Constants.SAMPLE_TEXT
import util.DataModels.Question
import util.DataModels.Quiz

class QuizController {
    private val questionController: QuestionController = QuestionController()
    private val quizService: QuizService = QuizService()

    @OptIn(LegacyOpenAI::class)
    suspend fun generateQuiz(quiz: Quiz) {
        // TODO: Given txt
        // TODO: THIS WORKS FOR MCQ Only
        val text = SAMPLE_TEXT

        val openAI = OpenAI("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")

        // Attempt to Generate Question:
        try {
            val questions: List<Question>

            // Complete Request Using OpenAPI
            val completionRequest = CompletionRequest(
                model = ModelId("text-davinci-003"),
                prompt = "Given the sample text: \n" + text + "\n \n" +
                        "Now, create ${quiz.totalMarks} multiple choice question(s). Store them in a JSON of the following type: data class Question( val _id: String, val question: String, val type: String, val options: List<String>, val hint: String,  marks: Int, val answer: String ) Only worry about the question, options, and answer fields, and keep the question and options as concise as possible (less than 10 words each). Follow this example for ${quiz.totalMarks} multiple choice questions, but an example of one multiple choice question stored in the JSON (remember to append the JSON correctly for more than one question) is:" + """ [ { "_id": "1", "question": "What is the primary role of an operating system?", "type": "MCQ", "options": ["Run applications", "Manage hardware resources", "Create virtual CPUs", "Handle file operations"], "hint": "", "marks": 0, "answer": "Manage hardware resources" } ] """,
                maxTokens = 300,
                temperature = 0.02
            )

            // Complete and Decode from String
            val completion: TextCompletion = openAI.completion(completionRequest)
            var result = completion.choices[0].text.trim()

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