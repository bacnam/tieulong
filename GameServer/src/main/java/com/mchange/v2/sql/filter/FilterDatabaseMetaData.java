package com.mchange.v2.sql.filter;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;
import java.sql.SQLException;

public abstract class FilterDatabaseMetaData
implements DatabaseMetaData
{
protected DatabaseMetaData inner;

private void __setInner(DatabaseMetaData paramDatabaseMetaData) {
this.inner = paramDatabaseMetaData;
}

public FilterDatabaseMetaData(DatabaseMetaData paramDatabaseMetaData) {
__setInner(paramDatabaseMetaData);
}

public FilterDatabaseMetaData() {}

public void setInner(DatabaseMetaData paramDatabaseMetaData) {
__setInner(paramDatabaseMetaData);
}
public DatabaseMetaData getInner() {
return this.inner;
}

public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
return this.inner.autoCommitFailureClosesAllResultSets();
}

public ResultSet getCatalogs() throws SQLException {
return this.inner.getCatalogs();
}

public boolean allProceduresAreCallable() throws SQLException {
return this.inner.allProceduresAreCallable();
}

public boolean allTablesAreSelectable() throws SQLException {
return this.inner.allTablesAreSelectable();
}

public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
return this.inner.dataDefinitionCausesTransactionCommit();
}

public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
return this.inner.dataDefinitionIgnoredInTransactions();
}

public boolean deletesAreDetected(int paramInt) throws SQLException {
return this.inner.deletesAreDetected(paramInt);
}

public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
return this.inner.doesMaxRowSizeIncludeBlobs();
}

public boolean generatedKeyAlwaysReturned() throws SQLException {
return this.inner.generatedKeyAlwaysReturned();
}

public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) throws SQLException {
return this.inner.getBestRowIdentifier(paramString1, paramString2, paramString3, paramInt, paramBoolean);
}

public String getCatalogSeparator() throws SQLException {
return this.inner.getCatalogSeparator();
}

public String getCatalogTerm() throws SQLException {
return this.inner.getCatalogTerm();
}

public ResultSet getClientInfoProperties() throws SQLException {
return this.inner.getClientInfoProperties();
}

public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
return this.inner.getColumnPrivileges(paramString1, paramString2, paramString3, paramString4);
}

public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
return this.inner.getColumns(paramString1, paramString2, paramString3, paramString4);
}

public Connection getConnection() throws SQLException {
return this.inner.getConnection();
}

public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws SQLException {
return this.inner.getCrossReference(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
}

public int getDatabaseMajorVersion() throws SQLException {
return this.inner.getDatabaseMajorVersion();
}

public int getDatabaseMinorVersion() throws SQLException {
return this.inner.getDatabaseMinorVersion();
}

public String getDatabaseProductName() throws SQLException {
return this.inner.getDatabaseProductName();
}

public String getDatabaseProductVersion() throws SQLException {
return this.inner.getDatabaseProductVersion();
}

public int getDefaultTransactionIsolation() throws SQLException {
return this.inner.getDefaultTransactionIsolation();
}

public int getDriverMajorVersion() {
return this.inner.getDriverMajorVersion();
}

public int getDriverMinorVersion() {
return this.inner.getDriverMinorVersion();
}

public String getDriverName() throws SQLException {
return this.inner.getDriverName();
}

public String getDriverVersion() throws SQLException {
return this.inner.getDriverVersion();
}

public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getExportedKeys(paramString1, paramString2, paramString3);
}

public String getExtraNameCharacters() throws SQLException {
return this.inner.getExtraNameCharacters();
}

public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
return this.inner.getFunctionColumns(paramString1, paramString2, paramString3, paramString4);
}

public ResultSet getFunctions(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getFunctions(paramString1, paramString2, paramString3);
}

public String getIdentifierQuoteString() throws SQLException {
return this.inner.getIdentifierQuoteString();
}

public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getImportedKeys(paramString1, paramString2, paramString3);
}

