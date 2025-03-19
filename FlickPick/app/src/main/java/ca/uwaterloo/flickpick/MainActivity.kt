package ca.uwaterloo.flickpick


import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import ca.uwaterloo.flickpick.ui.component.BottomNavBar
import ca.uwaterloo.flickpick.ui.screen.BrowseScreen
import ca.uwaterloo.flickpick.ui.screen.GroupsScreen
import ca.uwaterloo.flickpick.ui.screen.HomeScreen
import ca.uwaterloo.flickpick.ui.screen.MovieInfoScreen
import ca.uwaterloo.flickpick.ui.screen.ProfileScreen
import ca.uwaterloo.flickpick.ui.screen.RecommendationCarouselScreen
import ca.uwaterloo.flickpick.ui.screen.RecommendationScreen
import ca.uwaterloo.flickpick.ui.screen.ReviewScreen
import ca.uwaterloo.flickpick.ui.theme.FlickPickTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
        if (!mainNavController.popBackStack()) {
            (context as? ComponentActivity)?.finish()
        }
    }
    LaunchedEffect(Unit) {
        PrimaryUserRepository.loadPrimaryUserLists(context)
    }
    val primaryUserIsLoaded by PrimaryUserRepository.isLoaded.collectAsState()
    if (!primaryUserIsLoaded) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp)
            )
        }
        return
    }
    Scaffold(
        bottomBar = { BottomNavBar(mainNavController) }
    ){ padding ->
        NavHost(mainNavController, startDestination = "library", modifier = Modifier.padding(padding)) {
            composable("library") { HomeScreen(mainNavController) }
            composable("browse") { BrowseScreen(mainNavController) }
            composable("recommend") { RecommendationScreen(mainNavController) }
            composable("group") { GroupsScreen(mainNavController) }
            composable("profile") { ProfileScreen(mainNavController) }
            composable("movie/{movieId}") { navBackStackEntry ->
                val movieId = navBackStackEntry.arguments?.getString("movieId")
                if (movieId != null) {
                    MovieInfoScreen(mainNavController, movieId)
                }
            }
            composable("review/{movieId}") { navBackStackEntry ->
                val movieId = navBackStackEntry.arguments?.getString("movieId")
                if (movieId != null) {
                    ReviewScreen(mainNavController, movieId)
                }
            }
            composable("recommend/carousel") {
                RecommendationCarouselScreen(mainNavController)
            }
            composable("group/{groupId}") { navBackStackEntry ->
                val groupId = navBackStackEntry.arguments?.getString("groupId")
                if (groupId != null) {
                    //SCREEN NAME(mainNavController, groupId) // @ARYAN CHANGE SCREEN NAME
                }
            }
        }
    }
}