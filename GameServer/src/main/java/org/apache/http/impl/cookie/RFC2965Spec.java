package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.annotation.Obsolete;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Obsolete
@ThreadSafe
public class RFC2965Spec
extends RFC2109Spec
{
public RFC2965Spec() {
this((String[])null, false);
}

public RFC2965Spec(String[] datepatterns, boolean oneHeader) {
super(oneHeader, new CommonCookieAttributeHandler[] { new RFC2965VersionAttributeHandler(), new BasicPathHandler(), new RFC2965DomainAttributeHandler(), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : DATE_PATTERNS), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler() });
}

RFC2965Spec(boolean oneHeader, CommonCookieAttributeHandler... handlers) {
super(oneHeader, handlers);
}

public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
Args.notNull(header, "Header");
Args.notNull(origin, "Cookie origin");
if (!header.getName().equalsIgnoreCase("Set-Cookie2")) {
throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
}

HeaderElement[] elems = header.getElements();
return createCookies(elems, adjustEffectiveHost(origin));
}

protected List<Cookie> parse(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
return createCookies(elems, adjustEffectiveHost(origin));
}

private List<Cookie> createCookies(HeaderElement[] elems, CookieOrigin origin) throws MalformedCookieException {
List<Cookie> cookies = new ArrayList<Cookie>(elems.length);
for (HeaderElement headerelement : elems) {
String name = headerelement.getName();
String value = headerelement.getValue();
if (name == null || name.isEmpty()) {
throw new MalformedCookieException("Cookie name may not be empty");
}

BasicClientCookie2 cookie = new BasicClientCookie2(name, value);
cookie.setPath(getDefaultPath(origin));
cookie.setDomain(getDefaultDomain(origin));
cookie.setPorts(new int[] { origin.getPort() });

NameValuePair[] attribs = headerelement.getParameters();

Map<String, NameValuePair> attribmap = new HashMap<String, NameValuePair>(attribs.length);

for (int j = attribs.length - 1; j >= 0; j--) {
NameValuePair param = attribs[j];
attribmap.put(param.getName().toLowerCase(Locale.ROOT), param);
} 
for (Map.Entry<String, NameValuePair> entry : attribmap.entrySet()) {
NameValuePair attrib = entry.getValue();
String s = attrib.getName().toLowerCase(Locale.ROOT);

cookie.setAttribute(s, attrib.getValue());

CookieAttributeHandler handler = findAttribHandler(s);
if (handler != null) {
handler.parse(cookie, attrib.getValue());
}
} 
cookies.add(cookie);
} 
return cookies;
}

public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
Args.notNull(cookie, "Cookie");
Args.notNull(origin, "Cookie origin");
super.validate(cookie, adjustEffectiveHost(origin));
}

public boolean match(Cookie cookie, CookieOrigin origin) {
Args.notNull(cookie, "Cookie");
Args.notNull(origin, "Cookie origin");
return super.match(cookie, adjustEffectiveHost(origin));
}

protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
super.formatCookieAsVer(buffer, cookie, version);

if (cookie instanceof ClientCookie) {

String s = ((ClientCookie)cookie).getAttribute("port");
if (s != null) {
buffer.append("; $Port");
buffer.append("=\"");
if (!s.trim().isEmpty()) {
int[] ports = cookie.getPorts();
if (ports != null) {
int len = ports.length;
for (int i = 0; i < len; i++) {
if (i > 0) {
buffer.append(",");
}
buffer.append(Integer.toString(ports[i]));
} 
} 
} 
buffer.append("\"");
} 
} 
}

private static CookieOrigin adjustEffectiveHost(CookieOrigin origin) {
String host = origin.getHost();

boolean isLocalHost = true;
for (int i = 0; i < host.length(); i++) {
char ch = host.charAt(i);
if (ch == '.' || ch == ':') {
isLocalHost = false;
break;
} 
} 
if (isLocalHost) {
host = host + ".local";
return new CookieOrigin(host, origin.getPort(), origin.getPath(), origin.isSecure());
} 

return origin;
}

public int getVersion() {
return 1;
}

public Header getVersionHeader() {
CharArrayBuffer buffer = new CharArrayBuffer(40);
buffer.append("Cookie2");
buffer.append(": ");
buffer.append("$Version=");
buffer.append(Integer.toString(getVersion()));
return (Header)new BufferedHeader(buffer);
}

public String toString() {
return "rfc2965";
}
}

