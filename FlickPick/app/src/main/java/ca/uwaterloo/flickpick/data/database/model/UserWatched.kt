package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserWatched(
    val userID: String,
    val movieID: String,
)