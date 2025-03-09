package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.StarHalf
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StarRatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val stars = 5
    Row(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val starWidth = size.width / stars
                    val clickedStar = (offset.x / starWidth).toInt()
                    val isHalf = (offset.x % starWidth) < (starWidth / 2)
                    val newRating = if (isHalf) clickedStar + 0.5f else clickedStar + 1f
                    onRatingChanged(newRating)
                }
            }
    ) {
        for (i in 1..stars) {
            val icon = when {
                i <= rating -> Icons.Rounded.Star
                i - 0.5f == rating -> Icons.AutoMirrored.Rounded.StarHalf
                else -> Icons.Rounded.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp)
                    .size(42.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewStarRatingBar() {
    StarRatingBar(3.5f, {})
}