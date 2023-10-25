package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import composables.button

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

    // Handle the create new account button press
    fun handleCreateAccount() {
        changePage("AccountCreation")
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

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Don't have an account? Click below to create one!",
            style = TextStyle(
                fontSize = 14.sp
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TODO: DECIDE WHERE TO PLACE THIS BUTTON
        button("I need an account this instant since this product is so amazing!", true, ::handleCreateAccount)
    }
}