package com.ecc.initializer;

import com.ecc.MqType;

class MqListenerFactory {

    public MqMessageListener createListener(MqType type) {
        if (type.equals(MqType.ACTIVE_MQ)) {
            return new ActiveMqListener();
        } else if (type.equals(MqType.RABBIT_MQ)) {
            return new RabbitMqListener();
        } else {
            throw new RuntimeException("MQ type does not supported.");
        }
    }

}
