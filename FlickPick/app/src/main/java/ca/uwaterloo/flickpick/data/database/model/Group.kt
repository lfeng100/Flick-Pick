package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Group(
    val groupID: String,
    val groupName: String,
    val adminUserID: String?,
    val adminUsername: String?,
    val groupSize: Int?
)