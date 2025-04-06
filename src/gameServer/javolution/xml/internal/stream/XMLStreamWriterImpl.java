/*     */ package javolution.xml.internal.stream;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import javolution.io.UTF8StreamWriter;
/*     */ import javolution.lang.Realtime;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.text.Text;
/*     */ import javolution.text.TextBuilder;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ import javolution.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Realtime
/*     */ public final class XMLStreamWriterImpl
/*     */   implements XMLStreamWriter
/*     */ {
/*     */   private static final int BUFFER_LENGTH = 2048;
/*  46 */   private int _nesting = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private TextBuilder[] _qNames = new TextBuilder[16];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isElementOpen;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isEmptyElement;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final char[] _buffer = new char[2048];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   private final NamespacesImpl _namespaces = new NamespacesImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int _index;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isRepairingNamespaces;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  86 */   private String _repairingPrefix = "ns";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String _indentation;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private String _lineSeparator = "\n";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int _indentationLevel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _automaticEmptyElements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _noEmptyElementTag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int _autoNSCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean _isAttributeValue;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Writer _writer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String _encoding;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   private final UTF8StreamWriter _utf8StreamWriter = new UTF8StreamWriter();
/*     */   
/*     */   private final XMLOutputFactoryImpl _factory;
/*     */   
/*     */   private final CharArray _noChar;
/*     */   
/*     */   private final CharArray _tmpCharArray;
/*     */   
/*     */   private final TextBuilder _autoPrefix;
/*     */   
/*     */   public XMLStreamWriterImpl() {
/* 151 */     this(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(OutputStream out) throws XMLStreamException {
/* 171 */     this._utf8StreamWriter.setOutput(out);
/* 172 */     this._encoding = "UTF-8";
/* 173 */     setOutput((Writer)this._utf8StreamWriter);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(OutputStream out, String encoding) throws XMLStreamException {
/* 186 */     if (encoding.equals("UTF-8") || encoding.equals("utf-8") || encoding.equals("ASCII")) {
/*     */       
/* 188 */       setOutput(out);
/*     */     } else {
/*     */       try {
/* 191 */         this._encoding = encoding;
/* 192 */         setOutput(new OutputStreamWriter(out, encoding));
/* 193 */       } catch (UnsupportedEncodingException e) {
/* 194 */         throw new XMLStreamException(e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOutput(Writer writer) throws XMLStreamException {
/* 208 */     if (this._writer != null)
/* 209 */       throw new IllegalStateException("Writer not closed or reset"); 
/* 210 */     this._writer = writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRepairingNamespaces(boolean isRepairingNamespaces) {
/* 221 */     this._isRepairingNamespaces = isRepairingNamespaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRepairingPrefix(String repairingPrefix) {
/* 233 */     this._repairingPrefix = repairingPrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIndentation(String indentation) {
/* 244 */     this._indentation = indentation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLineSeparator(String lineSeparator) {
/* 253 */     this._lineSeparator = lineSeparator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAutomaticEmptyElements(boolean automaticEmptyElements) {
/* 266 */     this._automaticEmptyElements = automaticEmptyElements;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoEmptyElementTag(boolean noEmptyElementTag) {
/* 278 */     this._noEmptyElementTag = noEmptyElementTag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 283 */     this._automaticEmptyElements = false;
/* 284 */     this._autoNSCount = 0;
/* 285 */     this._encoding = null;
/* 286 */     this._indentation = null;
/* 287 */     this._indentationLevel = 0;
/* 288 */     this._index = 0;
/* 289 */     this._isAttributeValue = false;
/* 290 */     this._isElementOpen = false;
/* 291 */     this._isEmptyElement = false;
/* 292 */     this._isRepairingNamespaces = false;
/* 293 */     this._namespaces.reset();
/* 294 */     this._nesting = 0;
/* 295 */     this._noEmptyElementTag = false;
/* 296 */     this._repairingPrefix = "ns";
/* 297 */     this._utf8StreamWriter.reset();
/* 298 */     this._writer = null;
/*     */     
/* 300 */     if (this._factory != null) {
/* 301 */       this._factory.recycle(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeStartElement(CharSequence localName) throws XMLStreamException {
/* 307 */     if (localName == null)
/* 308 */       throw new XMLStreamException("Local name cannot be null"); 
/* 309 */     writeNewElement(null, localName, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeStartElement(CharSequence namespaceURI, CharSequence localName) throws XMLStreamException {
/* 315 */     if (localName == null)
/* 316 */       throw new XMLStreamException("Local name cannot be null"); 
/* 317 */     if (namespaceURI == null)
/* 318 */       throw new XMLStreamException("Namespace URI cannot be null"); 
/* 319 */     writeNewElement(null, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeStartElement(CharSequence prefix, CharSequence localName, CharSequence namespaceURI) throws XMLStreamException {
/* 325 */     if (localName == null)
/* 326 */       throw new XMLStreamException("Local name cannot be null"); 
/* 327 */     if (namespaceURI == null)
/* 328 */       throw new XMLStreamException("Namespace URI cannot be null"); 
/* 329 */     if (prefix == null)
/* 330 */       throw new XMLStreamException("Prefix cannot be null"); 
/* 331 */     writeNewElement(prefix, localName, namespaceURI);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(CharSequence localName) throws XMLStreamException {
/* 337 */     writeStartElement(localName);
/* 338 */     this._isEmptyElement = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(CharSequence namespaceURI, CharSequence localName) throws XMLStreamException {
/* 344 */     writeStartElement(namespaceURI, localName);
/* 345 */     this._isEmptyElement = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEmptyElement(CharSequence prefix, CharSequence localName, CharSequence namespaceURI) throws XMLStreamException {
/* 351 */     writeStartElement(prefix, localName, namespaceURI);
/* 352 */     this._isEmptyElement = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEndElement() throws XMLStreamException {
/* 357 */     if (this._isElementOpen) {
/* 358 */       if (this._isEmptyElement) {
/* 359 */         closeOpenTag();
/*     */       } else {
/* 361 */         if (this._automaticEmptyElements) {
/* 362 */           this._isEmptyElement = true;
/* 363 */           closeOpenTag();
/*     */           return;
/*     */         } 
/* 366 */         closeOpenTag();
/*     */       } 
/*     */     }
/*     */     
/* 370 */     if (this._indentation != null && this._indentationLevel != this._nesting - 1) {
/*     */ 
/*     */       
/* 373 */       writeNoEscape(this._lineSeparator);
/* 374 */       for (int i = 1; i < this._nesting; i++) {
/* 375 */         writeNoEscape(this._indentation);
/*     */       }
/*     */     } 
/*     */     
/* 379 */     write('<');
/* 380 */     write('/');
/* 381 */     writeNoEscape(this._qNames[this._nesting--]);
/* 382 */     write('>');
/* 383 */     this._namespaces.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEndDocument() throws XMLStreamException {
/* 388 */     if (this._isElementOpen)
/* 389 */       closeOpenTag(); 
/* 390 */     while (this._nesting > 0) {
/* 391 */       writeEndElement();
/*     */     }
/* 393 */     flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/* 398 */     if (this._writer != null) {
/* 399 */       if (this._nesting != 0) {
/* 400 */         writeEndDocument();
/*     */       }
/* 402 */       flush();
/*     */     } 
/* 404 */     reset();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws XMLStreamException {
/* 409 */     flushBuffer();
/*     */     try {
/* 411 */       this._writer.flush();
/* 412 */     } catch (IOException e) {
/* 413 */       throw new XMLStreamException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttribute(CharSequence localName, CharSequence value) throws XMLStreamException {
/* 420 */     if (localName == null)
/* 421 */       throw new XMLStreamException("Local name cannot be null"); 
/* 422 */     if (value == null)
/* 423 */       throw new XMLStreamException("Value cannot be null"); 
/* 424 */     writeAttributeOrNamespace(null, null, localName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttribute(CharSequence namespaceURI, CharSequence localName, CharSequence value) throws XMLStreamException {
/* 431 */     if (localName == null)
/* 432 */       throw new XMLStreamException("Local name cannot be null"); 
/* 433 */     if (value == null)
/* 434 */       throw new XMLStreamException("Value cannot be null"); 
/* 435 */     if (namespaceURI == null)
/* 436 */       throw new XMLStreamException("Namespace URI cannot be null"); 
/* 437 */     writeAttributeOrNamespace(null, namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeAttribute(CharSequence prefix, CharSequence namespaceURI, CharSequence localName, CharSequence value) throws XMLStreamException {
/* 444 */     if (localName == null)
/* 445 */       throw new XMLStreamException("Local name cannot be null"); 
/* 446 */     if (value == null)
/* 447 */       throw new XMLStreamException("Value cannot be null"); 
/* 448 */     if (namespaceURI == null)
/* 449 */       throw new XMLStreamException("Namespace URI cannot be null"); 
/* 450 */     if (prefix == null)
/* 451 */       throw new XMLStreamException("Prefix cannot be null"); 
/* 452 */     writeAttributeOrNamespace(prefix, namespaceURI, localName, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeNamespace(CharSequence prefix, CharSequence namespaceURI) throws XMLStreamException {
/*     */     CharArray charArray;
/* 458 */     if (prefix == null || prefix.length() == 0 || this._namespaces._xmlns.equals(prefix))
/*     */     {
/* 460 */       charArray = this._namespaces._defaultNsPrefix;
/*     */     }
/* 462 */     if (!this._isElementOpen)
/* 463 */       throw new IllegalStateException("No open start element"); 
/* 464 */     this._namespaces.setPrefix((CharSequence)charArray, (namespaceURI == null) ? (CharSequence)this._namespaces._nullNsURI : namespaceURI, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeDefaultNamespace(CharSequence namespaceURI) throws XMLStreamException {
/* 472 */     writeNamespace((CharSequence)this._namespaces._defaultNsPrefix, namespaceURI);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeComment(CharSequence data) throws XMLStreamException {
/* 477 */     if (this._isElementOpen)
/* 478 */       closeOpenTag(); 
/* 479 */     writeNoEscape("<!--");
/* 480 */     if (data != null) {
/* 481 */       writeNoEscape(data);
/*     */     }
/* 483 */     writeNoEscape("-->");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeProcessingInstruction(CharSequence target) throws XMLStreamException {
/* 489 */     writeProcessingInstruction(target, (CharSequence)this._noChar);
/*     */   }
/*     */   
/* 492 */   XMLStreamWriterImpl(XMLOutputFactoryImpl factory) { this._noChar = new CharArray("");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 589 */     this._tmpCharArray = new CharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 780 */     this._autoPrefix = new TextBuilder(); this._factory = factory; for (int i = 0; i < this._qNames.length;) this._qNames[i++] = new TextBuilder();  } public void writeProcessingInstruction(CharSequence target, CharSequence data) throws XMLStreamException { if (target == null) throw new XMLStreamException("Target cannot be null");  if (data == null) throw new XMLStreamException("Data cannot be null");  if (this._isElementOpen) closeOpenTag();  writeNoEscape("<?"); writeNoEscape(target); write(' '); writeNoEscape(data); writeNoEscape(" ?>"); } public void writeCData(CharSequence data) throws XMLStreamException { if (data == null) throw new XMLStreamException("Data cannot be null");  if (this._isElementOpen) closeOpenTag();  writeNoEscape("<![CDATA["); writeNoEscape(data); writeNoEscape("]]>"); } public void writeDTD(CharSequence dtd) throws XMLStreamException { if (dtd == null) throw new XMLStreamException("DTD cannot be null");  if (this._nesting > 0) throw new XMLStreamException("DOCTYPE declaration (DTD) when not in document root (prolog)");  writeNoEscape(dtd); } public void writeEntityRef(CharSequence name) throws XMLStreamException { write('&'); writeNoEscape(name); write(';'); } public void writeStartDocument() throws XMLStreamException { writeStartDocument(null, null); } public void writeStartDocument(CharSequence version) throws XMLStreamException { writeStartDocument(null, version); }
/*     */   public void writeStartDocument(CharSequence encoding, CharSequence version) throws XMLStreamException { if (this._nesting > 0) throw new XMLStreamException("Not in document root");  writeNoEscape("<?xml version=\""); if (version != null) { writeNoEscape(version); write('"'); } else { writeNoEscape("1.0\""); }  if (encoding != null) { writeNoEscape(" encoding=\""); writeNoEscape(encoding); write('"'); } else if (this._encoding != null) { writeNoEscape(" encoding=\""); writeNoEscape(this._encoding); write('"'); }  writeNoEscape(" ?>"); }
/*     */   public void writeCharacters(CharSequence text) throws XMLStreamException { if (this._isElementOpen) closeOpenTag();  if (text == null) return;  writeEscape(text); }
/*     */   public void writeCharacters(char[] text, int start, int length) throws XMLStreamException { this._tmpCharArray.setArray(text, start, length); writeCharacters((CharSequence)this._tmpCharArray); }
/* 784 */   private void resizeElemStack() { int oldLength = this._qNames.length;
/* 785 */     int newLength = oldLength * 2;
/*     */ 
/*     */     
/* 788 */     TextBuilder[] tmp = new TextBuilder[newLength];
/* 789 */     System.arraycopy(this._qNames, 0, tmp, 0, oldLength);
/* 790 */     this._qNames = tmp;
/* 791 */     for (int i = oldLength; i < newLength; i++)
/* 792 */       this._qNames[i] = new TextBuilder();  }
/*     */   public CharSequence getPrefix(CharSequence uri) throws XMLStreamException { return (CharSequence)this._namespaces.getPrefix(uri); }
/*     */   public void setPrefix(CharSequence prefix, CharSequence uri) throws XMLStreamException { this._namespaces.setPrefix(prefix, (uri == null) ? (CharSequence)this._namespaces._nullNsURI : uri, false); }
/*     */   public void setDefaultNamespace(CharSequence uri) throws XMLStreamException { setPrefix((CharSequence)this._namespaces._defaultNsPrefix, uri); }
/*     */   public Object getProperty(String name) throws IllegalArgumentException { if (name.equals("javolution.xml.stream.isRepairingNamespaces")) return new Boolean(this._isRepairingNamespaces);  if (name.equals("javolution.xml.stream.repairingPrefix")) return this._repairingPrefix;  if (name.equals("javolution.xml.stream.automaticEmptyElements"))
/*     */       return new Boolean(this._automaticEmptyElements);  if (name.equals("javolution.xml.stream.noEmptyElementTag"))
/*     */       return new Boolean(this._noEmptyElementTag);  if (name.equals("javolution.xml.stream.indentation"))
/*     */       return this._indentation;  if (name.equals("javolution.xml.stream.lineSeparator"))
/* 800 */       return this._lineSeparator;  throw new IllegalArgumentException("Property: " + name + " not supported"); } private final void writeNoEscape(String str) throws XMLStreamException { write(str, 0, str.length(), false); }
/*     */   private void writeNewElement(CharSequence prefix, CharSequence localName, CharSequence namespaceURI) throws XMLStreamException { if (this._isElementOpen) closeOpenTag();  if (this._indentation != null) { writeNoEscape(this._lineSeparator); this._indentationLevel = this._nesting; for (int i = 0; i < this._indentationLevel; i++) writeNoEscape(this._indentation);  }  write('<'); this._isElementOpen = true; if (++this._nesting >= this._qNames.length) resizeElemStack();  this._namespaces.push(); TextBuilder qName = this._qNames[this._nesting].clear(); if (namespaceURI != null && !this._namespaces._defaultNamespace.equals(namespaceURI)) { if (this._isRepairingNamespaces) { prefix = getRepairedPrefix(prefix, namespaceURI); } else if (prefix == null) { prefix = getPrefix(namespaceURI); if (prefix == null) throw new XMLStreamException("URI: " + namespaceURI + " not bound and repairing namespaces disabled");  }  if (prefix.length() > 0) { qName.append(prefix); qName.append(':'); }  }  qName.append(localName); writeNoEscape(qName); }
/*     */   private void writeAttributeOrNamespace(CharSequence prefix, CharSequence namespaceURI, CharSequence localName, CharSequence value) throws XMLStreamException { if (!this._isElementOpen) throw new IllegalStateException("No open start element");  write(' '); if (namespaceURI != null && !this._namespaces._defaultNamespace.equals(namespaceURI)) { if (this._isRepairingNamespaces) { prefix = getRepairedPrefix(prefix, namespaceURI); } else if (prefix == null) { prefix = getPrefix(namespaceURI); if (prefix == null) throw new XMLStreamException("URI: " + namespaceURI + " not bound and repairing namespaces disabled");  }  if (prefix.length() > 0) { writeNoEscape(prefix); write(':'); }  }  writeNoEscape(localName); write('='); write('"'); this._isAttributeValue = true; writeEscape(value); this._isAttributeValue = false; write('"'); }
/*     */   private void closeOpenTag() throws XMLStreamException { writeNamespaces(); this._isElementOpen = false; if (this._isEmptyElement) { if (this._noEmptyElementTag) { write('<'); write('/'); writeNoEscape(this._qNames[this._nesting]); write('>'); } else { write('/'); write('>'); }  this._nesting--; this._namespaces.pop(); this._isEmptyElement = false; } else { write('>'); }  }
/* 804 */   private void writeNamespaces() throws XMLStreamException { int i0 = (this._nesting > 1) ? this._namespaces._namespacesCount[this._nesting - 2] : 3; int i1 = this._namespaces._namespacesCount[this._nesting - 1]; int i2 = this._namespaces._namespacesCount[this._nesting]; for (int i = i0; i < i2; i++) { if ((this._isRepairingNamespaces && i < i1 && !this._namespaces._prefixesWritten[i]) || (i >= i1 && this._namespaces._prefixesWritten[i])) { if (this._isRepairingNamespaces) { CharArray prefix = this._namespaces.getPrefix((CharSequence)this._namespaces._namespaces[i], i); if (this._namespaces._prefixes[i].equals(prefix)) continue;  }  if (this._namespaces._prefixes[i].length() == 0) { writeAttributeOrNamespace(null, null, (CharSequence)this._namespaces._xmlns, (CharSequence)this._namespaces._namespaces[i]); } else { writeAttributeOrNamespace((CharSequence)this._namespaces._xmlns, (CharSequence)this._namespaces._xmlnsURI, (CharSequence)this._namespaces._prefixes[i], (CharSequence)this._namespaces._namespaces[i]); }  }  continue; }  } private CharSequence getRepairedPrefix(CharSequence prefix, CharSequence namespaceURI) throws XMLStreamException { TextBuilder textBuilder; CharArray prefixForURI = this._namespaces.getPrefix(namespaceURI); if (prefixForURI != null && (prefix == null || prefixForURI.equals(prefix))) return (CharSequence)prefixForURI;  if (prefix == null || prefix.length() == 0) textBuilder = this._autoPrefix.clear().append(this._repairingPrefix).append(this._autoNSCount++);  this._namespaces.setPrefix((CharSequence)textBuilder, namespaceURI, true); return (CharSequence)textBuilder; } private final void writeNoEscape(TextBuilder tb) throws XMLStreamException { write(tb, 0, tb.length(), false); }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void writeNoEscape(CharSequence csq) throws XMLStreamException {
/* 809 */     write(csq, 0, csq.length(), false);
/*     */   }
/*     */   
/*     */   private final void writeEscape(CharSequence csq) throws XMLStreamException {
/* 813 */     write(csq, 0, csq.length(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   private final void write(Object csq, int start, int length, boolean escapeMarkup) throws XMLStreamException {
/* 818 */     if (this._index + length <= 2048) {
/* 819 */       if (csq instanceof String) {
/* 820 */         ((String)csq).getChars(start, start + length, this._buffer, this._index);
/* 821 */       } else if (csq instanceof Text) {
/* 822 */         ((Text)csq).getChars(start, start + length, this._buffer, this._index);
/*     */       }
/* 824 */       else if (csq instanceof TextBuilder) {
/* 825 */         ((TextBuilder)csq).getChars(start, start + length, this._buffer, this._index);
/*     */       }
/* 827 */       else if (csq instanceof CharArray) {
/* 828 */         ((CharArray)csq).getChars(start, start + length, this._buffer, this._index);
/*     */       } else {
/*     */         
/* 831 */         getChars((CharSequence)csq, start, start + length, this._buffer, this._index);
/*     */       } 
/*     */       
/* 834 */       if (escapeMarkup) {
/* 835 */         int end = this._index + length;
/* 836 */         for (int i = this._index; i < end; ) {
/* 837 */           char c = this._buffer[i];
/* 838 */           if (c >= '?' || !isEscaped(c)) {
/*     */             i++; continue;
/*     */           } 
/* 841 */           this._index = i;
/* 842 */           flushBuffer();
/* 843 */           writeDirectEscapedCharacters(this._buffer, i, end);
/*     */           return;
/*     */         } 
/*     */       } 
/* 847 */       this._index += length;
/*     */     
/*     */     }
/* 850 */     else if (length <= 2048) {
/* 851 */       flushBuffer();
/* 852 */       write(csq, start, length, escapeMarkup);
/*     */     } else {
/* 854 */       int half = length >> 1;
/* 855 */       write(csq, start, half, escapeMarkup);
/* 856 */       write(csq, start + half, length - half, escapeMarkup);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getChars(CharSequence csq, int start, int end, char[] dest, int destPos) {
/* 863 */     for (int i = start, j = destPos; i < end;) {
/* 864 */       dest[j++] = csq.charAt(i++);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void writeDirectEscapedCharacters(char[] chars, int start, int end) throws XMLStreamException {
/*     */     try {
/* 872 */       int blockStart = start;
/* 873 */       for (int i = start; i < end; ) {
/* 874 */         char c = chars[i++];
/* 875 */         if (c >= '?' || !isEscaped(c)) {
/*     */           continue;
/*     */         }
/* 878 */         int j = i - blockStart - 1;
/* 879 */         if (j > 0) {
/* 880 */           this._writer.write(this._buffer, blockStart, j);
/*     */         }
/* 882 */         blockStart = i;
/* 883 */         switch (c) {
/*     */           case '<':
/* 885 */             this._writer.write("&lt;");
/*     */             continue;
/*     */           case '>':
/* 888 */             this._writer.write("&gt;");
/*     */             continue;
/*     */           case '\'':
/* 891 */             this._writer.write("&apos;");
/*     */             continue;
/*     */           case '"':
/* 894 */             this._writer.write("&quot;");
/*     */             continue;
/*     */           case '&':
/* 897 */             this._writer.write("&amp;");
/*     */             continue;
/*     */         } 
/* 900 */         this._writer.write("&#");
/* 901 */         this._writer.write((char)(48 + c / 10));
/* 902 */         this._writer.write((char)(48 + c % 10));
/* 903 */         this._writer.write(59);
/*     */       } 
/*     */ 
/*     */       
/* 907 */       int blockLength = end - blockStart;
/* 908 */       if (blockLength > 0) {
/* 909 */         this._writer.write(this._buffer, blockStart, blockLength);
/*     */       }
/* 911 */     } catch (IOException e) {
/* 912 */       throw new XMLStreamException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isEscaped(char c) {
/* 917 */     return ((c < ' ' && this._isAttributeValue) || (c == '"' && this._isAttributeValue) || c == '<' || c == '>' || c == '&');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private final void write(char c) throws XMLStreamException {
/* 923 */     if (this._index == 2048) {
/* 924 */       flushBuffer();
/*     */     }
/* 926 */     this._buffer[this._index++] = c;
/*     */   }
/*     */   
/*     */   private void flushBuffer() throws XMLStreamException {
/*     */     try {
/* 931 */       this._writer.write(this._buffer, 0, this._index);
/* 932 */     } catch (IOException e) {
/* 933 */       throw new XMLStreamException(e);
/*     */     } finally {
/* 935 */       this._index = 0;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/XMLStreamWriterImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */