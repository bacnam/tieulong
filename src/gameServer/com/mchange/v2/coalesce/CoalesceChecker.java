package com.mchange.v2.coalesce;

public interface CoalesceChecker {
  boolean checkCoalesce(Object paramObject1, Object paramObject2);
  
  int coalesceHash(Object paramObject);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/CoalesceChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */