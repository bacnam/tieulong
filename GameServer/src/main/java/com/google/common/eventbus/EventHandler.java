package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class EventHandler
{
private final Object target;
private final Method method;

EventHandler(Object target, Method method) {
Preconditions.checkNotNull(target, "EventHandler target cannot be null.");

Preconditions.checkNotNull(method, "EventHandler method cannot be null.");

this.target = target;
this.method = method;
method.setAccessible(true);
}

public void handleEvent(Object event) throws InvocationTargetException {
try {
this.method.invoke(this.target, new Object[] { event });
} catch (IllegalArgumentException e) {
throw new Error("Method rejected target/argument: " + event, e);
} catch (IllegalAccessException e) {
throw new Error("Method became inaccessible: " + event, e);
} catch (InvocationTargetException e) {
if (e.getCause() instanceof Error) {
throw (Error)e.getCause();
}
throw e;
} 
}

public String toString() {
return "[wrapper " + this.method + "]";
}

public int hashCode() {
int PRIME = 31;
return (31 + this.method.hashCode()) * 31 + this.target.hashCode();
}

public boolean equals(Object obj) {
if (this == obj) {
return true;
}

if (obj == null) {
return false;
}

if (getClass() != obj.getClass()) {
return false;
}

EventHandler other = (EventHandler)obj;

return (this.method.equals(other.method) && this.target == other.target);
}
}

