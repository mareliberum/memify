package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowSlider(
    viewModel: CanvasViewModel,
    parameter: MutableFloatState,
    modifier: Modifier = Modifier.fillMaxWidth(),
    valueRange: ClosedFloatingPointRange<Float> = 5f..99f,
    showValueLabel: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onValueChange: () -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent?.invoke()

        Slider(
            value = parameter.floatValue,
            onValueChange = {
                parameter.floatValue = it
                onValueChange()
            },
            onValueChangeFinished = {
                viewModel.showTextPreview = false
            },
            valueRange = valueRange,
            modifier = Modifier.weight(1f),
            thumb = {
                Box(
                    modifier =
                        Modifier
                            .size(10.dp)
                            .offset(y = 3.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape,
                            ),
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors =
                        SliderDefaults.colors(
                            activeTrackColor = MaterialTheme.colorScheme.primary,
                            inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                        ),
                    modifier =
                        Modifier
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                )
            },
        )

        trailingContent?.invoke()

        if (showValueLabel) {
            Text(
                text = parameter.floatValue.roundToInt().toString(),
                fontSize = 12.sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier =
                    Modifier
                        .widthIn(min = 22.dp)
                        .padding(start = 4.dp),
            )
        }
    }
}
