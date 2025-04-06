/*      */ package javolution.xml;
/*      */ 
/*      */ import javolution.text.CharArray;
/*      */ import javolution.text.TextBuilder;
/*      */ import javolution.text.TextContext;
/*      */ import javolution.text.TextFormat;
/*      */ import javolution.xml.internal.stream.XMLStreamReaderImpl;
/*      */ import javolution.xml.internal.stream.XMLStreamWriterImpl;
/*      */ import javolution.xml.sax.Attributes;
/*      */ import javolution.xml.stream.XMLStreamException;
/*      */ import javolution.xml.stream.XMLStreamReader;
/*      */ import javolution.xml.stream.XMLStreamWriter;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class XMLFormat<T>
/*      */ {
/*      */   private static final String NULL = "Null";
/*      */   
/*      */   public boolean isReferenceable() {
/*  138 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public T newInstance(Class<? extends T> cls, InputElement xml) throws XMLStreamException {
/*      */     try {
/*  155 */       return cls.newInstance();
/*  156 */     } catch (InstantiationException e) {
/*  157 */       throw new XMLStreamException(e);
/*  158 */     } catch (IllegalAccessException e) {
/*  159 */       throw new XMLStreamException(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void write(T paramT, OutputElement paramOutputElement) throws XMLStreamException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void read(InputElement paramInputElement, T paramT) throws XMLStreamException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class InputElement
/*      */   {
/*  191 */     final XMLStreamReaderImpl _reader = new XMLStreamReaderImpl();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private XMLBinding _binding;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private XMLReferenceResolver _referenceResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean _isReaderAtNext;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     InputElement() {
/*  212 */       reset();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public XMLStreamReader getStreamReader() {
/*  222 */       return (XMLStreamReader)this._reader;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasNext() throws XMLStreamException {
/*  234 */       if (!this._isReaderAtNext) {
/*  235 */         this._isReaderAtNext = true;
/*  236 */         this._reader.nextTag();
/*      */       } 
/*  238 */       return (this._reader.getEventType() == 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T getNext() throws XMLStreamException {
/*  249 */       if (!hasNext()) {
/*  250 */         throw new XMLStreamException("No more element to read", this._reader.getLocation());
/*      */       }
/*      */ 
/*      */       
/*  254 */       if (this._reader.getLocalName().equals("Null")) {
/*  255 */         if (this._reader.next() != 2)
/*  256 */           throw new XMLStreamException("Non Empty Null Element"); 
/*  257 */         this._isReaderAtNext = false;
/*  258 */         return null;
/*      */       } 
/*      */       
/*  261 */       Object ref = readReference();
/*  262 */       if (ref != null) {
/*  263 */         return (T)ref;
/*      */       }
/*      */       
/*  266 */       Class<?> cls = this._binding.readClass((XMLStreamReader)this._reader, false);
/*  267 */       return readInstanceOf(cls);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T get(String name) throws XMLStreamException {
/*  278 */       if (!hasNext() || !this._reader.getLocalName().equals(name))
/*      */       {
/*  280 */         return null;
/*      */       }
/*  282 */       Object ref = readReference();
/*  283 */       if (ref != null) {
/*  284 */         return (T)ref;
/*      */       }
/*      */       
/*  287 */       Class<?> cls = this._binding.readClass((XMLStreamReader)this._reader, true);
/*  288 */       return readInstanceOf(cls);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T get(String localName, String uri) throws XMLStreamException {
/*  301 */       if (uri == null) {
/*  302 */         return get(localName);
/*      */       }
/*  304 */       if (!hasNext() || !this._reader.getLocalName().equals(localName) || !this._reader.getNamespaceURI().equals(uri))
/*      */       {
/*      */         
/*  307 */         return null;
/*      */       }
/*  309 */       Object ref = readReference();
/*  310 */       if (ref != null) {
/*  311 */         return (T)ref;
/*      */       }
/*      */       
/*  314 */       Class<?> cls = this._binding.readClass((XMLStreamReader)this._reader, true);
/*  315 */       return readInstanceOf(cls);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T get(String name, Class<T> cls) throws XMLStreamException {
/*  327 */       if (!hasNext() || !this._reader.getLocalName().equals(name))
/*      */       {
/*  329 */         return null;
/*      */       }
/*  331 */       Object ref = readReference();
/*  332 */       if (ref != null) {
/*  333 */         return (T)ref;
/*      */       }
/*  335 */       return readInstanceOf(cls);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T get(String localName, String uri, Class<T> cls) throws XMLStreamException {
/*  349 */       if (uri == null) {
/*  350 */         return get(localName, cls);
/*      */       }
/*  352 */       if (!hasNext() || !this._reader.getLocalName().equals(localName) || !this._reader.getNamespaceURI().equals(uri))
/*      */       {
/*      */         
/*  355 */         return null;
/*      */       }
/*  357 */       Object ref = readReference();
/*  358 */       if (ref != null) {
/*  359 */         return (T)ref;
/*      */       }
/*  361 */       return readInstanceOf(cls);
/*      */     }
/*      */ 
/*      */     
/*      */     private Object readReference() throws XMLStreamException {
/*  366 */       if (this._referenceResolver == null)
/*  367 */         return null; 
/*  368 */       Object ref = this._referenceResolver.readReference(this);
/*  369 */       if (ref == null)
/*  370 */         return null; 
/*  371 */       if (this._reader.next() != 2)
/*  372 */         throw new XMLStreamException("Non Empty Reference Element"); 
/*  373 */       this._isReaderAtNext = false;
/*  374 */       return ref;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private <T> T readInstanceOf(Class<?> cls) throws XMLStreamException {
/*  382 */       XMLFormat<?> xmlFormat = this._binding.getFormat(cls);
/*      */ 
/*      */       
/*  385 */       this._isReaderAtNext = false;
/*  386 */       Object obj = xmlFormat.newInstance(cls, this);
/*      */ 
/*      */       
/*  389 */       if (this._referenceResolver != null) {
/*  390 */         this._referenceResolver.createReference(obj, this);
/*      */       }
/*      */ 
/*      */       
/*  394 */       xmlFormat.read(this, obj);
/*  395 */       if (hasNext()) {
/*  396 */         throw new XMLStreamException("Incomplete element reading", this._reader.getLocation());
/*      */       }
/*  398 */       this._isReaderAtNext = false;
/*  399 */       return (T)obj;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CharArray getText() throws XMLStreamException {
/*  410 */       CharArray txt = this._reader.getElementText();
/*  411 */       this._isReaderAtNext = true;
/*  412 */       return txt;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Attributes getAttributes() throws XMLStreamException {
/*  421 */       if (this._isReaderAtNext) {
/*  422 */         throw new XMLStreamException("Attributes should be read before content");
/*      */       }
/*  424 */       return this._reader.getAttributes();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CharArray getAttribute(String name) throws XMLStreamException {
/*  435 */       if (this._isReaderAtNext) {
/*  436 */         throw new XMLStreamException("Attributes should be read before reading content");
/*      */       }
/*  438 */       return this._reader.getAttributeValue(null, name);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String getAttribute(String name, String defaultValue) throws XMLStreamException {
/*  451 */       CharArray value = getAttribute(name);
/*  452 */       return (value != null) ? value.toString() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean getAttribute(String name, boolean defaultValue) throws XMLStreamException {
/*  465 */       CharArray value = getAttribute(name);
/*  466 */       return (value != null) ? value.toBoolean() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public char getAttribute(String name, char defaultValue) throws XMLStreamException {
/*  479 */       CharArray value = getAttribute(name);
/*  480 */       if (value == null)
/*  481 */         return defaultValue; 
/*  482 */       if (value.length() != 1) {
/*  483 */         throw new XMLStreamException("Single character expected (read '" + value + "')");
/*      */       }
/*  485 */       return value.charAt(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte getAttribute(String name, byte defaultValue) throws XMLStreamException {
/*  499 */       CharArray value = getAttribute(name);
/*  500 */       return (value != null) ? (byte)value.toInt() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public short getAttribute(String name, short defaultValue) throws XMLStreamException {
/*  514 */       CharArray value = getAttribute(name);
/*  515 */       return (value != null) ? (short)value.toInt() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getAttribute(String name, int defaultValue) throws XMLStreamException {
/*  529 */       CharArray value = getAttribute(name);
/*  530 */       return (value != null) ? value.toInt() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long getAttribute(String name, long defaultValue) throws XMLStreamException {
/*  544 */       CharArray value = getAttribute(name);
/*  545 */       return (value != null) ? value.toLong() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public float getAttribute(String name, float defaultValue) throws XMLStreamException {
/*  558 */       CharArray value = getAttribute(name);
/*  559 */       return (value != null) ? value.toFloat() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public double getAttribute(String name, double defaultValue) throws XMLStreamException {
/*  572 */       CharArray value = getAttribute(name);
/*  573 */       return (value != null) ? value.toDouble() : defaultValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> T getAttribute(String name, T defaultValue) throws XMLStreamException {
/*  587 */       CharArray value = getAttribute(name);
/*  588 */       if (value == null) {
/*  589 */         return defaultValue;
/*      */       }
/*  591 */       Class<?> type = defaultValue.getClass();
/*  592 */       TextFormat<?> format = TextContext.getFormat(type);
/*  593 */       if (format == null) {
/*  594 */         throw new XMLStreamException("No TextFormat defined for " + type);
/*      */       }
/*  596 */       return (T)format.parse((CharSequence)value);
/*      */     }
/*      */ 
/*      */     
/*      */     void setBinding(XMLBinding xmlBinding) {
/*  601 */       this._binding = xmlBinding;
/*      */     }
/*      */ 
/*      */     
/*      */     void setReferenceResolver(XMLReferenceResolver xmlReferenceResolver) {
/*  606 */       this._referenceResolver = xmlReferenceResolver;
/*      */     }
/*      */ 
/*      */     
/*      */     void reset() {
/*  611 */       this._binding = XMLBinding.DEFAULT;
/*  612 */       this._isReaderAtNext = false;
/*  613 */       this._reader.reset();
/*  614 */       this._referenceResolver = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class OutputElement
/*      */   {
/*  627 */     final XMLStreamWriterImpl _writer = new XMLStreamWriterImpl();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private XMLBinding _binding;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private XMLReferenceResolver _referenceResolver;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private TextBuilder _tmpTextBuilder;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public XMLStreamWriter getStreamWriter() {
/*  653 */       return (XMLStreamWriter)this._writer;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void add(Object obj) throws XMLStreamException {
/*  663 */       if (obj == null) {
/*  664 */         this._writer.writeEmptyElement("Null");
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/*  669 */       Class<?> cls = obj.getClass();
/*  670 */       this._binding.writeClass(cls, (XMLStreamWriter)this._writer, false);
/*      */ 
/*      */       
/*  673 */       XMLFormat<Object> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
/*      */       
/*  675 */       if (xmlFormat.isReferenceable() && writeReference(obj)) {
/*      */         return;
/*      */       }
/*  678 */       xmlFormat.write(obj, this);
/*  679 */       this._writer.writeEndElement();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void add(Object obj, String name) throws XMLStreamException {
/*  692 */       if (obj == null) {
/*      */         return;
/*      */       }
/*      */       
/*  696 */       this._writer.writeStartElement(name);
/*      */ 
/*      */       
/*  699 */       Class<?> cls = obj.getClass();
/*  700 */       this._binding.writeClass(cls, (XMLStreamWriter)this._writer, true);
/*      */ 
/*      */       
/*  703 */       XMLFormat<Object> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
/*      */       
/*  705 */       if (xmlFormat.isReferenceable() && writeReference(obj)) {
/*      */         return;
/*      */       }
/*  708 */       xmlFormat.write(obj, this);
/*  709 */       this._writer.writeEndElement();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void add(Object obj, String localName, String uri) throws XMLStreamException {
/*  724 */       if (obj == null) {
/*      */         return;
/*      */       }
/*      */       
/*  728 */       this._writer.writeStartElement(uri, localName);
/*      */ 
/*      */       
/*  731 */       Class<?> cls = obj.getClass();
/*  732 */       this._binding.writeClass(cls, (XMLStreamWriter)this._writer, true);
/*      */ 
/*      */       
/*  735 */       XMLFormat<Object> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
/*      */       
/*  737 */       if (xmlFormat.isReferenceable() && writeReference(obj)) {
/*      */         return;
/*      */       }
/*  740 */       xmlFormat.write(obj, this);
/*  741 */       this._writer.writeEndElement();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> void add(T obj, String name, Class<T> cls) throws XMLStreamException {
/*  755 */       if (obj == null) {
/*      */         return;
/*      */       }
/*      */       
/*  759 */       this._writer.writeStartElement(name);
/*      */ 
/*      */       
/*  762 */       XMLFormat<T> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
/*  763 */       if (xmlFormat.isReferenceable() && writeReference(obj)) {
/*      */         return;
/*      */       }
/*  766 */       xmlFormat.write(obj, this);
/*  767 */       this._writer.writeEndElement();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public <T> void add(T obj, String localName, String uri, Class<T> cls) throws XMLStreamException {
/*  782 */       if (obj == null) {
/*      */         return;
/*      */       }
/*      */       
/*  786 */       this._writer.writeStartElement(uri, localName);
/*      */ 
/*      */       
/*  789 */       XMLFormat<T> xmlFormat = (XMLFormat)this._binding.getFormat(cls);
/*  790 */       if (xmlFormat.isReferenceable() && writeReference(obj)) {
/*      */         return;
/*      */       }
/*  793 */       xmlFormat.write(obj, this);
/*  794 */       this._writer.writeEndElement();
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean writeReference(Object obj) throws XMLStreamException {
/*  799 */       if (this._referenceResolver == null || !this._referenceResolver.writeReference(obj, this))
/*      */       {
/*  801 */         return false; } 
/*  802 */       this._writer.writeEndElement();
/*  803 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addText(CharSequence text) throws XMLStreamException {
/*  814 */       this._writer.writeCharacters(text);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addText(String text) throws XMLStreamException {
/*  824 */       this._writer.writeCharacters(text);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, CharSequence value) throws XMLStreamException {
/*  836 */       if (value == null)
/*      */         return; 
/*  838 */       this._writer.writeAttribute(name, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, String value) throws XMLStreamException {
/*  850 */       if (value == null)
/*      */         return; 
/*  852 */       this._writer.writeAttribute(name, value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, boolean value) throws XMLStreamException {
/*  863 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */     OutputElement() {
/*  866 */       this._tmpTextBuilder = new TextBuilder();
/*      */       reset();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, char value) throws XMLStreamException {
/*  876 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, byte value) throws XMLStreamException {
/*  888 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, short value) throws XMLStreamException {
/*  899 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, int value) throws XMLStreamException {
/*  910 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, long value) throws XMLStreamException {
/*  921 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, float value) throws XMLStreamException {
/*  932 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, double value) throws XMLStreamException {
/*  943 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void setAttribute(String name, Object value) throws XMLStreamException {
/*  956 */       if (value == null)
/*      */         return; 
/*  958 */       setAttribute(name, (CharSequence)this._tmpTextBuilder.clear().append(value.toString()));
/*      */     }
/*      */ 
/*      */     
/*      */     void setBinding(XMLBinding xmlBinding) {
/*  963 */       this._binding = xmlBinding;
/*      */     }
/*      */ 
/*      */     
/*      */     void setReferenceResolver(XMLReferenceResolver xmlReferenceResolver) {
/*  968 */       this._referenceResolver = xmlReferenceResolver;
/*      */     }
/*      */ 
/*      */     
/*      */     void reset() {
/*  973 */       this._binding = XMLBinding.DEFAULT;
/*  974 */       this._writer.reset();
/*  975 */       this._writer.setRepairingNamespaces(true);
/*  976 */       this._writer.setAutomaticEmptyElements(true);
/*  977 */       this._referenceResolver = null;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Default
/*      */     extends XMLFormat<Object>
/*      */   {
/*      */     public boolean isReferenceable() {
/*  995 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object newInstance(Class<?> cls, XMLFormat.InputElement xml) throws XMLStreamException {
/* 1002 */       TextFormat<?> format = TextContext.getFormat(cls);
/* 1003 */       if (format == null) {
/* 1004 */         throw new XMLStreamException("No TextFormat defined to parse instances of " + cls);
/*      */       }
/* 1006 */       CharArray value = xml.getAttribute("value");
/* 1007 */       if (value == null) {
/* 1008 */         throw new XMLStreamException("Missing value attribute (to be able to parse the instance of " + cls + ")");
/*      */       }
/*      */       
/* 1011 */       return format.parse((CharSequence)value);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void read(XMLFormat.InputElement xml, Object obj) throws XMLStreamException {}
/*      */ 
/*      */ 
/*      */     
/*      */     public void write(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
/* 1022 */       TextBuilder tmp = new TextBuilder();
/* 1023 */       TextFormat<?> tf = TextContext.getFormat(obj.getClass());
/* 1024 */       tf.format(obj, tmp);
/* 1025 */       xml.setAttribute("value", (CharSequence)tmp);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/XMLFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */