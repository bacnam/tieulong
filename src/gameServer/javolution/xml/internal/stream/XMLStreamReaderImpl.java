package javolution.xml.internal.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javolution.context.LogContext;
import javolution.io.UTF8StreamReader;
import javolution.lang.Realtime;
import javolution.text.CharArray;
import javolution.xml.sax.Attributes;
import javolution.xml.stream.Location;
import javolution.xml.stream.NamespaceContext;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamReader;

@Realtime
public final class XMLStreamReaderImpl
implements XMLStreamReader
{
static final String[] NAMES_OF_EVENTS = new String[] { "UNDEFINED", "START_ELEMENT", "END_ELEMENT", "PROCESSING_INSTRUCTIONS", "CHARACTERS", "COMMENT", "SPACE", "START_DOCUMENT", "END_DOCUMENT", "ENTITY_REFERENCE", "ATTRIBUTE", "DTD", "CDATA", "NAMESPACE", "NOTATION_DECLARATION", "ENTITY_DECLARATION" };

static final int READER_BUFFER_CAPACITY = 4096;

CharArray _prolog;

private int _readIndex;

private int _readCount;

private char[] _data = new char[8192];

private int _index;

private int _depth;

private CharArray _qName;

private int _prefixSep;

private CharArray _attrQName;

private int _attrPrefixSep;

private CharArray _attrValue;

private int _eventType = 7;

private boolean _isEmpty;

boolean _charactersPending = false;

private int _start;

private int _state = 1;

private CharArray _text;

private Reader _reader;

private final char[] _readBuffer = new char[4096];

private int _startOffset;

private final LocationImpl _location = new LocationImpl();

private final NamespacesImpl _namespaces = new NamespacesImpl();

private final AttributesImpl _attributes = new AttributesImpl(this._namespaces);

private CharArray[] _elemStack = new CharArray[16];

private String _encoding;

private final EntitiesImpl _entities = new EntitiesImpl();

private final UTF8StreamReader _utf8StreamReader = new UTF8StreamReader();

private final XMLInputFactoryImpl _factory;

private static final int STATE_CHARACTERS = 1;

private static final int STATE_MARKUP = 2;
private static final int STATE_COMMENT = 3;
private static final int STATE_PI = 4;

public XMLStreamReaderImpl() {
this(null);
}
private static final int STATE_CDATA = 5; private static final int STATE_OPEN_TAGxREAD_ELEM_NAME = 6; private static final int STATE_OPEN_TAGxELEM_NAME_READ = 7; private static final int STATE_OPEN_TAGxREAD_ATTR_NAME = 8;
private static final int STATE_OPEN_TAGxATTR_NAME_READ = 9;
private static final int STATE_OPEN_TAGxEQUAL_READ = 10;
private static final int STATE_OPEN_TAGxREAD_ATTR_VALUE_SIMPLE_QUOTE = 11;
private static final int STATE_OPEN_TAGxREAD_ATTR_VALUE_DOUBLE_QUOTE = 12;
private static final int STATE_OPEN_TAGxEMPTY_TAG = 13;
private static final int STATE_CLOSE_TAGxREAD_ELEM_NAME = 14;
private static final int STATE_CLOSE_TAGxELEM_NAME_READ = 15;
private static final int STATE_DTD = 16;
private static final int STATE_DTD_INTERNAL = 17;
private final Runnable _createSeqLogic;
private CharArray[] _seqs;
private int _seqsIndex;
private int _seqsCapacity;

public void setInput(InputStream in) throws XMLStreamException {
setInput(in, detectEncoding(in));
CharArray prologEncoding = getCharacterEncodingScheme();

if (prologEncoding != null && !prologEncoding.equals(this._encoding) && (!isUTF8(prologEncoding) || !isUTF8(this._encoding))) {

int startOffset = this._readCount;
reset();
this._startOffset = startOffset;
setInput(in, prologEncoding.toString());
} 
}

private static boolean isUTF8(Object encoding) {
return (encoding.equals("utf-8") || encoding.equals("UTF-8") || encoding.equals("ASCII") || encoding.equals("utf8") || encoding.equals("UTF8"));
}

public void setInput(InputStream in, String encoding) throws XMLStreamException {
this._encoding = encoding;
if (isUTF8(encoding)) {
setInput((Reader)this._utf8StreamReader.setInput(in));
} else {
try {
setInput(new InputStreamReader(in, encoding));
} catch (UnsupportedEncodingException e) {
throw new XMLStreamException(e);
} 
} 
}

public void setInput(Reader reader) throws XMLStreamException {
if (this._reader != null)
throw new IllegalStateException("Reader not closed or reset"); 
this._reader = reader;
try {
int readCount = reader.read(this._readBuffer, this._startOffset, this._readBuffer.length - this._startOffset);

this._readCount = (readCount >= 0) ? (readCount + this._startOffset) : this._startOffset;

if (this._readCount >= 5 && this._readBuffer[0] == '<' && this._readBuffer[1] == '?' && this._readBuffer[2] == 'x' && this._readBuffer[3] == 'm' && this._readBuffer[4] == 'l' && this._readBuffer[5] == ' ') {

next();
this._prolog = getPIData();
this._index = this._prolog.offset() + this._prolog.length();
this._start = this._index;
this._eventType = 7;
} 
} catch (IOException e) {
throw new XMLStreamException(e);
} 
}

public int getDepth() {
return this._depth;
}

public CharArray getQName() {
if (this._eventType != 1 && this._eventType != 2)
{
throw new IllegalStateException("Not a start element or an end element");
}
return this._qName;
}

public CharArray getQName(int depth) {
if (depth > getDepth())
throw new IllegalArgumentException(); 
return this._elemStack[depth];
}

public Attributes getAttributes() {
if (this._eventType != 1)
throw new IllegalStateException("Not a start element"); 
return this._attributes;
}

public void setEntities(Map<String, String> entities) {
this._entities.setEntitiesMapping(entities);
}

public String toString() {
return "XMLStreamReader - State: " + NAMES_OF_EVENTS[this._eventType] + ", Location: " + this._location.toString();
}

public int next() throws XMLStreamException {
if (this._eventType == 1) {
if (this._isEmpty) {
this._isEmpty = false;
return this._eventType = 2;
} 
} else if (this._eventType == 2) {
this._namespaces.pop();
CharArray startElem = this._elemStack[this._depth--];
this._start = this._index = startElem.offset();
while (this._seqs[--this._seqsIndex] != startElem);
} 

label240: while (true) {
if (this._readIndex >= this._readCount && isEndOfStream())
return this._eventType; 
char c = this._readBuffer[this._readIndex++];
if (c <= '&') {
c = (c == '&') ? replaceEntity() : ((c < ' ') ? handleEndOfLine(c) : c);
}
this._data[this._index++] = c;

switch (this._state) {

case 1:
while (true) {

if (c == '<') {
int length = this._index - this._start - 1;
if (length > 0) {
if (this._charactersPending) {
this._text.setArray(this._data, this._text.offset(), this._text.length() + length);
} else {

this._text = newSeq(this._start, length);
this._charactersPending = true;
} 
this._start = this._index - 1;
} 
this._state = 2;

continue label240;
} 

if (this._readIndex >= this._readCount && isEndOfStream())
return this._eventType; 
c = this._readBuffer[this._readIndex++];
if (c <= '&') {
c = (c == '&') ? replaceEntity() : ((c < ' ') ? handleEndOfLine(c) : c);
}
this._data[this._index++] = c;
} 

case 5:
while (true) {
if (c == '>' && this._index - this._start >= 3 && this._data[this._index - 2] == ']' && this._data[this._index - 3] == ']') {

this._index -= 3;
int length = this._index - this._start;
if (length > 0) {
if (this._charactersPending) {
this._text.setArray(this._data, this._text.offset(), this._text.length() + length);
} else {

this._text = newSeq(this._start, length);
this._charactersPending = true;
} 
}
this._start = this._index;
this._state = 1;

continue label240;
} 

if (this._readIndex >= this._readCount)
reloadBuffer(); 
c = this._readBuffer[this._readIndex++];
if (c < ' ')
c = handleEndOfLine(c); 
this._data[this._index++] = c;
} 

case 16:
if (c == '>') {
this._text = newSeq(this._start, this._index - this._start);
this._index = this._start;
this._state = 1;
return this._eventType = 11;
}  if (c == '[') {
this._state = 17;
}
continue;

case 17:
if (c == ']') {
this._state = 16;
}
continue;

case 2:
if (this._index - this._start == 2) {
if (c == '/') {
this._start = this._index -= 2;
this._state = 14;
this._prefixSep = -1;
if (this._charactersPending) {
this._charactersPending = false;
return this._eventType = 4;
}  continue;
}  if (c == '?') {
this._start = this._index -= 2;
this._state = 4;
if (this._charactersPending) {
this._charactersPending = false;
return this._eventType = 4;
}  continue;
}  if (c != '!') {
this._data[this._start] = c;
this._index = this._start + 1;
this._state = 6;
this._prefixSep = -1;
if (this._charactersPending) {
this._charactersPending = false;
return this._eventType = 4;
} 
}  continue;
}  if (this._index - this._start == 4 && this._data[this._start + 1] == '!' && this._data[this._start + 2] == '-' && this._data[this._start + 3] == '-') {

this._start = this._index -= 4;
this._state = 3;
if (this._charactersPending) {
this._charactersPending = false;
return this._eventType = 4;
}  continue;
} 
if (this._index - this._start == 9 && this._data[this._start + 1] == '!' && this._data[this._start + 2] == '[' && this._data[this._start + 3] == 'C' && this._data[this._start + 4] == 'D' && this._data[this._start + 5] == 'A' && this._data[this._start + 6] == 'T' && this._data[this._start + 7] == 'A' && this._data[this._start + 8] == '[') {

this._start = this._index -= 9;
this._state = 5; continue;
} 
if (this._index - this._start == 9 && this._data[this._start + 1] == '!' && this._data[this._start + 2] == 'D' && this._data[this._start + 3] == 'O' && this._data[this._start + 4] == 'C' && this._data[this._start + 5] == 'T' && this._data[this._start + 6] == 'Y' && this._data[this._start + 7] == 'P' && this._data[this._start + 8] == 'E')
{

this._state = 16;
}
continue;

case 3:
while (true) {
if (c == '>' && this._index - this._start >= 3 && this._data[this._index - 2] == '-' && this._data[this._index - 3] == '-') {

this._index -= 3;
this._text = newSeq(this._start, this._index - this._start);
this._state = 1;
this._index = this._start;
return this._eventType = 5;
} 

if (this._readIndex >= this._readCount)
reloadBuffer(); 
c = this._readBuffer[this._readIndex++];
if (c < ' ')
c = handleEndOfLine(c); 
this._data[this._index++] = c;
} 

case 4:
if (c == '>' && this._index - this._start >= 2 && this._data[this._index - 2] == '?') {

this._index -= 2;
this._text = newSeq(this._start, this._index - this._start);
this._state = 1;
this._index = this._start;
return this._eventType = 3;
} 
continue;

case 6:
this._attributes.reset();
this._namespaces.push();

while (true) {
if (c < '@') {
if (c == '>') {
this._qName = newSeq(this._start, --this._index - this._start);
this._start = this._index;
this._state = 1;
processStartTag();
this._isEmpty = false;
return this._eventType = 1;
}  if (c == '/') {
this._qName = newSeq(this._start, --this._index - this._start);
this._start = this._index;
this._state = 13; continue label240;
} 
if (c == ':') {
this._prefixSep = this._index - 1;
} else if (c <= ' ') {
this._qName = newSeq(this._start, --this._index - this._start);
this._state = 7;

continue label240;
} 
} 
if (this._readIndex >= this._readCount)
reloadBuffer(); 
c = this._data[this._index++] = this._readBuffer[this._readIndex++];
} 

case 7:
if (c == '>') {
this._start = --this._index;
this._state = 1;
processStartTag();
this._isEmpty = false;
return this._eventType = 1;
}  if (c == '/') {
this._state = 13; continue;
}  if (c > ' ') {
this._start = this._index - 1;
this._attrPrefixSep = -1;
this._state = 8;
} 
continue;

case 8:
while (true) {
if (c < '@') {
if (c <= ' ') {
this._attrQName = newSeq(this._start, --this._index - this._start);
this._state = 9; continue label240;
} 
if (c == '=') {
this._attrQName = newSeq(this._start, --this._index - this._start);
this._state = 10; continue label240;
} 
if (c == ':') {
this._attrPrefixSep = this._index - 1;
}
} 

if (this._readIndex >= this._readCount)
reloadBuffer(); 
this._data[this._index++] = c = this._readBuffer[this._readIndex++];
} 

case 9:
if (c == '=') {
this._index--;
this._state = 10; continue;
}  if (c > ' ') throw new XMLStreamException("'=' expected", this._location);

continue;

case 10:
if (c == '\'') {
this._start = --this._index;
this._state = 11; continue;
}  if (c == '"') {
this._start = --this._index;
this._state = 12; continue;
}  if (c > ' ') throw new XMLStreamException("Quotes expected", this._location);

continue;

case 11:
while (true) {
if (c == '\'') {
this._attrValue = newSeq(this._start, --this._index - this._start);
processAttribute();
this._state = 7;

continue label240;
} 

if (this._readIndex >= this._readCount)
reloadBuffer(); 
c = this._readBuffer[this._readIndex++];
if (c == '&')
c = replaceEntity(); 
this._data[this._index++] = c;
} 

case 12:
while (true) {
if (c == '"') {
this._attrValue = newSeq(this._start, --this._index - this._start);
processAttribute();
this._state = 7;

continue label240;
} 

if (this._readIndex >= this._readCount)
reloadBuffer(); 
c = this._readBuffer[this._readIndex++];
if (c == '&')
c = replaceEntity(); 
this._data[this._index++] = c;
} 

case 13:
if (c == '>') {
this._start = --this._index;
this._state = 1;
processStartTag();
this._isEmpty = true;
return this._eventType = 1;
} 
throw new XMLStreamException("'>' expected", this._location);

case 14:
while (true) {
if (c < '@') {
if (c == '>') {
this._qName = newSeq(this._start, --this._index - this._start);
this._start = this._index;
this._state = 1;
processEndTag();
return this._eventType = 2;
}  if (c == ':') {
this._prefixSep = this._index - 1;
} else if (c <= ' ') {
this._qName = newSeq(this._start, --this._index - this._start);
this._state = 15;

continue label240;
} 
} 
if (this._readIndex >= this._readCount)
reloadBuffer(); 
c = this._data[this._index++] = this._readBuffer[this._readIndex++];
} 

case 15:
if (c == '>') {
this._start = --this._index;
this._state = 1;
processEndTag();
return this._eventType = 2;
}  if (c > ' ') throw new XMLStreamException("'>' expected", this._location); 
continue;
} 
break;
} 
throw new XMLStreamException("State unknown: " + this._state, this._location);
}

private void reloadBuffer() throws XMLStreamException {
if (this._reader == null)
throw new XMLStreamException("Input not specified"); 
this._location._column += this._readIndex;
this._location._charactersRead += this._readIndex;
this._readIndex = 0;
try {
this._readCount = this._reader.read(this._readBuffer, 0, this._readBuffer.length);
if (this._readCount <= 0 && (this._depth != 0 || this._state != 1))
{
throw new XMLStreamException("Unexpected end of document", this._location);
}
} catch (IOException e) {
throw new XMLStreamException(e);
} 
while (this._index + this._readCount >= this._data.length) {
increaseDataBuffer();
}
}

private boolean isEndOfStream() throws XMLStreamException {
if (this._readIndex >= this._readCount)
reloadBuffer(); 
if (this._readCount <= 0) {

if (this._eventType == 8) {
throw new XMLStreamException("End document has already been reached");
}
int length = this._index - this._start;
if (length > 0) {
if (this._charactersPending) {
this._text.setArray(this._data, this._text.offset(), this._text.length() + length);
} else {

this._text = newSeq(this._start, length);
} 
this._start = this._index;
this._eventType = 4;
} else {
this._eventType = 8;
} 
return true;
} 
return false;
}

private char handleEndOfLine(char c) throws XMLStreamException {
if (c == '\r') {

if (this._readIndex >= this._readCount)
reloadBuffer(); 
if (this._readIndex < this._readCount && this._readBuffer[this._readIndex] == '\n')
this._readIndex++; 
c = '\n';
} 
if (c == '\n')
{ this._location._line++;
this._location._column = -this._readIndex; }
else if (c == '\000') { throw new XMLStreamException("Illegal XML character U+0000", this._location); }

return c;
}

private char replaceEntity() throws XMLStreamException {
if (this._state == 3 || this._state == 4 || this._state == 5)
{
return '&';
}
int start = this._index;
this._data[this._index++] = '&';
while (true) {
if (this._readIndex >= this._readCount)
reloadBuffer(); 
char c1 = this._data[this._index++] = this._readBuffer[this._readIndex++];
if (c1 == ';')
break; 
if (c1 <= ' ') {
throw new XMLStreamException("';' expected", this._location);
}
} 
while (start + this._entities.getMaxLength() >= this._data.length) {
increaseDataBuffer();
}

int length = this._entities.replaceEntity(this._data, start, this._index - start);

this._index = start + length;

if (this._readIndex >= this._readCount)
reloadBuffer(); 
char c = this._readBuffer[this._readIndex++];
return (c == '&') ? (c = replaceEntity()) : c;
}

private void processAttribute() throws XMLStreamException {
if (this._attrPrefixSep < 0) {
if (isXMLNS(this._attrQName)) {
this._namespaces.setPrefix(this._namespaces._defaultNsPrefix, this._attrValue);
} else {
this._attributes.addAttribute(this._attrQName, null, this._attrQName, this._attrValue);
} 
} else {

int offset = this._attrQName.offset();
int length = this._attrQName.length();

CharArray prefix = newSeq(offset, this._attrPrefixSep - offset);

CharArray localName = newSeq(this._attrPrefixSep + 1, offset + length - this._attrPrefixSep - 1);

if (isXMLNS(prefix)) {
this._namespaces.setPrefix(localName, this._attrValue);
} else {
this._attributes.addAttribute(localName, prefix, this._attrQName, this._attrValue);
} 
} 
}

private static boolean isXMLNS(CharArray chars) {
return (chars.length() == 5 && chars.charAt(0) == 'x' && chars.charAt(1) == 'm' && chars.charAt(2) == 'l' && chars.charAt(3) == 'n' && chars.charAt(4) == 's');
}

private void processEndTag() throws XMLStreamException {
if (!this._qName.equals(this._elemStack[this._depth])) {
throw new XMLStreamException("Unexpected end tag for " + this._qName, this._location);
}
}

private void processStartTag() throws XMLStreamException {
if (++this._depth >= this._elemStack.length) {
increaseStack();
}
this._elemStack[this._depth] = this._qName;
}

public void reset() {
this._attributes.reset();
this._attrPrefixSep = 0;
this._attrQName = null;
this._attrValue = null;
this._attrQName = null;
this._charactersPending = false;
this._encoding = null;
this._entities.reset();
this._eventType = 7;
this._index = 0;
this._isEmpty = false;
this._location.reset();
this._namespaces.reset();
this._prolog = null;
this._readCount = 0;
this._reader = null;
this._depth = 0;
this._readIndex = 0;
this._seqsIndex = 0;
this._start = 0;
this._startOffset = 0;
this._state = 1;
this._utf8StreamReader.reset();

if (this._factory != null) {
this._factory.recycle(this);
}
}

private CharArray newSeq(int offset, int length) {
CharArray seq = (this._seqsIndex < this._seqsCapacity) ? this._seqs[this._seqsIndex++] : newSeq2();

return seq.setArray(this._data, offset, length);
}

private CharArray newSeq2() {
this._createSeqLogic.run();
return this._seqs[this._seqsIndex++];
}
XMLStreamReaderImpl(XMLInputFactoryImpl factory) {
this._createSeqLogic = new Runnable()
{
public void run() {
if (XMLStreamReaderImpl.this._seqsCapacity >= XMLStreamReaderImpl.this._seqs.length) {
CharArray[] tmp = new CharArray[XMLStreamReaderImpl.this._seqs.length * 2];
System.arraycopy(XMLStreamReaderImpl.this._seqs, 0, tmp, 0, XMLStreamReaderImpl.this._seqs.length);
XMLStreamReaderImpl.this._seqs = tmp;
} 
CharArray seq = new CharArray();
XMLStreamReaderImpl.this._seqs[XMLStreamReaderImpl.this._seqsCapacity++] = seq;
}
};

this._seqs = new CharArray[256];
this._factory = factory;
}

private void increaseDataBuffer() {
char[] tmp = new char[this._data.length * 2];
LogContext.info(new Object[] { new CharArray("XMLStreamReaderImpl: Data buffer increased to " + tmp.length) });

System.arraycopy(this._data, 0, tmp, 0, this._data.length);
this._data = tmp;
}

private void increaseStack() {
CharArray[] tmp = new CharArray[this._elemStack.length * 2];
LogContext.info(new Object[] { new CharArray("XMLStreamReaderImpl: CharArray stack increased to " + tmp.length) });

System.arraycopy(this._elemStack, 0, tmp, 0, this._elemStack.length);
this._elemStack = tmp;
}

private final class LocationImpl
implements Location
{
int _column;

int _line;
int _charactersRead;

private LocationImpl() {}

public int getLineNumber() {
return this._line + 1;
}

public int getColumnNumber() {
return this._column + XMLStreamReaderImpl.this._readIndex;
}

public int getCharacterOffset() {
return this._charactersRead + XMLStreamReaderImpl.this._readIndex;
}

public String getPublicId() {
return null;
}

public String getSystemId() {
return null;
}

public String toString() {
return "Line " + getLineNumber() + ", Column " + getColumnNumber();
}

public void reset() {
this._line = 0;
this._column = 0;
this._charactersRead = 0;
}
}

public void require(int type, CharSequence namespaceURI, CharSequence localName) throws XMLStreamException {
if (this._eventType != type) {
throw new XMLStreamException("Expected event: " + NAMES_OF_EVENTS[type] + ", found event: " + NAMES_OF_EVENTS[this._eventType]);
}

if (namespaceURI != null && !getNamespaceURI().equals(namespaceURI)) {
throw new XMLStreamException("Expected namespace URI: " + namespaceURI + ", found: " + getNamespaceURI());
}
if (localName != null && !getLocalName().equals(localName)) {
throw new XMLStreamException("Expected local name: " + localName + ", found: " + getLocalName());
}
}

public CharArray getElementText() throws XMLStreamException {
if (getEventType() != 1) throw new XMLStreamException("Parser must be on START_ELEMENT to read next text", getLocation());

CharArray text = null;
int eventType = next();
while (eventType != 2) {
if (eventType == 4) {
if (text == null) {
text = getText();
} else {
text.setArray(this._data, text.offset(), text.length() + getText().length());
}

} else if (eventType != 3 && eventType != 5) {

if (eventType == 8) {
throw new XMLStreamException("Unexpected end of document when reading element text content", getLocation());
}

if (eventType == 1) {
throw new XMLStreamException("Element text content may not contain START_ELEMENT", getLocation());
}

throw new XMLStreamException("Unexpected event type " + NAMES_OF_EVENTS[eventType], getLocation());
} 

eventType = next();
} 
return (text != null) ? text : newSeq(0, 0);
}

public Object getProperty(String name) throws IllegalArgumentException {
if (name.equals("javolution.xml.stream.isCoalescing"))
return Boolean.TRUE; 
if (name.equals("javolution.xml.stream.entities")) {
return this._entities.getEntitiesMapping();
}
throw new IllegalArgumentException("Property: " + name + " not supported");
}

public void close() throws XMLStreamException {
reset();
}

public int getAttributeCount() {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
return this._attributes.getLength();
}

public CharArray getAttributeLocalName(int index) {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
return this._attributes.getLocalName(index);
}

public CharArray getAttributeNamespace(int index) {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
CharArray prefix = this._attributes.getPrefix(index);
return this._namespaces.getNamespaceURINullAllowed((CharSequence)prefix);
}

public CharArray getAttributePrefix(int index) {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
return this._attributes.getPrefix(index);
}

public CharArray getAttributeType(int index) {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
return this._attributes.getType(index);
}

public CharArray getAttributeValue(CharSequence uri, CharSequence localName) {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
return (uri == null) ? this._attributes.getValue(localName) : this._attributes.getValue(uri, localName);
}

public CharArray getAttributeValue(int index) {
if (this._eventType != 1)
throw illegalState("Not a start element"); 
return this._attributes.getValue(index);
}

public CharArray getCharacterEncodingScheme() {
return readPrologAttribute((CharSequence)ENCODING);
}

private static final CharArray ENCODING = new CharArray("encoding");

public String getEncoding() {
return this._encoding;
}

public int getEventType() {
return this._eventType;
}

public CharArray getLocalName() {
if (this._eventType != 1 && this._eventType != 2)
{
throw illegalState("Not a start or end element"); } 
if (this._prefixSep < 0)
return this._qName; 
CharArray localName = newSeq(this._prefixSep + 1, this._qName.offset() + this._qName.length() - this._prefixSep - 1);

return localName;
}

public Location getLocation() {
return this._location;
}

public int getNamespaceCount() {
if (this._eventType != 1 && this._eventType != 2)
{
throw illegalState("Not a start or end element"); } 
return this._namespaces._namespacesCount[this._depth];
}

public CharArray getNamespacePrefix(int index) {
if (this._eventType != 1 && this._eventType != 2)
{
throw illegalState("Not a start or end element"); } 
return this._namespaces._prefixes[index];
}

public CharArray getNamespaceURI(CharSequence prefix) {
if (this._eventType != 1 && this._eventType != 2)
{
throw illegalState("Not a start or end element"); } 
return this._namespaces.getNamespaceURI(prefix);
}

public CharArray getNamespaceURI(int index) {
if (this._eventType != 1 && this._eventType != 2)
{
throw illegalState("Not a start or end element"); } 
return this._namespaces._namespaces[index];
}

public NamespaceContext getNamespaceContext() {
return this._namespaces;
}

public CharArray getNamespaceURI() {
return this._namespaces.getNamespaceURINullAllowed((CharSequence)getPrefix());
}

public CharArray getPrefix() {
if (this._eventType != 1 && this._eventType != 2)
{
throw illegalState("Not a start or end element"); } 
if (this._prefixSep < 0)
return null; 
int offset = this._qName.offset();
CharArray prefix = newSeq(offset, this._prefixSep - offset);
return prefix;
}

public CharArray getPIData() {
if (this._eventType != 3)
throw illegalState("Not a processing instruction"); 
int offset = this._text.indexOf(' ') + this._text.offset() + 1;
CharArray piData = newSeq(offset, this._text.length() - offset);
return piData;
}

public CharArray getPITarget() {
if (this._eventType != 3)
throw illegalState("Not a processing instruction"); 
CharArray piTarget = newSeq(this._text.offset(), this._text.indexOf(' ') + this._text.offset());

return piTarget;
}

public CharArray getText() {
if (this._eventType != 4 && this._eventType != 5 && this._eventType != 11)
{

throw illegalState("Not a text event"); } 
return this._text;
}

public char[] getTextCharacters() {
return getText().array();
}

public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
CharArray text = getText();
int copyLength = Math.min(length, text.length());
System.arraycopy(text.array(), sourceStart + text.offset(), target, targetStart, copyLength);

return copyLength;
}

