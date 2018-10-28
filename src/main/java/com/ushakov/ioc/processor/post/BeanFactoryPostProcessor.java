package com.ushakov.ioc.processor.post;

import com.ushakov.ioc.entity.Bean;

import java.util.List;

public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(List<Bean> beans);
}
