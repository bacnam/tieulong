package org.apache.http.conn.socket;

import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public interface LayeredConnectionSocketFactory extends ConnectionSocketFactory {
    Socket createLayeredSocket(Socket paramSocket, String paramString, int paramInt, HttpContext paramHttpContext) throws IOException, UnknownHostException;
}

