package com.ushakov.ioc.reader.xml;

import com.ushakov.ioc.entity.BeanDefinition;
import org.junit.Test;
import org.xml.sax.helpers.AttributesImpl;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class XmlHandlerTest {

    @Test
    public void testStartElement() {
        XmlHandler xmlHandler = new XmlHandler();
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, null, "id", "String", "userDao");
        attributes.addAttribute(null, null, "class", "String", "com.userstore.JdbcUserDao");
        xmlHandler.startElement(null, null, "bean", attributes);

        assertEquals(true, xmlHandler.bBean);
        assertNotNull(xmlHandler.beanDefinition);
        assertEquals("userDao", xmlHandler.beanDefinition.getId());
        assertEquals("com.userstore.JdbcUserDao", xmlHandler.beanDefinition.getClassName());

        attributes.clear();
        attributes.addAttribute(null, null, "name", "String", "url");
        attributes.addAttribute(null, null, "value", "String", "localhost");
        xmlHandler.startElement(null, null, "property", attributes);

        assertEquals(1, xmlHandler.beanDefinition.getValueDependency().size());
        assertEquals("localhost", xmlHandler.beanDefinition.getValueDependency().get("url"));

        attributes.clear();
        attributes.addAttribute(null, null, "name", "String", "jdbcUserDao");
        attributes.addAttribute(null, null, "ref", "String", "userDao");
        xmlHandler.startElement(null, null, "property", attributes);

        assertEquals(1, xmlHandler.beanDefinition.getRefDependency().size());
        assertEquals("userDao", xmlHandler.beanDefinition.getRefDependency().get("jdbcUserDao"));
    }

    @Test(expected = RuntimeException.class)
    public void testStartElementInvalidTag() {
        XmlHandler xmlHandler = new XmlHandler();
        xmlHandler.startElement(null, null, "some", null);
    }

    @Test(expected = RuntimeException.class)
    public void testStartElementPropertyOutsideBean() {
        XmlHandler xmlHandler = new XmlHandler();
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, null, "name", "String", "url");
        attributes.addAttribute(null, null, "value", "String", "localhost");
        xmlHandler.startElement(null, null, "property", attributes);
    }

    @Test(expected = RuntimeException.class)
    public void testStartElementPropertyWithoutNameAttribute() {
        XmlHandler xmlHandler = new XmlHandler();
        xmlHandler.bBean = true;
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, null, "port", "String", "3000");
        xmlHandler.startElement(null, null, "property", attributes);
    }

    @Test(expected = RuntimeException.class)
    public void testStartElementPropertyWithoutValueOrClassAttribute() {
        XmlHandler xmlHandler = new XmlHandler();
        xmlHandler.bBean = true;
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, null, "name", "String", "url");
        xmlHandler.startElement(null, null, "property", attributes);
    }

    @Test
    public void testEndElement() {
        XmlHandler xmlHandler = new XmlHandler();
        xmlHandler.bBean = true;
        BeanDefinition beanDefinition = new BeanDefinition();
        xmlHandler.beanDefinition = beanDefinition;
        xmlHandler.beanDefinitions = new ArrayList<>();
        xmlHandler.endElement(null, null, "bean");

        assertEquals(beanDefinition, xmlHandler.beanDefinitions.get(0));
        assertEquals(1, xmlHandler.beanDefinitions.size());

    }

    @Test(expected = RuntimeException.class)
    public void testEndElementInvalidXml() {
        XmlHandler xmlHandler = new XmlHandler();
        xmlHandler.bBean = false;
        xmlHandler.endElement(null, null, "bean");
    }
}