package javolution.xml.sax;

import org.xml.sax.*;

import java.io.IOException;

public interface XMLReader {
    boolean getFeature(String paramString) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setFeature(String paramString, boolean paramBoolean) throws SAXNotRecognizedException, SAXNotSupportedException;

    Object getProperty(String paramString) throws SAXNotRecognizedException, SAXNotSupportedException;

    void setProperty(String paramString, Object paramObject) throws SAXNotRecognizedException, SAXNotSupportedException;

    EntityResolver getEntityResolver();

    void setEntityResolver(EntityResolver paramEntityResolver);

    DTDHandler getDTDHandler();

    void setDTDHandler(DTDHandler paramDTDHandler);

    ContentHandler getContentHandler();

    void setContentHandler(ContentHandler paramContentHandler);

    ErrorHandler getErrorHandler();

    void setErrorHandler(ErrorHandler paramErrorHandler);

    void parse(InputSource paramInputSource) throws IOException, SAXException;

    void parse(String paramString) throws IOException, SAXException;
}

