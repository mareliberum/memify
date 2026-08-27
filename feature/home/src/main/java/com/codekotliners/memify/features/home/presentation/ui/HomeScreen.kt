package com.codekotliners.memify.features.home.presentation.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.codekotliners.memify.core.navigation.AUTH_SUCCESS_EVENT
import com.codekotliners.memify.core.navigation.entities.ImageType
import com.codekotliners.memify.core.navigation.entities.NavRoutes
import com.codekotliners.memify.core.theme.MemifyTheme
import com.codekotliners.memify.core.ui.components.AppScaffold
import com.codekotliners.memify.features.home.R
import com.codekotliners.memify.features.home.presentation.model.HomeAction
import com.codekotliners.memify.features.home.presentation.model.HomeFeedUiModel
import com.codekotliners.memify.features.home.presentation.model.HomeMessage
import com.codekotliners.memify.features.home.presentation.model.HomeNavigation
import com.codekotliners.memify.features.home.presentation.model.HomeUiState
import com.codekotliners.memify.features.home.presentation.ui.components.EmptyFeed
import com.codekotliners.memify.features.home.presentation.ui.components.ErrorScreen
import com.codekotliners.memify.features.home.presentation.ui.components.HomeFeed
import com.codekotliners.memify.features.home.presentation.ui.components.HomeTopBar
import com.codekotliners.memify.features.home.presentation.ui.components.LoadingFeed
import com.codekotliners.memify.features.home.presentation.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val loginResult = currentBackStackEntry?.savedStateHandle?.get<Boolean>(AUTH_SUCCESS_EVENT)
    val snackbarHostState = remember { SnackbarHostState() }
    val messageText =
        when (state.message) {
            HomeMessage.FeedRefreshFailed -> stringResource(R.string.feed_refresh_failed)
            HomeMessage.LikeUpdateFailed -> stringResource(R.string.like_update_failed)
            null -> null
        }

    LaunchedEffect(loginResult) {
        if (loginResult == true) {
            currentBackStackEntry.savedStateHandle.remove<Boolean>(AUTH_SUCCESS_EVENT)
            viewModel.onAction(HomeAction.Refresh)
        }
    }

    LaunchedEffect(state.message, messageText) {
        if (messageText != null) {
            snackbarHostState.showSnackbar(messageText)
            viewModel.onAction(HomeAction.MessageShown)
        }
    }

    LaunchedEffect(state.navigation) {
        when (state.navigation) {
            HomeNavigation.Auth -> {
                viewModel.onAction(HomeAction.NavigationHandled)
                navController.navigate(NavRoutes.Auth.route)
            }

            null -> Unit
        }
    }

    AppScaffold(
        navController = navController,
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeTopBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        HomeContent(
            state = state,
            onAction = viewModel::onAction,
            onPostClick = { postId ->
                navController.navigate(
                    NavRoutes.ImageViewer.createRoute(
                        type = ImageType.POST,
                        id = postId,
                    ),
                )
            },
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    state: HomeUiState,
    onAction: (HomeAction) -> Unit,
    onPostClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = { onAction(HomeAction.Refresh) },
        modifier = modifier.background(MaterialTheme.colorScheme.background),
    ) {
        when (val feed = state.feed) {
            HomeFeedUiModel.Loading -> LoadingFeed()
            HomeFeedUiModel.Empty -> EmptyFeed()
            is HomeFeedUiModel.Error ->
                ErrorScreen(
                    errorType = feed.type,
                    onRetry = { onAction(HomeAction.Refresh) },
                )

            is HomeFeedUiModel.Content ->
                HomeFeed(
                    posts = feed.posts,
                    pendingLikePostIds = state.pendingLikePostIds,
                    onLikeClick = { postId -> onAction(HomeAction.LikeClicked(postId)) },
                    onPostClick = onPostClick,
                )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EmptyHomePreview() {
    MemifyTheme {
        HomeContent(
            state = HomeUiState(feed = HomeFeedUiModel.Empty),
            onAction = {},
            onPostClick = {},
        )
    }
}
