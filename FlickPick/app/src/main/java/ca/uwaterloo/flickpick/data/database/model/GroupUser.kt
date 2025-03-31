package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupUser(
    val groupID: String,
    val userID: String,
    val joinedAt: String // TIMESTAMP
)