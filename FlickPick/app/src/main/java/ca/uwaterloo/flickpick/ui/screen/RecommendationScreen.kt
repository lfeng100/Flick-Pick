package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.managers.RecommendationRepository
import ca.uwaterloo.flickpick.ui.component.MovieCard
import ca.uwaterloo.flickpick.ui.component.TopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun RecommendationScreen(navController: NavController) {
    val recommendations by RecommendationRepository.recommendations.collectAsState()
    Scaffold(
        topBar = {
            TopBar("Your Picks",
                listOf(
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
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                val rows = recommendations.chunked(3)
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(rows) { row ->
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            row.forEach { movie ->
                                MovieCard(movie = movie, navController = navController)
                            }
                        }
                    }
                }
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            MovieDetails()
//        }
    }
}