package composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun settingsOverlay(
    handleOpenAccountSettings: () -> Unit,
    handleLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .offset(0.dp, 56.dp)
            .height(130.dp)
            .width(150.dp)
            .background(Color.Gray.copy(alpha=0.98f))
            .padding(15.dp)
    ) {
        // settings content including menu items
        secondaryButton("Profile", handleOpenAccountSettings)
        secondaryButton("Logout", handleLogout)
    }
}
