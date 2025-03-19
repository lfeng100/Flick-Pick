package ca.uwaterloo.flickpick.ui.component
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun CreateGroupNameField(
    value: MutableState<TextFieldValue>,
    placeHolderText: String = "Enter group name (Optional)"
){
    TextField(
        value = value.value,
        onValueChange = {newText: TextFieldValue-> value.value = newText},
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom=16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium),
        singleLine=true,
        placeholder = {Text(placeHolderText)}
    )
}