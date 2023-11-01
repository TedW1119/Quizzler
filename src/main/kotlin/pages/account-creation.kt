package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.runtime.*
import com.mongodb.kotlin.client.coroutine.MongoClient
import composables.button
import kotlinx.coroutines.runBlocking

// OBJECT: store form data
object FormData {
    var name: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""
}

// HELPER: render an input text row of the form
@Composable
@Preview
fun formField(field: String, updateField: (String, String) -> Unit) {
    var fieldValue by remember { mutableStateOf("") }

    // Handle the field being updated
    fun onFieldUpdate(value: String) {
        updateField(field, value)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = field
        )

        OutlinedTextField(
            value = fieldValue,
            onValueChange = {
                input -> fieldValue = input
                onFieldUpdate(fieldValue)
            }
        )
    }
}

@Composable
@Preview
fun accountCreation(changePage: (String) -> Unit) {

    // Store form data and fields
    // TODO: generate list of fields from FormData keys?
    val formData = FormData
    val fields = listOf(
        "Name",
        "Username",
        "Email",
        "Password"
    )

    // Handle the login button press
    fun handleCreateAccount() {

        // Replace the placeholder with your MongoDB deployment's connection string
        val uri = "mongodb+srv://abnormally:distributed@abnormally-distributed.naumhbd.mongodb.net/?retryWrites=true&w=majority"

        val mongoClient = MongoClient.create(uri)
        val database = mongoClient.getDatabase("abnormally-distributed")
        val collection = database.getCollection<Accounts>("accounts")

        runBlocking {
            val result = collection.insertOne(
                Accounts(2, formData.name, formData.username, formData.email, formData.password, "university", "2")
            )
        }
        changePage("Landing")
    }

    // Handle updating a field value
    fun updateField(field: String, value: String) {
        when (field) {
            "Name" -> formData.name = value
            "Username" -> formData.username = value
            "Email" -> formData.email = value
            "Password" -> formData.password = value
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        fields.forEach {
            field -> formField(field, ::updateField)
            Spacer(modifier = Modifier.height(24.dp))
        }

        button("Login", true, ::handleCreateAccount)
    }
}