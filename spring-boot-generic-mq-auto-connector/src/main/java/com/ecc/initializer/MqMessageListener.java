package com.ecc.initializer;

interface MqMessageListener {

    void listen(String brokenUrl, int concurrentConsumerNum, String queueName, BeanObject beanObject);

}
