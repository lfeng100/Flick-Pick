package ca.uwaterloo.flickpick.data.database.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ActivityItem(
    val type: String,
    val timestamp: String,
    val userID: String,
    val movieID: String,
    val movieTitle: String,
    val rating: Float? = null,
    val message: String? = null
){
    fun formatTimestamp(): String {
        return try {
            val parsed = LocalDateTime.parse(timestamp)
            parsed.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' h:mm a"))
        } catch (e: Exception) {
            timestamp
        }
    }
}