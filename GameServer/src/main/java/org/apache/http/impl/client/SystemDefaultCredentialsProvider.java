package org.apache.http.impl.client;

import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public class SystemDefaultCredentialsProvider
        implements CredentialsProvider {
    private static final Map<String, String> SCHEME_MAP = new ConcurrentHashMap<String, String>();

    static {
        SCHEME_MAP.put("Basic".toUpperCase(Locale.ROOT), "Basic");
        SCHEME_MAP.put("Digest".toUpperCase(Locale.ROOT), "Digest");
        SCHEME_MAP.put("NTLM".toUpperCase(Locale.ROOT), "NTLM");
        SCHEME_MAP.put("Negotiate".toUpperCase(Locale.ROOT), "SPNEGO");
        SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ROOT), "Kerberos");
    }

    private final BasicCredentialsProvider internal;

    public SystemDefaultCredentialsProvider() {
        this.internal = new BasicCredentialsProvider();
    }

    private static String translateScheme(String key) {
        if (key == null) {
            return null;
        }
        String s = SCHEME_MAP.get(key);
        return (s != null) ? s : key;
    }

    private static PasswordAuthentication getSystemCreds(AuthScope authscope, Authenticator.RequestorType requestorType) {
        String hostname = authscope.getHost();
        int port = authscope.getPort();
        HttpHost origin = authscope.getOrigin();
        String protocol = (origin != null) ? origin.getSchemeName() : ((port == 443) ? "https" : "http");

        return Authenticator.requestPasswordAuthentication(hostname, null, port, protocol, null, translateScheme(authscope.getScheme()), null, requestorType);
    }

    public void setCredentials(AuthScope authscope, Credentials credentials) {
        this.internal.setCredentials(authscope, credentials);
    }

    public Credentials getCredentials(AuthScope authscope) {
        Args.notNull(authscope, "Auth scope");
        Credentials localcreds = this.internal.getCredentials(authscope);
        if (localcreds != null) {
            return localcreds;
        }
        if (authscope.getHost() != null) {
            PasswordAuthentication systemcreds = getSystemCreds(authscope, Authenticator.RequestorType.SERVER);

            if (systemcreds == null) {
                systemcreds = getSystemCreds(authscope, Authenticator.RequestorType.PROXY);
            }

            if (systemcreds != null) {
                String domain = System.getProperty("http.auth.ntlm.domain");
                if (domain != null) {
                    return (Credentials) new NTCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()), null, domain);
                }

                if ("NTLM".equalsIgnoreCase(authscope.getScheme())) {
                    return (Credentials) new NTCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()), null, null);
                }

                return (Credentials) new UsernamePasswordCredentials(systemcreds.getUserName(), new String(systemcreds.getPassword()));
            }
        }

        return null;
    }

    public void clear() {
        this.internal.clear();
    }
}

