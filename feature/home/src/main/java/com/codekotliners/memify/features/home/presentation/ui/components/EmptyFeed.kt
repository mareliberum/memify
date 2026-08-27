package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.codekotliners.memify.features.home.R

@Composable
fun EmptyFeed() {
    FeedStateCard(
        illustrationResId = R.drawable.round_image_24,
        title = stringResource(R.string.empty_feed_title),
        description = stringResource(R.string.empty_feed_message),
    )
}
