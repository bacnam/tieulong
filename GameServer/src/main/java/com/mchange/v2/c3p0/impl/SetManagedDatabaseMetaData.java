package com.mchange.v2.c3p0.impl;

import com.mchange.v2.sql.filter.FilterDatabaseMetaData;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

final class SetManagedDatabaseMetaData
extends FilterDatabaseMetaData
{
Set activeResultSets;
Connection returnableProxy;

SetManagedDatabaseMetaData(DatabaseMetaData inner, Set activeResultSets, Connection returnableProxy) {
super(inner);
this.activeResultSets = activeResultSets;
this.returnableProxy = returnableProxy;
}

public Connection getConnection() throws SQLException {
return this.returnableProxy;
}

public ResultSet getProcedures(String a, String b, String c) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getProcedures(a, b, c), this.activeResultSets);
}

public ResultSet getProcedureColumns(String a, String b, String c, String d) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getProcedureColumns(a, b, c, d), this.activeResultSets);
}

public ResultSet getTables(String a, String b, String c, String[] d) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getTables(a, b, c, d), this.activeResultSets);
}

public ResultSet getSchemas() throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getSchemas(), this.activeResultSets);
}

public ResultSet getCatalogs() throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getCatalogs(), this.activeResultSets);
}

public ResultSet getTableTypes() throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getTableTypes(), this.activeResultSets);
}

public ResultSet getColumns(String a, String b, String c, String d) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getColumns(a, b, c, d), this.activeResultSets);
}

public ResultSet getColumnPrivileges(String a, String b, String c, String d) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getColumnPrivileges(a, b, c, d), this.activeResultSets);
}

public ResultSet getTablePrivileges(String a, String b, String c) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getTablePrivileges(a, b, c), this.activeResultSets);
}

public ResultSet getBestRowIdentifier(String a, String b, String c, int d, boolean e) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getBestRowIdentifier(a, b, c, d, e), this.activeResultSets);
}

public ResultSet getVersionColumns(String a, String b, String c) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getVersionColumns(a, b, c), this.activeResultSets);
}

public ResultSet getPrimaryKeys(String a, String b, String c) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getPrimaryKeys(a, b, c), this.activeResultSets);
}

public ResultSet getImportedKeys(String a, String b, String c) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getImportedKeys(a, b, c), this.activeResultSets);
}

public ResultSet getExportedKeys(String a, String b, String c) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getExportedKeys(a, b, c), this.activeResultSets);
}

public ResultSet getCrossReference(String a, String b, String c, String d, String e, String f) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getCrossReference(a, b, c, d, e, f), this.activeResultSets);
}

public ResultSet getTypeInfo() throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getTypeInfo(), this.activeResultSets);
}

public ResultSet getIndexInfo(String a, String b, String c, boolean d, boolean e) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getIndexInfo(a, b, c, d, e), this.activeResultSets);
}

public ResultSet getUDTs(String a, String b, String c, int[] d) throws SQLException {
return (ResultSet)new NullStatementSetManagedResultSet(this.inner.getUDTs(a, b, c, d), this.activeResultSets);
}
}

