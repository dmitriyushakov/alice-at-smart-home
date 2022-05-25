package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty

interface DeviceCapability: DeviceProperty {
    fun changeCapabilityState(changeState: JsonNode): JsonNode
}