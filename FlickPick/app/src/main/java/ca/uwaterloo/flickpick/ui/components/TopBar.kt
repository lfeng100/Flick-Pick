package ca.uwaterloo.flickpick.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(title: String, buttons: List<TopBarButtonData>? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp, 25.dp, 20.dp, 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween // Pushes elements to opposite ends
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(start = 12.dp)
        )
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            buttons?.forEach({ button ->
                IconButton(
                    onClick = button.onClick
                ) {
                    Icon(
                        imageVector = button.icon,
                        contentDescription = button.contentDesc
                    )
                }
            })
        }
    }
}

data class TopBarButtonData(
    var icon: ImageVector,
    var onClick: () -> Unit,
    var contentDesc: String = ""
)
