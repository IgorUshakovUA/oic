package com.ushakov.ioc.context.classpath;

import com.ushakov.ioc.context.ApplicationContext;
import com.ushakov.ioc.entity.BeanDefinition;
import com.ushakov.ioc.reader.BeanDefinitionReader;
import com.ushakov.ioc.reader.xml.XmlBeanDefinitionReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    private List<BeanDefinition> beanDefinitions;
    private BeanDefinitionReader beanDefinitionReader;

    private void init() {
        beanDefinitions = beanDefinitionReader.getBeanDefinitions();
    }

    public ClassPathApplicationContext(String path) {
        beanDefinitionReader = new XmlBeanDefinitionReader(path);
        init();
    }

    public ClassPathApplicationContext(InputStream inputStream) {
        beanDefinitionReader = new XmlBeanDefinitionReader(inputStream);
        init();
    }

    public <T> T getBean(Class<T> clazz) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            String className = beanDefinition.getClassName();
            if (className != null && className.equals(clazz.getCanonicalName())) {
                try {
                    return (T)clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public Object getBean(String id) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (beanDefinition.getId().equals(id)) {
                String className = beanDefinition.getClassName();
                if (className != null) {
                    try {
                        Class clazz = Class.forName(className);
                        return clazz.newInstance();
                    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
        }
        return null;
    }

    public <T> T getBean(String id, Class<T> clazz) {
        return (T) getBean(id);
    }

    public List<String> getBeanNames() {
        List<String> beanNames = new ArrayList<>();

        for (BeanDefinition beanDefinition : beanDefinitions) {
            beanNames.add(beanDefinition.getId());
        }

        return beanNames;
    }

    @Override
    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }

}
