package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val userID: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)