package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType
import ru.dm_ushakov.alice.aliceskill.error.DeviceException
import ru.dm_ushakov.alice.aliceskill.util.json.*
import java.io.PrintWriter
import java.io.StringWriter

abstract class BaseCapability {
    abstract val type: String
    abstract val retrievable: Boolean
    abstract val reportable: Boolean

    abstract fun getDescriptionJson(): JsonNode
    abstract fun getCapabilityStateJson(): JsonNode
    abstract fun executeChangingCapabilityState(changeState: JsonNode)

    protected fun changeCapabilityState(changeState: JsonNode): JsonNode {
        var deviceException: DeviceException? = null
        val instance: String = changeState.get("state").get("instance").asText()

        try {
            executeChangingCapabilityState(changeState)
        } catch (ex: DeviceException) {
            deviceException = ex
        } catch (ex: Exception) {
            val messageWriter = StringWriter()
            val printMessageWriter = PrintWriter(messageWriter)
            printMessageWriter.print("Internal capability exception - ")
            ex.printStackTrace(printMessageWriter)
            val message = messageWriter.toString()

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
                        put("error_code", deviceException.message)
                        deviceException.message?.let { put("error_message", it) }
                    }
                }
            }
        }
    }
}