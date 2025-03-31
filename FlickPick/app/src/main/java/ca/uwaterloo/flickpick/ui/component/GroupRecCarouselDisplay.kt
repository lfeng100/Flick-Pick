package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import ca.uwaterloo.flickpick.data.database.model.Movie
import ca.uwaterloo.flickpick.domain.repository.GroupRecommendationRepository
import ca.uwaterloo.flickpick.ui.screen.HeroImage
import coil.imageLoader
import coil.request.ImageRequest

@Composable
fun GroupRecCarouselDisplay(groupID: String, navController: NavController) {
    var showFilterCustomizer by remember { mutableStateOf(false) }
    val filters by GroupRecommendationRepository.filters
    val recommendations by GroupRecommendationRepository.recommendations.collectAsState()
    var targetMovie by remember { mutableStateOf<Movie?>(null) }
    LaunchedEffect(Unit) {
        GroupRecommendationRepository.clearPreviousRecommendations()
        GroupRecommendationRepository.fetchGroupRecommendations(groupID)
    }
    val context = LocalContext.current
    LaunchedEffect(recommendations) {
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
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        targetMovie?.getHighResPosterUrl()?.let { url ->
            HeroImage(url)
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.85f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (recommendations.isNotEmpty()) {
                    MovieCoverFlowCarousel(
                        navController = navController,
                        movies = recommendations,
                        onIndexChanged = { index ->
                            val i = index.coerceIn(0, 11)
                            targetMovie = recommendations[i]
                        },
                        onRefreshClicked = {
                            // Refresh personal recommendations by clearing and re-fetching
                            GroupRecommendationRepository.clearGroupRecommendations()
                            GroupRecommendationRepository.fetchGroupRecommendations(groupID)
                        }
                    )
                    targetMovie?.let { movie ->
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Box(Modifier.height(90.dp), Alignment.Center) {
                                MovieTitleText(movie, true)
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            MovieInteractionButtonRow(navController, movie, false)
                        }
                    }
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
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { showFilterCustomizer = true }
                ) {
                    Row(
                        modifier = Modifier.width(160.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Psychology,
                            contentDescription = "Preference Button"
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(text = if (filters == null) "Set preferences" else "Update preferences")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
    if (showFilterCustomizer) {
        FiltersCustomizer(initial = filters) { f ->
            showFilterCustomizer = false
            GroupRecommendationRepository.setFilters(f)
            GroupRecommendationRepository.clearGroupRecommendations()
            GroupRecommendationRepository.fetchGroupRecommendations(groupID)
        }
    }
}
