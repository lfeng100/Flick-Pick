package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddUserToGroup(
    val groupID: String,
    val userID: String
)