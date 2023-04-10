package com.projects.openaisqs.openai.api.models


data class Message(
    val role: String,
    val content: String,
)

fun createSystemMessage(content: String): Message {
    return Message("system", content)
}
fun createUserMessage(content: String): Message {
    return Message("user", content)
}
fun createAssistantMessage(content: String): Message {
    return Message("assistant", content)
}