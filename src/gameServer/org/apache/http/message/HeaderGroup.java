/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderIterator;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class HeaderGroup
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2608834160639271617L;
/*  61 */   private final List<Header> headers = new ArrayList<Header>(16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/*  68 */     this.headers.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(Header header) {
/*  78 */     if (header == null) {
/*     */       return;
/*     */     }
/*  81 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHeader(Header header) {
/*  90 */     if (header == null) {
/*     */       return;
/*     */     }
/*  93 */     this.headers.remove(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateHeader(Header header) {
/* 104 */     if (header == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 110 */     for (int i = 0; i < this.headers.size(); i++) {
/* 111 */       Header current = this.headers.get(i);
/* 112 */       if (current.getName().equalsIgnoreCase(header.getName())) {
/* 113 */         this.headers.set(i, header);
/*     */         return;
/*     */       } 
/*     */     } 
/* 117 */     this.headers.add(header);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHeaders(Header[] headers) {
/* 128 */     clear();
/* 129 */     if (headers == null) {
/*     */       return;
/*     */     }
/* 132 */     Collections.addAll(this.headers, headers);
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
/*     */   public Header getCondensedHeader(String name) {
/* 147 */     Header[] hdrs = getHeaders(name);
/*     */     
/* 149 */     if (hdrs.length == 0)
/* 150 */       return null; 
/* 151 */     if (hdrs.length == 1) {
/* 152 */       return hdrs[0];
/*     */     }
/* 154 */     CharArrayBuffer valueBuffer = new CharArrayBuffer(128);
/* 155 */     valueBuffer.append(hdrs[0].getValue());
/* 156 */     for (int i = 1; i < hdrs.length; i++) {
/* 157 */       valueBuffer.append(", ");
/* 158 */       valueBuffer.append(hdrs[i].getValue());
/*     */     } 
/*     */     
/* 161 */     return new BasicHeader(name.toLowerCase(Locale.ROOT), valueBuffer.toString());
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
/*     */   public Header[] getHeaders(String name) {
/* 176 */     List<Header> headersFound = new ArrayList<Header>();
/*     */ 
/*     */ 
/*     */     
/* 180 */     for (int i = 0; i < this.headers.size(); i++) {
/* 181 */       Header header = this.headers.get(i);
/* 182 */       if (header.getName().equalsIgnoreCase(name)) {
/* 183 */         headersFound.add(header);
/*     */       }
/*     */     } 
/*     */     
/* 187 */     return headersFound.<Header>toArray(new Header[headersFound.size()]);
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
/*     */   public Header getFirstHeader(String name) {
/* 202 */     for (int i = 0; i < this.headers.size(); i++) {
/* 203 */       Header header = this.headers.get(i);
/* 204 */       if (header.getName().equalsIgnoreCase(name)) {
/* 205 */         return header;
/*     */       }
/*     */     } 
/* 208 */     return null;
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
/*     */   public Header getLastHeader(String name) {
/* 221 */     for (int i = this.headers.size() - 1; i >= 0; i--) {
/* 222 */       Header header = this.headers.get(i);
/* 223 */       if (header.getName().equalsIgnoreCase(name)) {
/* 224 */         return header;
/*     */       }
/*     */     } 
/*     */     
/* 228 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header[] getAllHeaders() {
/* 237 */     return this.headers.<Header>toArray(new Header[this.headers.size()]);
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
/*     */   public boolean containsHeader(String name) {
/* 253 */     for (int i = 0; i < this.headers.size(); i++) {
/* 254 */       Header header = this.headers.get(i);
/* 255 */       if (header.getName().equalsIgnoreCase(name)) {
/* 256 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 260 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderIterator iterator() {
/* 271 */     return new BasicListHeaderIterator(this.headers, null);
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
/*     */   public HeaderIterator iterator(String name) {
/* 285 */     return new BasicListHeaderIterator(this.headers, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderGroup copy() {
/* 296 */     HeaderGroup clone = new HeaderGroup();
/* 297 */     clone.headers.addAll(this.headers);
/* 298 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 303 */     return super.clone();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 308 */     return this.headers.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/message/HeaderGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */