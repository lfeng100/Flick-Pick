package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.component.GroupCardsList
import ca.uwaterloo.flickpick.ui.component.TopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun GroupsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar("Groups",
                listOf(
                    TopBarButtonData(
                        icon = Icons.Rounded.Search,
                        onClick = {}
                    ),
                    TopBarButtonData(
                        icon = Icons.Rounded.AddCircleOutline,
                        onClick = {}
                    )
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            GroupCardsList(navController)
        }
    }
}

