package ca.uwaterloo.flickpick.ui.screen

import MovieRepository
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.component.FilterBottomSheet
import ca.uwaterloo.flickpick.ui.component.InfiniteMovieGrid
import ca.uwaterloo.flickpick.ui.component.SearchBar
import ca.uwaterloo.flickpick.ui.component.TopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun BrowseScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var isFiltering by remember { mutableStateOf(false) }

    val movies by MovieRepository.movies.collectAsState()
    val isFetching by MovieRepository.isFetching.collectAsState()
    Scaffold(
        topBar = {
            Column {
                TopBar("Browse",
                    listOf(
                        TopBarButtonData(
                            icon = Icons.Rounded.FilterList,
                            onClick = { isFiltering = !isFiltering }
                        )
                    ))

                SearchBar(
                    query = searchQuery,
                    onQueryChanged = {
                        searchQuery = it
                        MovieRepository.searchMovies(searchQuery)
                    }
                )
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

//class Browser : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        setContent {
//            FlickPickTheme {
//                val navController = rememberNavController()
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MovieBrowser(navController)
//                }
//            }
//        }
//    }
//}