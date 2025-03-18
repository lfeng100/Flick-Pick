import android.util.Log
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

object MovieRepository {
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies = _movies.asStateFlow()

    private val _isFetching = MutableStateFlow(false)
    val isFetching = _isFetching.asStateFlow()

    private val _selectedFilters = MutableStateFlow(mapOf<String, String>())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _tags = MutableStateFlow(emptyList<Tag>())
    val tags = _tags.asStateFlow()

    private val _titleQuery = MutableStateFlow("")
    val titleQuery = _titleQuery.asStateFlow()

    private val movieCache: MutableMap<String, Movie> = ConcurrentHashMap()
    private var job: Job? = null
    private var page = 0

    init {
        fetchTags()
    }

    fun fetchMoreMovies() {
        Log.i("MovieCatalog", "Fetching page $page from backend")
        if (job?.isActive == true) {
            Log.i("MovieCatalog", "Already fetching page $page, skipping")
            return
        }
        job = CoroutineScope(Dispatchers.IO).launch {
            fetchMoviesHelper()
        }
    }

    fun applyFilters(selectedTags: Map<String, String>) {
        val oldJob = job
        job = CoroutineScope(Dispatchers.Main).launch {
            _selectedFilters.value = selectedTags
            oldJob?.cancelAndJoin()
            page = 0
            _movies.value = emptyList()
            fetchMoviesHelper()
        }
    }

    fun applyTitleQuery(searchString: String) {
        val oldJob = job
        job = CoroutineScope(Dispatchers.Main).launch {
            _titleQuery.value = searchString
            oldJob?.cancelAndJoin()
            page = 0
            _movies.value = emptyList()
            fetchMoviesHelper()
        }
    }

    private suspend fun fetchMoviesHelper() {
        _isFetching.value = true
        try {
            val movieList =
                if (_titleQuery.value == "" && _selectedFilters.value.isEmpty())
                    DatabaseClient.apiService.getAllMovies(
                        limit = 12,
                        offset = page * 12
                    ).items
                else
                    DatabaseClient.apiService.searchMovies(
                        limit = 12,
                        offset = page * 12,
                        titleQuery = _titleQuery.value,
                        selectedTags = _selectedFilters.value.values.toList()
                    ).items
            _movies.value += movieList
            for (movie in movieList) {
                movieCache[movie.movieID] = movie
            }
            page += 1
        } catch (e: Exception) {
            Log.e("MovieRepository", "Error fetching movies: ${e.message}")
        }
        _isFetching.value = false
    }

    private fun fetchTags() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = DatabaseClient.apiService.getTags()
                _tags.value = response
            } catch (e: Exception) {
                Log.e("MovieRepository", "Error fetching tags: ${e.message}")
            }
        }
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
}
