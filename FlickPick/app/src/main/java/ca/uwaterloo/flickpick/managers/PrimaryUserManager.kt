package ca.uwaterloo.flickpick.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating

data class ReviewData(
    val movieId: String,
    val score: Float,
    val message: String?
)

object PrimaryUserManager {
    private val _reviews = mutableStateMapOf<String, ReviewData>()
    private val _watchlist = mutableStateListOf<String>()
    private val _watched = mutableStateListOf<String>()

    val reviews: State<Map<String, ReviewData>> = derivedStateOf { _reviews }
    val watchlist: State<List<String>> = derivedStateOf { _watchlist }
    val watched: State<List<String>> = derivedStateOf { _watched }

    fun addToWatchlist(movieId: String) {
        _watchlist.insertSorted(movieId)
    }

    fun removeFromWatchlist(movieId: String) {
        _watchlist.remove(movieId)
    }

    fun addToWatched(movieId: String) {
        _watched.insertSorted(movieId)
        _watchlist.remove(movieId)
    }

    fun removeFromWatched(movieId: String) {
        _watched.remove(movieId)
        _reviews.remove(movieId)
    }

    fun removeReview(movieId: String) {
        _reviews.remove(movieId)
    }

    fun addReview(movieId: String, score: Float, message: String?) {
        _reviews[movieId] = ReviewData(movieId, score, message)
        _watched.insertSorted(movieId)
        _watchlist.remove(movieId)
    }

    fun getAllRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        for((key, value) in _reviews) {
            ratingsList.add(Rating(key, value.score))
        }
        return ratingsList
    }

    private fun SnapshotStateList<String>.insertSorted(movieId: String) {
        val idx = binarySearch(movieId)
        val insertIdx = if (idx >= 0) idx else -idx - 1
        add(insertIdx, movieId)
    }
}