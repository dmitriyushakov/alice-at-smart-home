package ru.dm_ushakov.alice.aliceskill.serialization.state

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ru.dm_ushakov.alice.aliceskill.mappings.RangeStateMapping

class RangeStateMappingDeserializer: StdDeserializer<RangeStateMapping>(RangeStateMapping::class.java) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): RangeStateMapping
            = deserializeStateMapping(RangeStateMapping::class, parser) as RangeStateMapping
}