package com.mchange.v2.coalesce;

import java.util.Iterator;

public interface Coalescer {
  Object coalesce(Object paramObject);
  
  int countCoalesced();
  
  Iterator iterator();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/coalesce/Coalescer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */