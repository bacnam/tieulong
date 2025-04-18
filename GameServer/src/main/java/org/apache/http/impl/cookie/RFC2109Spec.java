package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.annotation.Obsolete;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Obsolete
@ThreadSafe
public class RFC2109Spec
extends CookieSpecBase
{
static final String[] DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };

private final boolean oneHeader;

public RFC2109Spec(String[] datepatterns, boolean oneHeader) {
super(new CommonCookieAttributeHandler[] { new RFC2109VersionHandler(), new BasicPathHandler(), new RFC2109DomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new BasicExpiresHandler((datepatterns != null) ? (String[])datepatterns.clone() : DATE_PATTERNS) });

this.oneHeader = oneHeader;
}

public RFC2109Spec() {
this((String[])null, false);
}

protected RFC2109Spec(boolean oneHeader, CommonCookieAttributeHandler... handlers) {
super(handlers);
this.oneHeader = oneHeader;
}

public List<Cookie> parse(Header header, CookieOrigin origin) throws MalformedCookieException {
Args.notNull(header, "Header");
Args.notNull(origin, "Cookie origin");
if (!header.getName().equalsIgnoreCase("Set-Cookie")) {
throw new MalformedCookieException("Unrecognized cookie header '" + header.toString() + "'");
}

HeaderElement[] elems = header.getElements();
return parse(elems, origin);
}

public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
Args.notNull(cookie, "Cookie");
String name = cookie.getName();
if (name.indexOf(' ') != -1) {
throw new CookieRestrictionViolationException("Cookie name may not contain blanks");
}
if (name.startsWith("$")) {
throw new CookieRestrictionViolationException("Cookie name may not start with $");
}
super.validate(cookie, origin);
}

public List<Header> formatCookies(List<Cookie> cookies) {
List<Cookie> cookieList;
Args.notEmpty(cookies, "List of cookies");

if (cookies.size() > 1) {

cookieList = new ArrayList<Cookie>(cookies);
Collections.sort(cookieList, (Comparator<? super Cookie>)CookiePathComparator.INSTANCE);
} else {
cookieList = cookies;
} 
if (this.oneHeader) {
return doFormatOneHeader(cookieList);
}
return doFormatManyHeaders(cookieList);
}

private List<Header> doFormatOneHeader(List<Cookie> cookies) {
int version = Integer.MAX_VALUE;

for (Cookie cookie : cookies) {
if (cookie.getVersion() < version) {
version = cookie.getVersion();
}
} 
CharArrayBuffer buffer = new CharArrayBuffer(40 * cookies.size());
buffer.append("Cookie");
buffer.append(": ");
buffer.append("$Version=");
buffer.append(Integer.toString(version));
for (Cookie cooky : cookies) {
buffer.append("; ");
Cookie cookie = cooky;
formatCookieAsVer(buffer, cookie, version);
} 
List<Header> headers = new ArrayList<Header>(1);
headers.add(new BufferedHeader(buffer));
return headers;
}

private List<Header> doFormatManyHeaders(List<Cookie> cookies) {
List<Header> headers = new ArrayList<Header>(cookies.size());
for (Cookie cookie : cookies) {
int version = cookie.getVersion();
CharArrayBuffer buffer = new CharArrayBuffer(40);
buffer.append("Cookie: ");
buffer.append("$Version=");
buffer.append(Integer.toString(version));
buffer.append("; ");
formatCookieAsVer(buffer, cookie, version);
headers.add(new BufferedHeader(buffer));
} 
return headers;
}

protected void formatParamAsVer(CharArrayBuffer buffer, String name, String value, int version) {
buffer.append(name);
buffer.append("=");
if (value != null) {
if (version > 0) {
buffer.append('"');
buffer.append(value);
buffer.append('"');
} else {
buffer.append(value);
} 
}
}

protected void formatCookieAsVer(CharArrayBuffer buffer, Cookie cookie, int version) {
formatParamAsVer(buffer, cookie.getName(), cookie.getValue(), version);
if (cookie.getPath() != null && 
cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("path")) {

buffer.append("; ");
formatParamAsVer(buffer, "$Path", cookie.getPath(), version);
} 

if (cookie.getDomain() != null && 
cookie instanceof ClientCookie && ((ClientCookie)cookie).containsAttribute("domain")) {

buffer.append("; ");
formatParamAsVer(buffer, "$Domain", cookie.getDomain(), version);
} 
}

public int getVersion() {
return 1;
}

public Header getVersionHeader() {
return null;
}

public String toString() {
return "rfc2109";
}
}

