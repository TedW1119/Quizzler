package utils

import kotlinx.serialization.Serializable
import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation

object DataModels {
    @Serializable
    data class Quiz(
        @BsonId
        @BsonRepresentation(BsonType.OBJECT_ID)
        val _id: String,
        val accountId: String,
        val questionIds: List<String>,
        val name: String,
        val subject: String,
        val difficulty: String,
        val questionType: String,
        val totalQuestions: Int,
        val totalMarks: Double,
        val hint: Boolean,
        val time: Int,
        val noteId: String
    )

    @Serializable
    data class Question(
        @BsonId
        @BsonRepresentation(BsonType.OBJECT_ID)
        val _id: String,
        val question: String,
        val type: String,
        val options: List<String>,
        val hint: String?,
        val marks: Int,
        val answer: String
    )

    @Serializable
    data class Note(
        @BsonId
        @BsonRepresentation(BsonType.OBJECT_ID)
        val _id: String,
        val text: String
    )

    @Serializable
    data class Account(
        @BsonId
        @BsonRepresentation(BsonType.OBJECT_ID)
        val _id: String,
        val name: String,
        val username: String,
        val email: String,
        val password: String,
        val educationLevel: String
    )

    // Store the form data for an Account
    class AccountFormData(
        var name: String = "",
        var username: String = "",
        var email: String = "",
        var password: String = "",
        var confirmPassword: String = "",
        var educationLevel: String = ""
    )
}
