package com.mchange.v2.encounter;

import com.mchange.v2.util.WeakIdentityHashMapFactory;

public class IdentityEncounterCounter
extends AbstractEncounterCounter
{
public IdentityEncounterCounter() {
super(WeakIdentityHashMapFactory.create());
}
}

