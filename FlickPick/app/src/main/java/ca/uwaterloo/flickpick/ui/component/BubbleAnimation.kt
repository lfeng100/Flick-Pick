import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.ui.theme.Purple40
import kotlin.random.Random
import kotlinx.coroutines.delay

data class Bubble(val x: Float, val y: Float, val radius: Float, val speed: Float)

@Composable
fun BubbleAnimation() {
    val bubbles = remember { mutableStateListOf<Bubble>() }
    val infiniteTransition = rememberInfiniteTransition()

    LaunchedEffect(Unit) {
        while (bubbles.size < 20) {
            bubbles.add(
                Bubble(
                    x = Random.nextFloat(),
                    y = Random.nextFloat() + 0.7f,
                    radius = Random.nextFloat() * 100 + 50,
                    speed = Random.nextFloat() * 0.9f + 0.7f
                )
            )
            delay(50)
        }
    }

    val animatedBubbles = bubbles.map { bubble ->
        val y by infiniteTransition.animateFloat(
            initialValue = bubble.y,
            targetValue = -0.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = (bubble.speed * 10000).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "bubbleAnimation"
        )
        bubble.copy(y = y)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        animatedBubbles.forEach { bubble ->
            drawCircle(
                color = Purple40,
                radius = bubble.radius,
                center = Offset(bubble.x * size.width, bubble.y * size.height)
            )
        }
    }
}