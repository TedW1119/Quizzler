package controllers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.Constants
import utils.DataModels.Note
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class NoteController {
    // Query a Note

    // Create/update a note
    fun upsertNote(note: Note) {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/note"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(note)))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() in 200..299) {
                println("Successfully created note")
            } else {
                println("HTTP request failed with status code: ${response.statusCode()}")
            }
        } catch (e: Exception) {
            // log error
            println(e)
        }
    }

    // Delete a Note
    /// TODO implement this, important when wanting to delete a quiz
}