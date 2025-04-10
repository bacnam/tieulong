package javolution.xml;

import javolution.text.CharArray;
import javolution.text.TextBuilder;
import javolution.text.TextContext;
import javolution.text.TextFormat;
import javolution.xml.internal.stream.XMLStreamReaderImpl;
import javolution.xml.internal.stream.XMLStreamWriterImpl;
import javolution.xml.sax.Attributes;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamReader;
import javolution.xml.stream.XMLStreamWriter;

public abstract class XMLFormat<T>
{
private static final String NULL = "Null";

public boolean isReferenceable() {
return true;
}

public T newInstance(Class<? extends T> cls, InputElement xml) throws XMLStreamException {
try {
return cls.newInstance();
} catch (InstantiationException e) {
throw new XMLStreamException(e);
} catch (IllegalAccessException e) {
throw new XMLStreamException(e);
} 
}

public abstract void write(T paramT, OutputElement paramOutputElement) throws XMLStreamException;

public abstract void read(InputElement paramInputElement, T paramT) throws XMLStreamException;

public static final class InputElement
{
final XMLStreamReaderImpl _reader = new XMLStreamReaderImpl();

private XMLBinding _binding;

private XMLReferenceResolver _referenceResolver;

private boolean _isReaderAtNext;

InputElement() {
reset();
}

public XMLStreamReader getStreamReader() {
return (XMLStreamReader)this._reader;
}

public boolean hasNext() throws XMLStreamException {
if (!this._isReaderAtNext) {
this._isReaderAtNext = true;
this._reader.nextTag();
} 
return (this._reader.getEventType() == 1);
}

public <T> T getNext() throws XMLStreamException {
if (!hasNext()) {
throw new XMLStreamException("No more element to read", this._reader.getLocation());
}

if (this._reader.getLocalName().equals("Null")) {
if (this._reader.next() != 2)
throw new XMLStreamException("Non Empty Null Element"); 
this._isReaderAtNext = false;
return null;
} 

Object ref = readReference();
if (ref != null) {
return (T)ref;
}

Class<?> cls = this._binding.readClass((XMLStreamReader)this._reader, false);
return readInstanceOf(cls);
}

public <T> T get(String name) throws XMLStreamException {
if (!hasNext() || !this._reader.getLocalName().equals(name))
{
return null;
}
Object ref = readReference();
if (ref != null) {
return (T)ref;
}

Class<?> cls = this._binding.readClass((XMLStreamReader)this._reader, true);
return readInstanceOf(cls);
}

public <T> T get(String localName, String uri) throws XMLStreamException {
if (uri == null) {
return get(localName);
}
if (!hasNext() || !this._reader.getLocalName().equals(localName) || !this._reader.getNamespaceURI().equals(uri))
{

return null;
}
Object ref = readReference();
if (ref != null) {
return (T)ref;
}

Class<?> cls = this._binding.readClass((XMLStreamReader)this._reader, true);
return readInstanceOf(cls);
}

public <T> T get(String name, Class<T> cls) throws XMLStreamException {
if (!hasNext() || !this._reader.getLocalName().equals(name))
{
return null;
}
Object ref = readReference();
if (ref != null) {
return (T)ref;
}
return readInstanceOf(cls);
}

public <T> T get(String localName, String uri, Class<T> cls) throws XMLStreamException {
if (uri == null) {
return get(localName, cls);
}
if (!hasNext() || !this._reader.getLocalName().equals(localName) || !this._reader.getNamespaceURI().equals(uri))
{

return null;
}
Object ref = readReference();
if (ref != null) {
return (T)ref;
}
return readInstanceOf(cls);
}

private Object readReference() throws XMLStreamException {
if (this._referenceResolver == null)
return null; 
Object ref = this._referenceResolver.readReference(this);
if (ref == null)
return null; 
if (this._reader.next() != 2)
throw new XMLStreamException("Non Empty Reference Element"); 
this._isReaderAtNext = false;
return ref;
}

private <T> T readInstanceOf(Class<?> cls) throws XMLStreamException {
XMLFormat<?> xmlFormat = this._binding.getFormat(cls);

this._isReaderAtNext = false;
Object obj = xmlFormat.newInstance(cls, this);

if (this._referenceResolver != null) {
this._referenceResolver.createReference(obj, this);
}

xmlFormat.read(this, obj);
if (hasNext()) {
throw new XMLStreamException("Incomplete element reading", this._reader.getLocation());
}
this._isReaderAtNext = false;
return (T)obj;
}

public CharArray getText() throws XMLStreamException {
CharArray txt = this._reader.getElementText();
this._isReaderAtNext = true;
return txt;
}

public Attributes getAttributes() throws XMLStreamException {
if (this._isReaderAtNext) {
throw new XMLStreamException("Attributes should be read before content");
}
return this._reader.getAttributes();
}

public CharArray getAttribute(String name) throws XMLStreamException {
if (this._isReaderAtNext) {
throw new XMLStreamException("Attributes should be read before reading content");
}
return this._reader.getAttributeValue(null, name);
}

public String getAttribute(String name, String defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? value.toString() : defaultValue;
}

public boolean getAttribute(String name, boolean defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? value.toBoolean() : defaultValue;
}

public char getAttribute(String name, char defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
if (value == null)
return defaultValue; 
if (value.length() != 1) {
throw new XMLStreamException("Single character expected (read '" + value + "')");
}
return value.charAt(0);
}

public byte getAttribute(String name, byte defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? (byte)value.toInt() : defaultValue;
}

public short getAttribute(String name, short defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? (short)value.toInt() : defaultValue;
}

public int getAttribute(String name, int defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? value.toInt() : defaultValue;
}

public long getAttribute(String name, long defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? value.toLong() : defaultValue;
}

public float getAttribute(String name, float defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? value.toFloat() : defaultValue;
}

public double getAttribute(String name, double defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
return (value != null) ? value.toDouble() : defaultValue;
}

public <T> T getAttribute(String name, T defaultValue) throws XMLStreamException {
CharArray value = getAttribute(name);
if (value == null) {
return defaultValue;
}
Class<?> type = defaultValue.getClass();
TextFormat<?> format = TextContext.getFormat(type);
if (format == null) {
throw new XMLStreamException("No TextFormat defined for " + type);
}
return (T)format.parse((CharSequence)value);
}

void setBinding(XMLBinding xmlBinding) {
this._binding = xmlBinding;
}

void setReferenceResolver(XMLReferenceResolver xmlReferenceResolver) {
this._referenceResolver = xmlReferenceResolver;
}

void reset() {
this._binding = XMLBinding.DEFAULT;
this._isReaderAtNext = false;
this._reader.reset();
this._referenceResolver = null;
}
}

public static final class OutputElement
{
final XMLStreamWriterImpl _writer = new XMLStreamWriterImpl();

private XMLBinding _binding;

private XMLReferenceResolver _referenceResolver;

private TextBuilder _tmpTextBuilder;

public XMLStreamWriter getStreamWriter() {
return (XMLStreamWriter)this._writer;
}

public void add(Object obj) throws XMLStreamException {
if (obj == null) {
this._writer.writeEmptyElement("Null");

return;
} 

Class<?> cls = obj.getClass();
this._binding.writeClass(cls, (XMLStreamWriter)this._writer, false);

XMLFormat<Object> xmlFormat = (XMLFormat)this._binding.getFormat(cls);

if (xmlFormat.isReferenceable() && writeReference(obj)) {
return;
}
xmlFormat.write(obj, this);
this._writer.writeEndElement();
}

public void add(Object obj, String name) throws XMLStreamException {
if (obj == null) {
return;
}

this._writer.writeStartElement(name);

Class<?> cls = obj.getClass();
this._binding.writeClass(cls, (XMLStreamWriter)this._writer, true);

XMLFormat<Object> xmlFormat = (XMLFormat)this._binding.getFormat(cls);

if (xmlFormat.isReferenceable() && writeReference(obj)) {
return;
}
xmlFormat.write(obj, this);
this._writer.writeEndElement();
}

public void add(Object obj, String localName, String uri) throws XMLStreamException {
if (obj == null) {
return;
}

this._writer.writeStartElement(uri, localName);

Class<?> cls = obj.getClass();
this._binding.writeClass(cls, (XMLStreamWriter)this._writer, true);

XMLFormat<Object> xmlFormat = (XMLFormat)this._binding.getFormat(cls);

if (xmlFormat.isReferenceable() && writeReference(obj)) {
return;
}
xmlFormat.write(obj, this);
this._writer.writeEndElement();
}

public <T> void add(T obj, String name, Class<T> cls) throws XMLStreamException {
if (obj == null) {
return;
}

this._writer.writeStartElement(name);

XMLFormat<T> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
if (xmlFormat.isReferenceable() && writeReference(obj)) {
return;
}
xmlFormat.write(obj, this);
this._writer.writeEndElement();
}

public <T> void add(T obj, String localName, String uri, Class<T> cls) throws XMLStreamException {
if (obj == null) {
return;
}

this._writer.writeStartElement(uri, localName);

XMLFormat<T> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
if (xmlFormat.isReferenceable() && writeReference(obj)) {
return;
}
xmlFormat.write(obj, this);
this._writer.writeEndElement();
}

private boolean writeReference(Object obj) throws XMLStreamException {
if (this._referenceResolver == null || !this._referenceResolver.writeReference(obj, this))
{
return false; } 
this._writer.writeEndElement();
return true;
}

public void addText(CharSequence text) throws XMLStreamException {
this._writer.writeCharacters(text);
}

public void addText(String text) throws XMLStreamException {
this._writer.writeCharacters(text);
}

public void setAttribute(String name, CharSequence value) throws XMLStreamException {
if (value == null)
return; 
this._writer.writeAttribute(name, value);
}

public void setAttribute(String name, String value) throws XMLStreamException {
if (value == null)
return; 
this._writer.writeAttribute(name, value);
}

public void setAttribute(String name, boolean value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}
OutputElement() {
this._tmpTextBuilder = new TextBuilder();
reset();
}

public void setAttribute(String name, char value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, byte value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, short value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, int value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, long value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, float value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, double value) throws XMLStreamException {
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
}

public void setAttribute(String name, Object value) throws XMLStreamException {
if (value == null)
return; 
setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value.toString()));
}

