package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group

data class GroupResponse(
    val items: List<Group>,
    val total: Int,
    val page: Int,
    val pages: Int
)