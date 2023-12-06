package pluginsTest.routing

import controllers.NoteController
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
import plugins.noteRouting
import util.DataModels.Note
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(InternalAPI::class)
class NoteTest {
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
     * POST NOTE
     */
    // valid note created
    @Test
    fun testValidPostNote() {
        val noteBody = Note(
            _id = "15551",
            text = "Test course notes"
        )
        val mockNoteController = Mockito.mock(NoteController::class.java)
        Mockito.doNothing().`when`(mockNoteController).createNote(noteBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                noteRouting(mockNoteController)
            }

            val response = client.post("/note") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(noteBody)
            }
            assertEquals(HttpStatusCode.Created, response.status)
        }
    }

    // bad request
    @Test
    fun testBadReqPostNote() {
        val noteBody = Note(
            _id = "15551",
            text = "Test course notes"
        )
        val mockNoteController = Mockito.mock(NoteController::class.java)
        Mockito.doNothing().`when`(mockNoteController).createNote(noteBody)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                noteRouting(mockNoteController)
            }

            val response = client.post("/note") {
                headers {
//                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(noteBody)
            }
            assertEquals(HttpStatusCode.BadRequest, response.status)
        }
    }

    // internal server error
    @Test
    fun testInvalidPostNote() {
        val noteBody = Note(
            _id = "15551",
            text = "Test course notes"
        )
        val mockNoteController = Mockito.mock(NoteController::class.java)
        doThrow(RuntimeException("Test Invalid request body format"))
            .`when`(mockNoteController)
            .createNote(noteBody)


        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                noteRouting(mockNoteController)
            }

            val response = client.post("/note") {
                headers {
                    append("Content-Type","application/json")
                }
                body = Json.encodeToString(noteBody)
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }

    /**
     * DELETE NOTE
     */
    // successful delete
    @Test
    fun testValidDeleteNote() {
        val mockNoteController = Mockito.mock(NoteController::class.java)
        Mockito.`when`(mockNoteController.deleteNote("123456")).thenReturn(true)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                noteRouting(mockNoteController)
            }

            val response = client.delete("/note/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NoContent, response.status)
        }
    }

    // account not found
    @Test
    fun testMissingDeleteNote() {
        val mockNoteController = Mockito.mock(NoteController::class.java)
        Mockito.`when`(mockNoteController.deleteNote("123456")).thenReturn(false)

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                noteRouting(mockNoteController)
            }

            val response = client.delete("/note/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.NotFound, response.status)
        }
    }

    // internal server error
    @Test
    fun testInvalidDeleteNote() {
        val mockNoteController = Mockito.mock(NoteController::class.java)
        Mockito.`when`(mockNoteController.deleteNote("123456")).thenThrow(java.lang.RuntimeException("Test internal server error"))

        testApplication {
            application {
                install(ContentNegotiation) {
                    json()
                }
                noteRouting(mockNoteController)
            }

            val response = client.delete("/note/123456") {
                headers {
                    append("Accept", "application/json")
                }
            }
            assertEquals(HttpStatusCode.InternalServerError, response.status)
        }
    }
}
