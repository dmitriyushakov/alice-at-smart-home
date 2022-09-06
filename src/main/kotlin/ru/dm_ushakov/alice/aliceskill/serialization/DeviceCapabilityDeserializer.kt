package ru.dm_ushakov.alice.aliceskill.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.dm_ushakov.alice.aliceskill.capabilities.DeviceCapability
import ru.dm_ushakov.alice.aliceskill.reflection.ComponentsRegistry

class DeviceCapabilityDeserializer: StdDeserializer<DeviceCapability>(DeviceCapability::class.java) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): DeviceCapability {
        val codec = parser.codec
        val componentNode = codec.readTree<JsonNode>(parser)
        val capabilityType = componentNode[TYPE_FIELD_NAME].asText() as String
        val typeComponentClasses = ComponentsRegistry.capClasses[capabilityType] ?: error("$capabilityType capability type is not allowed!")
        val componentName = componentNode[COMPONENT_FIELD_NAME].asText() as String
        val componentClass = typeComponentClasses[componentName] ?: error("$componentName component for $capabilityType capability type not found!")

        (componentNode as? ObjectNode)?.let {
            it.remove(TYPE_FIELD_NAME)
            it.remove(COMPONENT_FIELD_NAME)
        }

        return codec.treeToValue(componentNode, componentClass)
    }
}