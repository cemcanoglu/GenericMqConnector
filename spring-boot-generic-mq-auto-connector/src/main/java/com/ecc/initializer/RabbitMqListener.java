package com.ecc.initializer;

import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.ForgivingExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RabbitMqListener implements MqMessageListener {

    private Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);

    @Override
    public void listen(String brokenUrl, int concurrentConsumerNum, String queueName, BeanObject beanObject) {
        ConnectionFactory factory = new ConnectionFactory();
        try {
            ForgivingExceptionHandler forgivingExceptionHandler = new ForgivingExceptionHandler();
            factory.setUri(brokenUrl);
            factory.setExceptionHandler(forgivingExceptionHandler);
            Connection con = factory.newConnection();
            for (int i = 0; i < concurrentConsumerNum; i++) {
                Channel channel = con.createChannel();
                AMQP.Queue.DeclareOk declare = channel.queueDeclare(queueName, false, false, false, null);
                String queue = declare.getQueue();

                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    try {
                        String msg = new String(message.getBody());
                        MethodInvoker.invoke(beanObject, msg, () -> {
                            try {
                                long deliveryTag = message.getEnvelope().getDeliveryTag();
                                channel.basicAck(deliveryTag, false);
                            } catch (IOException e) {
                                logger.error("An error occurred while acknowledgement sending to queue -->" + queueName, e);
                            }
                        });
                    } catch (Exception e) {
                        logger.error("An unknown error occurred while consuming queue -->" + queueName, e);
                    }
                };
                channel.basicConsume(queue, false, deliverCallback, consumerTag -> {
                });

            }
            logger.info("Connected to queue (" + queueName + ") with " + concurrentConsumerNum + " consumer");

        } catch (Exception e) {
            logger.error("Because of an unknown error, cannot started to listen Rabbitmq", e);
        }

    }
}
