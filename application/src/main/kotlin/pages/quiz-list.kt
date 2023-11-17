package pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.button
import controllers.QuizController
import kotlinx.serialization.json.Json
import utils.Constants.BASE_URL
import utils.DataModels.Quiz
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

fun getQuizList(accountId: String): List<Quiz> {
    val client = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .uri(URI.create("${BASE_URL}/account/${accountId}/quizzes"))
        .GET()
        .build()
    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    return Json.decodeFromString<List<Quiz>>(response.body())
}

@Composable
fun quizList(changePage: (String, MutableMap<Any, Any>) -> Unit, accountId: String) {
    val quizzes = getQuizList(accountId)
    val quizController = QuizController()

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My Quizzes") },
                actions = {
                    Button(onClick = ::handleExitQuizList) {
                        Text("Exit")
                    }
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
            ),
            content = {
                if (quizzes.isNotEmpty()) {
                    items(quizzes.size) { index ->
                        Card(
                            backgroundColor = Color.LightGray,
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .clickable( onClick = { handleQuizTaking(quizzes[index]) } ),
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
                                    "Total Marks: ${quizzes[index].totalMarks}%",
                                    fontSize = 15.sp,
                                    color = Color(0xFFFFFFFF),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(2.dp)
                                )

                                fun deleteQuiz() {
                                    quizController.deleteQuiz(quizzes[index]._id)
                                }
                                button("Delete Quiz", true, ::deleteQuiz)
                            }
                        }
                    }
                } else {
                    items(1) {
                        Column (
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("You have not created any quizzes. Click below to create a quiz.")
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(onClick = ::handleUploadQuiz) {
                                Text("Create a Quiz")
                            }
                        }
                    }
                }
            }
        )
    }
}