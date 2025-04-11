package javolution.xml.internal.stream;

import javolution.util.FastTable;
import javolution.xml.stream.XMLInputFactory;
import javolution.xml.stream.XMLStreamException;

import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

public final class XMLInputFactoryImpl
        implements XMLInputFactory {
    private Map<String, String> _entities = null;
    private FastTable<XMLStreamReaderImpl> _recycled = (new FastTable()).shared();

    public XMLStreamReaderImpl createXMLStreamReader(InputStream stream) throws XMLStreamException {
        XMLStreamReaderImpl xmlReader = newReader();
        xmlReader.setInput(stream);
        return xmlReader;
    }

    public XMLStreamReaderImpl createXMLStreamReader(InputStream stream, String encoding) throws XMLStreamException {
        XMLStreamReaderImpl xmlReader = newReader();
        xmlReader.setInput(stream, encoding);
        return xmlReader;
    }

    public XMLStreamReaderImpl createXMLStreamReader(Reader reader) throws XMLStreamException {
        XMLStreamReaderImpl xmlReader = newReader();
        xmlReader.setInput(reader);
        return xmlReader;
    }

    public Object getProperty(String name) throws IllegalArgumentException {
        if (name.equals("javolution.xml.stream.isCoalescing"))
            return Boolean.TRUE;
        if (name.equals("javolution.xml.stream.entities")) {
            return this._entities;
        }
        throw new IllegalArgumentException("Property: " + name + " not supported");
    }

    public boolean isPropertySupported(String name) {
        return (name.equals("javolution.xml.stream.isCoalescing") || name.equals("javolution.xml.stream.entities"));
    }

    public void setProperty(String name, Object value) throws IllegalArgumentException {
        if (!name.equals("javolution.xml.stream.isCoalescing")) {
            if (name.equals("javolution.xml.stream.entities")) {
                this._entities = (Map<String, String>) value;
            } else {
                throw new IllegalArgumentException("Property: " + name + " not supported");
            }
        }
    }

    void recycle(XMLStreamReaderImpl reader) {
        this._recycled.addLast(reader);
    }

    private XMLStreamReaderImpl newReader() {
        XMLStreamReaderImpl xmlReader = (XMLStreamReaderImpl) this._recycled.pollLast();
        if (xmlReader == null) xmlReader = new XMLStreamReaderImpl(this);
        if (this._entities != null) {
            xmlReader.setEntities(this._entities);
        }
        return xmlReader;
    }

    public XMLInputFactory clone() {
        try {
            XMLInputFactoryImpl clone = (XMLInputFactoryImpl) super.clone();
            clone._recycled = (new FastTable()).shared();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }
    }
}

