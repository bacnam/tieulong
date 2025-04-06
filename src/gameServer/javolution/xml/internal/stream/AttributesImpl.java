/*     */ package javolution.xml.internal.stream;
/*     */ 
/*     */ import javolution.text.CharArray;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.xml.sax.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AttributesImpl
/*     */   implements Attributes
/*     */ {
/*     */   private static class AttributeImpl
/*     */   {
/*     */     CharArray localName;
/*     */     CharArray prefix;
/*     */     CharArray qName;
/*     */     CharArray value;
/*     */     
/*     */     private AttributeImpl() {}
/*     */     
/*     */     public String toString() {
/*  22 */       return this.qName + "=" + this.value;
/*     */     }
/*     */   }
/*     */   
/*  26 */   private static final CharArray CDATA = new CharArray("CDATA");
/*     */   
/*  28 */   private static final CharArray EMPTY = new CharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  33 */   private final FastTable<AttributeImpl> attributes = new FastTable();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int length;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final NamespacesImpl namespaces;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AttributesImpl(NamespacesImpl namespaces) {
/*  49 */     this.namespaces = namespaces;
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
/*     */   public void addAttribute(CharArray localName, CharArray prefix, CharArray qName, CharArray value) {
/*     */     AttributeImpl attribute;
/*  63 */     if (this.length >= this.attributes.size()) {
/*  64 */       attribute = new AttributeImpl();
/*  65 */       this.attributes.add(attribute);
/*     */     } else {
/*  67 */       attribute = (AttributeImpl)this.attributes.get(this.length);
/*     */     } 
/*  69 */     attribute.localName = localName;
/*  70 */     attribute.prefix = prefix;
/*  71 */     attribute.qName = qName;
/*  72 */     attribute.value = value;
/*  73 */     this.length++;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndex(CharSequence qName) {
/*  78 */     for (int i = 0; i < this.length; i++) {
/*  79 */       if (qName.equals(((AttributeImpl)this.attributes.get(i)).qName))
/*  80 */         return i; 
/*     */     } 
/*  82 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getIndex(CharSequence uri, CharSequence localName) {
/*  87 */     for (int i = 0; i < this.length; i++) {
/*  88 */       AttributeImpl attribute = (AttributeImpl)this.attributes.get(i);
/*  89 */       if (localName.equals(attribute.localName)) {
/*  90 */         if (attribute.prefix == null) {
/*  91 */           if (uri.length() == 0) {
/*  92 */             return i;
/*     */           }
/*  94 */         } else if (uri.equals(this.namespaces.getNamespaceURI((CharSequence)attribute.prefix))) {
/*  95 */           return i;
/*     */         } 
/*     */       }
/*     */     } 
/*  99 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLength() {
/* 104 */     return this.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getLocalName(int index) {
/* 109 */     if (index < 0 || index >= this.length) return null; 
/* 110 */     return ((AttributeImpl)this.attributes.get(index)).localName;
/*     */   }
/*     */   
/*     */   public CharArray getPrefix(int index) {
/* 114 */     if (index < 0 || index >= this.length) return null; 
/* 115 */     return ((AttributeImpl)this.attributes.get(index)).prefix;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getQName(int index) {
/* 120 */     if (index < 0 || index >= this.length) return null; 
/* 121 */     return ((AttributeImpl)this.attributes.get(index)).qName;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getType(CharSequence qName) {
/* 126 */     return (getIndex(qName) >= 0) ? CDATA : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getType(CharSequence uri, CharSequence localName) {
/* 131 */     return (getIndex(uri, localName) >= 0) ? CDATA : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getType(int index) {
/* 136 */     if (index < 0 || index >= this.length) return null; 
/* 137 */     return CDATA;
/*     */   }
/*     */   
/*     */   public CharArray getURI(int index) {
/* 141 */     if (index < 0 || index >= this.length) return null; 
/* 142 */     CharArray prefix = ((AttributeImpl)this.attributes.get(index)).prefix;
/* 143 */     return (prefix == null) ? EMPTY : this.namespaces.getNamespaceURI((CharSequence)prefix);
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getValue(CharSequence qName) {
/* 148 */     int index = getIndex(qName);
/* 149 */     return (index >= 0) ? ((AttributeImpl)this.attributes.get(index)).value : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getValue(CharSequence uri, CharSequence localName) {
/* 154 */     int index = getIndex(uri, localName);
/* 155 */     return (index >= 0) ? ((AttributeImpl)this.attributes.get(index)).value : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getValue(int index) {
/* 160 */     if (index < 0 || index >= this.length) return null; 
/* 161 */     return ((AttributeImpl)this.attributes.get(index)).value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 168 */     this.length = 0;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/AttributesImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */