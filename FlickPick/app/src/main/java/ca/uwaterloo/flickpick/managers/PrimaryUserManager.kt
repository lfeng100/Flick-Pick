package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating

object PrimaryUserManager {
    private val ratings = mutableMapOf<String, Float>()

    fun containsRatingForMovie(movieId: String): Boolean {
        return ratings.containsKey(movieId)
    }

    fun addRating(movieId: String, score: Float) {
        ratings[movieId] = score
        // TODO: This logic kind of sucks, need to find a better way to manage the recommendations flow
        RecommendationRepository.fetchPersonalRecommendations()
    }

    fun getRating(movieId: String) : Float? {
        return ratings[movieId]
    }

    fun getAllRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        for((key, value) in ratings) {
            ratingsList.add(Rating(key, value))
        }
        return ratingsList
    }
}