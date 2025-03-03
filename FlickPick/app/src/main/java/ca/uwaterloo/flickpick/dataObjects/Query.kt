package ca.uwaterloo.flickpick.dataObjects

data class Query(
    val ratings: List<Rating>,
    val filters: Filters? = null
)