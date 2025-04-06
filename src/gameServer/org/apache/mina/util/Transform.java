/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.LineNumberReader;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Transform
/*     */ {
/*     */   private static final String CDATA_START = "<![CDATA[";
/*     */   private static final String CDATA_END = "]]>";
/*     */   private static final String CDATA_PSEUDO_END = "]]&gt;";
/*     */   private static final String CDATA_EMBEDED_END = "]]>]]&gt;<![CDATA[";
/*  46 */   private static final int CDATA_END_LEN = "]]>".length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeTags(String input) {
/*  61 */     if (input == null || input.length() == 0 || (input.indexOf('"') == -1 && input.indexOf('&') == -1 && input.indexOf('<') == -1 && input.indexOf('>') == -1))
/*     */     {
/*     */ 
/*     */       
/*  65 */       return input;
/*     */     }
/*     */     
/*  68 */     StringBuilder buf = new StringBuilder(input.length() + 6);
/*     */ 
/*     */     
/*  71 */     int len = input.length();
/*  72 */     for (int i = 0; i < len; i++) {
/*  73 */       char ch = input.charAt(i);
/*  74 */       if (ch > '>') {
/*  75 */         buf.append(ch);
/*  76 */       } else if (ch == '<') {
/*  77 */         buf.append("&lt;");
/*  78 */       } else if (ch == '>') {
/*  79 */         buf.append("&gt;");
/*  80 */       } else if (ch == '&') {
/*  81 */         buf.append("&amp;");
/*  82 */       } else if (ch == '"') {
/*  83 */         buf.append("&quot;");
/*     */       } else {
/*  85 */         buf.append(ch);
/*     */       } 
/*     */     } 
/*  88 */     return buf.toString();
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
/*     */   public static void appendEscapingCDATA(StringBuffer buf, String str) {
/* 101 */     if (str != null) {
/* 102 */       int end = str.indexOf("]]>");
/* 103 */       if (end < 0) {
/* 104 */         buf.append(str);
/*     */       } else {
/* 106 */         int start = 0;
/* 107 */         while (end > -1) {
/* 108 */           buf.append(str.substring(start, end));
/* 109 */           buf.append("]]>]]&gt;<![CDATA[");
/* 110 */           start = end + CDATA_END_LEN;
/* 111 */           if (start < str.length()) {
/* 112 */             end = str.indexOf("]]>", start);
/*     */             continue;
/*     */           } 
/*     */           return;
/*     */         } 
/* 117 */         buf.append(str.substring(start));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String[] getThrowableStrRep(Throwable throwable) {
/* 128 */     StringWriter sw = new StringWriter();
/* 129 */     PrintWriter pw = new PrintWriter(sw);
/* 130 */     throwable.printStackTrace(pw);
/* 131 */     pw.flush();
/* 132 */     LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
/* 133 */     ArrayList<String> lines = new ArrayList<String>();
/*     */     try {
/* 135 */       String line = reader.readLine();
/* 136 */       while (line != null) {
/* 137 */         lines.add(line);
/* 138 */         line = reader.readLine();
/*     */       } 
/* 140 */     } catch (IOException ex) {
/* 141 */       lines.add(ex.toString());
/*     */     } 
/* 143 */     String[] rep = new String[lines.size()];
/* 144 */     lines.toArray(rep);
/* 145 */     return rep;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/Transform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */