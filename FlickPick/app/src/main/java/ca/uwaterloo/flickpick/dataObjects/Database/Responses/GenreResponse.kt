package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenresResponse(
    val genres: List<String>
)