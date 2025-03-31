package ca.uwaterloo.flickpick.data.database.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Preference(
    val userID: String,
    val tagID: String
)