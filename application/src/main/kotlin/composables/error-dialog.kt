package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun errorDialog(
    title: String,
    text: String,
    closeDialog: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { closeDialog() },
        text = {
            Column() {
                Text(title, color = Color.Red)
                Text(text)
            }
        },
        buttons = { }
    )
}