package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.OnOffCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.state.OnOffCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.OnOffStateMapping

@ComponentName("mqtt")
class MQTTOnOffCapability(
    val brokerId: String,
    val receiveStateTopic: String,
    val sendStateTopic: String,
    val stateMapping: OnOffStateMapping
): OnOffCapability() {
    override val split: Boolean get() = true
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: OnOffCapabilityState = OnOffCapabilityState(false)
        set(value) {
            val payload = stateMapping.convertFromState(value, this)
            broker.publish(sendStateTopic, payload)
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