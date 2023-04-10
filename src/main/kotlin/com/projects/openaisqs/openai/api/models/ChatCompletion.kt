package com.projects.openaisqs.openai.api.models

import com.fasterxml.jackson.annotation.JsonProperty


data class ChatCompletionResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage,
    @JsonProperty("object") val objectName: String,
    @JsonProperty("created") val createdTimestamp: Long,
)

data class Choice(
    val index: Int,
    val message: Message,
    @JsonProperty("finish_reason") val finishReason: String
)

data class Usage(
    @JsonProperty("prompt_tokens") val promptTokens: Int,
    @JsonProperty("completion_tokens") val completionTokens: Int,
    @JsonProperty("total_tokens") val totalTokens: Int
)

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
)
