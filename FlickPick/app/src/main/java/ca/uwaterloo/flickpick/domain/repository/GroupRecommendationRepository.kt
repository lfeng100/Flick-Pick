package ca.uwaterloo.flickpick.domain.repository

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.recommender.RecommenderClient
import ca.uwaterloo.flickpick.dataObjects.recommender.model.Filters
import ca.uwaterloo.flickpick.dataObjects.recommender.model.Rating
import ca.uwaterloo.flickpick.dataObjects.recommender.query.GroupRecommendationQuery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object GroupRecommendationRepository {
    private val previouslyRecommendedMovieIds = mutableListOf<String>()

    private val _recommendations = MutableStateFlow(emptyList<Movie>())
    val recommendations = _recommendations.asStateFlow()

    private val _filters = mutableStateOf<Filters?>(null)
    val filters : State<Filters?> = _filters

    fun setFilters(filters: Filters?) {
        _filters.value = filters
    }

    fun fetchGroupRecommendations(groupID: String) {
        if (_recommendations.value.isNotEmpty()) {
            return;
        }
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val users = GroupRepository.getAllUsersInGroup(groupID)
                val watchedMovies = mutableSetOf<String>()
                val groupRatings = mutableListOf<List<Rating>>()
                for (user in users) {
                    val userRatings = ReviewRepository.getReviewsForUser(user.userID)!!
                        .map { Rating(it.movieID, it.rating.toFloat()) }
                    groupRatings.add(userRatings)
                    watchedMovies.addAll(
                        ReviewRepository.getWatchedForUser(user.userID)!!.map { it.movieID })
                }
                val filters = Filters(
                    includedGenres = _filters.value?.includedGenres,
                    excludedGenres = _filters.value?.excludedGenres,
                    excludedMovieIDs = PrimaryUserRepository.watched.value + previouslyRecommendedMovieIds
                )
                val query = GroupRecommendationQuery(groupRatings, filters)
                val response = RecommenderClient.apiService.getGroupRecommendations(query)
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
                Log.e("API_ERROR", "Error getting group recommendations: ${e.message}")
            }
        }
    }

    fun clearGroupRecommendation() {
        // Add recommended movies to previously recommended movie IDs so that the same movie
        // isn't recommended again in the same session
        previouslyRecommendedMovieIds.addAll(_recommendations.value.map { it.movieID })
        _recommendations.value = emptyList()
    }

    fun clearPreviouslyRecommended() {
        previouslyRecommendedMovieIds.clear()
    }
}