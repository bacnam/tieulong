package com.mchange.v1.identicator;

public interface Identicator {
  boolean identical(Object paramObject1, Object paramObject2);
  
  int hash(Object paramObject);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/Identicator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */