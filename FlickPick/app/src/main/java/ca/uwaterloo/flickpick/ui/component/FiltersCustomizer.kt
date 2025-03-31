package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import ca.uwaterloo.flickpick.data.recommender.model.Filters
import ca.uwaterloo.flickpick.domain.filter.FiltersConstants
import ca.uwaterloo.flickpick.domain.filter.RecommenderFiltersBuilder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersCustomizer(initial: Filters?, onFiltersCreated: (Filters?) -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val filters = remember { RecommenderFiltersBuilder(initial) }
    var yearRange by remember {
        val start = initial?.minYear ?: 1930
        val end = initial?.maxYear ?: 2020
        mutableStateOf(start.toFloat()..end.toFloat())
    }
    var minScore by remember {
        mutableFloatStateOf(initial?.minScore ?: 0f)
    }
    var maxRuntime by remember {
        mutableFloatStateOf(initial?.maxRuntime?.toFloat() ?: 300f)
    }

    ModalBottomSheet(
        onDismissRequest =
        {
            if (yearRange.start.toInt() > 1930) {
                filters.setMinYear(yearRange.start.toInt())
            }
            if (yearRange.endInclusive.toInt() < 2020) {
                filters.setMaxYear(yearRange.endInclusive.toInt())
            }
            if (minScore > 0f) {
                filters.setMinScore(minScore)
            }
            if (maxRuntime < 300f) {
                filters.setMaxRuntime(maxRuntime.toInt())
            }
            onFiltersCreated(filters.buildFilters())
        },
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
            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var minYear = yearRange.start.toInt().toString()
                    var maxYear = yearRange.endInclusive.toInt().toString()
                    if (minYear == "1930") {
                        minYear = "before 1930"
                    }
                    if (maxYear == "2020") {
                        maxYear = "now"
                    }
                    Text(text = "Year: $minYear to $maxYear")
                    RangeSlider(
                        value = yearRange,
                        onValueChange = { yearRange = it },
                        valueRange = 1930f..2020f, // Define min and max values
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Minimum Score: ${"%.1f".format(minScore)}")
                    Slider(
                        value = minScore,
                        onValueChange = { minScore = it },
                        valueRange = 0f..4.0f,
                        steps = 40, // Optional: Adds discrete points
                    )
                }
            }
            item {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var runtimeStr = maxRuntime.toInt().toString()
                    if (runtimeStr == "300") {
                        runtimeStr = "∞"
                    }
                    Text(text = "Maximum Runtime: $runtimeStr min")
                    Slider(
                        value = maxRuntime,
                        onValueChange = { maxRuntime = it },
                        valueRange = 30f..300f,
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