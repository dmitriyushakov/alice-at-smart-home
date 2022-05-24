package ru.dm_ushakov.alice.aliceskill.notifications

import java.util.concurrent.atomic.AtomicLong

private val idCounter = AtomicLong()

class BasicNotificationSubscription<T>(override val emitter: BasicNotificationEmitter<T>, private val receiver: NotificationReceiver<T>): NotificationSubscription<T> {
    val id = idCounter.incrementAndGet()

    override fun deliverNotification(notification: T) {
        receiver.receiveNotification(notification)
    }

    override fun unsubscribe() {
        emitter.unsubscribe(id)
    }

    override fun hashCode() = id.toInt()
    override fun equals(other: Any?) = other is BasicNotificationSubscription<*> && id == other.id
}