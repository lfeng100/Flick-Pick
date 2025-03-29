package ca.uwaterloo.flickpick.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import ca.uwaterloo.flickpick.ui.component.BrowseMovieReminder
import ca.uwaterloo.flickpick.ui.component.GroupActivityList
import ca.uwaterloo.flickpick.ui.component.GroupRecCarouselDisplay
import ca.uwaterloo.flickpick.ui.component.UserCard
import ca.uwaterloo.flickpick.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMainScreen(navController: NavController, groupId: String) {

    val group = remember { mutableStateOf<Group?>(null) }
    val members = remember { mutableStateOf(emptyList<User>()) }
    val activities = remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val groupResponse = DatabaseClient.apiService.getGroupById(groupId)
            group.value = groupResponse

            val userResponse = DatabaseClient.apiService.getGroupUsersById(groupId).items
            members.value = userResponse

            val activityResponse = DatabaseClient.apiService.getGroupActivity(groupId)
            activities.value = activityResponse["activity"] as? List<Map<String, Any>> ?: emptyList()

        } catch (e: Exception) {
            Log.e("GroupMainScreen", "Error: ${e.message}")
        } finally {
            isLoading.value = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(group.value?.groupName ?: "Group") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        val tabTitles = listOf("Members", "Recommendations", "Activities")
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
        val coroutineScope = rememberCoroutineScope()

        Column(Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title, maxLines = 1) },
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
                        if (members.value.isEmpty()) {
                            BrowseMovieReminder(navController, "No members here! Come back soon")
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                item {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "List of Members in Group",
                                            color = Color.Gray,
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 50.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                items(members.value) { user ->
                                    UserCard(
                                        userName = user.username,
                                        rightIcon = Icons.Outlined.CheckCircle,
                                        onClick = {},
                                        rightIconColor = Purple40
                                    )
                                }
                            }
                        }
                    }

                    1 -> {
                        GroupRecCarouselDisplay(groupId, navController)
                    }

                    2 -> {
                        GroupActivityList(activities.value, navController)
                    }
                }
            }
        }
    }
}
