package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdate(
    val firstName: String?,
    val lastName: String?,
    val username: String?
)