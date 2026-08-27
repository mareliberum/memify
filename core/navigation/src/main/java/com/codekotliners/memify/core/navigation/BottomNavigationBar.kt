package com.codekotliners.memify.core.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.codekotliners.memify.core.navigation.entities.TopLevelDestination

@Composable
fun BottomNavigationBar(
    currentDestination: NavDestination?,
    onDestinationSelected: (TopLevelDestination) -> Unit,
) {
    NavigationBar(
        modifier =
            Modifier
                .height(85.dp)
                .fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        TopLevelDestination.entries.forEach { destination ->
            val selected = currentDestination?.let(destination::isSelected) == true
            NavigationBarItem(
                modifier = Modifier.weight(1f),
                selected = selected,
                onClick = { onDestinationSelected(destination) },
                icon = {
                    Icon(
                        painter =
                            painterResource(
                                if (selected) destination.selectedIconResId else destination.iconResId,
                            ),
                        contentDescription = stringResource(destination.labelResId),
                        modifier = Modifier.size(if (selected) 28.dp else 24.dp),
                    )
                },
                alwaysShowLabel = false,
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    ),
            )
        }
    }
}
