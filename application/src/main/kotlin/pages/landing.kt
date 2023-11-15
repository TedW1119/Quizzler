package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.button
import composables.menubar
import composables.settingsOverlay
import composables.sidebar
import controllers.AccountController
import utils.DataModels.Account

@Composable
@Preview
fun landing(changePage: (String) -> Unit, accountId: String) {
    val accountController: AccountController = AccountController()
    val account = accountController.getAccount(accountId) ?: return
    val username = account.name
    var isSidebarVisible by remember { mutableStateOf(false) }
    var isSettingsVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    fun handleUploadQuiz() {
        changePage("QuizUpload")
    }
    fun handleViewQuizzes() {
        changePage("QuizList")
    }
    fun handleLogout() {
        changePage("Login")
    }
    fun handleOpenAccountSettings() {
        changePage("AccountSettings")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable (
                interactionSource = interactionSource,
                indication = null
            ) {
                isSidebarVisible = false
                isSettingsVisible = false
            }
    )
    {
        menubar(sidebarClick = { isSidebarVisible = !isSidebarVisible }, settingsClick = { isSettingsVisible = !isSettingsVisible })

        // sidebar
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxHeight()
                .width(250.dp)
        ) {
            if (isSidebarVisible) {
                sidebar()
            }
        }

        // profile settings
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxHeight()
                .width(150.dp)
        ) {
            if (isSettingsVisible) {
                settingsOverlay(::handleOpenAccountSettings, ::handleLogout)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(0.dp, 56.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Welcome text
            Text(
                modifier = Modifier
                    .offset(0.dp, 56.dp),
                text = "Welcome Back $username",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            // create quiz and view quiz button
            val input = "test input"
            //fun handleSubmit() {
            //    println(input)
            //}
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                button("Create a Quiz", true, ::handleUploadQuiz, Modifier.padding(100.dp))
                button("View my Quizzes", true, ::handleViewQuizzes, Modifier.padding(100.dp))

            }
        }
    }
}

