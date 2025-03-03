package ca.uwaterloo.flickpick.dataObjects

import com.squareup.moshi.Json

data class Movie(
    val movieID: String,
    val title: String,
    val releaseYear: Int,
    val genres: List<String>, // Assuming JSON array
    val rating: Float?,
    val description: String?,
    @Json(name = "tmdb_id") val tmdbId: String?,
    val runtime: Int?,
    @Json(name = "poster_path") val posterPath: String?
)