void setBinding(XMLBinding xmlBinding) {
this._binding = xmlBinding;
}

void setReferenceResolver(XMLReferenceResolver xmlReferenceResolver) {
this._referenceResolver = xmlReferenceResolver;
}

void reset() {
this._binding = XMLBinding.DEFAULT;
this._writer.reset();
this._writer.setRepairingNamespaces(true);
this._writer.setAutomaticEmptyElements(true);
this._referenceResolver = null;
}
}

public static class Default
extends XMLFormat<Object>
{
public boolean isReferenceable() {
return false;
}

public Object newInstance(Class<?> cls, XMLFormat.InputElement xml) throws XMLStreamException {
TextFormat<?> format = TextContext.getFormat(cls);
if (format == null) {
throw new XMLStreamException("No TextFormat defined to parse instances of " + cls);
}
CharArray value = xml.getAttribute("value");
if (value == null) {
throw new XMLStreamException("Missing value attribute (to be able to parse the instance of " + cls + ")");
}

return format.parse((CharSequence)value);
}

public void read(XMLFormat.InputElement xml, Object obj) throws XMLStreamException {}

public void write(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
TextBuilder tmp = new TextBuilder();
TextFormat<?> tf = TextContext.getFormat(obj.getClass());
tf.format(obj, tmp);
xml.setAttribute("value", (CharSequence)tmp);
}
}
}

