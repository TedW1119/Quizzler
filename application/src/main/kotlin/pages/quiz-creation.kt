package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.buttonWithIndicator
import composables.errorDialog
import composables.primaryButton
import composables.secondaryButton
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
    var totalMarks:Double = -1.0
    var hint:Boolean = false
    var time:Int = 600 //seconds ?
}

@Composable
@Preview
fun quizCreation(changePage: (String) -> Unit, accountId: String, noteId: String) {
    val quizForm = QuizFormData
    val quizController = QuizController()

    // Track error state
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // track state of settings
    var selectedQuestionCount by remember { mutableStateOf(5) }
    var selectedDifficulty by remember { mutableStateOf("Easy") }
    var selectedQuestionType by remember { mutableStateOf("MCQ") }

    fun handleCreateQuiz() {
        val quiz = Quiz(
            _id = ObjectId().toString(),
            accountId = accountId,
            questionIds = mutableListOf(),
            name = quizForm.quizName,
            subject = quizForm.quizSubject,
            difficulty = quizForm.questionDifficulty,
            questionType = quizForm.questionType,
            totalQuestions = quizForm.totalQuestions,
            totalMarks = quizForm.totalMarks,
            hint = quizForm.hint,
            time = quizForm.time,
            noteId = noteId
        )
        quizController.generateQuiz(quiz)
    }

    // Callback for going back to upload
    fun handleCancel() {
        changePage("QuizUpload")
    }

    // Callback for going back to landing page (after quiz created)
    fun handleNext() {
        // Check for valid name and subject
        if (quizForm.quizName == "" || quizForm.quizSubject == "") {
            error = "You must enter a name and subject."
            showErrorDialog = true
            return
        }

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
                secondaryButton("Cancel",::handleCancel)

                Text(
                    "Select Your Quizzer Style",
                    fontSize = 32.sp,
                )

                primaryButton("Next", ::handleNext)
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
                                    "Number of Questions",
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
                                    buttonWithIndicator(
                                        text = "5",
                                        isSelected = selectedQuestionCount == 5,
                                        onClick = {
                                            quizForm.totalQuestions = 5
                                            selectedQuestionCount = 5
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "10",
                                        isSelected = selectedQuestionCount == 10,
                                        onClick = {
                                            quizForm.totalQuestions = 10
                                            selectedQuestionCount = 10
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "20",
                                        isSelected = selectedQuestionCount == 20,
                                        onClick = {
                                            quizForm.totalQuestions = 20
                                            selectedQuestionCount = 20
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "2?",
                                        isSelected = selectedQuestionCount == 2,
                                        onClick = {
                                            quizForm.totalQuestions = 2
                                            selectedQuestionCount = 2
                                        }
                                    )
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
                                    buttonWithIndicator(
                                        text = "Easy",
                                        isSelected = selectedDifficulty == "Easy",
                                        onClick = {
                                            quizForm.questionDifficulty = "Easy"
                                            selectedDifficulty = "Easy"
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "Medium",
                                        isSelected = selectedDifficulty == "Medium",
                                        onClick = {
                                            quizForm.questionDifficulty = "Medium"
                                            selectedDifficulty = "Medium"
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "Hard",
                                        isSelected = selectedDifficulty == "Hard",
                                        onClick = {
                                            quizForm.questionDifficulty = "Hard"
                                            selectedDifficulty = "Hard"
                                        }
                                    )
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
                                    buttonWithIndicator(
                                        text = "MCQ",
                                        isSelected = selectedQuestionType == "MCQ",
                                        onClick = {
                                            quizForm.questionType = "MCQ"
                                            selectedQuestionType = "MCQ"
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "T/F",
                                        isSelected = selectedQuestionType == "T/F",
                                        onClick = {
                                            quizForm.questionType = "T/F"
                                            selectedQuestionType = "T/F"
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    buttonWithIndicator(
                                        text = "Mix",
                                        isSelected = selectedQuestionType == "Mix",
                                        onClick = {
                                            quizForm.questionType = "Mix"
                                            selectedQuestionType = "Mix"
                                        }
                                    )
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
                                    .offset(0.dp, 24.dp),
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
                                        field -> formField(field, true, ::updateField)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Render dialogs
        if (showErrorDialog) {
            errorDialog("Error Creating Quiz", error) {
                showErrorDialog = false
                error = ""
            }
        }
    }
}
