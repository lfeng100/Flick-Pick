package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PrimaryUserManager {
    private val _ratings = MutableStateFlow(emptyMap<String, Float>())
    val ratings = _ratings.asStateFlow();

    fun addRating(movieId: String, score: Float) {
        _ratings.value += (movieId to score)
    }

    fun getRating(movieId: String) : Float? {
        return _ratings.value.get(movieId)
    }

    fun getAllRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        for((key, value) in _ratings.value) {
            ratingsList.add(Rating(key, value))
        }
        return ratingsList
    }
}