package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.PublicSuffixList;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.*;
import org.apache.http.util.Args;

@Immutable
public class PublicSuffixDomainFilter
        implements CommonCookieAttributeHandler {
    private final CommonCookieAttributeHandler handler;
    private final PublicSuffixMatcher publicSuffixMatcher;

    public PublicSuffixDomainFilter(CommonCookieAttributeHandler handler, PublicSuffixMatcher publicSuffixMatcher) {
        this.handler = (CommonCookieAttributeHandler) Args.notNull(handler, "Cookie handler");
        this.publicSuffixMatcher = (PublicSuffixMatcher) Args.notNull(publicSuffixMatcher, "Public suffix matcher");
    }

    public PublicSuffixDomainFilter(CommonCookieAttributeHandler handler, PublicSuffixList suffixList) {
        Args.notNull(handler, "Cookie handler");
        Args.notNull(suffixList, "Public suffix list");
        this.handler = handler;
        this.publicSuffixMatcher = new PublicSuffixMatcher(suffixList.getRules(), suffixList.getExceptions());
    }

    public static CommonCookieAttributeHandler decorate(CommonCookieAttributeHandler handler, PublicSuffixMatcher publicSuffixMatcher) {
        Args.notNull(handler, "Cookie attribute handler");
        return (publicSuffixMatcher != null) ? new PublicSuffixDomainFilter(handler, publicSuffixMatcher) : handler;
    }

    public boolean match(Cookie cookie, CookieOrigin origin) {
        String domain = cookie.getDomain();
        if (!domain.equalsIgnoreCase("localhost") && this.publicSuffixMatcher.matches(domain)) {
            return false;
        }
        return this.handler.match(cookie, origin);
    }

    public void parse(SetCookie cookie, String value) throws MalformedCookieException {
        this.handler.parse(cookie, value);
    }

    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        this.handler.validate(cookie, origin);
    }

    public String getAttributeName() {
        return this.handler.getAttributeName();
    }
}

