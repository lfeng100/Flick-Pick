package ca.uwaterloo.flickpick.ui.screen

import MovieRepository
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.ui.component.BackButtonTopBar
import ca.uwaterloo.flickpick.ui.component.MovieCard
import ca.uwaterloo.flickpick.ui.component.MovieGenres
import ca.uwaterloo.flickpick.ui.component.MovieInteractionButtonRow
import ca.uwaterloo.flickpick.ui.component.MovieDetails
import coil.compose.AsyncImage

@Composable
fun MovieInfoScreen(navController: NavController, movieId: String) {
    var targetMovie by remember { mutableStateOf<Movie?>(null) }
    LaunchedEffect(Unit) {
        targetMovie = MovieRepository.getMovieForId(movieId)
    }
    Scaffold (
        topBar = { BackButtonTopBar(navController) }
    ) { padding ->
        targetMovie?.let { movie ->
            val posterUrl = movie.getHighResPosterUrl()
            posterUrl?.let {
                HeroImage(posterUrl)
            }
            Box(Modifier.padding(padding)) {
                MovieInfoLayout(movie)
            }
        }
    }
}

@Composable
fun HeroImage(posterUrl: String) {
    AsyncImage(
        model = posterUrl,
        contentDescription = "Hero Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .graphicsLayer { alpha = 0.40f }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = 0.5f
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
            .blur(6.dp)
    )
}

@Composable
fun MovieInfoLayout(movie: Movie) {
    LazyColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
        items(1) {
            Row (verticalAlignment = Alignment.CenterVertically) {
                MovieCard(
                    movie = movie,
                    width = 130.dp,
                    onClick= { }
                )
                Spacer(modifier = Modifier.width(16.dp))
                MovieDetails(movie)
            }
            MovieInteractionButtonRow(movie)
            Text(text = movie.description ?: "", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(20.dp))
            MovieGenres(movie)
        }
    }
}

//class MovieInfoComponent : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FlickPickTheme {
//                val navController = rememberNavController()
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    MovieInfoScreen(navController, "avengers-endgame")
//                }
//            }
//        }
//    }
//}