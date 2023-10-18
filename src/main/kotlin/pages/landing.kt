package pages

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import composables.button

@Composable
@Preview
fun landing() {
    val input = "test input"
    fun handleSubmit() {
        println(input)
    }
    button("Test button", true, ::handleSubmit)
}