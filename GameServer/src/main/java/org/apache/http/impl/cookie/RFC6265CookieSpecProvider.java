package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;

@Immutable
public class RFC6265CookieSpecProvider
implements CookieSpecProvider
{
private final CompatibilityLevel compatibilityLevel;
private final PublicSuffixMatcher publicSuffixMatcher;
private volatile CookieSpec cookieSpec;

public enum CompatibilityLevel
{
STRICT,
RELAXED,
IE_MEDIUM_SECURITY;
}

public RFC6265CookieSpecProvider(CompatibilityLevel compatibilityLevel, PublicSuffixMatcher publicSuffixMatcher) {
this.compatibilityLevel = (compatibilityLevel != null) ? compatibilityLevel : CompatibilityLevel.RELAXED;
this.publicSuffixMatcher = publicSuffixMatcher;
}

public RFC6265CookieSpecProvider(PublicSuffixMatcher publicSuffixMatcher) {
this(CompatibilityLevel.RELAXED, publicSuffixMatcher);
}

public RFC6265CookieSpecProvider() {
this(CompatibilityLevel.RELAXED, null);
}

public CookieSpec create(HttpContext context) {
if (this.cookieSpec == null) {
synchronized (this) {
if (this.cookieSpec == null) {
switch (this.compatibilityLevel) {
case STRICT:
this.cookieSpec = new RFC6265StrictSpec(new CommonCookieAttributeHandler[] { new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(RFC6265StrictSpec.DATE_PATTERNS) });
break;

case IE_MEDIUM_SECURITY:
this.cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[] { new BasicPathHandler() { public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {} }, PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(RFC6265StrictSpec.DATE_PATTERNS) });
break;

default:
this.cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[] { new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler() });
break;
} 

}
} 
}
return this.cookieSpec;
}
}

