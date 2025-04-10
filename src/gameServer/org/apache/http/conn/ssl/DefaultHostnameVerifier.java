package org.apache.http.conn.ssl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.Certificate;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.conn.util.PublicSuffixMatcher;

@Immutable
public final class DefaultHostnameVerifier
implements HostnameVerifier
{
static final int DNS_NAME_TYPE = 2;
static final int IP_ADDRESS_TYPE = 7;
private final Log log = LogFactory.getLog(getClass());

private final PublicSuffixMatcher publicSuffixMatcher;

public DefaultHostnameVerifier(PublicSuffixMatcher publicSuffixMatcher) {
this.publicSuffixMatcher = publicSuffixMatcher;
}

public DefaultHostnameVerifier() {
this(null);
}

public final boolean verify(String host, SSLSession session) {
try {
Certificate[] certs = session.getPeerCertificates();
X509Certificate x509 = (X509Certificate)certs[0];
verify(host, x509);
return true;
} catch (SSLException ex) {
if (this.log.isDebugEnabled()) {
this.log.debug(ex.getMessage(), ex);
}
return false;
} 
}

public final void verify(String host, X509Certificate cert) throws SSLException {
boolean ipv4 = InetAddressUtils.isIPv4Address(host);
boolean ipv6 = InetAddressUtils.isIPv6Address(host);
int subjectType = (ipv4 || ipv6) ? 7 : 2;
List<String> subjectAlts = extractSubjectAlts(cert, subjectType);
if (subjectAlts != null && !subjectAlts.isEmpty()) {
if (ipv4) {
matchIPAddress(host, subjectAlts);
} else if (ipv6) {
matchIPv6Address(host, subjectAlts);
} else {
matchDNSName(host, subjectAlts, this.publicSuffixMatcher);
}

} else {

X500Principal subjectPrincipal = cert.getSubjectX500Principal();
String cn = extractCN(subjectPrincipal.getName("RFC2253"));
if (cn == null) {
throw new SSLException("Certificate subject for <" + host + "> doesn't contain " + "a common name and does not have alternative names");
}

matchCN(host, cn, this.publicSuffixMatcher);
} 
}

static void matchIPAddress(String host, List<String> subjectAlts) throws SSLException {
for (int i = 0; i < subjectAlts.size(); i++) {
String subjectAlt = subjectAlts.get(i);
if (host.equals(subjectAlt)) {
return;
}
} 
throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
}

static void matchIPv6Address(String host, List<String> subjectAlts) throws SSLException {
String normalisedHost = normaliseAddress(host);
for (int i = 0; i < subjectAlts.size(); i++) {
String subjectAlt = subjectAlts.get(i);
String normalizedSubjectAlt = normaliseAddress(subjectAlt);
if (normalisedHost.equals(normalizedSubjectAlt)) {
return;
}
} 
throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
}

static void matchDNSName(String host, List<String> subjectAlts, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
String normalizedHost = host.toLowerCase(Locale.ROOT);
for (int i = 0; i < subjectAlts.size(); i++) {
String subjectAlt = subjectAlts.get(i);
String normalizedSubjectAlt = subjectAlt.toLowerCase(Locale.ROOT);
if (matchIdentityStrict(normalizedHost, normalizedSubjectAlt, publicSuffixMatcher)) {
return;
}
} 
throw new SSLException("Certificate for <" + host + "> doesn't match any " + "of the subject alternative names: " + subjectAlts);
}

static void matchCN(String host, String cn, PublicSuffixMatcher publicSuffixMatcher) throws SSLException {
if (!matchIdentityStrict(host, cn, publicSuffixMatcher)) {
throw new SSLException("Certificate for <" + host + "> doesn't match " + "common name of the certificate subject: " + cn);
}
}

static boolean matchDomainRoot(String host, String domainRoot) {
if (domainRoot == null) {
return false;
}
return (host.endsWith(domainRoot) && (host.length() == domainRoot.length() || host.charAt(host.length() - domainRoot.length() - 1) == '.'));
}

private static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher, boolean strict) {
if (publicSuffixMatcher != null && host.contains(".") && 
!matchDomainRoot(host, publicSuffixMatcher.getDomainRoot(identity))) {
return false;
}

int asteriskIdx = identity.indexOf('*');
if (asteriskIdx != -1) {
String prefix = identity.substring(0, asteriskIdx);
String suffix = identity.substring(asteriskIdx + 1);
if (!prefix.isEmpty() && !host.startsWith(prefix)) {
return false;
}
if (!suffix.isEmpty() && !host.endsWith(suffix)) {
return false;
}

if (strict) {
String remainder = host.substring(prefix.length(), host.length() - suffix.length());

if (remainder.contains(".")) {
return false;
}
} 
return true;
} 
return host.equalsIgnoreCase(identity);
}

static boolean matchIdentity(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
return matchIdentity(host, identity, publicSuffixMatcher, false);
}

static boolean matchIdentity(String host, String identity) {
return matchIdentity(host, identity, null, false);
}

static boolean matchIdentityStrict(String host, String identity, PublicSuffixMatcher publicSuffixMatcher) {
return matchIdentity(host, identity, publicSuffixMatcher, true);
}

static boolean matchIdentityStrict(String host, String identity) {
return matchIdentity(host, identity, null, true);
}

static String extractCN(String subjectPrincipal) throws SSLException {
if (subjectPrincipal == null) {
return null;
}
try {
LdapName subjectDN = new LdapName(subjectPrincipal);
List<Rdn> rdns = subjectDN.getRdns();
for (int i = rdns.size() - 1; i >= 0; i--) {
Rdn rds = rdns.get(i);
Attributes attributes = rds.toAttributes();
Attribute cn = attributes.get("cn");
if (cn != null) {

try { Object value = cn.get();
if (value != null) {
return value.toString();
} }
catch (NoSuchElementException ignore) {  }
catch (NamingException ignore) {}
}
} 

return null;
} catch (InvalidNameException e) {
throw new SSLException(subjectPrincipal + " is not a valid X500 distinguished name");
} 
}

static List<String> extractSubjectAlts(X509Certificate cert, int subjectType) {
Collection<List<?>> c = null;
try {
c = cert.getSubjectAlternativeNames();
} catch (CertificateParsingException ignore) {}

List<String> subjectAltList = null;
if (c != null) {
for (List<?> aC : c) {
List<?> list = aC;
int type = ((Integer)list.get(0)).intValue();
if (type == subjectType) {
String s = (String)list.get(1);
if (subjectAltList == null) {
subjectAltList = new ArrayList<String>();
}
subjectAltList.add(s);
} 
} 
}
return subjectAltList;
}

static String normaliseAddress(String hostname) {
if (hostname == null) {
return hostname;
}
try {
InetAddress inetAddress = InetAddress.getByName(hostname);
return inetAddress.getHostAddress();
} catch (UnknownHostException unexpected) {
return hostname;
} 
}
}

