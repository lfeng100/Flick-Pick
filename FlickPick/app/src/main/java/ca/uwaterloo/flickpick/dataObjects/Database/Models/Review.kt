package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JsonClass(generateAdapter = true)
data class Review(
    val reviewID: String,
    val rating: Double,
    val message: String?,
    val timestamp: String,
    val userID: String,
    val movieID: String
) {
    fun formatTimestamp(): String {
        return try {
            val parsed = LocalDateTime.parse(timestamp)
            parsed.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' h:mm a"))
        } catch (e: Exception) {
            timestamp
        }
    }
}