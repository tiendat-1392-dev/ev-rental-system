package com.webserver.evrentalsystem.remoteconfig;

import com.webserver.evrentalsystem.entity.AppConfig;
import com.webserver.evrentalsystem.repository.AppConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RemoteConfigUtils {

    @Autowired
    private AppConfigRepository appConfigRepository;

    @SuppressWarnings("unchecked")
    public <T> T getAppConfig(Config config, Class<T> clazz) {
        try {
            Optional<AppConfig> appConfig = appConfigRepository.findById(config.getKey());
            if (appConfig.isEmpty()) {
                AppConfig newAppConfig = new AppConfig();
                newAppConfig.setKey(config.getKey());
                newAppConfig.setValue(config.getDefaultValue());
                appConfigRepository.save(newAppConfig);
                return (T) convertToType(config.getDefaultValue(), clazz);
            } else {
                return (T) convertToType(appConfig.get().getValue(), clazz);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void setAppConfig(Config config, String value) {
        Optional<AppConfig> appConfig = appConfigRepository.findById(config.getKey());
        if (appConfig.isPresent()) {
            appConfig.get().setValue(value);
            appConfigRepository.save(appConfig.get());
        } else {
            AppConfig newAppConfig = new AppConfig();
            newAppConfig.setKey(config.getKey());
            newAppConfig.setValue(value);
            appConfigRepository.save(newAppConfig);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T convertToType(String value, T type) {
        if (type == Boolean.class) {
            return (T) Boolean.valueOf(value);
        } else if (type == Integer.class) {
            return (T) Integer.valueOf(value);
        } else if (type == Long.class) {
            return (T) Long.valueOf(value);
        } else if (type == Float.class) {
            return (T) Float.valueOf(value);
        } else if (type == Double.class) {
            return (T) Double.valueOf(value);
        } else if (type == String.class) {
            return (T) value;
        } else {
            return null;
        }
    }
}