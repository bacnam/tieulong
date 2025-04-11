package com.mchange.v2.c3p0.util;

import javax.sql.PooledConnection;
import javax.sql.StatementEvent;
import javax.sql.StatementEventListener;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StatementEventSupport {
    PooledConnection source;
    HashSet mlisteners = new HashSet();

    public StatementEventSupport(PooledConnection source) {
        this.source = source;
    }

    public synchronized void addStatementEventListener(StatementEventListener mlistener) {
        this.mlisteners.add(mlistener);
    }

    public synchronized void removeStatementEventListener(StatementEventListener mlistener) {
        this.mlisteners.remove(mlistener);
    }

    public synchronized void printListeners() {
        System.err.println(this.mlisteners);
    }

    public synchronized int getListenerCount() {
        return this.mlisteners.size();
    }

    public void fireStatementClosed(PreparedStatement ps) {
        Set mlCopy;
        synchronized (this) {
            mlCopy = (Set) this.mlisteners.clone();
        }
        StatementEvent evt = new StatementEvent(this.source, ps);
        for (Iterator<StatementEventListener> i = mlCopy.iterator(); i.hasNext(); ) {

            StatementEventListener cl = i.next();
            cl.statementClosed(evt);
        }
    }

    public void fireStatementErrorOccurred(PreparedStatement ps, SQLException error) {
        Set mlCopy;
        synchronized (this) {
            mlCopy = (Set) this.mlisteners.clone();
        }
        StatementEvent evt = new StatementEvent(this.source, ps, error);
        for (Iterator<StatementEventListener> i = mlCopy.iterator(); i.hasNext(); ) {

            StatementEventListener cl = i.next();
            cl.statementErrorOccurred(evt);
        }
    }
}

