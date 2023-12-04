package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.errorDialog
import composables.primaryButton
import composables.secondaryButton
import controllers.AccountController
import utils.Constants.NUM_PFP
import kotlin.random.Random

@Composable
@Preview
fun login(changePage: (String, MutableMap<Any, Any>) -> Unit) {
    val accountController = AccountController()
    val data: MutableMap<Any, Any> = mutableMapOf()

    // Track the input field values
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Track error state
    var showErrorDialog by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    // Handle login
    fun handleLogin() {
        val account = accountController.getAccountFromLogin(identifier)
        if (account == null) {
            error = "No account found for the given credentials."
            showErrorDialog = true
        } else if (account.password != password) {
            error = "The password is incorrect."
            showErrorDialog = true
        } else {
            data["accountId"] = account._id
            data["profilePicId"] = Random.nextInt(0, NUM_PFP)
            changePage("Landing", data)
        }
    }

    // Handle creating a new account
    fun handleCreateAccount() {
        changePage("AccountCreation", data)
    }

    // Render UI
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFCBC3E3)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quizzler",
            style = TextStyle(
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Rizz up your Quiz Game!",
            style = TextStyle(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = identifier,
            onValueChange = { input -> identifier = input },
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

        primaryButton("Login", ::handleLogin)
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Don't have an account?",
                style = TextStyle(
                    fontSize = 14.sp
                )
            )
            Spacer(modifier = Modifier.width(20.dp))

            secondaryButton("Create Account", ::handleCreateAccount)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "An Abnormally Distributed product.",
            style = TextStyle(
                fontSize = 10.sp
            )
        )
    }

    // Render dialogs
    if (showErrorDialog) {
        errorDialog("Login Error", error) {
            showErrorDialog = false
            error = ""
        }
    }
}