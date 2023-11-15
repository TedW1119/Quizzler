package controllers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.Constants
import utils.DataModels
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class QuizController {
    // Query a quiz
    fun getQuiz(quizId: String): DataModels.Quiz? {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/quiz/${quizId}"))
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            return if (response.statusCode() in 200..299) {
                Json.decodeFromString<DataModels.Quiz>(response.body())
            } else {
                println("HTTP request failed with status code ${response.statusCode()}")
                null
            }
        } catch (e:Exception) {
            println(e)
            return null
        }
    }


    // Create/update a quiz
    fun upsertQuiz(quiz: DataModels.Quiz) {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/quiz"))
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