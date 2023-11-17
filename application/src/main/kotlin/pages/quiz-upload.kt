package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.button
import controllers.NoteController
import org.bson.types.ObjectId
import utils.DataModels.Note
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


/*
** Generate Quiz - Generate the quiz
 */
fun generateNote(selectedFile: File): Note {
    val output = StringBuilder()

    // 1. Read all lines from the file and save them into one string
    try {
        BufferedReader(FileReader(selectedFile)).use { br ->
            br.lines().forEach {
                output.append(it).append("\n")
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    // 2. create object and return its id
    return Note(
        _id = ObjectId().toString(),
        text = output.toString()
    )
}

@Composable
@Preview
fun quizUpload(changePage: (String, MutableMap<Any, Any>) -> Unit) {
    var selectedFile by remember { mutableStateOf<File?>(null) }
    val noteController = NoteController()

    // Callback for handling cancelling
    fun handleCancel() {
        changePage("Landing", mutableMapOf())
    }

    fun handleNext() {
        // Send file to backend
        val note = if (selectedFile != null) {
            generateNote(selectedFile!!)
        } else {
            // TODO make alert for need a file selected to proceed
            return
        }

        // insert note into mongo
        try {
            noteController.upsertNote(note)

            val newData: MutableMap<Any, Any> = mutableMapOf(
                "noteTextId" to note._id,
            )
//            println("CREATED NEW NOTE WITH ID ${note._id} and text ${note.text}")
            changePage("QuizCreation", newData)
        } catch (e: Exception) {
            println("Error inserting note")
            changePage("Landing", mutableMapOf())
        }
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
                    "Upload Your Slide Deck Here",
                    fontSize = 32.sp,
                )

                button("Next", true, ::handleNext)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // File Drop Area
            if (selectedFile != null) {
                Text("Selected File: ${selectedFile?.name}", fontSize = 20.sp)
            } else {
                Button(
                    onClick = {
                        val fileDialog = utils.FileDialog()   // Create new file Dialog
                        selectedFile = fileDialog.getFileToOpen()   // Get the file to open
                    }
                ) {
                    Text("Upload File")
                }
            }
        }
    }
}

