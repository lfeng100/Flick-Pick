import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object MovieCatalog {
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies = _movies.asStateFlow()

    fun fetchMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movieList = DatabaseClient.apiService.getAllMovies().items
                _movies.value = movieList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
