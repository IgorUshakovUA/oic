package com.ushakov.ioc.processor;

import com.ushakov.ioc.annotation.PostContruct;
import com.ushakov.ioc.entity.Bean;
import com.ushakov.ioc.entity.BeanDefinition;
import com.ushakov.ioc.processor.post.BeanFactoryPostProcessor;
import com.ushakov.ioc.processor.post.BeanPostProcessor;
import com.ushakov.ioc.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DefaultBeanDefinitionsProcessor implements BeanDefinitionsProcessor {

    @Override
    public List<Bean> processDefinitions(List<BeanDefinition> beanDefinitions) {
        List<Bean> beans = new ArrayList<>();
        List<Bean> systemBeans = new ArrayList<>();

        initBeans(beanDefinitions, beans);
        initStageA(beans, systemBeans);
        initStageB(beans, systemBeans);
        initStageC(beans);
        initStageD(beans, systemBeans);

        return beans;
    }

    void injectProperty(Object object, String name, String value) {
        injectReference(object, name, value);
    }

    void injectReference(Object object, String name, Object value) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase(name)) {
                boolean isAccessible = field.isAccessible();
                if (!isAccessible) {
                    field.setAccessible(true);
                }
                try {
                    field.set(object, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (!isAccessible) {
                        field.setAccessible(false);
                    }
                }
            }
        }
    }

    void initBeans(List<BeanDefinition> beanDefinitions, List<Bean> beans) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            Bean bean = new Bean();
            bean.setId(beanDefinition.getId());
            try {
                Object object = Class.forName(beanDefinition.getClassName()).newInstance();
                bean.setValue(object);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
            Map<String, String> valueDependencies = beanDefinition.getValueDependency();
            if (valueDependencies != null) {
                for (String key : valueDependencies.keySet()) {
                    if (!key.equalsIgnoreCase("type")) {
                        injectProperty(bean.getValue(), key, valueDependencies.get(key));
                    }
                }
            }
            Map<String, String> refDependencies = beanDefinition.getRefDependency();
            if (refDependencies != null) {
                for (String key : refDependencies.keySet()) {
                    for (Bean refBean : beans) {
                        if (refDependencies.get(key).equalsIgnoreCase(refBean.getId())) {
                            injectReference(bean.getValue(), key, refBean.getValue());
                        }
                    }
                }
            }
            beans.add(bean);
        }
    }

    void initStageA(List<Bean> beans, List<Bean> systemBeans) {
        Iterator<Bean> iterator = beans.iterator();
        while (iterator.hasNext()) {
            Bean bean = iterator.next();
            if (ReflectionUtil.implemetsInterface(bean.getValue(), BeanFactoryPostProcessor.class)) {
                systemBeans.add(bean);
                iterator.remove();
            }
        }

        for (Bean systemBean : systemBeans) {
            try {
                Object object = systemBean.getValue();
                Method method = object.getClass().getDeclaredMethod("postProcessBeanFactory", List.class);
                method.invoke(object, beans);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }

    void initStageB(List<Bean> beans, List<Bean> systemBeans) {
        Iterator<Bean> iterator = beans.iterator();
        while (iterator.hasNext()) {
            Bean bean = iterator.next();
            if (ReflectionUtil.implemetsInterface(bean.getValue(), BeanPostProcessor.class)) {
                systemBeans.add(bean);
                iterator.remove();
            }
        }

        for (Bean systemBean : systemBeans) {
            Object object = systemBean.getValue();
            if (ReflectionUtil.implemetsInterface(object, BeanPostProcessor.class)) {
                try {
                    Method method = object.getClass().getDeclaredMethod("postProcessBeforeInitialization", Object.class);
                    for (Bean bean : beans) {
                        bean.setValue(method.invoke(object, bean.getValue()));
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    void initStageC(List<Bean> beans) {
        for (Bean bean : beans) {
            List<Method> methods = ReflectionUtil.methodsContainingAnnotation(bean.getValue(), PostContruct.class);
            for (Method method : methods) {
                try {
                    method.invoke(bean.getValue());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    void initStageD(List<Bean> beans, List<Bean> systemBeans) {
        for (Bean systemBean : systemBeans) {
            Object object = systemBean.getValue();
            if (ReflectionUtil.implemetsInterface(object, BeanPostProcessor.class)) {
                try {
                    Method method = object.getClass().getDeclaredMethod("postProcessAfterInitialization", Object.class);
                    for (Bean bean : beans) {
                        bean.setValue(method.invoke(object, bean.getValue()));
                    }
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
