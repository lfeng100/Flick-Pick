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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.managers.RecommendationRepository
import ca.uwaterloo.flickpick.ui.component.MovieCard
import ca.uwaterloo.flickpick.ui.component.MovieCoverFlowCarousel
import ca.uwaterloo.flickpick.ui.component.MovieGrid
import ca.uwaterloo.flickpick.ui.component.TopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun RecommendationScreen(navController: NavController) {
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
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { navController.navigate("recommend/carousel") }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star Button"
                )
            }
        }
    }
}