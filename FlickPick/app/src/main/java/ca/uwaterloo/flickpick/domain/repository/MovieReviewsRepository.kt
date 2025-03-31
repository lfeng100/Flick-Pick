package ca.uwaterloo.flickpick.domain.repository

import android.util.Log
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.data.database.model.Review
import ca.uwaterloo.flickpick.data.database.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewWithUser(
    val review: Review,
    val user: User
)

object MovieReviewsRepository {
    private val movieIDToReviewData = mutableMapOf<String, ReviewData>()

    fun getReviewData(movieID: String) : ReviewData {
        val result = movieIDToReviewData[movieID]
        if (result == null) {
            val newData = ReviewData(movieID)
            movieIDToReviewData[movieID] = newData
            return newData
        } else {
            return result
        }
    }

    class ReviewData(private val movieID: String) {
        private val _reviewsList = MutableStateFlow(emptyList<ReviewWithUser>())
        val reviewsList = _reviewsList.asStateFlow()

        private val _isFetching = MutableStateFlow(false)
        val isFetching = _isFetching.asStateFlow()

        private var isLoaded = false

        private var page = 0
        private var job: Job? = null

        fun fetchMoreReviews() {
            Log.i("MovieReviews",
                "Fetching page $page of reviews for movie $movieID from the backend")
            if (isLoaded) {
                Log.i("MovieReviews", "Already loaded, skipping")
                return
            }
            if (job?.isActive == true) {
                Log.i("MovieReviews", "Already fetching page $page, skipping")
                return
            }
            job = CoroutineScope(Dispatchers.IO).launch {
                fetchReviewsHelper()
            }
        }

        fun refreshAndFetch() {
            val oldJob = job
            job = CoroutineScope(Dispatchers.IO).launch {
                oldJob?.cancelAndJoin()
                page = 0
                _isFetching.value = false
                _reviewsList.value = emptyList()
                fetchReviewsHelper()
            }
        }

        private suspend fun fetchReviewsHelper() {
            _isFetching.value = true
            try {
                // NOTE: This approach may cause some slight issues if another user reviews the movie
                // while you're viewing the reviews, since the backend may return different reviews,
                // and there is no attempt to reconcile this with the backend
                val items =
                    DatabaseClient.apiService.getReviewsForMovie(
                        movieID = movieID,
                        limit = 4,
                        offset = page * 4
                    ).items
                if (items.isEmpty()) {
                    isLoaded = true
                }
                for (item in items) {
                    if (item.userID == PrimaryUserRepository.getPrimaryUserID() ||
                        item.message == null) {
                        continue
                    }
                    val user = UserRepository.getUserForId(item.userID)
                    if (user != null) {
                        _reviewsList.value += ReviewWithUser(item, user)
                    }
                }
                page += 1
            } catch (e: Exception) {
                Log.e("ReviewsRepository", "Error fetching reviews: ${e.message}")
            }
            _isFetching.value = false
        }
    }
}