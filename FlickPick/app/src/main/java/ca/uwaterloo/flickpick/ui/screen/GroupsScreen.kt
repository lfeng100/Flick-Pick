package ca.uwaterloo.flickpick.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.domain.repository.GroupRepository
import ca.uwaterloo.flickpick.ui.component.JoinGroupCardsList
import ca.uwaterloo.flickpick.ui.component.TitleTopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData
import ca.uwaterloo.flickpick.ui.component.YourGroupCardsList
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun GroupsScreen(navController: NavController) {

    val userID: String? = FirebaseAuth.getInstance().currentUser?.uid

    if (userID == null) {
        Text("Please log in to see groups", modifier = Modifier.padding(16.dp))
        return
    }

    LaunchedEffect(key1 = userID, key2 = Unit) {
        GroupRepository.fetchJoinGroups(userID)
        GroupRepository.fetchYourGroups(userID)
    }

    val joinGroups by GroupRepository.joinGroups.collectAsState()
    val yourGroups by GroupRepository.yourGroups.collectAsState()

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
        val tabTitles = listOf("Your Groups", "Join Groups")
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
        val coroutineScope = rememberCoroutineScope()

        Column (Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            YourGroupCardsList(
                                yourGroups,
                                navController
                            )
                        }
                    }
                    1 -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            JoinGroupCardsList(
                                joinGroups,
                                navController
                            )
                        }
                    }
                }
            }
        }
    }
}

