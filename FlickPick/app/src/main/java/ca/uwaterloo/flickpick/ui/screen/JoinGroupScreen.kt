import android.text.Layout
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.dataObjects.Database.DatabaseClient
import ca.uwaterloo.flickpick.ui.component.JoinGroupCard
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun JoinGroupScreen(navController: NavController) {
    val userId = "1" //TODO: Update this ID to current userID when it is globally stored
    val groupId = "g1"
    var userNameByID by remember { mutableStateOf<String?>(null) }
    var groupCount by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        coroutineScope.launch {
            try {
                val user = DatabaseClient.apiService.getUserById(userId)
                userNameByID = user.username
                val groupResponse = DatabaseClient.apiService.getGroupsById(groupId)
                groupCount = groupResponse.total
                val groupName = DatabaseClient.apiService.getGroup(groupId)
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
            groupName = "Movie Lovers Unite Forever",
            userName = (userNameByID.toString()),
            memberCount = groupCount ?: 0,
            navController = navController
        )
    }
}