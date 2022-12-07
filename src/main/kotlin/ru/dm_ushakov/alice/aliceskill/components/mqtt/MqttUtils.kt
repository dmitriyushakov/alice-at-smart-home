package ru.dm_ushakov.alice.aliceskill.components.mqtt

import org.eclipse.paho.client.mqttv3.MqttMessage
import ru.dm_ushakov.alice.aliceskill.capabilities.BaseCapability
import ru.dm_ushakov.alice.aliceskill.properties.BaseProperty

val MqttMessage.text: String get() = payload.decodeToString()

fun BaseCapability.getMqttBroker(entityId: String): MQTTBroker
        = getCustomEntity(MQTTBroker::class, entityId) ?: error("Unable to get broker with entityId - \"$entityId\"!")

fun BaseProperty.getMqttBroker(entityId: String): MQTTBroker
        = getCustomEntity(MQTTBroker::class, entityId) ?: error("Unable to get broker with entityId - \"$entityId\"!")