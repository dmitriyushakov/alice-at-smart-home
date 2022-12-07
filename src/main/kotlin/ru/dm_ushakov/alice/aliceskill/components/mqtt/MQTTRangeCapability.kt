package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.RangeCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Range
import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeFunction
import ru.dm_ushakov.alice.aliceskill.capabilities.model.RangeUnit
import ru.dm_ushakov.alice.aliceskill.capabilities.state.RangeCapabilityState
import ru.dm_ushakov.alice.aliceskill.mappings.RangeStateMapping
import ru.dm_ushakov.alice.aliceskill.util.state.relativeFrom

@ComponentName("mqtt")
class MQTTRangeCapability(
    val brokerId: String,
    val receiveStateTopic: String,
    val sendStateTopic: String,
    val stateMapping: RangeStateMapping,
    override val instance: RangeFunction,
    override val unit: RangeUnit?,
    override val randomAccess: Boolean?,
    override val range: Range?
) : RangeCapability() {
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: RangeCapabilityState = RangeCapabilityState(instance, range?.min ?: 0.0)
        set(receivedValue) {
            val value = receivedValue.relativeFrom(field)
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