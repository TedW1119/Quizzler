package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import composables.button
import composables.slider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlin.time.DurationUnit

// Create data class to represent a MongoDB document
data class Settings (val hints: Boolean,
                     val bonus: Boolean,
                     val time: Int)
data class Quizzes (val id: Int,
                    val accountId: Int,
                    val questionIds: List<Int>,
                    val name: String,
                    val subject: String,
                    val difficulty: String,
                    val settings: Settings,
                    val totalMarks: Int)

@Composable
@Preview
fun quizCreation(changePage: (String) -> Unit) {

    fun mongoCreate() {
        // Replace the placeholder with your MongoDB deployment's connection string
        val uri = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"

        val mongoClient = MongoClient.create(uri)
        val database = mongoClient.getDatabase("abnormally-distributed")
        val collection = database.getCollection<Quizzes>("quizzes")

        runBlocking {
            val result = collection.insertOne(
                Quizzes(2, 1, listOf(12, 213, 123), "test", "history", "easy", Settings(hints = true, bonus = true, 1), 11)
            )
        }

        mongoClient.close()
    }

    // Callback for handling cancelling
    fun handleCancel() {
        changePage("Landing")
    }

    // Callback for handling upload
    fun handleNext() {
        changePage("QuizUpload")
        mongoCreate()
    }

    // Overall Box
    Box(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.background(Color.White).padding(16.dp),

        ) {
            // Title Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                button("Cancel", true, ::handleCancel)

                Text(
                    "Select Your Quizzer Style",
                    fontSize = 32.sp,
                )

                button("Next", true, ::handleNext)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selection Section
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // Row #1
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .weight(1f)
                    ) {
                        // # of Questions Box
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                        ) {
                            // Title of Box
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(2f)
                                    .background(Color.Gray, shape = RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "# of Questions",
                                    fontSize = 16.sp,
                                )
                            }
                            // Options
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(onClick = {}) {
                                        Text("5")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}) {
                                        Text("10")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}) {
                                        Text("20")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}) {
                                        Text("?")
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        // Difficulty Box
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                        ) {
                            // Title of Box
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(2f)
                                    .background(Color.Gray, shape = RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Question Types",
                                    fontSize = 16.sp,
                                )
                            }
                            // Options
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(onClick = {}, modifier = Modifier.background(Color.Green)) {
                                        Text("Easy")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}, modifier = Modifier.background(Color.Yellow)) {
                                        Text("Medium")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}, modifier = Modifier.background(Color.Red)) {
                                        Text("Hard")
                                    }
                                }
                            }
                        }
                    }

                    // Row #2
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .weight(1f)
                    ) {
                        // Question Types Box
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(Color.LightGray, shape = RoundedCornerShape(16.dp)),
                        ) {
                            // Title of Box
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(2f)
                                    .background(Color.Gray, shape = RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Question Types",
                                    fontSize = 16.sp,
                                )
                            }
                            // Options
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(onClick = {}) {
                                        Text("MCQ")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}) {
                                        Text("MSQ")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}) {
                                        Text("T/F")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {}) {
                                        Text("SA")
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        // Extra Settings Box
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .background(Color.LightGray, shape = RoundedCornerShape(16.dp))
                        ) {
                            // Title of Box
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(2f)
                                    .background(Color.Gray, shape = RoundedCornerShape(16.dp))
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    "Extra Settings",
                                    fontSize = 16.sp,
                                )
                            }
                            // Options
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                slider()
                            }
                        }
                    }
                }
            }
        }
    }
}
