package org.apache.http.conn.util;

import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;

import java.net.IDN;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ThreadSafe
public final class PublicSuffixMatcher {
    private final Map<String, String> rules;
    private final Map<String, String> exceptions;

    public PublicSuffixMatcher(Collection<String> rules, Collection<String> exceptions) {
        Args.notNull(rules, "Domain suffix rules");
        this.rules = new ConcurrentHashMap<String, String>(rules.size());
        for (String rule : rules) {
            this.rules.put(rule, rule);
        }
        if (exceptions != null) {
            this.exceptions = new ConcurrentHashMap<String, String>(exceptions.size());
            for (String exception : exceptions) {
                this.exceptions.put(exception, exception);
            }
        } else {
            this.exceptions = null;
        }
    }

    public String getDomainRoot(String domain) {
        if (domain == null) {
            return null;
        }
        if (domain.startsWith(".")) {
            return null;
        }
        String domainName = null;
        String segment = domain.toLowerCase(Locale.ROOT);
        while (segment != null) {

            if (this.exceptions != null && this.exceptions.containsKey(IDN.toUnicode(segment))) {
                return segment;
            }

            if (this.rules.containsKey(IDN.toUnicode(segment))) {
                break;
            }

            int nextdot = segment.indexOf('.');
            String nextSegment = (nextdot != -1) ? segment.substring(nextdot + 1) : null;

            if (nextSegment != null &&
                    this.rules.containsKey("*." + IDN.toUnicode(nextSegment))) {
                break;
            }

            if (nextdot != -1) {
                domainName = segment;
            }
            segment = nextSegment;
        }
        return domainName;
    }

    public boolean matches(String domain) {
        if (domain == null) {
            return false;
        }
        String domainRoot = getDomainRoot(domain.startsWith(".") ? domain.substring(1) : domain);
        return (domainRoot == null);
    }
}