public int getTextLength() {
return getText().length();
}

public int getTextStart() {
return getText().offset();
}

public CharArray getVersion() {
return readPrologAttribute((CharSequence)VERSION);
}

private static final CharArray VERSION = new CharArray("version");

public boolean isStandalone() {
CharArray standalone = readPrologAttribute((CharSequence)STANDALONE);
return (standalone != null) ? standalone.equals("no") : true;
}

public boolean standaloneSet() {
return (readPrologAttribute((CharSequence)STANDALONE) != null);
}

private static final CharArray STANDALONE = new CharArray("standalone");

public boolean hasName() {
return (this._eventType == 1 || this._eventType == 2);
}

public boolean hasNext() throws XMLStreamException {
return (this._eventType != 8);
}

public boolean hasText() {
return ((this._eventType == 4 || this._eventType == 5 || this._eventType == 11) && this._text.length() > 0);
}

public boolean isAttributeSpecified(int index) {
if (this._eventType != 1)
throw new IllegalStateException("Not a start element"); 
return (this._attributes.getValue(index) != null);
}

public boolean isCharacters() {
return (this._eventType == 4);
}

public boolean isEndElement() {
return (this._eventType == 2);
}

public boolean isStartElement() {
return (this._eventType == 1);
}

public boolean isWhiteSpace() {
if (isCharacters()) {
char[] chars = this._text.array();
for (int i = this._text.offset(), end = this._text.offset() + this._text.length(); i < end;) {
if (!isWhiteSpace(chars[i++]))
return false; 
} 
return true;
} 
return false;
}

