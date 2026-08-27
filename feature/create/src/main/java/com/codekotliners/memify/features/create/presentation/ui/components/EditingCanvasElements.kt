package com.codekotliners.memify.features.create.presentation.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.codekotliners.memify.features.create.domain.ColoredLine
import com.codekotliners.memify.features.create.domain.TextElement
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

@Composable
fun EditingCanvasElements(viewModel: CanvasViewModel) {
    val currentLine by rememberUpdatedState(viewModel.currentLine)
    val currentLineColor by rememberUpdatedState(viewModel.currentLineColor.value)
    val currentLineWidth by rememberUpdatedState(viewModel.currentLineWidth.floatValue)
    val textElementSizes = remember { mutableStateMapOf<Long, IntSize>() }

    Box(
        modifier =
            Modifier
                .size(viewModel.imageWidth.dp, viewModel.imageHeight.dp)
                .clipToBounds(),
    ) {
        Box(
            modifier =
                Modifier
                    .then(
                        if (viewModel.isPaintingEnabled) {
                            Modifier.drawingCanvas(viewModel)
                        } else {
                            Modifier
                        },
                    ).drawWithCache {
                        val currentPath = if (currentLine.size > 1) createDrawingLinePath(currentLine) else null

                        onDrawWithContent {
                            drawContent()
                            currentPath?.let {
                                drawPath(
                                    path = it,
                                    color = currentLineColor,
                                    style =
                                        Stroke(
                                            width = currentLineWidth,
                                            cap = StrokeCap.Round,
                                            join = StrokeJoin.Round,
                                        ),
                                )
                            }
                        }
                    },
        )

        viewModel.canvasElements.forEach { element ->
            key(element.id) {
                when (element) {
                    is ColoredLine -> DrawingElementView(element = element, viewModel = viewModel)
                    is TextElement ->
                        TextElementView(
                            element = element,
                            viewModel = viewModel,
                            onElementSizeChanged = { size -> textElementSizes[element.id] = size },
                        )
                }
            }
        }

        viewModel.selectedTextElement?.let { element ->
            textElementSizes[element.id]?.let { elementSize ->
                TextRotationHandle(
                    element = element,
                    elementSize = elementSize,
                    viewModel = viewModel,
                )
            }
        }
    }
}

private fun createDrawingLinePath(points: List<Offset>) =
    Path().apply {
        if (points.isNotEmpty()) {
            moveTo(points.first().x, points.first().y)
            points.forEach { point -> lineTo(point.x, point.y) }
        }
    }

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.drawingCanvas(viewModel: CanvasViewModel) =
    Modifier
        .fillMaxWidth()
        .aspectRatio(viewModel.imageWidth / viewModel.imageHeight)
        .pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    viewModel.currentLine.clear()
                    viewModel.currentLine.add(offset)
                },
                onDrag = { change, _ ->
                    change.consume()
                    viewModel.addPointToCurrentLine(change.position)
                },
                onDragEnd = {
                    viewModel.finalizeCurrentLine()
                },
            )
        }
