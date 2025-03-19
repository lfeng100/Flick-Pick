package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupCreate(
    val groupName: String,
    val adminUserID: String?,
    val adminUsername: String?,
    val groupSize: Int?
)