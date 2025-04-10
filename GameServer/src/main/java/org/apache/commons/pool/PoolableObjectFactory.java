package org.apache.commons.pool;

public interface PoolableObjectFactory {
  Object makeObject() throws Exception;

  void destroyObject(Object paramObject) throws Exception;

  boolean validateObject(Object paramObject);

  void activateObject(Object paramObject) throws Exception;

  void passivateObject(Object paramObject) throws Exception;
}

