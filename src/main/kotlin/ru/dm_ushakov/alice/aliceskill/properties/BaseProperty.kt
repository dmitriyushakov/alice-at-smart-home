package ru.dm_ushakov.alice.aliceskill.properties

import ru.dm_ushakov.alice.aliceskill.config.CustomEntity
import ru.dm_ushakov.alice.aliceskill.config.UserHome
import ru.dm_ushakov.alice.aliceskill.devices.Device
import ru.dm_ushakov.alice.aliceskill.notifications.BasicNotificationEmitter
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter
import ru.dm_ushakov.alice.aliceskill.util.onceInit
import kotlin.reflect.KClass

abstract class BaseProperty: DeviceProperty {
    override var home: UserHome by onceInit()
    override var device: Device by onceInit()

    fun <T: CustomEntity> getCustomEntity(klass: KClass<T>, entityId: String) =
        home.getCustomEntity(klass, entityId)

    override val updateNotificationEmitter: NotificationEmitter<DeviceProperty> = BasicNotificationEmitter()
    override fun hashCode() = keys.sumOf { it.hashCode() }
    override fun equals(other: Any?) = this === other
}