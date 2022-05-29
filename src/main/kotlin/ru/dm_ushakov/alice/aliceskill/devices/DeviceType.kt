package ru.dm_ushakov.alice.aliceskill.devices

enum class DeviceType(val deviceTypeName: String) {
    Light("devices.types.light"),
    Socket("devices.types.socket"),
    Switch("devices.types.switch"),
    Thermostat("devices.types.thermostat"),
    AirConditioner("devices.types.thermostat.ac"),
    MediaDevice("devices.types.media_device"),
    TV("devices.types.media_device.tv"),
    TVBox("devices.types.media_device.tv_box"),
    Receiver("devices.types.media_device.receiver"),
    Cooking("devices.types.cooking"),
    CoffeeMaker("devices.types.cooking.coffee_maker"),
    Kettle("devices.types.cooking.kettle"),
    Multicooker("devices.types.cooking.multicooker"),
    Openable("devices.types.openable"),
    Curtain("devices.types.openable.curtain"),
    Humidifier("devices.types.humidifier"),
    Purifier("devices.types.humidifier"),
    VacuumCleaner("devices.types.vacuum_cleaner"),
    WashingMachine("devices.types.washing_machine"),
    Dishwasher("devices.types.dishwasher"),
    Iron("devices.types.iron"),
    Sensor("devices.types.sensor"),
    Other("devices.types.other")
}