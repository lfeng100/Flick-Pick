package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewCreate(
    val rating: Double,
    val message: String?,
    val userID: String,
    val movieID: String
)