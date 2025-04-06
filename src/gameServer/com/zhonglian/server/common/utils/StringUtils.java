/*     */ package com.zhonglian.server.common.utils;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class StringUtils
/*     */ {
/*     */   static final Pattern COMMA_SEP_TRIM_REGEX;
/*     */   static final Pattern COMMA_SEP_NO_TRIM_REGEX;
/*     */   
/*     */   static {
/*     */     try {
/*  54 */       COMMA_SEP_TRIM_REGEX = Pattern.compile("\\s*\\,\\s*");
/*  55 */       COMMA_SEP_NO_TRIM_REGEX = Pattern.compile("\\,");
/*  56 */     } catch (PatternSyntaxException e) {
/*  57 */       CommLog.error(null, e);
/*  58 */       throw new InternalError(e.toString());
/*     */     } 
/*     */   }
/*     */   
/*  62 */   public static final String[] EMPTY_STRING_ARRAY = new String[0];
/*     */   
/*     */   public static List<Integer> string2Integer(String str) {
/*     */     try {
/*  66 */       List<Integer> intList = new ArrayList<>();
/*  67 */       String[] strList = str.split(";"); byte b; int i;
/*     */       String[] arrayOfString1;
/*  69 */       for (i = (arrayOfString1 = strList).length, b = 0; b < i; ) { String item = arrayOfString1[b];
/*  70 */         intList.add(Integer.valueOf(item)); b++; }
/*     */       
/*  72 */       return intList;
/*  73 */     } catch (Exception e) {
/*  74 */       return new ArrayList<>();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String replaceArgs(String src, String... args) {
/*  79 */     if (args == null || src == null) {
/*  80 */       return src;
/*     */     }
/*  82 */     for (int i = 0; i < args.length; i++) {
/*  83 */       src = src.replaceAll("\\{" + i + "\\}", args[i]);
/*     */     }
/*  85 */     return src;
/*     */   }
/*     */   
/*     */   public static String joinList(String[] list, String sep) {
/*  89 */     StringBuffer sb = new StringBuffer();
/*  90 */     if (list == null || list.length == 0) {
/*  91 */       return "";
/*     */     }
/*  93 */     for (int i = 0; i < list.length; i++) {
/*  94 */       sb.append(list[i]);
/*  95 */       if (i != list.length - 1) {
/*  96 */         sb.append(sep);
/*     */       }
/*     */     } 
/*     */     
/* 100 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static <T> String list2String(List<T> intList) {
/*     */     try {
/* 105 */       StringBuilder sBuilder = new StringBuilder();
/* 106 */       for (T num : intList) {
/* 107 */         sBuilder.append((new StringBuilder()).append(num).append(";").toString());
/*     */       }
/* 109 */       return sBuilder.toString();
/* 110 */     } catch (Exception e) {
/* 111 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List<String> mailArgs(String str) {
/* 116 */     return stringToList(str, "\t");
/*     */   }
/*     */   
/*     */   public static List<String> stringToList(String str, String splitor) {
/* 120 */     List<String> ret = new ArrayList<>();
/* 121 */     if (str == null || splitor == null) {
/* 122 */       return ret;
/*     */     }
/* 124 */     return Arrays.asList(str.split(splitor));
/*     */   }
/*     */   
/*     */   public static int compareToBytes(String str, byte[] rawString) {
/* 128 */     byte[] raw1 = str.getBytes(Charset.forName("US-ASCII"));
/*     */     
/* 130 */     int min = Math.min(raw1.length, rawString.length);
/* 131 */     int diff = 0;
/*     */     int i;
/* 133 */     for (i = 0; i < min; i++) {
/* 134 */       diff = raw1[i] - rawString[i];
/* 135 */       if (diff != 0) {
/* 136 */         return (diff > 0) ? 1 : -1;
/*     */       }
/*     */     } 
/* 139 */     if (rawString.length == raw1.length) {
/* 140 */       return 0;
/*     */     }
/*     */     
/* 143 */     return (i == rawString.length) ? 1 : -1;
/*     */   }
/*     */   
/*     */   public static int httpLikeHeader(byte[] input, int size) throws IOException {
/* 147 */     int cur = 0;
/* 148 */     int line_start = 0;
/* 149 */     int state = 0;
/* 150 */     int c = 0;
/*     */     
/* 152 */     int ContentLength = 0;
/* 153 */     boolean firstLine = true;
/*     */     
/* 155 */     while (cur < size) {
/* 156 */       c = input[cur];
/*     */       
/* 158 */       if (state == 0) {
/* 159 */         if (c == 13) {
/* 160 */           state = 1;
/*     */         }
/* 162 */       } else if (state == 1) {
/* 163 */         if (c == 10) {
/* 164 */           state = 2;
/*     */           
/* 166 */           if (!firstLine) {
/* 167 */             int lineLen = cur - line_start - 1;
/* 168 */             if (lineLen > 0) {
/* 169 */               String line = new String(input, line_start, lineLen);
/* 170 */               String[] split = line.split(":");
/* 171 */               if (split.length == 2) {
/* 172 */                 String key = split[0].trim();
/* 173 */                 if (key.equals("Content-Length")) {
/* 174 */                   ContentLength = Integer.parseInt(split[1].trim());
/*     */                 }
/*     */               } 
/*     */             } else {
/* 178 */               if (ContentLength > 0) {
/* 179 */                 if (cur + ContentLength + 1 <= size) {
/* 180 */                   return cur + ContentLength + 1;
/*     */                 }
/* 182 */                 return -3;
/*     */               } 
/*     */ 
/*     */               
/* 186 */               return cur + 1;
/*     */             } 
/*     */           } else {
/*     */             
/* 190 */             firstLine = false;
/*     */           } 
/* 192 */           state = 0;
/* 193 */           line_start = cur + 1;
/*     */         } else {
/* 195 */           return -2;
/*     */         } 
/*     */       } 
/* 198 */       cur++;
/*     */     } 
/* 200 */     return -1;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */