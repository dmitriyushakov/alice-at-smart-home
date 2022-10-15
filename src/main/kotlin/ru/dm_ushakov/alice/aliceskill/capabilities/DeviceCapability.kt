package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.devices.DeviceContentKey
import ru.dm_ushakov.alice.aliceskill.devices.Lifecycle
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter

interface DeviceCapability: Lifecycle {
    @get:JsonIgnore
    val updateNotificationEmitter: NotificationEmitter<DeviceCapability>
    val keys: List<DeviceContentKey>
    val type: String
    val retrievable: Boolean
    val reportable: Boolean

    @JsonIgnore
    fun getDescriptionJson(): JsonNode
    @JsonIgnore
    fun getStateJson(): JsonNode
    fun changeCapabilityState(changeState: JsonNode): JsonNode
}