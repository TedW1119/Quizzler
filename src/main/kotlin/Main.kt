
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
    var data = remember { mutableMapOf<Any, Any>() }

    fun changePage(newPage: String, newData: MutableMap<Any, Any> = mutableMapOf()) {
        currentPage = newPage
        // update data with newData
        data.putAll(newData)
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
            "QuizList" -> quizList(::changePage)
            "QuizTaking" -> quizTaking(::changePage, data)
            "QuizUpload" -> quizUpload(::changePage)
            "AccountSettings" -> accountSettings(::changePage, ObjectId("65487520d9f28b7ce8191478"))
        }
    }
}
