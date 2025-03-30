package ca.uwaterloo.flickpick.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.rounded.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.ActivityItem
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import ca.uwaterloo.flickpick.ui.component.BackButtonTopBarWithText
import ca.uwaterloo.flickpick.ui.component.BrowseMovieReminder
import ca.uwaterloo.flickpick.ui.component.GroupActivityList
import ca.uwaterloo.flickpick.ui.component.GroupRecCarouselDisplay
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData
import ca.uwaterloo.flickpick.ui.component.UserCard
import ca.uwaterloo.flickpick.ui.theme.Pink40
import ca.uwaterloo.flickpick.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupMainScreen(navController: NavController, groupId: String) {

    val group = remember { mutableStateOf<Group?>(null) }
    val members = remember { mutableStateOf(emptyList<User>()) }
    val activities = remember { mutableStateOf<List<ActivityItem>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val userMap = remember(members.value) { members.value.associate { it.userID to it.username }}
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val groupResponse = DatabaseClient.apiService.getGroupById(groupId)
            group.value = groupResponse

            val userResponse = DatabaseClient.apiService.getGroupUsersById(groupId).items
            members.value = userResponse

            val activityResponse = DatabaseClient.apiService.getGroupActivity(groupId)
            activities.value = activityResponse.activity

        } catch (e: Exception) {
            Log.e("GroupMainScreen", "Error: ${e.message}")
        } finally {
            isLoading.value = false
        }
    }
    Scaffold(
        topBar = {
            BackButtonTopBarWithText(
                navController = navController,
                text = group.value?.groupName ?: "Group",
                buttons = listOf(
                    TopBarButtonData(
                        icon = Icons.Rounded.RemoveCircleOutline,
                        onClick = {showDialog = true}
                    ),

                )
            )
        }
    ) { padding ->
        if (showDialog) {
            LeaveGroupDialog(
                showDialog = showDialog,
                onDismissRequest = { showDialog = false },
                leaveClick = {
                    showDialog = false
                }
            )
        }

        val tabTitles = listOf("Members", "Picks", "Activity")
        val pagerState = rememberPagerState(pageCount = { tabTitles.size })
        val coroutineScope = rememberCoroutineScope()

        Column(Modifier.padding(padding)) {
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
                        GroupActivityList(activities.value, userMap, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaveGroupDialog(showDialog: Boolean, onDismissRequest: () -> Unit, leaveClick: () -> Unit) {

    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFFF4B4B))
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "You are about to leave this group",
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Are you sure you want to leave this Group? You might miss out on new picks and activity",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = {
                                    onDismissRequest()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                            ) {
                                Text(
                                    text = "Leave",
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                )
                            }
                            Button(
                                onClick = {
                                    onDismissRequest()
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Pink40)
                            ) {
                                Text(
                                    text = "Stay",
                                    fontSize = 15.sp,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}