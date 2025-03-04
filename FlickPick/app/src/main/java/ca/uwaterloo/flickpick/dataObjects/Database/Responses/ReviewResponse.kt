package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Review

data class ReviewResponse(
    val items: List<Review>,
    val total: Int,
    val page: Int,
    val pages: Int
)