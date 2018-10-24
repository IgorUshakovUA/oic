package com.ushakov.ioc.context;

import java.util.List;

public interface ApplicationContext {
    <T> T getBean(Class<T> clazz);
    Object getBean(String id);
    <T> T getBean(String id, Class<T> clazz);
    List<String> getBeanNames();

}
