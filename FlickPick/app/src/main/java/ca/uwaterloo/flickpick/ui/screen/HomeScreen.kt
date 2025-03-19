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
import ca.uwaterloo.flickpick.domain.manager.PrimaryUserManager
import ca.uwaterloo.flickpick.ui.component.BrowseMovieReminder
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
    val watchedIds by PrimaryUserManager.watched
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
        topBar = { LogoTopBar() }
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
                    0 -> {
                        if (reviewed.isEmpty()) {
                            BrowseMovieReminder(navController, "You don't have any reviews!");
                            return@HorizontalPager;
                        }
                        MovieGrid(reviewed, navController)
                    }
                    1 -> {
                        if (watched.isEmpty()) {
                            BrowseMovieReminder(navController, "Your watched is empty!");
                            return@HorizontalPager;
                        }
                        MovieGrid(watched, navController)
                    }
                    2 -> {
                        if (watchlist.isEmpty()) {
                            BrowseMovieReminder(navController, "Your watchlist is empty!");
                            return@HorizontalPager;
                        }
                        MovieGrid(watchlist, navController)
                    }
                }
            }
        }
    }
}