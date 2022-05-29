package ru.dm_ushakov.alice.aliceskill.properties

import ru.dm_ushakov.alice.aliceskill.notifications.BasicNotificationEmitter
import ru.dm_ushakov.alice.aliceskill.notifications.NotificationEmitter

abstract class BaseProperty: DeviceProperty {
    override val updateNotificationEmitter: NotificationEmitter<DeviceProperty> = BasicNotificationEmitter()
}