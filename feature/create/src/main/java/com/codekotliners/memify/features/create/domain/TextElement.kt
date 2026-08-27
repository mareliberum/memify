package com.codekotliners.memify.features.create.domain

import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Immutable
data class TextElement(
    val text: String,
    val color: Color,
    val size: Float,
    val fontFamily: FontFamily,
    val fontWeight: FontWeight,
    val position: Offset,
    val textAlign: TextAlign = TextAlign.Center,
    val hasOutline: Boolean = false,
    override val id: Long = System.currentTimeMillis(),
) : CanvasElement
