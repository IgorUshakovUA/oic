package com.ushakov.ioc.processor;

import com.ushakov.ioc.annotation.PostContruct;
import com.ushakov.ioc.entity.Bean;
import com.ushakov.ioc.entity.BeanDefinition;
import com.ushakov.ioc.processor.post.BeanFactoryPostProcessor;
import com.ushakov.ioc.processor.post.BeanPostProcessor;
import org.junit.Before;
import org.junit.Test;
import sun.swing.BakedArrayList;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;

public class DefaultBeanDefinitionsProcessorTest {
    public static boolean isInvoked = false;

    @Before
    public void before() {
        isInvoked = false;
    }

    @Test
    public void testInjectReference() {
        class ObjectRef {

        }

        class ObjectTest {
            private ObjectRef field;

            ObjectRef getField() {
                return field;
            }
        }

        ObjectRef objectRef = new ObjectRef();
        ObjectTest objectTest = new ObjectTest();

        assertTrue(objectTest.getField() == null);

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.injectReference(objectTest, "field", objectRef);

        assertTrue(objectTest.getField() == objectRef);
    }

    @Test
    public void testInjectProperty() {
        class ObjectTest {
            private String field;

            String getField() {
                return field;
            }
        }

        ObjectTest objectTest = new ObjectTest();

        assertTrue(objectTest.getField() == null);

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.injectReference(objectTest, "field", "TestValue");

        assertTrue(objectTest.getField().equals("TestValue"));
    }

    @Test
    public void TestInitBeans() {
        BeanDefinition beanDefinition1 = new BeanDefinition();
        beanDefinition1.setId("bean1");
        beanDefinition1.setClassName(TestClass1.class.getCanonicalName());
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("field", "value1");
        beanDefinition1.setValueDependency(properties1);

        BeanDefinition beanDefinition2 = new BeanDefinition();
        beanDefinition2.setId("bean2");
        beanDefinition2.setClassName(TestClass2.class.getCanonicalName());
        Map<String, String> properties2 = new HashMap<>();
        properties2.put("field", "value2");
        beanDefinition2.setValueDependency(properties2);

        BeanDefinition beanDefinition3 = new BeanDefinition();
        beanDefinition3.setId("bean3");
        beanDefinition3.setClassName(TestClass3.class.getCanonicalName());
        Map<String, String> references = new HashMap<>();
        references.put("testClass2", "bean2");
        beanDefinition3.setRefDependency(references);

        List<BeanDefinition> beanDefinitions = new ArrayList();
        beanDefinitions.add(beanDefinition1);
        beanDefinitions.add(beanDefinition2);
        beanDefinitions.add(beanDefinition3);

        List<Bean> beans = new ArrayList<>();

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.initBeans(beanDefinitions, beans);

        assertEquals(3, beans.size());
        assertEquals("value1", ((TestClass1) beans.get(0).getValue()).getField());
        assertEquals("value2", ((TestClass2) beans.get(1).getValue()).getField());
        assertEquals(TestClass2.class.getCanonicalName(), ((TestClass3) beans.get(2).getValue()).getTestClass2().getClass().getCanonicalName());
    }

    @Test
    public void testInitStageA() {
        List<Bean> beans = new ArrayList<>();
        Bean bean = new Bean();
        bean.setId("bean1");
        bean.setValue(new TestClass1());
        beans.add(bean);
        bean = new Bean();
        bean.setId("bean2");
        bean.setValue(new TestClass2());
        beans.add(bean);
        bean = new Bean();
        bean.setId("bean3");
        bean.setValue(new TestClass4());
        beans.add(bean);
        List<Bean> systemBeans = new ArrayList<>();

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.initStageA(beans, systemBeans);

        assertEquals(true, isInvoked);
        assertEquals(1, systemBeans.size());
        assertEquals(2, beans.size());
    }

    @Test
    public void testInitStageB() {
        List<Bean> beans = new ArrayList<>();
        Bean bean1 = new Bean();
        bean1.setId("bean1");
        bean1.setValue(new TestClass1());
        beans.add(bean1);
        Bean bean2 = new Bean();
        bean2.setId("bean2");
        bean2.setValue(new TestClass2());
        beans.add(bean2);
        Bean bean3 = new Bean();
        bean3.setId("bean3");
        bean3.setValue(new TestClass5());
        beans.add(bean3);
        List<Bean> systemBeans = new ArrayList<>();

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.initStageB(beans, systemBeans);

        assertEquals(true, isInvoked);
        assertEquals(1, systemBeans.size());
        assertEquals(2, beans.size());
    }


