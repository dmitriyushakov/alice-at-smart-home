package ru.dm_ushakov.alice.aliceskill.config

import com.fasterxml.jackson.databind.JsonNode
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

abstract class ConfigurationService {
    private var configuration: JsonNode? = null
    private val lock: Lock = ReentrantLock()

    protected abstract fun basicConfiguration(): JsonNode
    protected abstract fun loadConfiguration(): JsonNode?
    protected abstract fun deserializeConfiguration(config: JsonNode): SkillConfiguration
    protected abstract fun saveConfiguration(configuration: JsonNode)

    fun getConfiguration(): SkillConfiguration {
        lock.withLock {
            var config = this.configuration
            if (config == null) {
                config = loadConfiguration()
                if (config == null) {
                    config = basicConfiguration()
                    saveConfiguration(config)
                }
            }
            configuration = config

            return deserializeConfiguration(config)
        }
    }

    fun applyChanges(configChanges: (JsonNode) -> Unit): SkillConfiguration {
        lock.withLock {
            var config = this.configuration
            if (config == null) {
                config = loadConfiguration()
                if (config == null) {
                    config = basicConfiguration()
                }
            }
            configChanges(config)
            val deserializedConfig = deserializeConfiguration(config)
            saveConfiguration(config)

            return deserializedConfig
        }
    }
}