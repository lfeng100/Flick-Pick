package ca.uwaterloo.flickpick.dataObjects

data class Filters(
    val included_genres: List<String>? = null,
    val excluded_genres: List<String>? = null,
    val min_year: Int? = null,
    val max_year: Int? = null,
    val languages: List<String>? = null,
    val max_runtime: Int? = null,
    val min_score: Float? = null
)