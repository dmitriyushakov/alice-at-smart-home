package ru.dm_ushakov.alice.aliceskill.config

import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.dm_ushakov.alice.aliceskill.serialization.yamlObjectMapper
import ru.dm_ushakov.alice.aliceskill.util.json.makeJsonObject
import java.io.File
import java.io.IOException

@Service
class YAMLFileConfigurationService(
    @Autowired val configurationFile: File
): ConfigurationService() {
    override fun basicConfiguration() = makeJsonObject {
        putArray("homes")
    }

    override fun loadConfiguration(): JsonNode? {
        try {
            return yamlObjectMapper.readTree(configurationFile)
        } catch (ioEx: IOException) {
            LOGGER.warn("Unable to load YAML configuration from ${configurationFile.absolutePath}", ioEx)
        }

        return null
    }

    override fun deserializeConfiguration(config: JsonNode): SkillConfiguration =
        yamlObjectMapper.treeToValue(config, SkillConfiguration::class.java)

    override fun saveConfiguration(configuration: JsonNode) {
        yamlObjectMapper.writeValue(configurationFile, configuration)
    }

    companion object {
        val LOGGER = LoggerFactory.getLogger(YAMLFileConfigurationService::class.java)
    }
}