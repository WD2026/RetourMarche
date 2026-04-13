package com.example.shop.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseUrlConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSourceProperties) {
            DataSourceProperties properties = (DataSourceProperties) bean;
            if (properties.getUrl() != null && properties.getUrl().startsWith("postgresql://")) {
                properties.setUrl(properties.getUrl().replaceFirst("postgresql://", "jdbc:postgresql://"));
            } else if (properties.getUrl() != null && properties.getUrl().startsWith("postgres://")) {
                properties.setUrl(properties.getUrl().replaceFirst("postgres://", "jdbc:postgresql://"));
            }
        }
        return bean;
    }
}
