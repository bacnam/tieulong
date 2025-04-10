package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;

public final class ListenableFutureTask<V>
extends FutureTask<V>
implements ListenableFuture<V>
{
private final ExecutionList executionList = new ExecutionList();

public static <V> ListenableFutureTask<V> create(Callable<V> callable) {
return new ListenableFutureTask<V>(callable);
}

public static <V> ListenableFutureTask<V> create(Runnable runnable, @Nullable V result) {
return new ListenableFutureTask<V>(runnable, result);
}

@Deprecated
@Beta
public ListenableFutureTask(Callable<V> callable) {
super(callable);
}

@Deprecated
@Beta
public ListenableFutureTask(Runnable runnable, @Nullable V result) {
super(runnable, result);
}

public void addListener(Runnable listener, Executor exec) {
this.executionList.add(listener, exec);
}

protected void done() {
this.executionList.execute();
}
}

