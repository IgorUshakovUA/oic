package com.ushakov.ioc.context.classpath;

import com.ushakov.ioc.context.ApplicationContext;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class ClassPathApplicationContextTest {
    private static final String data = "<beans>\n" +
            "   <bean id=\"string\" class=\"java.lang.String\">\n" +
            "      <property name=\"value\" value=\"Hello, World!\"/>\n" +
            "   </bean>\n" +
            "   <bean id=\"exception\" class=\"java.lang.Exception\">\n" +
            "      <property name=\"value\" value=\"1\"/>\n" +
            "   </bean>\n" +
            "</beans>";

    @Test
    public void getBeanByIdAsObject() throws Exception {
        try (InputStream inputStream = new ByteArrayInputStream(data.getBytes());) {
            ApplicationContext applicationContext = new ClassPathApplicationContext(inputStream);
            Object o = applicationContext.getBean("string");

            assertNotNull(o);
        }
    }

    @Test
    public void getBeanByIdAsException() throws Exception {
        try (InputStream inputStream = new ByteArrayInputStream(data.getBytes());) {
            ApplicationContext applicationContext = new ClassPathApplicationContext(inputStream);
            Exception e = applicationContext.getBean("exception", Exception.class);

            assertNotNull(e);
        }
    }

    @Test
    public void getBeanByClass() throws Exception {
        try (InputStream inputStream = new ByteArrayInputStream(data.getBytes());) {
            ApplicationContext applicationContext = new ClassPathApplicationContext(inputStream);
            String s = applicationContext.getBean(String.class);

            assertNotNull(s);
        }
    }

    @Test
    public void getBeanNames() throws Exception {
        try (InputStream inputStream = new ByteArrayInputStream(data.getBytes());) {
            ApplicationContext applicationContext = new ClassPathApplicationContext(inputStream);
            List<String> beanNames = applicationContext.getBeanNames();

            assertEquals(2, beanNames.size());
            assertEquals("string", beanNames.get(0));
            assertEquals("exception", beanNames.get(1));
        }
    }
}