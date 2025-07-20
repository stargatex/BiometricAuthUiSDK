package com.stargatex.mobile.lib.pinauth.ui.model.config

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.pinlock.resources.Res
import com.stargatex.mobile.lib.pinlock.resources.pin_description
import com.stargatex.mobile.lib.pinlock.resources.pin_error_prefix
import com.stargatex.mobile.lib.pinlock.resources.pin_screen_title

import org.jetbrains.compose.resources.stringResource

/**
 * Data class representing the text configuration for the PIN UI.
 * This class holds the customizable text elements displayed on the PIN entry screen.
 *
 * @property screenTitle The title displayed at the top of the PIN screen.
 * @property screenNote A descriptive note or instruction displayed below the title.
 * @property errorPrefix A prefix string used for displaying error messages.
 *
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class PinUiTextConfig(
    val screenTitle: String,
    val screenNote: String,
    val errorPrefix: String
) {
    public companion object {
        /**
         * Creates a default [PinUiTextConfig] with localized strings for the screen title, note, and error prefix.
         * This function should be called within a Composable context to access string resources.
         *
         * @return A [PinUiTextConfig] instance with default text values.
         */
        @Composable
        public fun default(): PinUiTextConfig = PinUiTextConfig(
            screenTitle = stringResource(Res.string.pin_screen_title),
            screenNote = stringResource(Res.string.pin_description),
            errorPrefix = stringResource(Res.string.pin_error_prefix)
        )
    }
}