public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
return this.inner.getIndexInfo(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
}

public int getJDBCMajorVersion() throws SQLException {
return this.inner.getJDBCMajorVersion();
}

public int getJDBCMinorVersion() throws SQLException {
return this.inner.getJDBCMinorVersion();
}

public int getMaxBinaryLiteralLength() throws SQLException {
return this.inner.getMaxBinaryLiteralLength();
}

public int getMaxCatalogNameLength() throws SQLException {
return this.inner.getMaxCatalogNameLength();
}

public int getMaxCharLiteralLength() throws SQLException {
return this.inner.getMaxCharLiteralLength();
}

public int getMaxColumnNameLength() throws SQLException {
return this.inner.getMaxColumnNameLength();
}

public int getMaxColumnsInGroupBy() throws SQLException {
return this.inner.getMaxColumnsInGroupBy();
}

public int getMaxColumnsInIndex() throws SQLException {
return this.inner.getMaxColumnsInIndex();
}

public int getMaxColumnsInOrderBy() throws SQLException {
return this.inner.getMaxColumnsInOrderBy();
}

public int getMaxColumnsInSelect() throws SQLException {
return this.inner.getMaxColumnsInSelect();
}

public int getMaxColumnsInTable() throws SQLException {
return this.inner.getMaxColumnsInTable();
}

public int getMaxConnections() throws SQLException {
return this.inner.getMaxConnections();
}

public int getMaxCursorNameLength() throws SQLException {
return this.inner.getMaxCursorNameLength();
}

public int getMaxIndexLength() throws SQLException {
return this.inner.getMaxIndexLength();
}

public int getMaxProcedureNameLength() throws SQLException {
return this.inner.getMaxProcedureNameLength();
}

public int getMaxRowSize() throws SQLException {
return this.inner.getMaxRowSize();
}

public int getMaxSchemaNameLength() throws SQLException {
return this.inner.getMaxSchemaNameLength();
}

public int getMaxStatementLength() throws SQLException {
return this.inner.getMaxStatementLength();
}

public int getMaxStatements() throws SQLException {
return this.inner.getMaxStatements();
}

public int getMaxTableNameLength() throws SQLException {
return this.inner.getMaxTableNameLength();
}

public int getMaxTablesInSelect() throws SQLException {
return this.inner.getMaxTablesInSelect();
}

public int getMaxUserNameLength() throws SQLException {
return this.inner.getMaxUserNameLength();
}

public String getNumericFunctions() throws SQLException {
return this.inner.getNumericFunctions();
}

public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getPrimaryKeys(paramString1, paramString2, paramString3);
}

public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
return this.inner.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
}

public String getProcedureTerm() throws SQLException {
return this.inner.getProcedureTerm();
}

public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getProcedures(paramString1, paramString2, paramString3);
}

public ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
return this.inner.getPseudoColumns(paramString1, paramString2, paramString3, paramString4);
}

public int getResultSetHoldability() throws SQLException {
return this.inner.getResultSetHoldability();
}

public RowIdLifetime getRowIdLifetime() throws SQLException {
return this.inner.getRowIdLifetime();
}

public String getSQLKeywords() throws SQLException {
return this.inner.getSQLKeywords();
}

public int getSQLStateType() throws SQLException {
return this.inner.getSQLStateType();
}

public String getSchemaTerm() throws SQLException {
return this.inner.getSchemaTerm();
}

public ResultSet getSchemas(String paramString1, String paramString2) throws SQLException {
return this.inner.getSchemas(paramString1, paramString2);
}

public ResultSet getSchemas() throws SQLException {
return this.inner.getSchemas();
}

public String getSearchStringEscape() throws SQLException {
return this.inner.getSearchStringEscape();
}

public String getStringFunctions() throws SQLException {
return this.inner.getStringFunctions();
}

public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getSuperTables(paramString1, paramString2, paramString3);
}

public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getSuperTypes(paramString1, paramString2, paramString3);
}

