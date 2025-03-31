package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewCreate(
    val rating: Double,
    val message: String?,
    val userID: String,
    val movieID: String
)