package com.ecc.config;

import com.ecc.MqType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "ecc")
public class EccMqConfiguration {

    //TODO not used yet..
    private String basePackage;


    @Value("${ecc.activemq.brokerUrl:}")
    private String activeMqBrokerUrl;

    @Value("${ecc.rabbitmq.brokerUrl:}")
    private String rabbitMqUrl;


    public String getActiveMqBrokerUrl() {
        return activeMqBrokerUrl;
    }

    public String getRabbitMqUrl() {
        return rabbitMqUrl;
    }

    public void setRabbitMqUrl(String rabbitMqUrl) {
        this.rabbitMqUrl = rabbitMqUrl;
    }

    public void setActiveMqBrokerUrl(String activeMqBrokerUrl) {
        this.activeMqBrokerUrl = activeMqBrokerUrl;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getBrokerUrl(MqType type) {
        return type.equals(MqType.ACTIVE_MQ) ? activeMqBrokerUrl : type.equals(MqType.RABBIT_MQ) ? rabbitMqUrl : null;
    }

}
