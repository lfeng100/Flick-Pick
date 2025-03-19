package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddUserToGroup(
    val groupID: String,
    val userID: String
)