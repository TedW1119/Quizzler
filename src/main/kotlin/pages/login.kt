package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoClient
import composables.button
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

// Create data class to represent a MongoDB document
data class Accounts(val id: Int,
                    val name: String,
                    val username: String,
                    val email: String,
                    val password: String,
                    val educationLevel: String,
                    val profilePicId: String)

@Composable
@Preview
fun login(changePage: (String) -> Unit) {

    // Track the input field values
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Handle the login button press
    fun handleLogin() {
        changePage("Landing")
    }

    fun mongo() {
        // Replace the placeholder with your MongoDB deployment's connection string
        val uri = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"

        val mongoClient = MongoClient.create(uri)
        val database = mongoClient.getDatabase("abnormally-distributed")
        val collection = database.getCollection<Accounts>("accounts")

        runBlocking {
            val doc = collection.find(Filters.eq("username", "thesnipe")).firstOrNull()
            if (doc != null) {
                println(doc)
            } else {
                println("No matching documents found.")
            }
        }

        mongoClient.close()
    }

    // Handle the create new account button press
    //fun handleCreateAccount() {
    //}

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Abnormally Distributed",
            style = TextStyle(
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Rizz up your Quiz Game",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { input -> username = input },
            label = { Text("Email or Username") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { input -> password = input },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        button("Login", true, ::handleLogin)

        Spacer(modifier = Modifier.height(16.dp))

        button("Mongo", true, ::mongo)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Don't have an account? Click here to create one!",
            style = TextStyle(
                fontSize = 14.sp
            )
        )
    }
}