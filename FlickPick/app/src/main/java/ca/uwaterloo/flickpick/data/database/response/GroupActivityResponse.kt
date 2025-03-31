package ca.uwaterloo.flickpick.data.database.response

import ca.uwaterloo.flickpick.data.database.model.ActivityItem

data class GroupActivityResponse(
    val groupID: String,
    val groupName: String,
    val activity: List<ActivityItem>
)