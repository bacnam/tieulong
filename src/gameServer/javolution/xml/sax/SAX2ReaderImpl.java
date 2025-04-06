/*     */ package javolution.xml.sax;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.text.Text;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SAX2ReaderImpl
/*     */   implements XMLReader
/*     */ {
/*  48 */   private static Sax2DefaultHandler DEFAULT_HANDLER = new Sax2DefaultHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   private final XMLReaderImpl _parser = new XMLReaderImpl();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   private final Proxy _proxy = new Proxy();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  68 */     return this._parser.getFeature(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  74 */     this._parser.setFeature(name, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  80 */     return this._parser.getProperty(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
/*  86 */     this._parser.setProperty(name, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {
/*  91 */     this._parser.setEntityResolver(resolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/*  96 */     return this._parser.getEntityResolver();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDTDHandler(DTDHandler handler) {
/* 101 */     this._parser.setDTDHandler(handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public DTDHandler getDTDHandler() {
/* 106 */     return this._parser.getDTDHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/* 111 */     if (handler != null) {
/* 112 */       this._proxy._sax2Handler = handler;
/* 113 */       this._parser.setContentHandler(this._proxy);
/*     */     } else {
/* 115 */       throw new NullPointerException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ContentHandler getContentHandler() {
/* 121 */     return (this._proxy._sax2Handler == DEFAULT_HANDLER) ? null : this._proxy._sax2Handler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler handler) {
/* 127 */     this._parser.setErrorHandler(handler);
/*     */   }
/*     */ 
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 132 */     return this._parser.getErrorHandler();
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(InputSource input) throws IOException, SAXException {
/*     */     try {
/* 138 */       this._parser.parse(input);
/*     */     } finally {
/* 140 */       this._parser.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws IOException, SAXException {
/*     */     try {
/* 147 */       this._parser.parse(systemId);
/*     */     } finally {
/* 149 */       this._parser.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 155 */     this._parser.reset();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final class Proxy
/*     */     implements ContentHandler, Attributes
/*     */   {
/* 167 */     private ContentHandler _sax2Handler = SAX2ReaderImpl.DEFAULT_HANDLER;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Attributes _attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {
/* 182 */       this._sax2Handler.setDocumentLocator(locator);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startDocument() throws SAXException {
/* 187 */       this._sax2Handler.startDocument();
/*     */     }
/*     */ 
/*     */     
/*     */     public void endDocument() throws SAXException {
/* 192 */       this._sax2Handler.endDocument();
/* 193 */       this._sax2Handler = SAX2ReaderImpl.DEFAULT_HANDLER;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startPrefixMapping(CharArray prefix, CharArray uri) throws SAXException {
/* 199 */       this._sax2Handler.startPrefixMapping(prefix.toString(), uri.toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public void endPrefixMapping(CharArray prefix) throws SAXException {
/* 204 */       this._sax2Handler.endPrefixMapping(prefix.toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void startElement(CharArray namespaceURI, CharArray localName, CharArray qName, Attributes atts) throws SAXException {
/* 211 */       this._attributes = atts;
/* 212 */       this._sax2Handler.startElement(namespaceURI.toString(), localName.toString(), qName.toString(), this);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void endElement(CharArray namespaceURI, CharArray localName, CharArray qName) throws SAXException {
/* 219 */       this._sax2Handler.endElement(namespaceURI.toString(), localName.toString(), qName.toString());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void characters(char[] ch, int start, int length) throws SAXException {
/* 226 */       this._sax2Handler.characters(ch, start, length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
/* 232 */       this._sax2Handler.ignorableWhitespace(ch, start, length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void processingInstruction(CharArray target, CharArray data) throws SAXException {
/* 238 */       this._sax2Handler.processingInstruction(target.toString(), data.toString());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void skippedEntity(CharArray name) throws SAXException {
/* 244 */       this._sax2Handler.skippedEntity(name.toString());
/*     */     }
/*     */ 
/*     */     
/*     */     public int getLength() {
/* 249 */       return (this._attributes != null) ? this._attributes.getLength() : 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getURI(int index) {
/* 254 */       CharArray charArray = (this._attributes != null) ? this._attributes.getURI(index) : null;
/*     */       
/* 256 */       return (charArray != null) ? charArray.toString() : "";
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLocalName(int index) {
/* 261 */       CharArray charArray = (this._attributes != null) ? this._attributes.getLocalName(index) : null;
/*     */       
/* 263 */       return (charArray != null) ? charArray.toString() : "";
/*     */     }
/*     */ 
/*     */     
/*     */     public String getQName(int index) {
/* 268 */       CharArray charArray = (this._attributes != null) ? this._attributes.getQName(index) : null;
/*     */       
/* 270 */       return (charArray != null) ? charArray.toString() : "";
/*     */     }
/*     */ 
/*     */     
/*     */     public String getType(int index) {
/* 275 */       return (this._attributes != null) ? this._attributes.getType(index).toString() : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue(int index) {
/* 281 */       CharArray charArray = (this._attributes != null) ? this._attributes.getValue(index) : null;
/*     */       
/* 283 */       return (charArray != null) ? charArray.toString() : null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getIndex(String uri, String localName) {
/* 288 */       return (uri != null && localName != null && this._attributes != null) ? this._attributes.getIndex(SAX2ReaderImpl.toCharSequence(uri), SAX2ReaderImpl.toCharSequence(localName)) : -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getIndex(String qName) {
/* 295 */       return (qName != null && this._attributes != null) ? this._attributes.getIndex(SAX2ReaderImpl.toCharSequence(qName)) : -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getType(String uri, String localName) {
/* 301 */       return (uri != null && localName != null && this._attributes != null) ? this._attributes.getType(SAX2ReaderImpl.toCharSequence(uri), SAX2ReaderImpl.toCharSequence(localName)).toString() : null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getType(String qName) {
/* 308 */       return (qName != null && this._attributes != null) ? this._attributes.getType(SAX2ReaderImpl.toCharSequence(qName)).toString() : null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue(String uri, String localName) {
/* 314 */       return (uri != null && localName != null && this._attributes != null && this._attributes.getValue(SAX2ReaderImpl.toCharSequence(uri), SAX2ReaderImpl.toCharSequence(localName)) != null) ? this._attributes.getValue(SAX2ReaderImpl.toCharSequence(uri), SAX2ReaderImpl.toCharSequence(localName)).toString() : null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getValue(String qName) {
/* 325 */       return (qName != null && this._attributes != null) ? this._attributes.getValue(SAX2ReaderImpl.toCharSequence(qName)).toString() : null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class Sax2DefaultHandler
/*     */     implements EntityResolver, DTDHandler, ContentHandler, ErrorHandler
/*     */   {
/*     */     private Sax2DefaultHandler() {}
/*     */     
/*     */     public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
/* 335 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void notationDecl(String name, String publicId, String systemId) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void setDocumentLocator(Locator locator) {}
/*     */ 
/*     */     
/*     */     public void startDocument() throws SAXException {}
/*     */ 
/*     */     
/*     */     public void endDocument() throws SAXException {}
/*     */ 
/*     */     
/*     */     public void startPrefixMapping(String prefix, String uri) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void endPrefixMapping(String prefix) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {}
/*     */     
/*     */     public void endElement(String uri, String localName, String qName) throws SAXException {}
/*     */     
/*     */     public void characters(char[] ch, int start, int length) throws SAXException {}
/*     */     
/*     */     public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
/*     */     
/*     */     public void processingInstruction(String target, String data) throws SAXException {}
/*     */     
/*     */     public void skippedEntity(String name) throws SAXException {}
/*     */     
/*     */     public void warning(SAXParseException exception) throws SAXException {}
/*     */     
/*     */     public void error(SAXParseException exception) throws SAXException {}
/*     */     
/*     */     public void fatalError(SAXParseException exception) throws SAXException {
/* 377 */       throw exception;
/*     */     }
/*     */   }
/*     */   
/*     */   private static CharSequence toCharSequence(Object obj) {
/* 382 */     return (obj instanceof CharSequence) ? (CharSequence)obj : (CharSequence)Text.valueOf(obj);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/sax/SAX2ReaderImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */