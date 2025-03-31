package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import ca.uwaterloo.flickpick.data.recommender.model.Filters
import ca.uwaterloo.flickpick.domain.filter.FiltersConstants
import ca.uwaterloo.flickpick.domain.filter.RecommenderFiltersBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersCustomizer(initial: Filters?, onFiltersCreated: (Filters?) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val filters = remember { RecommenderFiltersBuilder(initial) }
    ModalBottomSheet(
        onDismissRequest = { onFiltersCreated(filters.buildFilters()) },
        sheetState = sheetState,
        modifier = Modifier.fillMaxHeight(0.8f)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                for (genre in FiltersConstants.genreList) {
                    TriStateCheckBoxLabel(
                        text = genre,
                        state = filters.stateOfGenre(genre),
                        onClick = {
                            when (filters.stateOfGenre(genre)) {
                                ToggleableState.Off -> filters.includeGenre(genre)
                                ToggleableState.On -> filters.excludeGenre(genre)
                                ToggleableState.Indeterminate -> filters.removeGenre(genre)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TriStateCheckBoxLabel(text: String, state: ToggleableState, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TriStateCheckbox(
            state = state,
            onClick = { onClick(); }
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}