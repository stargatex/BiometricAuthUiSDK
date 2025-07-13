package com.stargatex.mobile.lib.pinauth.domain.model

import androidx.compose.runtime.Composable
import com.stargatex.mobile.lib.pinlock.resources.Res
import com.stargatex.mobile.lib.pinlock.resources.pin_description
import com.stargatex.mobile.lib.pinlock.resources.pin_subtitle
import com.stargatex.mobile.lib.pinlock.resources.pin_title
import org.jetbrains.compose.resources.stringResource

/**
 * @author Lahiru Jayawickrama (lahirujay)
 * @version 1.0
 */
public data class PinPromptConfig(
    val title: String,
    val subtitle: String,
    val description: String,
){
    public companion object {
        @Composable
        public fun default(): PinPromptConfig = PinPromptConfig(
            title = stringResource(Res.string.pin_title),
            subtitle = stringResource(Res.string.pin_subtitle),
            description = stringResource(Res.string.pin_description)
        )
    }
}
