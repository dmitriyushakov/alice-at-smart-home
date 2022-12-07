package ru.dm_ushakov.alice.aliceskill.serialization

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import ru.dm_ushakov.alice.aliceskill.capabilities.DeviceCapability
import ru.dm_ushakov.alice.aliceskill.config.CustomEntity
import ru.dm_ushakov.alice.aliceskill.devices.BasicDevice
import ru.dm_ushakov.alice.aliceskill.devices.Device
import ru.dm_ushakov.alice.aliceskill.mappings.*
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty
import ru.dm_ushakov.alice.aliceskill.serialization.state.*

private fun createSkillModule(): Module = SimpleModule().apply {
    addAbstractTypeMapping(Device::class.java, BasicDevice::class.java)
    addDeserializer(DeviceCapability::class.java, DeviceCapabilityDeserializer())
    addDeserializer(DeviceProperty::class.java, DevicePropertyDeserializer())
    addDeserializer(CustomEntity::class.java, CustomEntityDeserializer())
    addDeserializer(OnOffStateMapping::class.java, OnOffStateMappingDeserializer())
    addDeserializer(ToggleStateMapping::class.java, ToggleStateMappingDeserializer())
    addDeserializer(RangeStateMapping::class.java, RangeStateMappingDeserializer())
    addDeserializer(ModeStateMapping::class.java, ModeStateMappingDeserializer())
    addDeserializer(ColorStateMapping::class.java, ColorStateMappingDeserializer())
    addDeserializer(EventPropertyStateMapping::class.java, EventPropertyStateMappingDeserializer())
    addDeserializer(FloatPropertyStateMapping::class.java, FloatPropertyStateMappingDeserializer())
}

private fun createYamlObjectMapper(): ObjectMapper {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.findAndRegisterModules()
    mapper.registerModule(skillModule)

    return mapper
}

val skillModule = createSkillModule()
val yamlObjectMapper = createYamlObjectMapper()