package com.mchange.v2.encounter;

import java.util.HashMap;

public class StrongEqualityEncounterCounter
extends AbstractEncounterCounter
{
public StrongEqualityEncounterCounter() {
super(new HashMap<Object, Object>());
}
}

