package ru.dm_ushakov.alice.aliceskill.error

enum class DeviceErrorType(val errorCode: String) {
    DoorOpen("DOOR_OPEN"),
    LidOpen("LID_OPEN"),
    RemoteControlDisabled("REMOTE_CONTROL_DISABLED"),
    NotEnoughWater("NOT_ENOUGH_WATER"),
    LowChargeLevel("LOW_CHARGE_LEVEL"),
    ContainerFull("CONTAINER_FULL"),
    ContainerEmpty("CONTAINER_EMPTY"),
    DripTrayFull("DRIP_TRAY_FULL"),
    DeviceStuck("DEVICE_STUCK"),
    DeviceOff("DEVICE_OFF"),
    FirmwareOutOfDate("FIRMWARE_OUT_OF_DATE"),
    NotEnoughDetergent("NOT_ENOUGH_DETERGENT"),
    HumanInvolvementNeeded("HUMAN_INVOLVEMENT_NEEDED"),
    DeviceUnreachable("DEVICE_UNREACHABLE"),
    DeviceBusy("DEVICE_BUSY"),
    InternalError("INTERNAL_ERROR"),
    InvalidAction("INVALID_ACTION"),
    InvalidValue("INVALID_VALUE"),
    NotSupportedInCurrentMode("NOT_SUPPORTED_IN_CURRENT_MODE"),
    AccountLinkingError("ACCOUNT_LINKING_ERROR");

    override fun toString() = errorCode

    companion object {
        private val codeToError: Map<String, DeviceErrorType> = values().associateBy { it.errorCode }
        fun getError(errorCode: String) = codeToError[errorCode] ?: error("Unknown error code - $errorCode!")
    }
}