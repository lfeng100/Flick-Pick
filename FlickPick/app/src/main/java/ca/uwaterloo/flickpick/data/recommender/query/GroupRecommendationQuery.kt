package ca.uwaterloo.flickpick.data.recommender.query

import ca.uwaterloo.flickpick.data.recommender.model.Filters
import ca.uwaterloo.flickpick.data.recommender.model.Rating

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupRecommendationQuery(
    val groupRatings: List<List<Rating>>,
    val filters: Filters? = null
)