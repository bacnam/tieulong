package org.apache.commons.pool;

public interface KeyedPoolableObjectFactory {
  Object makeObject(Object paramObject) throws Exception;

  void destroyObject(Object paramObject1, Object paramObject2) throws Exception;

  boolean validateObject(Object paramObject1, Object paramObject2);

  void activateObject(Object paramObject1, Object paramObject2) throws Exception;

  void passivateObject(Object paramObject1, Object paramObject2) throws Exception;
}

