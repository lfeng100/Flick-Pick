package ca.uwaterloo.flickpick.managers

import MovieRepository
import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import kotlinx.coroutines.Dispatchers
import ca.uwaterloo.flickpick.dataObjects.recommender.RecommenderClient
import ca.uwaterloo.flickpick.dataObjects.recommender.model.Filters
import ca.uwaterloo.flickpick.dataObjects.recommender.query.RecommendationQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object RecommendationRepository {
    private val previouslyRecommendedMovieIds = mutableListOf<String>()

    private val _recommendations = MutableStateFlow(emptyList<Movie>())
    val recommendations = _recommendations.asStateFlow()

    fun fetchPersonalRecommendations() {
        if (_recommendations.value.isNotEmpty()) {
            return;
        }
        CoroutineScope(Dispatchers.IO).launch {
            val userRatings = PrimaryUserManager.getAllRatings()
            val filters = Filters(
                excludedMovieIDs = PrimaryUserManager.watched.value + previouslyRecommendedMovieIds
            )
            val query = RecommendationQuery(userRatings, filters)
            val response = RecommenderClient.apiService.getRecommendations(query)
            val recommendations = response.recommendations
            if (recommendations.isNotEmpty()) {
                try {
                    for (movieId in recommendations) {
                        val movieResponse = MovieRepository.getMovieForId(movieId)
                        if (movieResponse != null) {
                            _recommendations.value += movieResponse
                        } else {
                            Log.e("API_ERROR", "Error fetching movie with id $movieId")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                }
            }

        }
    }

    fun clearPersonalRecommendation() {
        // Add recommended movies to previously recommended movie IDs so that the same movie
        // isn't recommended again in the same session
        previouslyRecommendedMovieIds.addAll(_recommendations.value.map { it.movieID })
        _recommendations.value = emptyList()
    }
}