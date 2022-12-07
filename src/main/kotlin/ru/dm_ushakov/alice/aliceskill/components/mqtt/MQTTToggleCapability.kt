package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.ToggleCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ToggleFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ToggleCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.ToggleStateMapping

@ComponentName("mqtt")
class MQTTToggleCapability(
    val brokerId: String,
    val receiveStateTopic: String,
    val sendStateTopic: String,
    val stateMapping: ToggleStateMapping,
    override val instance: ToggleFunction
) : ToggleCapability() {
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: ToggleCapabilityState = ToggleCapabilityState(instance, false)
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