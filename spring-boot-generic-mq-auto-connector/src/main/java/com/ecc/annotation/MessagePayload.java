package com.ecc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Message bodies from the queue is parsed and converted to the desired dto.
 * <strong>Only</strong> JSON messages or strings are accepted for deserialization.
 * It supports strings and any type of dto which serialized with JSON.
 * This annotation <strong<>must</strong> be as parameter inside the method annotated with {@link MqListener @MqListener}
 *<pre>
 * {@code
 * @MqListener(queueName="xx",mqType=XX)
 * void testmethod(@MessagePayload TestXdto dto){
 *     dto.getX();
 * }
 * }
 * </pre>
 * @author Emin Cem Canoglu
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MessagePayload {
}
