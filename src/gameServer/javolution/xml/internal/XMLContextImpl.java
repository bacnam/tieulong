/*     */ package javolution.xml.internal;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javolution.context.AbstractContext;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.xml.DefaultXMLFormat;
/*     */ import javolution.xml.XMLContext;
/*     */ import javolution.xml.XMLFormat;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMLContextImpl
/*     */   extends XMLContext
/*     */ {
/*  30 */   private final FastMap<Class<?>, XMLFormat<?>> formats = new FastMap();
/*     */ 
/*     */   
/*     */   protected XMLContext inner() {
/*  34 */     XMLContextImpl ctx = new XMLContextImpl();
/*  35 */     ctx.formats.putAll(this.formats);
/*  36 */     return ctx;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> XMLFormat<T> searchFormat(Class<? extends T> type) {
/*  42 */     XMLFormat<T> xml = (XMLFormat)this.formats.get(type);
/*  43 */     if (xml != null)
/*  44 */       return xml; 
/*  45 */     DefaultXMLFormat format = type.<DefaultXMLFormat>getAnnotation(DefaultXMLFormat.class);
/*  46 */     if (format != null) {
/*  47 */       Class<? extends XMLFormat> formatClass = format.value();
/*     */       try {
/*  49 */         xml = formatClass.newInstance();
/*  50 */         synchronized (this.formats) {
/*     */           
/*  52 */           this.formats.put(type, xml);
/*     */         } 
/*  54 */         return xml;
/*  55 */       } catch (Throwable ex) {
/*  56 */         throw new RuntimeException(ex);
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     if (Map.class.isAssignableFrom(type))
/*  61 */       return MAP_XML; 
/*  62 */     if (Collection.class.isAssignableFrom(type))
/*  63 */       return COLLECTION_XML; 
/*  64 */     return OBJECT_XML;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> void setFormat(Class<? extends T> type, XMLFormat<T> format) {
/*  69 */     this.formats.put(type, format);
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
/*  80 */   private static final XMLFormat OBJECT_XML = (XMLFormat)new XMLFormat.Default();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   private static final XMLFormat COLLECTION_XML = new XMLFormat()
/*     */     {
/*     */       
/*     */       public void read(XMLFormat.InputElement xml, Object obj) throws XMLStreamException
/*     */       {
/*  94 */         Collection<Object> collection = (Collection)obj;
/*  95 */         while (xml.hasNext()) {
/*  96 */           collection.add(xml.getNext());
/*     */         }
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
/* 102 */         Collection collection = (Collection)obj;
/* 103 */         for (Iterator i = collection.iterator(); i.hasNext();) {
/* 104 */           xml.add(i.next());
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private static final XMLFormat MAP_XML = new XMLFormat()
/*     */     {
/*     */       
/*     */       public void read(XMLFormat.InputElement xml, Object obj) throws XMLStreamException
/*     */       {
/* 131 */         Map<Object, Object> map = (Map)obj;
/* 132 */         while (xml.hasNext()) {
/* 133 */           Object key = xml.get("Key");
/* 134 */           Object value = xml.get("Value");
/* 135 */           map.put(key, value);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public void write(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
/* 141 */         Map map = (Map)obj;
/* 142 */         for (Iterator<Map.Entry> it = map.entrySet().iterator(); it.hasNext(); ) {
/* 143 */           Map.Entry entry = it.next();
/* 144 */           xml.add(entry.getKey(), "Key");
/* 145 */           xml.add(entry.getValue(), "Value");
/*     */         } 
/*     */       }
/*     */     };
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/XMLContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */