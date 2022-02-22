package com.ecc.initializer;

import com.ecc.MqType;
import com.ecc.annotation.MqListener;
import com.ecc.config.EccMqConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;


class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private Logger logger = LoggerFactory.getLogger(ApplicationInitializer.class);
    @Autowired
    private EccMqConfiguration configuration;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        String defaultBasePackage = event.getApplicationContext().
                getBeansWithAnnotation(SpringBootApplication.class).entrySet()
                .stream()
                .findFirst()
                .get()
                .getValue().getClass().getPackage().getName();

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder().setUrls(
                ClasspathHelper.forPackage(defaultBasePackage)).setScanners(Scanners.MethodsAnnotated);

        Reflections reflections = new Reflections(configurationBuilder);
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(MqListener.class);

        if (ObjectUtils.isEmpty(methodsAnnotatedWith)) {
            logger.error("cannot find any method annotated @MqListener(queueName=\"{QUEUE_NAME}\") under classpath " + defaultBasePackage + ".*");
            return;
        }
        MqListenerFactory listenerFactory = new MqListenerFactory();
        methodsAnnotatedWith.forEach(method -> {
            MqListener declaredAnnotation = method.getDeclaredAnnotation(MqListener.class);
            String queueName = declaredAnnotation.queueName();
            MqType mqType = declaredAnnotation.mqType();
            String brokerUrl = declaredAnnotation.brokerUrl().isEmpty() ? configuration.getBrokerUrl(mqType) : declaredAnnotation.brokerUrl();
            if (!StringUtils.hasText(brokerUrl)) {
                logger.error("brokerUrl not found to connect " + queueName);
                return;
            }
            BeanObject beanObject = generateBean(method);
            int concurrentConsumerNum = declaredAnnotation.concurrentConsumerNum();
            MqMessageListener listener = listenerFactory.createListener(mqType);
            listener.listen(brokerUrl, concurrentConsumerNum, queueName, beanObject);
        });
    }

    private BeanObject generateBean(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();
        try {
            //TODO bean instance should be created as singleton
            Object beanInstance = declaringClass.getDeclaredConstructor().newInstance();
            return new BeanObject(beanInstance, method);

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            //TODO should be implemented
            e.printStackTrace();
        }
        return null;
    }
}


