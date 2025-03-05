package ca.uwaterloo.flickpick.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import coil.compose.AsyncImage

@Composable
fun MovieCard(movie: Movie, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .width(122.dp)
            .height(192.dp),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box() {
            movie.getPosterUrl()?.let { posterUrl ->
                AsyncImage(
                    model = posterUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { navController.navigate("movie") },
                    contentScale = ContentScale.Crop
                )
            }
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(Color.Transparent, Color.Black),
//                            startY = 300f
//                        )
//                    )
//            )
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomStart)
//                    .padding(16.dp)
//            ) {
//                Text(text = movie.title, style = MaterialTheme.typography.bodyLarge, color = Color.White)
//            }
        }
    }
}