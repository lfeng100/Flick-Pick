package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.R
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import ca.uwaterloo.flickpick.domain.repository.RecommendationRepository
import ca.uwaterloo.flickpick.ui.component.BrowseMovieReminder
import ca.uwaterloo.flickpick.ui.component.FiltersCustomizer
import ca.uwaterloo.flickpick.ui.component.TitleTopBar

@Composable
fun RecommendationScreen(navController: NavController) {
    Scaffold(
        topBar = { TitleTopBar("Your Picks") }
    ) { padding ->
        var showFilterCustomizer by remember { mutableStateOf(false) }
        val filters by RecommendationRepository.filters
        if (PrimaryUserRepository.reviews.value.isEmpty()) {
            Spacer(modifier = Modifier.padding(padding))
            BrowseMovieReminder(navController, "Add a review to get\nrecommendations!");
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(180.dp),
                // TODO: attribution to WR Graphic Garage from Noun Project
                painter = painterResource(id = R.drawable.movie_popcorn_graphic),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                contentDescription = "Watch Movie Icon",
                contentScale = ContentScale.Fit
            )
            Button(
                onClick = { navController.navigate("recommend/carousel") }
            ) {
                Row(
                    modifier = Modifier.width(160.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Star Button"
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = "Show me my picks")
                }
            }
            Spacer(Modifier.height(20.dp))
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
        }
        if (showFilterCustomizer) {
            FiltersCustomizer(initial = filters) { f ->
                showFilterCustomizer = false
                RecommendationRepository.setFilters(f)
                RecommendationRepository.clearPersonalRecommendations()
            }
        }
    }
}