package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Group(
    val groupID: String,
    val groupName: String,
    val adminUserID: String?
)