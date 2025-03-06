package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie

@Composable
fun InfiniteMovieGrid(movies: List<Movie>,
                      navController: NavController,
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
    val movieCardWidth = 126.dp
    val itemsPerRow =
        if (width > 0) with(LocalDensity.current) { width.toDp() } / movieCardWidth else 1
    val rows = movies.chunked(itemsPerRow.toInt())
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { size -> width = size.width }
    ) {
        items(rows) { row ->
            Row(
                modifier = Modifier.fillParentMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                row.forEach { movie ->
                    MovieCard(movie = movie, navController = navController)
                }
            }
        }
    }
}