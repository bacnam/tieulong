/*     */ package org.apache.mina.filter.codec.textline;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.PrintWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LineDelimiter
/*     */ {
/*     */   public static final LineDelimiter DEFAULT;
/*     */   
/*     */   static {
/*  43 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/*  44 */     PrintWriter out = new PrintWriter(bout, true);
/*  45 */     out.println();
/*  46 */     DEFAULT = new LineDelimiter(new String(bout.toByteArray()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final LineDelimiter AUTO = new LineDelimiter("");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   public static final LineDelimiter CRLF = new LineDelimiter("\r\n");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   public static final LineDelimiter UNIX = new LineDelimiter("\n");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  70 */   public static final LineDelimiter WINDOWS = CRLF;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  75 */   public static final LineDelimiter MAC = new LineDelimiter("\r");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   public static final LineDelimiter NUL = new LineDelimiter("\000");
/*     */ 
/*     */ 
/*     */   
/*     */   private final String value;
/*     */ 
/*     */ 
/*     */   
/*     */   public LineDelimiter(String value) {
/*  90 */     if (value == null) {
/*  91 */       throw new IllegalArgumentException("delimiter");
/*     */     }
/*     */     
/*  94 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/* 101 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 109 */     return this.value.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 117 */     if (this == o) {
/* 118 */       return true;
/*     */     }
/*     */     
/* 121 */     if (!(o instanceof LineDelimiter)) {
/* 122 */       return false;
/*     */     }
/*     */     
/* 125 */     LineDelimiter that = (LineDelimiter)o;
/*     */     
/* 127 */     return this.value.equals(that.value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 135 */     if (this.value.length() == 0) {
/* 136 */       return "delimiter: auto";
/*     */     }
/* 138 */     StringBuilder buf = new StringBuilder();
/* 139 */     buf.append("delimiter:");
/*     */     
/* 141 */     for (int i = 0; i < this.value.length(); i++) {
/* 142 */       buf.append(" 0x");
/* 143 */       buf.append(Integer.toHexString(this.value.charAt(i)));
/*     */     } 
/*     */     
/* 146 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/textline/LineDelimiter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */