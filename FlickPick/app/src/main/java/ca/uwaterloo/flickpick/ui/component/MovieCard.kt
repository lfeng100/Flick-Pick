package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.data.database.model.Movie
import coil.compose.AsyncImage

@Composable
fun MovieCard(movie: Movie, width: Dp, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .width(width)
            .height(width * 1.5f),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box() {
            val posterUrl = movie.getPosterUrl()
            posterUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = "Movie Poster",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onClick?.invoke() },
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black),
                                startY = 300f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}