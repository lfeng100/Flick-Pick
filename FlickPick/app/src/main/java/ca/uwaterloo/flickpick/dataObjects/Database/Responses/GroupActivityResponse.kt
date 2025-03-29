package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.ActivityItem

data class GroupActivityResponse(
    val groupID: String,
    val groupName: String,
    val activity: List<ActivityItem>
)