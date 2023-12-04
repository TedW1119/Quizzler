package services

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import util.Constants.ACCOUNT_COLLECTION
import util.Constants.MONGO_DB
import util.Constants.MONGO_URI
import util.DataModels.Account

// Service for the Accounts collection
class AccountService {

    // Connect to the db, and return the mongo client and collection
    private fun connect(): Pair<MongoClient, MongoCollection<Account>> {
        val mongoClient = MongoClient.create(MONGO_URI)
        val database = mongoClient.getDatabase(MONGO_DB)
        return Pair(mongoClient, database.getCollection<Account>(ACCOUNT_COLLECTION))
    }

    // Query an account document
    fun getAccount(id: String): Account? {
        val (client, collection) = connect()
        return try {
            runBlocking {
                collection.find(Filters.eq("_id", ObjectId(id))).first()
            }
        } catch (e: Exception) {
            println(e)
            null
        } finally {
            client.close()
        }
    }

    // Query an account document using login information
    fun getAccountFromLogin(identifier: String): Account? {
        val (client, collection) = connect()
        return try {
            runBlocking {
                collection.find(Filters.or(
                        Filters.eq("username", identifier),
                        Filters.eq("email", identifier)
                    )
                ).first()
            }
        } catch (e: Exception) {
            println(e)
            null
        } finally {
            client.close()
        }
    }

    // Create/update an account document
    fun upsertAccount(account: Account) {
        val (client, collection) = connect()
        try {
            runBlocking {
                collection.replaceOne(
                    Filters.eq("_id", ObjectId(account._id)),
                    account,
                    ReplaceOptions().upsert(true)
                )
            }
        } catch (e: Exception) {
            println(e)
        } finally {
            client.close()
        }
    }

    // Delete an account document
    fun deleteAccount(id: String): Boolean {
        val (client, collection) = connect()
        return try {
            runBlocking {
                val response = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
                response.deletedCount > 0
            }
        } catch (e: Exception) {
            println(e)
            false
        } finally {
            client.close()
        }
    }
}