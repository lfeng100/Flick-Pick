package ca.uwaterloo.flickpick.domain.repository

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Review
import ca.uwaterloo.flickpick.dataObjects.Database.Models.ReviewCreate
import ca.uwaterloo.flickpick.dataObjects.Database.Models.UserWatched
import ca.uwaterloo.flickpick.dataObjects.recommender.model.Rating
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object PrimaryUserRepository {
    data class PrimaryUserReviewData(
        val movieId: String,
        val score: Float,
        val message: String?
    )

    val userID: String
        get() = getPrimaryUserID()!!

    private val _reviews = mutableStateMapOf<String, PrimaryUserReviewData>()
    private val _watchlist = mutableStateListOf<String>()
    private val _watched = mutableStateListOf<String>()

    val reviews: State<Map<String, PrimaryUserReviewData>> = derivedStateOf { _reviews }
    val watchlist: State<List<String>> = derivedStateOf { _watchlist }
    val watched: State<List<String>> = derivedStateOf { _watched }

    private val reviewMap = mutableMapOf<String, Review>()

    private val channel = Channel<suspend () -> Unit>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            for (task in channel) {
                task()
            }
        }
    }

    private fun enqueueRequest(request: suspend () -> Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            channel.send(request)
        }
    }

    fun getPrimaryUserID(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun addToWatchlist(movieId: String) {
        if (_watchlist.insertSorted(movieId)) {
            enqueueRequest {
                Log.d("PrimaryUserRepository", "$movieId add to watchlist")
                DatabaseClient.apiService.addUserWatchlist(UserWatched(userID, movieId))
            }
        }
    }

    fun removeFromWatchlist(movieId: String) {
        if (_watchlist.remove(movieId)) {
            enqueueRequest {
                Log.d("PrimaryUserRepository", "$movieId removed from watchlist")
                DatabaseClient.apiService.deleteUserWatchlist(userID, movieId)
            }
        }
    }

    fun addToWatched(movieId: String) {
        if (_watched.insertSorted(movieId)) {
            enqueueRequest {
                Log.d("PrimaryUserRepository", "$movieId added to watched")
                DatabaseClient.apiService.addUserWatched(UserWatched(userID, movieId))
            }
        }
        removeFromWatchlist(movieId)
    }

    fun removeFromWatched(movieId: String) {
        if (_watched.remove(movieId)) {
            enqueueRequest {
                Log.d("PrimaryUserRepository", "$movieId removed from watched")
                DatabaseClient.apiService.deleteUserWatched(userID, movieId)
            }
        }
        removeReview(movieId)
    }

    fun removeReview(movieId: String) {
        if (_reviews.remove(movieId) != null) {
            enqueueRequest {
                Log.d("PrimaryUserRepository", "Review removed for $movieId")
                val review = reviewMap[movieId]
                DatabaseClient.apiService.deleteReview(review?.reviewID!!)
                reviewMap.remove(movieId)
            }
        }
    }

    fun addReview(movieId: String, score: Float, message: String?) {
        if (!_reviews.containsKey(movieId)) {
            _reviews[movieId] = PrimaryUserReviewData(movieId, score, message)
            enqueueRequest {
                Log.d("PrimaryUserRepository", "Review added for $movieId")
                reviewMap[movieId] = DatabaseClient.apiService.createReview(
                    ReviewCreate(
                        rating = score.toDouble(),
                        message = message,
                        userID = userID,
                        movieID = movieId
                    )
                )
            }
        }
        // The watched list will update automatically in the backend when a review is added
        _watched.insertSorted(movieId)
        removeFromWatchlist(movieId)
    }

    fun getAllRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        for((key, value) in _reviews) {
            ratingsList.add(Rating(key, value.score))
        }
        return ratingsList
    }

    private fun SnapshotStateList<String>.insertSorted(movieId: String) : Boolean {
        val idx = binarySearch(movieId)
        if (idx >= 0) return false
        val insertIdx = -idx - 1
        add(insertIdx, movieId)
        return true
    }

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

    fun loadPrimaryUserLists() {
        if (_isLoaded.value) {
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            while (!_isLoaded.value) {
                _reviews.clear()
                _watchlist.clear()
                _watched.clear()
                try {
                    val reviews = ReviewRepository.getReviewsForUser(userID)
                    reviews!!.forEach { review ->
                        reviewMap[review.movieID] = review
                        _reviews[review.movieID] = PrimaryUserReviewData(
                            movieId = review.movieID,
                            score = review.rating.toFloat(),
                            message = review.message,
                        )
                    }
                    _watched.addAll(ReviewRepository.getWatchedForUser(userID)!!.map {it.movieID})
                    _watchlist.addAll(ReviewRepository.getWatchlistForUser(userID)!!.map {it.movieID})
                    _isLoaded.value = true
                } catch (e: Exception) {
                    Log.e("PrimaryUserRepository", "Error loading lists: ${e.message}")
                }
            }
        }
    }

    fun clear() {
        _reviews.clear()
        _watchlist.clear()
        _watched.clear()
        reviewMap.clear()
        _isLoaded.value = false
    }
}