package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Deprecated
@Immutable
public class RFC2109SpecFactory
implements CookieSpecFactory, CookieSpecProvider
{
private final CookieSpec cookieSpec;

public RFC2109SpecFactory(String[] datepatterns, boolean oneHeader) {
this.cookieSpec = new RFC2109Spec(datepatterns, oneHeader);
}

public RFC2109SpecFactory() {
this(null, false);
}

public CookieSpec newInstance(HttpParams params) {
if (params != null) {

String[] patterns = null;
Collection<?> param = (Collection)params.getParameter("http.protocol.cookie-datepatterns");

if (param != null) {
patterns = new String[param.size()];
patterns = param.<String>toArray(patterns);
} 
boolean singleHeader = params.getBooleanParameter("http.protocol.single-cookie-header", false);

return new RFC2109Spec(patterns, singleHeader);
} 
return new RFC2109Spec();
}

public CookieSpec create(HttpContext context) {
return this.cookieSpec;
}
}

