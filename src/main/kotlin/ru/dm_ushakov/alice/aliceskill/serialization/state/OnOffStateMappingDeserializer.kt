package ru.dm_ushakov.alice.aliceskill.serialization.state

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ru.dm_ushakov.alice.aliceskill.mappings.OnOffStateMapping

class OnOffStateMappingDeserializer: StdDeserializer<OnOffStateMapping>(OnOffStateMapping::class.java) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): OnOffStateMapping
        = deserializeStateMapping(OnOffStateMapping::class, parser) as OnOffStateMapping
}