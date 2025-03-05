package ca.uwaterloo.flickpick.dataObjects.Recommender.Models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rating(
    val movie_id: String,
    val score: Float
)
