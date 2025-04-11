package org.apache.thrift.async;

public interface AsyncMethodCallback<T> {

    public void onComplete(T response);

    public void onError(Throwable throwable);
}
