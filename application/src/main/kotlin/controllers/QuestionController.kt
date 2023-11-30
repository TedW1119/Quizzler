package controllers

import kotlinx.serialization.json.Json
import utils.Constants
import utils.DataModels.Question
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class QuestionController {
    fun getQuestion(questionId: String): Question? {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/question/${questionId}"))
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            return if (response.statusCode() in 200..299) {
                Json.decodeFromString<Question>(response.body())
            } else {
                println("HTTP request failed with status code ${response.statusCode()}")
                null
            }
        } catch (e:Exception) {
            println(e)
            return null
        }
    }
}