package ru.dm_ushakov.alice.aliceskill.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import ru.dm_ushakov.alice.aliceskill.config.SkillConfiguration
import ru.dm_ushakov.alice.aliceskill.config.UserHome

class SkillConfigurationDeserializer: StdDeserializer<SkillConfiguration>(SkillConfiguration::class.java){
    private fun UserHome.fillParentNodeProperties() {
        val home = this
        for (device in devices) {
            device.home = home

            for (cap in device.capabilities) {
                cap.home = home
                cap.device = device
            }

            for (prop in device.properties) {
                prop.home = home
                prop.device = device
            }
        }
    }

    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): SkillConfiguration {
        val codec = parser.codec
        val rootNode = codec.readTree<JsonNode>(parser)

        val homesArr = rootNode["homes"]
        if (!homesArr.isArray) error("\"homes\" array expected in config!")
        else {
            val config = SkillConfiguration()
            for (homeNode in homesArr) {
                val home = codec.treeToValue(homeNode, UserHome::class.java) as UserHome
                home.fillParentNodeProperties()
                config.add(home)
            }

            return config
        }
    }
}