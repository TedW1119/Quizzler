package pluginsTest.routing

import controllers.QuizController
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.doThrow
import plugins.configureRouting
import plugins.quizRouting
import util.DataModels.Quiz
import kotlin.test.Test
import kotlin.test.assertEquals

// Unit tests for the HTTP endpoints for /quiz/. Note that controllers are mocked to isolate behaviour
@OptIn(InternalAPI::class)
class QuizTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    /**
     * GET QUIZ
     */
    // valid quiz fetched
    @Test
    fun testValidGetQuiz() {
        val quiz = Quiz(
            _id = "654321",
            accountId = "987654",
            questionIds = mutableListOf("2222", "1111", "3333"),
            name = "Test Quiz",
            subject = "Test Subject",
            difficulty = "Easy",
            questionType = "MCQ",
            totalQuestions = 2,
            totalMarks = 2.0,
            noteId = "1143"
        )
        val mockQuizController = Mockito.mock(QuizController::class.java)
        `when`(mockQuizController.getQuiz("654321")).thenReturn(quiz)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.get("/quiz/654321") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(quiz, Json.decodeFromString<Quiz>(response.body()))
        }
    }

    // quiz not found
    @Test
    fun testMissingGetQuiz() {
        val mockQuizController = Mockito.mock(QuizController::class.java)
        `when`(mockQuizController.getQuiz("654321")).thenReturn(null)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.get("/quiz/654321") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // internal server error
    @Test
    fun testErrorGetQuiz() {
        val mockQuizController = Mockito.mock(QuizController::class.java)
        `when`(mockQuizController.getQuiz("654321")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.get("/quiz/654321") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }

    /**
     * POST QUIZ
     */
    // valid quiz created
    @Test
    fun testValidPostQuiz() {
        val quizBody = Quiz(
            _id = "654321",
            accountId = "987654",
            questionIds = mutableListOf("2222", "1111", "3333"),
            name = "Test Quiz",
            subject = "Test Subject",
            difficulty = "Easy",
            questionType = "MCQ",
            totalQuestions = 2,
            totalMarks = 2.0,
            noteId = "1143"
        )
        val mockQuizController = Mockito.mock(QuizController::class.java)
        Mockito.doNothing().`when`(mockQuizController).createQuiz(quizBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.post("/quiz") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(quizBody)
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    // bad request
    @Test
    fun testBadRequestPostQuiz() {
        val quizBody = Quiz(
            _id = "654321",
            accountId = "987654",
            questionIds = mutableListOf("2222", "1111", "3333"),
            name = "Test Quiz",
            subject = "Test Subject",
            difficulty = "Easy",
            questionType = "MCQ",
            totalQuestions = 2,
            totalMarks = 2.0,
            noteId = "1143"
        )
        val mockQuizController = Mockito.mock(QuizController::class.java)
        Mockito.doNothing().`when`(mockQuizController).createQuiz(quizBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.post("/quiz") {
                headers {
//                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(quizBody)
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    // internal server error
    @Test
    fun testErrorPostQuiz() {
        val quizBody = Quiz(
            _id = "654321",
            accountId = "987654",
            questionIds = mutableListOf("2222", "1111", "3333"),
            name = "Test Quiz",
            subject = "Test Subject",
            difficulty = "Easy",
            questionType = "MCQ",
            totalQuestions = 2,
            totalMarks = 2.0,
            noteId = "1143"
        )
        val mockQuizController = Mockito.mock(QuizController::class.java)
        doThrow(RuntimeException("Test Internal server error"))
            .`when`(mockQuizController)
            .createQuiz(quizBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.post("/quiz") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(quizBody)
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }

    /**
     * DELETE QUIZ
     */
    // successful delete
    @Test
    fun testValidDeleteQuiz() {
        val mockQuizController = Mockito.mock(QuizController::class.java)
        `when`(mockQuizController.deleteQuiz("123456")).thenReturn(true)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.delete("/quiz/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

    // quiz not found
    @Test
    fun testNotFoundDeleteQuiz() {
        val mockQuizController = Mockito.mock(QuizController::class.java)
        `when`(mockQuizController.deleteQuiz("123456")).thenReturn(false)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.delete("/quiz/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // internal server error
    @Test
    fun testErrorDeleteQuiz() {
        val mockQuizController = Mockito.mock(QuizController::class.java)
        `when`(mockQuizController.deleteQuiz("123456")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                quizRouting(mockQuizController)
            }

            val response = client.delete("/quiz/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }

}