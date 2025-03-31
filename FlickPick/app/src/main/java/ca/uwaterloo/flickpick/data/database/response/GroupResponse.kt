package ca.uwaterloo.flickpick.data.database.response

import ca.uwaterloo.flickpick.data.database.model.Group
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupResponse(
    val items: List<Group>,
    val total: Int,
    val page: Int,
    val pages: Int
)