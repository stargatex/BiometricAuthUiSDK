package com.stargatex.mobile.lib.pinauth.domain.model

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.pinlock.resources.Res
import com.stargatex.mobile.lib.pinlock.resources.pin_description
import com.stargatex.mobile.lib.pinlock.resources.pin_subtitle
import com.stargatex.mobile.lib.pinlock.resources.pin_title
import org.jetbrains.compose.resources.stringResource

/**
 * Data class representing the configuration for a PIN prompt.
 *
 * This class holds the text elements to be displayed in a PIN input UI.
 *
 * @property title The main title text for the PIN prompt.
 * @property subtitle The subtitle text, usually displayed below the title.
 * @property description Additional descriptive text for the PIN prompt.
 *
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class PinPromptConfig(
    val title: String,
    val subtitle: String,
    val description: String,
){
    public companion object {
        /**
         * Returns a default [PinPromptConfig] with localized strings for title, subtitle, and description.
         * This function should be called within a Composable context.
         *
         * @return A [PinPromptConfig] instance with default localized text.
         */
        @Composable
        public fun default(): PinPromptConfig = PinPromptConfig(
            title = stringResource(Res.string.pin_title),
            subtitle = stringResource(Res.string.pin_subtitle),
            description = stringResource(Res.string.pin_description)
        )
    }
}
