package org.apache.commons.pool.impl;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.ObjectPoolFactory;
import org.apache.commons.pool.PoolableObjectFactory;

public class StackObjectPoolFactory
implements ObjectPoolFactory
{
protected PoolableObjectFactory _factory;
protected int _maxSleeping;
protected int _initCapacity;

public StackObjectPoolFactory() {
this((PoolableObjectFactory)null, 8, 4);
}

public StackObjectPoolFactory(int maxIdle) {
this((PoolableObjectFactory)null, maxIdle, 4);
}

public StackObjectPoolFactory(int maxIdle, int initIdleCapacity) {
this((PoolableObjectFactory)null, maxIdle, initIdleCapacity);
}

public StackObjectPoolFactory(PoolableObjectFactory factory) {
this(factory, 8, 4);
}

public StackObjectPoolFactory(PoolableObjectFactory factory, int maxIdle) {
this(factory, maxIdle, 4);
}

public StackObjectPoolFactory(PoolableObjectFactory factory, int maxIdle, int initIdleCapacity) {
this._factory = null;
this._maxSleeping = 8;
this._initCapacity = 4;
this._factory = factory;
this._maxSleeping = maxIdle;
this._initCapacity = initIdleCapacity;
}

public ObjectPool createPool() {
return new StackObjectPool(this._factory, this._maxSleeping, this._initCapacity);
}
}

