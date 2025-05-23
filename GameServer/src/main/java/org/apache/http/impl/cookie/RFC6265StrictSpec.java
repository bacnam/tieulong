package org.apache.http.impl.cookie;

import java.util.List;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;

@ThreadSafe
public class RFC6265StrictSpec
extends RFC6265CookieSpecBase
{
static final String[] DATE_PATTERNS = new String[] { "EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy" };

public RFC6265StrictSpec() {
super(new CommonCookieAttributeHandler[] { new BasicPathHandler(), new BasicDomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(DATE_PATTERNS) });
}

RFC6265StrictSpec(CommonCookieAttributeHandler... handlers) {
super(handlers);
}

public String toString() {
return "rfc6265-strict";
}
}

