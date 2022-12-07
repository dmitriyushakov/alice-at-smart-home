package ru.dm_ushakov.alice.aliceskill.components.mqtt

import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import ru.dm_ushakov.alice.aliceskill.annotation.ComponentName
import ru.dm_ushakov.alice.aliceskill.annotation.EntityType
import ru.dm_ushakov.alice.aliceskill.config.BasicCustomEntity

@ComponentName("mqtt")
@EntityType("mqttBroker")
class MQTTBroker(
    entityId: String,
    val serverURI: String,
    val clientId: String,
    val automaticReconnect: Boolean?,
    val cleanSession: Boolean?,
    val connectionTimeout: Int?,
    val keepAliveInterval: Int?,
    val maxInflight: Int?,
    val mqttVersion: String?,
    val username: String?,
    val password: String?,
    val serverURIs: List<String>?
): BasicCustomEntity(entityId) {
    private val client: IMqttClient by lazy {
        MqttClient(serverURI, clientId)
    }

    private val connectOptions: MqttConnectOptions by lazy {
        MqttConnectOptions().let { options ->
            automaticReconnect?.let { options.isAutomaticReconnect = it }
            cleanSession?.let { options.isCleanSession = it }
            connectionTimeout?.let { options.connectionTimeout = it }
            keepAliveInterval?.let { options.keepAliveInterval = it }
            maxInflight?.let { options.maxInflight = it }
            mqttVersion?.let { options.mqttVersion = when(it) {
                "3.1" -> MqttConnectOptions.MQTT_VERSION_3_1
                "3.1.1" -> MqttConnectOptions.MQTT_VERSION_3_1_1
                "default" -> MqttConnectOptions.MQTT_VERSION_DEFAULT
                else -> error("Unknown MQTT version option - \"$it\"!")
            } }
            username?.let { options.userName = it }
            password?.let { options.password = it.toCharArray() }
            serverURIs?.let { options.serverURIs = it.toTypedArray() }

            options
        }
    }

    private fun ensureConnected() {
        if (!client.isConnected) {
            client.connect(connectOptions)
        }
    }

    override fun onCreate() {
        ensureConnected()
    }

    override fun onDestroy() {
        client.disconnect()
        client.close()
    }

    fun publish(topic: String, payload: ByteArray) {
        ensureConnected()
        client.publish(topic, payload.let(::MqttMessage))
    }

    fun publish(topic: String, payload: String) {
        publish(topic, payload.encodeToByteArray())
    }

    fun subscribe(topicFilter: String, listener: (String, MqttMessage) -> Unit) {
        ensureConnected()
        client.subscribe(topicFilter, IMqttMessageListener(listener))
    }

    fun unsubscribe(topicFilter: String) {
        client.unsubscribe(topicFilter)
    }
}