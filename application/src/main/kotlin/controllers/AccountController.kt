package controllers

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
    fun getAccount(accountId: String): Account? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${Constants.BASE_URL}/account/${accountId}"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return Json.decodeFromString<Account>(response.body())
    }

    // Query quizzes associated with an account
    // TODO: implement this
    fun getAccountQuizzes(id: String): List<Quiz>? {
        return null
    }

    // Create/update an account
    fun upsertAccount(account: Account) {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("${Constants.BASE_URL}/account"))
            .POST(HttpRequest.BodyPublishers.ofString(account.toString()))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    // Delete an account
    // TODO: implement this
    fun deleteAccount(id: String): Boolean {
        return true
    }
}