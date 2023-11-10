//package net.codebot.models

//import kotlinx.serialization.Serializable

//@Serializable
data class Quiz (
    val id: Int,
    val accountId: Int,
    val questionIds: List<Int>,
    val name: String,
    val subject: String,
    val difficulty: String,
    val settings: Settings,
    val totalMarks: Int
)