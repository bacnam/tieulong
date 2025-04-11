package org.apache.http.nio.client.util;

import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;

import java.io.IOException;

public class HttpAsyncClientUtils {
    public static void closeQuietly(CloseableHttpAsyncClient httpAsyncClient) {
        if (httpAsyncClient != null)
            try {
                httpAsyncClient.close();
            } catch (IOException ignore) {
            }
    }
}

