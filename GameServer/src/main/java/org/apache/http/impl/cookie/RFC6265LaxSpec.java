package org.apache.http.impl.cookie;

import java.util.List;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;

@ThreadSafe
public class RFC6265LaxSpec
extends RFC6265CookieSpecBase
{
public RFC6265LaxSpec() {
super(new CommonCookieAttributeHandler[] { new BasicPathHandler(), new BasicDomainHandler(), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler() });
}

RFC6265LaxSpec(CommonCookieAttributeHandler... handlers) {
super(handlers);
}

public String toString() {
return "rfc6265-lax";
}
}

