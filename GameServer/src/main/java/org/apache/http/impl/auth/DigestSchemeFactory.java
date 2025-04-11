package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.nio.charset.Charset;

@Immutable
public class DigestSchemeFactory
        implements AuthSchemeFactory, AuthSchemeProvider {
    private final Charset charset;

    public DigestSchemeFactory(Charset charset) {
        this.charset = charset;
    }

    public DigestSchemeFactory() {
        this(null);
    }

    public AuthScheme newInstance(HttpParams params) {
        return (AuthScheme) new DigestScheme();
    }

    public AuthScheme create(HttpContext context) {
        return (AuthScheme) new DigestScheme(this.charset);
    }
}

