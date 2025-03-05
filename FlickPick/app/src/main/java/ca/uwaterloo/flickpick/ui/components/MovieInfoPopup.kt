package ca.uwaterloo.flickpick.ui.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ca.uwaterloo.flickpick.ui.theme.FlickPickTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.flickpick.R
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.managers.MovieInfoPopupManager
import ca.uwaterloo.flickpick.managers.PrimaryUserManager

@Composable
fun MovieInfoScreen(navController: NavController) {
    val movie by MovieInfoPopupManager.selectedMovie.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        HeroImage()
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(10.dp)
                .padding(bottom = 50.dp)
        ) {
            // TODO: Replace with actual movie data (this is dummy hardcoded data for now)
            MovieInfoCard(movie)
        }
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
        IconButton(
            onClick = { navController.navigate("profile") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun HeroImage() {
    Image(
        painter = painterResource(id = R.drawable.avengers),
        contentDescription = "Hero Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun MovieInfoCard(movie: Movie?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0f)),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.9f)),
        shape = RoundedCornerShape(36.dp)
    ) {
        Column(modifier = Modifier.padding(35.dp)) {
            MovieDetails(movie)
            Spacer(modifier = Modifier.height(30.dp))
            Text(text = movie?.description ?: "", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { /* TODO: Add Watch Later functionality */ },
                modifier = Modifier
                            .height(42.dp),
                        shape = RoundedCornerShape(10.dp),
                        //colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Watch Later")
                }
                Button(onClick = { /* TODO: Add Share functionality */ },
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    //colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Share")
                }
                var isRated by remember { mutableStateOf(PrimaryUserManager.containsRatingForMovie(movie?.movieID ?: "")) }
                Button(onClick =
                {
                    if (movie != null) {
                        PrimaryUserManager.addRating(movie.movieID, 5.0f)
                        isRated = true
                    }
                },
                    modifier = Modifier
                        .height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    //colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = if (isRated) "Liked!" else "Like")
                }
            }
        }
    }
}

class MovieInfoComponent : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickPickTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieInfoScreen(navController)
                }
            }
        }
    }
}