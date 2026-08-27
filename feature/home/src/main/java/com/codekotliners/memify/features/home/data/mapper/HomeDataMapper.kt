package com.codekotliners.memify.features.home.data.mapper

import com.codekotliners.memify.core.network.models.PostDto
import com.codekotliners.memify.core.network.models.ToggleLikeResponseDto
import com.codekotliners.memify.features.home.data.model.HomeAuthorData
import com.codekotliners.memify.features.home.data.model.HomeLikeData
import com.codekotliners.memify.features.home.data.model.HomePostData
import com.codekotliners.memify.features.home.domain.model.HomeAuthor
import com.codekotliners.memify.features.home.domain.model.HomeLikeUpdate
import com.codekotliners.memify.features.home.domain.model.HomePost

internal fun PostDto.toHomeData(author: HomeAuthorData): HomePostData =
    HomePostData(
        id = id,
        imageUrl = imageUrl,
        width = width,
        height = height,
        likesCount = likesCount,
        isLiked = isLiked,
        author = author,
    )

internal fun ToggleLikeResponseDto.toHomeData(postId: String): HomeLikeData =
    HomeLikeData(
        postId = postId,
        isLiked = isLiked,
        likesCount = likesCount,
    )

internal fun HomePostData.toDomain(): HomePost =
    HomePost(
        id = id,
        imageUrl = imageUrl,
        width = width,
        height = height,
        likesCount = likesCount,
        isLiked = isLiked,
        author =
            HomeAuthor(
                displayName = author.displayName,
                avatarUrl = author.avatarUrl,
            ),
    )

internal fun HomeLikeData.toDomain(): HomeLikeUpdate =
    HomeLikeUpdate(
        postId = postId,
        isLiked = isLiked,
        likesCount = likesCount,
    )
