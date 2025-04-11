package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Beta
public final class HostAndPort {
    private static final int NO_PORT = -1;
    private static final Pattern BRACKET_PATTERN = Pattern.compile("^\\[(.*:.*)\\](?::(\\d*))?$");
    private final String host;
    private final int port;
    private final boolean hasBracketlessColons;

    private HostAndPort(String host, int port, boolean hasBracketlessColons) {
        this.host = host;
        this.port = port;
        this.hasBracketlessColons = hasBracketlessColons;
    }

    public static HostAndPort fromParts(String host, int port) {
        Preconditions.checkArgument(isValidPort(port));
        HostAndPort parsedHost = fromString(host);
        Preconditions.checkArgument(!parsedHost.hasPort());
        return new HostAndPort(parsedHost.host, port, parsedHost.hasBracketlessColons);
    }

    public static HostAndPort fromString(String hostPortString) {
        String host;
        Preconditions.checkNotNull(hostPortString);

        String portString = null;
        boolean hasBracketlessColons = false;

        if (hostPortString.startsWith("[")) {

            Matcher matcher = BRACKET_PATTERN.matcher(hostPortString);
            Preconditions.checkArgument(matcher.matches(), "Invalid bracketed host/port: %s", new Object[]{hostPortString});

            host = matcher.group(1);
            portString = matcher.group(2);
        } else {
            int colonPos = hostPortString.indexOf(':');
            if (colonPos >= 0 && hostPortString.indexOf(':', colonPos + 1) == -1) {

                host = hostPortString.substring(0, colonPos);
                portString = hostPortString.substring(colonPos + 1);
            } else {

                host = hostPortString;
                hasBracketlessColons = (colonPos >= 0);
            }
        }

        int port = -1;
        if (portString != null) {

            try {
                port = Integer.parseInt(portString);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unparseable port number: " + hostPortString);
            }
            Preconditions.checkArgument(isValidPort(port), "Port number out of range: %s", new Object[]{hostPortString});
        }

        return new HostAndPort(host, port, hasBracketlessColons);
    }

    private static boolean isValidPort(int port) {
        return (port >= 0 && port <= 65535);
    }

    public String getHostText() {
        return this.host;
    }

    public boolean hasPort() {
        return (this.port >= 0);
    }

    public int getPort() {
        Preconditions.checkState(hasPort());
        return this.port;
    }

    public int getPortOrDefault(int defaultPort) {
        return hasPort() ? this.port : defaultPort;
    }

    public HostAndPort withDefaultPort(int defaultPort) {
        Preconditions.checkArgument(isValidPort(defaultPort));
        if (hasPort() || this.port == defaultPort) {
            return this;
        }
        return new HostAndPort(this.host, defaultPort, this.hasBracketlessColons);
    }

    public HostAndPort requireBracketsForIPv6() {
        Preconditions.checkArgument(!this.hasBracketlessColons, "Possible bracketless IPv6 literal: %s", new Object[]{this.host});

        return this;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof HostAndPort) {
            HostAndPort that = (HostAndPort) other;
            return (Objects.equal(this.host, that.host) && this.port == that.port && this.hasBracketlessColons == that.hasBracketlessColons);
        }

        return false;
    }

    public int hashCode() {
        return Objects.hashCode(new Object[]{this.host, Integer.valueOf(this.port), Boolean.valueOf(this.hasBracketlessColons)});
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(this.host.length() + 7);
        if (this.host.indexOf(':') >= 0) {
            builder.append('[').append(this.host).append(']');
        } else {
            builder.append(this.host);
        }
        if (hasPort()) {
            builder.append(':').append(this.port);
        }
        return builder.toString();
    }
}

