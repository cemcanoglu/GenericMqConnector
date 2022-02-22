package com.ecc.initializer;

import com.ecc.config.EccMqConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {EccMqConfiguration.class})
public class EccMqAutoConfiguration {

    @Bean
    ApplicationInitializer addBeanToApplicationContext() {
        return new ApplicationInitializer();
    }
}
