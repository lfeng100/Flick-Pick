package ca.uwaterloo.flickpick.managers

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import ca.uwaterloo.flickpick.dataObjects.Recommender.Models.Rating

object PrimaryUserManager {
    private val _ratings = mutableStateMapOf<String, Float>()
    private val _watchlist = mutableStateListOf<String>()
    private val _watched = mutableStateListOf<String>()

    val ratings: State<Map<String, Float>> = derivedStateOf { _ratings }
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
        _ratings.remove(movieId)
    }

    fun removeRating(movieId: String) {
        _ratings.remove(movieId)
    }

    fun addRating(movieId: String, score: Float) {
        _ratings[movieId] = score
        _watched.insertSorted(movieId)
        _watchlist.remove(movieId)
    }

    fun getAllRatings(): List<Rating> {
        val ratingsList = mutableListOf<Rating>()
        for((key, value) in _ratings) {
            ratingsList.add(Rating(key, value))
        }
        return ratingsList
    }

    private fun SnapshotStateList<String>.insertSorted(movieId: String) {
        val idx = binarySearch(movieId)
        val insertIdx = if (idx >= 0) idx else -idx - 1
        add(insertIdx, movieId)
    }
}