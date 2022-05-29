package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType
import ru.dm_ushakov.alice.aliceskill.error.DeviceException
import ru.dm_ushakov.alice.aliceskill.notifications.BasicNotificationEmitter
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class BaseCapability: DeviceCapability {
    override val updateNotificationEmitter: NotificationEmitter<DeviceCapability> = BasicNotificationEmitter()

    protected abstract fun executeChangingCapabilityState(changeState: JsonNode)

    final override fun changeCapabilityState(changeState: JsonNode): JsonNode {
        var deviceException: DeviceException? = null
        val instance: String = changeState.get("state").get("instance").asText()

        try {
            executeChangingCapabilityState(changeState)
        } catch (ex: DeviceException) {
            deviceException = ex
        } catch (ex: Exception) {
            val message = "Internal capability exception - " + ex.stackTraceToString()
            deviceException = DeviceException(DeviceErrorType.InternalError, message)
        }

        return makeJsonObject {
            put("type", type)

            putObject("state") {
                put("instance", instance)

                putObject("action_result") {
                    if (deviceException == null) {
                        put("status", "DONE")
                    } else {
                        put("status", "ERROR")
                        put("error_code", deviceException.message)
                        deviceException.message?.let { put("error_message", it) }
                    }
                }
            }
        }
    }

    override fun hashCode() = keys.sumOf { it.hashCode() }
    override fun equals(other: Any?) = this === other
}