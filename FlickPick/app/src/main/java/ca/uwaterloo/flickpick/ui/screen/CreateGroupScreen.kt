package ca.uwaterloo.flickpick.ui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.data.database.model.AddUserToGroup
import ca.uwaterloo.flickpick.data.database.model.GroupCreate
import ca.uwaterloo.flickpick.data.database.model.User
import ca.uwaterloo.flickpick.ui.component.CreateGroupNameField
import ca.uwaterloo.flickpick.ui.component.TitleTopBar
import ca.uwaterloo.flickpick.ui.component.TopBarButtonData
import ca.uwaterloo.flickpick.ui.component.UserCard
import ca.uwaterloo.flickpick.ui.theme.PurpleGrey40
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun CreateGroupScreen(navController: NavController) {
    val groupName = remember { mutableStateOf(TextFieldValue("")) }
    val users = remember { mutableStateOf(emptyList<User>()) }
    val isLoading = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val selectedUsers = remember { mutableStateOf(mutableSetOf<String>()) }
    val showAlert = remember {mutableStateOf(false)}

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

    fun createGroup() {
        if (groupName.value.text.isBlank()){
            showAlert.value = true
            return
        }

        coroutineScope.launch {
            try {
                val adminUserID = FirebaseAuth.getInstance().currentUser?.uid
                val createGroup = GroupCreate(
                    groupName = groupName.value.text,
                    adminUserID = adminUserID,
                    adminUsername = "michael_g",
                    groupSize = selectedUsers.value.size + 1
                )
                val response = DatabaseClient.apiService.createGroup(createGroup)
                val groupID = response.groupID

                if (adminUserID != null) {
                    val addUserToGroup = AddUserToGroup(
                        groupID = groupID,
                        userID = adminUserID
                    )
                    try {
                        DatabaseClient.apiService.addUserToGroup(addUserToGroup)
                    } catch (e: Exception) {
                        Log.e("CreateGroupScreen", "Error adding admin to group: ${e.message}")
                    }
                }

                selectedUsers.value.forEach { userID ->
                    try {
                        val addUserToGroup = AddUserToGroup(
                            groupID = groupID,
                            userID = userID
                        )

                        val addUserResponse = DatabaseClient.apiService.addUserToGroup(addUserToGroup)
                        Log.d("Create Group Screen", "User added to group: $addUserResponse")
                    } catch (e: Exception) {
                        Log.e("CreateGroupScreen", "Error adding user to group: ${e.message}")
                    }
                }

                navController.navigate("group")
            } catch (e: Exception) {
                Log.e("CreateGroupScreen", "Error creating group: ${e.message}")
            }
        }
    }

    Scaffold(
        topBar = {
            TitleTopBar("Create Groups",
                listOf(
                    TopBarButtonData(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = { navController.popBackStack() }
                    )
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(start=16.dp, end=16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                CreateGroupNameField(
                    value = groupName,
                    placeHolderText = "Enter group name"
                )
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(users.value) { user ->
                        val isSelected = selectedUsers.value.contains(user.userID)

                        UserCard(
                            userName = user.username,
                            rightIcon = if (isSelected) Icons.Outlined.CheckCircle else Icons.Outlined.AddCircle,
                            onClick = {
                                selectedUsers.value = selectedUsers.value.toMutableSet().apply {
                                    if (isSelected) {
                                        remove(user.userID)
                                        Log.d("UserCard", "User deselected: ${user.username} (ID: ${user.userID})")
                                    } else {
                                        add(user.userID)
                                        Log.d("UserCard", "User selected: ${user.username} (ID: ${user.userID})")
                                    }
                                }
                            },
                            rightIconColor = if (isSelected) Color.Green else PurpleGrey40
                        )
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
                Button(
                    onClick = { createGroup() },
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
    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = { showAlert.value = false },
            confirmButton = {
                TextButton(onClick = { showAlert.value = false }) {
                    Text("OK")
                }
            },
            title = { Text("Missing Group Name") },
            text = { Text("Please enter a group name before creating the group.") }
        )
    }
}
