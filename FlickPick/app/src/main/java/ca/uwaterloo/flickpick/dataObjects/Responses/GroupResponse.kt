package ca.uwaterloo.flickpick.dataObjects.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Group

data class GroupResponse(
    val items: List<Group>,
    val total: Int,
    val page: Int,
    val pages: Int
)