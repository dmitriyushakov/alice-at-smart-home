package ru.dm_ushakov.alice.aliceskill.config

import com.fasterxml.jackson.databind.JsonNode

abstract class ConfigurationService {
    private var configuration: JsonNode? = null

    protected abstract fun basicConfiguration(): JsonNode
    protected abstract fun loadConfiguration(): JsonNode?
    protected abstract fun deserializeConfiguration(config: JsonNode): SkillConfiguration
    protected abstract fun saveConfiguration(configuration: JsonNode)

    fun getConfiguration(): SkillConfiguration {
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

    fun applyChanges(configChanges: (JsonNode) -> Unit): SkillConfiguration {
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