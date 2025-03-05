package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class UserResponse(
    val user_id: String,
    val token: String
)