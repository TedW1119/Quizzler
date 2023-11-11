package util

import kotlinx.serialization.Serializable
import org.bson.BsonType
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonRepresentation

object DataModels {
    @Serializable
    data class Settings(
        val hint: Boolean,
        val bonus: Boolean,
        val time: Int
    )

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
        val settings: Settings,
        val totalMarks: Int
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
    data class Account(
        @BsonId
        @BsonRepresentation(BsonType.OBJECT_ID)
        val _id: String,
        val name: String,
        val username: String,
        val email: String,
        val password: String,
        val educationLevel: String,
        val profilePicId: String
    )
}
