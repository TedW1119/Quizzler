package services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import util.Constants.MONGO_DB
import util.Constants.MONGO_URI
import util.Constants.QUIZ_COLLECTION
import util.DataModels.Quiz

class QuizService {
    // Connect to db and returns the client and collection as a pair
    private fun connect(): Pair<MongoClient, MongoCollection<Quiz>> {
        val mongoClient = MongoClient.create(MONGO_URI)
        val database = mongoClient.getDatabase(MONGO_DB)

        return Pair(mongoClient, database.getCollection<Quiz>(QUIZ_COLLECTION))
    }


    // Inserts Quiz object into collection (or updates if existing)
    fun createQuiz(quiz: Quiz) {
        val (client, collection) = connect()

        try {
            runBlocking {
                //collection.insertOne(quiz)
                collection.replaceOne(
                    Filters.eq("_id", ObjectId(quiz._id)),
                    quiz,
                    ReplaceOptions().upsert(true)
                )
            }
        } catch (e: Exception) {
            throw e
        } finally {
            client.close()
        }
    }

    // Searches quiz collection for quiz with specified id, or returns null if not found
    fun getQuiz(id: String): Quiz? {
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

    fun getQuizzesByAccountId(accountId: String): List<Quiz>? {
        val (client, collection) = connect()

        return try {
            runBlocking {
                collection.find(Filters.eq("accountId", accountId)).toList()
            }
        } catch (e: Exception) {
            throw e
        } finally {
            client.close()
        }
    }

    // Deletes quiz based on ID and returns true if successful
    fun deleteQuiz(id: String): Boolean {
        val (client, collection) = connect()

        return try {
            runBlocking {
                val deleteRes = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
                // >0 means successful delete, 0 means not found
                deleteRes.deletedCount > 0
            }
        } catch (e: Exception) {
            println(e)
            false
        } finally {
                client.close()
        }
    }
}