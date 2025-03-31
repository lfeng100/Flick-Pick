import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.data.database.DatabaseClient
import ca.uwaterloo.flickpick.domain.repository.PrimaryUserRepository
import ca.uwaterloo.flickpick.ui.component.JoinGroupCard
import kotlinx.coroutines.launch

@Composable
fun JoinGroupScreen(navController: NavController, groupId: String) {
    val userId = PrimaryUserRepository.getPrimaryUserID()!!
    var userNameByID by remember { mutableStateOf<String?>(null) }
    var groupCount by remember { mutableStateOf<Int?>(null) }
    var groupName by remember { mutableStateOf<String?>(null) }
    var groupOwner by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        coroutineScope.launch {
            try {
                val user = DatabaseClient.apiService.getUserById(userId)
                if (user != null) {
                    userNameByID = user.username
                }
                val groupResponse = DatabaseClient.apiService.getGroupsById(groupId)
                groupCount = groupResponse.groupSize
                groupName = groupResponse.groupName
                groupOwner = groupResponse.adminUsername
                Log.d("JoinGroupScreen", "User name: $userNameByID, Group count: $groupCount")
            } catch (e: Exception) {
                Log.e("JoinGroupScreen", "Error fetching user or group", e)
            }
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
       BubbleAnimation()
        Text(
            text = "Join a Group",
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            style = MaterialTheme.typography.headlineLarge
        )
        JoinGroupCard(
            groupName = groupName ?: "Join this Group",
            userName = (userNameByID.toString()),
            adminUsername = groupOwner ?: "Unknown",
            memberCount = groupCount ?: 0,
            navController = navController,
            userId = userId,
            groupId = groupId
        )
    }
}