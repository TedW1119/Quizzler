package pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun quizResult(changePage: (String) -> Unit, data: MutableMap<Any, Any>) {
    val scorePair = data["quizScore"] as Pair<*, *>
    val score = scorePair.first as Int
    val questionCount = scorePair.second as Int
    val result = calculatePercentage(score, questionCount)

    fun handleExit() {
        changePage("QuizList")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "You have submitted!",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your Score: $score/$questionCount ($result%)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = ::handleExit) {
            Text("Return to Quiz List")
        }
    }
}
