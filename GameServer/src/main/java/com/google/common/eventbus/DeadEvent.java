package com.google.common.eventbus;

import com.google.common.annotations.Beta;

@Beta
public class DeadEvent
{
private final Object source;
private final Object event;

public DeadEvent(Object source, Object event) {
this.source = source;
this.event = event;
}

public Object getSource() {
return this.source;
}

public Object getEvent() {
return this.event;
}
}

