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
import java.io.File

@Composable
@Preview
fun quizUpload(changePage: (String) -> Unit) {
    var selectedFile by remember { mutableStateOf<File?>(null) }

    // Callback for handling cancelling
    fun handleCancel() {
        changePage("Landing")
    }

    // Callback for going to quiz creation settings
    fun handleQuizCreation() {
        changePage("QuizCreation")
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

                button("Next", true, ::handleQuizCreation)
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
