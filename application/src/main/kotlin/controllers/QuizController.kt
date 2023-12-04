package controllers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.Constants
import utils.DataModels.Quiz
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class QuizController {
    // Create/update a quiz
    fun generateQuiz(quiz: Quiz) {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/quiz/generate"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(Json.encodeToString(quiz)))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() in 200..299) {
                println("Successfully created quiz")
            } else {
                println("HTTP request failed with status code: ${response.statusCode()}")
            }
        } catch (e: Exception) {
            // log error
            println(e)
        }
    }

    fun updateQuiz(quiz: Quiz): String? {
        val quizSerialized = Json.encodeToString(quiz)
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/quiz"))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(quizSerialized))
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
            return if (response.statusCode() in 200..299) {
                println("Successfully created quiz")
                response.body()
            } else {
                println("HTTP request failed with status code: ${response.statusCode()}")
                null
            }
        } catch (e: Exception) {
            // log error
            println(e)
            return null
        }
    }

    // Delete a quiz (returns true if deleted successfully, false if not)
    fun deleteQuiz(id: String): Boolean {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/quiz/$id"))
                .DELETE()
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            // Check if the response status code is successful (2xx)
            return if (response.statusCode() in 200..299) {
                println("Successfully deleted quiz")
                true
            } else {
                // Handle non-successful status codes if needed
                println("HTTP request failed with status code: ${response.statusCode()}")
                false
            }
        } catch (e: Exception) {
            // Handle the exception, log it, or perform any necessary error handling
            println(e)
            return false
        }
    }
}