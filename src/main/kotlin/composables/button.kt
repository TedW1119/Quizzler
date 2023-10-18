package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*

@Composable
@Preview
fun button(text: String, filled: Boolean, action: () -> Unit) {

    MaterialTheme {
        if (filled) {
            Button(onClick = action) {
                Text(text)
            }
        } else {
            OutlinedButton(onClick = action) {
                Text(text)
            }
        }
    }

}