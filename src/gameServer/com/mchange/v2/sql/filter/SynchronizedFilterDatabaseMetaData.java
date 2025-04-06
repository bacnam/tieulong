/*     */ package com.mchange.v2.sql.filter;
/*     */ 
/*     */ import java.sql.Connection;
/*     */ import java.sql.DatabaseMetaData;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.RowIdLifetime;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SynchronizedFilterDatabaseMetaData
/*     */   implements DatabaseMetaData
/*     */ {
/*     */   protected DatabaseMetaData inner;
/*     */   
/*     */   private void __setInner(DatabaseMetaData paramDatabaseMetaData) {
/*  57 */     this.inner = paramDatabaseMetaData;
/*     */   }
/*     */   
/*     */   public SynchronizedFilterDatabaseMetaData(DatabaseMetaData paramDatabaseMetaData) {
/*  61 */     __setInner(paramDatabaseMetaData);
/*     */   }
/*     */   
/*     */   public SynchronizedFilterDatabaseMetaData() {}
/*     */   
/*     */   public synchronized void setInner(DatabaseMetaData paramDatabaseMetaData) {
/*  67 */     __setInner(paramDatabaseMetaData);
/*     */   }
/*     */   public synchronized DatabaseMetaData getInner() {
/*  70 */     return this.inner;
/*     */   }
/*     */   
/*     */   public synchronized boolean autoCommitFailureClosesAllResultSets() throws SQLException {
/*  74 */     return this.inner.autoCommitFailureClosesAllResultSets();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getCatalogs() throws SQLException {
/*  79 */     return this.inner.getCatalogs();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean allProceduresAreCallable() throws SQLException {
/*  84 */     return this.inner.allProceduresAreCallable();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean allTablesAreSelectable() throws SQLException {
/*  89 */     return this.inner.allTablesAreSelectable();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean dataDefinitionCausesTransactionCommit() throws SQLException {
/*  94 */     return this.inner.dataDefinitionCausesTransactionCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean dataDefinitionIgnoredInTransactions() throws SQLException {
/*  99 */     return this.inner.dataDefinitionIgnoredInTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean deletesAreDetected(int paramInt) throws SQLException {
/* 104 */     return this.inner.deletesAreDetected(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
/* 109 */     return this.inner.doesMaxRowSizeIncludeBlobs();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean generatedKeyAlwaysReturned() throws SQLException {
/* 114 */     return this.inner.generatedKeyAlwaysReturned();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) throws SQLException {
/* 119 */     return this.inner.getBestRowIdentifier(paramString1, paramString2, paramString3, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getCatalogSeparator() throws SQLException {
/* 124 */     return this.inner.getCatalogSeparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getCatalogTerm() throws SQLException {
/* 129 */     return this.inner.getCatalogTerm();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getClientInfoProperties() throws SQLException {
/* 134 */     return this.inner.getClientInfoProperties();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 139 */     return this.inner.getColumnPrivileges(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 144 */     return this.inner.getColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Connection getConnection() throws SQLException {
/* 149 */     return this.inner.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws SQLException {
/* 154 */     return this.inner.getCrossReference(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getDatabaseMajorVersion() throws SQLException {
/* 159 */     return this.inner.getDatabaseMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getDatabaseMinorVersion() throws SQLException {
/* 164 */     return this.inner.getDatabaseMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getDatabaseProductName() throws SQLException {
/* 169 */     return this.inner.getDatabaseProductName();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getDatabaseProductVersion() throws SQLException {
/* 174 */     return this.inner.getDatabaseProductVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getDefaultTransactionIsolation() throws SQLException {
/* 179 */     return this.inner.getDefaultTransactionIsolation();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getDriverMajorVersion() {
/* 184 */     return this.inner.getDriverMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getDriverMinorVersion() {
/* 189 */     return this.inner.getDriverMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getDriverName() throws SQLException {
/* 194 */     return this.inner.getDriverName();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getDriverVersion() throws SQLException {
/* 199 */     return this.inner.getDriverVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 204 */     return this.inner.getExportedKeys(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getExtraNameCharacters() throws SQLException {
/* 209 */     return this.inner.getExtraNameCharacters();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 214 */     return this.inner.getFunctionColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getFunctions(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 219 */     return this.inner.getFunctions(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getIdentifierQuoteString() throws SQLException {
/* 224 */     return this.inner.getIdentifierQuoteString();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 229 */     return this.inner.getImportedKeys(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
/* 234 */     return this.inner.getIndexInfo(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getJDBCMajorVersion() throws SQLException {
/* 239 */     return this.inner.getJDBCMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getJDBCMinorVersion() throws SQLException {
/* 244 */     return this.inner.getJDBCMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxBinaryLiteralLength() throws SQLException {
/* 249 */     return this.inner.getMaxBinaryLiteralLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxCatalogNameLength() throws SQLException {
/* 254 */     return this.inner.getMaxCatalogNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxCharLiteralLength() throws SQLException {
/* 259 */     return this.inner.getMaxCharLiteralLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxColumnNameLength() throws SQLException {
/* 264 */     return this.inner.getMaxColumnNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxColumnsInGroupBy() throws SQLException {
/* 269 */     return this.inner.getMaxColumnsInGroupBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxColumnsInIndex() throws SQLException {
/* 274 */     return this.inner.getMaxColumnsInIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxColumnsInOrderBy() throws SQLException {
/* 279 */     return this.inner.getMaxColumnsInOrderBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxColumnsInSelect() throws SQLException {
/* 284 */     return this.inner.getMaxColumnsInSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxColumnsInTable() throws SQLException {
/* 289 */     return this.inner.getMaxColumnsInTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxConnections() throws SQLException {
/* 294 */     return this.inner.getMaxConnections();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxCursorNameLength() throws SQLException {
/* 299 */     return this.inner.getMaxCursorNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxIndexLength() throws SQLException {
/* 304 */     return this.inner.getMaxIndexLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxProcedureNameLength() throws SQLException {
/* 309 */     return this.inner.getMaxProcedureNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxRowSize() throws SQLException {
/* 314 */     return this.inner.getMaxRowSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxSchemaNameLength() throws SQLException {
/* 319 */     return this.inner.getMaxSchemaNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxStatementLength() throws SQLException {
/* 324 */     return this.inner.getMaxStatementLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxStatements() throws SQLException {
/* 329 */     return this.inner.getMaxStatements();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxTableNameLength() throws SQLException {
/* 334 */     return this.inner.getMaxTableNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxTablesInSelect() throws SQLException {
/* 339 */     return this.inner.getMaxTablesInSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getMaxUserNameLength() throws SQLException {
/* 344 */     return this.inner.getMaxUserNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getNumericFunctions() throws SQLException {
/* 349 */     return this.inner.getNumericFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 354 */     return this.inner.getPrimaryKeys(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 359 */     return this.inner.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getProcedureTerm() throws SQLException {
/* 364 */     return this.inner.getProcedureTerm();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 369 */     return this.inner.getProcedures(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 374 */     return this.inner.getPseudoColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getResultSetHoldability() throws SQLException {
/* 379 */     return this.inner.getResultSetHoldability();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized RowIdLifetime getRowIdLifetime() throws SQLException {
/* 384 */     return this.inner.getRowIdLifetime();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getSQLKeywords() throws SQLException {
/* 389 */     return this.inner.getSQLKeywords();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int getSQLStateType() throws SQLException {
/* 394 */     return this.inner.getSQLStateType();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getSchemaTerm() throws SQLException {
/* 399 */     return this.inner.getSchemaTerm();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getSchemas(String paramString1, String paramString2) throws SQLException {
/* 404 */     return this.inner.getSchemas(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getSchemas() throws SQLException {
/* 409 */     return this.inner.getSchemas();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getSearchStringEscape() throws SQLException {
/* 414 */     return this.inner.getSearchStringEscape();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getStringFunctions() throws SQLException {
/* 419 */     return this.inner.getStringFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 424 */     return this.inner.getSuperTables(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 429 */     return this.inner.getSuperTypes(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getSystemFunctions() throws SQLException {
/* 434 */     return this.inner.getSystemFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 439 */     return this.inner.getTablePrivileges(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getTableTypes() throws SQLException {
/* 444 */     return this.inner.getTableTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) throws SQLException {
/* 449 */     return this.inner.getTables(paramString1, paramString2, paramString3, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getTimeDateFunctions() throws SQLException {
/* 454 */     return this.inner.getTimeDateFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getTypeInfo() throws SQLException {
/* 459 */     return this.inner.getTypeInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) throws SQLException {
/* 464 */     return this.inner.getUDTs(paramString1, paramString2, paramString3, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getUserName() throws SQLException {
/* 469 */     return this.inner.getUserName();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 474 */     return this.inner.getVersionColumns(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean insertsAreDetected(int paramInt) throws SQLException {
/* 479 */     return this.inner.insertsAreDetected(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isCatalogAtStart() throws SQLException {
/* 484 */     return this.inner.isCatalogAtStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean locatorsUpdateCopy() throws SQLException {
/* 489 */     return this.inner.locatorsUpdateCopy();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean nullPlusNonNullIsNull() throws SQLException {
/* 494 */     return this.inner.nullPlusNonNullIsNull();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean nullsAreSortedAtEnd() throws SQLException {
/* 499 */     return this.inner.nullsAreSortedAtEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean nullsAreSortedAtStart() throws SQLException {
/* 504 */     return this.inner.nullsAreSortedAtStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean nullsAreSortedHigh() throws SQLException {
/* 509 */     return this.inner.nullsAreSortedHigh();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean nullsAreSortedLow() throws SQLException {
/* 514 */     return this.inner.nullsAreSortedLow();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean othersDeletesAreVisible(int paramInt) throws SQLException {
/* 519 */     return this.inner.othersDeletesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean othersInsertsAreVisible(int paramInt) throws SQLException {
/* 524 */     return this.inner.othersInsertsAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean othersUpdatesAreVisible(int paramInt) throws SQLException {
/* 529 */     return this.inner.othersUpdatesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean ownDeletesAreVisible(int paramInt) throws SQLException {
/* 534 */     return this.inner.ownDeletesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean ownInsertsAreVisible(int paramInt) throws SQLException {
/* 539 */     return this.inner.ownInsertsAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean ownUpdatesAreVisible(int paramInt) throws SQLException {
/* 544 */     return this.inner.ownUpdatesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean storesLowerCaseIdentifiers() throws SQLException {
/* 549 */     return this.inner.storesLowerCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
/* 554 */     return this.inner.storesLowerCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean storesMixedCaseIdentifiers() throws SQLException {
/* 559 */     return this.inner.storesMixedCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
/* 564 */     return this.inner.storesMixedCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean storesUpperCaseIdentifiers() throws SQLException {
/* 569 */     return this.inner.storesUpperCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
/* 574 */     return this.inner.storesUpperCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsANSI92EntryLevelSQL() throws SQLException {
/* 579 */     return this.inner.supportsANSI92EntryLevelSQL();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsANSI92FullSQL() throws SQLException {
/* 584 */     return this.inner.supportsANSI92FullSQL();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsANSI92IntermediateSQL() throws SQLException {
/* 589 */     return this.inner.supportsANSI92IntermediateSQL();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsAlterTableWithAddColumn() throws SQLException {
/* 594 */     return this.inner.supportsAlterTableWithAddColumn();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsAlterTableWithDropColumn() throws SQLException {
/* 599 */     return this.inner.supportsAlterTableWithDropColumn();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsBatchUpdates() throws SQLException {
/* 604 */     return this.inner.supportsBatchUpdates();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCatalogsInDataManipulation() throws SQLException {
/* 609 */     return this.inner.supportsCatalogsInDataManipulation();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCatalogsInIndexDefinitions() throws SQLException {
/* 614 */     return this.inner.supportsCatalogsInIndexDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
/* 619 */     return this.inner.supportsCatalogsInPrivilegeDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCatalogsInProcedureCalls() throws SQLException {
/* 624 */     return this.inner.supportsCatalogsInProcedureCalls();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCatalogsInTableDefinitions() throws SQLException {
/* 629 */     return this.inner.supportsCatalogsInTableDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsColumnAliasing() throws SQLException {
/* 634 */     return this.inner.supportsColumnAliasing();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsConvert(int paramInt1, int paramInt2) throws SQLException {
/* 639 */     return this.inner.supportsConvert(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsConvert() throws SQLException {
/* 644 */     return this.inner.supportsConvert();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCoreSQLGrammar() throws SQLException {
/* 649 */     return this.inner.supportsCoreSQLGrammar();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsCorrelatedSubqueries() throws SQLException {
/* 654 */     return this.inner.supportsCorrelatedSubqueries();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
/* 659 */     return this.inner.supportsDataDefinitionAndDataManipulationTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsDataManipulationTransactionsOnly() throws SQLException {
/* 664 */     return this.inner.supportsDataManipulationTransactionsOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsDifferentTableCorrelationNames() throws SQLException {
/* 669 */     return this.inner.supportsDifferentTableCorrelationNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsExpressionsInOrderBy() throws SQLException {
/* 674 */     return this.inner.supportsExpressionsInOrderBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsExtendedSQLGrammar() throws SQLException {
/* 679 */     return this.inner.supportsExtendedSQLGrammar();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsFullOuterJoins() throws SQLException {
/* 684 */     return this.inner.supportsFullOuterJoins();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsGetGeneratedKeys() throws SQLException {
/* 689 */     return this.inner.supportsGetGeneratedKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsGroupBy() throws SQLException {
/* 694 */     return this.inner.supportsGroupBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsGroupByBeyondSelect() throws SQLException {
/* 699 */     return this.inner.supportsGroupByBeyondSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsGroupByUnrelated() throws SQLException {
/* 704 */     return this.inner.supportsGroupByUnrelated();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsIntegrityEnhancementFacility() throws SQLException {
/* 709 */     return this.inner.supportsIntegrityEnhancementFacility();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsLikeEscapeClause() throws SQLException {
/* 714 */     return this.inner.supportsLikeEscapeClause();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsLimitedOuterJoins() throws SQLException {
/* 719 */     return this.inner.supportsLimitedOuterJoins();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsMinimumSQLGrammar() throws SQLException {
/* 724 */     return this.inner.supportsMinimumSQLGrammar();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsMixedCaseIdentifiers() throws SQLException {
/* 729 */     return this.inner.supportsMixedCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
/* 734 */     return this.inner.supportsMixedCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsMultipleOpenResults() throws SQLException {
/* 739 */     return this.inner.supportsMultipleOpenResults();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsMultipleResultSets() throws SQLException {
/* 744 */     return this.inner.supportsMultipleResultSets();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsMultipleTransactions() throws SQLException {
/* 749 */     return this.inner.supportsMultipleTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsNamedParameters() throws SQLException {
/* 754 */     return this.inner.supportsNamedParameters();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsNonNullableColumns() throws SQLException {
/* 759 */     return this.inner.supportsNonNullableColumns();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsOpenCursorsAcrossCommit() throws SQLException {
/* 764 */     return this.inner.supportsOpenCursorsAcrossCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsOpenCursorsAcrossRollback() throws SQLException {
/* 769 */     return this.inner.supportsOpenCursorsAcrossRollback();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsOpenStatementsAcrossCommit() throws SQLException {
/* 774 */     return this.inner.supportsOpenStatementsAcrossCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsOpenStatementsAcrossRollback() throws SQLException {
/* 779 */     return this.inner.supportsOpenStatementsAcrossRollback();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsOrderByUnrelated() throws SQLException {
/* 784 */     return this.inner.supportsOrderByUnrelated();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsOuterJoins() throws SQLException {
/* 789 */     return this.inner.supportsOuterJoins();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsPositionedDelete() throws SQLException {
/* 794 */     return this.inner.supportsPositionedDelete();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsPositionedUpdate() throws SQLException {
/* 799 */     return this.inner.supportsPositionedUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsResultSetConcurrency(int paramInt1, int paramInt2) throws SQLException {
/* 804 */     return this.inner.supportsResultSetConcurrency(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsResultSetHoldability(int paramInt) throws SQLException {
/* 809 */     return this.inner.supportsResultSetHoldability(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsResultSetType(int paramInt) throws SQLException {
/* 814 */     return this.inner.supportsResultSetType(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSavepoints() throws SQLException {
/* 819 */     return this.inner.supportsSavepoints();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSchemasInDataManipulation() throws SQLException {
/* 824 */     return this.inner.supportsSchemasInDataManipulation();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSchemasInIndexDefinitions() throws SQLException {
/* 829 */     return this.inner.supportsSchemasInIndexDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
/* 834 */     return this.inner.supportsSchemasInPrivilegeDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSchemasInProcedureCalls() throws SQLException {
/* 839 */     return this.inner.supportsSchemasInProcedureCalls();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSchemasInTableDefinitions() throws SQLException {
/* 844 */     return this.inner.supportsSchemasInTableDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSelectForUpdate() throws SQLException {
/* 849 */     return this.inner.supportsSelectForUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsStatementPooling() throws SQLException {
/* 854 */     return this.inner.supportsStatementPooling();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 859 */     return this.inner.supportsStoredFunctionsUsingCallSyntax();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsStoredProcedures() throws SQLException {
/* 864 */     return this.inner.supportsStoredProcedures();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSubqueriesInComparisons() throws SQLException {
/* 869 */     return this.inner.supportsSubqueriesInComparisons();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSubqueriesInExists() throws SQLException {
/* 874 */     return this.inner.supportsSubqueriesInExists();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSubqueriesInIns() throws SQLException {
/* 879 */     return this.inner.supportsSubqueriesInIns();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsSubqueriesInQuantifieds() throws SQLException {
/* 884 */     return this.inner.supportsSubqueriesInQuantifieds();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsTableCorrelationNames() throws SQLException {
/* 889 */     return this.inner.supportsTableCorrelationNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsTransactionIsolationLevel(int paramInt) throws SQLException {
/* 894 */     return this.inner.supportsTransactionIsolationLevel(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsTransactions() throws SQLException {
/* 899 */     return this.inner.supportsTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsUnion() throws SQLException {
/* 904 */     return this.inner.supportsUnion();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean supportsUnionAll() throws SQLException {
/* 909 */     return this.inner.supportsUnionAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean updatesAreDetected(int paramInt) throws SQLException {
/* 914 */     return this.inner.updatesAreDetected(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean usesLocalFilePerTable() throws SQLException {
/* 919 */     return this.inner.usesLocalFilePerTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean usesLocalFiles() throws SQLException {
/* 924 */     return this.inner.usesLocalFiles();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getURL() throws SQLException {
/* 929 */     return this.inner.getURL();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isReadOnly() throws SQLException {
/* 934 */     return this.inner.isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 939 */     return this.inner.getAttributes(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 944 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
/* 949 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/SynchronizedFilterDatabaseMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */