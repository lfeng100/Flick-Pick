package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie

@Composable
fun PaginatedMovieGrid(movies: List<Movie>,
                       navController: NavController,
                       onLoadMore: () -> Unit) {
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val total = layoutInfo.totalItemsCount

                if (lastIndex != null && lastIndex >= total - 1) {
                    onLoadMore()
                }
            }
    }
    val rows = movies.chunked(3) // TODO: dynamically set this based on container width
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
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