package ca.uwaterloo.flickpick.data.database.response

import ca.uwaterloo.flickpick.data.database.model.Movie
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