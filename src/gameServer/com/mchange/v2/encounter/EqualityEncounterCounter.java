package com.mchange.v2.encounter;

import java.util.WeakHashMap;

public class EqualityEncounterCounter
extends AbstractEncounterCounter
{
public EqualityEncounterCounter() {
super(new WeakHashMap<Object, Object>());
}
}

