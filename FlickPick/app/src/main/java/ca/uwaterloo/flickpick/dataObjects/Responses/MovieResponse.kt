package ca.uwaterloo.flickpick.dataObjects.Responses

import ca.uwaterloo.flickpick.dataObjects.Movie

data class MovieResponse(
    val items: List<Movie>,
    val total: Int,
    val page: Int,
    val pages: Int
)
