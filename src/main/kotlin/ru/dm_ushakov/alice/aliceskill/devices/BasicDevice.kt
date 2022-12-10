package ru.dm_ushakov.alice.aliceskill.devices

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.dm_ushakov.alice.aliceskill.capabilities.DeviceCapability
import ru.dm_ushakov.alice.aliceskill.config.UserHome
import ru.dm_ushakov.alice.aliceskill.notifications.BasicNotificationEmitter
import ru.dm_ushakov.alice.aliceskill.notifications.FunctionNotificationReceiver
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty
import ru.dm_ushakov.alice.aliceskill.util.onceInit

private val logger: Logger = LoggerFactory.getLogger(BasicDevice::class.java)

class BasicDevice(
    override val id: String,
    override var name: String,
    override var description: String?,
    override var room: String?,
    override var type: DeviceType,
    capabilities: List<DeviceCapability>,
    properties: List<DeviceProperty>
): BaseDevice() {
    private var afterCreate = false
    private val capabilityUpdateNotificationReceiver = FunctionNotificationReceiver<DeviceCapability> {
        updateNotificationEmitter.sendNotification(this@BasicDevice)
    }
    private val propertyUpdateNotificationReceiver = FunctionNotificationReceiver<DeviceProperty> {
        updateNotificationEmitter.sendNotification(this@BasicDevice)
    }

    override val updateNotificationEmitter: BasicNotificationEmitter<Device> = BasicNotificationEmitter()

    override var home: UserHome by onceInit()

    override var capabilities: List<DeviceCapability> = capabilities
        set(value) {
            synchronized(this) {
                val oldCapabilitiesSet = value.toSet()
                val newCapabilitySet = field.toSet()

                for (capability in value) {
                    if (capability !in oldCapabilitiesSet) {
                        capability.updateNotificationEmitter.subscribe(capabilityUpdateNotificationReceiver)
                        if (afterCreate) capability.onCreate()
                    }
                }

                for (capability in field) {
                    if (capability !in newCapabilitySet) {
                        capability.onDestroy()
                    }
                }

                field = value
            }
        }

    override var properties: List<DeviceProperty> = properties
        set(value) {
            synchronized(this) {
                val oldPropertiesSet = value.toSet()
                val newPropertiesSet = field.toSet()

                for (property in value) {
                    if (property !in oldPropertiesSet) {
                        property.updateNotificationEmitter.subscribe(propertyUpdateNotificationReceiver)
                        if (afterCreate) property.onCreate()
                    }
                }

                for (property in field) {
                    if (property !in newPropertiesSet) {
                        property.onDestroy()
                    }
                }

                field = value
            }
        }

    override fun onCreate() {
        synchronized(this) {
            afterCreate = true

            for (capability in capabilities) {
                try {
                    capability.updateNotificationEmitter.subscribe(capabilityUpdateNotificationReceiver)
                    capability.onCreate()
                } catch (ex: Exception) {
                    logger.error("Failed to initialize \"${capability.type}\" capability in \"$name\" ($id) device from \"${home.userId}\" home.", ex)
                }
            }

            for (property in properties) {
                try {
                    property.updateNotificationEmitter.subscribe(propertyUpdateNotificationReceiver)
                    property.onCreate()
                } catch (ex: Exception) {
                    logger.error("Failed to initialize \"${property.type}\" property in \"$name\" ($id) device from \"${home.userId}\" home.", ex)
                }
            }
        }
    }

    override fun onDestroy() {
        synchronized(this) {
            if (afterCreate) {
                for (capability in capabilities) {
                    try {
                        capability.onDestroy()
                    } catch (ex: Exception) {
                        logger.error("Failed to destroy \"${capability.type}\" capability in \"$name\" ($id) device from \"${home.userId}\" home.", ex)
                    }
                }

                for (property in properties) {
                    try {
                        property.onDestroy()
                    } catch (ex: Exception) {
                        logger.error("Failed to destroy \"${property.type}\" property in \"$name\" ($id) device from \"${home.userId}\" home.", ex)
                    }
                }
            }
        }
    }
}