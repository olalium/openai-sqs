# OpenAi SQS 🧠🧠🧠
A baseline for using OpenAi's API with SQS queues. 
The "agents" configured will read messages from a queue, send them to OpenAi and then write the response to another queue. 
The app is designed to be run on AWS SQS, but any queue system which implements the QueueService interface can be used.
## Example Agent configuration
The following is an example of an agent which will act as a programmer.
```
@Component
class ProgrammerAgent (private val openAiService: OpenAiService, private val queueService: QueueService): Agent {
    override val inputQueueName = "programmer-input"
    override val outputQueueName = "programmer-output"
    
    override val modelId = "gpt-3.5-turbo-0301"
    override val systemMessageContent = "Act as an experienced kotlin spring boot programmer"
    
    @Scheduled(fixedDelay = 1000)
    override fun processQueueMessage() {
        ...
    }
}
```

# Setup
## Prerequisites
Java 17

## install, compile and build jar
1. to install dependencies, compile and build jar run ```./mvnw install```
2. then run the outputted jar with ```java -jar ```

## Environmental variables
You need to pass the following environmental variables. 
| Environmental variable | description                                                              |
|------------------------|--------------------------------------------------------------------------|
| aws-region             | aws region of your SQS service                                           |
| aws-access-key         | aws access key for your aws IAM user (Need the appropriate permissions)  |
| aws-secret-key         | aws secret key for your aws IAM user (Need the appropriate permissions)  |
| openai-api-key         | your OpenAi api key                                                      |

# Future work
- [ ] Add more agents
- [ ] Add support for user chat history
- [ ] Add vector database support for storing responses
- [ ] Add support for AWS Cognito / Azure AD for authentication
- [ ] Deploy cost-effective solution on AWS