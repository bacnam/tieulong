package com.notnoop.apns.internal;

import javax.net.ssl.SSLSocketFactory;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;

public final class TlsTunnelBuilder {
    public static String readAsciiUntilCrlf(InputStream in) throws IOException {
        StringBuilder result = new StringBuilder(80);
        int c;
        while ((c = in.read()) != -1) {
            if (c == 10) {
                if (result.length() > 0 && result.charAt(result.length() - 1) == '\r') {
                    result.deleteCharAt(result.length() - 1);
                }
                return result.toString();
            }
            result.append((char) c);
        }
        throw new EOFException("Expected CRLF");
    }

    public Socket build(SSLSocketFactory factory, Proxy proxy, String host, int port) throws IOException {
        boolean success = false;
        Socket proxySocket = null;
        try {
            InetSocketAddress proxyAddress = (InetSocketAddress) proxy.address();
            proxySocket = new Socket(proxyAddress.getAddress(), proxyAddress.getPort());
            makeTunnel(host, port, proxySocket.getOutputStream(), proxySocket.getInputStream());

            Socket socket = factory.createSocket(proxySocket, host, port, true);
            success = true;
            return socket;
        } finally {
            if (!success) {
                Utilities.close(proxySocket);
            }
        }
    }

    void makeTunnel(String host, int port, OutputStream out, InputStream in) throws IOException {
        String userAgent = "java-apns";
        String connect = String.format("CONNECT %1$s:%2$d HTTP/1.1\r\nHost: %1$s:%2$d\r\nUser-Agent: %3$s\r\nProxy-Connection: Keep-Alive\r\n\r\n", new Object[]{host, Integer.valueOf(port), userAgent});

        out.write(connect.getBytes("UTF-8"));

        String statusLine = readAsciiUntilCrlf(in);
        if (!statusLine.matches("HTTP\\/1\\.\\d 2\\d\\d .*")) {
            throw new ProtocolException("TLS tunnel failed: " + statusLine);
        }
        while (readAsciiUntilCrlf(in).length() != 0) ;
    }
}

