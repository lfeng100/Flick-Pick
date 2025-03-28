package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterPillSelection(
    tags: List<Tag>,
    selectedTagId: String?,
    onSelectionChanged: (Tag?) -> Unit
) {
    FlowRow {
        tags.forEach { tag ->
            FilterPill(
                tag = tag,
                isSelected = selectedTagId == tag.tagID,
                onSelected = { isSelectedNow ->
                    onSelectionChanged(if (isSelectedNow) tag else null)
                }
            )
        }
    }
}
@Composable
fun FilterPill(tag: Tag, isSelected: Boolean, onSelected: (Boolean) -> Unit) {
    var surfaceColor = Color.LightGray
    var textColor = Color.Black
    if (isSelected){
        surfaceColor =  MaterialTheme.colorScheme.primary
        textColor = Color.White
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                onSelected(!isSelected)
            }
    ) {
        Text(
            text = tag.displayName(),
            color = textColor,
            modifier = Modifier.padding(8.dp)
        )
    }
}