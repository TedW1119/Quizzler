package plugins

import controllers.AccountController
import controllers.QuestionController
import controllers.QuizController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import util.DataModels
import util.DataModels.Quiz


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

// Quiz API                     TODO LOG ERROR ITEMS
fun Application.quizRouting() {
    val quizController = QuizController()
    routing {
        // get quiz
        get("/quiz/{id}") {
            val id = call.parameters["id"]
            if (id != null) {
                try {
                    val quiz = quizController.getQuiz(id)
                    if (quiz != null) {
                        call.respond(message = quiz, status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Quiz not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }

        // create quiz (or update existing quiz)
        post("/quiz") {
            try {
                val quiz = call.receive<Quiz>()
                // store in database
                quizController.createQuiz(quiz)
                call.response.status(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                // Handle ContentTransformationException, which occurs when there's an issue with deserializing the request body
                call.respond(HttpStatusCode.BadRequest, "Invalid request body format")
            } catch (e: Exception) {
                // Handle other exceptions that might occur during the processing of the request
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }

        // delete quiz
        delete("/quiz/{id}") {
            val id = call.parameters["id"]

            if (id != null) {
                try {
                    val isDeleted = quizController.deleteQuiz(id)

                    if (isDeleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Quiz not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }
    }
}

// Question API
fun Application.questionRouting() {
    val questionController = QuestionController()
    routing {
        get("/question/{id}") {
            val id = call.parameters["id"]
            if (id != null) {
                try {
                    val question = questionController.getQuestion(id)
                    if (question != null) {
                        call.respond(message = question, status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Question not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }

        post("/question") {
            try {
                val question = call.receive<DataModels.Question>()
                questionController.createQuestion(question)
                call.response.status(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request body format")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }
    }
}

fun Application.accountRouting() {
    val accountController = AccountController()
    routing {
        get("/account/{id}/quizzes") {
            val id = call.parameters["id"]
            if (id != null) {
                try {
                    val accountQuizzes = accountController.getAccountQuizzes(id)
                    if (!accountQuizzes.isNullOrEmpty()) {
                        call.respond(message = accountQuizzes, status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Account quizzes not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }
    }
}
