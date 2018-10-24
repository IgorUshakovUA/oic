package com.ushakov.ioc.reader.xml;

import com.ushakov.ioc.entity.BeanDefinition;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.junit.Assert.*;

public class XmlBeanDefinitionReaderTest {

    @Test
    public void testGetBeanDefinitions() throws Exception {
        String data = "<beans>\n" +
                "    <bean id=\"userDao\" class=\"com.userstore.JdbcUserDao\">\n" +
                "        <property name=\"url\" value=\"localhost\"/>\n" +
                "        <property name=\"port\" value=\"3000\"/>\n" +
                "    </bean>\n" +
                "\n" +
                "    <bean id=\"userService\" class=\"com.userstore.DefaultUserService\">\n" +
                "        <property name=\"suffix\" value=\"value_suffix\"/>\n" +
                "        <property name=\"jdbcUserDao\" ref=\"userDao\"/>\n" +
                "    </bean>\n" +
                "</beans>";
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes());) {
            XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(byteArrayInputStream);
            List<BeanDefinition> beanDefinitions = xmlBeanDefinitionReader.getBeanDefinitions();
            assertEquals(2, beanDefinitions.size());
            assertEquals(2, beanDefinitions.get(0).getValueDependency().size());
            assertEquals("localhost", beanDefinitions.get(0).getValueDependency().get("url"));
            assertEquals("3000", beanDefinitions.get(0).getValueDependency().get("port"));
            assertEquals(1, beanDefinitions.get(1).getValueDependency().size());
            assertEquals("value_suffix", beanDefinitions.get(1).getValueDependency().get("suffix"));
            assertEquals(1, beanDefinitions.get(1).getRefDependency().size());
            assertEquals("userDao", beanDefinitions.get(1).getRefDependency().get("jdbcUserDao"));
        }
    }
}