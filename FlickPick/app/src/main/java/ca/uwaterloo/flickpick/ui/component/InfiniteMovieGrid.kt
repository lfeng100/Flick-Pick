package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.data.database.model.Movie

@Composable
fun InfiniteMovieGrid(movies: List<Movie>,
                      navController: NavController,
                      isFetching: Boolean,
                      onLoadMore: () -> Unit) {
    val listState = rememberLazyListState()
    var width by remember { mutableIntStateOf(0) }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val total = layoutInfo.totalItemsCount

                if (lastIndex == null || lastIndex >= total - 1) {
                    onLoadMore()
                }
            }
    }
    val itemsPerRow = 3
    val movieCardWidth =
        if (width > 0) {
            with(LocalDensity.current) { (width.toDp() - 12.dp) / itemsPerRow }
        } else 0.dp
    val rows = movies.chunked(itemsPerRow)
    if (movies.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (isFetching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp)
                )
            } else {
                Text(
                    text = "No movies found",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.75f)
                )
            }
        }
    } else {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start=16.dp, end=16.dp, bottom=16.dp)
                .onSizeChanged { size -> width = size.width }
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
}