private static boolean isWhiteSpace(char c) {
return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
}

public int nextTag() throws XMLStreamException {
int eventType = next();
while (eventType == 5 || eventType == 3 || eventType == 11 || (eventType == 4 && isWhiteSpace()))
{

eventType = next();
}
if (eventType != 1 && eventType != 2)
{
throw new XMLStreamException("Tag expected (but found " + NAMES_OF_EVENTS[this._eventType] + ")");
}
return eventType;
}

private IllegalStateException illegalState(String msg) {
return new IllegalStateException(msg + " (" + NAMES_OF_EVENTS[this._eventType] + ")");
}

private String detectEncoding(InputStream input) throws XMLStreamException {
int byte0, byte1;
try {
byte0 = input.read();
} catch (IOException e) {
throw new XMLStreamException(e);
} 
if (byte0 == -1)
throw new XMLStreamException("Premature End-Of-File"); 
if (byte0 == 60) {
this._readBuffer[this._startOffset++] = '<';
return "UTF-8";
} 

try {
byte1 = input.read();
} catch (IOException e) {
throw new XMLStreamException(e);
} 
if (byte1 == -1)
throw new XMLStreamException("Premature End-Of-File"); 
if (byte0 == 0 && byte1 == 60) {
this._readBuffer[this._startOffset++] = '<';
return "UTF-16BE";
}  if (byte0 == 60 && byte1 == 0) {
this._readBuffer[this._startOffset++] = '<';
return "UTF-16LE";
}  if (byte0 == 255 && byte1 == 254)
return "UTF-16"; 
if (byte0 == 254 && byte1 == 255) {
return "UTF-16";
}
this._readBuffer[this._startOffset++] = (char)byte0;
this._readBuffer[this._startOffset++] = (char)byte1;
return "UTF-8";
}

private final CharArray readPrologAttribute(CharSequence name) {
if (this._prolog == null)
return null; 
int READ_EQUAL = 0;
int READ_QUOTE = 1;
int VALUE_SIMPLE_QUOTE = 2;
int VALUE_DOUBLE_QUOTE = 3;

int i = this._prolog.indexOf(name);
if (i >= 0) {
i += this._prolog.offset();
int maxIndex = this._prolog.offset() + this._prolog.length();
i += name.length();
int state = 0;
int valueOffset = 0;
while (i < maxIndex) {
char c = this._prolog.array()[i++];
switch (state) {
case 0:
if (c == '=') {
state = 1;
}

case 1:
if (c == '"') {
state = 3;
valueOffset = i; continue;
}  if (c == '\'') {
state = 2;
valueOffset = i;
} 

case 2:
if (c == '\'') {
return newSeq(valueOffset, i - valueOffset - 1);
}
case 3:
if (c == '"') {
return newSeq(valueOffset, i - valueOffset - 1);
}
} 
} 
} 
return null;
}
}

