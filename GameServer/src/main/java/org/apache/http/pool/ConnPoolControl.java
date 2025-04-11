package org.apache.http.pool;

public interface ConnPoolControl<T> {
    int getMaxTotal();

    void setMaxTotal(int paramInt);

    int getDefaultMaxPerRoute();

    void setDefaultMaxPerRoute(int paramInt);

    void setMaxPerRoute(T paramT, int paramInt);

    int getMaxPerRoute(T paramT);

    PoolStats getTotalStats();

    PoolStats getStats(T paramT);
}

