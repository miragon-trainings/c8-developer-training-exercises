package io.miragon.example.adapter.process.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Properties;

@PropertySource(
    value = {"classpath:zeebe-application.yaml"},
    factory = ZeebeEnvironmentConfiguration.YamlPropertySourceFactory.class
)
public class ZeebeEnvironmentConfiguration {

    public static class YamlPropertySourceFactory implements PropertySourceFactory {
        @Override
        public org.springframework.core.env.PropertySource<?> createPropertySource(String name, EncodedResource resource) {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            Properties properties = factory.getObject();
            if (properties == null) {
                properties = new Properties();
            }
            String propertySourceName = name != null ? name : resource.getResource().getFilename();
            return new PropertiesPropertySource(propertySourceName, properties);
        }
    }
}
