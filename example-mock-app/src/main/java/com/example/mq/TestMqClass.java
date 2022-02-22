package com.example.mq;

import com.ecc.Acknowledgement;
import com.ecc.MqType;
import com.ecc.annotation.Ack;
import com.ecc.annotation.MessagePayload;
import com.ecc.annotation.MqListener;
import com.example.dto.ExampleSmsMessageDto;


//{
//        "toAddress":"05548368737",
//        "fromAddress":"05354997213",
//        "messageContent":"testMessage....."
//}

public class TestMqClass {

    @MqListener(brokerUrl = "tcp://127.0.0.1:61616", mqType = MqType.ACTIVE_MQ, queueName = "sms-message-queue", concurrentConsumerNum = 4)
    public void onMessageCome(@MessagePayload ExampleSmsMessageDto message, @Ack String ack) {
        System.out.println(System.currentTimeMillis() + "     " + message.getMessageContent());

    }

    @MqListener(mqType = MqType.ACTIVE_MQ, queueName = "test-queue")
    public void testMessage(@MessagePayload String message, @Ack Acknowledgement ack) {
        System.out.println(System.currentTimeMillis() + "     " + "From testMessage() " + message);
    }

    @MqListener(brokerUrl = "amqp://admin:admin@localhost:5672", mqType = MqType.RABBIT_MQ, queueName = "rabbit-test-queue", concurrentConsumerNum = 3)
    public void testRabbitMq(@MessagePayload ExampleSmsMessageDto message) {
        System.out.println(System.currentTimeMillis() + "     " + "From testRabbitMq() " + message.getMessageContent());


    }


}
