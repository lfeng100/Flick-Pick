package ca.uwaterloo.flickpick.dataObjects.Querys

import ca.uwaterloo.flickpick.dataObjects.Recommender.Filters
import ca.uwaterloo.flickpick.dataObjects.Recommender.Rating

data class RecommendationQuery(
    val ratings: List<Rating>,
    val filters: Filters? = null
)