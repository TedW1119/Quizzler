package controllers

import services.QuizService

class QuizController {
    val quizService: QuizService = QuizService()


    fun test() {
        println("TEST CONTROLLER")
        quizService.test()
    }
}