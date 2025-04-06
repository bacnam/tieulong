package com.google.common.eventbus;

import com.google.common.collect.Multimap;

interface HandlerFindingStrategy {
  Multimap<Class<?>, EventHandler> findAllHandlers(Object paramObject);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/eventbus/HandlerFindingStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */