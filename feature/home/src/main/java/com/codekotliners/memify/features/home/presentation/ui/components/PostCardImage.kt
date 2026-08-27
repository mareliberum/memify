package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.codekotliners.memify.core.ui.LocalNavAnimatedVisibilityScope
import com.codekotliners.memify.core.ui.LocalSharedTransitionScope
import com.codekotliners.memify.core.ui.components.CenteredCircularProgressIndicator
import com.codekotliners.memify.features.home.R
import com.codekotliners.memify.features.home.presentation.model.HomePostUiModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun PostCardImage(
    post: HomePostUiModel,
    onImageClick: () -> Unit,
) {
    val painter =
        rememberAsyncImagePainter(
            model =
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(post.imageUrl)
                    .crossfade(true)
                    .build(),
        )
    val sharedTransitionScope =
        LocalSharedTransitionScope.current
            ?: error("No SharedTransitionScope found – make sure you’re inside a SharedTransitionLayout")
    val animatedVisibilityScope =
        LocalNavAnimatedVisibilityScope.current
            ?: error("No AnimatedVisibilityScope found – make sure you’re inside your AnimatedContent/NavHost")
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(post.aspectRatio)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onImageClick,
                ),
    ) {
        with(sharedTransitionScope) {
            Image(
                painter = painter,
                contentDescription = stringResource(R.string.post_image_description),
                modifier =
                    Modifier
                        .fillMaxSize()
                        .sharedBounds(
                            rememberSharedContentState(post.id),
                            animatedVisibilityScope,
                        ),
                contentScale = ContentScale.Fit,
            )
        }

        when (painter.state) {
            is AsyncImagePainter.State.Error -> ErrorPostImage()
            is AsyncImagePainter.State.Loading -> CenteredCircularProgressIndicator()
            is AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Success -> Unit
        }
    }
}
