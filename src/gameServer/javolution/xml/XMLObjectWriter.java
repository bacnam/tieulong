package javolution.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamWriter;

public class XMLObjectWriter
{
private final XMLFormat.OutputElement _xml = new XMLFormat.OutputElement();

private Writer _writer;

private OutputStream _outputStream;

public static XMLObjectWriter newInstance(OutputStream out) throws XMLStreamException {
XMLObjectWriter writer = new XMLObjectWriter();
writer.setOutput(out);
return writer;
}

public static XMLObjectWriter newInstance(OutputStream out, String encoding) throws XMLStreamException {
XMLObjectWriter writer = new XMLObjectWriter();
writer.setOutput(out, encoding);
return writer;
}

public static XMLObjectWriter newInstance(Writer out) throws XMLStreamException {
XMLObjectWriter writer = new XMLObjectWriter();
writer.setOutput(out);
return writer;
}

public XMLStreamWriter getStreamWriter() {
return (XMLStreamWriter)this._xml._writer;
}

public XMLObjectWriter setOutput(OutputStream out) throws XMLStreamException {
if (this._outputStream != null || this._writer != null)
throw new IllegalStateException("Writer not closed or reset"); 
this._xml._writer.setOutput(out);
this._outputStream = out;
this._xml._writer.writeStartDocument();
return this;
}

public XMLObjectWriter setOutput(OutputStream out, String encoding) throws XMLStreamException {
if (this._outputStream != null || this._writer != null)
throw new IllegalStateException("Writer not closed or reset"); 
this._xml._writer.setOutput(out, encoding);
this._outputStream = out;
this._xml._writer.writeStartDocument();
return this;
}

public XMLObjectWriter setOutput(Writer out) throws XMLStreamException {
if (this._outputStream != null || this._writer != null)
throw new IllegalStateException("Writer not closed or reset"); 
this._xml._writer.setOutput(out);
this._writer = out;
this._xml._writer.writeStartDocument();
return this;
}

public XMLObjectWriter setBinding(XMLBinding binding) {
this._xml.setBinding(binding);
return this;
}

public XMLObjectWriter setIndentation(String indentation) {
this._xml._writer.setIndentation(indentation);
return this;
}

public XMLObjectWriter setReferenceResolver(XMLReferenceResolver referenceResolver) {
this._xml.setReferenceResolver(referenceResolver);
return this;
}

public void write(Object obj) throws XMLStreamException {
this._xml.add(obj);
}

public void write(Object obj, String name) throws XMLStreamException {
this._xml.add(obj, name);
}

public void write(Object obj, String localName, String uri) throws XMLStreamException {
this._xml.add(obj, localName, uri);
}

public <T> void write(T obj, String name, Class<T> cls) throws XMLStreamException {
this._xml.add(obj, name, cls);
}

public <T> void write(T obj, String localName, String uri, Class<T> cls) throws XMLStreamException {
this._xml.add(obj, localName, uri, cls);
}

public void flush() throws XMLStreamException {
this._xml._writer.flush();
}

public void close() throws XMLStreamException {
try {
if (this._outputStream != null) {
this._xml._writer.writeEndDocument();
this._xml._writer.close();
this._outputStream.close();
reset();
} else if (this._writer != null) {
this._xml._writer.writeEndDocument();
this._xml._writer.close();
this._writer.close();
reset();
}

} catch (IOException e) {
throw new XMLStreamException(e);
} 
}

public void reset() {
this._xml.reset();
this._outputStream = null;
this._writer = null;
}
}

