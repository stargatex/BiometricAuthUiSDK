package com.stargatex.mobile.lib.biometricauth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAuthResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.model.BiometricAvailabilityResult
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.BiometricAuthUseCase
import com.stargatex.mobile.lib.biometricauth.domain.biometric.usecase.BiometricAvailabilityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @author Lahiru Jayawickrama (stargatex)
 * @version 1.0.0
 */
class BiometricVerifyViewModel(
    private val biometricAuthUseCase: BiometricAuthUseCase,
    private val biometricAvailabilityUseCase: BiometricAvailabilityUseCase
) : ViewModel() {
    private val _availability = MutableStateFlow<BiometricAvailabilityResult?>(null)
    val availability: StateFlow<BiometricAvailabilityResult?> = _availability

    private val _authResult = MutableStateFlow<BiometricAuthResult?>(null)
    val authResult: StateFlow<BiometricAuthResult?> = _authResult

    fun checkBiometricAvailability() {
        viewModelScope.launch {
            _availability.value = biometricAvailabilityUseCase()
        }
    }

    fun startAuthentication() {
        viewModelScope.launch {
            _authResult.value = biometricAuthUseCase()
        }
    }
}