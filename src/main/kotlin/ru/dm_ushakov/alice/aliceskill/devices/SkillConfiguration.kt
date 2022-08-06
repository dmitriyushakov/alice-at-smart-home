package ru.dm_ushakov.alice.aliceskill.devices

class SkillConfiguration {
    private val homesMut: MutableMap<String, UserHome> = mutableMapOf()
    val homes: Map<String, UserHome> = homesMut.toMap()

    fun add(home: UserHome) {
        homesMut[home.userId] = home
    }

    fun addAll(homes: Iterable<UserHome>) {
        for (home in homes) {
            add(home)
        }
    }
}