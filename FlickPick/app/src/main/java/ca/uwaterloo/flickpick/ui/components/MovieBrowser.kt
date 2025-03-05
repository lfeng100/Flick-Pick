package ca.uwaterloo.flickpick.ui.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ca.uwaterloo.flickpick.ui.theme.FlickPickTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun MovieBrowser(navController: NavController) {
    val movies by MovieCatalogRepository.movies.collectAsState()
    Scaffold(
        topBar = {
            TopBar("Browse",
                listOf(
                    TopBarButtonData(
                        icon = Icons.Rounded.Search,
                        onClick = {}
                    ),
                    TopBarButtonData(
                        icon = Icons.Rounded.FilterList,
                        onClick = {}
                    )
                ))
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                val rows = movies.chunked(3)
                LazyColumn(
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
        }
    }
}

class Browser : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FlickPickTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieBrowser(navController)
                }
            }
        }
    }
}