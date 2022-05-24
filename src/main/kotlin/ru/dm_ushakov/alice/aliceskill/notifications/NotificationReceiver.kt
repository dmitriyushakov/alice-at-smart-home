package ru.dm_ushakov.alice.aliceskill.notifications

interface NotificationReceiver<T> {
    fun receiveNotification(incomingValue: T)
}