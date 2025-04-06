/*     */ package javolution.xml.internal.stream;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Map;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.xml.stream.XMLInputFactory;
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
/*     */ public final class XMLInputFactoryImpl
/*     */   implements XMLInputFactory
/*     */ {
/*  23 */   private Map<String, String> _entities = null;
/*  24 */   private FastTable<XMLStreamReaderImpl> _recycled = (new FastTable()).shared();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamReaderImpl createXMLStreamReader(InputStream stream) throws XMLStreamException {
/*  30 */     XMLStreamReaderImpl xmlReader = newReader();
/*  31 */     xmlReader.setInput(stream);
/*  32 */     return xmlReader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamReaderImpl createXMLStreamReader(InputStream stream, String encoding) throws XMLStreamException {
/*  38 */     XMLStreamReaderImpl xmlReader = newReader();
/*  39 */     xmlReader.setInput(stream, encoding);
/*  40 */     return xmlReader;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamReaderImpl createXMLStreamReader(Reader reader) throws XMLStreamException {
/*  46 */     XMLStreamReaderImpl xmlReader = newReader();
/*  47 */     xmlReader.setInput(reader);
/*  48 */     return xmlReader;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  53 */     if (name.equals("javolution.xml.stream.isCoalescing"))
/*  54 */       return Boolean.TRUE; 
/*  55 */     if (name.equals("javolution.xml.stream.entities")) {
/*  56 */       return this._entities;
/*     */     }
/*  58 */     throw new IllegalArgumentException("Property: " + name + " not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPropertySupported(String name) {
/*  65 */     return (name.equals("javolution.xml.stream.isCoalescing") || name.equals("javolution.xml.stream.entities"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws IllegalArgumentException {
/*  72 */     if (!name.equals("javolution.xml.stream.isCoalescing"))
/*     */     {
/*  74 */       if (name.equals("javolution.xml.stream.entities")) {
/*  75 */         this._entities = (Map<String, String>)value;
/*     */       } else {
/*  77 */         throw new IllegalArgumentException("Property: " + name + " not supported");
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void recycle(XMLStreamReaderImpl reader) {
/*  84 */     this._recycled.addLast(reader);
/*     */   }
/*     */   
/*     */   private XMLStreamReaderImpl newReader() {
/*  88 */     XMLStreamReaderImpl xmlReader = (XMLStreamReaderImpl)this._recycled.pollLast();
/*  89 */     if (xmlReader == null) xmlReader = new XMLStreamReaderImpl(this); 
/*  90 */     if (this._entities != null) {
/*  91 */       xmlReader.setEntities(this._entities);
/*     */     }
/*  93 */     return xmlReader;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLInputFactory clone() {
/*     */     try {
/*  99 */       XMLInputFactoryImpl clone = (XMLInputFactoryImpl)super.clone();
/* 100 */       clone._recycled = (new FastTable()).shared();
/* 101 */       return clone;
/* 102 */     } catch (CloneNotSupportedException e) {
/* 103 */       throw new Error();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/XMLInputFactoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */