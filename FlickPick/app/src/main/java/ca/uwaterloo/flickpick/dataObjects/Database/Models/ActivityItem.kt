package ca.uwaterloo.flickpick.dataObjects.Database.Models

data class ActivityItem(
    val type: String,
    val timestamp: String,
    val userID: String,
    val movieID: String,
    val movieTitle: String,
    val rating: Float? = null,
    val message: String? = null
)