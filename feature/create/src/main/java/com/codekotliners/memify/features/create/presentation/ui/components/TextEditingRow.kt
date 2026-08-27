package com.codekotliners.memify.features.create.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codekotliners.memify.core.theme.FontFamilyImpact
import com.codekotliners.memify.features.create.R
import com.codekotliners.memify.features.create.presentation.viewmodel.CanvasViewModel

@Composable
fun TextEditingRow(viewModel: CanvasViewModel) {
    val isEditingExisting = viewModel.selectedElementId != null

    Row(
        modifier =
            Modifier
                .padding(10.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ColorSelectionButton(viewModel)
        FontFamilySelectionButton(viewModel)
        FontWeightSelectionButton(viewModel)
        TextAlignSelectionButtons(viewModel)
        OutlineToggleButton(viewModel)

        if (isEditingExisting) {
            Divider()
            IconActionButton(
                iconResource = R.drawable.round_edit_24,
                contentDescription = stringResource(R.string.edit_text_action),
                onClick = { viewModel.startEditingSelectedText() },
            )
            IconActionButton(
                iconResource = R.drawable.round_content_copy_24,
                contentDescription = stringResource(R.string.duplicate_text_action),
                onClick = { viewModel.duplicateSelectedText() },
            )
            IconActionButton(
                iconResource = R.drawable.baseline_delete_outline_24,
                contentDescription = stringResource(R.string.delete_text_action),
                onClick = { viewModel.deleteSelectedElement() },
            )
        }

        DoneButton(onClick = { viewModel.toggleWritingMode() })
    }
}

@Composable
internal fun Divider() {
    Box(
        modifier =
            Modifier
                .width(1.dp)
                .height(24.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)),
    )
}

@Composable
internal fun IconActionButton(iconResource: Int, contentDescription: String, onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(iconResource),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
internal fun DoneButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "✓",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun ColorSelectionButton(viewModel: CanvasViewModel) {
    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.showColors = !viewModel.showColors },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "A",
                color = viewModel.currentTextColor.value,
                fontSize = 18.sp,
                modifier = Modifier.padding(4.dp),
            )
        }

        ColorsDropdownMenu(
            showColors = viewModel.showColors,
            onShowColorsFalse = { viewModel.showColors = false },
            onChangeSelectedColor = { viewModel.setTextColor(it) },
            currentColor = viewModel.currentTextColor.value,
        )
    }
}

@Composable
private fun FontFamilySelectionButton(viewModel: CanvasViewModel) {
    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.showFonts = !viewModel.showFonts },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "F",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontFamily = viewModel.currentFontFamily.value,
                modifier = Modifier.padding(4.dp),
            )
        }

        FontsDropdownMenu(viewModel)
    }
}

@Composable
private fun FontWeightSelectionButton(viewModel: CanvasViewModel) {
    Box {
        Box(
            modifier =
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable { viewModel.showWeights = !viewModel.showWeights },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "W",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = viewModel.currentFontWeight.value,
                modifier = Modifier.padding(4.dp),
            )
        }

        WeightsDropdownMenu(viewModel)
    }
}

@Composable
private fun TextAlignSelectionButtons(viewModel: CanvasViewModel) {
    val options =
        listOf(
            Triple(TextAlign.Left, "L", stringResource(R.string.align_left_action)),
            Triple(TextAlign.Center, "C", stringResource(R.string.align_center_action)),
            Triple(TextAlign.Right, "R", stringResource(R.string.align_right_action)),
        )

    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        options.forEach { (align, label, _) ->
            val isActive = viewModel.currentTextAlign.value == align
            Box(
                modifier =
                    Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable { viewModel.setTextAlign(align) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun OutlineToggleButton(viewModel: CanvasViewModel) {
    val isActive = viewModel.currentTextHasOutline
    Box(
        modifier =
            Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Transparent)
                .clickable { viewModel.toggleTextOutline() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "S",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun FontsDropdownMenu(viewModel: CanvasViewModel) {
    val fonts =
        listOf(
            FontFamilyImpact to stringResource(R.string.Impact),
            FontFamily.Default to stringResource(R.string.Default),
            FontFamily.SansSerif to stringResource(R.string.Sans),
            FontFamily.Serif to stringResource(R.string.Serif),
            FontFamily.Monospace to stringResource(R.string.Mono),
            FontFamily.Cursive to stringResource(R.string.Cursive),
        )

    DropdownMenu(
        expanded = viewModel.showFonts,
        onDismissRequest = { viewModel.showFonts = false },
        shape = RoundedCornerShape(20.dp),
    ) {
        fonts.forEach { (family, name) ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = name,
                        fontFamily = family,
                        color =
                            if (family == viewModel.currentFontFamily.value) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    )
                },
                onClick = {
                    viewModel.setFontFamily(family)
                    viewModel.showFonts = false
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun WeightsDropdownMenu(viewModel: CanvasViewModel) {
    val weights =
        listOf(
            FontWeight.Light to stringResource(R.string.Light),
            FontWeight.Normal to stringResource(R.string.Normal),
            FontWeight.Medium to stringResource(R.string.Medium),
            FontWeight.Bold to stringResource(R.string.Bold),
        )

    DropdownMenu(
        expanded = viewModel.showWeights,
        onDismissRequest = { viewModel.showWeights = false },
        shape = RoundedCornerShape(20.dp),
    ) {
        weights.forEach { (weight, name) ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = name,
                        fontWeight = weight,
                        color =
                            if (weight == viewModel.currentFontWeight.value) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                    )
                },
                onClick = {
                    viewModel.setFontWeight(weight)
                    viewModel.showWeights = false
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
