package com.mchange.v2.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlProperties
        extends Properties {
    static final String DTD_SYSTEM_ID = "http:";
    static final String DTD_RSRC_PATH = "dtd/xml-properties.dtd";
    DocumentBuilder docBuilder;
    Transformer identityTransformer;

    public XmlProperties() throws ParserConfigurationException, TransformerConfigurationException {
        EntityResolver entityResolver = new EntityResolver() {
            public InputSource resolveEntity(String param1String1, String param1String2) {
                if ("http:") {

                    InputStream inputStream = XmlProperties.class.getResourceAsStream("dtd/xml-properties.dtd");
                    return new InputSource(inputStream);
                }
                return null;
            }
        };

        ErrorHandler errorHandler = new ErrorHandler() {
            public void warning(SAXParseException param1SAXParseException) throws SAXException {
                System.err.println("[Warning] " + param1SAXParseException.toString());
            }

            public void error(SAXParseException param1SAXParseException) throws SAXException {
                System.err.println("[Error] " + param1SAXParseException.toString());
            }

            public void fatalError(SAXParseException param1SAXParseException) throws SAXException {
                System.err.println("[Fatal Error] " + param1SAXParseException.toString());
            }
        };

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);
        documentBuilderFactory.setCoalescing(true);
        documentBuilderFactory.setIgnoringComments(true);
        this.docBuilder = documentBuilderFactory.newDocumentBuilder();
        this.docBuilder.setEntityResolver(entityResolver);
        this.docBuilder.setErrorHandler(errorHandler);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        this.identityTransformer = transformerFactory.newTransformer();
        this.identityTransformer.setOutputProperty("indent", "yes");
        this.identityTransformer.setOutputProperty("doctype-system", "http:");
    }

    public synchronized void loadXml(InputStream paramInputStream) throws IOException, SAXException {
        Document document = this.docBuilder.parse(paramInputStream);
        NodeList nodeList = document.getElementsByTagName("property");
        byte b;
        int i;
        for (b = 0, i = nodeList.getLength(); b < i; b++) {
            extractProperty(nodeList.item(b));
        }
    }

    private void extractProperty(Node paramNode) {
        Element element = (Element) paramNode;
        String str1 = element.getAttribute("name");
        boolean bool = Boolean.valueOf(element.getAttribute("trim")).booleanValue();
        NodeList nodeList = element.getChildNodes();
        int i = nodeList.getLength();

        assert i >= 0 && i <= 1 : "Bad number of children of property element: " + i;

        String str2 = (i == 0) ? "" : ((Text) nodeList.item(0)).getNodeValue();
        if (bool) {
            str2 = str2.trim();
        }
        put(str1, str2);
    }

    public synchronized void saveXml(OutputStream paramOutputStream) throws IOException, TransformerException {
        storeXml(paramOutputStream, (String) null);
    }

    public synchronized void storeXml(OutputStream paramOutputStream, String paramString) throws IOException, TransformerException {
        Document document = this.docBuilder.newDocument();
        if (paramString != null) {

            Comment comment = document.createComment(paramString);
            document.appendChild(comment);
        }

        Element element = document.createElement("xml-properties");

//        for (Iterator<String> iterator = keySet().iterator(); iterator.hasNext(); ) {
//
//            Element element1 = document.createElement("property");
//            String str1 = iterator.next();
//            String str2 = (String) get(str1);
//            element1.setAttribute("name", str1);
//            Text text = document.createTextNode(str2);
//            element1.appendChild(text);
//            element.appendChild(element1);
//        }
        document.appendChild(element);

        this.identityTransformer.transform(new DOMSource(document), new StreamResult(paramOutputStream));
    }

    public static void main(String[] paramArrayOfString) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(paramArrayOfString[0]));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramArrayOfString[1]));
            XmlProperties xmlProperties = new XmlProperties();
            xmlProperties.loadXml(bufferedInputStream);
            xmlProperties.list(System.out);
            xmlProperties.storeXml(bufferedOutputStream, "This is the resaved test document.");
            bufferedOutputStream.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {

            try {
                if (bufferedInputStream != null) bufferedInputStream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            try {
                if (bufferedOutputStream != null) bufferedOutputStream.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
}

