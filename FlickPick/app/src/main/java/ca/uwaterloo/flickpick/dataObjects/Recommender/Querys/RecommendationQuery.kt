package ca.uwaterloo.flickpick.dataObjects.Recommender.Querys

import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Filters
import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating

data class RecommendationQuery(
    val ratings: List<Rating>,
    val filters: Filters? = null
)