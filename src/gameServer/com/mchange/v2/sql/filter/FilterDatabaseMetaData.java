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
/*     */ public abstract class FilterDatabaseMetaData
/*     */   implements DatabaseMetaData
/*     */ {
/*     */   protected DatabaseMetaData inner;
/*     */   
/*     */   private void __setInner(DatabaseMetaData paramDatabaseMetaData) {
/*  57 */     this.inner = paramDatabaseMetaData;
/*     */   }
/*     */   
/*     */   public FilterDatabaseMetaData(DatabaseMetaData paramDatabaseMetaData) {
/*  61 */     __setInner(paramDatabaseMetaData);
/*     */   }
/*     */   
/*     */   public FilterDatabaseMetaData() {}
/*     */   
/*     */   public void setInner(DatabaseMetaData paramDatabaseMetaData) {
/*  67 */     __setInner(paramDatabaseMetaData);
/*     */   }
/*     */   public DatabaseMetaData getInner() {
/*  70 */     return this.inner;
/*     */   }
/*     */   
/*     */   public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
/*  74 */     return this.inner.autoCommitFailureClosesAllResultSets();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getCatalogs() throws SQLException {
/*  79 */     return this.inner.getCatalogs();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allProceduresAreCallable() throws SQLException {
/*  84 */     return this.inner.allProceduresAreCallable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean allTablesAreSelectable() throws SQLException {
/*  89 */     return this.inner.allTablesAreSelectable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
/*  94 */     return this.inner.dataDefinitionCausesTransactionCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
/*  99 */     return this.inner.dataDefinitionIgnoredInTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean deletesAreDetected(int paramInt) throws SQLException {
/* 104 */     return this.inner.deletesAreDetected(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
/* 109 */     return this.inner.doesMaxRowSizeIncludeBlobs();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean generatedKeyAlwaysReturned() throws SQLException {
/* 114 */     return this.inner.generatedKeyAlwaysReturned();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getBestRowIdentifier(String paramString1, String paramString2, String paramString3, int paramInt, boolean paramBoolean) throws SQLException {
/* 119 */     return this.inner.getBestRowIdentifier(paramString1, paramString2, paramString3, paramInt, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCatalogSeparator() throws SQLException {
/* 124 */     return this.inner.getCatalogSeparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCatalogTerm() throws SQLException {
/* 129 */     return this.inner.getCatalogTerm();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getClientInfoProperties() throws SQLException {
/* 134 */     return this.inner.getClientInfoProperties();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getColumnPrivileges(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 139 */     return this.inner.getColumnPrivileges(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 144 */     return this.inner.getColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 149 */     return this.inner.getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getCrossReference(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws SQLException {
/* 154 */     return this.inner.getCrossReference(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDatabaseMajorVersion() throws SQLException {
/* 159 */     return this.inner.getDatabaseMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDatabaseMinorVersion() throws SQLException {
/* 164 */     return this.inner.getDatabaseMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDatabaseProductName() throws SQLException {
/* 169 */     return this.inner.getDatabaseProductName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDatabaseProductVersion() throws SQLException {
/* 174 */     return this.inner.getDatabaseProductVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDefaultTransactionIsolation() throws SQLException {
/* 179 */     return this.inner.getDefaultTransactionIsolation();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDriverMajorVersion() {
/* 184 */     return this.inner.getDriverMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDriverMinorVersion() {
/* 189 */     return this.inner.getDriverMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDriverName() throws SQLException {
/* 194 */     return this.inner.getDriverName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDriverVersion() throws SQLException {
/* 199 */     return this.inner.getDriverVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getExportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 204 */     return this.inner.getExportedKeys(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getExtraNameCharacters() throws SQLException {
/* 209 */     return this.inner.getExtraNameCharacters();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getFunctionColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 214 */     return this.inner.getFunctionColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getFunctions(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 219 */     return this.inner.getFunctions(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIdentifierQuoteString() throws SQLException {
/* 224 */     return this.inner.getIdentifierQuoteString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getImportedKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 229 */     return this.inner.getImportedKeys(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getIndexInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2) throws SQLException {
/* 234 */     return this.inner.getIndexInfo(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getJDBCMajorVersion() throws SQLException {
/* 239 */     return this.inner.getJDBCMajorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getJDBCMinorVersion() throws SQLException {
/* 244 */     return this.inner.getJDBCMinorVersion();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxBinaryLiteralLength() throws SQLException {
/* 249 */     return this.inner.getMaxBinaryLiteralLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxCatalogNameLength() throws SQLException {
/* 254 */     return this.inner.getMaxCatalogNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxCharLiteralLength() throws SQLException {
/* 259 */     return this.inner.getMaxCharLiteralLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxColumnNameLength() throws SQLException {
/* 264 */     return this.inner.getMaxColumnNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxColumnsInGroupBy() throws SQLException {
/* 269 */     return this.inner.getMaxColumnsInGroupBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxColumnsInIndex() throws SQLException {
/* 274 */     return this.inner.getMaxColumnsInIndex();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxColumnsInOrderBy() throws SQLException {
/* 279 */     return this.inner.getMaxColumnsInOrderBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxColumnsInSelect() throws SQLException {
/* 284 */     return this.inner.getMaxColumnsInSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxColumnsInTable() throws SQLException {
/* 289 */     return this.inner.getMaxColumnsInTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxConnections() throws SQLException {
/* 294 */     return this.inner.getMaxConnections();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxCursorNameLength() throws SQLException {
/* 299 */     return this.inner.getMaxCursorNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxIndexLength() throws SQLException {
/* 304 */     return this.inner.getMaxIndexLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxProcedureNameLength() throws SQLException {
/* 309 */     return this.inner.getMaxProcedureNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxRowSize() throws SQLException {
/* 314 */     return this.inner.getMaxRowSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSchemaNameLength() throws SQLException {
/* 319 */     return this.inner.getMaxSchemaNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStatementLength() throws SQLException {
/* 324 */     return this.inner.getMaxStatementLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxStatements() throws SQLException {
/* 329 */     return this.inner.getMaxStatements();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTableNameLength() throws SQLException {
/* 334 */     return this.inner.getMaxTableNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTablesInSelect() throws SQLException {
/* 339 */     return this.inner.getMaxTablesInSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxUserNameLength() throws SQLException {
/* 344 */     return this.inner.getMaxUserNameLength();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNumericFunctions() throws SQLException {
/* 349 */     return this.inner.getNumericFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getPrimaryKeys(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 354 */     return this.inner.getPrimaryKeys(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProcedureColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 359 */     return this.inner.getProcedureColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProcedureTerm() throws SQLException {
/* 364 */     return this.inner.getProcedureTerm();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getProcedures(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 369 */     return this.inner.getProcedures(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getPseudoColumns(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 374 */     return this.inner.getPseudoColumns(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getResultSetHoldability() throws SQLException {
/* 379 */     return this.inner.getResultSetHoldability();
/*     */   }
/*     */ 
/*     */   
/*     */   public RowIdLifetime getRowIdLifetime() throws SQLException {
/* 384 */     return this.inner.getRowIdLifetime();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSQLKeywords() throws SQLException {
/* 389 */     return this.inner.getSQLKeywords();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSQLStateType() throws SQLException {
/* 394 */     return this.inner.getSQLStateType();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSchemaTerm() throws SQLException {
/* 399 */     return this.inner.getSchemaTerm();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSchemas(String paramString1, String paramString2) throws SQLException {
/* 404 */     return this.inner.getSchemas(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSchemas() throws SQLException {
/* 409 */     return this.inner.getSchemas();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSearchStringEscape() throws SQLException {
/* 414 */     return this.inner.getSearchStringEscape();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStringFunctions() throws SQLException {
/* 419 */     return this.inner.getStringFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSuperTables(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 424 */     return this.inner.getSuperTables(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getSuperTypes(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 429 */     return this.inner.getSuperTypes(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSystemFunctions() throws SQLException {
/* 434 */     return this.inner.getSystemFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTablePrivileges(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 439 */     return this.inner.getTablePrivileges(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTableTypes() throws SQLException {
/* 444 */     return this.inner.getTableTypes();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTables(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString) throws SQLException {
/* 449 */     return this.inner.getTables(paramString1, paramString2, paramString3, paramArrayOfString);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTimeDateFunctions() throws SQLException {
/* 454 */     return this.inner.getTimeDateFunctions();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getTypeInfo() throws SQLException {
/* 459 */     return this.inner.getTypeInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getUDTs(String paramString1, String paramString2, String paramString3, int[] paramArrayOfint) throws SQLException {
/* 464 */     return this.inner.getUDTs(paramString1, paramString2, paramString3, paramArrayOfint);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUserName() throws SQLException {
/* 469 */     return this.inner.getUserName();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getVersionColumns(String paramString1, String paramString2, String paramString3) throws SQLException {
/* 474 */     return this.inner.getVersionColumns(paramString1, paramString2, paramString3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean insertsAreDetected(int paramInt) throws SQLException {
/* 479 */     return this.inner.insertsAreDetected(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCatalogAtStart() throws SQLException {
/* 484 */     return this.inner.isCatalogAtStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean locatorsUpdateCopy() throws SQLException {
/* 489 */     return this.inner.locatorsUpdateCopy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nullPlusNonNullIsNull() throws SQLException {
/* 494 */     return this.inner.nullPlusNonNullIsNull();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nullsAreSortedAtEnd() throws SQLException {
/* 499 */     return this.inner.nullsAreSortedAtEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nullsAreSortedAtStart() throws SQLException {
/* 504 */     return this.inner.nullsAreSortedAtStart();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nullsAreSortedHigh() throws SQLException {
/* 509 */     return this.inner.nullsAreSortedHigh();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean nullsAreSortedLow() throws SQLException {
/* 514 */     return this.inner.nullsAreSortedLow();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean othersDeletesAreVisible(int paramInt) throws SQLException {
/* 519 */     return this.inner.othersDeletesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean othersInsertsAreVisible(int paramInt) throws SQLException {
/* 524 */     return this.inner.othersInsertsAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean othersUpdatesAreVisible(int paramInt) throws SQLException {
/* 529 */     return this.inner.othersUpdatesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ownDeletesAreVisible(int paramInt) throws SQLException {
/* 534 */     return this.inner.ownDeletesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ownInsertsAreVisible(int paramInt) throws SQLException {
/* 539 */     return this.inner.ownInsertsAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean ownUpdatesAreVisible(int paramInt) throws SQLException {
/* 544 */     return this.inner.ownUpdatesAreVisible(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean storesLowerCaseIdentifiers() throws SQLException {
/* 549 */     return this.inner.storesLowerCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
/* 554 */     return this.inner.storesLowerCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean storesMixedCaseIdentifiers() throws SQLException {
/* 559 */     return this.inner.storesMixedCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
/* 564 */     return this.inner.storesMixedCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean storesUpperCaseIdentifiers() throws SQLException {
/* 569 */     return this.inner.storesUpperCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
/* 574 */     return this.inner.storesUpperCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsANSI92EntryLevelSQL() throws SQLException {
/* 579 */     return this.inner.supportsANSI92EntryLevelSQL();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsANSI92FullSQL() throws SQLException {
/* 584 */     return this.inner.supportsANSI92FullSQL();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsANSI92IntermediateSQL() throws SQLException {
/* 589 */     return this.inner.supportsANSI92IntermediateSQL();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsAlterTableWithAddColumn() throws SQLException {
/* 594 */     return this.inner.supportsAlterTableWithAddColumn();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsAlterTableWithDropColumn() throws SQLException {
/* 599 */     return this.inner.supportsAlterTableWithDropColumn();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsBatchUpdates() throws SQLException {
/* 604 */     return this.inner.supportsBatchUpdates();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCatalogsInDataManipulation() throws SQLException {
/* 609 */     return this.inner.supportsCatalogsInDataManipulation();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
/* 614 */     return this.inner.supportsCatalogsInIndexDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
/* 619 */     return this.inner.supportsCatalogsInPrivilegeDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCatalogsInProcedureCalls() throws SQLException {
/* 624 */     return this.inner.supportsCatalogsInProcedureCalls();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCatalogsInTableDefinitions() throws SQLException {
/* 629 */     return this.inner.supportsCatalogsInTableDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsColumnAliasing() throws SQLException {
/* 634 */     return this.inner.supportsColumnAliasing();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsConvert(int paramInt1, int paramInt2) throws SQLException {
/* 639 */     return this.inner.supportsConvert(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsConvert() throws SQLException {
/* 644 */     return this.inner.supportsConvert();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCoreSQLGrammar() throws SQLException {
/* 649 */     return this.inner.supportsCoreSQLGrammar();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsCorrelatedSubqueries() throws SQLException {
/* 654 */     return this.inner.supportsCorrelatedSubqueries();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
/* 659 */     return this.inner.supportsDataDefinitionAndDataManipulationTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
/* 664 */     return this.inner.supportsDataManipulationTransactionsOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsDifferentTableCorrelationNames() throws SQLException {
/* 669 */     return this.inner.supportsDifferentTableCorrelationNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsExpressionsInOrderBy() throws SQLException {
/* 674 */     return this.inner.supportsExpressionsInOrderBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsExtendedSQLGrammar() throws SQLException {
/* 679 */     return this.inner.supportsExtendedSQLGrammar();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsFullOuterJoins() throws SQLException {
/* 684 */     return this.inner.supportsFullOuterJoins();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsGetGeneratedKeys() throws SQLException {
/* 689 */     return this.inner.supportsGetGeneratedKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsGroupBy() throws SQLException {
/* 694 */     return this.inner.supportsGroupBy();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsGroupByBeyondSelect() throws SQLException {
/* 699 */     return this.inner.supportsGroupByBeyondSelect();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsGroupByUnrelated() throws SQLException {
/* 704 */     return this.inner.supportsGroupByUnrelated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsIntegrityEnhancementFacility() throws SQLException {
/* 709 */     return this.inner.supportsIntegrityEnhancementFacility();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsLikeEscapeClause() throws SQLException {
/* 714 */     return this.inner.supportsLikeEscapeClause();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsLimitedOuterJoins() throws SQLException {
/* 719 */     return this.inner.supportsLimitedOuterJoins();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsMinimumSQLGrammar() throws SQLException {
/* 724 */     return this.inner.supportsMinimumSQLGrammar();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsMixedCaseIdentifiers() throws SQLException {
/* 729 */     return this.inner.supportsMixedCaseIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
/* 734 */     return this.inner.supportsMixedCaseQuotedIdentifiers();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsMultipleOpenResults() throws SQLException {
/* 739 */     return this.inner.supportsMultipleOpenResults();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsMultipleResultSets() throws SQLException {
/* 744 */     return this.inner.supportsMultipleResultSets();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsMultipleTransactions() throws SQLException {
/* 749 */     return this.inner.supportsMultipleTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsNamedParameters() throws SQLException {
/* 754 */     return this.inner.supportsNamedParameters();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsNonNullableColumns() throws SQLException {
/* 759 */     return this.inner.supportsNonNullableColumns();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
/* 764 */     return this.inner.supportsOpenCursorsAcrossCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
/* 769 */     return this.inner.supportsOpenCursorsAcrossRollback();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
/* 774 */     return this.inner.supportsOpenStatementsAcrossCommit();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
/* 779 */     return this.inner.supportsOpenStatementsAcrossRollback();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsOrderByUnrelated() throws SQLException {
/* 784 */     return this.inner.supportsOrderByUnrelated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsOuterJoins() throws SQLException {
/* 789 */     return this.inner.supportsOuterJoins();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsPositionedDelete() throws SQLException {
/* 794 */     return this.inner.supportsPositionedDelete();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsPositionedUpdate() throws SQLException {
/* 799 */     return this.inner.supportsPositionedUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsResultSetConcurrency(int paramInt1, int paramInt2) throws SQLException {
/* 804 */     return this.inner.supportsResultSetConcurrency(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsResultSetHoldability(int paramInt) throws SQLException {
/* 809 */     return this.inner.supportsResultSetHoldability(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsResultSetType(int paramInt) throws SQLException {
/* 814 */     return this.inner.supportsResultSetType(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSavepoints() throws SQLException {
/* 819 */     return this.inner.supportsSavepoints();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSchemasInDataManipulation() throws SQLException {
/* 824 */     return this.inner.supportsSchemasInDataManipulation();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSchemasInIndexDefinitions() throws SQLException {
/* 829 */     return this.inner.supportsSchemasInIndexDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
/* 834 */     return this.inner.supportsSchemasInPrivilegeDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSchemasInProcedureCalls() throws SQLException {
/* 839 */     return this.inner.supportsSchemasInProcedureCalls();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSchemasInTableDefinitions() throws SQLException {
/* 844 */     return this.inner.supportsSchemasInTableDefinitions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSelectForUpdate() throws SQLException {
/* 849 */     return this.inner.supportsSelectForUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsStatementPooling() throws SQLException {
/* 854 */     return this.inner.supportsStatementPooling();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 859 */     return this.inner.supportsStoredFunctionsUsingCallSyntax();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsStoredProcedures() throws SQLException {
/* 864 */     return this.inner.supportsStoredProcedures();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSubqueriesInComparisons() throws SQLException {
/* 869 */     return this.inner.supportsSubqueriesInComparisons();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSubqueriesInExists() throws SQLException {
/* 874 */     return this.inner.supportsSubqueriesInExists();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSubqueriesInIns() throws SQLException {
/* 879 */     return this.inner.supportsSubqueriesInIns();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsSubqueriesInQuantifieds() throws SQLException {
/* 884 */     return this.inner.supportsSubqueriesInQuantifieds();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsTableCorrelationNames() throws SQLException {
/* 889 */     return this.inner.supportsTableCorrelationNames();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsTransactionIsolationLevel(int paramInt) throws SQLException {
/* 894 */     return this.inner.supportsTransactionIsolationLevel(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsTransactions() throws SQLException {
/* 899 */     return this.inner.supportsTransactions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsUnion() throws SQLException {
/* 904 */     return this.inner.supportsUnion();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportsUnionAll() throws SQLException {
/* 909 */     return this.inner.supportsUnionAll();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean updatesAreDetected(int paramInt) throws SQLException {
/* 914 */     return this.inner.updatesAreDetected(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesLocalFilePerTable() throws SQLException {
/* 919 */     return this.inner.usesLocalFilePerTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean usesLocalFiles() throws SQLException {
/* 924 */     return this.inner.usesLocalFiles();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getURL() throws SQLException {
/* 929 */     return this.inner.getURL();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly() throws SQLException {
/* 934 */     return this.inner.isReadOnly();
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSet getAttributes(String paramString1, String paramString2, String paramString3, String paramString4) throws SQLException {
/* 939 */     return this.inner.getAttributes(paramString1, paramString2, paramString3, paramString4);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 944 */     return this.inner.isWrapperFor(paramClass);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object unwrap(Class<?> paramClass) throws SQLException {
/* 949 */     return this.inner.unwrap(paramClass);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/FilterDatabaseMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */