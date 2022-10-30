package ru.dm_ushakov.alice.aliceskill.util

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

fun notFound(message: String): Nothing = throw ResponseStatusException(HttpStatus.NOT_FOUND, message)