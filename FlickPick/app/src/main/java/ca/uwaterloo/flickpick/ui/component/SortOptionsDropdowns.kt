package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortOptionsDropdowns(
    selectedSortBy: String,
    selectedSortOrder: String,
    onSortChanged: (String, String) -> Unit,
    updateSortBy: (String) -> Unit,
    updateSortOrder: (String) -> Unit
) {
    var sortByExpanded by remember { mutableStateOf(false) }
    var sortOrderExpanded by remember { mutableStateOf(false) }

    val sortOptions = listOf("title", "rating", "releaseYear")
    val sortDisplayNames = mapOf(
        "title" to "Title",
        "rating" to "Rating",
        "releaseYear" to "Release Year"
    )

    val orderOptions = listOf("asc", "desc")
    val orderDisplayNames = mapOf(
        "asc" to "Ascending",
        "desc" to "Descending"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            ExposedDropdownMenuBox(
                expanded = sortByExpanded,
                onExpandedChange = { sortByExpanded = !sortByExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = sortDisplayNames[selectedSortBy] ?: selectedSortBy,
                    onValueChange = {},
                    label = { Text("Sort By") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortByExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = sortByExpanded,
                    onDismissRequest = { sortByExpanded = false }
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(sortDisplayNames[option] ?: option) },
                            onClick = {
                                sortByExpanded = false
                                updateSortBy(option)
                                onSortChanged(option, selectedSortOrder)
                            }
                        )
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            ExposedDropdownMenuBox(
                expanded = sortOrderExpanded,
                onExpandedChange = { sortOrderExpanded = !sortOrderExpanded }
            ) {
                TextField(
                    readOnly = true,
                    value = orderDisplayNames[selectedSortOrder] ?: selectedSortOrder,
                    onValueChange = {},
                    label = { Text("Order") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortOrderExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = sortOrderExpanded,
                    onDismissRequest = { sortOrderExpanded = false }
                ) {
                    orderOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(orderDisplayNames[option] ?: option) },
                            onClick = {
                                sortOrderExpanded = false
                                updateSortOrder(option)
                                onSortChanged(selectedSortBy, option)
                            }
                        )
                    }
                }
            }
        }
    }
}