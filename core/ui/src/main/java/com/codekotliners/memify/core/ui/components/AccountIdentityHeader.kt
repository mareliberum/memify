package com.codekotliners.memify.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun AccountIdentityHeader(
    title: String,
    subtitle: String,
    avatarUrl: String?,
    avatarContentDescription: String?,
    modifier: Modifier = Modifier,
    onAvatarClick: (() -> Unit)? = null,
    avatarBadge: (@Composable BoxScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.size(98.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape)
                        .padding(5.dp)
                        .clip(CircleShape)
                        .then(
                            if (onAvatarClick != null) {
                                Modifier.clickable(onClick = onAvatarClick)
                            } else {
                                Modifier
                            },
                        ).border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                        ).background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center,
            ) {
                if (avatarUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = avatarUrl),
                        contentDescription = avatarContentDescription,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(46.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            avatarBadge?.invoke(this)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = title.ifEmpty { EMPTY_TITLE_PLACEHOLDER },
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
            textAlign = TextAlign.Center,
        )
    }
}

private const val EMPTY_TITLE_PLACEHOLDER = "\u00A0"
