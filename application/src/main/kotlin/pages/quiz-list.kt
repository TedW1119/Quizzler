package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.alertDialog
import composables.primaryButton
import composables.secondaryButton
import controllers.AccountController
import controllers.QuizController
import utils.DataModels.Quiz

@Composable
fun quizList(changePage: (String, MutableMap<Any, Any>) -> Unit, accountId: String) {
    val accountController = AccountController()
    val quizController = QuizController()
    var quizzes by remember { mutableStateOf(accountController.getAccountQuizzes(accountId)) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteQuizIndex by remember { mutableStateOf(-1) }

    fun handleQuizTaking(quiz: Quiz) {
        val newData: MutableMap<Any, Any> = mutableMapOf(
            "quiz" to quiz,
        )
        changePage("QuizTaking", newData)
    }

    fun handleExitQuizList() {
        val newData: MutableMap<Any, Any> = mutableMapOf()
        changePage("Landing", newData)
    }

    fun handleUploadQuiz() {
        val newData: MutableMap<Any, Any> = mutableMapOf()
        changePage("QuizUpload", newData)
    }

    fun handleDeleteQuiz() {
        if (deleteQuizIndex == -1) { // deleteQuizIndex has not been set
            return
        }
        quizController.deleteQuiz(quizzes[deleteQuizIndex]._id)
        quizzes = quizzes.filter { it != quizzes[deleteQuizIndex] }
        showDeleteDialog = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Quizzes") },
                actions = {
                    secondaryButton("Exit", ::handleExitQuizList)
                }
            )
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(350.dp),
            contentPadding = PaddingValues(
                start = 50.dp,
                top = 50.dp,
                end = 50.dp,
                bottom = 50.dp
            )
        ) {
            if (quizzes.isNotEmpty()) {
                items(quizzes.size) { index ->
                    Card(
                        backgroundColor = Color.LightGray,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .clickable(onClick = { handleQuizTaking(quizzes[index]) }),
                        elevation = 16.dp,
                    ) {
                        Column( // Use a Column to stack Texts vertically
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(
                                quizzes[index].name,
                                fontSize = 25.sp,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                "Subject: ${quizzes[index].subject}",
                                fontSize = 15.sp,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(2.dp)
                            )
                            Text(
                                "Difficulty: ${quizzes[index].difficulty}",
                                fontSize = 15.sp,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(2.dp)
                            )
                            Text(
                                "Latest Grade: ${if (quizzes[index].totalMarks == -1.0) "—" else "${quizzes[index].totalMarks}%"}",
                                fontSize = 15.sp,
                                color = Color(0xFFFFFFFF),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(2.dp)
                            )
                            primaryButton("Delete Quiz") {
                                showDeleteDialog = true
                                deleteQuizIndex = index
                            }
                        }
                    }
                }
            } else {
                items(1) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("You have not created any quizzes. Click below to create a quiz.")
                        Spacer(modifier = Modifier.height(10.dp))
                        primaryButton("Create a Quiz", ::handleUploadQuiz)
                    }
                }
            }
        }
    }
    if (showDeleteDialog) {
        alertDialog(
            setShowDialog = { showDeleteDialog = false },
            title = "Quiz Deletion",
            content = "Are you sure you want to delete this quiz?",
            primaryButtonText = "Delete",
            handlePrimaryButton = ::handleDeleteQuiz,
            secondaryButtonText = "Cancel",
            handleSecondaryButton = { showDeleteDialog = false }
        )
    }
}