    @Test
    public void TestInitStageC() {
        class TestObject {
            @PostContruct
            public void testMethod() {
                isInvoked = true;
            }
        }

        Bean bean = new Bean();
        bean.setId("test");
        bean.setValue(new TestObject());

        List<Bean> beans = new ArrayList<>();
        beans.add(bean);

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.initStageC(beans);

        assertEquals(true, isInvoked);

    }

    @Test
    public void testInitStageD() {
        List<Bean> beans = new ArrayList<>();
        Bean bean1 = new Bean();
        bean1.setId("bean1");
        bean1.setValue(new TestClass1());
        beans.add(bean1);
        Bean bean2 = new Bean();
        bean2.setId("bean2");
        bean2.setValue(new TestClass2());
        beans.add(bean2);
        List<Bean> systemBeans = new ArrayList<>();
        Bean bean3 = new Bean();
        bean3.setId("bean3");
        bean3.setValue(new TestClass5());
        systemBeans.add(bean3);

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        defaultBeanDefinitionsProcessor.initStageD(beans, systemBeans);

        assertEquals(true, isInvoked);
    }

    @Test
    public void testProcessDefinitionsITest() {
        BeanDefinition beanDefinition1 = new BeanDefinition();
        beanDefinition1.setId("bean1");
        beanDefinition1.setClassName(TestClass1.class.getCanonicalName());
        Map<String, String> properties1 = new HashMap<>();
        properties1.put("field", "value1");
        beanDefinition1.setValueDependency(properties1);

        BeanDefinition beanDefinition2 = new BeanDefinition();
        beanDefinition2.setId("bean2");
        beanDefinition2.setClassName(TestClass2.class.getCanonicalName());
        Map<String, String> properties2 = new HashMap<>();
        properties2.put("field", "value2");
        beanDefinition2.setValueDependency(properties2);

        BeanDefinition beanDefinition3 = new BeanDefinition();
        beanDefinition3.setId("bean3");
        beanDefinition3.setClassName(TestClass3.class.getCanonicalName());
        Map<String, String> references = new HashMap<>();
        references.put("testClass2", "bean2");
        beanDefinition3.setRefDependency(references);

        BeanDefinition beanDefinition4 = new BeanDefinition();
        beanDefinition4.setId("bean4");
        beanDefinition4.setClassName(TestClass4.class.getCanonicalName());

        BeanDefinition beanDefinition5 = new BeanDefinition();
        beanDefinition5.setId("bean5");
        beanDefinition5.setClassName(TestClass5.class.getCanonicalName());

        List<BeanDefinition> beanDefinitions = new ArrayList();
        beanDefinitions.add(beanDefinition1);
        beanDefinitions.add(beanDefinition2);
        beanDefinitions.add(beanDefinition3);
        beanDefinitions.add(beanDefinition4);
        beanDefinitions.add(beanDefinition5);

        DefaultBeanDefinitionsProcessor defaultBeanDefinitionsProcessor = new DefaultBeanDefinitionsProcessor();
        List<Bean> beans = defaultBeanDefinitionsProcessor.processDefinitions(beanDefinitions);

        assertEquals(3,beans.size());
        assertEquals("One more value",beans.get(0).getValue());
        assertEquals("One more value",beans.get(1).getValue());
        assertEquals("One more value",beans.get(2).getValue());
    }
}

class TestClass1 {
    private String field;

    public String getField() {
        return field;
    }
}

class TestClass2 {
    private String field;

    public String getField() {
        return field;
    }
}

class TestClass3 {
    private TestClass2 testClass2;

    public TestClass2 getTestClass2() {
        return testClass2;
    }
}

class TestClass4 implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(List<Bean> beans) {
        DefaultBeanDefinitionsProcessorTest.isInvoked = true;
    }
}

class TestClass5 implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object object) {
        DefaultBeanDefinitionsProcessorTest.isInvoked = true;
        return "New value";
    }

    @Override
    public Object postProcessAfterInitialization(Object object) {
        DefaultBeanDefinitionsProcessorTest.isInvoked = true;
        return "One more value";
    }
}