package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.component.GroupCardsList
import ca.uwaterloo.flickpick.ui.component.TitleTopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData

@Composable
fun GroupsScreen(navController: NavController) {
    val groups by GroupRepository.groups.collectAsState()

    Scaffold(
        topBar = {
            TitleTopBar("Groups",
                listOf(
                    TopBarButtonData(
                        icon = Icons.Rounded.Search,
                        onClick = {}
                    ),
                    TopBarButtonData(
                        icon = Icons.Rounded.AddCircleOutline,
                        onClick = {navController.navigate("create_group")}
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
            GroupCardsList(
                groups,
                navController
            )
        }
    }
}

