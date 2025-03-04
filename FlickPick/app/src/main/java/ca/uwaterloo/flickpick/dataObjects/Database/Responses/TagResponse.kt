package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag

data class TagResponse(
    val items: List<Tag>
)
