package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.errorDialog
import composables.primaryButton
import composables.secondaryButton
import controllers.NoteController
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.bson.types.ObjectId
import utils.DataModels.Note
import utils.FileDialog
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

/*
** Private File Type Function - Determines Extension
 */
private fun getFileType(file: File): String {
    val fileName = file.name
    val extension = fileName.substringAfterLast('.', "")

    // Return Statements
    return when (extension) {
        "txt" -> "txt"
        else -> "pdf"   // front end handles all other cases (must be pdf)
    }
}

/*
** Generate Quiz - Generate the quiz
 */
fun generateNote(selectedFile: File, fileType: String): Note {
    var finalOutput = ""

    // For TXT
    if (fileType == "txt") {
        val output = StringBuilder()
        // Read all lines from the file and save them into one string
        try {
            BufferedReader(FileReader(selectedFile)).use { br ->
                br.lines().forEach {
                    output.append(it).append("\n")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        finalOutput = output.toString()
    // For PDF
    } else {
        // Read all lines from the file and save them into one string
        try {
            val document = PDDocument.load(selectedFile)
            val textStripper = PDFTextStripper()
            finalOutput = textStripper.getText(document)
            document.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Create object and return its id
    return Note(
        _id = ObjectId().toString(),
        text = finalOutput
    )
}

@Composable
@Preview
fun quizUpload(changePage: (String, MutableMap<Any, Any>) -> Unit) {
    var selectedFile by remember { mutableStateOf<File?>(null) }
    val noteController = NoteController()

    // Track error state
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Callback for handling cancelling
    fun handleCancel() {
        changePage("Landing", mutableMapOf())
    }

    fun handleNext() {
        // Send file to backend
        val note = if (selectedFile != null) {
            val fileType = getFileType(selectedFile!!)
            generateNote(selectedFile!!, fileType)
        } else {
            error = "You must upload a file."
            showErrorDialog = true
            return
        }

        // insert note into Mongo
        try {
            noteController.upsertNote(note)

            val newData: MutableMap<Any, Any> = mutableMapOf(
                "noteTextId" to note._id,
            )
            changePage("QuizCreation", newData)
        } catch (e: Exception) {
            println("Error inserting note")
            changePage("Landing", mutableMapOf())
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                secondaryButton("Cancel", ::handleCancel)
                Text(
                    "File Upload",
                    fontSize = 24.sp,
                )
                primaryButton("Next", ::handleNext)
            }
        }
    ) {
        Column (
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            // File Upload Area
            OutlinedButton(
                onClick = {
                    val fileDialog = FileDialog()   // Create new file Dialog
                    selectedFile = fileDialog.getFileToOpen()   // Get the file to open
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(vertical = 16.dp),
                border = BorderStroke(2.dp, Color.Gray),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.primary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Upload File Here",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text("Selected File: ${if (selectedFile != null) selectedFile?.name else "None"}", fontSize = 20.sp)
        }
    }

    // Render dialogs
    if (showErrorDialog) {
        errorDialog("Error Uploading a File", error) {
            showErrorDialog = false
            error = ""
        }
    }
}
