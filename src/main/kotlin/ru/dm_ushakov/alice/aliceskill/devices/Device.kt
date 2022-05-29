package ru.dm_ushakov.alice.aliceskill.devices

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.DeviceCapability
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty

interface Device: Lifecycle {
    val updateNotificationEmitter: NotificationEmitter<Device>

    val id: String
    val name: String
    val description: String?
    val room: String?
    val type: DeviceType
    val capabilities: List<DeviceCapability>
    val properties: List<DeviceProperty>

    fun getDescriptionJson(): JsonNode
    fun getStateJson(): JsonNode
    fun changeCapabilityState(changeState: JsonNode): JsonNode
}