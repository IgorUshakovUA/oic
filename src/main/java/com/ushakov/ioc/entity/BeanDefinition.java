package com.ushakov.ioc.entity;

import java.util.Map;

public class BeanDefinition {
    private String id;
    private String className;
    Map<String,String> valueDependency;
    Map<String,String> refDependency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getValueDependency() {
        return valueDependency;
    }

    public void setValueDependency(Map<String, String> valueDependency) {
        this.valueDependency = valueDependency;
    }

    public Map<String, String> getRefDependency() {
        return refDependency;
    }

    public void setRefDependency(Map<String, String> refDependency) {
        this.refDependency = refDependency;
    }
}
