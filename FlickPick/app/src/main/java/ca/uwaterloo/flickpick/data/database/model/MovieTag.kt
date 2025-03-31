package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieTag(
    val movieID: String,
    val tagID: String
)