package com.example.parsing_lab5;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Parser {

    public static List<String> parseXML(String xmlString) {
        List<String> currencyRates = new ArrayList<>();

        try {
            // Преобразуем строку в InputStream
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));

            // Парсим XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            NodeList items = document.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Element item = (Element) items.item(i);

                String currencyCode = item.getElementsByTagName("targetCurrency").item(0).getTextContent();
                String rate = item.getElementsByTagName("exchangeRate").item(0).getTextContent();

                currencyRates.add(currencyCode + " - " + rate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return currencyRates;
    }
}
