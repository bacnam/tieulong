package org.apache.http.nio.conn;

import org.apache.http.nio.reactor.IOSession;
import org.apache.http.params.HttpParams;

@Deprecated
public interface ClientAsyncConnectionFactory {
    ClientAsyncConnection create(String paramString, IOSession paramIOSession, HttpParams paramHttpParams);
}

