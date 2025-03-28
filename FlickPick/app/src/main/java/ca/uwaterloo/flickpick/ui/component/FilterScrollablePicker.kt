package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag
import kotlinx.coroutines.flow.collectLatest

@Composable
fun FilterScrollablePicker(
    tags: List<Tag>,
    selectedTagId: String?,
    onSelectionChanged: (Tag) -> Unit
) {
    val itemHeight = 48.dp
    // starts at first index here
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = tags.indexOfFirst { it.tagID == selectedTagId }.coerceAtLeast(0))
    val flingBehavior = rememberSnapFlingBehavior(listState)

    //snap to items
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collectLatest { (index, offset) ->
                val adjustedIndex = index + if (offset > itemHeight.value / 2) 1 else 0
                tags.getOrNull(adjustedIndex)?.let { tag ->
                    if (tag.tagID != selectedTagId) {
                        onSelectionChanged(tag)
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .height(itemHeight * 3)
            .clip(RoundedCornerShape(12.dp))
    ) {
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = itemHeight),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tags.size) { index ->
                val tag = tags[index]
                PickerItem(tag.displayName(), tag.tagID == selectedTagId, itemHeight)
            }
        }
    }
}

@Composable
private fun PickerItem(label: String, isSelected: Boolean, itemHeight: Dp) {
    var backgroundColor = Color.Transparent
    var textColor = Color.Gray
    var fontWeight = FontWeight.Normal

    if (isSelected){
        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        textColor = MaterialTheme.colorScheme.primary
        fontWeight = FontWeight.Bold
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(itemHeight)
            .background(backgroundColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}
