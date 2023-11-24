package pages.account

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import composables.*
import controllers.AccountController
import org.bson.types.ObjectId
import utils.DataModels.Account

@Composable
@Preview
fun accountCreation(changePage: (String, MutableMap<Any, Any>) -> Unit) {
    val accountController: AccountController = AccountController()
    var data: MutableMap<Any, Any> = mutableMapOf()

    // Track error state
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Store form data and fields
    val formData = AccountFormData
    val fields = listOf(
        "Name",
        "Username",
        "Email",
        "Password",
        "Confirm Password"
    )

    // Handle going back to login page
    fun handleGoBack() {
        changePage("Login", data)
    }

    // Handle creating a new account
    fun handleCreateAccount() {

        // Check for matching passwords
        if (formData.password != formData.confirmPassword) {
            error = "The password and confirmed password do not match."
            showErrorDialog = true
            return
        }

        // Check for unique username and email
        val existingUsername = accountController.getAccountFromLogin(formData.username)
        val existingEmail = accountController.getAccountFromLogin(formData.email)
        if (existingUsername != null && existingEmail != null) {
            error = "An account with this username and this email exists."
            showErrorDialog = true
            return
        } else if (existingUsername != null) {
            error = "An account with this username exists."
            showErrorDialog = true
            return
        } else if (existingEmail != null) {
            error = "An account with this email exists."
            showErrorDialog = true
            return
        }

        // Create the account
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

    // Render UI
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFCBC3E3)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        fields.forEach {
            field -> formField(field, true, ::updateField)
            Spacer(modifier = Modifier.height(24.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            secondaryButton("Return to Login", ::handleGoBack)
            Spacer(modifier = Modifier.width(20.dp))

            primaryButton("Create Account", ::handleCreateAccount)
        }
    }

    // Render dialogs
    if (showErrorDialog) {
        errorDialog("Error Creating Account", error) {
            showErrorDialog = false
            error = ""
        }
    }
}