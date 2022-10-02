package ru.dm_ushakov.alice.aliceskill.config.operations

import com.fasterxml.jackson.databind.JsonNode

abstract class ConfigOperation: (JsonNode) -> Unit {
    override fun invoke(config: JsonNode) {
        applyChanges(config)
    }

    abstract fun applyChanges(config: JsonNode)
}