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
import controllers.AccountController
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
//import utils.DataModels.Account

@Composable
@Preview
fun login(changePage: (String, MutableMap<Any, Any>) -> Unit) {
    val accountController: AccountController = AccountController()
    var data: MutableMap<Any, Any> = mutableMapOf()

    // Track the input field values
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Handle the login button press
    fun handleLogin() {
        val account = accountController.getAccountFromLogin(identifier)
        if (account == null) {
            // TODO: display error message
            println("No account found")
        } else if (account.password != password) {
            // TODO: display error message
            println("Password does not match")
        } else {
            data = mutableMapOf("accountId" to account._id)
            changePage("Landing", data)
        }
    }

    // Handle the create new account button press
    fun handleCreateAccount() {
        changePage("AccountCreation", data)
    }

    // TODO: remove this function after testing
    fun handleAutoLoginTEMP() {
        data = mutableMapOf("accountId" to "654276f4bd672473259d1812")
        changePage("Landing", data)
    }

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

        button("Login", true, ::handleLogin)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Don't have an account? Click below to create one!",
            style = TextStyle(
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: DECIDE WHERE TO PLACE THIS BUTTON
        button("Create Account", true, ::handleCreateAccount)

        // TODO: remove this button after testing
        Spacer(modifier = Modifier.height(16.dp))
        button("Auto-login (TEMP)", true, ::handleAutoLoginTEMP)
    }
}