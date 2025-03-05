package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupUser(
    val groupID: String,
    val userID: String,
    val joinedAt: String // TIMESTAMP
)