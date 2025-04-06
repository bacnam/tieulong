/*     */ package javolution.xml;
/*     */ 
/*     */ import java.io.ObjectStreamException;
/*     */ import javolution.lang.Immutable;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.text.TextBuilder;
/*     */ import javolution.util.FastMap;
/*     */ import javolution.util.function.Equalities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class QName
/*     */   implements XMLSerializable, Immutable<QName>, CharSequence
/*     */ {
/*     */   private final transient CharArray _localName;
/*     */   private final transient CharArray _namespaceURI;
/*     */   private final String _toString;
/*  57 */   private static final FastMap<CharSequence, QName> FULL_NAME_TO_QNAME = new FastMap(Equalities.LEXICAL);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final long serialVersionUID = -6126031630693748647L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private QName(String namespaceURI, String localName, String toString) {
/*  69 */     this._namespaceURI = (namespaceURI == null) ? null : new CharArray(namespaceURI);
/*     */     
/*  71 */     this._localName = new CharArray(localName);
/*  72 */     this._toString = toString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QName valueOf(CharSequence name) {
/*  83 */     QName qName = (QName)FULL_NAME_TO_QNAME.get(name);
/*  84 */     return (qName != null) ? qName : createNoNamespace(name.toString());
/*     */   }
/*     */ 
/*     */   
/*     */   private static QName createNoNamespace(String name) {
/*  89 */     String localName = name;
/*  90 */     String namespaceURI = null;
/*  91 */     if (name.length() > 0 && name.charAt(0) == '{') {
/*  92 */       int index = name.lastIndexOf('}');
/*  93 */       localName = name.substring(index + 1);
/*  94 */       namespaceURI = name.substring(1, index);
/*     */     } 
/*  96 */     QName qName = new QName(namespaceURI, localName, name);
/*  97 */     synchronized (FULL_NAME_TO_QNAME) {
/*  98 */       QName tmp = (QName)FULL_NAME_TO_QNAME.putIfAbsent(name, qName);
/*  99 */       return (tmp == null) ? qName : tmp;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QName valueOf(String name) {
/* 110 */     QName qName = (QName)FULL_NAME_TO_QNAME.get(name);
/* 111 */     return (qName != null) ? qName : createNoNamespace(name);
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
/*     */   public static QName valueOf(CharSequence namespaceURI, CharSequence localName) {
/* 124 */     if (namespaceURI == null)
/* 125 */       return valueOf(localName); 
/* 126 */     TextBuilder tmp = new TextBuilder();
/* 127 */     tmp.append('{');
/* 128 */     tmp.append(namespaceURI);
/* 129 */     tmp.append('}');
/* 130 */     tmp.append(localName);
/* 131 */     return valueOf((CharSequence)tmp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence getLocalName() {
/* 141 */     return (CharSequence)this._localName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharSequence getNamespaceURI() {
/* 151 */     return (CharSequence)this._namespaceURI;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 161 */     return (this == obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 170 */     return this._toString;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 181 */     return this._toString.hashCode();
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
/*     */   public char charAt(int index) {
/* 193 */     return this._toString.charAt(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 203 */     return this._toString.length();
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
/*     */   public CharSequence subSequence(int start, int end) {
/* 218 */     return this._toString.substring(start, end);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object readResolve() throws ObjectStreamException {
/* 223 */     return valueOf(this._toString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName value() {
/* 230 */     return this;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/QName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */