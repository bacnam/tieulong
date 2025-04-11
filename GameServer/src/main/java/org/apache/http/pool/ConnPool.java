package org.apache.http.pool;

import org.apache.http.concurrent.FutureCallback;

import java.util.concurrent.Future;

public interface ConnPool<T, E> {
    Future<E> lease(T paramT, Object paramObject, FutureCallback<E> paramFutureCallback);

    void release(E paramE, boolean paramBoolean);
}

