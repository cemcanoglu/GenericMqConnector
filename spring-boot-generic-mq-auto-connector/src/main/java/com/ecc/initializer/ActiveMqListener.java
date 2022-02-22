package com.ecc.initializer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ActiveMqListener implements MqMessageListener {

    private Logger logger = LoggerFactory.getLogger(ActiveMqListener.class);

    public void listen(String brokenUrl, int concurrentConsumerNum, String queueName, BeanObject beanObject) {
        try {

            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
            factory.setBrokerURL(brokenUrl);
            Connection connection = factory.createConnection();
            ExecutorService executorService = Executors.newFixedThreadPool(concurrentConsumerNum);
            for (int i = 0; i < concurrentConsumerNum; i++) {
                Session session = connection.createSession(true, 1);
                Queue sms_messages = session.createQueue(queueName);
                MessageConsumer consumer = session.createConsumer(sms_messages);
                connection.start();

                executorService.execute(() -> {
                    boolean errorOccurred = false;
                    while (!errorOccurred) {
                        try {
                            ActiveMQTextMessage message = (ActiveMQTextMessage) consumer.receive();
                            MethodInvoker.invoke(beanObject, message.getText(), () -> {
                                try {
                                    message.acknowledge();
                                    session.commit();
                                } catch (JMSException e) {
                                    logger.error("Acknowledgement error occurred on activemq (queue : " + queueName + ")");
                                }
                            });

                        } catch (JMSException e) {
                            logger.error("An error occurred on activemq and it stopped listening queue -->" + queueName, e);
                            errorOccurred = true;
                        } catch (Exception e) {
                            logger.error("An unknown error occurred in queue -->" + queueName, e);
                        }

                    }
                });
            }
            logger.info("Connected to queue (" + queueName + ") with " + concurrentConsumerNum + " thread");
        } catch (JMSException e) {
            logger.error("An error occurred on startup.. while trying to connect (" + queueName + ")", e);
        }

    }

}
