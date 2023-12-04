package pluginsTest.Routing

import controllers.QuestionController
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
import org.mockito.kotlin.doThrow
import plugins.configureRouting
import plugins.questionRouting
import util.DataModels.Question
import kotlin.test.Test
import kotlin.test.assertEquals

// Unit tests for the HTTP endpoints for /question/. Note that controllers are mocked to isolate behaviour
@OptIn(InternalAPI::class)
class QuestionTest {
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
     * GET QUESTION
     */
    // valid question fetched
    @Test
    fun testValidGetQuestion() {
        val question = Question (
            _id = "12313",
            question = "Test question text",
            type = "MCQ",
            options = mutableListOf("opt1", "opt2"),
            hint = null,
            marks = 1,
            answer = "Test Answer"
        )
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.`when`(mockQuestionController.getQuestion("12313")).thenReturn(question)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.get("/question/12313") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(question, Json.decodeFromString<Question>(response.body()))
        }
    }

    // question not found
    @Test
    fun testMissingGetQuestion() {
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.`when`(mockQuestionController.getQuestion("12313")).thenReturn(null)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.get("/question/12313") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // internal server error
    @Test
    fun testErrorGetQuestion() {
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.`when`(mockQuestionController.getQuestion("12313")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.get("/question/12313") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }


    /**
     * POST QUESTION
     */
    // valid question created
    @Test
    fun testValidPostQuestion() {
        val questionBody = Question (
            _id = "12313",
            question = "Test question text",
            type = "MCQ",
            options = mutableListOf("opt1", "opt2"),
            hint = null,
            marks = 1,
            answer = "Test Answer"
        )
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.doNothing().`when`(mockQuestionController).createQuestion(questionBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.post("/question") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(questionBody)
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    // bad request (invalid body format)
    @Test
    fun testBadReqPostQuestion() {
        val questionBody = Question (
            _id = "12313",
            question = "Test question text",
            type = "MCQ",
            options = mutableListOf("opt1", "opt2"),
            hint = null,
            marks = 1,
            answer = "Test Answer"
        )
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.doNothing().`when`(mockQuestionController).createQuestion(questionBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.post("/question") {
                headers {
                    //append("Content-Type","application/json")
                }
                body = Json.encodeToString(questionBody)
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    // internal server error
    @Test
    fun testInvalidPostQuestion() {
        val questionBody = Question (
            _id = "12313",
            question = "Test question text",
            type = "MCQ",
            options = mutableListOf("opt1", "opt2"),
            hint = null,
            marks = 1,
            answer = "Test Answer"
        )
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        doThrow(RuntimeException("Test Internal server error"))
            .`when`(mockQuestionController)
            .createQuestion(questionBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.post("/question") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(questionBody)
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }

    /**
     * DELETE QUESTION
     */
    // successful delete
    @Test
    fun testValidDeleteQuestion() {
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.`when`(mockQuestionController.deleteQuestion("123456")).thenReturn(true)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.delete("/question/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

    // question not found
    @Test
    fun testMissingDeleteQuestion() {
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.`when`(mockQuestionController.deleteQuestion("123456")).thenReturn(false)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.delete("/question/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // internal server error
    @Test
    fun testInvalidDeleteQuestion() {
        val mockQuestionController = Mockito.mock(QuestionController::class.java)
        Mockito.`when`(mockQuestionController.deleteQuestion("123456")).thenThrow(java.lang.RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                questionRouting(mockQuestionController)
            }

            val response = client.delete("/question/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }
}
