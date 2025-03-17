package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            Text("Select Filters", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))

            val tagTypes = mapOf(
                "Genre" to tags.filter { it.tagType == "genre" },
                "Language" to tags.filter { it.tagType == "language" },
                "Year" to tags.filter { it.tagType == "year" }
            )

            tagTypes.forEach { (tagType, tagsWithType) ->
                if (tagsWithType.isNotEmpty()) {
                    val selectedTag = tagsWithType.firstOrNull { it.tagID == selectedTags[tagType] }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$tagType: ${selectedTag?.tagName ?: "None"}",
                            fontWeight = FontWeight.Bold
                        )

                        ToggleChangeFilterButton(
                            isExpanded = expandedTagType == tagType,
                            onToggle = {
                                expandedTagType = if (expandedTagType == tagType) null else tagType
                            }
                        )
                    }

                    if (expandedTagType == tagType) {
                        TagSelectionRow(
                            tags = tagsWithType,
                            selectedTagId = selectedTags[tagType],
                            tagType = tagType,
                            onSelectionChanged = { newTagId ->
                                val newFilters = selectedTags.toMutableMap()
                                if (newTagId != null) {
                                    //change for type
                                    newFilters[tagType] = newTagId
                                } else {
                                    //otherwise, remove it
                                    newFilters.remove(tagType)
                                }

                                onFiltersChanged(newFilters)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ToggleChangeFilterButton(isExpanded: Boolean, onToggle: () -> Unit) {
    TextButton(onClick = onToggle) {
        Text(if (isExpanded) "Done" else "Change")
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagSelectionRow(
    tags: List<Tag>,
    selectedTagId: String?,
    tagType: String,
    onSelectionChanged: (String?) -> Unit
) {
    FlowRow {
        tags.forEach { tag ->
            val isSelected = selectedTagId == tag.tagID
            FilterChip(
                tag = tag,
                isSelected = isSelected,
                onSelected = { isSelectedNow ->
                    onSelectionChanged(if (isSelectedNow) tag.tagID else null)
                }
            )
        }
    }
}