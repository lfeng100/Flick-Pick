import android.text.Layout
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
import ca.uwaterloo.flickpick.ui.component.JoinGroupCard
import kotlin.random.Random
import kotlinx.coroutines.delay

@Composable
fun JoinGroupScreen(navController: NavController) {
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
            userName = "JoshDaGoat",
            memberCount = 5,
            navController = navController
        )
    }
}