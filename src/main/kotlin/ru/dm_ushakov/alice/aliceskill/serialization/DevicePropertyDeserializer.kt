package ru.dm_ushakov.alice.aliceskill.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.dm_ushakov.alice.aliceskill.properties.DeviceProperty
import ru.dm_ushakov.alice.aliceskill.reflection.ComponentsRegistry

class DevicePropertyDeserializer: StdDeserializer<DeviceProperty>(DeviceProperty::class.java) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): DeviceProperty {
        val codec = parser.codec
        val componentNode = codec.readTree<JsonNode>(parser)
        val capabilityType = componentNode[TYPE_FIELD_NAME].asText() as String
        val typeComponentClasses = ComponentsRegistry.propClasses[capabilityType] ?: error("$capabilityType property type is not allowed!")
        val componentName = componentNode[COMPONENT_FIELD_NAME].asText() as String
        val componentClass = typeComponentClasses[componentName] ?: error("$componentName property for $capabilityType capability type not found!")

        (componentNode as? ObjectNode)?.let {
            it.remove(TYPE_FIELD_NAME)
            it.remove(COMPONENT_FIELD_NAME)
        }

        return codec.treeToValue(componentNode, componentClass)
    }
}