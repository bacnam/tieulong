/*     */ package com.mchange.v2.io;
/*     */ 
/*     */ import java.io.FilterWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndentedWriter
/*     */   extends FilterWriter
/*     */ {
/*     */   static final String EOL;
/*     */   
/*     */   static {
/*  46 */     String str = System.getProperty("line.separator");
/*  47 */     EOL = (str != null) ? str : "\r\n";
/*     */   }
/*     */   
/*  50 */   int indent_level = 0;
/*     */   boolean at_line_start = true;
/*     */   
/*     */   public IndentedWriter(Writer paramWriter) {
/*  54 */     super(paramWriter);
/*     */   }
/*     */   private boolean isEol(char paramChar) {
/*  57 */     return (paramChar == '\r' || paramChar == '\n');
/*     */   }
/*     */   public void upIndent() {
/*  60 */     this.indent_level++;
/*     */   }
/*     */   public void downIndent() {
/*  63 */     this.indent_level--;
/*     */   }
/*     */   
/*     */   public void write(int paramInt) throws IOException {
/*  67 */     this.out.write(paramInt);
/*  68 */     this.at_line_start = isEol((char)paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
/*  73 */     this.out.write(paramArrayOfchar, paramInt1, paramInt2);
/*  74 */     this.at_line_start = isEol(paramArrayOfchar[paramInt1 + paramInt2 - 1]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(String paramString, int paramInt1, int paramInt2) throws IOException {
/*  79 */     if (paramInt2 > 0) {
/*     */       
/*  81 */       this.out.write(paramString, paramInt1, paramInt2);
/*  82 */       this.at_line_start = isEol(paramString.charAt(paramInt1 + paramInt2 - 1));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void printIndent() throws IOException {
/*  88 */     for (byte b = 0; b < this.indent_level; b++) {
/*  89 */       this.out.write(9);
/*     */     }
/*     */   }
/*     */   
/*     */   public void print(String paramString) throws IOException {
/*  94 */     if (this.at_line_start)
/*  95 */       printIndent(); 
/*  96 */     this.out.write(paramString);
/*  97 */     char c = paramString.charAt(paramString.length() - 1);
/*  98 */     this.at_line_start = isEol(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public void println(String paramString) throws IOException {
/* 103 */     if (this.at_line_start)
/* 104 */       printIndent(); 
/* 105 */     this.out.write(paramString);
/* 106 */     this.out.write(EOL);
/* 107 */     this.at_line_start = true;
/*     */   }
/*     */   
/*     */   public void print(boolean paramBoolean) throws IOException {
/* 111 */     print(String.valueOf(paramBoolean));
/*     */   }
/*     */   public void print(byte paramByte) throws IOException {
/* 114 */     print(String.valueOf(paramByte));
/*     */   }
/*     */   public void print(char paramChar) throws IOException {
/* 117 */     print(String.valueOf(paramChar));
/*     */   }
/*     */   public void print(short paramShort) throws IOException {
/* 120 */     print(String.valueOf(paramShort));
/*     */   }
/*     */   public void print(int paramInt) throws IOException {
/* 123 */     print(String.valueOf(paramInt));
/*     */   }
/*     */   public void print(long paramLong) throws IOException {
/* 126 */     print(String.valueOf(paramLong));
/*     */   }
/*     */   public void print(float paramFloat) throws IOException {
/* 129 */     print(String.valueOf(paramFloat));
/*     */   }
/*     */   public void print(double paramDouble) throws IOException {
/* 132 */     print(String.valueOf(paramDouble));
/*     */   }
/*     */   public void print(Object paramObject) throws IOException {
/* 135 */     print(String.valueOf(paramObject));
/*     */   }
/*     */   public void println(boolean paramBoolean) throws IOException {
/* 138 */     println(String.valueOf(paramBoolean));
/*     */   }
/*     */   public void println(byte paramByte) throws IOException {
/* 141 */     println(String.valueOf(paramByte));
/*     */   }
/*     */   public void println(char paramChar) throws IOException {
/* 144 */     println(String.valueOf(paramChar));
/*     */   }
/*     */   public void println(short paramShort) throws IOException {
/* 147 */     println(String.valueOf(paramShort));
/*     */   }
/*     */   public void println(int paramInt) throws IOException {
/* 150 */     println(String.valueOf(paramInt));
/*     */   }
/*     */   public void println(long paramLong) throws IOException {
/* 153 */     println(String.valueOf(paramLong));
/*     */   }
/*     */   public void println(float paramFloat) throws IOException {
/* 156 */     println(String.valueOf(paramFloat));
/*     */   }
/*     */   public void println(double paramDouble) throws IOException {
/* 159 */     println(String.valueOf(paramDouble));
/*     */   }
/*     */   public void println(Object paramObject) throws IOException {
/* 162 */     println(String.valueOf(paramObject));
/*     */   }
/*     */   public void println() throws IOException {
/* 165 */     println("");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/io/IndentedWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */