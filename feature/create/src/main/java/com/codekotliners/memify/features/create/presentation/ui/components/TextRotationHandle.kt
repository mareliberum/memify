package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.create.R
import com.codekotliners.memify.features.create.domain.TextElement
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private val rotationHandleSize = 32.dp
private val rotationHandleIconSize = 20.dp
private const val HALF_ROTATION_DEGREES = 180f
private const val FULL_ROTATION_DEGREES = 360f

@Composable
fun TextRotationHandle(
    element: TextElement,
    elementSize: IntSize,
    viewModel: CanvasViewModel,
) {
    val handleRadius = with(LocalDensity.current) { rotationHandleSize.toPx() / 2f }
    val elementCenter =
        element.position +
            Offset(
                x = elementSize.width / 2f,
                y = elementSize.height / 2f,
            )
    val cornerOffset =
        rotate(
            offset = Offset(elementSize.width / 2f, -elementSize.height / 2f),
            rotationDegrees = element.rotationDegrees,
        )
    val handleTopLeft = elementCenter + cornerOffset - Offset(handleRadius, handleRadius)
    val currentElementCenter = rememberUpdatedState(elementCenter)
    val currentHandleTopLeft = rememberUpdatedState(handleTopLeft)

    Box(
        modifier =
            Modifier
                .offset {
                    IntOffset(
                        x = handleTopLeft.x.roundToInt(),
                        y = handleTopLeft.y.roundToInt(),
                    )
                }.size(rotationHandleSize)
                .shadow(elevation = 3.dp, shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .border(width = 1.5.dp, color = MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                .pointerInput(element.id) {
                    var previousAngle: Float? = null

                    detectDragGestures(
                        onDragStart = { touchOffset ->
                            previousAngle =
                                angleBetween(
                                    center = currentElementCenter.value,
                                    point = currentHandleTopLeft.value + touchOffset,
                                )
                        },
                        onDragEnd = { previousAngle = null },
                        onDragCancel = { previousAngle = null },
                        onDrag = { change, _ ->
                            val angle =
                                angleBetween(
                                    center = currentElementCenter.value,
                                    point = currentHandleTopLeft.value + change.position,
                                )
                            previousAngle?.let { previous ->
                                viewModel.rotateText(
                                    elementId = element.id,
                                    rotationDelta = shortestRotationDelta(previous, angle),
                                )
                            }
                            previousAngle = angle
                            change.consume()
                        },
                    )
                },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_rotate_right_24),
            contentDescription = stringResource(R.string.rotate_text_action),
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(rotationHandleIconSize),
        )
    }
}

private fun rotate(offset: Offset, rotationDegrees: Float): Offset {
    val radians = Math.toRadians(rotationDegrees.toDouble())
    val cosine = cos(radians).toFloat()
    val sine = sin(radians).toFloat()
    return Offset(
        x = offset.x * cosine - offset.y * sine,
        y = offset.x * sine + offset.y * cosine,
    )
}

private fun angleBetween(center: Offset, point: Offset): Float =
    Math
        .toDegrees(
            atan2(
                y = point.y - center.y,
                x = point.x - center.x,
            ).toDouble(),
        ).toFloat()

private fun shortestRotationDelta(previousAngle: Float, currentAngle: Float): Float =
    (currentAngle - previousAngle + FULL_ROTATION_DEGREES + HALF_ROTATION_DEGREES) %
        FULL_ROTATION_DEGREES - HALF_ROTATION_DEGREES
