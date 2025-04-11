package com.mchange.v2.c3p0.impl;

import com.mchange.v1.util.ClosableResource;
import com.mchange.v2.c3p0.stmt.GooGooStatementCache;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;

abstract class AbstractC3P0PooledConnection
        implements PooledConnection, ClosableResource {
    final Object inInternalUseLock = new Object();

    abstract Connection getPhysicalConnection();

    abstract void initStatementCache(GooGooStatementCache paramGooGooStatementCache);

    abstract void closeMaybeCheckedOut(boolean paramBoolean) throws SQLException;
}

