package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun button(text: String, filled: Boolean, action: () -> Unit, mod: androidx.compose.ui.Modifier) {
    MaterialTheme {
        if (filled) {
            Button(onClick = action, modifier = mod) {
                Text(text)
            }
        } else {
            OutlinedButton(onClick = action, modifier = mod) {
                Text(text)
            }
        }
    }
}

// TODO: remove all instances of this button
@Composable
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

@Composable
fun primaryButton(text: String, action: () -> Unit) {
    Button(onClick = action) {
        Text(text)
    }
}

@Composable
fun secondaryButton(text: String, action: () -> Unit) {
    Button(
        onClick = action,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEEEEEE))
    ) {
        Text(text, color = Color(0xFF5D3FD3))
    }
}
