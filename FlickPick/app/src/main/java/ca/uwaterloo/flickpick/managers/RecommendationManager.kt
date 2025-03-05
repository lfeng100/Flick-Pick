package ca.uwaterloo.flickpick.managers

import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Filters
import ca.uwaterloo.flickpick.dataObjects.Recommender.Querys.RecommendationQuery
import ca.uwaterloo.flickpick.dataObjects.Recommender.RecommenderClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object RecommendationManager {
    private val _recommendations = MutableStateFlow(emptyList<Movie>())
    val recommendations = _recommendations.asStateFlow()

    fun fetchPersonalRecommendations() {
        CoroutineScope(Dispatchers.IO).launch {
            val userRatings = listOf(
                Rating("avengers-endgame", 4.5f)
            )

            val filters = Filters(
                included_genres = listOf("Action"),
                min_score = 4.0f
            )

            val query = RecommendationQuery(userRatings, filters)

            val response = RecommenderClient.apiService.getRecommendations(query)
            val recommendations = response.recommendations
            if (recommendations.isNotEmpty()) {
                try {
                    val recList = mutableListOf<Movie>()
                    for (movieId in recommendations) {
                        val movieResponse = DatabaseClient.apiService.getMovieById(movieId)
                        println(movieResponse.movieID)
                        println(movieResponse.genres)
                        recList.add(movieResponse)
                    }
                    _recommendations.value = recList;
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                }
            }

        }
    }


//    override fun getGroupReco(selectedGroup: Group): Movie {
//        // Recommend a random movie for the group (can be improved with real logic)
//        return getPersonalReco()
//    }
}