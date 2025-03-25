package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.R
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import coil.imageLoader
import coil.request.ImageRequest
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sign

@Composable
fun MovieCoverFlowCarousel(
    navController: NavController,
    movies: List<Movie>,
    onIndexChanged: (Int) -> Unit,
    onRefreshClicked: () -> Unit
) {
    val listState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(listState)
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                onIndexChanged(listState.firstVisibleItemIndex)
            }
    }
    val context = LocalContext.current
    LaunchedEffect(movies) {
        for (movie in movies) {
            val posterUrl = movie.getPosterUrl()
            posterUrl?.let { url ->
                val request = ImageRequest.Builder(context)
                    .data(url)
                    .build()
                context.imageLoader.enqueue(request)
            }
        }
    }
    var width by remember { mutableIntStateOf(0) }
    val cardWidth =
        if (width > 0) {
            with(LocalDensity.current) { (width / 2.25).toInt().toDp() }
        } else 0.dp
    LazyRow(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = Modifier
            .width(480.dp)
            .onSizeChanged { width = it.width }
    ) {
        item {
            CoverFlowStartItem(listState) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(cardWidth)
                        .height(cardWidth * 1.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier.padding(start = cardWidth / 4),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.height(64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                modifier = Modifier.size(36.dp),
                                painter = painterResource(id = R.drawable.film_reel_icon),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                                contentDescription = "Reel Icon",
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text(
                            text = "Explore your queue",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        itemsIndexed(movies) { index, movie ->
            CoverFlowItem(listState, index + 1) {
                MovieCard(
                    movie = movie,
                    width = cardWidth,
                    onClick = {
                        navController.navigate("movie/${movie.movieID}")
                    }
                )
            }
        }
        item {
            CoverFlowEndItem(listState, movies.size + 1) {
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .width(cardWidth)
                        .height(cardWidth * 1.5f),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier.padding(end = cardWidth / 4),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            modifier = Modifier.height(64.dp),
                            onClick = { onRefreshClicked() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Icon(
                                modifier = Modifier.size(42.dp),
                                imageVector = Icons.Filled.Refresh,
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = "Refresh Icon",
                            )
                        }
                        Text(
                            text = "Refresh your queue",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CoverFlowStartItem(
    listState: LazyListState,
    content: @Composable () -> Unit
) {
    fun startFade(): Float {
        val layoutInfo = listState.layoutInfo
        val currentItem = layoutInfo.visibleItemsInfo.find { it.index == 0 } ?: return 0f
        return 1f - min(1f,-currentItem.offset / 85f)
    }
    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = startFade()
            }
    ) {
        content()
    }
}

@Composable
fun CoverFlowEndItem(
    listState: LazyListState,
    index: Int,
    content: @Composable () -> Unit
) {
    fun endFade(): Float {
        val layoutInfo = listState.layoutInfo
        val currentItem = layoutInfo.visibleItemsInfo.find { it.index == index } ?: return 0f
        val endOffset = currentItem.offset + currentItem.size - listState.layoutInfo.viewportEndOffset
        return 1f - min(1f, endOffset / 85f)
    }
    Box(
        modifier = Modifier
            .graphicsLayer {
                alpha = endFade()
            }
    ) {
        content()
    }
}

@Composable
fun CoverFlowItem(
    listState: LazyListState,
    index: Int,
    content: @Composable () -> Unit
) {
    val offsetRatio = itemOffsetRatio(listState, index)
    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = 1f - (0.45f * abs(offsetRatio))
                scaleY = 1f - (0.45f * abs(offsetRatio))
                rotationY = -offsetRatio * 45f
                translationX = -offsetRatio.sign * (offsetRatio * offsetRatio) * 120f
                alpha = 1f - (0.5f * abs(offsetRatio))
            }
    ) {
        content()
    }
}

fun itemOffsetRatio(listState: LazyListState, index: Int): Float {
    val layoutInfo = listState.layoutInfo
    val currentItem = layoutInfo.visibleItemsInfo.find { it.index == index } ?: return 0f
    val center = layoutInfo.viewportSize.width / 2
    val itemCenter = (currentItem.offset + currentItem.size / 2) + layoutInfo.beforeContentPadding
    return (itemCenter - center).toFloat() / center
}