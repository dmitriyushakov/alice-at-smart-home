package ru.dm_ushakov.alice.aliceskill.util.authorization

import kotlin.random.Random

private val authTokenChars = sequenceOf('a'..'z', 'A'..'Z', '0'..'9').flatten().toList().toCharArray()

private fun generateRandomString(charCount: Int, charArray: CharArray): String {
    val random = Random.Default
    val charBuf = CharArray(charCount) { _ -> charArray[random.nextInt(charArray.size)] }
    return String(charBuf)
}

fun cutAuthorizationHeaderValue(header: String): String {
    val prefix = "Bearer "
    if (header.startsWith(prefix)) {
        return header.substring(prefix.length)
    } else {
        return header
    }
}

fun generateRandomCode() = generateRandomString(8, authTokenChars)
fun generateRandomToken() = generateRandomString(32, authTokenChars)