/*     */ package org.apache.mina.proxy.utils;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.security.sasl.AuthenticationException;
/*     */ import javax.security.sasl.SaslException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtilities
/*     */ {
/*     */   public static String getDirectiveValue(HashMap<String, String> directivesMap, String directive, boolean mandatory) throws AuthenticationException {
/*  54 */     String value = directivesMap.get(directive);
/*  55 */     if (value == null) {
/*  56 */       if (mandatory) {
/*  57 */         throw new AuthenticationException("\"" + directive + "\" mandatory directive is missing");
/*     */       }
/*     */       
/*  60 */       return "";
/*     */     } 
/*     */     
/*  63 */     return value;
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
/*     */   public static void copyDirective(HashMap<String, String> directives, StringBuilder sb, String directive) {
/*  75 */     String directiveValue = directives.get(directive);
/*  76 */     if (directiveValue != null) {
/*  77 */       sb.append(directive).append(" = \"").append(directiveValue).append("\", ");
/*     */     }
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
/*     */   public static String copyDirective(HashMap<String, String> src, HashMap<String, String> dst, String directive) {
/*  92 */     String directiveValue = src.get(directive);
/*  93 */     if (directiveValue != null) {
/*  94 */       dst.put(directive, directiveValue);
/*     */     }
/*     */     
/*  97 */     return directiveValue;
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
/*     */   public static HashMap<String, String> parseDirectives(byte[] buf) throws SaslException {
/* 109 */     HashMap<String, String> map = new HashMap<String, String>();
/* 110 */     boolean gettingKey = true;
/* 111 */     boolean gettingQuotedValue = false;
/* 112 */     boolean expectSeparator = false;
/*     */ 
/*     */     
/* 115 */     ByteArrayOutputStream key = new ByteArrayOutputStream(10);
/* 116 */     ByteArrayOutputStream value = new ByteArrayOutputStream(10);
/*     */     
/* 118 */     int i = skipLws(buf, 0);
/* 119 */     while (i < buf.length) {
/* 120 */       byte bch = buf[i];
/*     */       
/* 122 */       if (gettingKey) {
/* 123 */         if (bch == 44) {
/* 124 */           if (key.size() != 0) {
/* 125 */             throw new SaslException("Directive key contains a ',':" + key);
/*     */           }
/*     */ 
/*     */           
/* 129 */           i = skipLws(buf, i + 1); continue;
/* 130 */         }  if (bch == 61) {
/* 131 */           if (key.size() == 0) {
/* 132 */             throw new SaslException("Empty directive key");
/*     */           }
/*     */           
/* 135 */           gettingKey = false;
/* 136 */           i = skipLws(buf, i + 1);
/*     */ 
/*     */           
/* 139 */           if (i < buf.length) {
/* 140 */             if (buf[i] == 34) {
/* 141 */               gettingQuotedValue = true;
/* 142 */               i++;
/*     */             }  continue;
/*     */           } 
/* 145 */           throw new SaslException("Valueless directive found: " + key.toString());
/*     */         } 
/* 147 */         if (isLws(bch)) {
/*     */           
/* 149 */           i = skipLws(buf, i + 1);
/*     */ 
/*     */           
/* 152 */           if (i < buf.length) {
/* 153 */             if (buf[i] != 61)
/* 154 */               throw new SaslException("'=' expected after key: " + key.toString()); 
/*     */             continue;
/*     */           } 
/* 157 */           throw new SaslException("'=' expected after key: " + key.toString());
/*     */         } 
/*     */         
/* 160 */         key.write(bch);
/* 161 */         i++; continue;
/*     */       } 
/* 163 */       if (gettingQuotedValue) {
/*     */         
/* 165 */         if (bch == 92) {
/*     */           
/* 167 */           i++;
/* 168 */           if (i < buf.length) {
/* 169 */             value.write(buf[i]);
/* 170 */             i++;
/*     */             continue;
/*     */           } 
/* 173 */           throw new SaslException("Unmatched quote found for directive: " + key.toString() + " with value: " + value.toString());
/*     */         } 
/*     */         
/* 176 */         if (bch == 34) {
/*     */           
/* 178 */           i++;
/* 179 */           gettingQuotedValue = false;
/* 180 */           expectSeparator = true; continue;
/*     */         } 
/* 182 */         value.write(bch);
/* 183 */         i++; continue;
/*     */       } 
/* 185 */       if (isLws(bch) || bch == 44) {
/*     */         
/* 187 */         extractDirective(map, key.toString(), value.toString());
/* 188 */         key.reset();
/* 189 */         value.reset();
/* 190 */         gettingKey = true;
/* 191 */         gettingQuotedValue = expectSeparator = false;
/* 192 */         i = skipLws(buf, i + 1); continue;
/* 193 */       }  if (expectSeparator) {
/* 194 */         throw new SaslException("Expecting comma or linear whitespace after quoted string: \"" + value.toString() + "\"");
/*     */       }
/*     */       
/* 197 */       value.write(bch);
/* 198 */       i++;
/*     */     } 
/*     */ 
/*     */     
/* 202 */     if (gettingQuotedValue) {
/* 203 */       throw new SaslException("Unmatched quote found for directive: " + key.toString() + " with value: " + value.toString());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 208 */     if (key.size() > 0) {
/* 209 */       extractDirective(map, key.toString(), value.toString());
/*     */     }
/*     */     
/* 212 */     return map;
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
/*     */   private static void extractDirective(HashMap<String, String> map, String key, String value) throws SaslException {
/* 225 */     if (map.get(key) != null) {
/* 226 */       throw new SaslException("Peer sent more than one " + key + " directive");
/*     */     }
/*     */     
/* 229 */     map.put(key, value);
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
/*     */   public static boolean isLws(byte b) {
/* 241 */     switch (b) {
/*     */       case 9:
/*     */       case 10:
/*     */       case 13:
/*     */       case 32:
/* 246 */         return true;
/*     */     } 
/*     */     
/* 249 */     return false;
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
/*     */   private static int skipLws(byte[] buf, int start) {
/*     */     int i;
/* 262 */     for (i = start; i < buf.length; i++) {
/* 263 */       if (!isLws(buf[i])) {
/* 264 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 268 */     return i;
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
/*     */   public static String stringTo8859_1(String str) throws UnsupportedEncodingException {
/* 280 */     if (str == null) {
/* 281 */       return "";
/*     */     }
/*     */     
/* 284 */     return new String(str.getBytes("UTF8"), "8859_1");
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
/*     */   public static String getSingleValuedHeader(Map<String, List<String>> headers, String key) {
/* 296 */     List<String> values = headers.get(key);
/*     */     
/* 298 */     if (values == null) {
/* 299 */       return null;
/*     */     }
/*     */     
/* 302 */     if (values.size() > 1) {
/* 303 */       throw new IllegalArgumentException("Header with key [\"" + key + "\"] isn't single valued !");
/*     */     }
/*     */     
/* 306 */     return values.get(0);
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
/*     */   public static void addValueToHeader(Map<String, List<String>> headers, String key, String value, boolean singleValued) {
/* 321 */     List<String> values = headers.get(key);
/*     */     
/* 323 */     if (values == null) {
/* 324 */       values = new ArrayList<String>(1);
/* 325 */       headers.put(key, values);
/*     */     } 
/*     */     
/* 328 */     if (singleValued && values.size() == 1) {
/* 329 */       values.set(0, value);
/*     */     } else {
/* 331 */       values.add(value);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/utils/StringUtilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */