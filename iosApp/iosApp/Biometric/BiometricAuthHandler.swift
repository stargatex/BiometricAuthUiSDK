//
//  BiometricAuthHandler.swift
//  iosApp
//
//  Created by Lahiru Jayawickrama on 2025-06-16.
//

import Foundation
import LocalAuthentication

@objc public enum BiometricAuthResult: Int {
    case success
    case canceled
    case fallback
    case attemptExhausted
    case failed
    case error
}

@objc public enum BiometricAvailability: Int {
    case available
    case noEnrollment
    case noHardware
    case unknown
}

@objc public class BiometricAuthHelper: NSObject {

    @objc public func checkAvailability() -> BiometricAvailability {
        let context = LAContext()
        var error: NSError?

        let canEvaluate = context.canEvaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, error: &error)

        if canEvaluate {
            return .available
        } else if let err = error {
            switch err.code {
            case LAError.biometryNotEnrolled.rawValue:
                return .noEnrollment
            case LAError.biometryNotAvailable.rawValue:
                return .noHardware
            default:
                return .unknown
            }
        }
        return .unknown
    }

    @objc public func authenticate(reason: String, completion: @escaping (BiometricAuthResult, String?) -> Void) {
        let context = LAContext()
        context.evaluatePolicy(.deviceOwnerAuthenticationWithBiometrics, localizedReason: reason) { success, error in
            DispatchQueue.main.async {
                if success {
                    completion(.success, nil)
                } else if let err = error as? LAError {
                    switch err.code {
                    case .userCancel:
                        completion(.canceled, nil)
                    case .userFallback:
                        completion(.fallback, nil)
                    case .biometryLockout:
                        completion(.attemptExhausted, nil)
                    default:
                        completion(.error, err.localizedDescription)
                    }
                } else {
                    completion(.failed, nil)
                }
            }
        }
    }
}
