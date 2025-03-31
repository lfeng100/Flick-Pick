package ca.uwaterloo.flickpick.data.recommender.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Filters(
    val includedGenres: List<String>? = null,
    val excludedGenres: List<String>? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val languages: List<String>? = null,
    val maxRuntime: Int? = null,
    val minScore: Float? = null,
    val excludedMovieIDs: List<String>? = null
)