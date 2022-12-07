package ru.dm_ushakov.alice.aliceskill.serialization.state

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ObjectNode
import ru.dm_ushakov.alice.aliceskill.mappings.StateMapping
import ru.dm_ushakov.alice.aliceskill.reflection.ComponentsRegistry
import ru.dm_ushakov.alice.aliceskill.serialization.COMPONENT_FIELD_NAME
import kotlin.reflect.KClass

fun deserializeStateMapping(keyClass: KClass<*>, parser: JsonParser): StateMapping<*, *> {
    val codec = parser.codec
    val componentNode = codec.readTree<JsonNode>(parser)
    val capabilityType = componentNode[COMPONENT_FIELD_NAME].asText() as String
    val mappingImpl = ComponentsRegistry.stateMappingImplementations[keyClass]?.get(capabilityType)
        ?: error("Unable to found mapping for \"$capabilityType\" capability!")

    (componentNode as? ObjectNode)?.let {
        it.remove(COMPONENT_FIELD_NAME)
    }

    return codec.treeToValue(componentNode, mappingImpl)
}