package javolution.xml.internal.stream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import javolution.io.UTF8StreamWriter;
import javolution.lang.Realtime;
import javolution.text.CharArray;
import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamWriter;

@Realtime
public final class XMLStreamWriterImpl
implements XMLStreamWriter
{
private static final int BUFFER_LENGTH = 2048;
private int _nesting = 0;

private TextBuilder[] _qNames = new TextBuilder[16];

private boolean _isElementOpen;

private boolean _isEmptyElement;

private final char[] _buffer = new char[2048];

private final NamespacesImpl _namespaces = new NamespacesImpl();

private int _index;

private boolean _isRepairingNamespaces;

private String _repairingPrefix = "ns";

private String _indentation;

private String _lineSeparator = "\n";

private int _indentationLevel;

private boolean _automaticEmptyElements;

private boolean _noEmptyElementTag;

private int _autoNSCount;

private boolean _isAttributeValue;

private Writer _writer;

private String _encoding;

private final UTF8StreamWriter _utf8StreamWriter = new UTF8StreamWriter();

private final XMLOutputFactoryImpl _factory;

private final CharArray _noChar;

private final CharArray _tmpCharArray;

private final TextBuilder _autoPrefix;

public XMLStreamWriterImpl() {
this(null);
}

public void setOutput(OutputStream out) throws XMLStreamException {
this._utf8StreamWriter.setOutput(out);
this._encoding = "UTF-8";
setOutput((Writer)this._utf8StreamWriter);
}

public void setOutput(OutputStream out, String encoding) throws XMLStreamException {
if (encoding.equals("UTF-8") || encoding.equals("utf-8") || encoding.equals("ASCII")) {

setOutput(out);
} else {
try {
this._encoding = encoding;
setOutput(new OutputStreamWriter(out, encoding));
} catch (UnsupportedEncodingException e) {
throw new XMLStreamException(e);
} 
} 
}

public void setOutput(Writer writer) throws XMLStreamException {
if (this._writer != null)
throw new IllegalStateException("Writer not closed or reset"); 
this._writer = writer;
}

public void setRepairingNamespaces(boolean isRepairingNamespaces) {
this._isRepairingNamespaces = isRepairingNamespaces;
}

public void setRepairingPrefix(String repairingPrefix) {
this._repairingPrefix = repairingPrefix;
}

public void setIndentation(String indentation) {
this._indentation = indentation;
}

public void setLineSeparator(String lineSeparator) {
this._lineSeparator = lineSeparator;
}

public void setAutomaticEmptyElements(boolean automaticEmptyElements) {
this._automaticEmptyElements = automaticEmptyElements;
}

public void setNoEmptyElementTag(boolean noEmptyElementTag) {
this._noEmptyElementTag = noEmptyElementTag;
}

public void reset() {
this._automaticEmptyElements = false;
this._autoNSCount = 0;
this._encoding = null;
this._indentation = null;
this._indentationLevel = 0;
this._index = 0;
this._isAttributeValue = false;
this._isElementOpen = false;
this._isEmptyElement = false;
this._isRepairingNamespaces = false;
this._namespaces.reset();
this._nesting = 0;
this._noEmptyElementTag = false;
this._repairingPrefix = "ns";
this._utf8StreamWriter.reset();
this._writer = null;

if (this._factory != null) {
this._factory.recycle(this);
}
}

public void writeStartElement(CharSequence localName) throws XMLStreamException {
if (localName == null)
throw new XMLStreamException("Local name cannot be null"); 
writeNewElement(null, localName, null);
}

public void writeStartElement(CharSequence namespaceURI, CharSequence localName) throws XMLStreamException {
if (localName == null)
throw new XMLStreamException("Local name cannot be null"); 
if (namespaceURI == null)
throw new XMLStreamException("Namespace URI cannot be null"); 
writeNewElement(null, localName, namespaceURI);
}

public void writeStartElement(CharSequence prefix, CharSequence localName, CharSequence namespaceURI) throws XMLStreamException {
if (localName == null)
throw new XMLStreamException("Local name cannot be null"); 
if (namespaceURI == null)
throw new XMLStreamException("Namespace URI cannot be null"); 
if (prefix == null)
throw new XMLStreamException("Prefix cannot be null"); 
writeNewElement(prefix, localName, namespaceURI);
}

public void writeEmptyElement(CharSequence localName) throws XMLStreamException {
writeStartElement(localName);
this._isEmptyElement = true;
}

public void writeEmptyElement(CharSequence namespaceURI, CharSequence localName) throws XMLStreamException {
writeStartElement(namespaceURI, localName);
this._isEmptyElement = true;
}

public void writeEmptyElement(CharSequence prefix, CharSequence localName, CharSequence namespaceURI) throws XMLStreamException {
writeStartElement(prefix, localName, namespaceURI);
this._isEmptyElement = true;
}

public void writeEndElement() throws XMLStreamException {
if (this._isElementOpen) {
if (this._isEmptyElement) {
closeOpenTag();
} else {
if (this._automaticEmptyElements) {
this._isEmptyElement = true;
closeOpenTag();
return;
} 
closeOpenTag();
} 
}

if (this._indentation != null && this._indentationLevel != this._nesting - 1) {

writeNoEscape(this._lineSeparator);
for (int i = 1; i < this._nesting; i++) {
writeNoEscape(this._indentation);
}
} 

write('<');
write('/');
writeNoEscape(this._qNames[this._nesting--]);
write('>');
this._namespaces.pop();
}

public void writeEndDocument() throws XMLStreamException {
if (this._isElementOpen)
closeOpenTag(); 
while (this._nesting > 0) {
writeEndElement();
}
flush();
}

public void close() throws XMLStreamException {
if (this._writer != null) {
if (this._nesting != 0) {
writeEndDocument();
}
flush();
} 
reset();
}

public void flush() throws XMLStreamException {
flushBuffer();
try {
this._writer.flush();
} catch (IOException e) {
throw new XMLStreamException(e);
} 
}

public void writeAttribute(CharSequence localName, CharSequence value) throws XMLStreamException {
if (localName == null)
throw new XMLStreamException("Local name cannot be null"); 
if (value == null)
throw new XMLStreamException("Value cannot be null"); 
writeAttributeOrNamespace(null, null, localName, value);
}

public void writeAttribute(CharSequence namespaceURI, CharSequence localName, CharSequence value) throws XMLStreamException {
if (localName == null)
throw new XMLStreamException("Local name cannot be null"); 
if (value == null)
throw new XMLStreamException("Value cannot be null"); 
if (namespaceURI == null)
throw new XMLStreamException("Namespace URI cannot be null"); 
writeAttributeOrNamespace(null, namespaceURI, localName, value);
}

public void writeAttribute(CharSequence prefix, CharSequence namespaceURI, CharSequence localName, CharSequence value) throws XMLStreamException {
if (localName == null)
throw new XMLStreamException("Local name cannot be null"); 
if (value == null)
throw new XMLStreamException("Value cannot be null"); 
if (namespaceURI == null)
throw new XMLStreamException("Namespace URI cannot be null"); 
if (prefix == null)
throw new XMLStreamException("Prefix cannot be null"); 
writeAttributeOrNamespace(prefix, namespaceURI, localName, value);
}

public void writeNamespace(CharSequence prefix, CharSequence namespaceURI) throws XMLStreamException {
CharArray charArray;
if (prefix == null || prefix.length() == 0 || this._namespaces._xmlns.equals(prefix))
{
charArray = this._namespaces._defaultNsPrefix;
}
if (!this._isElementOpen)
throw new IllegalStateException("No open start element"); 
this._namespaces.setPrefix((CharSequence)charArray, (namespaceURI == null) ? (CharSequence)this._namespaces._nullNsURI : namespaceURI, true);
}

public void writeDefaultNamespace(CharSequence namespaceURI) throws XMLStreamException {
writeNamespace((CharSequence)this._namespaces._defaultNsPrefix, namespaceURI);
}

public void writeComment(CharSequence data) throws XMLStreamException {
if (this._isElementOpen)
closeOpenTag(); 
writeNoEscape("<!--");
if (data != null) {
writeNoEscape(data);
}
writeNoEscape("-->");
}

public void writeProcessingInstruction(CharSequence target) throws XMLStreamException {
writeProcessingInstruction(target, (CharSequence)this._noChar);
}

XMLStreamWriterImpl(XMLOutputFactoryImpl factory) { this._noChar = new CharArray("");

this._tmpCharArray = new CharArray();

this._autoPrefix = new TextBuilder(); this._factory = factory; for (int i = 0; i < this._qNames.length;) this._qNames[i++] = new TextBuilder();  } public void writeProcessingInstruction(CharSequence target, CharSequence data) throws XMLStreamException { if (target == null) throw new XMLStreamException("Target cannot be null");  if (data == null) throw new XMLStreamException("Data cannot be null");  if (this._isElementOpen) closeOpenTag();  writeNoEscape("<?"); writeNoEscape(target); write(' '); writeNoEscape(data); writeNoEscape(" ?>"); } public void writeCData(CharSequence data) throws XMLStreamException { if (data == null) throw new XMLStreamException("Data cannot be null");  if (this._isElementOpen) closeOpenTag();  writeNoEscape("<![CDATA["); writeNoEscape(data); writeNoEscape("]]>"); } public void writeDTD(CharSequence dtd) throws XMLStreamException { if (dtd == null) throw new XMLStreamException("DTD cannot be null");  if (this._nesting > 0) throw new XMLStreamException("DOCTYPE declaration (DTD) when not in document root (prolog)");  writeNoEscape(dtd); } public void writeEntityRef(CharSequence name) throws XMLStreamException { write('&'); writeNoEscape(name); write(';'); } public void writeStartDocument() throws XMLStreamException { writeStartDocument(null, null); } public void writeStartDocument(CharSequence version) throws XMLStreamException { writeStartDocument(null, version); }
public void writeStartDocument(CharSequence encoding, CharSequence version) throws XMLStreamException { if (this._nesting > 0) throw new XMLStreamException("Not in document root");  writeNoEscape("<?xml version=\""); if (version != null) { writeNoEscape(version); write('"'); } else { writeNoEscape("1.0\""); }  if (encoding != null) { writeNoEscape(" encoding=\""); writeNoEscape(encoding); write('"'); } else if (this._encoding != null) { writeNoEscape(" encoding=\""); writeNoEscape(this._encoding); write('"'); }  writeNoEscape(" ?>"); }
public void writeCharacters(CharSequence text) throws XMLStreamException { if (this._isElementOpen) closeOpenTag();  if (text == null) return;  writeEscape(text); }
public void writeCharacters(char[] text, int start, int length) throws XMLStreamException { this._tmpCharArray.setArray(text, start, length); writeCharacters((CharSequence)this._tmpCharArray); }
private void resizeElemStack() { int oldLength = this._qNames.length;
int newLength = oldLength * 2;

TextBuilder[] tmp = new TextBuilder[newLength];
System.arraycopy(this._qNames, 0, tmp, 0, oldLength);
this._qNames = tmp;
for (int i = oldLength; i < newLength; i++)
this._qNames[i] = new TextBuilder();  }
public CharSequence getPrefix(CharSequence uri) throws XMLStreamException { return (CharSequence)this._namespaces.getPrefix(uri); }
public void setPrefix(CharSequence prefix, CharSequence uri) throws XMLStreamException { this._namespaces.setPrefix(prefix, (uri == null) ? (CharSequence)this._namespaces._nullNsURI : uri, false); }
public void setDefaultNamespace(CharSequence uri) throws XMLStreamException { setPrefix((CharSequence)this._namespaces._defaultNsPrefix, uri); }
public Object getProperty(String name) throws IllegalArgumentException { if (name.equals("javolution.xml.stream.isRepairingNamespaces")) return new Boolean(this._isRepairingNamespaces);  if (name.equals("javolution.xml.stream.repairingPrefix")) return this._repairingPrefix;  if (name.equals("javolution.xml.stream.automaticEmptyElements"))
return new Boolean(this._automaticEmptyElements);  if (name.equals("javolution.xml.stream.noEmptyElementTag"))
return new Boolean(this._noEmptyElementTag);  if (name.equals("javolution.xml.stream.indentation"))
return this._indentation;  if (name.equals("javolution.xml.stream.lineSeparator"))
return this._lineSeparator;  throw new IllegalArgumentException("Property: " + name + " not supported"); } private final void writeNoEscape(String str) throws XMLStreamException { write(str, 0, str.length(), false); }
private void writeNewElement(CharSequence prefix, CharSequence localName, CharSequence namespaceURI) throws XMLStreamException { if (this._isElementOpen) closeOpenTag();  if (this._indentation != null) { writeNoEscape(this._lineSeparator); this._indentationLevel = this._nesting; for (int i = 0; i < this._indentationLevel; i++) writeNoEscape(this._indentation);  }  write('<'); this._isElementOpen = true; if (++this._nesting >= this._qNames.length) resizeElemStack();  this._namespaces.push(); TextBuilder qName = this._qNames[this._nesting].clear(); if (namespaceURI != null && !this._namespaces._defaultNamespace.equals(namespaceURI)) { if (this._isRepairingNamespaces) { prefix = getRepairedPrefix(prefix, namespaceURI); } else if (prefix == null) { prefix = getPrefix(namespaceURI); if (prefix == null) throw new XMLStreamException("URI: " + namespaceURI + " not bound and repairing namespaces disabled");  }  if (prefix.length() > 0) { qName.append(prefix); qName.append(':'); }  }  qName.append(localName); writeNoEscape(qName); }
private void writeAttributeOrNamespace(CharSequence prefix, CharSequence namespaceURI, CharSequence localName, CharSequence value) throws XMLStreamException { if (!this._isElementOpen) throw new IllegalStateException("No open start element");  write(' '); if (namespaceURI != null && !this._namespaces._defaultNamespace.equals(namespaceURI)) { if (this._isRepairingNamespaces) { prefix = getRepairedPrefix(prefix, namespaceURI); } else if (prefix == null) { prefix = getPrefix(namespaceURI); if (prefix == null) throw new XMLStreamException("URI: " + namespaceURI + " not bound and repairing namespaces disabled");  }  if (prefix.length() > 0) { writeNoEscape(prefix); write(':'); }  }  writeNoEscape(localName); write('='); write('"'); this._isAttributeValue = true; writeEscape(value); this._isAttributeValue = false; write('"'); }
private void closeOpenTag() throws XMLStreamException { writeNamespaces(); this._isElementOpen = false; if (this._isEmptyElement) { if (this._noEmptyElementTag) { write('<'); write('/'); writeNoEscape(this._qNames[this._nesting]); write('>'); } else { write('/'); write('>'); }  this._nesting--; this._namespaces.pop(); this._isEmptyElement = false; } else { write('>'); }  }
private void writeNamespaces() throws XMLStreamException { int i0 = (this._nesting > 1) ? this._namespaces._namespacesCount[this._nesting - 2] : 3; int i1 = this._namespaces._namespacesCount[this._nesting - 1]; int i2 = this._namespaces._namespacesCount[this._nesting]; for (int i = i0; i < i2; i++) { if ((this._isRepairingNamespaces && i < i1 && !this._namespaces._prefixesWritten[i]) || (i >= i1 && this._namespaces._prefixesWritten[i])) { if (this._isRepairingNamespaces) { CharArray prefix = this._namespaces.getPrefix((CharSequence)this._namespaces._namespaces[i], i); if (this._namespaces._prefixes[i].equals(prefix)) continue;  }  if (this._namespaces._prefixes[i].length() == 0) { writeAttributeOrNamespace(null, null, (CharSequence)this._namespaces._xmlns, (CharSequence)this._namespaces._namespaces[i]); } else { writeAttributeOrNamespace((CharSequence)this._namespaces._xmlns, (CharSequence)this._namespaces._xmlnsURI, (CharSequence)this._namespaces._prefixes[i], (CharSequence)this._namespaces._namespaces[i]); }  }  continue; }  } private CharSequence getRepairedPrefix(CharSequence prefix, CharSequence namespaceURI) throws XMLStreamException { TextBuilder textBuilder; CharArray prefixForURI = this._namespaces.getPrefix(namespaceURI); if (prefixForURI != null && (prefix == null || prefixForURI.equals(prefix))) return (CharSequence)prefixForURI;  if (prefix == null || prefix.length() == 0) textBuilder = this._autoPrefix.clear().append(this._repairingPrefix).append(this._autoNSCount++);  this._namespaces.setPrefix((CharSequence)textBuilder, namespaceURI, true); return (CharSequence)textBuilder; } private final void writeNoEscape(TextBuilder tb) throws XMLStreamException { write(tb, 0, tb.length(), false); }

private final void writeNoEscape(CharSequence csq) throws XMLStreamException {
write(csq, 0, csq.length(), false);
}

private final void writeEscape(CharSequence csq) throws XMLStreamException {
write(csq, 0, csq.length(), true);
}

private final void write(Object csq, int start, int length, boolean escapeMarkup) throws XMLStreamException {
if (this._index + length <= 2048) {
if (csq instanceof String) {
((String)csq).getChars(start, start + length, this._buffer, this._index);
} else if (csq instanceof Text) {
((Text)csq).getChars(start, start + length, this._buffer, this._index);
}
else if (csq instanceof TextBuilder) {
((TextBuilder)csq).getChars(start, start + length, this._buffer, this._index);
}
else if (csq instanceof CharArray) {
((CharArray)csq).getChars(start, start + length, this._buffer, this._index);
} else {

getChars((CharSequence)csq, start, start + length, this._buffer, this._index);
} 

if (escapeMarkup) {
int end = this._index + length;
for (int i = this._index; i < end; ) {
char c = this._buffer[i];
if (c >= '?' || !isEscaped(c)) {
i++; continue;
} 
this._index = i;
flushBuffer();
writeDirectEscapedCharacters(this._buffer, i, end);
return;
} 
} 
this._index += length;

}
else if (length <= 2048) {
flushBuffer();
write(csq, start, length, escapeMarkup);
} else {
int half = length >> 1;
write(csq, start, half, escapeMarkup);
write(csq, start + half, length - half, escapeMarkup);
} 
}

private static void getChars(CharSequence csq, int start, int end, char[] dest, int destPos) {
for (int i = start, j = destPos; i < end;) {
dest[j++] = csq.charAt(i++);
}
}

private final void writeDirectEscapedCharacters(char[] chars, int start, int end) throws XMLStreamException {
try {
int blockStart = start;
for (int i = start; i < end; ) {
char c = chars[i++];
if (c >= '?' || !isEscaped(c)) {
continue;
}
int j = i - blockStart - 1;
if (j > 0) {
this._writer.write(this._buffer, blockStart, j);
}
blockStart = i;
switch (c) {
case '<':
this._writer.write("&lt;");
continue;
case '>':
this._writer.write("&gt;");
continue;
case '\'':
this._writer.write("&apos;");
continue;
case '"':
this._writer.write("&quot;");
continue;
case '&':
this._writer.write("&amp;");
continue;
} 
this._writer.write("&#");
this._writer.write((char)(48 + c / 10));
this._writer.write((char)(48 + c % 10));
this._writer.write(59);
} 

int blockLength = end - blockStart;
if (blockLength > 0) {
this._writer.write(this._buffer, blockStart, blockLength);
}
} catch (IOException e) {
throw new XMLStreamException(e);
} 
}

private boolean isEscaped(char c) {
return ((c < ' ' && this._isAttributeValue) || (c == '"' && this._isAttributeValue) || c == '<' || c == '>' || c == '&');
}

private final void write(char c) throws XMLStreamException {
if (this._index == 2048) {
flushBuffer();
}
this._buffer[this._index++] = c;
}

private void flushBuffer() throws XMLStreamException {
try {
this._writer.write(this._buffer, 0, this._index);
} catch (IOException e) {
throw new XMLStreamException(e);
} finally {
this._index = 0;
} 
}
}

