package com.project.Project.common.util;

import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class PropertyUtil {

    public static String getProperty(String propertyName) {
        return getProperty(propertyName, null);
    }

    public static String getProperty(String propertyName, String defaultValue) {
        String value = defaultValue;
        ApplicationContext applicationContext = ApplicationContextServe.getApplicationContext();
        value = Optional.ofNullable(applicationContext.getEnvironment().getProperty(propertyName)).orElse(defaultValue);
        return value;
    }
}
