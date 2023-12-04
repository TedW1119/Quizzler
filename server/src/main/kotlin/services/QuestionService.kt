package services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import util.Constants.MONGO_DB
import util.Constants.MONGO_URI
import util.Constants.QUESTION_COLLECTION
import util.DataModels.Question

class QuestionService() {
    private fun connect(): Pair<MongoClient, MongoCollection<Question>> {
        val mongoClient = MongoClient.create(MONGO_URI)
        val database = mongoClient.getDatabase(MONGO_DB)

        return Pair(mongoClient, database.getCollection<Question>(QUESTION_COLLECTION))
    }


    fun createQuestion(question: Question) {
        val (client, collection) = connect()

        try {
            runBlocking {
                collection.replaceOne(
                    Filters.eq("_id", ObjectId(question._id)),
                    question,
                    ReplaceOptions().upsert(true)
                )
            }
        } catch (e: Exception) {
            throw e
        } finally {
            client.close()
        }
    }

    fun getQuestion(id: String): Question? {
        val (client, collection) = connect()

        return try {
            runBlocking {
                collection.find(Filters.eq("_id", ObjectId(id))).first()
            }
        } catch (e: Exception) {
            throw e
        } finally {
            client.close()
        }
    }

    fun deleteQuestion(id: String): Boolean {
        val (client, collection) = connect()

        return try {
            runBlocking {
                val response = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
                response.deletedCount > 0
            }
        } catch (e: Exception) {
            false
        } finally {
            client.close()
        }
    }
}
