package ru.dm_ushakov.alice.aliceskill.notifications

import java.util.concurrent.atomic.AtomicLong

private val idCounter = AtomicLong()

class BasicNotificationEmitter<T>: NotificationEmitter<T>, NotificationReceiver<T> {
    val id = idCounter.incrementAndGet()

    private val subscriptionsMap: MutableMap<Long, NotificationSubscription<T>> = mutableMapOf()
    private var subscriptions: List<NotificationSubscription<T>> = emptyList()

    private fun updateSubscriptionsList() {
        subscriptions = subscriptionsMap.toList().sortedBy { it.first }.map { it.second }
    }

    override fun subscribe(receiver: NotificationReceiver<T>): NotificationSubscription<T> {
        synchronized(subscriptionsMap) {
            val subscription = BasicNotificationSubscription(this, receiver)
            subscriptionsMap[subscription.id] = subscription
            updateSubscriptionsList()

            return subscription
        }
    }

    fun unsubscribe(id: Long) {
        synchronized(subscriptionsMap) {
            subscriptionsMap.remove(id)
            updateSubscriptionsList()
        }
    }

    fun sendNotification(notification: T) {
        for (subscription in subscriptions) {
            subscription.deliverNotification(notification)
        }
    }

    override fun receiveNotification(incomingValue: T) {
        sendNotification(incomingValue)
    }

    override fun hashCode() = id.toInt()
    override fun equals(other: Any?) = other is BasicNotificationEmitter<*> && id == other.id
}