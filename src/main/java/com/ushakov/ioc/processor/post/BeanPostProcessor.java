package com.ushakov.ioc.processor.post;

public interface BeanPostProcessor <T>{
    T postProcessBeforeInitialization(T object);
    T postProcessAfterInitialization(T object);
}
