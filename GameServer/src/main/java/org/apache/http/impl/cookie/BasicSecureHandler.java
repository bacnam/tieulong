package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.*;
import org.apache.http.util.Args;

@Immutable
public class BasicSecureHandler
        extends AbstractCookieAttributeHandler
        implements CommonCookieAttributeHandler {
    public void parse(SetCookie cookie, String value) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        cookie.setSecure(true);
    }

    public boolean match(Cookie cookie, CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        return (!cookie.isSecure() || origin.isSecure());
    }

    public String getAttributeName() {
        return "secure";
    }
}

