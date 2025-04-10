package javolution.xml.internal.stream;

import java.io.OutputStream;
import java.io.Writer;
import javolution.util.FastTable;
import javolution.xml.stream.XMLOutputFactory;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamWriter;

public final class XMLOutputFactoryImpl
implements XMLOutputFactory
{
private Boolean _automaticEmptyElements = Boolean.FALSE;

private String _indentation;

private Boolean _isRepairingNamespaces = Boolean.FALSE;

private String _lineSeparator = "\n";

private Boolean _noEmptyElementTag = Boolean.FALSE;

private String _repairingPrefix = "ns";

private FastTable<XMLStreamWriterImpl> _recycled = (new FastTable()).shared();

public XMLStreamWriterImpl createXMLStreamWriter(OutputStream stream) throws XMLStreamException {
XMLStreamWriterImpl xmlWriter = newWriter();
xmlWriter.setOutput(stream);
return xmlWriter;
}

public XMLStreamWriterImpl createXMLStreamWriter(OutputStream stream, String encoding) throws XMLStreamException {
if (encoding == null || encoding.equals("UTF-8") || encoding.equals("utf-8"))
{
return createXMLStreamWriter(stream); } 
XMLStreamWriterImpl xmlWriter = newWriter();
xmlWriter.setOutput(stream, encoding);
return xmlWriter;
}

public XMLStreamWriterImpl createXMLStreamWriter(Writer writer) throws XMLStreamException {
XMLStreamWriterImpl xmlWriter = newWriter();
xmlWriter.setOutput(writer);
return xmlWriter;
}

public Object getProperty(String name) throws IllegalArgumentException {
if (name.equals("javolution.xml.stream.isRepairingNamespaces"))
return this._isRepairingNamespaces; 
if (name.equals("javolution.xml.stream.repairingPrefix"))
return this._repairingPrefix; 
if (name.equals("javolution.xml.stream.automaticEmptyElements"))
return this._automaticEmptyElements; 
if (name.equals("javolution.xml.stream.noEmptyElementTag"))
return this._noEmptyElementTag; 
if (name.equals("javolution.xml.stream.indentation"))
return this._indentation; 
if (name.equals("javolution.xml.stream.lineSeparator")) {
return this._lineSeparator;
}
throw new IllegalArgumentException("Property: " + name + " not supported");
}

public boolean isPropertySupported(String name) {
return (name.equals("javolution.xml.stream.isRepairingNamespaces") || name.equals("javolution.xml.stream.repairingPrefix") || name.equals("javolution.xml.stream.automaticEmptyElements") || name.equals("javolution.xml.stream.noEmptyElementTag") || name.equals("javolution.xml.stream.indentation") || name.equals("javolution.xml.stream.lineSeparator"));
}

public void setProperty(String name, Object value) throws IllegalArgumentException {
if (name.equals("javolution.xml.stream.isRepairingNamespaces")) {
this._isRepairingNamespaces = (Boolean)value;
} else if (name.equals("javolution.xml.stream.repairingPrefix")) {
this._repairingPrefix = (String)value;
} else if (name.equals("javolution.xml.stream.automaticEmptyElements")) {
this._automaticEmptyElements = (Boolean)value;
} else if (name.equals("javolution.xml.stream.noEmptyElementTag")) {
this._noEmptyElementTag = (Boolean)value;
} else if (name.equals("javolution.xml.stream.indentation")) {
this._indentation = (String)value;
} else if (name.equals("javolution.xml.stream.lineSeparator")) {
this._lineSeparator = (String)value;
} else {
throw new IllegalArgumentException("Property: " + name + " not supported");
} 
}

void recycle(XMLStreamWriterImpl xmlWriter) {
this._recycled.addLast(xmlWriter);
}

private XMLStreamWriterImpl newWriter() {
XMLStreamWriterImpl xmlWriter = (XMLStreamWriterImpl)this._recycled.pollLast();
if (xmlWriter == null) xmlWriter = new XMLStreamWriterImpl(this); 
xmlWriter.setRepairingNamespaces(this._isRepairingNamespaces.booleanValue());
xmlWriter.setRepairingPrefix(this._repairingPrefix);
xmlWriter.setIndentation(this._indentation);
xmlWriter.setLineSeparator(this._lineSeparator);
xmlWriter.setAutomaticEmptyElements(this._automaticEmptyElements.booleanValue());

xmlWriter.setNoEmptyElementTag(this._noEmptyElementTag.booleanValue());
return xmlWriter;
}

public XMLOutputFactory clone() {
try {
XMLOutputFactoryImpl clone = (XMLOutputFactoryImpl)super.clone();
clone._recycled = (new FastTable()).shared();
return clone;
} catch (CloneNotSupportedException e) {
throw new Error();
} 
}
}

