package ru.dm_ushakov.alice.aliceskill.config

interface ConfigurationLifecycleService {
    val configuration: SkillConfiguration
    fun update()
}