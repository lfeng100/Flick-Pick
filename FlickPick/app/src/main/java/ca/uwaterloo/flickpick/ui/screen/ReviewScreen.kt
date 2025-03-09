package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.managers.PrimaryUserManager
import ca.uwaterloo.flickpick.ui.component.BackButtonTopBar
import ca.uwaterloo.flickpick.ui.component.MovieTitleText
import ca.uwaterloo.flickpick.ui.component.StarRatingBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun ReviewScreen(navController: NavController, movieId: String) {
    var targetMovie by remember { mutableStateOf<Movie?>(null) }
    LaunchedEffect(Unit) {
        targetMovie = MovieRepository.getMovieForId(movieId)
    }
    var score by remember {
        mutableFloatStateOf(
            PrimaryUserManager.reviews.value[movieId]?.score ?: 0f
        )
    }
    var message by remember { mutableStateOf("") }
    Scaffold (
        topBar = {
            BackButtonTopBar(
                navController = navController,
                backButtonIcon = Icons.Filled.Close,
                buttons = listOf(
                    TopBarButtonData(
                        Icons.Filled.Check,
                        {
                            PrimaryUserManager.addReview(movieId, score, message)
                            navController.popBackStack()
                        }
                    )
                ))
        }
    ) { padding ->
        targetMovie?.let { movie ->
            Box(modifier = Modifier
                .padding(padding)
                .padding(start = 20.dp, end = 20.dp),
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    MovieTitleText(movie)
                    Spacer(Modifier.height(20.dp))
                    StarRatingBar(score, { score = it })
                    Spacer(Modifier.height(20.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Add review") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}