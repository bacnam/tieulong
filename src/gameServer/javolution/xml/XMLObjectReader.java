package javolution.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamReader;

public class XMLObjectReader
{
private final XMLFormat.InputElement _xml = new XMLFormat.InputElement();

private Reader _reader;

private InputStream _inputStream;

public static XMLObjectReader newInstance(InputStream in) throws XMLStreamException {
XMLObjectReader reader = new XMLObjectReader();
reader.setInput(in);
return reader;
}

public static XMLObjectReader newInstance(InputStream in, String encoding) throws XMLStreamException {
XMLObjectReader reader = new XMLObjectReader();
reader.setInput(in, encoding);
return reader;
}

public static XMLObjectReader newInstance(Reader in) throws XMLStreamException {
XMLObjectReader reader = new XMLObjectReader();
reader.setInput(in);
return reader;
}

public XMLStreamReader getStreamReader() {
return (XMLStreamReader)this._xml._reader;
}

public XMLObjectReader setInput(InputStream in) throws XMLStreamException {
if (this._inputStream != null || this._reader != null)
throw new IllegalStateException("Reader not closed or reset"); 
this._xml._reader.setInput(in);
this._inputStream = in;
return this;
}

public XMLObjectReader setInput(InputStream in, String encoding) throws XMLStreamException {
if (this._inputStream != null || this._reader != null)
throw new IllegalStateException("Reader not closed or reset"); 
this._xml._reader.setInput(in, encoding);
this._inputStream = in;
return this;
}

public XMLObjectReader setInput(Reader in) throws XMLStreamException {
if (this._inputStream != null || this._reader != null)
throw new IllegalStateException("Reader not closed or reset"); 
this._xml._reader.setInput(in);
this._reader = in;
return this;
}

public XMLObjectReader setBinding(XMLBinding binding) {
this._xml.setBinding(binding);
return this;
}

public XMLObjectReader setReferenceResolver(XMLReferenceResolver referenceResolver) {
this._xml.setReferenceResolver(referenceResolver);
return this;
}

public boolean hasNext() throws XMLStreamException {
return this._xml.hasNext();
}

public <T> T read() throws XMLStreamException {
return this._xml.getNext();
}

public <T> T read(String name) throws XMLStreamException {
return this._xml.get(name);
}

public <T> T read(String localName, String uri) throws XMLStreamException {
return this._xml.get(localName, uri);
}

public <T> T read(String name, Class<T> cls) throws XMLStreamException {
return this._xml.get(name, cls);
}

public <T> T read(String localName, String uri, Class<T> cls) throws XMLStreamException {
return this._xml.get(localName, uri, cls);
}

public void close() throws XMLStreamException {
try {
if (this._inputStream != null) {
this._inputStream.close();
reset();
} else if (this._reader != null) {
this._reader.close();
reset();
} 
} catch (IOException e) {
throw new XMLStreamException(e);
} 
}

public void reset() {
this._xml.reset();
this._reader = null;
this._inputStream = null;
}
}

