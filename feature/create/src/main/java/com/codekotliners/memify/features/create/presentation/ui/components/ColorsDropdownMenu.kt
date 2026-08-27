package com.codekotliners.memify.features.create.presentation.ui.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.codekotliners.memify.features.create.R

private const val GRID_COLUMNS = 4
private const val SV_BOX_SIZE_DP = 200
private const val HUE_MAX = 360f
private const val FULL_SATURATION_VALUE = 1f
private const val SV_INDICATOR_SIZE_DP = 16
private const val HUE_THUMB_WIDTH_DP = 6

private val palette =
    listOf(
        Color.Black,
        Color(0xFF3D3D3D),
        Color(0xFF9E9E9E),
        Color.White,
        Color(0xFFE53935),
        Color(0xFFFF9800),
        Color(0xFFFDD835),
        Color(0xFF43A047),
        Color(0xFF00ACC1),
        Color(0xFF1E88E5),
        Color(0xFF3949AB),
        Color(0xFF8E24AA),
        Color(0xFFD81B60),
        Color(0xFF6D4C41),
        Color.Cyan,
        Color.Magenta,
    )

@Composable
fun ColorsDropdownMenu(
    showColors: Boolean,
    onShowColorsFalse: () -> Unit,
    onChangeSelectedColor: (color: Color) -> Unit,
    currentColor: Color = Color.Unspecified,
) {
    var showCustomPicker by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = showColors,
        onDismissRequest = onShowColorsFalse,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            palette.chunked(GRID_COLUMNS).forEach { rowColors ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    rowColors.forEach { color ->
                        ColorSwatch(
                            color = color,
                            isSelected = color == currentColor,
                            onClick = {
                                onChangeSelectedColor(color)
                                onShowColorsFalse()
                            },
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            CustomColorSwatch(onClick = { showCustomPicker = true })
        }
    }

    if (showCustomPicker) {
        ColorPickerDialog(
            initialColor = if (currentColor.isSpecified) currentColor else Color.Black,
            onColorSelected = {
                onChangeSelectedColor(it)
                showCustomPicker = false
                onShowColorsFalse()
            },
            onDismiss = { showCustomPicker = false },
        )
    }
}

@Composable
private fun ColorSwatch(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(color)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.35f),
                    shape = CircleShape,
                ).clickable(onClick = onClick),
    )
}

@Composable
private fun CustomColorSwatch(onClick: () -> Unit) {
    Row(
        modifier =
            Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(onClick = onClick)
                .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(26.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.sweepGradient(
                            listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red),
                        ),
                    ).border(1.dp, Color.Gray.copy(alpha = 0.4f), CircleShape),
        )
        Text(
            text = stringResource(R.string.custom_color_action),
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ColorPickerDialog(
    initialColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit,
) {
    val initialHsv =
        remember(initialColor) {
            val hsv = FloatArray(3)
            AndroidColor.colorToHSV(initialColor.toArgb(), hsv)
            hsv
        }
    var hue by remember { mutableFloatStateOf(initialHsv[0]) }
    var saturation by remember { mutableFloatStateOf(initialHsv[1]) }
    var value by remember { mutableFloatStateOf(initialHsv[2]) }

    val pickedColor = Color(AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value)))

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp), color = MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.custom_color_action),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp),
                )

                SaturationValueBox(
                    hue = hue,
                    saturation = saturation,
                    value = value,
                    onChange = { s, v ->
                        saturation = s;
                        value = v
                    },
                )

                Spacer(Modifier.height(16.dp))

                HueSlider(hue = hue, onHueChange = { hue = it })

                Spacer(Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier =
                            Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(pickedColor)
                                .border(1.dp, Color.Gray.copy(alpha = 0.4f), CircleShape),
                    )
                    Text(
                        text = "#" + String.format("%06X", pickedColor.toArgb() and 0xFFFFFF),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel_action)) }
                    Button(onClick = { onColorSelected(pickedColor) }) { Text(stringResource(R.string.apply_action)) }
                }
            }
        }
    }
}

@Composable
private fun SaturationValueBox(
    hue: Float,
    saturation: Float,
    value: Float,
    onChange: (saturation: Float, value: Float) -> Unit,
) {
    val boxSizeDp = SV_BOX_SIZE_DP.dp
    val pureHueColor = Color(AndroidColor.HSVToColor(floatArrayOf(hue, FULL_SATURATION_VALUE, FULL_SATURATION_VALUE)))

    Box(
        modifier =
            Modifier
                .size(boxSizeDp)
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(listOf(Color.White, pureHueColor)))
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
                .pointerInput(hue) {
                    detectTapGestures { offset ->
                        val x = offset.x.coerceIn(0f, size.width.toFloat())
                        val y = offset.y.coerceIn(0f, size.height.toFloat())
                        onChange(x / size.width, 1f - y / size.height)
                    }
                }.pointerInput(hue) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val x = change.position.x.coerceIn(0f, size.width.toFloat())
                        val y = change.position.y.coerceIn(0f, size.height.toFloat())
                        onChange(x / size.width, 1f - y / size.height)
                    }
                },
    ) {
        val indicatorOffset = Offset(saturation * boxSizeDp.value, (1f - value) * boxSizeDp.value)
        Box(
            modifier =
                Modifier
                    .offset(
                        x = indicatorOffset.x.dp - (SV_INDICATOR_SIZE_DP / 2).dp,
                        y = indicatorOffset.y.dp - (SV_INDICATOR_SIZE_DP / 2).dp,
                    ).size(SV_INDICATOR_SIZE_DP.dp)
                    .border(2.dp, Color.White, CircleShape)
                    .border(1.dp, Color.Black.copy(alpha = 0.3f), CircleShape),
        )
    }
}

@Composable
private fun HueSlider(hue: Float, onHueChange: (Float) -> Unit) {
    val hueColors =
        remember {
            (0..360 step 30).map {
                Color(AndroidColor.HSVToColor(floatArrayOf(it.toFloat(), FULL_SATURATION_VALUE, FULL_SATURATION_VALUE)))
            }
        }

    Box(
        modifier =
            Modifier
                .size(width = SV_BOX_SIZE_DP.dp, height = 26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Brush.horizontalGradient(hueColors))
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val x = offset.x.coerceIn(0f, size.width.toFloat())
                        onHueChange((x / size.width) * HUE_MAX)
                    }
                }.pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        val x = change.position.x.coerceIn(0f, size.width.toFloat())
                        onHueChange((x / size.width) * HUE_MAX)
                    }
                },
    ) {
        val thumbX = (hue / HUE_MAX) * SV_BOX_SIZE_DP
        Box(
            modifier =
                Modifier
                    .offset(x = thumbX.dp - (HUE_THUMB_WIDTH_DP / 2).dp)
                    .size(width = HUE_THUMB_WIDTH_DP.dp, height = 26.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color.White)
                    .border(1.dp, Color.Black.copy(alpha = 0.3f), RoundedCornerShape(3.dp)),
        )
    }
}
