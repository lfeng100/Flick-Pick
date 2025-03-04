package ca.uwaterloo.flickpick.dataObjects.Database.Models

data class Group(
    val groupID: String,
    val groupName: String,
    val adminUserID: String?
)