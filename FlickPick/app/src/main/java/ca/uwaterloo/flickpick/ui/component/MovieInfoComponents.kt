package ca.uwaterloo.flickpick.ui.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.R
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.managers.PrimaryUserManager

@Composable
fun MovieDetails(movie: Movie) {
    Column(Modifier.fillMaxWidth()) {
        val releaseYearStr = movie.releaseYear.toString()
        val ratingStr = if (movie.rating == null) "?" else "${movie.rating}"
        val runtimeStr = if (movie.runtime == null) "?" else "${movie.runtime}"
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$releaseYearStr • $runtimeStr min • ★ $ratingStr",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MovieTitleText(movie: Movie, centered: Boolean = false) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (centered) Alignment.CenterHorizontally else Alignment.Start
    ) {
        val releaseYearStr = movie.releaseYear.toString()
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize)) {
                    append(movie.title)
                    append(" ")
                }
                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                    append(releaseYearStr)
                }
            },
            textAlign = if (centered) TextAlign.Center else TextAlign.Start
        )
    }
}

data class ToggleButtonItem(
    val checkedIcon: ImageVector,
    val uncheckedIcon: ImageVector,
    var checkedTint: Color = Color.White,
    var isChecked : State<Boolean>,
    var onChecked : () -> Unit,
    var onUnchecked : () -> Unit
)

@Composable
fun MovieInteractionButtonRow(navController: NavController, movie: Movie, showTMDBLink: Boolean = true) {
    val isReviewChecked = remember {
        derivedStateOf {
            val ratings by PrimaryUserManager.reviews
            ratings[movie.movieID] != null
        }
    }
    val isWatchedChecked = remember {
        derivedStateOf {
            val watched by PrimaryUserManager.watched
            watched.binarySearch(movie.movieID) >= 0
        }
    }
    val isWatchlistChecked = remember {
        derivedStateOf {
            val watchlist by PrimaryUserManager.watchlist
            watchlist.binarySearch(movie.movieID) >= 0
        }
    }
    val items = listOf(
        ToggleButtonItem(
            checkedIcon = Icons.Filled.Star,
            uncheckedIcon = Icons.Outlined.StarBorder,
            isChecked = isReviewChecked,
            onChecked = {
                navController.navigate("review/${movie.movieID}")
            },
            onUnchecked = { PrimaryUserManager.removeReview(movie.movieID) }
        ),
        ToggleButtonItem(
            checkedIcon = Icons.Filled.Visibility,
            uncheckedIcon = Icons.Outlined.Visibility,
            isChecked = isWatchedChecked,
            onChecked = { PrimaryUserManager.addToWatched(movie.movieID) },
            onUnchecked = { PrimaryUserManager.removeFromWatched(movie.movieID) }
        ),
        ToggleButtonItem(
            checkedIcon = Icons.Filled.WatchLater,
            uncheckedIcon = Icons.Outlined.WatchLater,
            isChecked = isWatchlistChecked,
            onChecked = { PrimaryUserManager.addToWatchlist(movie.movieID) },
            onUnchecked = { PrimaryUserManager.removeFromWatchlist(movie.movieID) }
        )
    )
    Row (
        modifier = Modifier.fillMaxWidth(),
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                MovieInteractionButton(item)
            }
        }
        if (showTMDBLink) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TMDBLinkButton(movie.tmdbId)
            }
        }
    }
}

@Composable
fun MovieInteractionButton(item: ToggleButtonItem) {
    IconToggleButton(
        modifier = Modifier.padding(12.dp),
        checked = item.isChecked.value,
        onCheckedChange = {
            if (it) {
                item.onChecked()
            } else {
                item.onUnchecked()
            }
        },
    ) {
        val icon = if (item.isChecked.value)
            item.checkedIcon else item.uncheckedIcon
        Icon(
            imageVector = icon,
            contentDescription = "Toggle Button",
            modifier = Modifier.size(32.dp),
            tint = if (item.isChecked.value)
                item.checkedTint else Color.Gray
        )
    }
}

@Composable
fun TMDBLinkButton(tmdbId: String?) {
    val context = LocalContext.current
    IconButton(
        modifier = Modifier.padding(12.dp),
        onClick = {
            if (tmdbId != null) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.themoviedb.org/movie/$tmdbId")
                )
                context.startActivity(intent)
            }
        }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.tmdb_icon),
            contentDescription = "Open In TMDB Button",
            modifier = Modifier.size(32.dp),
            tint = Color.Gray
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieGenres(movie: Movie) {
    FlowRow (
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        movie.genres.forEach { genre ->
            Box(
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.inverseSurface),
                        RoundedCornerShape(25)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = genre, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}