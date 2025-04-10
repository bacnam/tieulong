package com.mchange.v2.c3p0.util;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;

public class ConnectionEventSupport
{
PooledConnection source;
HashSet mlisteners = new HashSet();

public ConnectionEventSupport(PooledConnection source) {
this.source = source;
}
public synchronized void addConnectionEventListener(ConnectionEventListener mlistener) {
this.mlisteners.add(mlistener);
}
public synchronized void removeConnectionEventListener(ConnectionEventListener mlistener) {
this.mlisteners.remove(mlistener);
}
public synchronized void printListeners() {
System.err.println(this.mlisteners);
}
public synchronized int getListenerCount() {
return this.mlisteners.size();
}

public void fireConnectionClosed() {
Set mlCopy;
synchronized (this) {
mlCopy = (Set)this.mlisteners.clone();
} 
ConnectionEvent evt = new ConnectionEvent(this.source);
for (Iterator<ConnectionEventListener> i = mlCopy.iterator(); i.hasNext(); ) {

ConnectionEventListener cl = i.next();
cl.connectionClosed(evt);
} 
}

public void fireConnectionErrorOccurred(SQLException error) {
Set mlCopy;
synchronized (this) {
mlCopy = (Set)this.mlisteners.clone();
} 
ConnectionEvent evt = new ConnectionEvent(this.source, error);
for (Iterator<ConnectionEventListener> i = mlCopy.iterator(); i.hasNext(); ) {

ConnectionEventListener cl = i.next();
cl.connectionErrorOccurred(evt);
} 
}
}

