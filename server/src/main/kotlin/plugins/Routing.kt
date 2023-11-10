package plugins

import controllers.QuizController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import util.DataModels.Quiz



fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

// Quiz API
fun Application.quizRouting() {
    var quizController = QuizController()
    routing {
        // get quiz
        get("/quiz/{id}") {
            val id = call.parameters["id"]
            quizController.test()
            call.respondText(id.toString(), status = HttpStatusCode.OK)
        }

        // create quiz
        post("/quiz") {
            try {
                val quiz = call.receive<Quiz>()
                // store in database
                quizController.test()
                call.response.status(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                // Handle ContentTransformationException, which occurs when there's an issue with deserializing the request body
                call.respond(HttpStatusCode.BadRequest, "Invalid request body format")
            } catch (e: Exception) {
                // Handle other exceptions that might occur during the processing of the request
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }

        // update quiz

        // delete quiz
        delete(path="/quiz") {
            println("BBB")
        }
    }
}
