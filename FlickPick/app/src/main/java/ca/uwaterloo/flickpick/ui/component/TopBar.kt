package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ca.uwaterloo.flickpick.R

@Composable
fun TopBar(title: String, buttons: List<TopBarButtonData>? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start=16.dp, end=16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            buttons?.forEach { button ->
                IconButton(
                    onClick = button.onClick
                ) {
                    Icon(
                        imageVector = button.icon,
                        contentDescription = button.contentDesc
                    )
                }
            }
        }
    }
}

@Composable
fun BackButtonTopBar(navController: NavController,
                     backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     buttons: List<TopBarButtonData>? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start=16.dp, end=16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
        ) {
            Icon(
                imageVector = backButtonIcon,
                contentDescription = "Back",
            )
        }
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            buttons?.forEach { button ->
                IconButton(
                    onClick = button.onClick
                ) {
                    Icon(
                        imageVector = button.icon,
                        contentDescription = button.contentDesc
                    )
                }
            }
        }
    }
}

data class TopBarButtonData(
    var icon: ImageVector,
    var onClick: () -> Unit,
    var contentDesc: String = ""
)

@Composable
fun LogoTopBar(buttons: List<TopBarButtonData>? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start=16.dp, end=16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.height(40.dp),
            painter = painterResource(id = R.drawable.flickpick_logo),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = "Flickpick Logo",
            contentScale = ContentScale.Fit
        )
        Row(
            horizontalArrangement = Arrangement.Start
        ) {
            buttons?.forEach { button ->
                IconButton(
                    onClick = button.onClick
                ) {
                    Icon(
                        imageVector = button.icon,
                        contentDescription = button.contentDesc
                    )
                }
            }
        }
    }
}