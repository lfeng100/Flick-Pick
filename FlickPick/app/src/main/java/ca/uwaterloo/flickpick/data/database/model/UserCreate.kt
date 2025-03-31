package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserCreate(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val userID: String
)