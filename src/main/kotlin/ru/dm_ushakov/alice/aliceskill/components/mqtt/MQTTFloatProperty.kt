package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.mappings.FloatPropertyStateMapping
import ru.dm_ushakov.alice.aliceskill.properties.FloatProperty
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatFunction
import ru.dm_ushakov.alice.aliceskill.properties.model.FloatUnit
import ru.dm_ushakov.alice.aliceskill.properties.state.FloatPropertyState

@ComponentName("mqtt")
class MQTTFloatProperty(
    val brokerId: String,
    val receiveStateTopic: String,
    val stateMapping: FloatPropertyStateMapping,
    override val instance: FloatFunction,
    override val unit: FloatUnit?
) : FloatProperty() {
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: FloatPropertyState = FloatPropertyState(instance, 0.0)
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