package com.mchange.v2.c3p0.stmt;

import com.mchange.v2.async.AsynchronousRunner;
import java.sql.Connection;

public final class GlobalMaxOnlyStatementCache
extends GooGooStatementCache
{
int max_statements;
GooGooStatementCache.Deathmarch globalDeathmarch = new GooGooStatementCache.Deathmarch(this);

public GlobalMaxOnlyStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer, int max_statements) {
super(blockingTaskAsyncRunner, deferredStatementDestroyer);
this.max_statements = max_statements;
}

protected GooGooStatementCache.ConnectionStatementManager createConnectionStatementManager() {
return new GooGooStatementCache.SimpleConnectionStatementManager();
}

void addStatementToDeathmarches(Object pstmt, Connection physicalConnection) {
this.globalDeathmarch.deathmarchStatement(pstmt);
}
void removeStatementFromDeathmarches(Object pstmt, Connection physicalConnection) {
this.globalDeathmarch.undeathmarchStatement(pstmt);
}

boolean prepareAssimilateNewStatement(Connection pcon) {
int global_size = countCachedStatements();
return (global_size < this.max_statements || (global_size == this.max_statements && this.globalDeathmarch.cullNext()));
}
}

