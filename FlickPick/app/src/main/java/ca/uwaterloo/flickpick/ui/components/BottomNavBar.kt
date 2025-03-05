package ca.uwaterloo.flickpick.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.VideoLibrary
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BottomNavBar(navController : NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = remember { mutableStateOf(navController.currentDestination?.route ?: "") }

    LaunchedEffect(backStackEntry?.destination?.route) {
        currentRoute.value = backStackEntry?.destination?.route ?: ""
    }

    data class NavItem(
        val label: String,
        val route: String,
        val icon: ImageVector
    )

    val items = listOf(
        NavItem("Library", "library", Icons.Rounded.VideoLibrary),
        NavItem("Browse", "browse", Icons.Rounded.Explore),
        NavItem("Picks", "recommend", Icons.Rounded.Movie),
        NavItem("Groups", "groups", Icons.Rounded.Groups),
        NavItem("Profile", "profile", Icons.Rounded.AccountCircle)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute.value == item.route,
                onClick = { navController.navigate(item.route) },
                alwaysShowLabel = true
            )
        }
    }
}

@Preview
@Composable
fun PreviewBottomNavBar() {
    BottomNavBar(rememberNavController())
}