package ca.uwaterloo.flickpick.domain.repository

import MovieRepository
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import ca.uwaterloo.flickpick.data.database.model.Movie
import kotlinx.coroutines.Dispatchers
import ca.uwaterloo.flickpick.data.recommender.RecommenderClient
import ca.uwaterloo.flickpick.data.recommender.model.Filters
import ca.uwaterloo.flickpick.data.recommender.query.RecommendationQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object RecommendationRepository {
    private val previouslyRecommendedMovieIds = mutableListOf<String>()

    private val _recommendations = MutableStateFlow(emptyList<Movie>())
    val recommendations = _recommendations.asStateFlow()

    private val _filters = mutableStateOf<Filters?>(null)
    val filters : State<Filters?> = _filters

    fun setFilters(filters: Filters?) {
        _filters.value = filters
    }

    fun fetchPersonalRecommendations() {
        if (_recommendations.value.isNotEmpty()) {
            return;
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRatings = PrimaryUserRepository.getAllRatings()
                val filters = Filters(
                    includedGenres = _filters.value?.includedGenres,
                    excludedGenres = _filters.value?.excludedGenres,
                    excludedMovieIDs = PrimaryUserRepository.watched.value + previouslyRecommendedMovieIds
                )
                Log.i("Recommender", "included genres " + filters.includedGenres)
                Log.i("Recommender", "excluded genres " + filters.excludedGenres)
                val query = RecommendationQuery(userRatings, filters)
                val response = RecommenderClient.apiService.getRecommendations(query)
                val recommendations = response.recommendations
                if (recommendations.isNotEmpty()) {
                    for (movieId in recommendations) {
                        val movieResponse = MovieRepository.getMovieForId(movieId)
                        if (movieResponse != null) {
                            _recommendations.value += movieResponse
                        } else {
                            Log.e("API_ERROR", "Error fetching movie with id $movieId")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error getting recommendations: ${e.message}")
            }
        }
    }

    fun clearPersonalRecommendations() {
        // Add recommended movies to previously recommended movie IDs so that the same movie
        // isn't recommended again in the same session
        previouslyRecommendedMovieIds.addAll(_recommendations.value.map { it.movieID })
        _recommendations.value = emptyList()
    }

    fun clear() {
        previouslyRecommendedMovieIds.clear()
        _recommendations.value = emptyList()
        _filters.value = null
    }
}