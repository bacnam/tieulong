/*     */ package javolution.xml.internal.stream;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javolution.text.CharArray;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.xml.stream.NamespaceContext;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NamespacesImpl
/*     */   implements NamespaceContext
/*     */ {
/*     */   static final int NBR_PREDEFINED_NAMESPACES = 3;
/*  33 */   final CharArray _nullNsURI = new CharArray("");
/*     */   
/*  35 */   final CharArray _defaultNsPrefix = new CharArray("");
/*     */   
/*  37 */   final CharArray _xml = new CharArray("xml");
/*     */   
/*  39 */   final CharArray _xmlURI = new CharArray("http://www.w3.org/XML/1998/namespace");
/*     */ 
/*     */   
/*  42 */   final CharArray _xmlns = new CharArray("xmlns");
/*     */   
/*  44 */   final CharArray _xmlnsURI = new CharArray("http://www.w3.org/2000/xmlns/");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   private int _nesting = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  54 */   CharArray[] _prefixes = new CharArray[16];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   CharArray[] _namespaces = new CharArray[this._prefixes.length];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   boolean[] _prefixesWritten = new boolean[this._prefixes.length];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   int[] _namespacesCount = new int[16];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   CharArray _defaultNamespace = this._nullNsURI;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int _defaultNamespaceIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharArray[] _prefixesTmp;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CharArray[] _namespacesTmp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CharArray getNamespaceURI(CharSequence prefix) {
/*  96 */     if (prefix == null)
/*  97 */       throw new IllegalArgumentException("null prefix not allowed"); 
/*  98 */     return getNamespaceURINullAllowed(prefix);
/*     */   }
/*     */   
/*     */   CharArray getNamespaceURINullAllowed(CharSequence prefix) {
/* 102 */     if (prefix == null || prefix.length() == 0)
/* 103 */       return this._defaultNamespace; 
/* 104 */     int count = this._namespacesCount[this._nesting];
/* 105 */     for (int i = count; --i >= 0;) {
/* 106 */       if (this._prefixes[i].equals(prefix))
/* 107 */         return this._namespaces[i]; 
/*     */     } 
/* 109 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public CharArray getPrefix(CharSequence uri) {
/* 114 */     if (uri == null)
/* 115 */       throw new IllegalArgumentException("null namespace URI not allowed"); 
/* 116 */     return this._defaultNamespace.equals(uri) ? this._defaultNsPrefix : getPrefix(uri, this._namespacesCount[this._nesting]);
/*     */   }
/*     */ 
/*     */   
/*     */   CharArray getPrefix(CharSequence uri, int count) {
/* 121 */     for (int i = count; --i >= 0; ) {
/* 122 */       CharArray prefix = this._prefixes[i];
/* 123 */       CharArray namespace = this._namespaces[i];
/* 124 */       if (namespace.equals(uri)) {
/*     */         
/* 126 */         boolean isPrefixOverwritten = false;
/* 127 */         for (int j = i + 1; j < count; j++) {
/* 128 */           if (prefix.equals(this._prefixes[j])) {
/* 129 */             isPrefixOverwritten = true;
/*     */             break;
/*     */           } 
/*     */         } 
/* 133 */         if (!isPrefixOverwritten)
/* 134 */           return prefix; 
/*     */       } 
/*     */     } 
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<CharArray> getPrefixes(CharSequence namespaceURI) {
/* 142 */     FastTable<CharArray> prefixes = new FastTable();
/* 143 */     for (int i = this._namespacesCount[this._nesting]; --i >= 0;) {
/* 144 */       if (this._namespaces[i].equals(namespaceURI)) {
/* 145 */         prefixes.add(this._prefixes[i]);
/*     */       }
/*     */     } 
/* 148 */     return prefixes.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   void setPrefix(CharArray prefix, CharArray uri) {
/* 153 */     int index = this._namespacesCount[this._nesting];
/* 154 */     this._prefixes[index] = prefix;
/* 155 */     this._namespaces[index] = uri;
/* 156 */     if (prefix.length() == 0) {
/* 157 */       this._defaultNamespaceIndex = index;
/* 158 */       this._defaultNamespace = uri;
/*     */     } 
/* 160 */     this._namespacesCount[this._nesting] = this._namespacesCount[this._nesting] + 1; if (this._namespacesCount[this._nesting] + 1 >= this._prefixes.length) {
/* 161 */       resizePrefixStack();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void setPrefix(CharSequence prefix, CharSequence uri, boolean isWritten) {
/* 168 */     int index = this._namespacesCount[this._nesting];
/* 169 */     this._prefixesWritten[index] = isWritten;
/* 170 */     int prefixLength = prefix.length();
/* 171 */     CharArray prefixTmp = this._prefixesTmp[index];
/* 172 */     if (prefixTmp == null || (prefixTmp.array()).length < prefixLength) {
/* 173 */       this._prefixesTmp[index] = (new CharArray()).setArray(new char[prefixLength + 32], 0, 0);
/*     */       
/* 175 */       prefixTmp = this._prefixesTmp[index];
/*     */     } 
/* 177 */     for (int i = 0; i < prefixLength; i++) {
/* 178 */       prefixTmp.array()[i] = prefix.charAt(i);
/*     */     }
/* 180 */     prefixTmp.setArray(prefixTmp.array(), 0, prefixLength);
/*     */     
/* 182 */     int uriLength = uri.length();
/* 183 */     CharArray namespaceTmp = this._namespacesTmp[index];
/* 184 */     if (namespaceTmp == null || (namespaceTmp.array()).length < uriLength) {
/* 185 */       this._namespacesTmp[index] = (new CharArray()).setArray(new char[uriLength + 32], 0, 0);
/*     */       
/* 187 */       namespaceTmp = this._namespacesTmp[index];
/*     */     } 
/* 189 */     for (int j = 0; j < uriLength; j++) {
/* 190 */       namespaceTmp.array()[j] = uri.charAt(j);
/*     */     }
/* 192 */     namespaceTmp.setArray(namespaceTmp.array(), 0, uriLength);
/*     */ 
/*     */     
/* 195 */     setPrefix(prefixTmp, namespaceTmp);
/*     */   }
/*     */   
/* 198 */   public NamespacesImpl() { this._prefixesTmp = new CharArray[this._prefixes.length];
/*     */     
/* 200 */     this._namespacesTmp = new CharArray[this._prefixes.length]; this._prefixes[0] = this._defaultNsPrefix; this._namespaces[0] = this._nullNsURI; this._prefixes[1] = this._xml; this._namespaces[1] = this._xmlURI;
/*     */     this._prefixes[2] = this._xmlns;
/*     */     this._namespaces[2] = this._xmlnsURI;
/* 203 */     this._namespacesCount[0] = 3; } void pop() { if (this._namespacesCount[--this._nesting] <= this._defaultNamespaceIndex) {
/* 204 */       searchDefaultNamespace();
/*     */     } }
/*     */ 
/*     */   
/*     */   private void searchDefaultNamespace() {
/* 209 */     int count = this._namespacesCount[this._nesting];
/* 210 */     for (int i = count; --i >= 0;) {
/* 211 */       if (this._prefixes[i].length() == 0) {
/* 212 */         this._defaultNamespaceIndex = i;
/*     */         return;
/*     */       } 
/*     */     } 
/* 216 */     throw new Error("Cannot find default namespace");
/*     */   }
/*     */   
/*     */   void push() {
/* 220 */     this._nesting++;
/* 221 */     if (this._nesting >= this._namespacesCount.length) {
/* 222 */       resizeNamespacesCount();
/*     */     }
/* 224 */     this._namespacesCount[this._nesting] = this._namespacesCount[this._nesting - 1];
/*     */   }
/*     */   
/*     */   public void reset() {
/* 228 */     this._defaultNamespace = this._nullNsURI;
/* 229 */     this._defaultNamespaceIndex = 0;
/* 230 */     this._namespacesCount[0] = 3;
/* 231 */     this._nesting = 0;
/*     */   }
/*     */   
/*     */   private void resizeNamespacesCount() {
/* 235 */     int oldLength = this._namespacesCount.length;
/* 236 */     int newLength = oldLength * 2;
/*     */ 
/*     */     
/* 239 */     int[] tmp = new int[newLength];
/* 240 */     System.arraycopy(this._namespacesCount, 0, tmp, 0, oldLength);
/* 241 */     this._namespacesCount = tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resizePrefixStack() {
/* 246 */     int oldLength = this._prefixes.length;
/* 247 */     int newLength = oldLength * 2;
/*     */ 
/*     */     
/* 250 */     CharArray[] tmp0 = new CharArray[newLength];
/* 251 */     System.arraycopy(this._prefixes, 0, tmp0, 0, oldLength);
/* 252 */     this._prefixes = tmp0;
/*     */ 
/*     */     
/* 255 */     CharArray[] tmp1 = new CharArray[newLength];
/* 256 */     System.arraycopy(this._namespaces, 0, tmp1, 0, oldLength);
/* 257 */     this._namespaces = tmp1;
/*     */ 
/*     */     
/* 260 */     boolean[] tmp2 = new boolean[newLength];
/* 261 */     System.arraycopy(this._prefixesWritten, 0, tmp2, 0, oldLength);
/* 262 */     this._prefixesWritten = tmp2;
/*     */ 
/*     */     
/* 265 */     CharArray[] tmp3 = new CharArray[newLength];
/* 266 */     System.arraycopy(this._prefixesTmp, 0, tmp3, 0, oldLength);
/* 267 */     this._prefixesTmp = tmp3;
/*     */ 
/*     */     
/* 270 */     CharArray[] tmp4 = new CharArray[newLength];
/* 271 */     System.arraycopy(this._namespacesTmp, 0, tmp4, 0, oldLength);
/* 272 */     this._namespacesTmp = tmp4;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/NamespacesImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */