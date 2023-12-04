package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.alertDialog
import composables.primaryButton
import composables.secondaryButton
import controllers.QuestionController
import controllers.QuizController
import utils.DataModels.Quiz
import utils.calculatePercentage

@Composable
@Preview
fun quizTaking(changePage: (String, MutableMap<Any, Any>) -> Unit, data: MutableMap<Any, Any>) {
    val quiz = data["quiz"] as Quiz
    var questionIndex by remember { mutableStateOf(0) }
    val answers = remember { mutableStateListOf<String?>(null) }
    val questionController = QuestionController()
    val questions = remember { mutableStateListOf(questionController.getQuestion(quiz.questionIds.first()))}

    // LaunchedEffect ensures that code does not rerun in recomposition
    LaunchedEffect(Unit) {
        for (i in 0..quiz.questionIds.size) {
            answers.add(null)
        }
    }

    var showSubmitDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    var selectedOption by remember { mutableStateOf<String?>(null) }

    fun handleExitQuiz() {
        val newData: MutableMap<Any, Any> = mutableMapOf()
        changePage("QuizList", newData)
    }

    fun handleSubmit() {
        // calculate the number of correct answers
        var correctCount = 0
        for (i in 0 until questions.size) {
            if (questions[i]?.answer == answers[i]) {
                correctCount += 1
            }
        }

        val newTotalMarks = calculatePercentage(correctCount, quiz.questionIds.size)
        val updatedQuiz = quiz.copy(totalMarks = newTotalMarks)
        val quizController = QuizController()
        quizController.updateQuiz(updatedQuiz)

        val newData: MutableMap<Any, Any> = mutableMapOf(
            "quizScore" to Pair(correctCount, quiz.questionIds.size)
        )
        changePage("QuizResult", newData)
    }

    Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = questions[questionIndex]!!.question,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp),
                        softWrap = true
                    )
                }
                Column(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Question ${questionIndex + 1} of ${quiz.questionIds.size}", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .height(5.dp)
                            .width(100.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Green)
                                .fillMaxHeight()
                                .width((100 * (questionIndex + 1) / quiz.questionIds.size).dp)
                        )
                    }
                }
            }

            Box (
                // set weight to 1f so that it pushes the buttons to the button of the page
                modifier = Modifier.weight(1f, fill = true).fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val options = questions[questionIndex]!!.options
                    options.forEachIndexed { _, option ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = option == selectedOption,
                                onClick = {
                                    selectedOption = option
                                    answers[questionIndex] = option
                                }
                            )
                            Text(text = option, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                secondaryButton("Exit Quiz") {
                    showExitDialog = true
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        questionIndex -= 1
                        selectedOption = if (answers[questionIndex] != null) {
                            answers[questionIndex]
                        } else {
                            null
                        }
                    },
                    enabled = questionIndex != 0
                ) {
                    Text("Previous Question")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        questionIndex += 1
                        if (questions.size == questionIndex) {
                            questions.add(questionController.getQuestion(quiz.questionIds[questionIndex]))
                        }
                        selectedOption = if (answers[questionIndex] != null) {
                            answers[questionIndex]
                        } else {
                            null
                        }
                    },
                    enabled = questionIndex < quiz.questionIds.size - 1) {
                    Text("Next Question")
                }
                Spacer(modifier = Modifier.width(8.dp))
                primaryButton("Submit") {
                    showSubmitDialog = true
                }
            }
        }

    if (showSubmitDialog) {
        val numCompleted = answers.count { it != null }
        alertDialog(
            setShowDialog = { showSubmitDialog = false },
            title = "Are you sure you want to submit the quiz?",
            content = "You have completed ${numCompleted}/${quiz.questionIds.size} questions.",
            primaryButtonText = "Submit",
            handlePrimaryButton = ::handleSubmit,
            secondaryButtonText = "Cancel",
            handleSecondaryButton = { showSubmitDialog = false }
        )
    }

    if (showExitDialog) {
        alertDialog(
            setShowDialog = { showExitDialog = false },
            title = "Are you sure you want to leave the quiz?",
            content = "Your result will not be saved.",
            primaryButtonText = "Exit",
            handlePrimaryButton = ::handleExitQuiz,
            secondaryButtonText = "Cancel",
            handleSecondaryButton = { showExitDialog = false }
        )
    }
}
