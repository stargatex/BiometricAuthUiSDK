# Changelog

## [v1.3.0] - 2026-07-14
### Changed
- Improved compatibility (Android)

## [v1.3.0] - 2026-06-09
### Changed
- Refactored biometric and PIN authentication initialization for improved lifecycle management.
- Refactored biometric and PIN authentication API for improved clarity and maintainability.
- Optimized biometric authentication initialization and improved resource management.

## [v1.2.1] - 2026-06-09
### Added
- Added `BioKeyX.Authenticate(...)` for headless biometric authentication flows without rendering the SDK biometric screen.
- Added `additionalOptions` slot to `PINKeyX.Compose(...)` to support custom actions below the PIN keypad.

### Changed
- Updated biometric verification flow and sample app to support developer-owned authentication screens.
- Expanded `README.md` with usage examples for headless biometric authentication and custom PIN action options.

## [v1.2.0] - 2026-06-05
### Added
- Added `isPinSet` API to check whether a PIN is already configured.
- Added fallback button in biometric authentication flow to allow PIN unlock.

### Changed
- Updated biometric authentication UI.
- Introduced `PinMode` and updated PIN flow handling for `UNLOCK`, `SET`, and `CHANGE` operations.

## [v1.1.0-alpha] - 2025-07-20
### Added
- Combined release for Biometric Lock & PIN Lock
- Added KDoc for public methods

## [v1.0.0-p-alpha] - 2025-07-17
### Added
- Initial release for PIN Lock

## [v1.0.0-alpha] - 2025-07-15
### Added
- Initial release for Biometric Lock
