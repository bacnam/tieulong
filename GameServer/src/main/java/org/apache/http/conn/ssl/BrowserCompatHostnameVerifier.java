package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;
import org.apache.http.annotation.Immutable;

@Deprecated
@Immutable
public class BrowserCompatHostnameVerifier
extends AbstractVerifier
{
public static final BrowserCompatHostnameVerifier INSTANCE = new BrowserCompatHostnameVerifier();

public final void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
verify(host, cns, subjectAlts, false);
}

public final String toString() {
return "BROWSER_COMPATIBLE";
}
}

