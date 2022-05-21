package ru.dm_ushakov.alice.aliceskill.capabilities.model

data class ColorScene(val scenes: List<Scene>) {
    init {
        if (scenes.isEmpty()) error("Should be at least 1 scene in ColorScene instance.")
    }
}