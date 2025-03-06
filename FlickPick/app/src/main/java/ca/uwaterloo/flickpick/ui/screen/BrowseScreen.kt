package ca.uwaterloo.flickpick.ui.screen

import MovieRepository
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.component.MovieCard
import ca.uwaterloo.flickpick.ui.component.PaginatedMovieGrid
import ca.uwaterloo.flickpick.ui.component.TopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun BrowseScreen(navController: NavController) {
    val movies by MovieRepository.movies.collectAsState()
    val isFetching by MovieRepository.isFetching.collectAsState()
    Scaffold(
        topBar = {
            TopBar("Browse",
                listOf(
                    TopBarButtonData(
                        icon = Icons.Rounded.Search,
                        onClick = {}
                    ),
                    TopBarButtonData(
                        icon = Icons.Rounded.FilterList,
                        onClick = {}
                    )
                ))
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            PaginatedMovieGrid(
                movies,
                navController,
                onLoadMore = {
                    if (!isFetching) {
                        MovieRepository.fetchMoreMovies()
                    }
                }
            )
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