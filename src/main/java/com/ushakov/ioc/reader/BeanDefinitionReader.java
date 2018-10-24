package com.ushakov.ioc.reader;

import com.ushakov.ioc.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> getBeanDefinitions();
}
