package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.features.create.domain.TextElement
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

private const val OUTLINE_WIDTH_DP = 1.1f
private const val OUTLINE_LUMINANCE_THRESHOLD = 0.5f

@Composable
fun TextElementView(
    element: TextElement,
    viewModel: CanvasViewModel,
) {
    val isSelected = viewModel.selectedElementId == element.id

    Box(
        modifier =
            Modifier
                .graphicsLayer(
                    translationX = element.position.x,
                    translationY = element.position.y,
                ).pointerInput(element.id) {
                    detectTapGestures(
                        onTap = { viewModel.selectElement(element) },
                        onDoubleTap = {
                            viewModel.selectElement(element)
                            viewModel.startEditingSelectedText()
                        },
                    )
                }.pointerInput(element.id) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        if (viewModel.selectedElementId != element.id) {
                            viewModel.selectElement(element)
                        }
                        viewModel.transformText(
                            elementId = element.id,
                            positionDelta = pan,
                            zoom = zoom,
                        )
                    }
                }.padding(6.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isSelected) {
            Box(
                modifier =
                    Modifier
                        .matchParentSize()
                        .border(1.5.dp, Color.White, RoundedCornerShape(6.dp))
                        .border(1.dp, Color.Black.copy(alpha = 0.35f), RoundedCornerShape(6.dp)),
            )
        }
        TextElementContent(element)
    }
}

@Composable
private fun TextElementContent(element: TextElement) {
    val outlineOffsets =
        remember {
            (-1..1).flatMap { dx -> (-1..1).map { dy -> dx to dy } }.filterNot { it.first == 0 && it.second == 0 }
        }

    Box(contentAlignment = Alignment.Center) {
        if (element.hasOutline) {
            val outlineColor =
                if (element.color.luminance() > OUTLINE_LUMINANCE_THRESHOLD) Color.Black else Color.White
            outlineOffsets.forEach { (dx, dy) ->
                Text(
                    text = element.text,
                    color = outlineColor,
                    fontSize = element.size.sp,
                    fontFamily = element.fontFamily,
                    fontWeight = element.fontWeight,
                    textAlign = element.textAlign,
                    modifier =
                        Modifier.offset(
                            x = (dx * OUTLINE_WIDTH_DP).dp,
                            y = (dy * OUTLINE_WIDTH_DP).dp,
                        ),
                )
            }
        }
        Text(
            text = element.text,
            color = element.color,
            fontSize = element.size.sp,
            fontFamily = element.fontFamily,
            fontWeight = element.fontWeight,
            textAlign = element.textAlign,
        )
    }
}
