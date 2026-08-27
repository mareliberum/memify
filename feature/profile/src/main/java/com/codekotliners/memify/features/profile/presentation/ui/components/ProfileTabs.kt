package com.codekotliners.memify.features.profile.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.profile.R
import com.codekotliners.memify.features.profile.presentation.model.ProfileTab
import com.codekotliners.memify.features.profile.presentation.model.ProfileUiState

@Composable
internal fun ProfileTabs(
    state: ProfileUiState,
    onTabSelected: (ProfileTab) -> Unit,
) {
    val tabs =
        if (state.isLoggedIn) {
            listOf(
                ProfileTab.CREATED to stringResource(R.string.created),
                ProfileTab.LIKED to stringResource(R.string.liked),
            )
        } else {
            listOf(ProfileTab.CREATED to stringResource(R.string.created))
        }
    val selectedTab = if (state.isLoggedIn) state.selectedTab else ProfileTab.CREATED
    val selectedIndex = tabs.indexOfFirst { (tab, _) -> tab == selectedTab }.coerceAtLeast(0)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        edgePadding = 8.dp,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier =
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedIndex])
                        .padding(horizontal = 18.dp),
                height = 3.dp,
                color = MaterialTheme.colorScheme.primary,
            )
        },
    ) {
        tabs.forEach { (tab, title) ->
            val isSelected = selectedTab == tab
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
            )
        }
    }
}
