package com.ushakov.ioc.util;

import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {
    public static boolean implemetsInterface(Object object, Class clazz) {
        for (Class<?> anInterface : object.getClass().getInterfaces()) {
            if(anInterface == clazz) {
                return true;
            }
        }
        return false;
    }

    public static List<Method> methodsContainingAnnotation(Object object, Class<? extends Annotation> annotationClass) {
        List<Method> methods = new ArrayList<>();
        for (Method method : object.getClass().getMethods()) {
            if(method.isAnnotationPresent(annotationClass)) {
                methods.add(method);
            }
        }
        return methods;
    }
}
