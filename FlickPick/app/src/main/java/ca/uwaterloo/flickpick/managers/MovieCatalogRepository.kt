import android.util.Log
import androidx.compose.runtime.mutableStateOf
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MovieCatalogRepository {
    private val movieCache = mutableMapOf<String, Movie>()

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun fetchMoreMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movieList = DatabaseClient.apiService.getAllMovies().items
                _movies.value = movieList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getMovieForId(movieId: String): Movie? {
        if (movieCache.containsKey(movieId)) {
            return movieCache.get(movieId)
        }
        return withContext(Dispatchers.IO) { // Switch to IO thread
            try {
                val movie = DatabaseClient.apiService.getMovieById(movieId)
                movieCache.put(movie.movieID, movie)
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                null
            }
        }
    }
}
