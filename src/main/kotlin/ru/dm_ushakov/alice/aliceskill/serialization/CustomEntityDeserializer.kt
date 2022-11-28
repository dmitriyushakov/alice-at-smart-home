package ru.dm_ushakov.alice.aliceskill.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.dm_ushakov.alice.aliceskill.config.CustomEntity
import ru.dm_ushakov.alice.aliceskill.reflection.ComponentsRegistry

class CustomEntityDeserializer: StdDeserializer<CustomEntity>(CustomEntity::class.java) {
    override fun deserialize(parser: JsonParser, context: DeserializationContext?): CustomEntity {
        val codec = parser.codec
        val componentNode = codec.readTree<JsonNode>(parser)
        val componentName = componentNode[COMPONENT_FIELD_NAME].asText() as String
        val entityTypeToCls = ComponentsRegistry.customEntityClasses[componentName]
            ?: error("Component \"$componentName\" have no any registered custom entities.")
        val entityType = componentNode[TYPE_FIELD_NAME].asText() as String
        val entityClass = entityTypeToCls[entityType]
            ?: error("Component \"$componentName\" have no registered \"$entityType\" entity.")

        (componentNode as? ObjectNode)?.let {
            it.remove(TYPE_FIELD_NAME)
            it.remove(COMPONENT_FIELD_NAME)
        }

        return codec.treeToValue(componentNode, entityClass)
    }
}