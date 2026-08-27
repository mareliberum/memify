package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.home.presentation.model.HomePostUiModel

@Composable
internal fun HomePostCard(
    post: HomePostUiModel,
    isLikePending: Boolean,
    onLikeClick: () -> Unit,
    onImageClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
            PostCardHeader(post)
            PostCardImage(post, onImageClick)
            PostCardFooter(
                post = post,
                isLikePending = isLikePending,
                onLikeClick = onLikeClick,
            )
        }
    }
}
