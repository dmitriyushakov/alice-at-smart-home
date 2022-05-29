package ru.dm_ushakov.alice.aliceskill.devices

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType
import ru.dm_ushakov.alice.aliceskill.error.DeviceException
import ru.dm_ushakov.alice.aliceskill.error.deviceError
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class BaseDevice: Device {
    override fun getDescriptionJson() = makeJsonObject {
        var haveCapOrProp = false
        put("id", id)
        put("name", name)
        description?.let { put("description", it) }
        room?.let { put("room", it) }
        put("type", type.deviceTypeName)

        val capabilities = this@BaseDevice.capabilities
        val properties = this@BaseDevice.capabilities

        if (capabilities.isNotEmpty()) {
            haveCapOrProp = true
            putArray("capabilities", capabilities) { cap ->
                add(cap.getDescriptionJson())
            }
        }

        if (properties.isNotEmpty()) {
            haveCapOrProp = true
            putArray("properties", properties) { prop ->
                add(prop.getDescriptionJson())
            }
        }

        if (!haveCapOrProp) deviceError(DeviceErrorType.InternalError, "Device \"$id\" don't have any capabilities or properties!")
    }

    override fun getStateJson(): JsonNode {
        var error: DeviceException? = null
        val capabilitiesStates = mutableListOf<JsonNode>()
        val propertiesStates = mutableListOf<JsonNode>()
        var haveCapOrProp = false

        try {
            val capabilities = this.capabilities
            val properties = this.properties

            if (capabilities.isNotEmpty()) {
                haveCapOrProp = true
                capabilitiesStates.addAll(capabilities.map { it.getStateJson() })
            }

            if (properties.isNotEmpty()) {
                haveCapOrProp = true
                propertiesStates.addAll(properties.map { it.getStateJson() })
            }

            if (!haveCapOrProp) deviceError(DeviceErrorType.InternalError, "Device don't have any capability or property!")
        } catch (ex: DeviceException) {
            error = ex
        } catch (ex: Exception) {
            val errorMessage = "Internal device error:\n" + ex.stackTraceToString()
            error = DeviceException(DeviceErrorType.InternalError, errorMessage)
        }

        return makeJsonObject {
            put("id", id)

            if (capabilitiesStates.isNotEmpty()) {
                putArray("capabilities", capabilitiesStates) { state ->
                    add(state)
                }
            }

            if (propertiesStates.isNotEmpty()) {
                putArray("properties", propertiesStates) { state ->
                    add(state)
                }
            }

            error?.let { error ->
                put("error_code", error.errorType.errorCode)
                error.message?.let { put("error_message", it) }
            }
        }
    }

    override fun changeCapabilityState(changeState: JsonNode): JsonNode {
        val capabilitiesArray = changeState["capabilities"] as? ArrayNode
        val outputCapabilityResponses: MutableList<JsonNode> = mutableListOf()
        val keysToCapabilities = capabilities.flatMap { cap -> cap.keys.map { key-> key to cap } }.toMap()
        var error: DeviceException? = null

        try {
            if (capabilitiesArray == null) {
                deviceError(DeviceErrorType.InvalidAction, "Should be capabilities array in state change request!")
            } else {
                if (capabilitiesArray.isEmpty) deviceError(DeviceErrorType.InvalidAction, "Should be at least 1 state in device state change request!")

                for (capabilityStateRequest in capabilitiesArray) {
                    val type = capabilityStateRequest["type"].asText()
                    val instance = capabilityStateRequest["state"]["instance"].asText()
                    val key = DeviceContentKey(type, instance)

                    val capability = keysToCapabilities[key]

                    if (capability == null) {
                        deviceError(DeviceErrorType.InvalidAction, "Unknown capability key for $id device - $key!")
                    } else {
                        val changeResponse = capability.changeCapabilityState(capabilityStateRequest)
                        outputCapabilityResponses.add(changeResponse)
                    }
                }
            }
        } catch (ex: DeviceException) {
            error = ex
        } catch (ex: Exception) {
            val errorMessage = "Internal error:\n" + ex.stackTraceToString()
            error = DeviceException(DeviceErrorType.InternalError, errorMessage, ex)
        }

        return makeJsonObject {
            put("id", id)

            if (error != null) {
                putObject("action_result") {
                    put("status", "ERROR")
                    put("error_code", error.errorType.errorCode)
                    error.message?.let { put("error_message", it) }
                }
            } else {
                putArray("capabilities", outputCapabilityResponses) { resp ->
                    add(resp)
                }
            }
        }
    }
}