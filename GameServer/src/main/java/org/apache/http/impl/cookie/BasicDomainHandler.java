package org.apache.http.impl.cookie;

import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.InetAddressUtils;
import org.apache.http.cookie.*;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

import java.util.Locale;

@Immutable
public class BasicDomainHandler
        implements CommonCookieAttributeHandler {
    static boolean domainMatch(String domain, String host) {
        if (InetAddressUtils.isIPv4Address(host) || InetAddressUtils.isIPv6Address(host)) {
            return false;
        }
        String normalizedDomain = domain.startsWith(".") ? domain.substring(1) : domain;
        if (host.endsWith(normalizedDomain)) {
            int prefix = host.length() - normalizedDomain.length();

            if (prefix == 0) {
                return true;
            }
            if (prefix > 1 && host.charAt(prefix - 1) == '.') {
                return true;
            }
        }
        return false;
    }

    public void parse(SetCookie cookie, String value) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        if (TextUtils.isBlank(value)) {
            throw new MalformedCookieException("Blank or null value for domain attribute");
        }

        if (value.endsWith(".")) {
            return;
        }
        String domain = value;
        if (domain.startsWith(".")) {
            domain = domain.substring(1);
        }
        domain = domain.toLowerCase(Locale.ROOT);
        cookie.setDomain(domain);
    }

    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");

        String host = origin.getHost();
        String domain = cookie.getDomain();
        if (domain == null) {
            throw new CookieRestrictionViolationException("Cookie 'domain' may not be null");
        }
        if (!host.equals(domain) && !domainMatch(domain, host)) {
            throw new CookieRestrictionViolationException("Illegal 'domain' attribute \"" + domain + "\". Domain of origin: \"" + host + "\"");
        }
    }

    public boolean match(Cookie cookie, CookieOrigin origin) {
        Args.notNull(cookie, "Cookie");
        Args.notNull(origin, "Cookie origin");
        String host = origin.getHost();
        String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }
        if (domain.startsWith(".")) {
            domain = domain.substring(1);
        }
        domain = domain.toLowerCase(Locale.ROOT);
        if (host.equals(domain)) {
            return true;
        }
        if (cookie instanceof ClientCookie && (
                (ClientCookie) cookie).containsAttribute("domain")) {
            return domainMatch(domain, host);
        }

        return false;
    }

    public String getAttributeName() {
        return "domain";
    }
}

