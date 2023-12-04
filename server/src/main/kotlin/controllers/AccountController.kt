package controllers

import services.AccountService
import util.DataModels.Account
import util.DataModels.Quiz

// Controller for account operations
class AccountController {
    private val quizController: QuizController = QuizController()
    private val accountService: AccountService = AccountService()

    // Query an account
    fun getAccount(id: String): Account? {
        return accountService.getAccount(id)
    }

    // Query an account using login information
    fun getAccountFromLogin(identifier: String): Account? {
        return accountService.getAccountFromLogin(identifier)
    }

    // Query quizzes associated with an account
    fun getAccountQuizzes(id: String): List<Quiz>? {
        return quizController.getQuizzesByAccountId((id))
    }

    // Create/update an account
    fun upsertAccount(account: Account) {
        accountService.upsertAccount(account)
    }

    // Delete an account
    fun deleteAccount(id: String): Boolean {
        return accountService.deleteAccount(id)
    }
}