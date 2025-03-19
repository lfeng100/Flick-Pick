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
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import ca.uwaterloo.flickpick.ui.component.BrowseMovieReminder
import ca.uwaterloo.flickpick.ui.component.LogoTopBar
import ca.uwaterloo.flickpick.ui.component.MovieGrid
import kotlinx.coroutines.launch

@Composable
fun GroupMainScreen(navController: NavController) {

    var reviewed by remember { mutableStateOf(emptyList<Movie>()) }
    var recommendation by remember { mutableStateOf(emptyList<Movie>()) }


    Scaffold(
        topBar = { LogoTopBar() }
    ) { padding ->
        val tabTitles = listOf("Members", "Recommendations")
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
                            BrowseMovieReminder(navController, "No members here! Come back soon");
                            return@HorizontalPager;
                        }
                        MovieGrid(reviewed, navController)
                    }
                    1 -> {
                        if (recommendation.isEmpty()) {
                            BrowseMovieReminder(navController, "You don't have any recommendations yet!");
                            return@HorizontalPager;
                        }
                        MovieGrid(recommendation, navController)
                    }
                }
            }
        }
    }
}