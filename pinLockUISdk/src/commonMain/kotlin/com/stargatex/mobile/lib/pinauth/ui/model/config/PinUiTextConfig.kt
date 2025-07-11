package com.stargatex.mobile.lib.pinauth.ui.model.config

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.pinlock.resources.Res
import com.stargatex.mobile.lib.pinlock.resources.pin_description
import com.stargatex.mobile.lib.pinlock.resources.pin_error_prefix
import com.stargatex.mobile.lib.pinlock.resources.pin_screen_title

import org.jetbrains.compose.resources.stringResource

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class PinUiTextConfig(
    val screenTitle: String,
    val screenNote: String,
    val errorPrefix: String
) {
    public companion object {
        @Composable
        public fun default(): PinUiTextConfig = PinUiTextConfig(
            screenTitle = stringResource(Res.string.pin_screen_title),
            screenNote = stringResource(Res.string.pin_description),
            errorPrefix = stringResource(Res.string.pin_error_prefix)
        )
    }
}
