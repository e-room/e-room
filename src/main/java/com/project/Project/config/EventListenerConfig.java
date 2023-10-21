package com.project.Project.config;

import com.project.Project.common.exception.CustomException;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.repository.EventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class EventListenerConfig {

    private final ApplicationContext applicationContext;


    private final List<EventListener> eventListeners;

    @PostConstruct
    public void injectRepository() {
        eventListeners.forEach((eventListener -> {
            Arrays.stream(eventListener.getClass().getDeclaredFields())
                    .filter(field -> Modifier.isStatic(field.getModifiers()))
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            field.set(field.getType(), applicationContext.getBean(field.getType()));
                        } catch (IllegalAccessException e) {
                            throw new CustomException(ErrorCode.EVENT_LISTENER_INJECTION);
                        }
                    });
        }));
    }

}
