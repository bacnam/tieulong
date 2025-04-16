package javolution.xml.sax;

import javolution.text.CharArray;
import javolution.xml.internal.stream.XMLStreamReaderImpl;
import javolution.xml.stream.XMLStreamException;
import org.xml.sax.*;

import java.io.*;
import java.net.URL;

public class XMLReaderImpl implements XMLReader {

    private static final CharArray NO_CHAR = new CharArray("");
    private static final DefaultHandler DEFAULT_HANDLER = new DefaultHandler();

    private final XMLStreamReaderImpl _xmlReader = new XMLStreamReaderImpl();
    private ContentHandler _contentHandler = DEFAULT_HANDLER;
    private ErrorHandler _errorHandler = DEFAULT_HANDLER;
    private EntityResolver _entityResolver;
    private DTDHandler _dtdHandler;

    public XMLReaderImpl() {}

    public Object getProperty(String name) throws SAXNotRecognizedException {
        throw new SAXNotRecognizedException("Property " + name + " not recognized");
    }

    public void setProperty(String name, Object value) throws SAXNotRecognizedException {
        throw new SAXNotRecognizedException("Property " + name + " not recognized");
    }

    public EntityResolver getEntityResolver() {
        return _entityResolver;
    }

    public void setEntityResolver(EntityResolver resolver) {
        this._entityResolver = resolver;
    }

    public DTDHandler getDTDHandler() {
        return _dtdHandler;
    }

    public void setDTDHandler(DTDHandler handler) {
        this._dtdHandler = handler;
    }

    public ContentHandler getContentHandler() {
        return (_contentHandler == DEFAULT_HANDLER) ? null : _contentHandler;
    }

    public void setContentHandler(ContentHandler handler) {
        if (handler == null) throw new NullPointerException();
        this._contentHandler = handler;
    }

    public ErrorHandler getErrorHandler() {
        return (_errorHandler == DEFAULT_HANDLER) ? null : _errorHandler;
    }

    public void setErrorHandler(ErrorHandler handler) {
        if (handler == null) throw new NullPointerException();
        this._errorHandler = handler;
    }

    public boolean getFeature(String name) throws SAXNotRecognizedException {
        if ("http://xml.org/sax/features/namespaces".equals(name)) return true;
        if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) return true;
        throw new SAXNotRecognizedException("Feature " + name + " not recognized");
    }

    public void setFeature(String name, boolean value) throws SAXNotRecognizedException {
        if ("http://xml.org/sax/features/namespaces".equals(name) ||
                "http://xml.org/sax/features/namespace-prefixes".equals(name)) {
            // No-op, always true
            return;
        }
        throw new SAXNotRecognizedException("Feature " + name + " not recognized");
    }

    public void reset() {
        setContentHandler(DEFAULT_HANDLER);
        setErrorHandler(DEFAULT_HANDLER);
        this._xmlReader.reset();
    }

    private void parseAll() throws XMLStreamException, SAXException {
        int eventType = this._xmlReader.getEventType();
        if (eventType != 7) throw new SAXException("Currently parsing");

        _contentHandler.startDocument();
        boolean doContinue = true;

        while (doContinue) {
            switch (_xmlReader.next()) {
                case 1: // START_ELEMENT
                    for (int i = 0; i < _xmlReader.getNamespaceCount(); i++) {
                        CharArray prefix = _xmlReader.getNamespacePrefix(i);
                        prefix = (prefix == null) ? NO_CHAR : prefix;
                        _contentHandler.startPrefixMapping(prefix.toString(), _xmlReader.getNamespaceURI(i).toString());
                    }
                    _contentHandler.startElement(
                            getSafe(_xmlReader.getNamespaceURI()),
                            getSafe(_xmlReader.getLocalName()),
                            getSafe(_xmlReader.getQName()),
                            _xmlReader.getAttributes());
                    break;
                case 2: // END_ELEMENT
                    _contentHandler.endElement(
                            getSafe(_xmlReader.getNamespaceURI()),
                            getSafe(_xmlReader.getLocalName()),
                            getSafe(_xmlReader.getQName()));
                    for (int i = 0; i < _xmlReader.getNamespaceCount(); i++) {
                        CharArray prefix = _xmlReader.getNamespacePrefix(i);
                        prefix = (prefix == null) ? NO_CHAR : prefix;
                        _contentHandler.endPrefixMapping(prefix.toString());
                    }
                    break;
                case 4: // CHARACTERS
                case 12: // CDATA
                    CharArray text = _xmlReader.getText();
                    _contentHandler.characters(text.array(), text.offset(), text.length());
                    break;
                case 6: // WHITESPACE
                    CharArray ws = _xmlReader.getText();
                    _contentHandler.ignorableWhitespace(ws.array(), ws.offset(), ws.length());
                    break;
                case 3: // PROCESSING_INSTRUCTION
                    _contentHandler.processingInstruction(_xmlReader.getPITarget(), _xmlReader.getPIData());
                    break;
                case 8: // END_DOCUMENT
                    doContinue = false;
                    _xmlReader.close();
                    break;
            }
        }

        _contentHandler.endDocument();
    }

    private String getSafe(CharArray c) {
        return (c == null) ? "" : c.toString();
    }

    public void parse(InputSource input) throws IOException, SAXException {
        Reader reader = input.getCharacterStream();
        if (reader != null) {
            parse(reader);
        } else {
            InputStream in = input.getByteStream();
            if (in != null) {
                parse(in, input.getEncoding());
            } else {
                parse(input.getSystemId());
            }
        }
    }

    public void parse(String systemId) throws IOException, SAXException {
        try (InputStream in = new URL(systemId).openStream()) {
            parse(in);
        } catch (Exception e) {
            try (InputStream fallback = new FileInputStream(systemId)) {
                parse(fallback);
            } catch (Exception e2) {
                throw new UnsupportedOperationException("Cannot parse " + systemId);
            }
        }
    }

    public void parse(InputStream in) throws IOException, SAXException {
        try {
            _xmlReader.setInput(in);
            parseAll();
        } catch (XMLStreamException e) {
            throw new SAXException(e.getMessage(), e);
        } finally {
            _xmlReader.reset();
        }
    }

    public void parse(InputStream in, String encoding) throws IOException, SAXException {
        try {
            _xmlReader.setInput(in, encoding);
            parseAll();
        } catch (XMLStreamException e) {
            throw new SAXException(e.getMessage(), e);
        } finally {
            _xmlReader.reset();
        }
    }

    public void parse(Reader reader) throws IOException, SAXException {
        try {
            _xmlReader.setInput(reader);
            parseAll();
        } catch (XMLStreamException e) {
            throw new SAXException(e.getMessage(), e);
        } finally {
            _xmlReader.reset();
        }
    }
}
