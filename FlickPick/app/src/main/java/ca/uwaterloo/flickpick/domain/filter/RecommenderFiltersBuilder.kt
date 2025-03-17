package ca.uwaterloo.flickpick.domain.filter

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.state.ToggleableState
import ca.uwaterloo.flickpick.dataObjects.recommender.model.Filters

object FiltersConstants {
    val genreList = listOf(
        "Action", "Adventure", "Animation", "Comedy",
        "Crime", "Documentary", "Drama", "Family", "Fantasy",
        "History", "Horror", "Music", "Mystery", "Romance",
        "Science Fiction", "Thriller", "TV Movie", "War", "Western"
    )
}

class RecommenderFiltersBuilder() {
    private val _includedGenres = mutableStateListOf<String>()
    private val _excludedGenres = mutableStateListOf<String>()

    val includedGenres: State<List<String>> = derivedStateOf { _includedGenres }
    val excludedGenres: State<List<String>> = derivedStateOf { _excludedGenres }

    constructor(initial: Filters?) : this() {
        initial?. let { f ->
            f.includedGenres?.let { incl ->
                _includedGenres.addAll(incl)
            }
            f.excludedGenres?.let { excl ->
                _excludedGenres.addAll(excl)
            }
        }
    }

    fun includeGenre(genre: String) {
        _includedGenres.add(genre)
        _excludedGenres.remove(genre)
    }

    fun excludeGenre(genre: String) {
        _excludedGenres.add(genre)
        _includedGenres.remove(genre)
    }

    fun removeGenre(genre: String) {
        _includedGenres.remove(genre)
        _excludedGenres.remove(genre)
    }

    fun buildFilters(): Filters? {
        if (_includedGenres.isEmpty() &&
            _excludedGenres.isEmpty()) {
            return null;
        }
        val included = if(_includedGenres.isEmpty()) null else _includedGenres
        val excluded = if(_excludedGenres.isEmpty()) null else _excludedGenres
        return Filters(
            includedGenres = included,
            excludedGenres = excluded
        )
    }

    fun stateOfGenre(genre: String): ToggleableState {
        if (_includedGenres.contains(genre)) {
            return ToggleableState.On
        } else if (_excludedGenres.contains(genre)) {
            return ToggleableState.Indeterminate
        } else {
            return ToggleableState.Off
        }
    }
}