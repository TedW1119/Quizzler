package composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun alertDialog(
    setShowDialog: (Boolean) -> Unit,
    title: String,
    content: String,
    primaryButtonText: String,
    handlePrimaryButton: () -> Unit,
    secondaryButtonText: String? = null,
    handleSecondaryButton: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        title = { Text(title) },
        text = { Text(content) },
        buttons = {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp, bottom = 8.dp, top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (secondaryButtonText != null) {
                    OutlinedButton(
                        onClick = handleSecondaryButton,
                    ) {
                        Text(secondaryButtonText)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Button(
                    onClick = handlePrimaryButton,
                ) {
                    Text(primaryButtonText)
                }
            }
        }
    )
}