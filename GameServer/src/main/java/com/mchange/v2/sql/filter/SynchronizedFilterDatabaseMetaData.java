package com.mchange.v2.sql.filter;

import java.sql.*;

public abstract class SynchronizedFilterDatabaseMetaData
        implements DatabaseMetaData {
    protected DatabaseMetaData inner;

    public SynchronizedFilterDatabaseMetaData(DatabaseMetaData paramDatabaseMetaData) {
        __setInner(paramDatabaseMetaData);
    }

    public SynchronizedFilterDatabaseMetaData() {
    }

    private void __setInner(DatabaseMetaData paramDatabaseMetaData) {
        this.inner = paramDatabaseMetaData;
    }

    public synchronized DatabaseMetaData getInner() {
        return this.inner;
    }

    public synchronized void setInner(DatabaseMetaData paramDatabaseMetaData) {
        __setInner(paramDatabaseMetaData);
    }

    public synchronized boolean autoCommitFailureClosesAllResultSets() throws SQLException {
        return this.inner.autoCommitFailureClosesAllResultSets();
    }

    public synchronized ResultSet getCatalogs() throws SQLException {
        return this.inner.getCatalogs();
    }

    public synchronized boolean allProceduresAreCallable() throws SQLException {
        return this.inner.allProceduresAreCallable();
    }

    public synchronized boolean allTablesAreSelectable() throws SQLException {
        return this.inner.allTablesAreSelectable();
    }

    public synchronized boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return this.inner.dataDefinitionCausesTransactionCommit();
    }

    public synchronized boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return this.inner.dataDefinitionIgnoredInTransactions();
    }

    public synchronized boolean deletesAreDetected(int paramInt) throws SQLException {
        return this.inner.deletesAreDetected(paramInt);
    }

    public synchronized boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return this.inner.doesMaxRowSizeIncludeBlobs();
    }

    public synchronized boolean generatedKeyAlwaysReturned() throws SQLException {
        return this.inner.generatedKeyAlwaysReturned();
    }

    public synchronized ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) throws SQLException {
        return this.inner.getBestRowIdentifier(paramString1, paramString2, paramString3, paramInt, paramBoolean);
    }

    public synchronized String getCatalogSeparator() throws SQLException {
        return this.inner.getCatalogSeparator();
    }

    public synchronized String getCatalogTerm() throws SQLException {
        return this.inner.getCatalogTerm();
    }

    public synchronized ResultSet getClientInfoProperties() throws SQLException {
        return this.inner.getClientInfoProperties();
    }

    public synchronized ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
        return this.inner.getColumnPrivileges(paramString1, paramString2, paramString3, paramString4);
    }

    public synchronized ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
        return this.inner.getColumns(paramString1, paramString2, paramString3, paramString4);
    }

    public synchronized Connection getConnection() throws SQLException {
        return this.inner.getConnection();
    }

    public synchronized ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws SQLException {
        return this.inner.getCrossReference(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
    }

    public synchronized int getDatabaseMajorVersion() throws SQLException {
        return this.inner.getDatabaseMajorVersion();
    }

    public synchronized int getDatabaseMinorVersion() throws SQLException {
        return this.inner.getDatabaseMinorVersion();
    }

    public synchronized String getDatabaseProductName() throws SQLException {
        return this.inner.getDatabaseProductName();
    }

    public synchronized String getDatabaseProductVersion() throws SQLException {
        return this.inner.getDatabaseProductVersion();
    }

    public synchronized int getDefaultTransactionIsolation() throws SQLException {
        return this.inner.getDefaultTransactionIsolation();
    }

    public synchronized int getDriverMajorVersion() {
        return this.inner.getDriverMajorVersion();
    }

    public synchronized int getDriverMinorVersion() {
        return this.inner.getDriverMinorVersion();
    }

    public synchronized String getDriverName() throws SQLException {
        return this.inner.getDriverName();
    }

    public synchronized String getDriverVersion() throws SQLException {
        return this.inner.getDriverVersion();
    }

    public synchronized ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getExportedKeys(paramString1, paramString2, paramString3);
    }

    public synchronized String getExtraNameCharacters() throws SQLException {
        return this.inner.getExtraNameCharacters();
    }

    public synchronized ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
        return this.inner.getFunctionColumns(paramString1, paramString2, paramString3, paramString4);
    }

    public synchronized ResultSet getFunctions(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getFunctions(paramString1, paramString2, paramString3);
    }

    public synchronized String getIdentifierQuoteString() throws SQLException {
        return this.inner.getIdentifierQuoteString();
    }

    public synchronized ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getImportedKeys(paramString1, paramString2, paramString3);
    }

    public synchronized ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
        return this.inner.getIndexInfo(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
    }

    public synchronized int getJDBCMajorVersion() throws SQLException {
        return this.inner.getJDBCMajorVersion();
    }

    public synchronized int getJDBCMinorVersion() throws SQLException {
        return this.inner.getJDBCMinorVersion();
    }

    public synchronized int getMaxBinaryLiteralLength() throws SQLException {
        return this.inner.getMaxBinaryLiteralLength();
    }

    public synchronized int getMaxCatalogNameLength() throws SQLException {
        return this.inner.getMaxCatalogNameLength();
    }

    public synchronized int getMaxCharLiteralLength() throws SQLException {
        return this.inner.getMaxCharLiteralLength();
    }

    public synchronized int getMaxColumnNameLength() throws SQLException {
        return this.inner.getMaxColumnNameLength();
    }

    public synchronized int getMaxColumnsInGroupBy() throws SQLException {
        return this.inner.getMaxColumnsInGroupBy();
    }

    public synchronized int getMaxColumnsInIndex() throws SQLException {
        return this.inner.getMaxColumnsInIndex();
    }

    public synchronized int getMaxColumnsInOrderBy() throws SQLException {
        return this.inner.getMaxColumnsInOrderBy();
    }

    public synchronized int getMaxColumnsInSelect() throws SQLException {
        return this.inner.getMaxColumnsInSelect();
    }

    public synchronized int getMaxColumnsInTable() throws SQLException {
        return this.inner.getMaxColumnsInTable();
    }

    public synchronized int getMaxConnections() throws SQLException {
        return this.inner.getMaxConnections();
    }

    public synchronized int getMaxCursorNameLength() throws SQLException {
        return this.inner.getMaxCursorNameLength();
    }

    public synchronized int getMaxIndexLength() throws SQLException {
        return this.inner.getMaxIndexLength();
    }

    public synchronized int getMaxProcedureNameLength() throws SQLException {
        return this.inner.getMaxProcedureNameLength();
    }

    public synchronized int getMaxRowSize() throws SQLException {
        return this.inner.getMaxRowSize();
    }

    public synchronized int getMaxSchemaNameLength() throws SQLException {
        return this.inner.getMaxSchemaNameLength();
    }

    public synchronized int getMaxStatementLength() throws SQLException {
        return this.inner.getMaxStatementLength();
    }

    public synchronized int getMaxStatements() throws SQLException {
        return this.inner.getMaxStatements();
    }

    public synchronized int getMaxTableNameLength() throws SQLException {
        return this.inner.getMaxTableNameLength();
    }

    public synchronized int getMaxTablesInSelect() throws SQLException {
        return this.inner.getMaxTablesInSelect();
    }

    public synchronized int getMaxUserNameLength() throws SQLException {
        return this.inner.getMaxUserNameLength();
    }

    public synchronized String getNumericFunctions() throws SQLException {
        return this.inner.getNumericFunctions();
    }

    public synchronized ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getPrimaryKeys(paramString1, paramString2, paramString3);
    }

    public synchronized ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
        return this.inner.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
    }

    public synchronized String getProcedureTerm() throws SQLException {
        return this.inner.getProcedureTerm();
    }

    public synchronized ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getProcedures(paramString1, paramString2, paramString3);
    }

    public synchronized ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
        return this.inner.getPseudoColumns(paramString1, paramString2, paramString3, paramString4);
    }

    public synchronized int getResultSetHoldability() throws SQLException {
        return this.inner.getResultSetHoldability();
    }

    public synchronized RowIdLifetime getRowIdLifetime() throws SQLException {
        return this.inner.getRowIdLifetime();
    }

    public synchronized String getSQLKeywords() throws SQLException {
        return this.inner.getSQLKeywords();
    }

    public synchronized int getSQLStateType() throws SQLException {
        return this.inner.getSQLStateType();
    }

    public synchronized String getSchemaTerm() throws SQLException {
        return this.inner.getSchemaTerm();
    }

    public synchronized ResultSet getSchemas(String paramString1, String paramString2) throws SQLException {
        return this.inner.getSchemas(paramString1, paramString2);
    }

    public synchronized ResultSet getSchemas() throws SQLException {
        return this.inner.getSchemas();
    }

    public synchronized String getSearchStringEscape() throws SQLException {
        return this.inner.getSearchStringEscape();
    }

    public synchronized String getStringFunctions() throws SQLException {
        return this.inner.getStringFunctions();
    }

    public synchronized ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getSuperTables(paramString1, paramString2, paramString3);
    }

    public synchronized ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getSuperTypes(paramString1, paramString2, paramString3);
    }

    public synchronized String getSystemFunctions() throws SQLException {
        return this.inner.getSystemFunctions();
    }

    public synchronized ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getTablePrivileges(paramString1, paramString2, paramString3);
    }

    public synchronized ResultSet getTableTypes() throws SQLException {
        return this.inner.getTableTypes();
    }

    public synchronized ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) throws SQLException {
        return this.inner.getTables(paramString1, paramString2, paramString3, paramArrayOfString);
    }

    public synchronized String getTimeDateFunctions() throws SQLException {
        return this.inner.getTimeDateFunctions();
    }

    public synchronized ResultSet getTypeInfo() throws SQLException {
        return this.inner.getTypeInfo();
    }

    public synchronized ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) throws SQLException {
        return this.inner.getUDTs(paramString1, paramString2, paramString3, paramArrayOfint);
    }

    public synchronized String getUserName() throws SQLException {
        return this.inner.getUserName();
    }

    public synchronized ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) throws SQLException {
        return this.inner.getVersionColumns(paramString1, paramString2, paramString3);
    }

    public synchronized boolean insertsAreDetected(int paramInt) throws SQLException {
        return this.inner.insertsAreDetected(paramInt);
    }

    public synchronized boolean isCatalogAtStart() throws SQLException {
        return this.inner.isCatalogAtStart();
    }

    public synchronized boolean locatorsUpdateCopy() throws SQLException {
        return this.inner.locatorsUpdateCopy();
    }

    public synchronized boolean nullPlusNonNullIsNull() throws SQLException {
        return this.inner.nullPlusNonNullIsNull();
    }

    public synchronized boolean nullsAreSortedAtEnd() throws SQLException {
        return this.inner.nullsAreSortedAtEnd();
    }

    public synchronized boolean nullsAreSortedAtStart() throws SQLException {
        return this.inner.nullsAreSortedAtStart();
    }

    public synchronized boolean nullsAreSortedHigh() throws SQLException {
        return this.inner.nullsAreSortedHigh();
    }

    public synchronized boolean nullsAreSortedLow() throws SQLException {
        return this.inner.nullsAreSortedLow();
    }

    public synchronized boolean othersDeletesAreVisible(int paramInt) throws SQLException {
        return this.inner.othersDeletesAreVisible(paramInt);
    }

    public synchronized boolean othersInsertsAreVisible(int paramInt) throws SQLException {
        return this.inner.othersInsertsAreVisible(paramInt);
    }

    public synchronized boolean othersUpdatesAreVisible(int paramInt) throws SQLException {
        return this.inner.othersUpdatesAreVisible(paramInt);
    }

    public synchronized boolean ownDeletesAreVisible(int paramInt) throws SQLException {
        return this.inner.ownDeletesAreVisible(paramInt);
    }

    public synchronized boolean ownInsertsAreVisible(int paramInt) throws SQLException {
        return this.inner.ownInsertsAreVisible(paramInt);
    }

    public synchronized boolean ownUpdatesAreVisible(int paramInt) throws SQLException {
        return this.inner.ownUpdatesAreVisible(paramInt);
    }

    public synchronized boolean storesLowerCaseIdentifiers() throws SQLException {
        return this.inner.storesLowerCaseIdentifiers();
    }

    public synchronized boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return this.inner.storesLowerCaseQuotedIdentifiers();
    }

    public synchronized boolean storesMixedCaseIdentifiers() throws SQLException {
        return this.inner.storesMixedCaseIdentifiers();
    }

    public synchronized boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return this.inner.storesMixedCaseQuotedIdentifiers();
    }

    public synchronized boolean storesUpperCaseIdentifiers() throws SQLException {
        return this.inner.storesUpperCaseIdentifiers();
    }

    public synchronized boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return this.inner.storesUpperCaseQuotedIdentifiers();
    }

    public synchronized boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return this.inner.supportsANSI92EntryLevelSQL();
    }

    public synchronized boolean supportsANSI92FullSQL() throws SQLException {
        return this.inner.supportsANSI92FullSQL();
    }

    public synchronized boolean supportsANSI92IntermediateSQL() throws SQLException {
        return this.inner.supportsANSI92IntermediateSQL();
    }

    public synchronized boolean supportsAlterTableWithAddColumn() throws SQLException {
        return this.inner.supportsAlterTableWithAddColumn();
    }

    public synchronized boolean supportsAlterTableWithDropColumn() throws SQLException {
        return this.inner.supportsAlterTableWithDropColumn();
    }

    public synchronized boolean supportsBatchUpdates() throws SQLException {
        return this.inner.supportsBatchUpdates();
    }

    public synchronized boolean supportsCatalogsInDataManipulation() throws SQLException {
        return this.inner.supportsCatalogsInDataManipulation();
    }

    public synchronized boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return this.inner.supportsCatalogsInIndexDefinitions();
    }

    public synchronized boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return this.inner.supportsCatalogsInPrivilegeDefinitions();
    }

    public synchronized boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return this.inner.supportsCatalogsInProcedureCalls();
    }

    public synchronized boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return this.inner.supportsCatalogsInTableDefinitions();
    }

    public synchronized boolean supportsColumnAliasing() throws SQLException {
        return this.inner.supportsColumnAliasing();
    }

    public synchronized boolean supportsConvert(int paramInt1, int paramInt2) throws SQLException {
        return this.inner.supportsConvert(paramInt1, paramInt2);
    }

    public synchronized boolean supportsConvert() throws SQLException {
        return this.inner.supportsConvert();
    }

    public synchronized boolean supportsCoreSQLGrammar() throws SQLException {
        return this.inner.supportsCoreSQLGrammar();
    }

    public synchronized boolean supportsCorrelatedSubqueries() throws SQLException {
        return this.inner.supportsCorrelatedSubqueries();
    }

    public synchronized boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return this.inner.supportsDataDefinitionAndDataManipulationTransactions();
    }

    public synchronized boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return this.inner.supportsDataManipulationTransactionsOnly();
    }

    public synchronized boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return this.inner.supportsDifferentTableCorrelationNames();
    }

    public synchronized boolean supportsExpressionsInOrderBy() throws SQLException {
        return this.inner.supportsExpressionsInOrderBy();
    }

    public synchronized boolean supportsExtendedSQLGrammar() throws SQLException {
        return this.inner.supportsExtendedSQLGrammar();
    }

    public synchronized boolean supportsFullOuterJoins() throws SQLException {
        return this.inner.supportsFullOuterJoins();
    }

    public synchronized boolean supportsGetGeneratedKeys() throws SQLException {
        return this.inner.supportsGetGeneratedKeys();
    }

    public synchronized boolean supportsGroupBy() throws SQLException {
        return this.inner.supportsGroupBy();
    }

    public synchronized boolean supportsGroupByBeyondSelect() throws SQLException {
        return this.inner.supportsGroupByBeyondSelect();
    }

    public synchronized boolean supportsGroupByUnrelated() throws SQLException {
        return this.inner.supportsGroupByUnrelated();
    }

    public synchronized boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return this.inner.supportsIntegrityEnhancementFacility();
    }

    public synchronized boolean supportsLikeEscapeClause() throws SQLException {
        return this.inner.supportsLikeEscapeClause();
    }

    public synchronized boolean supportsLimitedOuterJoins() throws SQLException {
        return this.inner.supportsLimitedOuterJoins();
    }

    public synchronized boolean supportsMinimumSQLGrammar() throws SQLException {
        return this.inner.supportsMinimumSQLGrammar();
    }

    public synchronized boolean supportsMixedCaseIdentifiers() throws SQLException {
        return this.inner.supportsMixedCaseIdentifiers();
    }

    public synchronized boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return this.inner.supportsMixedCaseQuotedIdentifiers();
    }

    public synchronized boolean supportsMultipleOpenResults() throws SQLException {
        return this.inner.supportsMultipleOpenResults();
    }

    public synchronized boolean supportsMultipleResultSets() throws SQLException {
        return this.inner.supportsMultipleResultSets();
    }

    public synchronized boolean supportsMultipleTransactions() throws SQLException {
        return this.inner.supportsMultipleTransactions();
    }

    public synchronized boolean supportsNamedParameters() throws SQLException {
        return this.inner.supportsNamedParameters();
    }

    public synchronized boolean supportsNonNullableColumns() throws SQLException {
        return this.inner.supportsNonNullableColumns();
    }

    public synchronized boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return this.inner.supportsOpenCursorsAcrossCommit();
    }

    public synchronized boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return this.inner.supportsOpenCursorsAcrossRollback();
    }

    public synchronized boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return this.inner.supportsOpenStatementsAcrossCommit();
    }

    public synchronized boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return this.inner.supportsOpenStatementsAcrossRollback();
    }

    public synchronized boolean supportsOrderByUnrelated() throws SQLException {
        return this.inner.supportsOrderByUnrelated();
    }

    public synchronized boolean supportsOuterJoins() throws SQLException {
        return this.inner.supportsOuterJoins();
    }

    public synchronized boolean supportsPositionedDelete() throws SQLException {
        return this.inner.supportsPositionedDelete();
    }

    public synchronized boolean supportsPositionedUpdate() throws SQLException {
        return this.inner.supportsPositionedUpdate();
    }

    public synchronized boolean supportsResultSetConcurrency(int paramInt1, int paramInt2) throws SQLException {
        return this.inner.supportsResultSetConcurrency(paramInt1, paramInt2);
    }

    public synchronized boolean supportsResultSetHoldability(int paramInt) throws SQLException {
        return this.inner.supportsResultSetHoldability(paramInt);
    }

    public synchronized boolean supportsResultSetType(int paramInt) throws SQLException {
        return this.inner.supportsResultSetType(paramInt);
    }

    public synchronized boolean supportsSavepoints() throws SQLException {
        return this.inner.supportsSavepoints();
    }

    public synchronized boolean supportsSchemasInDataManipulation() throws SQLException {
        return this.inner.supportsSchemasInDataManipulation();
    }

    public synchronized boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return this.inner.supportsSchemasInIndexDefinitions();
    }

    public synchronized boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return this.inner.supportsSchemasInPrivilegeDefinitions();
    }

    public synchronized boolean supportsSchemasInProcedureCalls() throws SQLException {
        return this.inner.supportsSchemasInProcedureCalls();
    }

    public synchronized boolean supportsSchemasInTableDefinitions() throws SQLException {
        return this.inner.supportsSchemasInTableDefinitions();
    }

    public synchronized boolean supportsSelectForUpdate() throws SQLException {
        return this.inner.supportsSelectForUpdate();
    }

    public synchronized boolean supportsStatementPooling() throws SQLException {
        return this.inner.supportsStatementPooling();
    }

    public synchronized boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
        return this.inner.supportsStoredFunctionsUsingCallSyntax();
    }

    public synchronized boolean supportsStoredProcedures() throws SQLException {
        return this.inner.supportsStoredProcedures();
    }

    public synchronized boolean supportsSubqueriesInComparisons() throws SQLException {
        return this.inner.supportsSubqueriesInComparisons();
    }

    public synchronized boolean supportsSubqueriesInExists() throws SQLException {
        return this.inner.supportsSubqueriesInExists();
    }

    public synchronized boolean supportsSubqueriesInIns() throws SQLException {
        return this.inner.supportsSubqueriesInIns();
    }

    public synchronized boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return this.inner.supportsSubqueriesInQuantifieds();
    }

    public synchronized boolean supportsTableCorrelationNames() throws SQLException {
        return this.inner.supportsTableCorrelationNames();
    }

    public synchronized boolean supportsTransactionIsolationLevel(int paramInt) throws SQLException {
        return this.inner.supportsTransactionIsolationLevel(paramInt);
    }

    public synchronized boolean supportsTransactions() throws SQLException {
        return this.inner.supportsTransactions();
    }

    public synchronized boolean supportsUnion() throws SQLException {
        return this.inner.supportsUnion();
    }

    public synchronized boolean supportsUnionAll() throws SQLException {
        return this.inner.supportsUnionAll();
    }

    public synchronized boolean updatesAreDetected(int paramInt) throws SQLException {
        return this.inner.updatesAreDetected(paramInt);
    }

    public synchronized boolean usesLocalFilePerTable() throws SQLException {
        return this.inner.usesLocalFilePerTable();
    }

    public synchronized boolean usesLocalFiles() throws SQLException {
        return this.inner.usesLocalFiles();
    }

    public synchronized String getURL() throws SQLException {
        return this.inner.getURL();
    }

    public synchronized boolean isReadOnly() throws SQLException {
        return this.inner.isReadOnly();
    }

    public synchronized ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
        return this.inner.getAttributes(paramString1, paramString2, paramString3, paramString4);
    }

    public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
        return this.inner.isWrapperFor(paramClass);
    }

    public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
        return this.inner.unwrap(paramClass);
    }
}

