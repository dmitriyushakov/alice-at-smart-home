package ru.dm_ushakov.alice.aliceskill.capabilities.model

enum class Scene(val sceneName: String) {
    Alarm("alarm"),
    Alice("alice"),
    Candle("candle"),
    Dinner("dinner"),
    Fantasy("fantasy"),
    Garland("garland"),
    Jungle("jungle"),
    Movie("movie"),
    Neon("neon"),
    Night("night"),
    Ocean("ocean"),
    Party("party"),
    Reading("reading"),
    Rest("rest"),
    Romance("romance"),
    Siren("siren"),
    Sunrise("sunrise"),
    Sunset("sunset");

    override fun toString() = sceneName

    companion object {
        private val nameToScene: Map<String, Scene> = values().associateBy { it.sceneName }
        fun getScene(sceneName: String): Scene = nameToScene[sceneName] ?: error("Wrong name for scene - $sceneName")
    }
}