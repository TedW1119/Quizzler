package pages.account

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
import pages.Account

//import utils.DataModels.Account



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
        val collection = database.getCollection<Account>("Account")

        runBlocking {
            val result = collection.insertOne(
                Account(2, FormData.name, FormData.username, FormData.email, FormData.password, "university", "2")
            )
        }
        changePage("Landing")
    }

    // Handle updating a field value
    fun updateField(field: String, value: String) {
        when (field) {
            "Name" -> FormData.name = value
            "Username" -> FormData.username = value
            "Email" -> FormData.email = value
            "Password" -> FormData.password = value
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        fields.forEach {
            field -> formField(field, false, ::updateField)
            Spacer(modifier = Modifier.height(24.dp))
        }

        button("Login", true, ::handleCreateAccount)
    }
}