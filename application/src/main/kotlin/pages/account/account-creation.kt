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
import controllers.AccountController
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import utils.DataModels.Account



@Composable
@Preview
fun accountCreation(changePage: (String, MutableMap<Any, Any>) -> Unit) {
    val accountController: AccountController = AccountController()
    var data: MutableMap<Any, Any> = mutableMapOf()

    // Store form data and fields
    var formData = AccountFormData
    val fields = listOf(
        "Name",
        "Username",
        "Email",
        "Password",
        "Confirm Password"
    )

    // Handle the login button press
    fun handleCreateAccount() {
        // TODO: perform error checking here
        val accountId = ObjectId().toString()
        val account = Account(
            accountId,
            formData.name,
            formData.username,
            formData.email,
            formData.password,
            "testSchool",
            "testPicId"
        )
        accountController.upsertAccount(account)
        data = mutableMapOf("accountId" to accountId)
        changePage("Landing", data)
    }

    // Handle updating a field value
    fun updateField(field: String, value: String) {
        when (field) {
            "Name" -> formData.name = value
            "Username" -> formData.username = value
            "Email" -> formData.email = value
            "Password" -> formData.password = value
            "Confirm Password" -> formData.confirmPassword = value
            "Education Level" -> formData.educationLevel = value
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

        button("Create Account", true, ::handleCreateAccount)
    }
}