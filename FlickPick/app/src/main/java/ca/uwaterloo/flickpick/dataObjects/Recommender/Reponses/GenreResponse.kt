package ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreResponse(
    val genres: List<String>
)