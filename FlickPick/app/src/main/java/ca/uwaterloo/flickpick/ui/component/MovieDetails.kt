package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MovieDetails(movie: Movie) {
    Column(Modifier.fillMaxWidth()) {
        val releaseYearStr = movie.releaseYear.toString()
        val runtimeStr = if (movie.runtime == null) "" else "${movie.runtime} min"

        Text(text = movie.title, style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(8.dp))
        Text(text = "$releaseYearStr • $runtimeStr", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            movie.genres.forEach { genre ->
                Box(
                    modifier = Modifier
                        .border(BorderStroke(2.dp, MaterialTheme.colorScheme.inverseSurface),
                                RoundedCornerShape(12))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(text = genre, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}