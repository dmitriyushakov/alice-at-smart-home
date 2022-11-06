package ru.dm_ushakov.alice.aliceskill.authorization

interface AuthorizationService {
    fun joinRequest(userId: String): JoinRequestResult
    fun generateCode(clientId: String, callback: (String) -> Unit): GenerateCodeRequest
    fun registerCode(clientId: String, clientSecret: String, code:String): String
}