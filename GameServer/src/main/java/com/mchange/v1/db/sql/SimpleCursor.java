package com.mchange.v1.db.sql;

import com.mchange.v1.util.UIterator;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SimpleCursor
        implements UIterator {
    ResultSet rs;
    int available = -1;

    public SimpleCursor(ResultSet paramResultSet) {
        this.rs = paramResultSet;
    }

    public boolean hasNext() throws SQLException {
        ratchet();
        return (this.available == 1);
    }

    public Object next() throws SQLException {
        ratchet();
        Object object = objectFromResultSet(this.rs);
        clear();
        return object;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void close() throws Exception {
        this.rs.close();
        this.rs = null;
    }

    public void finalize() throws Exception {
        if (this.rs != null) close();
    }

    protected abstract Object objectFromResultSet(ResultSet paramResultSet) throws SQLException;

    private void ratchet() throws SQLException {
        if (this.available == -1)
            this.available = this.rs.next() ? 1 : 0;
    }

    private void clear() {
        this.available = -1;
    }
}

