package ca.uwaterloo.flickpick.dataObjects.recommender.query

import ca.uwaterloo.flickpick.dataObjects.recommender.model.Filters
import ca.uwaterloo.flickpick.dataObjects.recommender.model.Rating

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecommendationQuery(
    val ratings: List<Rating>,
    val filters: Filters? = null
)