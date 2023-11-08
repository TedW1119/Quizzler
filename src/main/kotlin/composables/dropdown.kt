package composables

import androidx.compose.material.*

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun dropdown(
    field: String,
    //items: List<String>,
    updateSelectedItem: (String, String) -> Unit,
    defaultItem: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(defaultItem) }

    // TODO: temporary item list, should change this to a parameter
    val items: List<String> = listOf("Elementary", "Secondary", "Post-Secondary", "Graduate", "Self-Taught")

    // Handle the field being updated
    fun onSelectedItemUpdate(value: String) {
        updateSelectedItem(field, value)
    }

    Column {
        Box(modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true })) {
            Text(selectedItem, modifier = Modifier.padding(16.dp))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    expanded = false
                    onSelectedItemUpdate(selectedItem)
                }) {
                    Text(item)
                }
            }
        }
    }
}
