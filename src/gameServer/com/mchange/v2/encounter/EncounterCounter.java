package com.mchange.v2.encounter;

public interface EncounterCounter {
  long encounter(Object paramObject);

  long reset(Object paramObject);

  void resetAll();
}

