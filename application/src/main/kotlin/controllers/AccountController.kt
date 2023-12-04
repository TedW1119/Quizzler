package controllers

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.Constants
import utils.DataModels.Account
import utils.DataModels.Quiz
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

// Controller for account operations
class AccountController {

    // Query an account
    fun getAccount(accountId: String): Account {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${Constants.BASE_URL}/account/${accountId}"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return Json.decodeFromString<Account>(response.body())
    }

    // Query an account using login information
    fun getAccountFromLogin(identifier: String): Account? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${Constants.BASE_URL}/account/${identifier}/login"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return try {
            Json.decodeFromString<Account>(response.body())
        } catch (e: Exception) {
            println(e)
            null
        }
    }

    fun getAccountQuizzes(id: String): List<Quiz> {
        val client = HttpClient.newBuilder().build()

        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("${Constants.BASE_URL}/account/${id}/quizzes"))
                .GET()
                .build()
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            return if (response.statusCode() in 200..299) {
                Json.decodeFromString<List<Quiz>>(response.body())
            } else {
                println("HTTP request failed with status code ${response.statusCode()}")
                emptyList()
            }
        } catch (e:Exception) {
            println(e)
            return emptyList()
        }
    }

    // Create/update an account
    fun upsertAccount(account: Account) {
        val payload = Json.encodeToString(account)
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${Constants.BASE_URL}/account"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(payload))
            .build()
        client.send(request, HttpResponse.BodyHandlers.ofString())
    }
}