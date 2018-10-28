package com.ushakov.ioc.util;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.*;

public class ReflectionUtilTest {

    @Test
    public void TestImplemetsInterface() {
        assertTrue(ReflectionUtil.implemetsInterface(new String(), CharSequence.class));
        assertFalse(ReflectionUtil.implemetsInterface(new String(), Runnable.class));
    }

    @Test
    public void testMethodsContainingAnnotation() {
        List<Method> methods = ReflectionUtil.methodsContainingAnnotation(new ReflectionUtilTest(), Test.class);
        assertEquals(2,methods.size());
    }
}