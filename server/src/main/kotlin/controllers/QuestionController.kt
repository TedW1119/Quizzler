package controllers

import services.QuestionService
import util.DataModels.Question

class QuestionController {
    private val questionService: QuestionService = QuestionService()

    fun createQuestion(question: Question) {
        questionService.createQuestion(question)
    }

    fun getQuestion(id: String): Question? {
        return questionService.getQuestion(id)
    }
}