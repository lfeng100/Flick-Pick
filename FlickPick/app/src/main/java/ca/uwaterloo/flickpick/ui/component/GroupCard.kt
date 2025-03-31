package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.data.database.model.Group

@Composable
fun GroupCard(group: Group, onClick: (() -> Unit)) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(45.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = "Group Icon",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = group.groupName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Outlined.ArrowCircleRight,
                contentDescription = "Arrow Icon",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
fun JoinGroupCardsList(groups: List<Group>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(groups) { group ->
            GroupCard(
                group = group,
                onClick = {
                    navController.navigate("group/${group.groupID}")
                }
            )
        }
    }
}

@Composable
fun YourGroupCardsList(groups: List<Group>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(groups) { group ->
            GroupCard(
                group = group,
                onClick = {
                    navController.navigate("groupmain/${group.groupID}")
                }
            )
        }
    }
}

