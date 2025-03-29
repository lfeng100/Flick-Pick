package ca.uwaterloo.flickpick.ui.component


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import java.time.LocalDateTime
import androidx.compose.runtime.LaunchedEffect
import java.time.format.DateTimeFormatter

@Composable
fun GroupActivityList(
    activityList: List<Map<String, Any>>,
    navController: NavController
) {
    val movieCache = remember { mutableStateMapOf<String, Movie>() }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(activityList) { activity ->
            val movieID = activity["movieID"] as? String ?: return@items
            val type = activity["type"] as? String ?: return@items
            val movieTitle = activity["movieTitle"] as? String ?: "Unknown"

            val cachedMovie = movieCache[movieID]
            val movieState = remember { mutableStateOf<Movie?>(cachedMovie) }

            LaunchedEffect(movieID) {
                if (cachedMovie == null) {
                    try {
                        val movie = DatabaseClient.apiService.getMovieById(movieID)
                        movieCache[movieID] = movie
                        movieState.value = movie
                    } catch (e: Exception) {
                        Log.e("GroupActivityList", "Error loading movie $movieID: ${e.message}")
                    }
                }
            }

            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                movieState.value?.let { movie ->
                    MovieCard(
                        movie = movie,
                        width = 120.dp,
                        onClick = { navController.navigate("movie/${movie.movieID}") }
                    )
                }

                val message = when (type) {
                    "review" -> {
                        val review = activity["message"] as? String ?: ""
                        "Reviewed \"$movieTitle\": $review"
                    }
                    "watched" -> "Watched \"$movieTitle\""
                    "watchlist" -> "Added \"$movieTitle\" to watchlist"
                    else -> "Did something with \"$movieTitle\""
                }

                Text(
                    text = message,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}



@Composable
fun GroupActivityCard(
    title: String,
    timestamp: String,
    posterPath: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            if (posterPath != null) {
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w200/$posterPath",
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = formatTimestamp(timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

fun formatTimestamp(raw: String): String {
    return try {
        val parsed = LocalDateTime.parse(raw)
        parsed.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    } catch (e: Exception) {
        raw
    }
}