package ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecommendationResponse(
    val recommendations: List<String>
)