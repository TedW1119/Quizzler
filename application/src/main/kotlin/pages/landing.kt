package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.button
import composables.menubar
import composables.settingsOverlay
import controllers.AccountController
import utils.Constants.NUM_PFP
import kotlin.random.Random

@Composable
@Preview
fun landing(changePage: (String, MutableMap<Any, Any>) -> Unit, accountId: String, profilePicId: Int) {

    val data: MutableMap<Any, Any> = mutableMapOf()
    // Query account data
    val accountController = AccountController()
    val account = accountController.getAccount(accountId) ?: return
    val username = account.name

    var isSidebarVisible by remember { mutableStateOf(false) }
    var isSettingsVisible by remember { mutableStateOf(false) }
    var currentProfilePic by remember { mutableStateOf(profilePicId) }
    val interactionSource = remember { MutableInteractionSource() }

    fun handleCreateQuiz() {
        data["profilePicId"] = currentProfilePic
        changePage("QuizUpload", data)
    }
    fun handleViewQuizzes() {
        data["profilePicId"] = currentProfilePic
        changePage("QuizList", data)
    }
    fun handleLogout() {
        changePage("Login", data)
    }
    fun handleOpenAccountSettings() {
        data["profilePicId"] = currentProfilePic
        changePage("AccountSettings", data)
    }

    fun shuffleProfilePic() {
        currentProfilePic = Random.nextInt(0, NUM_PFP)
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
        menubar(
            randomizePic = { shuffleProfilePic() },
            settingsClick = { isSettingsVisible = !isSettingsVisible },
            profilePicId = currentProfilePic
        )

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
                text = "Quizzler",
                style = TextStyle(
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                modifier = Modifier
                    .offset(0.dp, 112.dp),
                text = "Welcome Back $username",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                modifier = Modifier
                    .offset(0.dp, 400.dp),
                text = "An Abnormally Distributed product.",
                style = TextStyle(
                    fontSize = 10.sp
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                button("Create a Quiz", true, ::handleCreateQuiz, Modifier.padding(100.dp))
                button("View my Quizzes", true, ::handleViewQuizzes, Modifier.padding(100.dp))
            }
        }
    }
}

