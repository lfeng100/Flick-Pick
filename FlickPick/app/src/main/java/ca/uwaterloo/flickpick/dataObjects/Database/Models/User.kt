package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val email: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val userID: String
)