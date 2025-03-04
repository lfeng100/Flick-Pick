package ca.uwaterloo.flickpick.dataObjects.Database.Models

data class GroupUser(
    val groupID: String,
    val userID: String,
    val joinedAt: String // TIMESTAMP
)