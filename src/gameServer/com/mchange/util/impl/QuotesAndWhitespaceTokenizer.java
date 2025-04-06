/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QuotesAndWhitespaceTokenizer
/*     */   extends StringEnumerationHelperBase
/*     */ {
/*     */   Object current;
/*  44 */   LinkedList list = new LinkedList();
/*     */ 
/*     */   
/*     */   public QuotesAndWhitespaceTokenizer(String paramString) throws IllegalArgumentException {
/*  48 */     int i = 0;
/*  49 */     int j = paramString.length();
/*  50 */     while (i < j) {
/*     */       
/*  52 */       int k = paramString.indexOf('"', i);
/*  53 */       if (k >= 0) {
/*     */         
/*  55 */         StringTokenizer stringTokenizer1 = new StringTokenizer(paramString.substring(i, k));
/*  56 */         if (stringTokenizer1.hasMoreTokens()) this.list.add(stringTokenizer1); 
/*  57 */         int m = paramString.indexOf('"', k + 1);
/*  58 */         if (m == -1)
/*  59 */           throw new IllegalArgumentException("Badly quoted string: " + paramString); 
/*  60 */         this.list.add(paramString.substring(k + 1, m));
/*  61 */         i = m + 1;
/*     */         
/*     */         continue;
/*     */       } 
/*  65 */       StringTokenizer stringTokenizer = new StringTokenizer(paramString.substring(i));
/*  66 */       if (stringTokenizer.hasMoreTokens()) this.list.add(stringTokenizer);
/*     */     
/*     */     } 
/*     */     
/*  70 */     advance();
/*     */   }
/*     */   
/*     */   public synchronized boolean hasMoreStrings() {
/*  74 */     return (this.current != null);
/*     */   }
/*     */   
/*     */   public synchronized String nextString() {
/*  78 */     if (this.current instanceof String) {
/*     */       
/*  80 */       String str1 = (String)this.current;
/*  81 */       advance();
/*  82 */       return str1;
/*     */     } 
/*     */ 
/*     */     
/*  86 */     StringTokenizer stringTokenizer = (StringTokenizer)this.current;
/*  87 */     String str = stringTokenizer.nextToken();
/*  88 */     if (!stringTokenizer.hasMoreTokens()) advance(); 
/*  89 */     return str;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void advance() {
/*  95 */     if (this.list.isEmpty()) {
/*  96 */       this.current = null;
/*     */     } else {
/*     */       
/*  99 */       this.current = this.list.getFirst();
/* 100 */       this.list.removeFirst();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 106 */     String str = "\t  \n\r";
/*     */     
/* 108 */     for (QuotesAndWhitespaceTokenizer quotesAndWhitespaceTokenizer = new QuotesAndWhitespaceTokenizer(str); quotesAndWhitespaceTokenizer.hasMoreStrings();)
/* 109 */       System.out.println(quotesAndWhitespaceTokenizer.nextString()); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/QuotesAndWhitespaceTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */