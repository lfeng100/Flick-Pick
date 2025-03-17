package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.domain.manager.RecommendationRepository
import ca.uwaterloo.flickpick.ui.component.BackButtonTopBar
import ca.uwaterloo.flickpick.ui.component.MovieCoverFlowCarousel
import ca.uwaterloo.flickpick.ui.component.MovieInteractionButtonRow
import ca.uwaterloo.flickpick.ui.component.MovieTitleText
import coil.imageLoader
import coil.request.ImageRequest

@Composable
fun RecommendationCarouselScreen(navController: NavController) {
    val recommendations by RecommendationRepository.recommendations.collectAsState()
    var targetMovie by remember { mutableStateOf<Movie?>(null) }
    LaunchedEffect(Unit) {
        RecommendationRepository.fetchPersonalRecommendations()
    }
    val context = LocalContext.current
    LaunchedEffect(recommendations) {
        // Preload hero images
        for (movie in recommendations) {
            val highResPosterUrl = movie.getHighResPosterUrl()
            highResPosterUrl?.let { url ->
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .build()
                context.imageLoader.enqueue(request)
            }
        }
    }
    Scaffold (
        topBar = { BackButtonTopBar(navController) }
    ) { padding ->
        targetMovie?.getHighResPosterUrl()?.let { url ->
            HeroImage(url)
        }
        Column (
            modifier = Modifier
                .padding(padding)
                .padding(top = 150.dp)
                .fillMaxSize(),
        ) {
            if (recommendations.isNotEmpty()) {
                MovieCoverFlowCarousel(
                    navController = navController,
                    movies = recommendations,
                    onIndexChanged = { index ->
                        targetMovie = recommendations[index]
                    },
                    onRefreshClicked = {
                        // Refresh personal recommendations by clearing and re-fetching
                        RecommendationRepository.clearPersonalRecommendation()
                        RecommendationRepository.fetchPersonalRecommendations()
                    }
                )
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp * 1.5f)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            targetMovie?.let { movie ->
                Spacer(modifier = Modifier.height(32.dp))
                Column (
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Box(Modifier.height(90.dp), Alignment.Center) {
                        MovieTitleText(movie, true)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    MovieInteractionButtonRow(navController, movie, false)
                }
            }
        }
    }
}