public String getSystemFunctions() throws SQLException {
return this.inner.getSystemFunctions();
}

public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getTablePrivileges(paramString1, paramString2, paramString3);
}

public ResultSet getTableTypes() throws SQLException {
return this.inner.getTableTypes();
}

public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) throws SQLException {
return this.inner.getTables(paramString1, paramString2, paramString3, paramArrayOfString);
}

public String getTimeDateFunctions() throws SQLException {
return this.inner.getTimeDateFunctions();
}

public ResultSet getTypeInfo() throws SQLException {
return this.inner.getTypeInfo();
}

public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) throws SQLException {
return this.inner.getUDTs(paramString1, paramString2, paramString3, paramArrayOfint);
}

public String getUserName() throws SQLException {
return this.inner.getUserName();
}

public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) throws SQLException {
return this.inner.getVersionColumns(paramString1, paramString2, paramString3);
}

public boolean insertsAreDetected(int paramInt) throws SQLException {
return this.inner.insertsAreDetected(paramInt);
}

public boolean isCatalogAtStart() throws SQLException {
return this.inner.isCatalogAtStart();
}

public boolean locatorsUpdateCopy() throws SQLException {
return this.inner.locatorsUpdateCopy();
}

public boolean nullPlusNonNullIsNull() throws SQLException {
return this.inner.nullPlusNonNullIsNull();
}

public boolean nullsAreSortedAtEnd() throws SQLException {
return this.inner.nullsAreSortedAtEnd();
}

public boolean nullsAreSortedAtStart() throws SQLException {
return this.inner.nullsAreSortedAtStart();
}

public boolean nullsAreSortedHigh() throws SQLException {
return this.inner.nullsAreSortedHigh();
}

public boolean nullsAreSortedLow() throws SQLException {
return this.inner.nullsAreSortedLow();
}

public boolean othersDeletesAreVisible(int paramInt) throws SQLException {
return this.inner.othersDeletesAreVisible(paramInt);
}

public boolean othersInsertsAreVisible(int paramInt) throws SQLException {
return this.inner.othersInsertsAreVisible(paramInt);
}

public boolean othersUpdatesAreVisible(int paramInt) throws SQLException {
return this.inner.othersUpdatesAreVisible(paramInt);
}

public boolean ownDeletesAreVisible(int paramInt) throws SQLException {
return this.inner.ownDeletesAreVisible(paramInt);
}

public boolean ownInsertsAreVisible(int paramInt) throws SQLException {
return this.inner.ownInsertsAreVisible(paramInt);
}

public boolean ownUpdatesAreVisible(int paramInt) throws SQLException {
return this.inner.ownUpdatesAreVisible(paramInt);
}

public boolean storesLowerCaseIdentifiers() throws SQLException {
return this.inner.storesLowerCaseIdentifiers();
}

public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
return this.inner.storesLowerCaseQuotedIdentifiers();
}

public boolean storesMixedCaseIdentifiers() throws SQLException {
return this.inner.storesMixedCaseIdentifiers();
}

public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
return this.inner.storesMixedCaseQuotedIdentifiers();
}

public boolean storesUpperCaseIdentifiers() throws SQLException {
return this.inner.storesUpperCaseIdentifiers();
}

public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
return this.inner.storesUpperCaseQuotedIdentifiers();
}

public boolean supportsANSI92EntryLevelSQL() throws SQLException {
return this.inner.supportsANSI92EntryLevelSQL();
}

public boolean supportsANSI92FullSQL() throws SQLException {
return this.inner.supportsANSI92FullSQL();
}

public boolean supportsANSI92IntermediateSQL() throws SQLException {
return this.inner.supportsANSI92IntermediateSQL();
}

public boolean supportsAlterTableWithAddColumn() throws SQLException {
return this.inner.supportsAlterTableWithAddColumn();
}

public boolean supportsAlterTableWithDropColumn() throws SQLException {
return this.inner.supportsAlterTableWithDropColumn();
}

