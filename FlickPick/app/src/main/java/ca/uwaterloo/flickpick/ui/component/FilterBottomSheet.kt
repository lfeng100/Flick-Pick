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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    tags: List<Tag>,
    selectedTags: Map<String, String>,
    onFiltersChanged: (Map<String, String>) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var selectedMap by remember { mutableStateOf(selectedTags.toMutableMap()) }

    var expandedCategory by remember { mutableStateOf<String?>(null) }

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

            val categories = mapOf(
                "Genre" to tags.filter { it.tagType == "genre" },
                "Language" to tags.filter { it.tagType == "language" },
                "Year" to tags.filter { it.tagType == "year" }
            )

            categories.forEach { (categoryName, categoryTags) ->
                if (categoryTags.isNotEmpty()) {
                    val selectedTag = categoryTags.find { it.tagID == selectedMap[categoryName] }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$categoryName: ${selectedTag?.tagName ?: "None"}",
                            fontWeight = FontWeight.Bold
                        )

                        // 🔥 Toggle expand/collapse
                        TextButton(onClick = {
                            expandedCategory = if (expandedCategory == categoryName) null else categoryName
                        }) {
                            Text(if (expandedCategory == categoryName) "Done" else "Change")
                        }
                    }

                    if (expandedCategory == categoryName) {
                        FlowRow {
                            categoryTags.forEach { tag ->
                                val isSelected = selectedMap[categoryName] == tag.tagID
                                FilterChip(
                                    tag = tag,
                                    isSelected = isSelected,
                                    onSelected = { isSelectedNow ->
                                        val newFilters = selectedMap.toMutableMap()

                                        if (isSelectedNow) {
                                            newFilters[categoryName] = tag.tagID
                                        } else {
                                            newFilters.remove(categoryName)
                                        }

                                        selectedMap = newFilters
                                        onFiltersChanged(newFilters)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
