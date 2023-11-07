import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.bson.types.ObjectId
import pages.*
import pages.account.accountCreation
import pages.account.accountSettings

fun main() = application {
    var currentPage by remember { mutableStateOf("Login" )}

    fun changePage(newPage: String) {
        currentPage = newPage
    }

    Window(
        title = "Abnormally Distributed",
        onCloseRequest = ::exitApplication
    ) {
        when (currentPage) {
            "Login" -> login(::changePage)
            "AccountCreation" -> accountCreation(::changePage)
            "Landing" -> landing(::changePage)
            "QuizCreation" -> quizCreation(::changePage)
            "QuizTaking" -> quizMCQ(::changePage)
            "QuizUpload" -> quizUpload(::changePage)
            "AccountSettings" -> accountSettings(::changePage, ObjectId("65487520d9f28b7ce8191478"))
        }
    }
}
