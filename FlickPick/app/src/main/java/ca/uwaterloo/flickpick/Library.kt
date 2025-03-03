package ca.uwaterloo.flickpick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ca.uwaterloo.flickpick.ui.theme.FlickPickTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class Library : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickPickTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieLibrary(navController)
                }
            }
        }
    }
}

data class MovieData(val title: String, val imageUrl: String)

@Composable
fun MovieLibrary(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Movie Library", style = MaterialTheme.typography.headlineLarge, color = Color.White)
        Text(text = "Explore the collection of movies", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(20.dp))

        val movies = listOf(
            MovieData("Jumanji", "jumanji"),
            MovieData("The Godfather", "godfather"),
            MovieData("The Dark Knight", "dark_knight"),
            MovieData("Fast and Furious", "fast_furious"),
            MovieData("Avengers", "avengers"),
            MovieData("Inception", "inception"),
            MovieData("Titanic", "titanic"),
            MovieData("Avatar", "avatar"),
            MovieData("Interstellar", "interstellar"),
            MovieData("Gladiator", "gladiator"),
            MovieData("Jurassic Park", "jurassic_park"),
            MovieData("The Matrix", "matrix"),
            MovieData("Interstellar", "interstellar"),
            MovieData("Gladiator", "gladiator"),
            MovieData("Jurassic Park", "jurassic_park"),
            MovieData("The Matrix", "matrix")
        )

        val rows = movies.chunked(3)

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(rows) { row ->
                Row(
                    modifier = Modifier.fillParentMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    row.forEach { movie ->
                        ReusableMovieCard(movie = movie.title.toString(), navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ReusableMovieCard(movie: String, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(110.dp)
            .height(180.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.avengers),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clickable { navController.navigate("MovieInfo") },
                contentScale = ContentScale.Crop

            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 300f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(text = movie, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            }
        }
    }
}