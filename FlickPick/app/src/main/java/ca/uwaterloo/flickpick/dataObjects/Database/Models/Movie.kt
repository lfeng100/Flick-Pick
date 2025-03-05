package ca.uwaterloo.flickpick.dataObjects.Database.Models

import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
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
 {
    fun getPosterUrl(): String? {
        Log.d("MovieItem", "Poster Path: ${posterPath}")
        return posterPath?.let { "https://image.tmdb.org/t/p/w200$it" }
    }
}