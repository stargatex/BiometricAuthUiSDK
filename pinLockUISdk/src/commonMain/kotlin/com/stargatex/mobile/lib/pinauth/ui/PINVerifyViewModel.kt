package com.stargatex.mobile.lib.pinauth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargatex.mobile.lib.pinauth.domain.usecase.PINUseCase
import com.stargatex.mobile.lib.pinauth.ui.model.state.PinScreenState
import com.stargatex.mobile.lib.pinauth.ui.model.state.PinUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
internal class PINVerifyViewModel(
    private val pinUseCase: PINUseCase
) : ViewModel() {

    private val _pinScreenUiState = MutableStateFlow<PinScreenState>(PinScreenState.Loading)
    val pinScreenUiState = _pinScreenUiState.asStateFlow()

    private val _pinUiEvents = MutableSharedFlow<PinUiEvent>()
    val pinUiEvents = _pinUiEvents.asSharedFlow()

    private var tempNewPin: String? = null

    init {
        viewModelScope.launch {
            val oldPin = pinUseCase.fetchSavedPINUseCase.invoke()
            _pinScreenUiState.value =
                if (oldPin != null) PinScreenState.Unlock else PinScreenState.Save
        }
    }

    fun afterPinEntered(pin: String) {
        when (_pinScreenUiState.value) {
            is PinScreenState.Save -> {
                tempNewPin = pin
                _pinScreenUiState.value = PinScreenState.Confirm
            }

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

            else -> {}
        }
    }
}