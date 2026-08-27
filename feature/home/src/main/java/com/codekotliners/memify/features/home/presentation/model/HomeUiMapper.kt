package com.codekotliners.memify.features.home.presentation.model

import com.codekotliners.memify.features.home.domain.model.HomePost

internal fun HomePost.toUiModel(): HomePostUiModel =
    HomePostUiModel(
        id = id,
        imageUrl = imageUrl,
        aspectRatio = width.coerceAtLeast(1).toFloat() / height.coerceAtLeast(1),
        authorName = author.displayName,
        authorAvatarUrl = author.avatarUrl,
        likesCount = likesCount,
        isLiked = isLiked,
    )
