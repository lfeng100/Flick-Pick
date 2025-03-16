package ca.uwaterloo.flickpick.dataObjects.recommender.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecommendationResponse(
    val recommendations: List<String>
)