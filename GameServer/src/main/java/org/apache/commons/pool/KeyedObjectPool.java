package org.apache.commons.pool;

import java.util.NoSuchElementException;

public interface KeyedObjectPool {
    Object borrowObject(Object paramObject) throws Exception, NoSuchElementException, IllegalStateException;

    void returnObject(Object paramObject1, Object paramObject2) throws Exception;

    void invalidateObject(Object paramObject1, Object paramObject2) throws Exception;

    void addObject(Object paramObject) throws Exception, IllegalStateException, UnsupportedOperationException;

    int getNumIdle(Object paramObject) throws UnsupportedOperationException;

    int getNumActive(Object paramObject) throws UnsupportedOperationException;

    int getNumIdle() throws UnsupportedOperationException;

    int getNumActive() throws UnsupportedOperationException;

    void clear() throws Exception, UnsupportedOperationException;

    void clear(Object paramObject) throws Exception, UnsupportedOperationException;

    void close() throws Exception;

    void setFactory(KeyedPoolableObjectFactory paramKeyedPoolableObjectFactory) throws IllegalStateException, UnsupportedOperationException;
}

