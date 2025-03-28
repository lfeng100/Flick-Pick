package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onFiltersChanged: (Map<String, String>) -> Unit,
    onDismiss: () -> Unit
) {
    val tags by MovieRepository.tags.collectAsState()
    val selectedTags by MovieRepository.selectedFilters.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var expandedTagType by remember { mutableStateOf<String?>(null) }

    fun onSelectionChangedFor(tagType: String, newTag: Tag?) {
        val newFilters = selectedTags.toMutableMap()
        if (newTag != null) newFilters[tagType] = newTag.tagID
        else newFilters.remove(tagType)
        onFiltersChanged(newFilters)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.9f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select Filters", style = MaterialTheme.typography.headlineSmall)

                TextButton(onClick = { onFiltersChanged(emptyMap()) }) {
                    Text("Clear All")
                }
            }

            val tagTypes = mapOf(
                "Genre" to tags.filter { it.tagType == "genre" },
                "Language" to tags.filter { it.tagType == "language" },
                "Year" to tags.filter { it.tagType == "year" }
            )

            tagTypes.forEach { (tagType, tagsWithType) ->
                if (tagsWithType.isNotEmpty()) {
                    val selectedTag = tagsWithType.firstOrNull { it.tagID == selectedTags[tagType] }

                    FilterRow(
                        tagType = tagType,
                        selectedTagName = selectedTag?.displayName(),
                        isExpanded = expandedTagType == tagType,
                        onToggleExpand = {
                            expandedTagType = if (expandedTagType == tagType) null else tagType
                        },
                        onClear = {
                            val newFilters = selectedTags.toMutableMap()
                            newFilters.remove(tagType)
                            onFiltersChanged(newFilters)
                        }
                    )

                    if (expandedTagType == tagType) {
                        // scrollable picker for year and languages
                        if (tagType == "Year" || tagType == "Language") {
                            FilterScrollablePicker(
                                tags = tagsWithType,
                                selectedTagId = selectedTags[tagType],
                                onSelectionChanged = { newTag ->
                                    onSelectionChangedFor(tagType, newTag)
                                }
                            )
                        } else {
                            FilterPillSelection(
                                tags = tagsWithType,
                                selectedTagId = selectedTags[tagType],
                                onSelectionChanged = { newTag ->
                                    onSelectionChangedFor(tagType, newTag)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterRow(
    tagType: String,
    selectedTagName: String?,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "$tagType:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
            if (selectedTagName != null) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = selectedTagName,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { onClear() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✕",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(6.dp))
                Text("None", color = Color.Gray)
            }
        }
        TextButton(onClick = onToggleExpand) {
            Text(if (isExpanded) "Done" else "Change")
        }
    }
}