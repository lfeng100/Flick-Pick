package ca.uwaterloo.flickpick.ui.component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import ca.uwaterloo.flickpick.dataObjects.Database.Models.ActivityItem

@Composable
fun GroupActivityList(
    activityList: List<ActivityItem>,
    userMap: Map<String, String>,
    navController: NavController
){
    val listState = rememberLazyListState()
    var visibleCount by remember { mutableStateOf(5) }
    val movieCache = remember { mutableStateMapOf<String, Movie?>() }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                lastVisibleIndex?.let { index ->
                    if (index >= visibleCount - 2 && visibleCount < activityList.size) {
                        visibleCount += 5
                    }
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(activityList.take(visibleCount)) { activity ->
            val movie = movieCache[activity.movieID]

            if (movie == null && !movieCache.containsKey(activity.movieID)) {
                LaunchedEffect(activity.movieID) {
                    try {
                        val fetched = DatabaseClient.apiService.getMovieById(activity.movieID)
                        movieCache[activity.movieID] = fetched
                    } catch (e: Exception) {
                        Log.e("GroupActivityList", "Failed to fetch movie ${activity.movieID}: ${e.message}")
                        movieCache[activity.movieID] = null
                    }
                }
            }

            movie?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MovieCard(
                        movie = it,
                        width = 100.dp,
                        onClick = { navController.navigate("movie/${it.movieID}") }
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {


                        // activity description
                        val username = userMap[activity.userID] ?: "Someone"
                        val activityDescription = when (activity.type) {
                            "review" -> "$username reviewed ${activity.movieTitle}\n\n${activity.message}"
                            "watched" -> "$username watched ${activity.movieTitle}"
                            "watchlist" -> "$username added ${activity.movieTitle} to watchlist"
                            else -> "$username did some activity with “${activity.movieTitle}”"
                        }

                        Text(
                            text = activityDescription ,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // starts
                        if (activity.type == "review") {
                            activity.rating?.let { rating ->
                                ReadOnlyStarRatingBar(
                                    rating = rating,
                                )
                            }
                        }

                        Text(
                            text = activity.formatTimestamp(),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
                Divider()
            }
        }
    }
}

@Composable
fun ReadOnlyStarRatingBar(rating: Float, modifier: Modifier = Modifier) {

    Row(modifier = modifier.padding(top = 4.dp)) {
        repeat(5) { index ->
            val starPosition = index + 1
            val icon = when {
                rating >= starPosition -> Icons.Rounded.Star
                rating >= starPosition - 0.5 -> Icons.AutoMirrored.Rounded.StarHalf
                else -> Icons.Rounded.StarBorder
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .size(20.dp)
            )
        }
    }
}