package ca.uwaterloo.flickpick.data.database.response

import ca.uwaterloo.flickpick.data.database.model.Tag
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TagResponse(
    val items: List<Tag>
)
