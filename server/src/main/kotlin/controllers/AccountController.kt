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

    // Query quizzes associated with an account
    fun getAccountQuizzes(id: String): List<Quiz>? {
        return quizController.getQuizzesByAccountId((id))
    }

    // Create/update an account
    fun upsertAccount(account: Account) {
        accountService.upsertQuiz(account)
    }

    // Delete an account
    fun deleteAccount(id: String): Boolean {
        return accountService.deleteAccount(id)
    }
}