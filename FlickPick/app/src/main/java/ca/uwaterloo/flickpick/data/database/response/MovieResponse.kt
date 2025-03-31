package ca.uwaterloo.flickpick.data.database.response

import ca.uwaterloo.flickpick.data.database.model.Movie
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MovieResponse(
    val items: List<Movie>,
    val total: Int,
    val page: Int,
    val pages: Int
)
