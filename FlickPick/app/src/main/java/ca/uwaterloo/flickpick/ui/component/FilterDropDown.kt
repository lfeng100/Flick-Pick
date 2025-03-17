package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropDown(tags: List<Tag>, selectedTags: List<String>, onFiltersChanged: (List<String>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedSet by remember { mutableStateOf(selectedTags.toMutableSet()) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            value = "Filters (${selectedSet.size})",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.menuAnchor(), // Needed for correct positioning
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val categories = mapOf(
                "Genres" to tags.filter { it.tagType == "genre" },
                "Languages" to tags.filter { it.tagType == "language" },
                "Years" to tags.filter { it.tagType == "year" }
            )

            categories.forEach { (categoryName, categoryTags) ->
                if (categoryTags.isNotEmpty()) {
                    Text(
                        text = categoryName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )

                    categoryTags.forEach { tag ->
                        val isSelected = selectedSet.contains(tag.tagID)
                        FilterChip(
                            tag = tag,
                            isSelected = isSelected,
                            onSelected = { isSelectedNow ->
                                if (isSelectedNow) selectedSet.add(tag.tagID)
                                else selectedSet.remove(tag.tagID)
                                onFiltersChanged(selectedSet.toList()) // Notify parent about changes
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FilterChip(tag: Tag, isSelected: Boolean, onSelected: (Boolean) -> Unit) {
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterBottomSheet(
    tags: List<Tag>,
    selectedTags: List<String>,
    onFiltersChanged: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val selectedSet = remember { mutableStateOf(selectedTags.toMutableSet()) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.9f) // Ensures scrolling works
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
                "Genres" to tags.filter { it.tagType == "genre" },
                "Languages" to tags.filter { it.tagType == "language" },
                "Years" to tags.filter { it.tagType == "year" }
            )

            categories.forEach { (categoryName, categoryTags) ->
                if (categoryTags.isNotEmpty()) {
                    Text(
                        text = categoryName,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    FlowRow {
                        categoryTags.forEach { tag ->
                            FilterChip(
                                tag = tag,
                                isSelected = selectedSet.value.contains(tag.tagID),
                                onSelected = { isSelectedNow ->
                                    if (isSelectedNow) selectedSet.value.add(tag.tagID)
                                    else selectedSet.value.remove(tag.tagID)

                                    onFiltersChanged(selectedSet.value.toList())
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



