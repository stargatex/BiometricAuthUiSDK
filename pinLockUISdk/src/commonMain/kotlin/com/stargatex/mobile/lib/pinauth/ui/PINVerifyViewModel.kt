package com.stargatex.mobile.lib.pinauth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargatex.mobile.lib.pinauth.domain.model.PinMode
import com.stargatex.mobile.lib.pinauth.domain.usecase.PINUseCase
import com.stargatex.mobile.lib.pinauth.ui.model.state.PinScreenState
import com.stargatex.mobile.lib.pinauth.ui.model.state.PinUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing PIN verification screen states and logic.
 * Handles three modes: UNLOCK, SET, and CHANGE.
 *
 * @param pinUseCase Use case for PIN operations (save, fetch, clear).
 * @param mode The PIN operation mode (UNLOCK, SET, or CHANGE). Defaults to auto-detection.
 *
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal class PINVerifyViewModel(
    private val pinUseCase: PINUseCase,
    private val mode: PinMode? = null
) : ViewModel() {

    private val _pinScreenUiState = MutableStateFlow<PinScreenState>(PinScreenState.Loading)
    val pinScreenUiState = _pinScreenUiState.asStateFlow()

    private val _pinUiEvents = MutableSharedFlow<PinUiEvent>()
    val pinUiEvents = _pinUiEvents.asSharedFlow()

    private var tempNewPin: String? = null
    private var tempOldPin: String? = null

    init {
        viewModelScope.launch {
            val oldPin = pinUseCase.fetchSavedPINUseCase.invoke()

            // Determine the initial state based on mode or current state
            val initialState = when {
                mode == PinMode.CHANGE -> {
                    // Change mode: always start with verifying old PIN
                    PinScreenState.VerifyOldForChange
                }
                mode == PinMode.SET -> {
                    // Set mode: always show save screen
                    PinScreenState.Save
                }
                mode == PinMode.UNLOCK -> {
                    // Unlock mode: always show unlock screen
                    PinScreenState.Unlock
                }
                else -> {
                    // Auto-detect based on whether PIN exists
                    if (oldPin != null) PinScreenState.Unlock else PinScreenState.Save
                }
            }

            _pinScreenUiState.value = initialState
        }
    }

    fun afterPinEntered(pin: String) {
        when (_pinScreenUiState.value) {
            // SET mode: User sets a new PIN for the first time
            is PinScreenState.Save -> {
                tempNewPin = pin
                _pinScreenUiState.value = PinScreenState.Confirm
            }

            // SET mode: User confirms the new PIN
            is PinScreenState.Confirm -> {
                if (tempNewPin == pin) {
                    viewModelScope.launch {
                        pinUseCase.savedPINUseCase.invoke(pin)
                        _pinUiEvents.emit(PinUiEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _pinUiEvents.emit(PinUiEvent.Error("PINs do not match"))
                    }
                }
            }

            // UNLOCK mode: User enters existing PIN to verify
            is PinScreenState.Unlock -> {
                viewModelScope.launch {
                    val savedPin = pinUseCase.fetchSavedPINUseCase.invoke()
                    if (savedPin == pin) {
                        _pinUiEvents.emit(PinUiEvent.Success)
                    } else {
                        _pinUiEvents.emit(PinUiEvent.Error("Incorrect PIN"))
                    }
                }
            }

            // CHANGE mode: Step 1 - Verify the old PIN before allowing change
            is PinScreenState.VerifyOldForChange -> {
                viewModelScope.launch {
                    val savedPin = pinUseCase.fetchSavedPINUseCase.invoke()
                    if (savedPin == pin) {
                        // Old PIN verified - clear it and move to new PIN setup
                        tempOldPin = pin
                        pinUseCase.clearPINUseCase.invoke()
                        _pinScreenUiState.value = PinScreenState.SaveNew
                    } else {
                        _pinUiEvents.emit(PinUiEvent.Error("Incorrect PIN"))
                    }
                }
            }

            // CHANGE mode: Step 2 - User enters a new PIN (after verification)
            is PinScreenState.SaveNew -> {
                tempNewPin = pin
                _pinScreenUiState.value = PinScreenState.ConfirmNew
            }

            // CHANGE mode: Step 3 - User confirms the new PIN
            is PinScreenState.ConfirmNew -> {
                if (tempNewPin == pin) {
                    viewModelScope.launch {
                        pinUseCase.savedPINUseCase.invoke(pin)
                        _pinUiEvents.emit(PinUiEvent.Success)
                    }
                } else {
                    viewModelScope.launch {
                        _pinUiEvents.emit(PinUiEvent.Error("PINs do not match"))
                    }
                }
            }

            else -> {}
        }
    }
}