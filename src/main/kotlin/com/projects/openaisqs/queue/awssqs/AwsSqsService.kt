package com.projects.openaisqs.queue.awssqs

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.sqs.SqsClient
import aws.sdk.kotlin.services.sqs.model.*
import com.projects.openaisqs.queue.MessageId
import com.projects.openaisqs.queue.QueueMessage
import com.projects.openaisqs.queue.QueueService
import com.projects.openaisqs.queue.QueueUrl
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component

@Component
class AwsSqsService(private val awsConfig: AwsConfig): QueueService {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun createQueue(queueNameVal: String): QueueUrl {
        val createQueueRequest = CreateQueueRequest {
            queueName = queueNameVal
        }
        try {
            getSQSClient().use { sqsClient ->
                val result = sqsClient.createQueue(createQueueRequest)
                if (result.queueUrl == null) {
                    throw Exception("Failed to create queue: $result")
                }
                return result.queueUrl!!
            }
        } catch (e: Exception) {
            logger.warn("Failed to create queue: $e")
            throw e
        }
    }

    override suspend fun listQueues(): List<QueueUrl> {
        try {
            getSQSClient().use { sqsClient ->
                val result = sqsClient.listQueues()
                if (result.queueUrls == null) {
                    return emptyList()
                }
                return result.queueUrls!!
            }
        } catch (e: Exception) {
            logger.warn("Failed to list queues: $e")
            throw e
        }
    }

    @Cacheable("chatQueueUrl")
    override suspend fun getQueueUrl(queueNameVal: String): QueueUrl {
        val getQueueUrlRequest = GetQueueUrlRequest {
            queueName =  queueNameVal
        }
        try {
            getSQSClient().use { sqsClient ->
                val result = sqsClient.getQueueUrl(getQueueUrlRequest)
                if (result.queueUrl == null) {
                    throw Exception("Failed to get queue: $result")
                }

                return result.queueUrl!!
            }
        } catch (e: Exception) {
            logger.warn("Failed to get queue: $e")
            throw e
        }
    }

    override suspend fun sendMessage(queueUrlVal: QueueUrl, message: String): MessageId {
        val sendRequest = SendMessageRequest {
            queueUrl = queueUrlVal
            messageBody = message
            delaySeconds = 10
        }

        try {
            getSQSClient().use { sqsClient ->
                val result = sqsClient.sendMessage(sendRequest)
                if (result.messageId == null) {
                    throw Exception("Failed to send message: $result")
                }
                return result.messageId!!
            }
        } catch (e: Exception) {
            logger.warn("Failed to send message: $e")
            throw e
        }
    }

    override suspend fun sendBatchMessages(queueUrlVal: QueueUrl, messages: List<String>): List<MessageId> {
        val batchRequestEntries = messages.map { SendMessageBatchRequestEntry { messageBody = it } }
        val sendMessageBatchRequest = SendMessageBatchRequest {
            queueUrl = queueUrlVal
            entries = batchRequestEntries
        }

        try {
            getSQSClient().use { sqsClient ->
                val result = sqsClient.sendMessageBatch(sendMessageBatchRequest)
                if (result.failed != null || result.successful == null) {
                    throw Exception("Failed to send message: $result")
                }
                return result.successful!!.mapNotNull { it.messageId }
            }
        } catch (e: Exception) {
            logger.warn("Failed to send message: $e")
            throw e
        }

    }

    override suspend fun receiveBatchMessages(queueUrlVal: QueueUrl, maxNumberOfMessagesVal: Int?): List<QueueMessage> {
        val receiveMessageRequest = ReceiveMessageRequest {
            queueUrl = queueUrlVal
            maxNumberOfMessages = maxNumberOfMessagesVal ?: 1
        }

        try {
            getSQSClient().use { sqsClient ->
                val result = sqsClient.receiveMessage(receiveMessageRequest)
                if (result.messages == null) {
                    return emptyList()
                }
                return result.messages!!.map{
                    QueueMessage (
                        it.messageId,
                        it.body
                    )
                }
            }
        } catch (e: Exception) {
            logger.warn("Failed to receive message: $e")
            throw e
        }
    }

    private fun getSQSClient(): SqsClient = SqsClient {
        region = awsConfig.region
        credentialsProvider = StaticCredentialsProvider {
            accessKeyId = awsConfig.accessKey
            secretAccessKey = awsConfig.secretKey
        }
    }
}