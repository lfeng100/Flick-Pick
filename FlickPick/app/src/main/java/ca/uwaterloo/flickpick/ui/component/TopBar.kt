package ca.uwaterloo.flickpick.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
fun TopBar(buttons: List<TopBarButtonData>? = null, content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(start=16.dp, end=16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.weight(1f)) {
            content()
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
                        contentDescription = button.contentDesc,
                        tint = if (button.tint == null ) LocalContentColor.current else button.tint!!
                    )
                }
            }
        }
    }
}

data class TopBarButtonData(
    var icon: ImageVector,
    var onClick: () -> Unit,
    var contentDesc: String = "",
    var tint: Color? = null
)

@Composable
fun TitleTopBar(title: String, buttons: List<TopBarButtonData>? = null) {
    TopBar(buttons) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun BackButtonTopBar(navController: NavController,
                     backButtonIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
                     buttons: List<TopBarButtonData>? = null) {
    TopBar (buttons) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
        ) {
            Icon(
                imageVector = backButtonIcon,
                contentDescription = "Back",
            )
        }
    }
}

@Composable
fun LogoTopBar() {
    TopBar {
        Image(
            modifier = Modifier.height(40.dp),
            painter = painterResource(id = R.drawable.flickpick_logo),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = "Flickpick Logo",
            contentScale = ContentScale.Fit
        )
    }
}