package com.codekotliners.memify.core.mappers

import com.codekotliners.memify.core.models.Post
import com.codekotliners.memify.core.models.User
import com.codekotliners.memify.core.network.models.PostDto

fun PostDto.toPost(user: User): Post =
    Post(
        id = id,
        imageUrl = imageUrl,
        authorId = authorId,
        likesCount = likesCount,
        templateId = templateId,
        height = height,
        width = width,
        isLiked = isLiked,
        author = user,
    )

fun Post.toPostDto(): PostDto =
    PostDto(
        id = id,
        authorId = authorId,
        imageUrl = imageUrl,
        templateId = templateId,
        width = width,
        height = height,
        likesCount = likesCount,
        isLiked = isLiked,
    )
