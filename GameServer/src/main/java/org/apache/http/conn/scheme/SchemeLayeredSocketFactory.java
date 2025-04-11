package org.apache.http.conn.scheme;

import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

@Deprecated
public interface SchemeLayeredSocketFactory extends SchemeSocketFactory {
    Socket createLayeredSocket(Socket paramSocket, String paramString, int paramInt, HttpParams paramHttpParams) throws IOException, UnknownHostException;
}

