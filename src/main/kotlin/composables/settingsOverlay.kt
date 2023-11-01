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
fun settingsOverlay(handleLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .offset(0.dp, 56.dp)
            .height(150.dp)
            .width(150.dp)
            .background(Color.Gray.copy(alpha=0.98f))
            .padding(15.dp)
    ) {
        // settings content, including menu items, can be added here
        // Note: just text as placeholders for now, they will be buttons in the future
        Text("Profile", Modifier.padding(2.dp), fontSize = 15.sp)
        Text("Settings", Modifier.padding(2.dp), fontSize = 15.sp)
        button("Logout", false, handleLogout)
    }
}
