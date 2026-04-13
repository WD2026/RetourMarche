package com.example.shop.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class DatabaseUrlConfig implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSourceProperties) {
            DataSourceProperties properties = (DataSourceProperties) bean;
            String originalUrl = properties.getUrl();
            
            if (originalUrl != null && (originalUrl.startsWith("postgresql://") || originalUrl.startsWith("postgres://"))) {
                try {
                    URI uri = new URI(originalUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() != -1 ? uri.getPort() : 5432;
                    String path = uri.getPath(); 
                    
                    String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                    
                    if (uri.getUserInfo() != null) {
                        String[] userInfo = uri.getUserInfo().split(":");
                        if (userInfo.length > 0) properties.setUsername(userInfo[0]);
                        if (userInfo.length > 1) properties.setPassword(userInfo[1]);
                    }
                    
                    properties.setUrl(jdbcUrl);
                } catch (Exception e) {
                    // Fallback to simple replace if URI parsing fails
                    properties.setUrl(originalUrl.replaceFirst("postgres(ql)?://", "jdbc:postgresql://"));
                }
            }
        }
        return bean;
    }
}
