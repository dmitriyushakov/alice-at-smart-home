package ru.dm_ushakov.alice.aliceskill.components.mqtt

import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.capabilities.ColorSettingCapability
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorModel
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorModel.RGB
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorModel.HSV
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorScene
import ru.dm_ushakov.alice.aliceskill.capabilities.model.TemperatureRange
import ru.dm_ushakov.alice.aliceskill.capabilities.state.*
import ru.dm_ushakov.alice.aliceskill.mappings.ColorStateMapping

@ComponentName("mqtt")
class MQTTColorSettingCapability(
    val brokerId: String,
    val receiveStateTopic: String,
    val sendStateTopic: String,
    val stateMapping: ColorStateMapping,
    override val colorModel: ColorModel? = null,
    override val temperatureRange: TemperatureRange? = null,
    override val colorScene: ColorScene? = null
) : ColorSettingCapability() {
    override val retrievable: Boolean get() = true
    override val reportable: Boolean get() = true

    private val broker: MQTTBroker by lazy { getMqttBroker(brokerId) }

    override var state: BaseColorSettingCapabilityState =
        colorModel?.let {
            when(it) {
                RGB -> RGBColorSettingCapabilityState(0, 0, 0)
                HSV -> HSVColorSettingCapabilityState(0, 0, 0)
            }
        } ?:
        temperatureRange?.let { temperatureRange ->
            val average = temperatureRange.max?.let { max -> temperatureRange.min?.let { min -> (max + min) / 2 } }
            val defaultTemperature = average ?: temperatureRange.min ?: temperatureRange.max ?: 2000
            KelvinColorSettingCapabilityState(defaultTemperature)
        } ?:
        colorScene?.let { it.scenes.firstOrNull()?.let(::SceneColorSettingCapabilityState) } ?: error("Unable to get permitable state for color capability!")
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