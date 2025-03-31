package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.data.database.model.Movie

@Composable
fun MovieGrid(movies: List<Movie>, navController: NavController) {
    var width by remember { mutableIntStateOf(0) }

    val movieCardWidth = 122.dp
    val itemsPerRow =
        if (width > 0) {
            with(LocalDensity.current) { width.toDp() } / (movieCardWidth + 4.dp)
        } else 1
    val rows = movies.chunked(itemsPerRow.toInt())
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .onSizeChanged { size -> width = size.width },
    ) {
        items(rows) { row ->
            Row(
                modifier = Modifier.fillParentMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                row.forEach { movie ->
                    MovieCard(
                        movie = movie,
                        width = movieCardWidth,
                        onClick = {
                            navController.navigate("movie/${movie.movieID}")
                        }
                    )
                }
            }
        }
    }
}