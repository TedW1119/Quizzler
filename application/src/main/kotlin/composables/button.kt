package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun button(text: String, filled: Boolean, action: () -> Unit, mod: Modifier) {
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

@Composable
fun buttonWithIndicator(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) MaterialTheme.colors.primary else Color.Gray
        ),
        modifier = Modifier.height(50.dp)
    ) {
        Text(text = text, color = Color.White)
    }
}
