/*     */ package javolution.xml;
/*     */ 
/*     */ import javolution.util.FastMap;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ import javolution.xml.stream.XMLStreamReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLBinding
/*     */   implements XMLSerializable
/*     */ {
/* 105 */   static final XMLBinding DEFAULT = new XMLBinding();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   private QName _classAttribute = QName.valueOf("class");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private final FastMap<Class<?>, QName> _classToAlias = new FastMap();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private final FastMap<QName, Class<?>> _aliasToClass = new FastMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = 6611041662550083919L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAlias(Class<?> cls, QName qName) {
/* 134 */     this._classToAlias.put(cls, qName);
/* 135 */     this._aliasToClass.put(qName, cls);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setAlias(Class<?> cls, String alias) {
/* 146 */     setAlias(cls, QName.valueOf(alias));
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
/*     */   public void setClassAttribute(QName classAttribute) {
/* 158 */     this._classAttribute = classAttribute;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setClassAttribute(String name) {
/* 168 */     setClassAttribute((name == null) ? null : QName.valueOf(name));
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
/*     */   protected XMLFormat<?> getFormat(Class<?> forClass) throws XMLStreamException {
/* 181 */     return XMLContext.getFormat(forClass);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Class<?> readClass(XMLStreamReader reader, boolean useAttributes) throws XMLStreamException {
/*     */     try {
/*     */       QName classQName;
/* 207 */       if (useAttributes) {
/* 208 */         if (this._classAttribute == null) {
/* 209 */           throw new XMLStreamException("Binding has no class attribute defined, cannot retrieve class");
/*     */         }
/* 211 */         classQName = QName.valueOf((CharSequence)reader.getAttributeValue(this._classAttribute.getNamespaceURI(), this._classAttribute.getLocalName()));
/*     */ 
/*     */         
/* 214 */         if (classQName == null) {
/* 215 */           throw new XMLStreamException("Cannot retrieve class (class attribute not found)");
/*     */         }
/*     */       } else {
/* 218 */         classQName = QName.valueOf((CharSequence)reader.getNamespaceURI(), (CharSequence)reader.getLocalName());
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 223 */       Class<?> cls = (Class)this._aliasToClass.get(classQName);
/* 224 */       if (cls != null) {
/* 225 */         return cls;
/*     */       }
/*     */       
/* 228 */       cls = (Class)this._aliasToClass.get(QName.valueOf(classQName.getLocalName()));
/* 229 */       if (cls != null) {
/* 230 */         return cls;
/*     */       }
/*     */       
/* 233 */       cls = Class.forName(classQName.getLocalName().toString());
/* 234 */       if (cls == null) {
/* 235 */         throw new XMLStreamException("Class " + classQName.getLocalName() + " not found (see javolution.lang.Reflection to support additional class loader)");
/*     */       }
/*     */ 
/*     */       
/* 239 */       this._aliasToClass.put(classQName, cls);
/* 240 */       return cls;
/* 241 */     } catch (ClassNotFoundException ex) {
/* 242 */       throw new RuntimeException(ex);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeClass(Class<?> cls, XMLStreamWriter writer, boolean useAttributes) throws XMLStreamException {
/* 266 */     QName qName = (QName)this._classToAlias.get(cls);
/* 267 */     String name = (qName != null) ? qName.toString() : cls.getName();
/* 268 */     if (useAttributes) {
/* 269 */       if (this._classAttribute == null)
/*     */         return; 
/* 271 */       if (this._classAttribute.getNamespaceURI() == null) {
/* 272 */         writer.writeAttribute(this._classAttribute.getLocalName(), name);
/*     */       } else {
/* 274 */         writer.writeAttribute(this._classAttribute.getNamespaceURI(), this._classAttribute.getLocalName(), name);
/*     */       }
/*     */     
/*     */     }
/* 278 */     else if (qName != null) {
/* 279 */       if (qName.getNamespaceURI() == null) {
/* 280 */         writer.writeStartElement(qName.getLocalName());
/*     */       } else {
/* 282 */         writer.writeStartElement(qName.getNamespaceURI(), qName.getLocalName());
/*     */       } 
/*     */     } else {
/*     */       
/* 286 */       writer.writeStartElement(name);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reset() {
/* 292 */     this._classAttribute = QName.valueOf("class");
/* 293 */     this._aliasToClass.clear();
/* 294 */     this._classToAlias.clear();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/XMLBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */