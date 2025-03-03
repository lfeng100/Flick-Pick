package ca.uwaterloo.flickpick.dataObjects

data class Movie(
    val movieID: String,
    val title: String,
    val releaseYear: Int,
    val genres: List<String>,
    val rating: Float?
)
