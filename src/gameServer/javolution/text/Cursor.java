/*     */ package javolution.text;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Cursor
/*     */ {
/*     */   private int index;
/*     */   
/*     */   public final int getIndex() {
/*  62 */     return this.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setIndex(int i) {
/*  71 */     this.index = i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean atEnd(CharSequence csq) {
/*  82 */     return (this.index >= csq.length());
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
/*     */   public final boolean at(char c, CharSequence csq) {
/*  94 */     return (this.index < csq.length()) ? ((csq.charAt(this.index) == c)) : false;
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
/*     */   public final boolean at(CharSet charSet, CharSequence csq) {
/* 106 */     return (this.index < csq.length()) ? charSet.contains(csq.charAt(this.index)) : false;
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
/*     */   public final boolean at(String str, CharSequence csq) {
/* 120 */     int i = this.index;
/* 121 */     int length = csq.length();
/* 122 */     for (int j = 0; j < str.length();) {
/* 123 */       if (i >= length || str.charAt(j++) != csq.charAt(i++))
/* 124 */         return false; 
/*     */     } 
/* 126 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final char currentChar(CharSequence csq) {
/* 137 */     return csq.charAt(this.index);
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
/*     */   public final char nextChar(CharSequence csq) {
/* 149 */     return csq.charAt(this.index++);
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
/*     */   public final boolean skipAny(char c, CharSequence csq) {
/* 163 */     int i = this.index;
/* 164 */     int n = csq.length();
/* 165 */     for (; i < n && csq.charAt(i) == c; i++);
/* 166 */     if (i == this.index)
/* 167 */       return false; 
/* 168 */     this.index = i;
/* 169 */     return true;
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
/*     */   public final boolean skipAny(CharSet charSet, CharSequence csq) {
/* 189 */     int i = this.index;
/* 190 */     int n = csq.length();
/* 191 */     for (; i < n && charSet.contains(csq.charAt(i)); i++);
/* 192 */     if (i == this.index)
/* 193 */       return false; 
/* 194 */     this.index = i;
/* 195 */     return true;
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
/*     */   public final boolean skip(char c, CharSequence csq) {
/* 212 */     if (at(c, csq)) {
/* 213 */       this.index++;
/* 214 */       return true;
/*     */     } 
/* 216 */     return false;
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
/*     */   public final boolean skip(CharSet charSet, CharSequence csq) {
/* 234 */     if (at(charSet, csq)) {
/* 235 */       this.index++;
/* 236 */       return true;
/*     */     } 
/* 238 */     return false;
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
/*     */   public final boolean skip(String str, CharSequence csq) {
/* 257 */     if (at(str, csq)) {
/* 258 */       this.index += str.length();
/* 259 */       return true;
/*     */     } 
/* 261 */     return false;
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
/*     */   public final CharSequence nextToken(CharSequence csq, char c) {
/* 279 */     int n = csq.length();
/* 280 */     for (int i = this.index; i < n; i++) {
/* 281 */       if (csq.charAt(i) != c) {
/* 282 */         int j = i;
/* 283 */         while (++j < n && csq.charAt(j) != c);
/*     */ 
/*     */         
/* 286 */         this.index = j;
/* 287 */         return csq.subSequence(i, j);
/*     */       } 
/*     */     } 
/* 290 */     this.index = n;
/* 291 */     return null;
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
/*     */   public final CharSequence nextToken(CharSequence csq, CharSet charSet) {
/* 308 */     int n = csq.length();
/* 309 */     for (int i = this.index; i < n; i++) {
/* 310 */       if (!charSet.contains(csq.charAt(i))) {
/* 311 */         int j = i;
/* 312 */         while (++j < n && !charSet.contains(csq.charAt(j)));
/*     */ 
/*     */         
/* 315 */         this.index = j;
/* 316 */         return csq.subSequence(i, j);
/*     */       } 
/*     */     } 
/* 319 */     this.index = n;
/* 320 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CharSequence head(CharSequence csq) {
/* 330 */     return csq.subSequence(0, this.index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CharSequence tail(CharSequence csq) {
/* 340 */     return csq.subSequence(this.index, csq.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Cursor increment() {
/* 349 */     return increment(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Cursor increment(int i) {
/* 359 */     this.index += i;
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 370 */     return "Cursor: " + this.index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 381 */     if (obj == null)
/* 382 */       return false; 
/* 383 */     if (!(obj instanceof Cursor))
/* 384 */       return false; 
/* 385 */     return (this.index == ((Cursor)obj).index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 395 */     return this.index;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/Cursor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */