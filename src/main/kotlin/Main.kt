import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import pages.login

fun main() = application {
    Window(
        title = "APP NAME HERE",
        onCloseRequest = ::exitApplication
    ) {
        login()
    }
}
