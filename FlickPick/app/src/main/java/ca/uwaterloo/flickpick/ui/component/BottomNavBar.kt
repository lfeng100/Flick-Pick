package ca.uwaterloo.flickpick.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavBar(navController : NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val screen = navBackStackEntry?.destination?.route

    data class NavItem(
        val label: String,
        val route: String,
        val icon: ImageVector
    )

    val items = listOf(
        NavItem("Library", "library", Icons.Filled.VideoLibrary),
        NavItem("Browse", "browse", Icons.Filled.Explore),
        NavItem("Picks", "recommend", Icons.Filled.Movie),
        NavItem("Groups", "groups", Icons.Filled.Groups),
        NavItem("Profile", "profile", Icons.Filled.AccountCircle)
    )
    AnimatedVisibility(
        visible = items.any { it.route == screen},
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 5.dp
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = screen == item.route,
                    onClick =
                    {
                        navController.navigate(item.route);
                    },
                    alwaysShowLabel = true
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavBar() {
    BottomNavBar(rememberNavController())
}