package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Review(
    val reviewID: String,
    val rating: Double,
    val message: String?,
    val timestamp: String, // TIMESTAMP
    val userID: String,
    val movieID: String
)