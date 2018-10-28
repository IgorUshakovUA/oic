package com.ushakov.ioc.processor;

import com.ushakov.ioc.entity.Bean;
import com.ushakov.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionsProcessor {
    List<Bean> processDefinitions(List<BeanDefinition> beanDefinitions);
}
