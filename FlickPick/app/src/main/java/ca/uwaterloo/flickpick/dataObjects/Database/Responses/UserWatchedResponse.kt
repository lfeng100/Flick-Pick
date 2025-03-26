package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserWatchedWithMovie(
    val userID: String,
    val movieID: String,
    val timestamp: String,
    val movie: Movie
)

@JsonClass(generateAdapter = true)
data class UserWatchedWithMovieResponse(
    val items: List<UserWatchedWithMovie>,
    val total: Int,
    val page: Int,
    val pages: Int
)