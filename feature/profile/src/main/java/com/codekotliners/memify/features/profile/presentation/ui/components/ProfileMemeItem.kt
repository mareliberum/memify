package com.codekotliners.memify.features.profile.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.codekotliners.memify.core.ui.components.CenteredWidget
import com.codekotliners.memify.core.ui.components.shimmerEffect
import com.codekotliners.memify.features.profile.R
import com.codekotliners.memify.features.profile.presentation.model.ProfileMemeUiModel

@Composable
internal fun CreatedMemeItem(meme: ProfileMemeUiModel) {
    ProfileMemeCard(
        meme = meme,
        showLike = false,
    )
}

@Composable
internal fun LikedMemeItem(meme: ProfileMemeUiModel) {
    ProfileMemeCard(
        meme = meme,
        showLike = true,
    )
}

@Composable
private fun ProfileMemeCard(
    meme: ProfileMemeUiModel,
    showLike: Boolean,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .aspectRatio(meme.aspectRatio),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            val painter = rememberAsyncImagePainter(meme.imageUrl)

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )

            when (painter.state) {
                is AsyncImagePainter.State.Error -> ErrorProfileImage()
                is AsyncImagePainter.State.Loading -> {
                    CenteredWidget(modifier = Modifier.shimmerEffect()) {}
                }

                is AsyncImagePainter.State.Success,
                AsyncImagePainter.State.Empty,
                -> {
                    if (showLike) {
                        Icon(
                            painter = painterResource(R.drawable.template_like_on),
                            contentDescription = stringResource(R.string.liked),
                            tint = MaterialTheme.colorScheme.error,
                            modifier =
                                Modifier
                                    .padding(2.dp)
                                    .background(
                                        brush =
                                            Brush.radialGradient(
                                                colors =
                                                    listOf(
                                                        Color.Black.copy(alpha = 0.22f),
                                                        Color.Transparent,
                                                    ),
                                                center = Offset.Unspecified,
                                                radius = 46f,
                                            ),
                                        shape = CircleShape,
                                    ).padding(4.dp)
                                    .align(Alignment.TopEnd),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorProfileImage() {
    CenteredWidget {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = stringResource(R.string.profile_image_load_failed),
            modifier = Modifier.size(30.dp),
        )
    }
}
