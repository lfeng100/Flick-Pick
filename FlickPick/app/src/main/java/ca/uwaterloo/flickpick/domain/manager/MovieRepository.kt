import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

object MovieRepository {
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies = _movies.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()

    private val _tags = MutableStateFlow(emptyList<Tag>())
    val tags = _tags.asStateFlow()

    private val _selectedFilters = MutableStateFlow(mapOf<String, String>())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val movieCache: MutableMap<String, Movie> = ConcurrentHashMap()
    private var job: Job? = null
    private var page = 0

    init{
        fetchTags()
    }
    fun fetchMoreMovies() {
        Log.i("MovieCatalog", "Fetching page $page from backend")
        if (job?.isActive == true) {
            Log.i("MovieCatalog", "Already fetching page $page, skipping")
            return
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            _isFetching.value = true
            try {
                val movieList = DatabaseClient.apiService.getAllMovies(12, page * 12).items
                _movies.value += movieList
                for (movie in movieList) {
                    movieCache[movie.movieID] = movie
                }
                page += 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
            _isFetching.value = false
        }
    }
    fun fetchTags() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = DatabaseClient.apiService.getTags()
                _tags.value = response
            } catch (e: Exception) {
                Log.e("MovieRepository", "Error fetching tags: ${e.message}")
            }
        }
    }

    fun applyFilters(selectedTags: Map<String, String>) {
        _selectedFilters.value = selectedTags
        searchMovies()
    }

    suspend fun getMovieForId(movieId: String): Movie? {
        if (movieCache[movieId] != null) {
            Log.i("MovieCatalog", "Cache hit for movie with id $movieId")
            return movieCache[movieId]
        }
        Log.i("MovieCatalog", "Fetching movie with id $movieId from backend")
        return withContext(Dispatchers.IO) {
            try {
                val movie = DatabaseClient.apiService.getMovieById(movieId)
                movieCache[movieId] = movie
                movie
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error fetching movies: ${e.message}")
                null
            }
        }
    }
    fun searchMovies(titleQuery: String? = null, selectedTags: Map<String, String>? = null) {
        if (job?.isActive == true) {
            Log.i("MovieCatalog", "Already searching, skipping")
            return
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            _isFetching.value = true
            try {
                val response = DatabaseClient.apiService.searchMovies(
                    titleQuery,
                    selectedTags?.values?.toList() ?: _selectedFilters.value.values.toList()
                )
                _movies.value = response.items
                Log.i("MovieRepository", "Movies updated with filters: ${_selectedFilters.value}")
            } catch (e: Exception) {
                Log.e("MovieRepository", "Error searching movies: ${e.message}")
            }
            _isFetching.value = false
        }
    }
}
