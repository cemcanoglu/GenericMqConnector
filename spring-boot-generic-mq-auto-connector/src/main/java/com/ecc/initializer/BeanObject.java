package com.ecc.initializer;

import com.ecc.Acknowledgement;
import com.ecc.annotation.Ack;
import com.ecc.annotation.MessagePayload;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

final class BeanObject {

    private final Object object;

    private final Method method;

    private int payloadIndex;

    private int ackIndex;

    private final Object[] defaultParameterValues;

    boolean payloadFound = false;

    boolean manualAckFound = false;

    public BeanObject(Object object, Method method) {


        this.object = object;
        this.method = method;

        int parameterCount = method.getParameterCount();
        defaultParameterValues = new Object[parameterCount];
        Class<?>[] parameterTypes = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameterCount; i++) {
            if (!payloadFound && parameters[i].isAnnotationPresent(MessagePayload.class)) {
                payloadIndex = i;
                payloadFound = true;
            }
            if (parameterTypes[i].isPrimitive()) {
                defaultParameterValues[i] = 0;
            }
            if (!manualAckFound && parameters[i].isAnnotationPresent(Ack.class) && parameterTypes[i].isAssignableFrom(Acknowledgement.class)) {
                ackIndex = i;
                manualAckFound = true;
            }

        }
        if (!payloadFound) {
            throw new RuntimeException("Cannot find @MessagePayload Annotation on parameter of method -> " + method.getDeclaringClass().getName() + "." + method.getName());
        }
    }

    public boolean isManualAckFound() {
        return manualAckFound;
    }

    public int getAckIndex() {
        return ackIndex;
    }

    public Object[] getDefaultParameterValues() {
        return defaultParameterValues;
    }

    public int getPayloadIndex() {
        return payloadIndex;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

}
