package com.mchange.v1.xmlprops;

import com.mchange.v1.xml.StdErrErrorHandler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SaxXmlPropsParser {
    static final String DEFAULT_XML_READER = "org.apache.xerces.parsers.SAXParser";
    static final String XMLPROPS_NAMESPACE_URI = "http:";

    public static Properties parseXmlProps(InputStream paramInputStream) throws XmlPropsException {
        try {
            String str = "org.apache.xerces.parsers.SAXParser";
            XMLReader xMLReader = (XMLReader) Class.forName(str).newInstance();
            InputSource inputSource = new InputSource(paramInputStream);
            return parseXmlProps(inputSource, xMLReader, null, null);
        } catch (XmlPropsException xmlPropsException) {
            throw xmlPropsException;
        } catch (Exception exception) {

            exception.printStackTrace();
            throw new XmlPropsException("Exception while instantiating XMLReader.", exception);
        }
    }

    private static Properties parseXmlProps(InputSource paramInputSource, XMLReader paramXMLReader, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler) throws XmlPropsException {
        try {
            StdErrErrorHandler stdErrErrorHandler;
            if (paramEntityResolver != null)
                paramXMLReader.setEntityResolver(paramEntityResolver);
            if (paramErrorHandler == null)
                stdErrErrorHandler = new StdErrErrorHandler();
            paramXMLReader.setErrorHandler((ErrorHandler) stdErrErrorHandler);
            XmlPropsContentHandler xmlPropsContentHandler = new XmlPropsContentHandler();
            paramXMLReader.setContentHandler(xmlPropsContentHandler);
            paramXMLReader.parse(paramInputSource);
            return xmlPropsContentHandler.getLastProperties();
        } catch (Exception exception) {

            if (exception instanceof SAXException) {
                ((SAXException) exception).getException().printStackTrace();
            }
            exception.printStackTrace();
            throw new XmlPropsException(exception);
        }
    }

    static class XmlPropsContentHandler
            implements ContentHandler {
        Locator locator;

        Properties props;
        String name;
        StringBuffer valueBuf;

        public void setDocumentLocator(Locator param1Locator) {
            this.locator = param1Locator;
        }

        public void startDocument() throws SAXException {
            this.props = new Properties();
        }

        public void startElement(String param1String1, String param1String2, String param1String3, Attributes param1Attributes) {
            System.err.println("--> startElement( " + param1String1 + ", " + param1String2 + ", " + param1Attributes + ")");
            if (!param1String1.equals("") && !param1String1.equals("http:")) {
                return;
            }

            if (param1String2.equals("property")) {

                this.name = param1Attributes.getValue(param1String1, "name");
                this.valueBuf = new StringBuffer();
            }
        }

        public void characters(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
            if (this.valueBuf != null) {
                this.valueBuf.append(param1ArrayOfchar, param1Int1, param1Int2);
            }
        }

        public void ignorableWhitespace(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
            if (this.valueBuf != null) {
                this.valueBuf.append(param1ArrayOfchar, param1Int1, param1Int2);
            }
        }

        public void endElement(String param1String1, String param1String2, String param1String3) throws SAXException {
            if (!param1String1.equals("") && !param1String1.equals("http:")) {
                return;
            }
            if (param1String2.equals("property")) {

                System.err.println("NAME: " + this.name);
                this.props.put(this.name, this.valueBuf.toString());
                this.valueBuf = null;
            }
        }

        public void endDocument() throws SAXException {
        }

        public void startPrefixMapping(String param1String1, String param1String2) throws SAXException {
        }

        public void endPrefixMapping(String param1String) throws SAXException {
        }

        public void processingInstruction(String param1String1, String param1String2) throws SAXException {
        }

        public void skippedEntity(String param1String) throws SAXException {
        }

        public Properties getLastProperties() {
            return this.props;
        }
    }

    public static void main(String[] paramArrayOfString) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramArrayOfString[0]));
            SaxXmlPropsParser saxXmlPropsParser = new SaxXmlPropsParser();
            Properties properties = parseXmlProps(bufferedInputStream);
            for (String str1 : properties.keySet()) {

                String str2 = properties.getProperty(str1);
                System.err.println(str1 + '=' + str2);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

