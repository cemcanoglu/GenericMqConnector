package com.ecc.initializer;

import com.ecc.Acknowledgement;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class MethodInvoker {

    public static void invoke(BeanObject beanObject, String message, Acknowledgement acknowledgement) throws InvocationTargetException {
        Method method = beanObject.getMethod();
        Object bean = beanObject.getObject();
        int payloadIndex = beanObject.getPayloadIndex();
        int ackIndex = beanObject.getAckIndex();
        boolean manualAckFound = beanObject.isManualAckFound();
        Object[] parameterValues = beanObject.getDefaultParameterValues();
        Gson gson = new Gson();

        Class<?> payloadType = method.getParameterTypes()[payloadIndex];
        parameterValues[payloadIndex] = payloadType.isAssignableFrom(String.class) ? message : gson.fromJson(message, payloadType);

        try {
            if (manualAckFound) {
                parameterValues[ackIndex] = acknowledgement;
                method.invoke(bean, parameterValues);
            } else {
                method.invoke(bean, parameterValues);
                acknowledgement.doAck();
            }
        } catch (IllegalAccessException e) {
            //TODO
            e.printStackTrace();
        }
    }

}
