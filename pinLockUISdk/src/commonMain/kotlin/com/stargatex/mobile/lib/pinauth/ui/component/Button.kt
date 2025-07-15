package com.stargatex.mobile.lib.pinauth.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

@Composable
internal fun NumberButton(
    modifier: Modifier = Modifier,
    number: Int = 0,
    onClick: () -> Unit,
    enable: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(true),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        enabled = enable
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
    }
}

@Composable
internal fun ActionButton(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
    enable: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    border: BorderStroke? = ButtonDefaults.outlinedButtonBorder(true),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        border = border,
        colors = colors,
        contentPadding = contentPadding,
        enabled = enable
    ) {
        if (icon != null) {
            icon()
        }

        if (icon != null && label != null) {
            Spacer(Modifier.size(ButtonDefaults.MinHeight))
        }

        if (label != null) {
            label()
        }
    }
}

@Preview
@Composable
internal fun previewNumberButton() {
    NumberButton(
        onClick = {},
        number = 5,
        modifier = Modifier
    )
}