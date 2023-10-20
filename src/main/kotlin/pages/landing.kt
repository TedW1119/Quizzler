package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import composables.menubar
import composables.sidebar
import composables.button
import composables.settingsOverlay

@Composable
@Preview
fun landing(changePage: (String) -> Unit) {
    val username = "Mr. Creeper"
    var isSidebarVisible by remember { mutableStateOf(false) }
    var isSettingsVisible by remember { mutableStateOf(false) }

    fun handleCreateQuiz() {
        changePage("QuizCreation")
    }
    fun handleTakeQuiz() {
        changePage("QuizTaking")
    }

    Box(
        modifier = Modifier.fillMaxSize()
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
                settingsOverlay()
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
                button("Create a Quiz", true, ::handleCreateQuiz, Modifier.padding(100.dp))
                button("View my Quizzes", true, ::handleTakeQuiz, Modifier.padding(100.dp))

            }
        }
    }
}

