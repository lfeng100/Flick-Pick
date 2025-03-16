package ca.uwaterloo.flickpick.dataObjects.recommender.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rating(
    val movieID: String,
    val score: Float
)
