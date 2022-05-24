package ru.dm_ushakov.alice.aliceskill.notifications

class FunctionNotificationReceiver<T>(private val receiverFunction: (T) -> Unit): NotificationReceiver<T> {
    override fun receiveNotification(incomingValue: T) = receiverFunction(incomingValue)
}