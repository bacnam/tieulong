package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;

import java.util.Collection;

@Immutable
public class ProxyAuthenticationStrategy
        extends AuthenticationStrategyImpl {
    public static final ProxyAuthenticationStrategy INSTANCE = new ProxyAuthenticationStrategy();

    public ProxyAuthenticationStrategy() {
        super(407, "Proxy-Authenticate");
    }

    Collection<String> getPreferredAuthSchemes(RequestConfig config) {
        return config.getProxyPreferredAuthSchemes();
    }
}

