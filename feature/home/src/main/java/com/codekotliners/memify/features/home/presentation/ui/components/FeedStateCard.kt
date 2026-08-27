package com.codekotliners.memify.features.home.presentation.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun FeedStateCard(
    @DrawableRes illustrationResId: Int,
    title: String,
    description: String,
    action: (@Composable () -> Unit)? = null,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier =
                Modifier
                    .widthIn(max = 440.dp)
                    .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                FeedStateIllustration(illustrationResId)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
                    textAlign = TextAlign.Center,
                )

                if (action != null) {
                    Spacer(modifier = Modifier.height(24.dp))
                    action()
                }
            }
        }
    }
}

@Composable
private fun FeedStateIllustration(
    @DrawableRes illustrationResId: Int,
) {
    Box(
        modifier = Modifier.size(104.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f), CircleShape),
        )
        Box(
            modifier =
                Modifier
                    .size(88.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(illustrationResId),
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .size(14.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.24f), CircleShape),
        )
    }
}
