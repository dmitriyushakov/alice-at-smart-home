package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.ModeCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Mode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ModeFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.state.ModeCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.ModeStateMapping

@ComponentName("mqtt")
class MQTTModeCapability(
    val brokerId: String,
    val receiveStateTopic: String,
    val sendStateTopic: String,
    val stateMapping: ModeStateMapping,
    override val instance: ModeFunction,
    override val modes: List<Mode>
): ModeCapability() {
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: ModeCapabilityState = ModeCapabilityState(instance, instance.recommendedModes.first())
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