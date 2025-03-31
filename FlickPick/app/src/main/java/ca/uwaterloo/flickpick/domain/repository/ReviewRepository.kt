package ca.uwaterloo.flickpick.domain.repository

import MovieRepository
import android.util.Log
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.data.database.model.Movie
import ca.uwaterloo.flickpick.data.database.model.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ReviewRepository {
    suspend fun getReviewsForUser(userID: String): List<Review>? {
        return withContext(Dispatchers.IO) {
            try {
                val reviewList = mutableListOf<Review>()
                var page = 0
                while (true) {
                    val items = DatabaseClient.apiService.getReviewsForUser(
                        userID = userID,
                        limit = 100,
                        offset = page * 100
                    ).items
                    if (items.isEmpty()) {
                        break
                    }
                    reviewList.addAll(items.map{it.toReview()})
                    MovieRepository.addMovies(items.map{it.movie})
                    page += 1
                }
                reviewList
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching reviews for user $userID: ${e.message}")
                null
            }
        }
    }

    suspend fun getWatchlistForUser(userID: String): List<Movie>? {
        return withContext(Dispatchers.IO) {
            try {
                val movieList = mutableListOf<Movie>()
                var page = 0
                while (true) {
                    val items = DatabaseClient.apiService.getUserWatchlist(
                        userID = userID,
                        limit = 100,
                        offset = page * 100
                    ).items
                    if (items.isEmpty()) {
                        break
                    }
                    movieList.addAll(items.map{it.movie})
                    MovieRepository.addMovies(items.map{it.movie})
                    page += 1
                }
                movieList

            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching watchlist for user $userID: ${e.message}")
                null
            }
        }
    }

    suspend fun getWatchedForUser(userID: String): List<Movie>? {
        return withContext(Dispatchers.IO) {
            try {
                val movieList = mutableListOf<Movie>()
                var page = 0
                while (true) {
                    val items = DatabaseClient.apiService.getUserWatched(
                        userID = userID,
                        limit = 100,
                        offset = page * 100
                    ).items
                    if (items.isEmpty()) {
                        break
                    }
                    movieList.addAll(items.map{it.movie})
                    MovieRepository.addMovies(items.map{it.movie})
                    page += 1
                }
                movieList
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching watched for user $userID: ${e.message}")
                null
            }
        }
    }
}