package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
@Preview
fun input(label: String) {
    val text = remember { mutableStateOf("") }

    OutlinedTextField(
        value = text.value,
        singleLine = true,
        onValueChange = { newText -> text.value = newText },
        label = { Text(label) }
    )
}