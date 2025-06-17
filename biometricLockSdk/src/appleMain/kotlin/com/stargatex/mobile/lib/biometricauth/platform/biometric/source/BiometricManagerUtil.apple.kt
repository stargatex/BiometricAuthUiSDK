package com.stargatex.mobile.lib.biometricauth.platform.biometric.source

import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAuthResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.BiometricAvailabilityResultDto
import com.stargatex.mobile.lib.biometricauth.platform.biometric.model.LockConfigDto
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthHelper
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthResultAttemptExhausted
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthResultCanceled
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthResultError
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthResultFailed
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAuthResultSuccess
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAvailabilityAvailable
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAvailabilityNoEnrollment
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAvailabilityNoHardware
import com.stargatex.mobile.lib.biometricauth.swift.BiometricAvailabilityUnknown
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@OptIn(ExperimentalForeignApi::class)
actual class BiometricManagerUtil(
    private val biometricAuthHelper: BiometricAuthHelper
) {
    actual suspend fun authenticate(lockConfigDto: LockConfigDto): BiometricAuthResultDto =
        suspendCancellableCoroutine { cont ->
            biometricAuthHelper.authenticateWithReason("Authenticate with biometrics") { result, errorMessage ->
                val authResult = when (result) {
                    BiometricAuthResultSuccess -> BiometricAuthResultDto.Success
                    BiometricAuthResultCanceled -> BiometricAuthResultDto.NegativeButtonClick
                    BiometricAuthResultAttemptExhausted -> BiometricAuthResultDto.AttemptExhausted
                    BiometricAuthResultFailed -> BiometricAuthResultDto.Failed
                    BiometricAuthResultError -> BiometricAuthResultDto.Error(
                        errorMessage ?: "Unknown Error"
                    )

                    else -> BiometricAuthResultDto.Error("Unsupported Result")
                }
                cont.resume(authResult)
            }
        }

    actual suspend fun canAuthenticate(): BiometricAvailabilityResultDto {
        return when (biometricAuthHelper.checkAvailability()) {
            BiometricAvailabilityAvailable -> BiometricAvailabilityResultDto.Available
            BiometricAvailabilityNoEnrollment -> BiometricAvailabilityResultDto.NoEnrollment
            BiometricAvailabilityNoHardware -> BiometricAvailabilityResultDto.NoHardware
            BiometricAvailabilityUnknown -> BiometricAvailabilityResultDto.Unknown
            else -> BiometricAvailabilityResultDto.Unknown
        }
    }
}