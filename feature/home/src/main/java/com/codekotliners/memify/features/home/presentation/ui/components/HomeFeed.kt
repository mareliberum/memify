package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.home.presentation.model.HomePostUiModel

@Composable
internal fun HomeFeed(
    posts: List<HomePostUiModel>,
    pendingLikePostIds: Set<String>,
    onLikeClick: (String) -> Unit,
    onPostClick: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(
            items = posts,
            key = { post -> post.id },
        ) { post ->
            HomePostCard(
                post = post,
                isLikePending = post.id in pendingLikePostIds,
                onLikeClick = { onLikeClick(post.id) },
                onImageClick = { onPostClick(post.id) },
            )
        }
    }
}
