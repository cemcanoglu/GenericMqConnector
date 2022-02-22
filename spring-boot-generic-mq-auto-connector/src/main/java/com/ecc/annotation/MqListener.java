package com.ecc.annotation;

import com.ecc.MqType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MQ connection can be provided putting @MqListener annotation over a
 * method of any class written anywhere in the spring boot component base package.
 *
 * @author Emin Cem Canoglu
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MqListener {

    /**
     * Represents the number of concurrent listening threads on "blocking" connections
     * such as activemq.
     * Represents the number of listener channels on "non-blocking" connections like rabbitmq
     * Default value is 1
     */
    int concurrentConsumerNum() default 1;

    /**
     * The name of the queue to listen.
     */
    String queueName();

    /**
     * The URL of the mq to be connected. If this is not used in MqListener annotation,
     * brokerUrl will be automatically configured by property file like application.yml, application.properties
     * example of property key in property file is --> ecc.activemq.brokerUrl=tcp://127.0.0.1:61616
     */
    String brokerUrl() default "";

    /**
     * The type of the mq like ActiveMq, RabbitMq...
     */
    MqType mqType();
}
