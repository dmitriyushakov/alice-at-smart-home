package ru.dm_ushakov.alice.aliceskill.notifications

interface NotificationEmitter<T> {
    fun subscribe(receiver: NotificationReceiver<T>): NotificationSubscription<T>
}