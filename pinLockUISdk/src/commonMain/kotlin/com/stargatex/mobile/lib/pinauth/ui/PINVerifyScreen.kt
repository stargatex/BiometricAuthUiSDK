package com.stargatex.mobile.lib.pinauth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stargatex.mobile.lib.pinauth.domain.model.LockConfig
import com.stargatex.mobile.lib.pinauth.domain.model.PinPromptConfig
import com.stargatex.mobile.lib.pinauth.ui.component.NumericKeyPad
import com.stargatex.mobile.lib.pinauth.ui.model.config.PinUiTextConfig
import com.stargatex.mobile.lib.pinauth.ui.model.state.PinScreenState
import com.stargatex.mobile.lib.pinauth.ui.model.state.PinUiEvent
import com.stargatex.mobile.lib.pinlock.resources.Res
import com.stargatex.mobile.lib.pinlock.resources.pin_confirm_pin
import com.stargatex.mobile.lib.pinlock.resources.pin_enter_pin
import com.stargatex.mobile.lib.pinlock.resources.pin_set_new_pin
import org.jetbrains.compose.resources.stringResource


/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */

@Composable
internal fun PinVerifyScreen(
    verifyViewModel: PINVerifyViewModel,
    shouldCheckAvailability: Boolean = true,
    lockConfig: LockConfig = LockConfig(PinPromptConfig.default()),
    uiTextConfig: PinUiTextConfig = PinUiTextConfig.default(),
    onUnlockSuccess: () -> Unit = {},
    onFallback: () -> Unit = {},
    onAuthFailure: (String) -> Unit = {}
) {
    val screenUiState by verifyViewModel.pinScreenUiState.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var pin by remember { mutableStateOf("") }

    fun updatePin(newPin: String) {
        if (newPin.length <= 4) {
            pin = newPin
        }
        if (pin.length == 4) {
            verifyViewModel.afterPinEntered(pin)
            pin = ""
        }
    }

    LaunchedEffect(Unit) {
        verifyViewModel.pinUiEvents.collect { event ->
            when (event) {
                is PinUiEvent.Error -> errorMessage = event.message
                is PinUiEvent.Success -> {
                    onUnlockSuccess()
                    errorMessage = "onUnlockSuccess"
                }
            }
        }
    }

    val title: String = when (screenUiState) {
        is PinScreenState.Confirm -> stringResource(Res.string.pin_confirm_pin)
        is PinScreenState.Loading -> ""
        is PinScreenState.Save -> stringResource(Res.string.pin_set_new_pin)
        is PinScreenState.Unlock -> stringResource(Res.string.pin_enter_pin)
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Text(uiTextConfig.screenTitle, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text(title)
        Spacer(Modifier.height(16.dp))
        Text(pin, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        NumericKeyPad(
            modifier = Modifier.fillMaxWidth(),
            onNumericKeyClick = {
                println("Pressed $it")
                updatePin(pin + it.toString())
            },
            onBackSpaceClick = {
                updatePin(pin.dropLast(1))
            }
        )
        errorMessage?.let {
            Spacer(Modifier.height(16.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(16.dp))
        //Text(uiTextConfig.screenNote)
    }

}