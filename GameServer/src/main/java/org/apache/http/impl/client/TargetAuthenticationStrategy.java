package org.apache.http.impl.client;

import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;

import java.util.Collection;

@Immutable
public class TargetAuthenticationStrategy
        extends AuthenticationStrategyImpl {
    public static final TargetAuthenticationStrategy INSTANCE = new TargetAuthenticationStrategy();

    public TargetAuthenticationStrategy() {
        super(401, "WWW-Authenticate");
    }

    Collection<String> getPreferredAuthSchemes(RequestConfig config) {
        return config.getTargetPreferredAuthSchemes();
    }
}

