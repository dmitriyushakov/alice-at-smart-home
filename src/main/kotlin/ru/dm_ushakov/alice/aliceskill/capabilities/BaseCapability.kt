package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import ru.dm_ushakov.alice.aliceskill.config.CustomEntity
import ru.dm_ushakov.alice.aliceskill.config.UserHome
import ru.dm_ushakov.alice.aliceskill.devices.Device
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType
import ru.dm_ushakov.alice.aliceskill.error.DeviceException
import ru.dm_ushakov.alice.aliceskill.notifications.BasicNotificationEmitter
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter
import ru.dm_ushakov.alice.aliceskill.util.json.*
import ru.dm_ushakov.alice.aliceskill.util.onceInit
import kotlin.reflect.KClass

private val logger = LoggerFactory.getLogger(BaseCapability::class.java)

abstract class BaseCapability: DeviceCapability {
    private val notificationSender = BasicNotificationEmitter<DeviceCapability>()
    override val updateNotificationEmitter: NotificationEmitter<DeviceCapability> get() = notificationSender

    override var home: UserHome by onceInit()
    override var device: Device by onceInit()

    fun <T:CustomEntity> getCustomEntity(klass: KClass<T>, entityId: String) =
        home.getCustomEntity(klass, entityId)

    protected abstract fun executeChangingCapabilityState(changeState: JsonNode)

    final override fun changeCapabilityState(changeState: JsonNode): JsonNode {
        var deviceException: DeviceException? = null
        val instance: String = changeState.get("state").get("instance").asText()

        try {
            executeChangingCapabilityState(changeState)
        } catch (ex: DeviceException) {
            logger.error("Device error occurred during change capability state.", ex)
            deviceException = ex
        } catch (ex: Exception) {
            logger.error("Unknown error occurred during change capability state.", ex)
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

    protected fun sendNotification() {
        notificationSender.sendNotification(this)
    }

    override fun hashCode() = keys.sumOf { it.hashCode() }
    override fun equals(other: Any?) = this === other
}