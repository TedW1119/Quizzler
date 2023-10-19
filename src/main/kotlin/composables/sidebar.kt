package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun sidebar() {
    Column(
        modifier = Modifier
            .offset(0.dp, 56.dp)
            .fillMaxHeight()
            .width(250.dp)
            .background(Color.Gray.copy(alpha=0.98f))
    ) {
        // Sidebar content, including menu items, can be added here

        // continue taking
        Column(

        )
        {
            Text("Continue Taking", Modifier.padding(5.dp), fontSize = 25.sp)
            Text("History of the World", Modifier.padding(2.dp), fontSize = 15.sp)
            Text("Labour Economics", Modifier.padding(2.dp), fontSize = 15.sp)
            Text("UI Design 101", Modifier.padding(2.dp), fontSize = 15.sp)
        }

        Column(modifier = Modifier
            .height(50.dp)
        ) {}

        // recommended quizzes
        Column(

        )
        {
            Text("Recommended", Modifier.padding(5.dp), fontSize = 25.sp)
            Text("The Cold War", Modifier.padding(2.dp), fontSize = 15.sp)
            Text("Trade Policies", Modifier.padding(2.dp), fontSize = 15.sp)
            Text("UI Design 102", Modifier.padding(2.dp), fontSize = 15.sp)
        }


    }
}
