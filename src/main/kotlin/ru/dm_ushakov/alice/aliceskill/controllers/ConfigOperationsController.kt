package ru.dm_ushakov.alice.aliceskill.controllers

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import ru.dm_ushakov.alice.aliceskill.capabilities.DeviceCapability
import ru.dm_ushakov.alice.aliceskill.config.*
import ru.dm_ushakov.alice.aliceskill.config.operations.*
import ru.dm_ushakov.alice.aliceskill.controllers.requests.CreateOrUpdateDeviceRequest
import ru.dm_ushakov.alice.aliceskill.devices.Device
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty
import ru.dm_ushakov.alice.aliceskill.util.notFound

@RestController
@RequestMapping(path = ["/internal_api/config/"])
class ConfigOperationsController(
    @Autowired
    val configurationService: ConfigurationService,
    @Autowired
    val configurationLifecycleService: ConfigurationLifecycleService
) {
    private val configuration: SkillConfiguration get() = configurationService.getConfiguration()
    private fun apply(operation: (JsonNode) -> Unit) {
        configurationService.applyChanges(operation)
        configurationLifecycleService.update()
    }

    @RequestMapping(method = [GET], path = ["/"])
    fun getConfig(): SkillConfiguration {
        return configuration
    }

    @RequestMapping(method = [GET], path = ["/{userId}"])
    fun getHome(@PathVariable("userId") userId: String): UserHome {
        val home = configuration.homes[userId]
        if (home == null) {
            notFound("Home with id \"$userId\" not found!")
        } else {
            return home
        }
    }

    @RequestMapping(method = [PUT], path = ["/{userId}"])
    @ResponseStatus(code = CREATED)
    fun createHome(@PathVariable("userId") userId: String) {
        apply(CreateHome(userId))
    }

    @RequestMapping(method = [DELETE], path = ["/{userId}"])
    fun deleteHome(@PathVariable("userId") userId: String) {
        apply(DeleteHome(userId))
    }

    @RequestMapping(method = [POST, PUT], path = ["/{userId}/customEntities"])
    fun createOrUpdateCustomEntities(@PathVariable("userId") userId: String, @RequestBody entityData: JsonNode) {
        apply(CreateOrUpdateCustomEntity(userId, entityData))
    }

    @RequestMapping(method = [GET], path = ["/{userId}/customEntities"])
    fun getCustomEntities(@PathVariable("userId") userId: String): List<CustomEntity> {
        val home = configuration.homes[userId]
        if (home == null) {
            notFound("Home with id \"$userId\" not found!")
        } else {
            return home.customEntities
        }
    }
    @RequestMapping(method = [GET], path = ["/{userId}/customEntities/{entityId}"])
    fun getCustomEntity(@PathVariable("userId") userId: String, @PathVariable("entityId") entityId: String): CustomEntity {
        val home = configuration.homes[userId]
        if (home == null) {
            notFound("Home with id \"$userId\" not found!")
        } else {
            val entity = home.customEntities.firstOrNull { it.entityId == entityId }
            if (entity == null) {
                notFound("Entity with id \"$entityId\" not found!")
            } else {
                return entity
            }
        }
    }
    @RequestMapping(method = [DELETE], path = ["/{userId}/customEntities/{entityId}"])
    fun deleteCustomEntity(@PathVariable("userId") userId: String, @PathVariable("entityId") entityId: String) {
        apply(DeleteCustomEntity(userId, entityId))
    }

    @RequestMapping(method = [GET], path = ["/{userId}/devices/{deviceId}"])
    fun getDevice(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String): Device {
        val home = configuration.homes[userId]
        if (home == null) {
            notFound("Home with id \"$userId\" not found!")
        } else {
            val device = home.devices.firstOrNull { it.id == deviceId }
            if (device == null) {
                notFound("Device with id \"$deviceId\" not found!")
            } else {
                return device
            }
        }
    }

    @RequestMapping(method = [POST, PUT], path = ["/{userId}/devices/{deviceId}"])
    fun createOrUpdateDevice(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @RequestBody request: CreateOrUpdateDeviceRequest) {
        apply(CreateOrUpdateDevice(userId, deviceId, request.name, request.type))
    }

    @RequestMapping(method = [DELETE], path = ["/{userId}/devices/{deviceId}"])
    fun deleteDevice(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String) {
        apply(DeleteDevice(userId, deviceId))
    }

    @RequestMapping(method = [GET], path = ["/{userId}/devices/{deviceId}/cap/{capType}"])
    fun getCapability(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @PathVariable("capType") capType: String): DeviceCapability {
        val home = configuration.homes[userId]
        if (home == null) {
            notFound("Home with id \"$userId\" not found!")
        } else {
            val device = home.devices.firstOrNull { it.id == deviceId }
            if (device == null) {
                notFound("Device with id \"$deviceId\" not found!")
            } else {
                val cap = device.capabilities.firstOrNull { it.type == capType }
                if (cap == null) {
                    notFound("Capability with type \"$capType\" not found!")
                } else {
                    return cap
                }
            }
        }
    }

    @RequestMapping(method = [POST, PUT], path = ["/{userId}/devices/{deviceId}/cap"])
    fun createOrUpdateCapability(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @RequestBody capabilityData: JsonNode) {
        apply(CreateOrUpdateCapability(userId, deviceId, capabilityData))
    }

    @RequestMapping(method = [DELETE], path = ["/{userId}/devices/{deviceId}/cap/{capType}"])
    fun deleteCapability(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @PathVariable("capType") capType: String) {
        apply(DeleteCapability(userId, deviceId, capType))
    }

    @RequestMapping(method = [GET], path = ["/{userId}/devices/{deviceId}/prop/{propType}"])
    fun getProperty(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @PathVariable("propType") propType: String): DeviceProperty {
        val home = configuration.homes[userId]
        if (home == null) {
            notFound("Home with id \"$userId\" not found!")
        } else {
            val device = home.devices.firstOrNull { it.id == deviceId }
            if (device == null) {
                notFound("Device with id \"$deviceId\" not found!")
            } else {
                val prop = device.properties.firstOrNull { it.type == propType }
                if (prop == null) {
                    notFound("Property with type \"$propType\" not found!")
                } else {
                    return prop
                }
            }
        }
    }

    @RequestMapping(method = [POST, PUT], path = ["/{userId}/devices/{deviceId}/prop"])
    fun createOrUpdateProperty(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @RequestBody propertyData: JsonNode) {
        apply(CreateOrUpdateProperty(userId, deviceId, propertyData))
    }

    @RequestMapping(method = [DELETE], path = ["/{userId}/devices/{deviceId}/prop/{propType}"])
    fun deleteProperty(@PathVariable("userId") userId: String, @PathVariable("deviceId") deviceId: String, @PathVariable("propType") propType: String) {
        apply(DeleteProperty(userId, deviceId, propType))
    }
}