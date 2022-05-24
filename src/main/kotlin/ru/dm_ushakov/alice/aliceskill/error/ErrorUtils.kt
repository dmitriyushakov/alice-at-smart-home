package ru.dm_ushakov.alice.aliceskill.error

fun deviceError(errorType: DeviceErrorType, errorMessage: String? = null, caused: Throwable? = null): Nothing {
    throw DeviceException(errorType, errorMessage, caused)
}

fun invalidValue(errorMessage: String? = null, caused: Throwable? = null): Nothing =
    deviceError(DeviceErrorType.InvalidValue, errorMessage, caused)