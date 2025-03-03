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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


@Composable
fun HomeScreenCarousel (navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Discover Movies", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Check out some of these movie recommendations", style = MaterialTheme.typography.bodyLarge)
        
        // Horizontal Carousel of Movies - TODO: Convert hardcoded movies to a list of movies from API
        val movies = listOf("Jumanji", "The Godfather", "The Dark Knight", "Fast and Furious", "Avengers")
        LazyRow(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(movies) { movie ->
                ReusableMovieCard(movie = movie, imageUrl = "avengers", navController = navController)
            }
        }
    }
}

@Composable
fun ReusableMovieCard(movie: String, imageUrl: String, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(200.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box() {
            Image(
                painter = painterResource(id = R.drawable.avengers),
                contentDescription = null,
                modifier = Modifier.fillMaxSize().clickable { navController.navigate("library") },
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

@Composable
fun HomeScreenRecentlyWatched(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Recently Watched", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Rewatch some of your favourite movies", style = MaterialTheme.typography.bodyLarge)

        // Horizontal Carousel of Movies - TODO: Convert hardcoded movies to a list of movies from API
        val movies = listOf("Jumanji", "The Godfather", "The Dark Knight", "Fast and Furious", "Avengers")
        LazyRow(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(movies) { movie ->
                ReusableMovieCard(movie = movie, imageUrl = "", navController = navController)
            }
        }
    }
}

@Composable
fun HomeScreenTrending(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Trending in Groups", style = MaterialTheme.typography.headlineLarge)
        Text(text = "Your friends have been binge watching these", style = MaterialTheme.typography.bodyLarge)

        // Horizontal Carousel of Movies - TODO: Convert hardcoded movies to a list of movies from API
        val movies = listOf("Jumanji", "The Godfather", "The Dark Knight", "Fast and Furious", "Avengers")
        LazyRow(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(movies) { movie ->
                ReusableMovieCard(movie = movie, imageUrl = "", navController = navController)
            }
        }
    }
}

@Composable
fun HomeScreenTitle (navController: NavController) {
    val firebaseAuthentication = FirebaseAuthentication()
    Button(
        onClick = {
            firebaseAuthentication.signOut(navController)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red
        )
    ) {
        Text("Temp Logout", color = Color.White)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp))
        ) {
            Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
    modifier = Modifier.fillMaxWidth().padding(16.dp)
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.jumanjiimage),
            contentDescription = "Logo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    Text(
        text = "FlickPick",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(start = 16.dp)
    )
                Box(Modifier.fillMaxWidth())
                {
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
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 50.dp,
                        bottomEnd = 50.dp,
                        topStart = 50.dp,
                        topEnd = 50.dp
                    )
                )
                .background(Color(0xFFFFFFF))
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.jumanjiimage),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun HomeScreenContent(navController: NavController) {
    LazyColumn {
        item {
            HomeScreenTitle(navController = navController)
        }
        item {
            HomeScreenCarousel(navController = navController)
        }
        item {
            HomeScreenRecentlyWatched(navController = navController)
        }
        item {
            HomeScreenTrending(navController = navController)
        }
    }

}

