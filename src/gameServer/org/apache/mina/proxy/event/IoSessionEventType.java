package org.apache.mina.proxy.event;

public enum IoSessionEventType
{
CREATED(1), OPENED(2), IDLE(3), CLOSED(4);

private final int id;

IoSessionEventType(int id) {
this.id = id;
}

public int getId() {
return this.id;
}

public String toString() {
switch (this) {
case CREATED:
return "- CREATED event -";
case OPENED:
return "- OPENED event -";
case IDLE:
return "- IDLE event -";
case CLOSED:
return "- CLOSED event -";
} 
return "- Event Id=" + this.id + " -";
}
}

