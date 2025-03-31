package ca.uwaterloo.flickpick.data.database.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupUsers(
    val groupID: String,
    val groupName: String,
    val adminUserID: String,
    val adminUsername: String,
    val groupSize: Int
)