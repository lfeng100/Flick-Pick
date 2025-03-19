package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupUsers(
    val groupID: String,
    val groupName: String,
    val adminUserID: String,
    val adminUsername: String,
    val groupSize: Int
)