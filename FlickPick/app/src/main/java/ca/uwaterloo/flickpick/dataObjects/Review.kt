package ca.uwaterloo.flickpick.dataObjects

data class Review(
    val review_id: String,
    val user_id: String,
    val movie_id: String,
    val rating: Float,
    val comment: String
)