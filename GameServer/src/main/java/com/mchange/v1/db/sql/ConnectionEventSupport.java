package com.mchange.v1.db.sql;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.PooledConnection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class ConnectionEventSupport {
    PooledConnection source;
    Set mlisteners = new HashSet();

    public ConnectionEventSupport(PooledConnection paramPooledConnection) {
        this.source = paramPooledConnection;
    }

    public synchronized void addConnectionEventListener(ConnectionEventListener paramConnectionEventListener) {
        this.mlisteners.add(paramConnectionEventListener);
    }

    public synchronized void removeConnectionEventListener(ConnectionEventListener paramConnectionEventListener) {
        this.mlisteners.remove(paramConnectionEventListener);
    }

    public synchronized void fireConnectionClosed() {
        ConnectionEvent connectionEvent = new ConnectionEvent(this.source);
        for (ConnectionEventListener connectionEventListener : this.mlisteners) {

            connectionEventListener.connectionClosed(connectionEvent);
        }
    }

    public synchronized void fireConnectionErrorOccurred(SQLException paramSQLException) {
        ConnectionEvent connectionEvent = new ConnectionEvent(this.source, paramSQLException);
        for (ConnectionEventListener connectionEventListener : this.mlisteners) {

            connectionEventListener.connectionErrorOccurred(connectionEvent);
        }
    }
}

