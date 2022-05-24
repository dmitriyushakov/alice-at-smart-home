package ru.dm_ushakov.alice.aliceskill.notifications

fun <T> NotificationReceiver<T>.reassignEmitters(
    oldSubscriptions: List<NotificationSubscription<T>>,
    newEmitters: List<NotificationEmitter<T>>): List<NotificationSubscription<T>> {
    val oldEmittersSet = oldSubscriptions.map { it.emitter }.toSet()
    val newEmittersSet = newEmitters.toSet()
    val oldEmittersToSubscription = oldSubscriptions.map { it.emitter to it }.toMap()
    val newSubscriptions: MutableList<NotificationSubscription<T>> = mutableListOf()

    for (emitter in oldEmittersSet) {
        if (emitter !in newEmittersSet) {
            oldEmittersToSubscription[emitter]?.unsubscribe()
        } else {
            oldEmittersToSubscription[emitter]?.let { newSubscriptions.add(it) }
        }
    }

    for (emitter in newEmittersSet) {
        if (emitter !in oldEmittersSet) {
            val subscription = emitter.subscribe(this)
            newSubscriptions.add(subscription)
        }
    }

    return newSubscriptions
}