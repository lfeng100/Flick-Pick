package ca.uwaterloo.flickpick

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration


class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickPickTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreenContent()
                }
            }
        }
    }
}

@Composable
fun HomeScreenCarousel () {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(120.dp))
        Text(text = "Discover Movies", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Check out some of these movie recommendations", style = MaterialTheme.typography.bodyLarge)
        
        // Horizontal Carousel of Movies - TODO: Convert hardcoded movies to a list of movies from API
        val movies = listOf("The Shawshank Redemption", "The Godfather", "The Dark Knight", "Pulp Fiction", "Forrest Gump")
        LazyRow(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(movies) { movie ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .width(200.dp)
                        .height(200.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Movie of the day", style = MaterialTheme.typography.headlineMedium)
                        Text(text = movie, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenTitle () {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                .background(Color(0xFF0080FF))
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "FlickPick",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
                .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun HomeScreenContent() {
    HomeScreenTitle();
    HomeScreenCarousel();
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    FlickPickTheme {
        HomeScreenContent()
    }
}