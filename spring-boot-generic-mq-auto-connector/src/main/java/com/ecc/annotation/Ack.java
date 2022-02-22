package com.ecc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



/**
 * It provides doing <strong>manually</strong> acknowledgement. It should be used for {@link com.ecc.Acknowledgement}
 * parameter of a method which annotated {@link MqListener}
 * <pre>
 * {@code
 * @MqListener(queueName="xx",mqType=XX)
 * void testmethod(@Ack Acknowledgement ack){
 *     //some codes
 *     .
 *     .
 *     ack.doAck():
 * }
 * }
 * </pre>
 * @see com.ecc.Acknowledgement
 * @author Emin Cem Canoglu
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Ack {
}
