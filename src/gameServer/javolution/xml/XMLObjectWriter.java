/*     */ package javolution.xml;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLObjectWriter
/*     */ {
/*  45 */   private final XMLFormat.OutputElement _xml = new XMLFormat.OutputElement();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Writer _writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private OutputStream _outputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLObjectWriter newInstance(OutputStream out) throws XMLStreamException {
/*  70 */     XMLObjectWriter writer = new XMLObjectWriter();
/*  71 */     writer.setOutput(out);
/*  72 */     return writer;
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
/*     */   public static XMLObjectWriter newInstance(OutputStream out, String encoding) throws XMLStreamException {
/*  84 */     XMLObjectWriter writer = new XMLObjectWriter();
/*  85 */     writer.setOutput(out, encoding);
/*  86 */     return writer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static XMLObjectWriter newInstance(Writer out) throws XMLStreamException {
/*  97 */     XMLObjectWriter writer = new XMLObjectWriter();
/*  98 */     writer.setOutput(out);
/*  99 */     return writer;
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
/*     */   public XMLStreamWriter getStreamWriter() {
/* 112 */     return (XMLStreamWriter)this._xml._writer;
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
/*     */   public XMLObjectWriter setOutput(OutputStream out) throws XMLStreamException {
/* 124 */     if (this._outputStream != null || this._writer != null)
/* 125 */       throw new IllegalStateException("Writer not closed or reset"); 
/* 126 */     this._xml._writer.setOutput(out);
/* 127 */     this._outputStream = out;
/* 128 */     this._xml._writer.writeStartDocument();
/* 129 */     return this;
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
/*     */   public XMLObjectWriter setOutput(OutputStream out, String encoding) throws XMLStreamException {
/* 142 */     if (this._outputStream != null || this._writer != null)
/* 143 */       throw new IllegalStateException("Writer not closed or reset"); 
/* 144 */     this._xml._writer.setOutput(out, encoding);
/* 145 */     this._outputStream = out;
/* 146 */     this._xml._writer.writeStartDocument();
/* 147 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLObjectWriter setOutput(Writer out) throws XMLStreamException {
/* 158 */     if (this._outputStream != null || this._writer != null)
/* 159 */       throw new IllegalStateException("Writer not closed or reset"); 
/* 160 */     this._xml._writer.setOutput(out);
/* 161 */     this._writer = out;
/* 162 */     this._xml._writer.writeStartDocument();
/* 163 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLObjectWriter setBinding(XMLBinding binding) {
/* 173 */     this._xml.setBinding(binding);
/* 174 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLObjectWriter setIndentation(String indentation) {
/* 185 */     this._xml._writer.setIndentation(indentation);
/* 186 */     return this;
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
/*     */   public XMLObjectWriter setReferenceResolver(XMLReferenceResolver referenceResolver) {
/* 198 */     this._xml.setReferenceResolver(referenceResolver);
/* 199 */     return this;
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
/*     */   public void write(Object obj) throws XMLStreamException {
/* 211 */     this._xml.add(obj);
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
/*     */   public void write(Object obj, String name) throws XMLStreamException {
/* 224 */     this._xml.add(obj, name);
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
/*     */   public void write(Object obj, String localName, String uri) throws XMLStreamException {
/* 240 */     this._xml.add(obj, localName, uri);
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
/*     */   public <T> void write(T obj, String name, Class<T> cls) throws XMLStreamException {
/* 254 */     this._xml.add(obj, name, cls);
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
/*     */   public <T> void write(T obj, String localName, String uri, Class<T> cls) throws XMLStreamException {
/* 269 */     this._xml.add(obj, localName, uri, cls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void flush() throws XMLStreamException {
/* 277 */     this._xml._writer.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() throws XMLStreamException {
/*     */     try {
/* 286 */       if (this._outputStream != null) {
/* 287 */         this._xml._writer.writeEndDocument();
/* 288 */         this._xml._writer.close();
/* 289 */         this._outputStream.close();
/* 290 */         reset();
/* 291 */       } else if (this._writer != null) {
/* 292 */         this._xml._writer.writeEndDocument();
/* 293 */         this._xml._writer.close();
/* 294 */         this._writer.close();
/* 295 */         reset();
/*     */       }
/*     */     
/* 298 */     } catch (IOException e) {
/* 299 */       throw new XMLStreamException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 307 */     this._xml.reset();
/* 308 */     this._outputStream = null;
/* 309 */     this._writer = null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/XMLObjectWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */