/*     */ package org.apache.http.conn.ssl;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.ldap.LdapName;
/*     */ import javax.naming.ldap.Rdn;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.conn.util.InetAddressUtils;
/*     */ import org.apache.http.conn.util.PublicSuffixMatcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class DefaultHostnameVerifier
/*     */   implements HostnameVerifier
/*     */ {
/*     */   static final int DNS_NAME_TYPE = 2;
/*     */   static final int IP_ADDRESS_TYPE = 7;
/*  69 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final PublicSuffixMatcher publicSuffixMatcher;
/*     */   
/*     */   public DefaultHostnameVerifier(PublicSuffixMatcher publicSuffixMatcher) {
/*  74 */     this.publicSuffixMatcher = publicSuffixMatcher;
/*     */   }
/*     */   
/*     */   public DefaultHostnameVerifier() {
/*  78 */     this(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean verify(String host, SSLSession session) {
/*     */     try {
/*  84 */       Certificate[] certs = session.getPeerCertificates();
/*  85 */       X509Certificate x509 = (X509Certificate)certs[0];
/*  86 */       verify(host, x509);
/*  87 */       return true;
/*  88 */     } catch (SSLException ex) {
/*  89 */       if (this.log.isDebugEnabled()) {
/*  90 */         this.log.debug(ex.getMessage(), ex);
/*     */       }
/*  92 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void verify(String host, X509Certificate cert) throws SSLException {
/*  98 */     boolean ipv4 = InetAddressUtils.isIPv4Address(host);
/*  99 */     boolean ipv6 = InetAddressUtils.isIPv6Address(host);
/* 100 */     int subjectType = (ipv4 || ipv6) ? 7 : 2;
/* 101 */     List<String> subjectAlts = extractSubjectAlts(cert, subjectType);
/* 102 */     if (subjectAlts != null && !subjectAlts.isEmpty()) {
/* 103 */       if (ipv4) {
/* 104 */         matchIPAddress(host, subjectAlts);
/* 105 */       } else if (ipv6) {
/* 106 */         matchIPv6Address(host, subjectAlts);
/*     */       } else {
/* 108 */         matchDNSName(host, subjectAlts, this.publicSuffixMatcher);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 113 */       X500Principal subjectPrincipal = cert.getSubjectX500Principal();
/* 114 */       String cn = extractCN(subjectPrincipal.getName("RFC2253"));
/* 115 */       if (cn == null) {
/* 116 */         throw new SSLException("Certificate subject for <" + host + "> doesn't contain " + "a common name and does not have alternative names");
/*     */       }
/*     */       
/* 119 */       matchCN(host, cn, this.publicSuffixMatcher);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void matchIPAddress(String host, List<String> subjectAlts) throws SSLException {
/* 124 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 125 */       String subjectAlt = subjectAlts.get(i);
/* 126 */       if (host.equals(subjectAlt)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 130 */     throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */   
/*     */   static void matchIPv6Address(String host, List<String> subjectAlts) throws SSLException {
/* 135 */     String normalisedHost = normaliseAddress(host);
/* 136 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 137 */       String subjectAlt = subjectAlts.get(i);
/* 138 */       String normalizedSubjectAlt = normaliseAddress(subjectAlt);
/* 139 */       if (normalisedHost.equals(normalizedSubjectAlt)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 143 */     throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void matchDNSName(String host, List<String> subjectAlts, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
/* 149 */     String normalizedHost = host.toLowerCase(Locale.ROOT);
/* 150 */     for (int i = 0; i < subjectAlts.size(); i++) {
/* 151 */       String subjectAlt = subjectAlts.get(i);
/* 152 */       String normalizedSubjectAlt = subjectAlt.toLowerCase(Locale.ROOT);
/* 153 */       if (matchIdentityStrict(normalizedHost, normalizedSubjectAlt, publicSuffixMatcher)) {
/*     */         return;
/*     */       }
/*     */     } 
/* 157 */     throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void matchCN(String host, String cn, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
/* 163 */     if (!matchIdentityStrict(host, cn, publicSuffixMatcher)) {
/* 164 */       throw new SSLException("Certificate for <" + host + "> doesn't match " + "common name of the certificate subject: " + cn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchDomainRoot(String host, String domainRoot) {
/* 170 */     if (domainRoot == null) {
/* 171 */       return false;
/*     */     }
/* 173 */     return (host.endsWith(domainRoot) && (host.length() == domainRoot.length() || host.charAt(host.length() - domainRoot.length() - 1) == '.'));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher, boolean strict) {
/* 180 */     if (publicSuffixMatcher != null && host.contains(".") && 
/* 181 */       !matchDomainRoot(host, publicSuffixMatcher.getDomainRoot(identity))) {
/* 182 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     int asteriskIdx = identity.indexOf('*');
/* 192 */     if (asteriskIdx != -1) {
/* 193 */       String prefix = identity.substring(0, asteriskIdx);
/* 194 */       String suffix = identity.substring(asteriskIdx + 1);
/* 195 */       if (!prefix.isEmpty() && !host.startsWith(prefix)) {
/* 196 */         return false;
/*     */       }
/* 198 */       if (!suffix.isEmpty() && !host.endsWith(suffix)) {
/* 199 */         return false;
/*     */       }
/*     */       
/* 202 */       if (strict) {
/* 203 */         String remainder = host.substring(prefix.length(), host.length() - suffix.length());
/*     */         
/* 205 */         if (remainder.contains(".")) {
/* 206 */           return false;
/*     */         }
/*     */       } 
/* 209 */       return true;
/*     */     } 
/* 211 */     return host.equalsIgnoreCase(identity);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
/* 216 */     return matchIdentity(host, identity, publicSuffixMatcher, false);
/*     */   }
/*     */   
/*     */   static boolean matchIdentity(String host, String identity) {
/* 220 */     return matchIdentity(host, identity, null, false);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
/* 225 */     return matchIdentity(host, identity, publicSuffixMatcher, true);
/*     */   }
/*     */   
/*     */   static boolean matchIdentityStrict(String host, String identity) {
/* 229 */     return matchIdentity(host, identity, null, true);
/*     */   }
/*     */   
/*     */   static String extractCN(String subjectPrincipal) throws SSLException {
/* 233 */     if (subjectPrincipal == null) {
/* 234 */       return null;
/*     */     }
/*     */     try {
/* 237 */       LdapName subjectDN = new LdapName(subjectPrincipal);
/* 238 */       List<Rdn> rdns = subjectDN.getRdns();
/* 239 */       for (int i = rdns.size() - 1; i >= 0; i--) {
/* 240 */         Rdn rds = rdns.get(i);
/* 241 */         Attributes attributes = rds.toAttributes();
/* 242 */         Attribute cn = attributes.get("cn");
/* 243 */         if (cn != null) {
/*     */           
/* 245 */           try { Object value = cn.get();
/* 246 */             if (value != null) {
/* 247 */               return value.toString();
/*     */             } }
/* 249 */           catch (NoSuchElementException ignore) {  }
/* 250 */           catch (NamingException ignore) {}
/*     */         }
/*     */       } 
/*     */       
/* 254 */       return null;
/* 255 */     } catch (InvalidNameException e) {
/* 256 */       throw new SSLException(subjectPrincipal + " is not a valid X500 distinguished name");
/*     */     } 
/*     */   }
/*     */   
/*     */   static List<String> extractSubjectAlts(X509Certificate cert, int subjectType) {
/* 261 */     Collection<List<?>> c = null;
/*     */     try {
/* 263 */       c = cert.getSubjectAlternativeNames();
/* 264 */     } catch (CertificateParsingException ignore) {}
/*     */     
/* 266 */     List<String> subjectAltList = null;
/* 267 */     if (c != null) {
/* 268 */       for (List<?> aC : c) {
/* 269 */         List<?> list = aC;
/* 270 */         int type = ((Integer)list.get(0)).intValue();
/* 271 */         if (type == subjectType) {
/* 272 */           String s = (String)list.get(1);
/* 273 */           if (subjectAltList == null) {
/* 274 */             subjectAltList = new ArrayList<String>();
/*     */           }
/* 276 */           subjectAltList.add(s);
/*     */         } 
/*     */       } 
/*     */     }
/* 280 */     return subjectAltList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String normaliseAddress(String hostname) {
/* 287 */     if (hostname == null) {
/* 288 */       return hostname;
/*     */     }
/*     */     try {
/* 291 */       InetAddress inetAddress = InetAddress.getByName(hostname);
/* 292 */       return inetAddress.getHostAddress();
/* 293 */     } catch (UnknownHostException unexpected) {
/* 294 */       return hostname;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/conn/ssl/DefaultHostnameVerifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */