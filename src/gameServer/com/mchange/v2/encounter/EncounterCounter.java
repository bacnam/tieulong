package com.mchange.v2.encounter;

public interface EncounterCounter {
  long encounter(Object paramObject);
  
  long reset(Object paramObject);
  
  void resetAll();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/encounter/EncounterCounter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */