package ru.dm_ushakov.alice.aliceskill.notifications

interface NotificationSubscription<T> {
    val emitter: NotificationEmitter<T>
    fun deliverNotification(notification: T)
    fun unsubscribe()
}