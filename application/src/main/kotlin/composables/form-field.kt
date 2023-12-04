package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// ----------------------------------------------------------------------------
// UI composable for a form text input field
// ----------------------------------------------------------------------------
@Composable
@Preview
fun formField(
    field: String,
    defaultLabel: Boolean,
    updateField: (String, String) -> Unit,
    defaultValue: String = ""
) {
    var fieldValue by remember { mutableStateOf(defaultValue) }

    // Handle the field being updated
    fun onFieldUpdate(value: String) {
        updateField(field, value)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        // Default Appearance
        if (defaultLabel) {
            OutlinedTextField(
                label = { Text(field) },
                value = fieldValue,
                onValueChange = { input ->
                    fieldValue = input
                    onFieldUpdate(fieldValue)
                }
            )

            // Label beside the text input
        } else {
            Text(
                text = field
            )
            OutlinedTextField(
                value = fieldValue,
                onValueChange = { input ->
                    fieldValue = input
                    onFieldUpdate(fieldValue)
                }
            )
        }

    }
}