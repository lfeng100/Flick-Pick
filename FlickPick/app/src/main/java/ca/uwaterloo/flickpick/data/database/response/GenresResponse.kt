package ca.uwaterloo.flickpick.data.database.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenresResponse(
    val genres: List<String>
)