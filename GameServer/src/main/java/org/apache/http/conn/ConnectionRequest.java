package org.apache.http.conn;

import org.apache.http.HttpClientConnection;
import org.apache.http.concurrent.Cancellable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public interface ConnectionRequest extends Cancellable {
    HttpClientConnection get(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException;
}

