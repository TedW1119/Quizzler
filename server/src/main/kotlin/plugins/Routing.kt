package plugins

import controllers.AccountController
import controllers.NoteController
import controllers.QuestionController
import controllers.QuizController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import util.DataModels
import util.DataModels.Account
import util.DataModels.Quiz
import util.DataModels.Note


fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
    }
}

// Account API
fun Application.accountRouting(accountController: AccountController) {
    //val accountController = AccountController()
    routing {

        // Get an account
        get("/account/{id}") {
            val id = call.parameters["id"]
            if (id != null) {
                try {
                    val account = accountController.getAccount(id)
                    if (account != null) {
                        call.respond(message = account, status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Account not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }

        // Get an account using login information
        get("/account/{identifier}/login") {
            val identifier = call.parameters["identifier"]
            if (identifier != null) {
                try {
                    val account = accountController.getAccountFromLogin(identifier)
                    if (account != null) {
                        call.respond(message = account, status = HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Account not found")
                    }
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }

        // Get quizzes associated with an account
        get("/account/{id}/quizzes") {
            val id = call.parameters["id"]
            if (id != null) {
                try {
                    val accountQuizzes = accountController.getAccountQuizzes(id) ?: emptyList()
                    call.respond(message = accountQuizzes, status = HttpStatusCode.OK)
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }

        // Create/update an account
        post("/account") {
            try {
                val account = call.receive<Account>()
                print(account)
                accountController.upsertAccount(account)
                call.response.status(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                // Handle ContentTransformationException, which occurs when there's an issue with deserializing the request body
                call.respond(HttpStatusCode.BadRequest, "Invalid request body format")
            } catch (e: Exception) {
                // Handle other exceptions that might occur during the processing of the request
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }

        // Delete an account
        delete("/account/{id}") {
            val id = call.parameters["id"]
            if (id != null) {
                try {
                    val isDeleted = accountController.deleteAccount(id)
                    if (isDeleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Account not found")
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

// Quiz API
fun Application.quizRouting(quizController: QuizController) {
//    val quizController = QuizController()
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

        // generate quiz (or update existing quiz)
        post("/quiz/generate") {
            try {
                val quiz = call.receive<Quiz>()
                // generate quiz contents
                quizController.generateQuiz(quiz)
                call.response.status(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                // Handle ContentTransformationException, which occurs when there's an issue with deserializing the request body
                call.respond(HttpStatusCode.BadRequest, "Invalid request body format")
            } catch (e: Exception) {
                // Handle other exceptions that might occur during the processing of the request
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
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
fun Application.questionRouting(questionController: QuestionController) {
    //val questionController = QuestionController()
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

        delete("/question/{id}") {
            val id = call.parameters["id"]

            if (id != null) {
                try {
                    val isDeleted = questionController.deleteQuestion(id)

                    if (isDeleted) {
                        call.respond(HttpStatusCode.NoContent)
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
    }
}

// Note API
fun Application.noteRouting(noteController: NoteController) {
    //val noteController = NoteController()
    routing {
        // create note (or update existing note)
        post("/note") {
            try {
                val note = call.receive<Note>()
                // store in database
                noteController.createNote(note)
                call.response.status(HttpStatusCode.Created)
            } catch (e: ContentTransformationException) {
                // Handle ContentTransformationException, which occurs when there's an issue with deserializing the request body
                call.respond(HttpStatusCode.BadRequest, "Invalid request body format")
            } catch (e: Exception) {
                // Handle other exceptions that might occur during the processing of the request
                call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
            }
        }

        // delete note
        delete("/note/{id}") {
            val id = call.parameters["id"]

            if (id != null) {
                try {
                    val isDeleted = noteController.deleteNote(id)

                    if (isDeleted) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Note not found")
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
