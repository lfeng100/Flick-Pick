package ca.uwaterloo.flickpick.managers

import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.Group
import ca.uwaterloo.flickpick.dataObjects.Database.Movie
import ca.uwaterloo.flickpick.dataObjects.Recommender.Rating
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ca.uwaterloo.flickpick.dataObjects.Recommender.Filters
import ca.uwaterloo.flickpick.dataObjects.Recommender.Querys.RecommendationQuery
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.RecommendationResponse

class RecommendationManager(private var selectedGroup: Group?) {
    suspend fun getPersonalReco(): Movie? {
        return withContext(Dispatchers.IO) {
            //TODO: replace with actual filters and userratings
            val userRatings = listOf(
                Rating("avengers-endgame", 4.5f)
            )

            val filters = Filters(
                included_genres = listOf("Action"),
                min_score = 4.0f
            )

            val query = RecommendationQuery(userRatings, filters)

            try {
                val response = RecommenderClient.apiService.getRecommendations(query)
                val recommendations = response.recommendations
                if (recommendations.isNotEmpty()) {
                    return@withContext try {
                        val movieResponse = DatabaseClient.apiService.getMovieById(recommendations[0])
                        println(movieResponse.movieID)
                        println(movieResponse.genres)
                        movieResponse
                    } catch (e: Exception) {
                        Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                        null
                    }
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching movies: ${e.message}")
            }
            return@withContext null
        }
    }


//    override fun getGroupReco(selectedGroup: Group): Movie {
//        // Recommend a random movie for the group (can be improved with real logic)
//        return getPersonalReco()
//    }
}