public boolean supportsBatchUpdates() throws SQLException {
return this.inner.supportsBatchUpdates();
}

public boolean supportsCatalogsInDataManipulation() throws SQLException {
return this.inner.supportsCatalogsInDataManipulation();
}

public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
return this.inner.supportsCatalogsInIndexDefinitions();
}

public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
return this.inner.supportsCatalogsInPrivilegeDefinitions();
}

public boolean supportsCatalogsInProcedureCalls() throws SQLException {
return this.inner.supportsCatalogsInProcedureCalls();
}

public boolean supportsCatalogsInTableDefinitions() throws SQLException {
return this.inner.supportsCatalogsInTableDefinitions();
}

public boolean supportsColumnAliasing() throws SQLException {
return this.inner.supportsColumnAliasing();
}

public boolean supportsConvert(int paramInt1, int paramInt2) throws SQLException {
return this.inner.supportsConvert(paramInt1, paramInt2);
}

public boolean supportsConvert() throws SQLException {
return this.inner.supportsConvert();
}

public boolean supportsCoreSQLGrammar() throws SQLException {
return this.inner.supportsCoreSQLGrammar();
}

public boolean supportsCorrelatedSubqueries() throws SQLException {
return this.inner.supportsCorrelatedSubqueries();
}

public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
return this.inner.supportsDataDefinitionAndDataManipulationTransactions();
}

public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
return this.inner.supportsDataManipulationTransactionsOnly();
}

public boolean supportsDifferentTableCorrelationNames() throws SQLException {
return this.inner.supportsDifferentTableCorrelationNames();
}

public boolean supportsExpressionsInOrderBy() throws SQLException {
return this.inner.supportsExpressionsInOrderBy();
}

public boolean supportsExtendedSQLGrammar() throws SQLException {
return this.inner.supportsExtendedSQLGrammar();
}

public boolean supportsFullOuterJoins() throws SQLException {
return this.inner.supportsFullOuterJoins();
}

public boolean supportsGetGeneratedKeys() throws SQLException {
return this.inner.supportsGetGeneratedKeys();
}

public boolean supportsGroupBy() throws SQLException {
return this.inner.supportsGroupBy();
}

public boolean supportsGroupByBeyondSelect() throws SQLException {
return this.inner.supportsGroupByBeyondSelect();
}

public boolean supportsGroupByUnrelated() throws SQLException {
return this.inner.supportsGroupByUnrelated();
}

public boolean supportsIntegrityEnhancementFacility() throws SQLException {
return this.inner.supportsIntegrityEnhancementFacility();
}

public boolean supportsLikeEscapeClause() throws SQLException {
return this.inner.supportsLikeEscapeClause();
}

public boolean supportsLimitedOuterJoins() throws SQLException {
return this.inner.supportsLimitedOuterJoins();
}

public boolean supportsMinimumSQLGrammar() throws SQLException {
return this.inner.supportsMinimumSQLGrammar();
}

public boolean supportsMixedCaseIdentifiers() throws SQLException {
return this.inner.supportsMixedCaseIdentifiers();
}

public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
return this.inner.supportsMixedCaseQuotedIdentifiers();
}

public boolean supportsMultipleOpenResults() throws SQLException {
return this.inner.supportsMultipleOpenResults();
}

public boolean supportsMultipleResultSets() throws SQLException {
return this.inner.supportsMultipleResultSets();
}

public boolean supportsMultipleTransactions() throws SQLException {
return this.inner.supportsMultipleTransactions();
}

public boolean supportsNamedParameters() throws SQLException {
return this.inner.supportsNamedParameters();
}

public boolean supportsNonNullableColumns() throws SQLException {
return this.inner.supportsNonNullableColumns();
}

public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
return this.inner.supportsOpenCursorsAcrossCommit();
}

public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
return this.inner.supportsOpenCursorsAcrossRollback();
}

