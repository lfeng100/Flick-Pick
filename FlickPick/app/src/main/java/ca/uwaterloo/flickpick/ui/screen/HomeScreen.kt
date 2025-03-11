package ca.uwaterloo.flickpick.ui.screen

import MovieRepository
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.managers.PrimaryUserManager
import ca.uwaterloo.flickpick.ui.component.LogoTopBar
import ca.uwaterloo.flickpick.ui.component.MovieGrid
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val reviewedIds by remember {
        derivedStateOf {
            PrimaryUserManager.watched.value.filter {
                PrimaryUserManager.reviews.value.containsKey(it)
            }
        }
    }
    val watchedIds by remember {
        derivedStateOf {
            PrimaryUserManager.watched.value.filter {
                !PrimaryUserManager.reviews.value.containsKey(it)
            }
        }
    }
    val watchlistIds by PrimaryUserManager.watchlist

    var reviewed by remember { mutableStateOf(emptyList<Movie>()) }
    var watched by remember { mutableStateOf(emptyList<Movie>()) }
    var watchlist by remember { mutableStateOf(emptyList<Movie>()) }

    LaunchedEffect(reviewedIds, watchedIds, watchlist) {
        reviewed = reviewedIds.mapNotNull {
            MovieRepository.getMovieForId(it)
        }
        watched = watchedIds.mapNotNull {
            MovieRepository.getMovieForId(it)
        }
        watchlist = watchlistIds.mapNotNull {
            MovieRepository.getMovieForId(it)
        }
    }
    Scaffold(
        topBar = { LogoTopBar(emptyList()) }
    ) { padding ->
        val tabTitles = listOf("Reviewed", "Watched", "Watchlist")
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
        val coroutineScope = rememberCoroutineScope()

        Column (Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> MovieGrid(reviewed, navController)
                    1 -> MovieGrid(watched, navController)
                    2 -> MovieGrid(watchlist, navController)
                }
            }
        }
    }
}

//@Composable
//fun MovieCarousel(navController: NavController, title: String, movies: List<Movie>) {
//    Column(modifier = Modifier.fillMaxWidth().padding(top=6.dp, bottom =6.dp, start=16.dp, end=16.dp)) {
//        Text(
//            text = title,
//            style = MaterialTheme.typography.titleLarge
//        )
//        LazyRow(modifier = Modifier.height(150.dp)) {
//            items(movies) { movie ->
//                MovieCard(
//                    movie = movie,
//                    width = 100.dp,
//                    onClick = {
//                        navController.navigate("movie/${movie.movieID}")
//                    }
//                )
//            }
//        }
//    }
//}

