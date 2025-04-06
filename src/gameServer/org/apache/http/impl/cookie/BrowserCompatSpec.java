/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.cookie.CommonCookieAttributeHandler;
/*     */ import org.apache.http.cookie.Cookie;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieOrigin;
/*     */ import org.apache.http.cookie.MalformedCookieException;
/*     */ import org.apache.http.message.BasicHeaderElement;
/*     */ import org.apache.http.message.BasicHeaderValueFormatter;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class BrowserCompatSpec
/*     */   extends CookieSpecBase
/*     */ {
/*  67 */   private static final String[] DEFAULT_DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BrowserCompatSpec(String[] datepatterns, BrowserCompatSpecFactory.SecurityLevel securityLevel) {
/*  86 */     super(new CommonCookieAttributeHandler[] { new BrowserCompatVersionAttributeHandler(), new BasicDomainHandler(), (securityLevel == BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_IE_MEDIUM) ? new BasicPathHandler() { public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {} } : new BasicPathHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : DEFAULT_DATE_PATTERNS) });
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
/*     */   public BrowserCompatSpec(String[] datepatterns) {
/* 103 */     this(datepatterns, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
/*     */   }
/*     */ 
/*     */   
/*     */   public BrowserCompatSpec() {
/* 108 */     this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
/* 114 */     Args.notNull(header, "Header");
/* 115 */     Args.notNull(origin, "Cookie origin");
/* 116 */     String headername = header.getName();
/* 117 */     if (!headername.equalsIgnoreCase("Set-Cookie")) {
/* 118 */       throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
/*     */     }
/*     */     
/* 121 */     HeaderElement[] helems = header.getElements();
/* 122 */     boolean versioned = false;
/* 123 */     boolean netscape = false;
/* 124 */     for (HeaderElement helem : helems) {
/* 125 */       if (helem.getParameterByName("version") != null) {
/* 126 */         versioned = true;
/*     */       }
/* 128 */       if (helem.getParameterByName("expires") != null) {
/* 129 */         netscape = true;
/*     */       }
/*     */     } 
/* 132 */     if (netscape || !versioned) {
/*     */       CharArrayBuffer buffer;
/*     */       ParserCursor cursor;
/* 135 */       NetscapeDraftHeaderParser parser = NetscapeDraftHeaderParser.DEFAULT;
/*     */ 
/*     */       
/* 138 */       if (header instanceof FormattedHeader) {
/* 139 */         buffer = ((FormattedHeader)header).getBuffer();
/* 140 */         cursor = new ParserCursor(((FormattedHeader)header).getValuePos(), buffer.length());
/*     */       }
/*     */       else {
/*     */         
/* 144 */         String s = header.getValue();
/* 145 */         if (s == null) {
/* 146 */           throw new MalformedCookieException("Header value is null");
/*     */         }
/* 148 */         buffer = new CharArrayBuffer(s.length());
/* 149 */         buffer.append(s);
/* 150 */         cursor = new ParserCursor(0, buffer.length());
/*     */       } 
/* 152 */       HeaderElement elem = parser.parseHeader(buffer, cursor);
/* 153 */       String name = elem.getName();
/* 154 */       String value = elem.getValue();
/* 155 */       if (name == null || name.isEmpty()) {
/* 156 */         throw new MalformedCookieException("Cookie name may not be empty");
/*     */       }
/* 158 */       BasicClientCookie cookie = new BasicClientCookie(name, value);
/* 159 */       cookie.setPath(getDefaultPath(origin));
/* 160 */       cookie.setDomain(getDefaultDomain(origin));
/*     */ 
/*     */       
/* 163 */       NameValuePair[] attribs = elem.getParameters();
/* 164 */       for (int j = attribs.length - 1; j >= 0; j--) {
/* 165 */         NameValuePair attrib = attribs[j];
/* 166 */         String s = attrib.getName().toLowerCase(Locale.ROOT);
/* 167 */         cookie.setAttribute(s, attrib.getValue());
/* 168 */         CookieAttributeHandler handler = findAttribHandler(s);
/* 169 */         if (handler != null) {
/* 170 */           handler.parse(cookie, attrib.getValue());
/*     */         }
/*     */       } 
/*     */       
/* 174 */       if (netscape) {
/* 175 */         cookie.setVersion(0);
/*     */       }
/* 177 */       return (List)Collections.singletonList(cookie);
/*     */     } 
/* 179 */     return parse(helems, origin);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isQuoteEnclosed(String s) {
/* 184 */     return (s != null && s.startsWith("\"") && s.endsWith("\""));
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Header> formatCookies(List<Cookie> cookies) {
/* 189 */     Args.notEmpty(cookies, "List of cookies");
/* 190 */     CharArrayBuffer buffer = new CharArrayBuffer(20 * cookies.size());
/* 191 */     buffer.append("Cookie");
/* 192 */     buffer.append(": ");
/* 193 */     for (int i = 0; i < cookies.size(); i++) {
/* 194 */       Cookie cookie = cookies.get(i);
/* 195 */       if (i > 0) {
/* 196 */         buffer.append("; ");
/*     */       }
/* 198 */       String cookieName = cookie.getName();
/* 199 */       String cookieValue = cookie.getValue();
/* 200 */       if (cookie.getVersion() > 0 && !isQuoteEnclosed(cookieValue)) {
/* 201 */         BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(buffer, (HeaderElement)new BasicHeaderElement(cookieName, cookieValue), false);
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/* 207 */         buffer.append(cookieName);
/* 208 */         buffer.append("=");
/* 209 */         if (cookieValue != null) {
/* 210 */           buffer.append(cookieValue);
/*     */         }
/*     */       } 
/*     */     } 
/* 214 */     List<Header> headers = new ArrayList<Header>(1);
/* 215 */     headers.add(new BufferedHeader(buffer));
/* 216 */     return headers;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVersion() {
/* 221 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Header getVersionHeader() {
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 231 */     return "compatibility";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/cookie/BrowserCompatSpec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */