import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import pages.landing
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        landing()
    }
}
