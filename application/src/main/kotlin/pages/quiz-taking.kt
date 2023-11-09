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
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

data class Questions (
    val id: Int,
    val question: String,
    val type: String,
    val options: List<String>,
    val hint: String?,
    val marks: Int,
    val answer: String)

@Composable
@Preview
fun quizTaking(changePage: (String) -> Unit, data: MutableMap<Any, Any>) {
    val uri = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"
    val client = MongoClient.create(uri)
    val database = client.getDatabase("abnormally-distributed")
    val collection = database.getCollection<Questions>("questions")
    val questionIds: List<String> = data["questionIds"] as List<String>
    var questions: List<Questions>
    runBlocking {
        questions = collection.find(Filters.`in`("id", questionIds)).toList()
    }
    var selectedOption by remember { mutableStateOf(1) }

    fun handleExitQuiz() {
        changePage("QuizList")
    }

    Column(
            modifier = Modifier.fillMaxSize().background(Color.White).padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "4. What does HTML stand for?",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Column(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Question 4 of 10", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .height(5.dp)
                            .width(120.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.Green)
                                .fillMaxHeight()
                                .width(45.dp)  // Assuming 4 out of 10 questions are completed, adjust this as needed
                        )
                    }
                }
            }

            Box (
                // set weight to 1f so that it pushes the buttons to the button of the page
                modifier = Modifier.weight(1f, fill = true).fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    val options = listOf("Hyper Text Makeup Language", "Hyper Text Markup Language", "Happy Turtles Munch Lettuce", "How To Machine Learning")
                    options.forEachIndexed { index, option ->
                        radioButtonOption(
                            text = option,
                            selected = selectedOption == index + 1,
                            onSelect = { selectedOption = index + 1 }
                        )
                    }
                }
            }


            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { handleExitQuiz() }) {
                    Text("Exit Quiz")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {}) {
                    Text("Previous Question")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {}) {
                    Text("Next Question")
                }
            }
        }
    }

@Composable
fun radioButtonOption(text: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(text = text, modifier = Modifier.padding(start = 8.dp))
    }
}
