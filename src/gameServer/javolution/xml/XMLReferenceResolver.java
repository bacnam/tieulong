/*     */ package javolution.xml;
/*     */ 
/*     */ import javolution.text.CharArray;
/*     */ import javolution.text.TextBuilder;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.util.Index;
/*     */ import javolution.util.function.Equalities;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XMLReferenceResolver
/*     */ {
/*  35 */   private FastMap<Object, Index> _objectToId = new FastMap(Equalities.IDENTITY);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   private FastTable<Object> _idToObject = new FastTable();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int _counter;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   private String _idName = "id";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private String _idURI = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private String _refName = "ref";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private String _refURI = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TextBuilder _tmp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdentifierAttribute(String name) {
/*  81 */     setIdentifierAttribute(name, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIdentifierAttribute(String localName, String uri) {
/*  92 */     this._idName = localName;
/*  93 */     this._idURI = uri;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReferenceAttribute(String name) {
/* 104 */     setReferenceAttribute(name, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setReferenceAttribute(String localName, String uri) {
/* 115 */     this._refName = localName;
/* 116 */     this._refURI = uri;
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
/*     */   public boolean writeReference(Object obj, XMLFormat.OutputElement xml) throws XMLStreamException {
/* 132 */     Index id = (Index)this._objectToId.get(obj);
/* 133 */     if (id == null) {
/* 134 */       id = Index.valueOf(this._counter++);
/* 135 */       this._objectToId.put(obj, id);
/* 136 */       this._tmp.clear().append(id.intValue());
/* 137 */       if (this._idURI == null) {
/* 138 */         xml.getStreamWriter().writeAttribute(this._idName, (CharSequence)this._tmp);
/*     */       } else {
/* 140 */         xml.getStreamWriter().writeAttribute(this._idURI, this._idName, (CharSequence)this._tmp);
/*     */       } 
/* 142 */       return false;
/*     */     } 
/* 144 */     this._tmp.clear().append(id.intValue());
/* 145 */     if (this._refURI == null) {
/* 146 */       xml._writer.writeAttribute(this._refName, (CharSequence)this._tmp);
/*     */     } else {
/* 148 */       xml._writer.writeAttribute(this._refURI, this._refName, (CharSequence)this._tmp);
/*     */     } 
/* 150 */     return true;
/*     */   }
/*     */   public XMLReferenceResolver() {
/* 153 */     this._tmp = new TextBuilder();
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
/*     */   public Object readReference(XMLFormat.InputElement xml) throws XMLStreamException {
/* 166 */     CharArray value = xml._reader.getAttributeValue(this._refURI, this._refName);
/* 167 */     if (value == null)
/* 168 */       return null; 
/* 169 */     int ref = value.toInt();
/* 170 */     if (ref >= this._idToObject.size())
/* 171 */       throw new XMLStreamException("Reference: " + value + " not found"); 
/* 172 */     return this._idToObject.get(ref);
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
/*     */   public void createReference(Object obj, XMLFormat.InputElement xml) throws XMLStreamException {
/* 186 */     CharArray value = xml._reader.getAttributeValue(this._idURI, this._idName);
/* 187 */     if (value == null)
/*     */       return; 
/* 189 */     int i = value.toInt();
/* 190 */     if (this._idToObject.size() != i) {
/* 191 */       throw new XMLStreamException("Identifier discontinuity detected (expected " + this._idToObject.size() + " found " + i + ")");
/*     */     }
/* 193 */     this._idToObject.add(obj);
/*     */   }
/*     */   
/*     */   public void reset() {
/* 197 */     this._idName = "id";
/* 198 */     this._idURI = null;
/* 199 */     this._refName = "ref";
/* 200 */     this._refURI = null;
/* 201 */     this._idToObject.clear();
/* 202 */     this._objectToId.clear();
/* 203 */     this._counter = 0;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/XMLReferenceResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */