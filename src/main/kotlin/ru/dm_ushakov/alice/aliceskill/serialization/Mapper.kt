package ru.dm_ushakov.alice.aliceskill.serialization

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import ru.dm_ushakov.alice.aliceskill.capabilities.DeviceCapability
import ru.dm_ushakov.alice.aliceskill.config.CustomEntity
import ru.dm_ushakov.alice.aliceskill.devices.BasicDevice
import ru.dm_ushakov.alice.aliceskill.devices.Device
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty

private fun createSkillModule(): Module {
    val skillModule = SimpleModule()
    skillModule.addAbstractTypeMapping(Device::class.java, BasicDevice::class.java)
    skillModule.addDeserializer(DeviceCapability::class.java, DeviceCapabilityDeserializer())
    skillModule.addDeserializer(DeviceProperty::class.java, DevicePropertyDeserializer())
    skillModule.addDeserializer(CustomEntity::class.java, CustomEntityDeserializer())

    return skillModule
}

private fun createYamlObjectMapper(): ObjectMapper {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.findAndRegisterModules()
    mapper.registerModule(skillModule)

    return mapper
}

val skillModule = createSkillModule()
val yamlObjectMapper = createYamlObjectMapper()