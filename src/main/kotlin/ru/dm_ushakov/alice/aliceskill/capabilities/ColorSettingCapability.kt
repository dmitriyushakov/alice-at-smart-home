package ru.dm_ushakov.alice.aliceskill.capabilities

import com.fasterxml.jackson.databind.JsonNode
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorModel
import ru.dm_ushakov.alice.aliceskill.capabilities.model.ColorScene
import ru.dm_ushakov.alice.aliceskill.capabilities.model.Scene
import ru.dm_ushakov.alice.aliceskill.capabilities.model.TemperatureRange
import ru.dm_ushakov.alice.aliceskill.capabilities.state.*
import ru.dm_ushakov.alice.aliceskill.error.DeviceErrorType.*
import ru.dm_ushakov.alice.aliceskill.error.deviceError
import ru.dm_ushakov.alice.aliceskill.util.json.*

abstract class ColorSettingCapability: BaseCapability() {
    final override val type: String get() = "devices.capabilities.color_setting"
    abstract val colorModel: ColorModel?
    abstract val temperatureRange: TemperatureRange?
    abstract val colorScene: ColorScene?
    abstract var state: BaseColorSettingCapabilityState

    final override fun getDescriptionJson() = makeJsonObject {
        put("type", type)
        put("retrievable", retrievable)
        put("reportable", reportable)

        putObject("parameters") {
            var haveParameters = false

            colorModel?.let {
                haveParameters = true
                put("color_model", it.colorModelName)
            }

            temperatureRange?.let { range ->
                haveParameters = true
                putObject("temperature_k") {
                    range.min?.let{ put("min", it) }
                    range.max?.let{ put("max", it) }
                }
            }

            colorScene?.let { scenes ->
                haveParameters = true

                putObject("color_scene") {
                    putArray("scenes", scenes.scenes) { scene ->
                        addObject {
                            put("id", scene.sceneName)
                        }
                    }
                }
            }

            if (!haveParameters) error("Should be set at least one properties - colorModel, temperatureRange, colorScene!")
        }
    }

    final override fun getStateJson() = makeJsonObject {
        put("type", type)

        putObject("state") {
            val state = this@ColorSettingCapability.state

            if (state is HSVColorSettingCapabilityState) {
                put("instance", "hsv")

                putObject("value") {
                    state.apply {
                        put("h", hue)
                        put("s", saturation)
                        put("v", value)
                    }
                }
            } else if(state is RGBColorSettingCapabilityState) {
                put("instance", "rgb")
                put("value", state.value)
            } else if(state is KelvinColorSettingCapabilityState) {
                put("instance", "temperature_k")
                put("value", state.value)
            } else if(state is SceneColorSettingCapabilityState) {
                put("instance", "scene")
                put("value", state.value.sceneName)
            } else {
                error("Invalid capability state!")
            }
        }
    }

    final override fun executeChangingCapabilityState(changeState: JsonNode) {
        val incomeState = changeState["state"]
        val incomeInstance = incomeState["instance"].textValue()
        val valueObject = incomeState["value"]

        when(incomeInstance) {
            "hsv" -> {
                if (colorModel != ColorModel.HSV) deviceError(InvalidAction, "Unsupported HSV model for this capability!")

                val hue = valueObject["h"].intValue()
                val saturation = valueObject["s"].intValue()
                val value = valueObject["v"].intValue()

                state = HSVColorSettingCapabilityState(hue, saturation, value)
            }
            "rgb" -> {
                if (colorModel != ColorModel.RGB) deviceError(InvalidAction, "Unsupported RGB model for this capability!")

                state = RGBColorSettingCapabilityState(valueObject.intValue())
            }
            "temperature_k" -> {
                val temperatureRange = this.temperatureRange
                val temperature = valueObject.intValue()

                if (temperatureRange == null) deviceError(InvalidAction, "Unsupported temperature values for this capability!")

                temperatureRange.max?.let { maxValue ->
                    if (temperature > maxValue) {
                        deviceError(
                            InvalidValue,
                            "Try to set temperature $temperature while max limit for it $maxValue!"
                        )
                    }
                }

                temperatureRange.min?.let { minValue ->
                    if (temperature < minValue) {
                        deviceError(
                            InvalidValue,
                            "Try to set temperature $temperature while min limit for it $minValue!"
                        )
                    }
                }

                if (temperature < 0) {
                    deviceError(InvalidValue, "Temperature can't be less than zero!")
                }

                state = KelvinColorSettingCapabilityState(temperature)
            }
            "scene" -> {
                val colorScene = this.colorScene
                if (colorScene == null) deviceError(InvalidAction, "Unsupported scene model for this capability!")

                val sceneName = valueObject.asText()
                val scene = Scene.getScene(sceneName)
                val sceneExists = colorScene.scenes.any { it == scene }

                if (!sceneExists) deviceError(InvalidAction, "Unsupported scene for this capability - $sceneName!")

                state = SceneColorSettingCapabilityState(scene)
            }
            else -> deviceError(InvalidAction, "Should be one of this instances set - hsv, rgb, temperature_k, scene. Got - $incomeInstance.")
        }
    }
}