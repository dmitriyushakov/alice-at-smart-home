package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.mappings.EventPropertyStateMapping
import ru.dm_ushakov.alice.aliceskill.properties.EventProperty
import ru.dm_ushakov.alice.aliceskill.properties.model.EventFunction
import ru.dm_ushakov.alice.aliceskill.properties.model.EventType
import ru.dm_ushakov.alice.aliceskill.properties.state.EventPropertyState

@ComponentName("mqtt")
class MQTTEventProperty(
    val brokerId: String,
    val receiveStateTopic: String,
    val stateMapping: EventPropertyStateMapping,
    override val instance: EventFunction,
    override val events: List<EventType>
) : EventProperty() {
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: EventPropertyState = EventPropertyState(instance, instance.defaultEventType)
        set(value) {
            sendNotification()
            field = value
        }

    override fun onCreate() {
        broker.subscribe(receiveStateTopic) { _, msg ->
            state = stateMapping.convertToState(msg.text, this)
        }
    }

    override fun onDestroy() {
        broker.unsubscribe(receiveStateTopic)
    }
}