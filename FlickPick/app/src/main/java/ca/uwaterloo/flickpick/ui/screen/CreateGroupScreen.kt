package ca.uwaterloo.flickpick.ui.screen
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import ca.uwaterloo.flickpick.ui.component.CreateGroupNameField
import ca.uwaterloo.flickpick.ui.component.TopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData
import ca.uwaterloo.flickpick.ui.component.UserCard
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.ui.text.style.TextAlign
import ca.uwaterloo.flickpick.ui.component.TitleTopBar


@Composable
fun CreateGroupScreen(navController: NavController) {
    val groupName = remember { mutableStateOf(TextFieldValue("")) }

    val users = remember { mutableStateOf(emptyList<User>()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = DatabaseClient.apiService.getAllUsers().items
            Log.d("CreateGroupScreen", "API Response: $response")
            users.value = response
        } catch (e: Exception) {
            Log.e("CreateGroupScreen", "Error fetching users: ${e.message}")
        } finally {
            isLoading.value = false
        }
    }
    Scaffold(
//        topBar = {
//            TopBar("Create Group",
//                listOf(
//                    TopBarButtonData(
//                        icon = Icons.AutoMirrored.Filled.ArrowBack,
//                        onClick = { navController.popBackStack() }
//                    )
//                )
//            )
//        }
        topBar = {
            TitleTopBar("Create Groups",
                listOf(
                    TopBarButtonData(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = { navController.popBackStack()}
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
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                CreateGroupNameField(
                    value = groupName,
                    placeHolderText = "Enter group name (optional)"
                )

                LazyColumn(
                    modifier = Modifier.weight(1f) // Takes up available space above the button
                ) {
                    items(users.value) { user ->
                        UserCard(
                            userName = user.username,
                            onClick = { navController.popBackStack() })
                    }
                }
                Spacer(Modifier.padding(bottom = 120.dp))
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                //Create group button
                Button(
                    onClick = { navController.navigate("browse") },
                    modifier = Modifier
                        .width(240.dp)
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.width(140.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.GroupAdd,
                            contentDescription = "Create Group Button"
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = "Create Group",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

