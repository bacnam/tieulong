package javolution.xml.sax;

import javolution.text.CharArray;
import javolution.xml.internal.stream.XMLStreamReaderImpl;
import javolution.xml.stream.XMLStreamException;
import org.xml.sax.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

private static final CharArray NO_CHAR = new CharArray("");

public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
    throw new SAXNotRecognizedException("Property " + name + " not recognized");
}

public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
    throw new SAXNotRecognizedException("Property " + name + " not recognized");
}

public EntityResolver getEntityResolver() {
    return this._entityResolver;
}

public void setEntityResolver(EntityResolver resolver) {
    this._entityResolver = resolver;
}

public DTDHandler getDTDHandler() {
    return this._dtdHandler;
}

public void setDTDHandler(DTDHandler handler) {
    this._dtdHandler = handler;
}

public void reset() {
    setContentHandler(DEFAULT_HANDLER);
    setErrorHandler(DEFAULT_HANDLER);
    this._xmlReader.reset();
}

private void parseAll() throws XMLStreamException, SAXException {
    int eventType = this._xmlReader.getEventType();
    if (eventType != 7)
        throw new SAXException("Currently parsing");
    this._contentHandler.startDocument();

    boolean doContinue = true;
    while (doContinue) {
        CharArray uri;
        CharArray localName;
        CharArray qName;
        CharArray text;
        int i;
        int count;
        switch (this._xmlReader.next()) {

            case 1:
                for (i = 0, count = this._xmlReader.getNamespaceCount(); i < count; i++) {
                    CharArray prefix = this._xmlReader.getNamespacePrefix(i);
                    prefix = (prefix == null) ? NO_CHAR : prefix;
                    CharArray charArray1 = this._xmlReader.getNamespaceURI(i);
                    this._contentHandler.startPrefixMapping(prefix, charArray1);
                }

                uri = this._xmlReader.getNamespaceURI();
                uri = (uri == null) ? NO_CHAR : uri;
                localName = this._xmlReader.getLocalName();
                qName = this._xmlReader.getQName();
                this._contentHandler.startElement(uri, localName, qName, this._xmlReader.getAttributes());

            case 2:
                uri = this._xmlReader.getNamespaceURI();
                uri = (uri == null) ? NO_CHAR : uri;
                localName = this._xmlReader.getLocalName();
                qName = this._xmlReader.getQName();
                this._contentHandler.endElement(uri, localName, qName);

                for (i = 0, count = this._xmlReader.getNamespaceCount(); i < count; i++) {
                    CharArray prefix = this._xmlReader.getNamespacePrefix(i);
                    prefix = (prefix == null) ? NO_CHAR : prefix;
                    this._contentHandler.endPrefixMapping(prefix);
                }

            case 4:
            case 12:
                text = this._xmlReader.getText();
                this._contentHandler.characters(text.array(), text.offset(), text.length());

            case 6:
                text = this._xmlReader.getText();
                this._contentHandler.ignorableWhitespace(text.array(), text.offset(), text.length());

            case 3:
                this._contentHandler.processingInstruction(this._xmlReader.getPITarget(), this._xmlReader.getPIData());

            case 8:
                doContinue = false;
                this._xmlReader.close();
        }
    }
}

public class XMLReaderImpl
        implements XMLReader {
    private static DefaultHandler DEFAULT_HANDLER = new DefaultHandler();
    private final XMLStreamReaderImpl _xmlReader = new XMLStreamReaderImpl();
    private ContentHandler _contentHandler;
    private ErrorHandler _errorHandler;
    private EntityResolver _entityResolver;

    private DTDHandler _dtdHandler;

    public XMLReaderImpl() {
        setContentHandler(DEFAULT_HANDLER);
        setErrorHandler(DEFAULT_HANDLER);
    }

    SAXNotRecognizedException("Feature "+name +" not recognized");

    public void parse(InputStream in) throws IOException, SAXException {
        try {
            this._xmlReader.setInput(in);
            parseAll();
        } catch (XMLStreamException e) {
            if (e.getNestedException() instanceof IOException)
                throw (IOException) e.getNestedException();
            throw new SAXException(e.getMessage());
        } finally {
            this._xmlReader.reset();
        }
    }

    public void parse(InputStream in, String encoding) throws IOException, SAXException {
        try {
            this._xmlReader.setInput(in, encoding);
            parseAll();
        } catch (XMLStreamException e) {
            if (e.getNestedException() instanceof IOException)
                throw (IOException) e.getNestedException();
            throw new SAXException(e.getMessage());
        } finally {
            this._xmlReader.reset();
        }
    }

    public void parse(Reader reader) throws IOException, SAXException {
        try {
            this._xmlReader.setInput(reader);
            parseAll();
        } catch (XMLStreamException e) {
            if (e.getNestedException() instanceof IOException)
                throw (IOException) e.getNestedException();
            throw new SAXException(e.getMessage());
        } finally {
            this._xmlReader.reset();
        }
    }

    public void parse(InputSource input) throws IOException, SAXException {
        Reader reader = input.getCharacterStream();
        if (reader != null) {
            parse(reader);
        } else {
            InputStream inStream = input.getByteStream();
            if (inStream != null) {
                parse(inStream, input.getEncoding());
            } else {
                parse(input.getSystemId());
            }
        }
    }

    public void parse(String systemId) throws IOException, SAXException {
        InputStream inputStream;
        try {
            URL url = new URL(systemId);
            inputStream = url.openStream();
        } catch (Exception urlException) {
            try {
                inputStream = new FileInputStream(systemId);
            } catch (Exception fileException) {
                throw new UnsupportedOperationException("Cannot parse " + systemId);
            }
        }

        parse(inputStream);
    }

    public ContentHandler getContentHandler() {
        return (this._contentHandler == DEFAULT_HANDLER) ? null : this._contentHandler;
    }

    public void setContentHandler(ContentHandler handler) {
        if (handler != null) {
            this._contentHandler = handler;
        } else {
            throw new NullPointerException();
        }
    }

    public ErrorHandler getErrorHandler() {
        return (this._errorHandler == DEFAULT_HANDLER) ? null : this._errorHandler;
    }

    public void setErrorHandler(ErrorHandler handler) {
        if (handler != null) {
            this._errorHandler = handler;
        } else {
            throw new NullPointerException();
        }
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http:
        return true;
        if (name.equals("http:
        {
            return true;
        }
        throw new SAXNotRecognizedException("Feature " + name + " not recognized");
    }

throw new

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if (name.equals("http:
        return;
    }
}
}

