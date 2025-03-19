package ca.uwaterloo.flickpick.dataObjects.Database.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    val tagID: String,
    val tagName: String,
    val tagType: String
)