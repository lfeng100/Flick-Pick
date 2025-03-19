package ca.uwaterloo.flickpick.ui.screen

import MovieRepository
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.component.FilterBottomSheet
import ca.uwaterloo.flickpick.ui.component.InfiniteMovieGrid
import ca.uwaterloo.flickpick.ui.component.SearchBar
import ca.uwaterloo.flickpick.ui.component.TitleTopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun BrowseScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf(MovieRepository.titleQuery.value) }
    var isSearching by remember { mutableStateOf(searchQuery != "") }
    var isFiltering by remember { mutableStateOf(false) }
    val movies by MovieRepository.movies.collectAsState()
    val isFetching by MovieRepository.isFetching.collectAsState()
    val selectedFilters by MovieRepository.selectedFilters.collectAsState()

    Scaffold(
        topBar = {
            Column {
                TitleTopBar("Browse",
                    listOf(
                        TopBarButtonData(
                            icon = Icons.Rounded.Search,
                            onClick = {
                                isSearching = !isSearching
                                if (searchQuery != "") {
                                    searchQuery = ""
                                    MovieRepository.applyTitleQuery("")
                                }
                            },
                            tint = if (isSearching) Color(0xFFFFC107) else null
                        ),
                        TopBarButtonData(
                            icon = Icons.Rounded.FilterList,
                            onClick = { isFiltering = !isFiltering },
                            tint = if (selectedFilters.isNotEmpty()) Color(0xFFFFC107) else null
                        )
                    )
                )
                AnimatedVisibility(
                    visible = isSearching,
                    enter = fadeIn(animationSpec = tween(durationMillis = 200)) + slideInVertically(initialOffsetY = { -it }),
                    exit = fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutVertically(targetOffsetY = { -it })
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChanged = {
                            searchQuery = it
                            MovieRepository.applyTitleQuery(searchQuery)
                        }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Show Movies Grid
            InfiniteMovieGrid(
                movies,
                navController,
                isFetching = isFetching,
                onLoadMore = {
                    if (!isFetching) {
                        MovieRepository.fetchMoreMovies()
                    }
                }
            )
            if (isFiltering) {
                FilterBottomSheet(
                    onFiltersChanged = { newTags ->
                        MovieRepository.applyFilters(newTags)
                    },
                    onDismiss = { isFiltering = false }
                )
            }
        }
    }
}