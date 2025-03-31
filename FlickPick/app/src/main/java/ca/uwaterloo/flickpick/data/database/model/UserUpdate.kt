package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserUpdate(
    val firstName: String?,
    val lastName: String?,
    val username: String?
)