public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
return this.inner.supportsOpenStatementsAcrossCommit();
}

public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
return this.inner.supportsOpenStatementsAcrossRollback();
}

public boolean supportsOrderByUnrelated() throws SQLException {
return this.inner.supportsOrderByUnrelated();
}

public boolean supportsOuterJoins() throws SQLException {
return this.inner.supportsOuterJoins();
}

public boolean supportsPositionedDelete() throws SQLException {
return this.inner.supportsPositionedDelete();
}

public boolean supportsPositionedUpdate() throws SQLException {
return this.inner.supportsPositionedUpdate();
}

public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2) throws SQLException {
return this.inner.supportsResultSetConcurrency(paramInt1, paramInt2);
}

public boolean supportsResultSetHoldability(int paramInt) throws SQLException {
return this.inner.supportsResultSetHoldability(paramInt);
}

public boolean supportsResultSetType(int paramInt) throws SQLException {
return this.inner.supportsResultSetType(paramInt);
}

public boolean supportsSavepoints() throws SQLException {
return this.inner.supportsSavepoints();
}

public boolean supportsSchemasInDataManipulation() throws SQLException {
return this.inner.supportsSchemasInDataManipulation();
}

public boolean supportsSchemasInIndexDefinitions() throws SQLException {
return this.inner.supportsSchemasInIndexDefinitions();
}

public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
return this.inner.supportsSchemasInPrivilegeDefinitions();
}

public boolean supportsSchemasInProcedureCalls() throws SQLException {
return this.inner.supportsSchemasInProcedureCalls();
}

public boolean supportsSchemasInTableDefinitions() throws SQLException {
return this.inner.supportsSchemasInTableDefinitions();
}

public boolean supportsSelectForUpdate() throws SQLException {
return this.inner.supportsSelectForUpdate();
}

public boolean supportsStatementPooling() throws SQLException {
return this.inner.supportsStatementPooling();
}

public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
return this.inner.supportsStoredFunctionsUsingCallSyntax();
}

public boolean supportsStoredProcedures() throws SQLException {
return this.inner.supportsStoredProcedures();
}

public boolean supportsSubqueriesInComparisons() throws SQLException {
return this.inner.supportsSubqueriesInComparisons();
}

public boolean supportsSubqueriesInExists() throws SQLException {
return this.inner.supportsSubqueriesInExists();
}

public boolean supportsSubqueriesInIns() throws SQLException {
return this.inner.supportsSubqueriesInIns();
}

public boolean supportsSubqueriesInQuantifieds() throws SQLException {
return this.inner.supportsSubqueriesInQuantifieds();
}

public boolean supportsTableCorrelationNames() throws SQLException {
return this.inner.supportsTableCorrelationNames();
}

public boolean supportsTransactionIsolationLevel(int paramInt) throws SQLException {
return this.inner.supportsTransactionIsolationLevel(paramInt);
}

public boolean supportsTransactions() throws SQLException {
return this.inner.supportsTransactions();
}

public boolean supportsUnion() throws SQLException {
return this.inner.supportsUnion();
}

public boolean supportsUnionAll() throws SQLException {
return this.inner.supportsUnionAll();
}

public boolean updatesAreDetected(int paramInt) throws SQLException {
return this.inner.updatesAreDetected(paramInt);
}

public boolean usesLocalFilePerTable() throws SQLException {
return this.inner.usesLocalFilePerTable();
}

public boolean usesLocalFiles() throws SQLException {
return this.inner.usesLocalFiles();
}

public String getURL() throws SQLException {
return this.inner.getURL();
}

public boolean isReadOnly() throws SQLException {
return this.inner.isReadOnly();
}

public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
return this.inner.getAttributes(paramString1, paramString2, paramString3, paramString4);
}

public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
return this.inner.isWrapperFor(paramClass);
}

public Object unwrap(Class<?> paramClass) throws SQLException {
return this.inner.unwrap(paramClass);
}
}

