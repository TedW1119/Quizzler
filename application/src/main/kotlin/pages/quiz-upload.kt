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
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.gridfs.GridFSBuckets
import composables.button
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException


/*
** sendFileToMongo - Sends file to mongo
 */
fun sendFileToMongo(selectedFile: File) {
    try {
        val uri = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"
        val mongoClient = MongoClients.create(uri)
        val database = mongoClient.getDatabase("abnormally-distributed")
        val gridFSBucket = GridFSBuckets.create(database, "newBucket")

        // TODO: Create a GridFSBucket
//        FileInputStream(selectedFile).use { streamToUploadFrom ->
//            // Defines options that specify configuration information for files uploaded to the bucket
//            val options = GridFSUploadOptions()
//                .chunkSizeBytes(1048576)
//                .metadata(Document("type", "pptx"))
//            // Uploads a file from an input stream to the GridFS bucket
//            val fileId: ObjectId = gridFSBucket.uploadFromStream("powerpoint.pptx", streamToUploadFrom, options)
//            // Prints the "_id" value of the uploaded file
//            println("The file id of the uploaded file is: " + fileId.toHexString())
//        }
    } catch (e: Exception) {
        println("Error sending file to MongoDB: ${e.message}")
    }
}

/*
** Generate Quiz - Generate the quiz
 */
fun generateQuiz(selectedFile: File) {
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
    output.toString()
    println(output)

    // 2. Send output to OPENAI
    // TODO: Call backend
}

@Composable
@Preview
fun quizUpload(changePage: (String) -> Unit) {
    var selectedFile by remember { mutableStateOf<File?>(null) }

    // Callback for handling cancelling
    fun handleCancel() {
        changePage("Landing")
    }

    // TODO: Callback for handling starting the quiz
    fun handleNext() {
        if (selectedFile != null) {             // Send file to mongo
            sendFileToMongo(selectedFile!!)
            generateQuiz(selectedFile!!)        // generate quiz on next
        }
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

