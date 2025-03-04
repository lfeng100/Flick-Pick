package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie

data class MovieResponse(
    val items: List<Movie>,
    val total: Int,
    val page: Int,
    val pages: Int
)
