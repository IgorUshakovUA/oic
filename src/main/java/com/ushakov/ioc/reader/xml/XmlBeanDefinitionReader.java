package com.ushakov.ioc.reader.xml;

import com.ushakov.ioc.entity.BeanDefinition;
import com.ushakov.ioc.reader.BeanDefinitionReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanDefinitionReader implements BeanDefinitionReader {
    private InputStream inputStream;

    public XmlBeanDefinitionReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public XmlBeanDefinitionReader(String path) {
        Class clazz = this.getClass();
        inputStream = clazz.getClassLoader().getResourceAsStream(path);
    }

    public List<BeanDefinition> getBeanDefinitions() {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XmlHandler xmlHandler = new XmlHandler();
            saxParser.parse(inputStream, xmlHandler);
            return xmlHandler.getBeanDefinitions();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            new RuntimeException(e);
        }
        return null;
    }
}
