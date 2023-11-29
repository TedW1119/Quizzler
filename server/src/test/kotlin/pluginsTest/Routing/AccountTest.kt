package pluginsTest.Routing

import controllers.AccountController
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
import org.mockito.Mockito.*
import org.mockito.kotlin.doThrow
import plugins.accountRouting
import plugins.configureRouting
import util.DataModels.Quiz
import util.DataModels.Account
import java.lang.RuntimeException
import kotlin.test.Test
import kotlin.test.assertEquals

// Unit tests for the HTTP endpoints for /account/. Note that controllers are mocked to isolate behaviour
@OptIn(InternalAPI::class)
class AccountTest {
    // sample test
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
    * GET ACCOUNT
    */
    // valid account fetched
    @Test
    fun testValidGetAccount() {
        val account = Account(
            _id="123456",
            name = "Mocked User",
            username = "muser",
            email = "muser@outlook.com",
            password = "pwd",
            educationLevel = "University"
        )
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccount("123456")).thenReturn(account)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(account, Json.decodeFromString<Account>(response.body()))
        }
    }

    // account not found
    @Test
    fun testMissingAccountGetAccount() {
        val account = null
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccount("123456")).thenReturn(account)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // internal server error
    @Test
    fun testErrorAccountGetAccount() {
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccount("123456")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }


    /**
     * GET account with identifier
     */
    // valid account ID fetched
    @Test
    fun testValidGetAccountId() {
        val account = Account(
            _id="123456",
            name = "Mocked User",
            username = "muser",
            email = "muser@outlook.com",
            password = "pwd",
            educationLevel = "University"
        )
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccountFromLogin("123456")).thenReturn(account)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456/login") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(account, Json.decodeFromString<Account>(response.body()))
        }
    }

    // account ID not found
    @Test
    fun testMissingGetAccountId() {
        val account = null
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccountFromLogin("123456")).thenReturn(account)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456/login") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // Internal server error
    @Test
    fun testErrorGetAccountId() {
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccountFromLogin("123456")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456/login") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }


    /**
     * GET quiz with account
     */
    // valid account ID fetched
    @Test
    fun testValidGetQuizByAccountId() {
        val quiz1 = Quiz(
            _id = "654321",
            accountId = "12345",
            questionIds = mutableListOf("2222", "1111", "3333"),
            name = "q1test",
            subject = "tsub1",
            difficulty = "Easy",
            questionType = "MCQ",
            totalQuestions = 2,
            totalMarks = 2.0,
            hint = false,
            time = 600,
            noteId = "1143"
        )

        val quiz2 = Quiz(
            _id = "9999",
            accountId = "12345",
            questionIds = mutableListOf("1234", "5678", "9876"),
            name = "q2test",
            subject = "tsub2",
            difficulty = "Medium",
            questionType = "T/F",
            totalQuestions = 5,
            totalMarks = 5.0,
            hint = false,
            time = 600,
            noteId = "1222"
        )

        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccountQuizzes("12345")).thenReturn(listOf(quiz1, quiz2))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/12345/quizzes") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.OK, response.status)
            assertEquals(listOf(quiz1, quiz2), Json.decodeFromString<List<Quiz>>(response.body()))
        }
    }

    // Internal server error
    @Test
    fun testErrorGetQuizByAccountId() {
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.getAccountQuizzes("123456")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.get("/account/123456/quizzes") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }


    /**
     * POST account
     */
    // valid account created
    @Test
    fun testValidPostAccount() {
        val accountBody = Account(
            _id="123456",
            name = "Mocked User",
            username = "muser",
            email = "muser@outlook.com",
            password = "pwd",
            educationLevel = "University"
        )
        val mockAccountController = mock(AccountController::class.java)
        doNothing().`when`(mockAccountController).upsertAccount(accountBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.post("/account") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(accountBody)
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    // Bad request error
    @Test
    fun testBadReqPostAccount() {
        val accountBody = Account(
            _id="123456",
            name = "Mocked User",
            username = "muser",
            email = "muser@outlook.com",
            password = "pwd",
            educationLevel = "University"
        )
        val mockAccountController = mock(AccountController::class.java)
        doNothing().`when`(mockAccountController).upsertAccount(accountBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.post("/account") {
                headers {
//                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(accountBody)
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    // internal server error
    @Test
    fun testInvalidPostAccount() {
        val accountBody = Account(
            _id="123456",
            name = "Mocked User",
            username = "muser",
            email = "muser@outlook.com",
            password = "pwd",
            educationLevel = "University"
        )
        val mockAccountController = mock(AccountController::class.java)
        doThrow(RuntimeException("Test Invalid request body format"))
            .`when`(mockAccountController)
            .upsertAccount(accountBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.post("/account") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(accountBody)
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }


    /**
     * DELETE account
     */
    // Successful delete
    @Test
    fun testValidDeleteAccount() {
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.deleteAccount("123456")).thenReturn(true)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.delete("/account/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

    // Account not found
    @Test
    fun testMissingDeleteAccount() {
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.deleteAccount("123456")).thenReturn(false)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.delete("/account/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // Internal server error
    @Test
    fun testErrorDeleteAccount() {
        val mockAccountController = mock(AccountController::class.java)
        `when`(mockAccountController.deleteAccount("123456")).thenThrow(RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                accountRouting(mockAccountController)
            }

            val response = client.delete("/account/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }

}