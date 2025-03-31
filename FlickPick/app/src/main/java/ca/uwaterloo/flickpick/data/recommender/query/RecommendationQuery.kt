package ca.uwaterloo.flickpick.data.recommender.query

import ca.uwaterloo.flickpick.data.recommender.model.Filters
import ca.uwaterloo.flickpick.data.recommender.model.Rating

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecommendationQuery(
    val ratings: List<Rating>,
    val filters: Filters? = null
)