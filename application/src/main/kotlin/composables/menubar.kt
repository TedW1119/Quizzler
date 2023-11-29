package composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.getProfilePic


@Composable
fun menubar(sidebarClick: () -> Unit, settingsClick: () -> Unit, profilePicId: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF6200EE)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // hamburger button
        Box(
            modifier = Modifier
                .size(56.dp)
                .padding(8.dp)
                .clickable { sidebarClick() }
        ) {
            Image(
                painter = painterResource("hamburger.png"),
                contentDescription = "hamburger sidebar",
                modifier = Modifier.fillMaxWidth()
            )

        }

        // app name text
        Text("Abnormally Distributed", fontSize = 20.sp,)

        // profile button
        Box(
            modifier = Modifier
                .size(56.dp)
                .padding(8.dp)
                .clickable { settingsClick() }
        ) {
            Image(
                painter = painterResource(getProfilePic(profilePicId)),
                contentDescription = "profile menu",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
