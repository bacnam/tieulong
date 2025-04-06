/*     */ package javolution.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ import javolution.xml.stream.XMLStreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLObjectReader
/*     */ {
/*  44 */   private final XMLFormat.InputElement _xml = new XMLFormat.InputElement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Reader _reader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InputStream _inputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLObjectReader newInstance(InputStream in) throws XMLStreamException {
/*  64 */     XMLObjectReader reader = new XMLObjectReader();
/*  65 */     reader.setInput(in);
/*  66 */     return reader;
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
/*     */   public static XMLObjectReader newInstance(InputStream in, String encoding) throws XMLStreamException {
/*  78 */     XMLObjectReader reader = new XMLObjectReader();
/*  79 */     reader.setInput(in, encoding);
/*  80 */     return reader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLObjectReader newInstance(Reader in) throws XMLStreamException {
/*  91 */     XMLObjectReader reader = new XMLObjectReader();
/*  92 */     reader.setInput(in);
/*  93 */     return reader;
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
/*     */   public XMLStreamReader getStreamReader() {
/* 108 */     return (XMLStreamReader)this._xml._reader;
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
/*     */   public XMLObjectReader setInput(InputStream in) throws XMLStreamException {
/* 120 */     if (this._inputStream != null || this._reader != null)
/* 121 */       throw new IllegalStateException("Reader not closed or reset"); 
/* 122 */     this._xml._reader.setInput(in);
/* 123 */     this._inputStream = in;
/* 124 */     return this;
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
/*     */   public XMLObjectReader setInput(InputStream in, String encoding) throws XMLStreamException {
/* 137 */     if (this._inputStream != null || this._reader != null)
/* 138 */       throw new IllegalStateException("Reader not closed or reset"); 
/* 139 */     this._xml._reader.setInput(in, encoding);
/* 140 */     this._inputStream = in;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLObjectReader setInput(Reader in) throws XMLStreamException {
/* 152 */     if (this._inputStream != null || this._reader != null)
/* 153 */       throw new IllegalStateException("Reader not closed or reset"); 
/* 154 */     this._xml._reader.setInput(in);
/* 155 */     this._reader = in;
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLObjectReader setBinding(XMLBinding binding) {
/* 166 */     this._xml.setBinding(binding);
/* 167 */     return this;
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
/*     */   public XMLObjectReader setReferenceResolver(XMLReferenceResolver referenceResolver) {
/* 179 */     this._xml.setReferenceResolver(referenceResolver);
/* 180 */     return this;
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
/*     */   public boolean hasNext() throws XMLStreamException {
/* 193 */     return this._xml.hasNext();
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
/*     */   public <T> T read() throws XMLStreamException {
/* 205 */     return this._xml.getNext();
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
/*     */   public <T> T read(String name) throws XMLStreamException {
/* 219 */     return this._xml.get(name);
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
/*     */   public <T> T read(String localName, String uri) throws XMLStreamException {
/* 234 */     return this._xml.get(localName, uri);
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
/*     */   public <T> T read(String name, Class<T> cls) throws XMLStreamException {
/* 247 */     return this._xml.get(name, cls);
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
/*     */   public <T> T read(String localName, String uri, Class<T> cls) throws XMLStreamException {
/* 262 */     return this._xml.get(localName, uri, cls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/*     */     try {
/* 271 */       if (this._inputStream != null) {
/* 272 */         this._inputStream.close();
/* 273 */         reset();
/* 274 */       } else if (this._reader != null) {
/* 275 */         this._reader.close();
/* 276 */         reset();
/*     */       } 
/* 278 */     } catch (IOException e) {
/* 279 */       throw new XMLStreamException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 287 */     this._xml.reset();
/* 288 */     this._reader = null;
/* 289 */     this._inputStream = null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/XMLObjectReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */