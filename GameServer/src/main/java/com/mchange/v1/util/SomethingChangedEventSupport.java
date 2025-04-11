package com.mchange.v1.util;

import java.util.Enumeration;
import java.util.Vector;

public class SomethingChangedEventSupport {
    Object source;
    Vector listeners = new Vector();

    public SomethingChangedEventSupport(Object paramObject) {
        this.source = paramObject;
    }

    public synchronized void addSomethingChangedListener(SomethingChangedListener paramSomethingChangedListener) {
        if (!this.listeners.contains(paramSomethingChangedListener))
            this.listeners.addElement(paramSomethingChangedListener);
    }

    public synchronized void removeSomethingChangedListener(SomethingChangedListener paramSomethingChangedListener) {
        this.listeners.removeElement(paramSomethingChangedListener);
    }

    public synchronized void fireSomethingChanged() {
        SomethingChangedEvent somethingChangedEvent = new SomethingChangedEvent(this.source);
        for (Enumeration<SomethingChangedListener> enumeration = this.listeners.elements(); enumeration.hasMoreElements(); ) {

            SomethingChangedListener somethingChangedListener = enumeration.nextElement();
            somethingChangedListener.somethingChanged(somethingChangedEvent);
        }
    }
}

