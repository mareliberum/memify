package com.codekotliners.memify.features.create.domain

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Immutable
data class ColoredLine(
    val points: List<Offset>,
    val color: Color,
    val strokeWidth: Float,
    val position: Offset = Offset.Zero,
    val scale: Float = 1f,
    override val id: Long = System.currentTimeMillis(),
) : CanvasElement
