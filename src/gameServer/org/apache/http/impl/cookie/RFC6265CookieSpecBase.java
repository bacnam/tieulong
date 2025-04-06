/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.BitSet;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.CookiePriorityComparator;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.message.TokenParser;
/*     */ import org.apache.http.util.Args;
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
/*     */ @ThreadSafe
/*     */ class RFC6265CookieSpecBase
/*     */   implements CookieSpec
/*     */ {
/*     */   private static final char PARAM_DELIMITER = ';';
/*     */   private static final char COMMA_CHAR = ',';
/*     */   private static final char EQUAL_CHAR = '=';
/*     */   private static final char DQUOTE_CHAR = '"';
/*     */   private static final char ESCAPE_CHAR = '\\';
/*  74 */   private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(new int[] { 61, 59 });
/*  75 */   private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(new int[] { 59 });
/*  76 */   private static final BitSet SPECIAL_CHARS = TokenParser.INIT_BITSET(new int[] { 32, 34, 44, 59, 92 });
/*     */   
/*     */   private final CookieAttributeHandler[] attribHandlers;
/*     */   
/*     */   private final Map<String, CookieAttributeHandler> attribHandlerMap;
/*     */   
/*     */   private final TokenParser tokenParser;
/*     */   
/*     */   RFC6265CookieSpecBase(CommonCookieAttributeHandler... handlers) {
/*  85 */     this.attribHandlers = (CookieAttributeHandler[])handlers.clone();
/*  86 */     this.attribHandlerMap = new ConcurrentHashMap<String, CookieAttributeHandler>(handlers.length);
/*  87 */     for (CommonCookieAttributeHandler handler : handlers) {
/*  88 */       this.attribHandlerMap.put(handler.getAttributeName().toLowerCase(Locale.ROOT), handler);
/*     */     }
/*  90 */     this.tokenParser = TokenParser.INSTANCE;
/*     */   }
/*     */   
/*     */   static String getDefaultPath(CookieOrigin origin) {
/*  94 */     String defaultPath = origin.getPath();
/*  95 */     int lastSlashIndex = defaultPath.lastIndexOf('/');
/*  96 */     if (lastSlashIndex >= 0) {
/*  97 */       if (lastSlashIndex == 0)
/*     */       {
/*  99 */         lastSlashIndex = 1;
/*     */       }
/* 101 */       defaultPath = defaultPath.substring(0, lastSlashIndex);
/*     */     } 
/* 103 */     return defaultPath;
/*     */   }
/*     */   
/*     */   static String getDefaultDomain(CookieOrigin origin) {
/* 107 */     return origin.getHost();
/*     */   }
/*     */   public final List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/*     */     CharArrayBuffer buffer;
/*     */     ParserCursor cursor;
/* 112 */     Args.notNull(header, "Header");
/* 113 */     Args.notNull(origin, "Cookie origin");
/* 114 */     if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
/* 115 */       throw new MalformedCookieException("Unrecognized cookie header: '" + header.toString() + "'");
/*     */     }
/*     */ 
/*     */     
/* 119 */     if (header instanceof FormattedHeader) {
/* 120 */       buffer = ((FormattedHeader)header).getBuffer();
/* 121 */       cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */     } else {
/* 123 */       String s = header.getValue();
/* 124 */       if (s == null) {
/* 125 */         throw new MalformedCookieException("Header value is null");
/*     */       }
/* 127 */       buffer = new CharArrayBuffer(s.length());
/* 128 */       buffer.append(s);
/* 129 */       cursor = new ParserCursor(0, buffer.length());
/*     */     } 
/* 131 */     String name = this.tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
/* 132 */     if (name.length() == 0) {
/* 133 */       throw new MalformedCookieException("Cookie name is invalid: '" + header.toString() + "'");
/*     */     }
/* 135 */     if (cursor.atEnd()) {
/* 136 */       throw new MalformedCookieException("Cookie value is invalid: '" + header.toString() + "'");
/*     */     }
/* 138 */     int valueDelim = buffer.charAt(cursor.getPos());
/* 139 */     cursor.updatePos(cursor.getPos() + 1);
/* 140 */     if (valueDelim != 61) {
/* 141 */       throw new MalformedCookieException("Cookie value is invalid: '" + header.toString() + "'");
/*     */     }
/* 143 */     String value = this.tokenParser.parseValue(buffer, cursor, VALUE_DELIMS);
/* 144 */     if (!cursor.atEnd()) {
/* 145 */       cursor.updatePos(cursor.getPos() + 1);
/*     */     }
/* 147 */     BasicClientCookie cookie = new BasicClientCookie(name, value);
/* 148 */     cookie.setPath(getDefaultPath(origin));
/* 149 */     cookie.setDomain(getDefaultDomain(origin));
/* 150 */     cookie.setCreationDate(new Date());
/*     */     
/* 152 */     Map<String, String> attribMap = new LinkedHashMap<String, String>();
/* 153 */     while (!cursor.atEnd()) {
/* 154 */       String paramName = this.tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
/* 155 */       String paramValue = null;
/* 156 */       if (!cursor.atEnd()) {
/* 157 */         int paramDelim = buffer.charAt(cursor.getPos());
/* 158 */         cursor.updatePos(cursor.getPos() + 1);
/* 159 */         if (paramDelim == 61) {
/* 160 */           paramValue = this.tokenParser.parseToken(buffer, cursor, VALUE_DELIMS);
/* 161 */           if (!cursor.atEnd()) {
/* 162 */             cursor.updatePos(cursor.getPos() + 1);
/*     */           }
/*     */         } 
/*     */       } 
/* 166 */       cookie.setAttribute(paramName.toLowerCase(Locale.ROOT), paramValue);
/* 167 */       attribMap.put(paramName, paramValue);
/*     */     } 
/*     */     
/* 170 */     if (attribMap.containsKey("max-age")) {
/* 171 */       attribMap.remove("expires");
/*     */     }
/*     */     
/* 174 */     for (Map.Entry<String, String> entry : attribMap.entrySet()) {
/* 175 */       String paramName = entry.getKey();
/* 176 */       String paramValue = entry.getValue();
/* 177 */       CookieAttributeHandler handler = this.attribHandlerMap.get(paramName);
/* 178 */       if (handler != null) {
/* 179 */         handler.parse(cookie, paramValue);
/*     */       }
/*     */     } 
/*     */     
/* 183 */     return (List)Collections.singletonList(cookie);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
/* 189 */     Args.notNull(cookie, "Cookie");
/* 190 */     Args.notNull(origin, "Cookie origin");
/* 191 */     for (CookieAttributeHandler handler : this.attribHandlers) {
/* 192 */       handler.validate(cookie, origin);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean match(Cookie cookie, CookieOrigin origin) {
/* 198 */     Args.notNull(cookie, "Cookie");
/* 199 */     Args.notNull(origin, "Cookie origin");
/* 200 */     for (CookieAttributeHandler handler : this.attribHandlers) {
/* 201 */       if (!handler.match(cookie, origin)) {
/* 202 */         return false;
/*     */       }
/*     */     } 
/* 205 */     return true;
/*     */   }
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/*     */     List<? extends Cookie> sortedCookies;
/* 210 */     Args.notEmpty(cookies, "List of cookies");
/*     */     
/* 212 */     if (cookies.size() > 1) {
/*     */       
/* 214 */       sortedCookies = new ArrayList<Cookie>(cookies);
/* 215 */       Collections.sort(sortedCookies, (Comparator<? super Cookie>)CookiePriorityComparator.INSTANCE);
/*     */     } else {
/* 217 */       sortedCookies = cookies;
/*     */     } 
/* 219 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * sortedCookies.size());
/* 220 */     buffer.append("Cookie");
/* 221 */     buffer.append(": ");
/* 222 */     for (int n = 0; n < sortedCookies.size(); n++) {
/* 223 */       Cookie cookie = sortedCookies.get(n);
/* 224 */       if (n > 0) {
/* 225 */         buffer.append(';');
/* 226 */         buffer.append(' ');
/*     */       } 
/* 228 */       buffer.append(cookie.getName());
/* 229 */       String s = cookie.getValue();
/* 230 */       if (s != null) {
/* 231 */         buffer.append('=');
/* 232 */         if (containsSpecialChar(s)) {
/* 233 */           buffer.append('"');
/* 234 */           for (int i = 0; i < s.length(); i++) {
/* 235 */             char ch = s.charAt(i);
/* 236 */             if (ch == '"' || ch == '\\') {
/* 237 */               buffer.append('\\');
/*     */             }
/* 239 */             buffer.append(ch);
/*     */           } 
/* 241 */           buffer.append('"');
/*     */         } else {
/* 243 */           buffer.append(s);
/*     */         } 
/*     */       } 
/*     */     } 
/* 247 */     List<Header> headers = new ArrayList<Header>(1);
/* 248 */     headers.add(new BufferedHeader(buffer));
/* 249 */     return headers;
/*     */   }
/*     */   
/*     */   boolean containsSpecialChar(CharSequence s) {
/* 253 */     return containsChars(s, SPECIAL_CHARS);
/*     */   }
/*     */   
/*     */   boolean containsChars(CharSequence s, BitSet chars) {
/* 257 */     for (int i = 0; i < s.length(); i++) {
/* 258 */       char ch = s.charAt(i);
/* 259 */       if (chars.get(ch)) {
/* 260 */         return true;
/*     */       }
/*     */     } 
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getVersion() {
/* 268 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Header getVersionHeader() {
/* 273 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/cookie/RFC6265CookieSpecBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */