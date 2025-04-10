package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Deprecated
@Beta
public interface UninterruptibleFuture<V> extends Future<V> {
  V get() throws ExecutionException;

  V get(long paramLong, TimeUnit paramTimeUnit) throws ExecutionException, TimeoutException;
}

