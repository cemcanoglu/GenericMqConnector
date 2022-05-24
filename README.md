# Mq auto connector for Spring

## Purpose

The purpose of this project is providing, easy usage of connecting and listening to 
asynchronous queues using a common interface.

## Usage
You just put following dependency to your spring boot project's pom.xml
```xml
        <dependency>
            <groupId>com.ecc</groupId>
            <artifactId>spring-boot-generic-mq-auto-connector</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```
Then you can easily implement your code written under spring base package 
like in [Example Codes](#Example Codes) 


## Useful docker commands
``` bash
docker run -p 4369:4369 -p 5672:5672 -p 15672:15672 -p 25672:25672 --name rabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin  bitnami/rabbitmq:latest
docker run -p 61616:61616 -p 8161:8161 rmohr/activemq
```

## Example Codes
### ExampleSmsMessageDto(JSON)
```json
{
        "toAddress":"05548368737",
        "fromAddress":"05354997213",
        "messageContent":"testMessage....."
}
```
```java
@Component
public class TestMqClass {
    
    @MqListener(brokerUrl = "tcp://127.0.0.1:61616", mqType = MqType.ACTIVE_MQ, queueName = "sms-message-queue", concurrentConsumerNum = 4)
    public void onMessageCome(@MessagePayload ExampleSmsMessageDto message) {
        System.out.println(System.currentTimeMillis() + "     " + message.getMessageContent());
    }
}
```
```java
// NOTE : in this example brokerUrl comes from application property file or environment variables.
@Component
public class TestMqClassManualAck {

    @MqListener(mqType = MqType.ACTIVE_MQ, queueName = "test-queue")
    public void onMessageComeDoYourAck(@MessagePayload String message, @Ack Acknowledgement ack) {
        System.out.println(message);
        ack.doAck();
    }
}
```

## Example Configuration

You do not need to add following configuration in case using "brokerUlr" in MqListener annotation
This is only need for external configuration.

```yaml
ecc:
  activemq:
    brokerUrl: ${ACTIVE_MQ_BROKER:tcp://127.0.0.1:61616}
  rabbitmq:
    brokerUrl: ${RABBIT_MQ_BROKER:amqp://admin:admin@localhost:5672}
```

## Todos
Project is in development process.In next versions, auto reconnect and some failover mechanisms will be added.
And also other consumer implementations of MQs and Data Streams like Amazon SQS, ZeroMq and Kafka will be added in next versions.
