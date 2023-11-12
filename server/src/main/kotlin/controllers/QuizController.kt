package controllers

import services.QuizService
import util.DataModels.Quiz

class QuizController {
    private val quizService: QuizService = QuizService()

    fun createQuiz(quiz: Quiz) {
        quizService.createQuiz(quiz)
    }

    fun getQuiz(id: String): Quiz? {
        return quizService.getQuiz(id)
    }

    fun getQuizzesByAccountId(accountId: String): List<Quiz>? {
        return quizService.getQuizzesByAccountId((accountId))
    }


    fun deleteQuiz(id: String): Boolean {
        return quizService.deleteQuiz(id)
    }
}