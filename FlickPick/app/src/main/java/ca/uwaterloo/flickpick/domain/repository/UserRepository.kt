package ca.uwaterloo.flickpick.domain.repository

import android.util.Log
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.data.database.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

object UserRepository {
    private val userCache : MutableMap<String, User> = ConcurrentHashMap()

    suspend fun getUserForId(userID: String): User? {
        if (userCache[userID] != null) {
            Log.i("Users", "Cache hit for user with id $userID")
            return userCache[userID]
        }
        Log.i("MovieCatalog", "Fetching movie with id $userID from backend")
        return withContext(Dispatchers.IO) {
            try {
                val user = DatabaseClient.apiService.getUserById(userID)
                userCache[userID] = user
                user
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching user with id $userID: ${e.message}")
                null
            }
        }
    }
}