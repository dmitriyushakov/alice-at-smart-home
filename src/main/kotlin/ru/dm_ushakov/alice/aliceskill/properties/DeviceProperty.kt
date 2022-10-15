package ru.dm_ushakov.alice.aliceskill.properties

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.devices.DeviceContentKey
import ru.dm_ushakov.alice.aliceskill.devices.Lifecycle
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter

interface DeviceProperty: Lifecycle {
    @get:JsonIgnore
    val updateNotificationEmitter: NotificationEmitter<DeviceProperty>
    val keys: List<DeviceContentKey>
    val type: String
    val retrievable: Boolean
    val reportable: Boolean

    @JsonIgnore
    fun getDescriptionJson(): JsonNode
    @JsonIgnore
    fun getStateJson(): JsonNode
}