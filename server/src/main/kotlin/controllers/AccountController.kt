package controllers

import util.DataModels.Quiz

class AccountController {
    private val quizController: QuizController = QuizController()

    fun getAccountQuizzes(id: String): List<Quiz>? {
        return quizController.getQuizzesByAccountId((id))
    }
}