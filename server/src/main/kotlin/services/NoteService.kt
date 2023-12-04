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
import util.Constants.NOTE_COLLECTION
import util.DataModels.Note

class NoteService {
    // Connect to db and returns the client and collection as a pair
    private fun connect(): Pair<MongoClient, MongoCollection<Note>> {
        val mongoClient = MongoClient.create(MONGO_URI)
        val database = mongoClient.getDatabase(MONGO_DB)

        return Pair(mongoClient, database.getCollection<Note>(NOTE_COLLECTION))
    }

    fun getNote(id: String): Note? {
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

    // Inserts Note object into collection (or updates if existing)
    fun createNote(note: Note) {
        val (client, collection) = connect()

        try {
            runBlocking {
                //collection.insertOne(quiz)
                collection.replaceOne(
                    Filters.eq("_id", ObjectId(note._id)),
                    note,
                    ReplaceOptions().upsert(true)
                )
            }
        } catch (e: Exception) {
            throw e
        } finally {
            client.close()
        }
    }

    // Deletes Note from collection (or does nothing if note DNE)
    fun deleteNote(id: String):Boolean {
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
