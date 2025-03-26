package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Review
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReviewResponse(
    val items: List<Review>,
    val total: Int,
    val page: Int,
    val pages: Int
)

@JsonClass(generateAdapter = true)
data class ReviewWithMovie(
    val reviewID: String,
    val rating: Double,
    val message: String?,
    val timestamp: String,
    val userID: String,
    val movieID: String,
    val movie: Movie
) {
    fun toReview(): Review {
        return Review(reviewID, rating, message, timestamp, userID, movieID)
    }
}

@JsonClass(generateAdapter = true)
data class ReviewWithMovieResponse(
    val items: List<ReviewWithMovie>,
    val total: Int,
    val page: Int,
    val pages: Int
)