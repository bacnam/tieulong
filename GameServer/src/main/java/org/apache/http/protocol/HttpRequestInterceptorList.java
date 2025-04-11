package org.apache.http.protocol;

import org.apache.http.HttpRequestInterceptor;

import java.util.List;

@Deprecated
public interface HttpRequestInterceptorList {
    void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor);

    void addRequestInterceptor(HttpRequestInterceptor paramHttpRequestInterceptor, int paramInt);

    int getRequestInterceptorCount();

    HttpRequestInterceptor getRequestInterceptor(int paramInt);

    void clearRequestInterceptors();

    void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> paramClass);

    void setInterceptors(List<?> paramList);
}

