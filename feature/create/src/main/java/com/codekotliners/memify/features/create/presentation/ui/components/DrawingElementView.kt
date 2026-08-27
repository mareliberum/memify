package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

private const val HIT_PADDING_PX = 24f

@Composable
fun DrawingElementView(
    element: ColoredLine,
    viewModel: CanvasViewModel,
) {
    val isSelected = viewModel.selectedElementId == element.id
    val density = LocalDensity.current

    val bounds = remember(element.points, element.strokeWidth) { boundsOf(element.points, element.strokeWidth) }
    val localPath = remember(element.points, bounds) { localPathOf(element.points, bounds) }

    val widthDp = with(density) { bounds.width.toDp() }
    val heightDp = with(density) { bounds.height.toDp() }

    Box(
        modifier =
            Modifier
                .graphicsLayer(
                    translationX = bounds.left + element.position.x,
                    translationY = bounds.top + element.position.y,
                    scaleX = element.scale,
                    scaleY = element.scale,
                ).pointerInput(element.id) {
                    detectTapGestures(onTap = { viewModel.selectElement(element) })
                }.pointerInput(element.id) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        if (viewModel.selectedElementId != element.id) {
                            viewModel.selectElement(element)
                        }
                        viewModel.transformDrawing(
                            elementId = element.id,
                            positionDelta = pan,
                            zoom = zoom,
                        )
                    }
                }.size(width = widthDp, height = heightDp),
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
        Canvas(modifier = Modifier.matchParentSize()) {
            drawPath(
                path = localPath,
                color = element.color,
                style = Stroke(width = element.strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round),
            )
        }
    }
}

private fun boundsOf(points: List<Offset>, strokeWidth: Float): Rect {
    if (points.isEmpty()) {
        return Rect(0f, 0f, strokeWidth, strokeWidth)
    }
    var minX = points[0].x
    var maxX = points[0].x
    var minY = points[0].y
    var maxY = points[0].y
    points.forEach { point ->
        if (point.x < minX) minX = point.x
        if (point.x > maxX) maxX = point.x
        if (point.y < minY) minY = point.y
        if (point.y > maxY) maxY = point.y
    }
    val padding = strokeWidth / 2f + HIT_PADDING_PX
    return Rect(minX - padding, minY - padding, maxX + padding, maxY + padding)
}

private fun localPathOf(points: List<Offset>, bounds: Rect): Path =
    Path().apply {
        if (points.isNotEmpty()) {
            moveTo(points.first().x - bounds.left, points.first().y - bounds.top)
            points.forEach { point -> lineTo(point.x - bounds.left, point.y - bounds.top) }
        }
    }
