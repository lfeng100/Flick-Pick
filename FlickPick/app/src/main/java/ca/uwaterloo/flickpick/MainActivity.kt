package ca.uwaterloo.flickpick


import MovieCatalogRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.flickpick.managers.RecommendationManager
import ca.uwaterloo.flickpick.ui.components.BottomNavBar
import ca.uwaterloo.flickpick.ui.components.HomeScreenContent
import ca.uwaterloo.flickpick.ui.components.MovieBrowser
import ca.uwaterloo.flickpick.ui.components.MovieInfoScreen
import ca.uwaterloo.flickpick.ui.components.Profile
import ca.uwaterloo.flickpick.ui.components.RecommendationBrowser
import ca.uwaterloo.flickpick.ui.theme.FlickPickTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Figure out where to put this logic
        MovieCatalogRepository.fetchMoreMovies()

        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    FlickPickTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val loginFlowNavController = rememberNavController()
            val startDestination =
                if (FirebaseAuth.getInstance().currentUser != null) {
                    "home"
                } else {
                    "login"
                }
            NavHost(navController = loginFlowNavController, startDestination = startDestination) {
                composable("login") { Login(loginFlowNavController) }
                composable("signup") { Signup(loginFlowNavController) }
                composable("authenticated") { MainScreen() }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val mainNavController = rememberNavController()
    val context = LocalContext.current
    BackHandler {
        if (mainNavController.previousBackStackEntry != null) {
            mainNavController.popBackStack()
        } else {
            (context as? ComponentActivity)?.moveTaskToBack(true)
        }
    }
    Scaffold(
        bottomBar = { BottomNavBar(mainNavController) }
    ){ padding ->
        NavHost(mainNavController, startDestination = "library", modifier = Modifier.padding(padding)) {
            composable("library") { HomeScreenContent(mainNavController) }
            composable("browse") { MovieBrowser(mainNavController) }
            composable("recommend") { RecommendationBrowser(mainNavController) }
            composable("profile") { Profile(mainNavController) }
            composable("movie") { MovieInfoScreen(mainNavController) }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
