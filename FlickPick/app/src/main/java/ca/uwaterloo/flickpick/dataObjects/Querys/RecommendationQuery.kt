package ca.uwaterloo.flickpick.dataObjects.Querys

import ca.uwaterloo.flickpick.dataObjects.Filters
import ca.uwaterloo.flickpick.dataObjects.Rating

data class RecommendationQuery(
    val ratings: List<Rating>,
    val filters: Filters? = null
)