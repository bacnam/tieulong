/*     */ package org.apache.http.conn.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ public final class PublicSuffixListParser
/*     */ {
/*     */   private static final int MAX_LINE_LEN = 256;
/*     */   
/*     */   public PublicSuffixList parse(Reader reader) throws IOException {
/*  59 */     List<String> rules = new ArrayList<String>();
/*  60 */     List<String> exceptions = new ArrayList<String>();
/*  61 */     BufferedReader r = new BufferedReader(reader);
/*  62 */     StringBuilder sb = new StringBuilder(256);
/*  63 */     boolean more = true;
/*  64 */     while (more) {
/*  65 */       more = readLine(r, sb);
/*  66 */       String line = sb.toString();
/*  67 */       if (line.isEmpty()) {
/*     */         continue;
/*     */       }
/*  70 */       if (line.startsWith("//")) {
/*     */         continue;
/*     */       }
/*  73 */       if (line.startsWith(".")) {
/*  74 */         line = line.substring(1);
/*     */       }
/*     */       
/*  77 */       boolean isException = line.startsWith("!");
/*  78 */       if (isException) {
/*  79 */         line = line.substring(1);
/*     */       }
/*     */       
/*  82 */       if (isException) {
/*  83 */         exceptions.add(line); continue;
/*     */       } 
/*  85 */       rules.add(line);
/*     */     } 
/*     */     
/*  88 */     return new PublicSuffixList(rules, exceptions);
/*     */   }
/*     */   
/*     */   private boolean readLine(Reader r, StringBuilder sb) throws IOException {
/*  92 */     sb.setLength(0);
/*     */     
/*  94 */     boolean hitWhitespace = false; int b;
/*  95 */     while ((b = r.read()) != -1) {
/*  96 */       char c = (char)b;
/*  97 */       if (c == '\n') {
/*     */         break;
/*     */       }
/*     */       
/* 101 */       if (Character.isWhitespace(c)) {
/* 102 */         hitWhitespace = true;
/*     */       }
/* 104 */       if (!hitWhitespace) {
/* 105 */         sb.append(c);
/*     */       }
/* 107 */       if (sb.length() > 256) {
/* 108 */         return false;
/*     */       }
/*     */     } 
/* 111 */     return (b != -1);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/util/PublicSuffixListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */