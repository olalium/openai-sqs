package com.projects.openaisqs.queue

typealias MessageId = String
typealias QueueUrl = String
data class QueueMessage (
    val messageId: MessageId?,
    val body: String?
)

interface QueueService {
    suspend fun createQueue(queueNameVal: String): QueueUrl
    suspend fun listQueues(): List<QueueUrl>
    suspend fun getQueueUrl(queueNameVal: String): QueueUrl
    suspend fun sendMessage(queueUrlVal: QueueUrl, message: String): MessageId
    suspend fun sendBatchMessages(queueUrlVal: QueueUrl, messages: List<String>): List<MessageId>
    suspend fun receiveBatchMessages(queueUrlVal: QueueUrl, maxNumberOfMessagesVal: Int?): List<QueueMessage>
}