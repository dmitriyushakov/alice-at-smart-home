package ru.dm_ushakov.alice.aliceskill.controllers.requests

import ru.dm_ushakov.alice.aliceskill.devices.DeviceType

data class CreateOrUpdateDeviceRequest(val name: String, val type: DeviceType) {
    constructor(name: String, type: String): this(name, DeviceType.getDeviceType(type))
}