package utils

import kotlinx.serialization.Serializable

object DataModels {
    @Serializable
    data class Settings(
        val hint: Boolean,
        val bonus: Boolean,
        val time: Int
    )

    @Serializable
    data class Quiz(
        val id: String,
        val accountId: Int,
        val questionIds: List<String>,
        val name: String,
        val subject: String,
        val difficulty: String,
        val settings: Settings,
        val totalMarks: Int
    )

    @Serializable
    data class Question(
        val id: String,
        val question: String,
        val type: String,
        val options: List<String>,
        val hint: String?,
        val marks: Int,
        val answer: String
    )

    @Serializable
    data class Account(
        val id: String,
        val name: String,
        val username: String,
        val email: String,
        val password: String,
        val educationLevel: String,
        val profilePicId: String
    )
}
