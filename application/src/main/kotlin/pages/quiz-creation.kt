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
import composables.button
import controllers.QuizController
import org.bson.types.ObjectId
import pages.account.formField
import utils.DataModels.Quiz

//import utils.DataModels.Settings
//import utils.DataModels.Quiz

// Create object to store form data
object QuizFormData {
    // default values to prevent sending nulls
    var quizName:String = ""
    var quizSubject:String = ""
    var questionDifficulty:String = "Easy"
    var questionType:String = "MCQ"
    var totalQuestions:Int = 5
    var totalMarks:Double = 5.0
    var hint:Boolean = false
    var time:Int = 600 //seconds ?
}

@Composable
@Preview
fun quizCreation(changePage: (String) -> Unit) {
    val quizForm = QuizFormData
    val quizController = QuizController()

    // track state of settings
    var selectedQuestionCount by remember { mutableStateOf(5) }
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    var selectedQuestionType by remember { mutableStateOf("MCQ") }

    fun handleCreateQuiz() {
        val quiz = Quiz(
            _id = ObjectId().toString(),
            accountId = "654ea337eb947a7ceabb0643", // TODO change this to whoever is logged in
            questionIds = mutableListOf<String>(), // ?
            name = quizForm.quizName,
            subject = quizForm.quizSubject,
            difficulty = quizForm.questionDifficulty,
            questionType = quizForm.questionType,
            totalQuestions = quizForm.totalQuestions,
            totalMarks = quizForm.totalMarks,
            hint = quizForm.hint,
            time = quizForm.time
        )
        quizController.upsertQuiz(quiz)
    }

    // Callback for going back to upload
    fun handleCancel() {
        changePage("QuizUpload")
    }

    // Callback for going back to landing page (after quiz created)
    fun handleNext() {
        // note that the state remains the same, so the previous settings will still be the same as before when creating another quiz. TODO design decision on whether to keep this or not
        // only name and subject being reset right now
        handleCreateQuiz()
        quizForm.quizName = ""
        quizForm.quizSubject = ""
        quizForm.totalQuestions = 5
        quizForm.questionDifficulty = "Easy"
        quizForm.questionType = "MCQ"
        changePage("Landing")
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
                                    Button(onClick = {
                                        quizForm.totalQuestions = 5
                                        quizForm.totalMarks = 5.0
                                        println("set number of questions to 5")
                                        selectedQuestionCount = 5
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionCount == 5) Color.Blue else Color.Gray)
                                    ) {
                                        Text("5")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.totalQuestions = 10
                                        quizForm.totalMarks = 10.0
                                        println("set number of questions to 10")
                                        selectedQuestionCount = 10
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionCount == 10) Color.Blue else Color.Gray)
                                    ) {
                                        Text("10")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.totalQuestions = 20
                                        quizForm.totalMarks = 20.0
                                        println("set number of questions to 20")
                                        selectedQuestionCount = 20
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionCount == 20) Color.Blue else Color.Gray)
                                    ) {
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
                                    "Question Difficulty",
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
                                    Button(
                                        onClick = {
                                            quizForm.questionDifficulty = "Easy";
                                            println("set difficult of questions to Easy")
                                            selectedDifficulty = "Easy"
                                        },
                                        modifier = Modifier
                                            .background(if (selectedDifficulty == "Easy") Color.Green else Color.Gray)
                                    ) {
                                        Text("Easy")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.questionDifficulty = "Medium";
                                        println("set difficult of questions to Medium")
                                        selectedDifficulty = "Medium"
                                    },
                                        modifier = Modifier
                                            .background(if (selectedDifficulty == "Medium") Color.Yellow else Color.Gray)
                                    ) {
                                        Text("Medium")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.questionDifficulty = "Hard";
                                        println("set difficult of questions to Hard")
                                        selectedDifficulty = "Hard"
                                    },
                                        modifier = Modifier
                                            .background(if (selectedDifficulty == "Hard") Color.Red else Color.Gray)
                                    ) {
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
                                    Button(onClick = {
                                        quizForm.questionType = "MCQ";
                                        println("set question type to MCQ")
                                        selectedQuestionType = "MCQ"
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionType == "MCQ") Color.Blue else Color.Gray)
                                    ) {
                                        Text("MCQ")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.questionType = "MSQ";
                                        println("set question type to MSQ")
                                        selectedQuestionType = "MSQ"
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionType == "MSQ") Color.Blue else Color.Gray)
                                    ) {
                                        Text("MSQ")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.questionType = "T/F";
                                        println("set question type to T/F")
                                        selectedQuestionType = "T/F"
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionType == "T/F") Color.Blue else Color.Gray)
                                    ) {
                                        Text("T/F")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = {
                                        quizForm.questionType = "SA";
                                        println("set question type to SA")
                                        selectedQuestionType = "SA"
                                    },
                                        modifier = Modifier
                                            .background(if (selectedQuestionType == "SA") Color.Blue else Color.Gray)
                                    ) {
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
                                    .fillMaxHeight()
                                    .offset(0.dp, 32.dp),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    val fields = listOf(
                                        "Name",
                                        "Subject"
                                    )

                                    fun updateField(field: String, value: String) {
                                        when (field) {
                                            "Name" -> quizForm.quizName = value
                                            "Subject" -> quizForm.quizSubject = value
                                        }
                                    }

                                    fields.forEach {
                                        field -> formField(field, false, ::updateField)
                                        Spacer(modifier = Modifier.height(6.dp))
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
