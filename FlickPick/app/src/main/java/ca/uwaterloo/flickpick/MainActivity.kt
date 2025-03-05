package ca.uwaterloo.flickpick


import android.os.Build
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ca.uwaterloo.flickpick.ui.components.BottomNavBar
import ca.uwaterloo.flickpick.ui.theme.FlickPickTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    ){ paddingValues ->
        NavHost(mainNavController, startDestination = "home", modifier = Modifier.padding(paddingValues)) {
            composable("home") { HomeScreenContent(mainNavController) }
            composable("library") { MovieLibrary(mainNavController) }
            composable("movie") { MovieInfoScreen(mainNavController) }
            composable("profile") { Profile(mainNavController) }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    App()
}
