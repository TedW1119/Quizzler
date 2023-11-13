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
import util.Constants.ACCOUNT_COLLECTION
import util.DataModels.Account
import java.lang.Exception

// Service for the Accounts collection
class AccountService() {

    // Connect to the db, and return the mongo client and collection
    private fun connect(coll: String): Pair<MongoClient, MongoCollection<Account>> {
        val mongoClient = MongoClient.create(MONGO_URI)
        val database = mongoClient.getDatabase(MONGO_DB)
        return Pair(mongoClient, database.getCollection<Account>(coll))
    }

    // Query an account document
    fun getAccount(id: String): Account? {
        val (client, collection) = connect(ACCOUNT_COLLECTION)
        return try {
            runBlocking {
                collection.find(Filters.eq("_id", ObjectId(id))).first()
            }
        } catch (e: Exception) {
            // TODO: log the error
            null
        } finally {
            client.close()
        }
    }

    // Create/update an account document
    fun upsertQuiz(account: Account) {
        val (client, collection) = connect(ACCOUNT_COLLECTION)
        try {
            runBlocking {
                collection.replaceOne(
                    Filters.eq("_id", ObjectId(account._id)),
                    account,
                    ReplaceOptions().upsert(true)
                )
            }
        } catch (e: Exception) {
            // TODO: log the error
        } finally {
            client.close()
        }
    }

    // Delete an account document
    fun deleteAccount(id: String): Boolean {
        val (client, collection) = connect(ACCOUNT_COLLECTION)
        return try {
            runBlocking {
                val response = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
                response.deletedCount > 0
            }
        } catch (e: Exception) {
            // TODO: log the error
            false
        } finally {
            client.close()
        }
    }
}