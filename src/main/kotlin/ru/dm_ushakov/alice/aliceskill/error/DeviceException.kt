package ru.dm_ushakov.alice.aliceskill.error

class DeviceException(val errorType: DeviceErrorType, message: String? = null, cause: Throwable? = null) :
    Exception(message, cause)