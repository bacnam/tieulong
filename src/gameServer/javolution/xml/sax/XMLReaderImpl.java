/*     */ package javolution.xml.sax;
/*     */ 
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.xml.internal.stream.XMLStreamReaderImpl;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.DTDHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXNotRecognizedException;
/*     */ import org.xml.sax.SAXNotSupportedException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLReaderImpl
/*     */   implements XMLReader
/*     */ {
/*  53 */   private static DefaultHandler DEFAULT_HANDLER = new DefaultHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ContentHandler _contentHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ErrorHandler _errorHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  68 */   private final XMLStreamReaderImpl _xmlReader = new XMLStreamReaderImpl();
/*     */   
/*     */   private EntityResolver _entityResolver;
/*     */   
/*     */   private DTDHandler _dtdHandler;
/*     */   
/*     */   public XMLReaderImpl() {
/*  75 */     setContentHandler(DEFAULT_HANDLER);
/*  76 */     setErrorHandler(DEFAULT_HANDLER);
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
/*     */   public void parse(InputStream in) throws IOException, SAXException {
/*     */     try {
/*  92 */       this._xmlReader.setInput(in);
/*  93 */       parseAll();
/*  94 */     } catch (XMLStreamException e) {
/*  95 */       if (e.getNestedException() instanceof IOException)
/*  96 */         throw (IOException)e.getNestedException(); 
/*  97 */       throw new SAXException(e.getMessage());
/*     */     } finally {
/*  99 */       this._xmlReader.reset();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(InputStream in, String encoding) throws IOException, SAXException {
/*     */     try {
/* 117 */       this._xmlReader.setInput(in, encoding);
/* 118 */       parseAll();
/* 119 */     } catch (XMLStreamException e) {
/* 120 */       if (e.getNestedException() instanceof IOException)
/* 121 */         throw (IOException)e.getNestedException(); 
/* 122 */       throw new SAXException(e.getMessage());
/*     */     } finally {
/* 124 */       this._xmlReader.reset();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void parse(Reader reader) throws IOException, SAXException {
/*     */     try {
/* 142 */       this._xmlReader.setInput(reader);
/* 143 */       parseAll();
/* 144 */     } catch (XMLStreamException e) {
/* 145 */       if (e.getNestedException() instanceof IOException)
/* 146 */         throw (IOException)e.getNestedException(); 
/* 147 */       throw new SAXException(e.getMessage());
/*     */     } finally {
/* 149 */       this._xmlReader.reset();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(InputSource input) throws IOException, SAXException {
/* 155 */     Reader reader = input.getCharacterStream();
/* 156 */     if (reader != null) {
/* 157 */       parse(reader);
/*     */     } else {
/* 159 */       InputStream inStream = input.getByteStream();
/* 160 */       if (inStream != null) {
/* 161 */         parse(inStream, input.getEncoding());
/*     */       } else {
/* 163 */         parse(input.getSystemId());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void parse(String systemId) throws IOException, SAXException {
/*     */     InputStream inputStream;
/*     */     try {
/* 172 */       URL url = new URL(systemId);
/* 173 */       inputStream = url.openStream();
/* 174 */     } catch (Exception urlException) {
/*     */       try {
/* 176 */         inputStream = new FileInputStream(systemId);
/* 177 */       } catch (Exception fileException) {
/* 178 */         throw new UnsupportedOperationException("Cannot parse " + systemId);
/*     */       } 
/*     */     } 
/*     */     
/* 182 */     parse(inputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContentHandler(ContentHandler handler) {
/* 187 */     if (handler != null) {
/* 188 */       this._contentHandler = handler;
/*     */     } else {
/* 190 */       throw new NullPointerException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ContentHandler getContentHandler() {
/* 196 */     return (this._contentHandler == DEFAULT_HANDLER) ? null : this._contentHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setErrorHandler(ErrorHandler handler) {
/* 201 */     if (handler != null) {
/* 202 */       this._errorHandler = handler;
/*     */     } else {
/* 204 */       throw new NullPointerException();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ErrorHandler getErrorHandler() {
/* 210 */     return (this._errorHandler == DEFAULT_HANDLER) ? null : this._errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFeature(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 216 */     if (name.equals("http://xml.org/sax/features/namespaces"))
/* 217 */       return true; 
/* 218 */     if (name.equals("http://xml.org/sax/features/namespace-prefixes"))
/*     */     {
/* 220 */       return true;
/*     */     }
/* 222 */     throw new SAXNotRecognizedException("Feature " + name + " not recognized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 229 */     if (name.equals("http://xml.org/sax/features/namespaces") || name.equals("http://xml.org/sax/features/namespace-prefixes")) {
/*     */       return;
/*     */     }
/*     */     
/* 233 */     throw new SAXNotRecognizedException("Feature " + name + " not recognized");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 240 */     throw new SAXNotRecognizedException("Property " + name + " not recognized");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
/* 246 */     throw new SAXNotRecognizedException("Property " + name + " not recognized");
/*     */   }
/*     */ 
/*     */   
/*     */   public void setEntityResolver(EntityResolver resolver) {
/* 251 */     this._entityResolver = resolver;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityResolver getEntityResolver() {
/* 257 */     return this._entityResolver;
/*     */   }
/*     */   
/*     */   public void setDTDHandler(DTDHandler handler) {
/* 261 */     this._dtdHandler = handler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public DTDHandler getDTDHandler() {
/* 267 */     return this._dtdHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 272 */     setContentHandler(DEFAULT_HANDLER);
/* 273 */     setErrorHandler(DEFAULT_HANDLER);
/* 274 */     this._xmlReader.reset();
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
/*     */   private void parseAll() throws XMLStreamException, SAXException {
/* 286 */     int eventType = this._xmlReader.getEventType();
/* 287 */     if (eventType != 7)
/* 288 */       throw new SAXException("Currently parsing"); 
/* 289 */     this._contentHandler.startDocument();
/*     */     
/* 291 */     boolean doContinue = true;
/* 292 */     while (doContinue) {
/*     */       CharArray uri; CharArray localName; CharArray qName; CharArray text; int i; int count;
/* 294 */       switch (this._xmlReader.next()) {
/*     */ 
/*     */         
/*     */         case 1:
/* 298 */           for (i = 0, count = this._xmlReader.getNamespaceCount(); i < count; i++) {
/* 299 */             CharArray prefix = this._xmlReader.getNamespacePrefix(i);
/* 300 */             prefix = (prefix == null) ? NO_CHAR : prefix;
/* 301 */             CharArray charArray1 = this._xmlReader.getNamespaceURI(i);
/* 302 */             this._contentHandler.startPrefixMapping(prefix, charArray1);
/*     */           } 
/*     */ 
/*     */           
/* 306 */           uri = this._xmlReader.getNamespaceURI();
/* 307 */           uri = (uri == null) ? NO_CHAR : uri;
/* 308 */           localName = this._xmlReader.getLocalName();
/* 309 */           qName = this._xmlReader.getQName();
/* 310 */           this._contentHandler.startElement(uri, localName, qName, this._xmlReader.getAttributes());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 317 */           uri = this._xmlReader.getNamespaceURI();
/* 318 */           uri = (uri == null) ? NO_CHAR : uri;
/* 319 */           localName = this._xmlReader.getLocalName();
/* 320 */           qName = this._xmlReader.getQName();
/* 321 */           this._contentHandler.endElement(uri, localName, qName);
/*     */ 
/*     */           
/* 324 */           for (i = 0, count = this._xmlReader.getNamespaceCount(); i < count; i++) {
/* 325 */             CharArray prefix = this._xmlReader.getNamespacePrefix(i);
/* 326 */             prefix = (prefix == null) ? NO_CHAR : prefix;
/* 327 */             this._contentHandler.endPrefixMapping(prefix);
/*     */           } 
/*     */ 
/*     */         
/*     */         case 4:
/*     */         case 12:
/* 333 */           text = this._xmlReader.getText();
/* 334 */           this._contentHandler.characters(text.array(), text.offset(), text.length());
/*     */ 
/*     */ 
/*     */         
/*     */         case 6:
/* 339 */           text = this._xmlReader.getText();
/* 340 */           this._contentHandler.ignorableWhitespace(text.array(), text.offset(), text.length());
/*     */ 
/*     */ 
/*     */         
/*     */         case 3:
/* 345 */           this._contentHandler.processingInstruction(this._xmlReader.getPITarget(), this._xmlReader.getPIData());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         case 8:
/* 354 */           doContinue = false;
/* 355 */           this._xmlReader.close();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 362 */   private static final CharArray NO_CHAR = new CharArray("");
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/sax/XMLReaderImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */