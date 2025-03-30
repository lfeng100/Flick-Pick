package ca.uwaterloo.flickpick.ui.screen

import MovieRepository
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.domain.repository.MovieReviewsRepository
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import ca.uwaterloo.flickpick.domain.repository.ReviewWithUser
import ca.uwaterloo.flickpick.ui.component.BackButtonTopBar
import ca.uwaterloo.flickpick.ui.component.MovieCard
import ca.uwaterloo.flickpick.ui.component.MovieGenres
import ca.uwaterloo.flickpick.ui.component.MovieInteractionButtonRow
import ca.uwaterloo.flickpick.ui.component.MovieDetails
import ca.uwaterloo.flickpick.ui.component.ReadOnlyStarRatingBar
import coil.compose.AsyncImage

@Composable
fun MovieInfoScreen(navController: NavController, movieId: String) {
    var targetMovie by remember { mutableStateOf<Movie?>(null) }
    LaunchedEffect(Unit) {
        targetMovie = MovieRepository.getMovieForId(movieId)
    }
    Scaffold (
        topBar = { BackButtonTopBar(navController) }
    ) { padding ->
        targetMovie?.let { movie ->
            val posterUrl = movie.getHighResPosterUrl()
            posterUrl?.let {
                HeroImage(posterUrl)
            }
            Box(Modifier.padding(padding)) {
                MovieInfoLayout(navController, movie)
            }
        }
    }
}

@Composable
fun HeroImage(posterUrl: String) {
    AsyncImage(
        model = posterUrl,
        contentDescription = "Hero Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .graphicsLayer { alpha = 0.40f }
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black, Color.Transparent),
                        startY = 0.5f
                    ),
                    blendMode = BlendMode.DstIn
                )
            }
            .blur(6.dp)
    )
}

@Composable
fun MovieInfoLayout(navController: NavController, movie: Movie) {
    val reviewData = remember { MovieReviewsRepository.getReviewData(movie.movieID) }
    val listState = rememberLazyListState()
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index
                val total = layoutInfo.totalItemsCount
                if (lastIndex == null || lastIndex >= total - 1) {
                   reviewData.fetchMoreReviews()
                }
            }
    }
    LazyColumn(
        state = listState,
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row (verticalAlignment = Alignment.CenterVertically) {
                    MovieCard(
                        movie = movie,
                        width = 130.dp,
                        onClick= { }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    MovieDetails(movie)
                }
                MovieInteractionButtonRow(navController, movie)
                Text(text = movie.description ?: "", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(20.dp))
                MovieGenres(movie)
            }
        }
        item {
            YourReview(navController, movie.movieID)
        }
        item {
            ReviewColumn(movie.movieID)
        }
    }
}

@Composable
fun YourReview(navController: NavController, movieID: String) {
    val review by remember { derivedStateOf { PrimaryUserRepository.reviews.value[movieID] } }
    Spacer(Modifier.height(24.dp))
    Divider()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (review != null) {
                Text(
                    text = "Your Review",
                    style = MaterialTheme.typography.titleMedium
                )
                ReadOnlyStarRatingBar(review!!.score)
            } else {
                Text(
                    text = "Add A Review",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = { navController.navigate("review/${movieID}") }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Rate Movie"
                    )
                }
            }
        }
        if (review != null && review!!.message != null && review!!.message != "") {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = review!!.message!!)
        }
    }
}

@Composable
fun ReviewColumn(movieID: String) {
    val reviewData = remember { MovieReviewsRepository.getReviewData(movieID) }
    val reviews by reviewData.reviewsList
    val isFetching by reviewData.isFetching.collectAsState()
    Spacer(Modifier.height(24.dp))
    Divider()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "User Reviews",
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh Reviews"
                )
            }
        }
        for (reviewWithUser in reviews) {
            val review = reviewWithUser.review
            val user = reviewWithUser.user
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(user.username)
                    ReadOnlyStarRatingBar(review.rating.toFloat())
                }
                Text(
                    text = review.formatTimestamp(),
                    color = MaterialTheme.colorScheme.onBackground.copy(0.75f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                review.message?.let { Text(text = it) }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(Modifier.height(32.dp))
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (isFetching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(36.dp)
                )
            } else if (reviews.isEmpty()) {
                Text(
                    text = "No reviews found",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                )
            }
        }
    }
}