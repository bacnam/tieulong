package com.mchange.v2.c3p0.stmt;

import com.mchange.v2.async.AsynchronousRunner;
import java.sql.Connection;

public final class PerConnectionMaxOnlyStatementCache
extends GooGooStatementCache
{
int max_statements_per_connection;
GooGooStatementCache.DeathmarchConnectionStatementManager dcsm;

public PerConnectionMaxOnlyStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer, int max_statements_per_connection) {
super(blockingTaskAsyncRunner, deferredStatementDestroyer);
this.max_statements_per_connection = max_statements_per_connection;
}

protected GooGooStatementCache.ConnectionStatementManager createConnectionStatementManager() {
return this.dcsm = new GooGooStatementCache.DeathmarchConnectionStatementManager(this);
}

void addStatementToDeathmarches(Object pstmt, Connection physicalConnection) {
this.dcsm.getDeathmarch(physicalConnection).deathmarchStatement(pstmt);
}
void removeStatementFromDeathmarches(Object pstmt, Connection physicalConnection) {
this.dcsm.getDeathmarch(physicalConnection).undeathmarchStatement(pstmt);
}

boolean prepareAssimilateNewStatement(Connection pcon) {
int cxn_stmt_count = this.dcsm.getNumStatementsForConnection(pcon);
return (cxn_stmt_count < this.max_statements_per_connection || (cxn_stmt_count == this.max_statements_per_connection && this.dcsm.getDeathmarch(pcon).cullNext()));
}
}

