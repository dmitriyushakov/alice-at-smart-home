package ru.dm_ushakov.alice.aliceskill.config

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import ru.dm_ushakov.alice.aliceskill.serialization.SkillConfigurationDeserializer

@JsonDeserialize(using = SkillConfigurationDeserializer::class)
class SkillConfiguration {
    private val homesMut: MutableMap<String, UserHome> = mutableMapOf()
    val homes: Map<String, UserHome> get() = homesMut.toMap()

    fun add(home: UserHome) {
        homesMut[home.userId] = home
    }

    fun addAll(homes: Iterable<UserHome>) {
        for (home in homes) {
            add(home)
        }
    }
}