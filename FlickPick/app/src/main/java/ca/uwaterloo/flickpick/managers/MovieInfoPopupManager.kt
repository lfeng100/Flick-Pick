package ca.uwaterloo.flickpick.managers

import androidx.lifecycle.ViewModel
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MovieInfoPopupManager {
    private val _selectedMovie = MutableStateFlow<Movie?>(null) // Holds the selected data
    val selectedMovie = _selectedMovie.asStateFlow()

    fun selectMovie(movie: Movie) {
        _selectedMovie.value = movie
    }
}