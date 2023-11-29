import controllers.AccountController
import controllers.QuizController
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val quizController = QuizController()
    val accountController = AccountController()
    configureSecurity()
    configureSockets()
    configureSerialization()
    configureRouting()
    quizRouting(quizController)
    questionRouting()
    accountRouting(accountController)
    noteRouting()
}
