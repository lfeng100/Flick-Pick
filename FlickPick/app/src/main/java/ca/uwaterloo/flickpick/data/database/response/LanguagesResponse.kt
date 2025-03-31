package ca.uwaterloo.flickpick.data.database.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LanguagesResponse(
    val languages: List<String>
)