package ru.dm_ushakov.alice.aliceskill.capabilities.state

import ru.dm_ushakov.alice.aliceskill.capabilities.model.Scene

data class SceneColorSettingCapabilityState(val value: Scene): BaseColorSettingCapabilityState() {
    override val instance: String get() = "scene"
}