package org.apache.commons.pool;

public abstract class BaseObjectPool
implements ObjectPool
{
public abstract Object borrowObject() throws Exception;

public abstract void returnObject(Object paramObject) throws Exception;

public abstract void invalidateObject(Object paramObject) throws Exception;

public int getNumIdle() throws UnsupportedOperationException {
return -1;
}

public int getNumActive() throws UnsupportedOperationException {
return -1;
}

public void clear() throws Exception, UnsupportedOperationException {
throw new UnsupportedOperationException();
}

public void addObject() throws Exception, UnsupportedOperationException {
throw new UnsupportedOperationException();
}

public void close() throws Exception {
this.closed = true;
}

public void setFactory(PoolableObjectFactory factory) throws IllegalStateException, UnsupportedOperationException {
throw new UnsupportedOperationException();
}

protected final boolean isClosed() {
return this.closed;
}

protected final void assertOpen() throws IllegalStateException {
if (isClosed())
throw new IllegalStateException("Pool not open"); 
}

private volatile boolean closed = false;
}

