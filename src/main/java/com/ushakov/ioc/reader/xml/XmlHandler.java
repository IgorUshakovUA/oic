package com.ushakov.ioc.reader.xml;

import com.ushakov.ioc.entity.BeanDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XmlHandler extends DefaultHandler {
    ArrayList<BeanDefinition> beanDefinitions = new ArrayList<>();
    BeanDefinition beanDefinition;

    boolean bBean = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        if (qName.equalsIgnoreCase("bean")) {
            bBean = true;
            beanDefinition = new BeanDefinition();
            beanDefinition.setId(attributes.getValue("id"));
            beanDefinition.setClassName(attributes.getValue("class"));
            beanDefinition.setValueDependency(new HashMap<>());
            beanDefinition.setRefDependency(new HashMap<>());
        } else if (bBean && qName.equalsIgnoreCase("property")) {
            String name = attributes.getValue("name");
            if(name != null) {
                name = name.trim();
            }
            String value = attributes.getValue("value");
            if(value != null) {
                value = value.trim();
            }
            String ref = attributes.getValue("ref");
            if(ref != null) {
                ref = ref.trim();
            }
            if (name != null) {
                if (value == null && ref == null) {
                    throw new RuntimeException(new SAXException("Invalid XML: <property> should contain value or ref attribure!"));
                } else if (value != null) {
                    beanDefinition.getValueDependency().put(name, value);
                } else {
                    beanDefinition.getRefDependency().put(name, ref);
                }
            } else {
                throw new RuntimeException(new SAXException("Invalid XML: <property> should contain name attribure!"));
            }
        } else if (!bBean && qName.equalsIgnoreCase("property")) {
            throw new RuntimeException(new SAXException("Invalid XML: <property> should be included into <bean></bean>!"));
        } else if (!qName.equalsIgnoreCase("beans")) {
            throw new RuntimeException(new SAXException("Invalid XML: unknown element <" + qName + ">!"));
        }

    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) {
        if (bBean && qName.equalsIgnoreCase("bean")) {
            beanDefinitions.add(beanDefinition);
            beanDefinition = null;
            bBean = false;
        } else if (qName.equalsIgnoreCase("bean")) {
            throw new RuntimeException(new SAXException("Invalid XML: Closing </bean> happened before <bean> "));
        }
    }

    @Override
    public void characters(char ch[], int start, int length) {
    }

    public List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitions;
    }
}
