package ru.dm_ushakov.alice.aliceskill.capabilities.model

enum class ColorModel(val colorModelName: String) {
    HSV("hsv"),
    RGB("rgb");

    override fun toString() = colorModelName

    companion object {
        private val nameToColorModel: Map<String, ColorModel> = values().associateBy { it.colorModelName }
        fun getColorModel(modelName: String) = nameToColorModel[modelName] ?: error("Wrong color model name - $modelName")
    }
}