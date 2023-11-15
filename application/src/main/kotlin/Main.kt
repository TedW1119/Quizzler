
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
import utils.DataModels.Account

fun main() = application {

    // Tracks the current page being rendered
    var currentPage by remember { mutableStateOf("Login" )}

    // Tracks the data stored across the application (similar to a "store")
    //  data[account]: data for the currently-logged-in account
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
            "Landing" -> landing(::changePage, data["accountId"] as String)
            "QuizCreation" -> quizCreation(::changePage, data["accountId"] as String)
            "QuizList" -> quizList(::changePage, data["accountId"] as String)
            "QuizTaking" -> quizTaking(::changePage, data)
            "QuizUpload" -> quizUpload(::changePage)
            "AccountSettings" -> accountSettings(::changePage, data["accountId"] as String)
        }
    }
}
