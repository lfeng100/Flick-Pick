package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object PrimaryUserManager {
    val ratings = mutableMapOf<String, Float>()

    fun containsRatingForMovie(movieId: String): Boolean {
        return ratings.containsKey(movieId)
    }

    fun addRating(movieId: String, score: Float) {
        ratings[movieId] = score
        // TODO: This logic kind of sucks, need to find a better way to manage the recommendations flow
        RecommendationManager.fetchPersonalRecommendations()
    }

    fun getRating(movieId: String) : Float? {
        return ratings.get(movieId)
    }

    fun getAllRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        for((key, value) in ratings) {
            ratingsList.add(Rating(key, value))
        }
        return ratingsList
    }
}