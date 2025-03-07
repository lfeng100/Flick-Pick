package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie

@Composable
fun MovieDetails(movie: Movie) {
    Column(Modifier.fillMaxWidth()) {
        val releaseYearStr = movie.releaseYear.toString()
        val ratingStr = if (movie.rating == null) "?" else "★ ${movie.rating}"
        val runtimeStr = if (movie.runtime == null) "?" else "${movie.runtime} min"
        Text(text = movie.title, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$releaseYearStr • $runtimeStr • $ratingStr",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun MovieInteractButtons(movie : Movie) {
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            modifier = Modifier.padding(12.dp),
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Outlined.RemoveRedEye,
                contentDescription = "Watch Icon",
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.width(50.dp))
        IconButton(
            modifier = Modifier.padding(12.dp),
            onClick = { }
        ) {
            Icon(
                imageVector = Icons.Outlined.WatchLater,
                contentDescription = "Watch Later Icon",
                modifier = Modifier.size(28.dp)
            )
            Box(modifier = Modifier.offset(10.dp, 10.dp)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(MaterialTheme.colorScheme.background, shape = CircleShape)
                        .padding(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "Mini Plus Icon",
                        modifier = Modifier.size(12.dp),
                    )
                }
            }
        }
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
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.inverseSurface),
                        RoundedCornerShape(25))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = genre, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}