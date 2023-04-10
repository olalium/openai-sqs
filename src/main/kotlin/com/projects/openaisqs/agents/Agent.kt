package com.projects.openaisqs.agents

interface Agent {
    val modelId: String
    val systemMessageContent: String
    val inputQueueName: String
    val outputQueueName: String
    fun processQueueMessage()
}

class NoMessageException(message: String): Exception(message)