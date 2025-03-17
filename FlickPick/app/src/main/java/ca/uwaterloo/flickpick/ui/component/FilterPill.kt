package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag

@Composable
fun FilterPill(tag: Tag, isSelected: Boolean, onSelected: (Boolean) -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                onSelected(!isSelected)
            }
    ) {
        Text(
            text = tag.tagName,
            color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.padding(8.dp)
        )
    }
}