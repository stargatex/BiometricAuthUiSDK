package com.stargatex.mobile.lib.pinauth.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

@Composable
internal fun NumericKeyPad(
    modifier: Modifier = Modifier,
    onNumericKeyClick: (Int) -> Unit,
    onBackSpaceClick: () -> Unit,
) {
    var buttonSize by remember { mutableStateOf(IntSize.Zero) }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(35.dp)
    ) {
        for (row in 0 until 3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(50.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                for (col in 1..3) {
                    val number = row * 3 + col
                    NumberButton(
                        number = number,
                        onClick = { onNumericKeyClick(number) },
                    )
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
        ) {

            if (buttonSize != IntSize.Zero) {
                Spacer(
                    Modifier.size(
                        width = buttonSize.width.dp,
                        height = buttonSize.height.dp
                    )
                )
            } else {
                Spacer(Modifier.size(ButtonDefaults.MinWidth, ButtonDefaults.MinHeight))
            }

            NumberButton(
                number = 0,
                onClick = { onNumericKeyClick(0) }
            )

            ActionButton(
                onClick = {
                    onBackSpaceClick()
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Backspace,
                        contentDescription = ""
                    )
                }
            )
        }
    }
}