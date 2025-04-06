/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.RowIdLifetime;
/*      */ import java.sql.SQLException;
/*      */ import javax.sql.ConnectionEvent;
/*      */ import javax.sql.ConnectionEventListener;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class NewProxyDatabaseMetaData
/*      */   implements DatabaseMetaData
/*      */ {
/*      */   protected DatabaseMetaData inner;
/*      */   
/*      */   private void __setInner(DatabaseMetaData inner) {
/*   32 */     this.inner = inner;
/*      */   }
/*      */   
/*      */   NewProxyDatabaseMetaData(DatabaseMetaData inner) {
/*   36 */     __setInner(inner);
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getURL() throws SQLException {
/*      */     try {
/*   42 */       return this.inner.getURL();
/*      */     }
/*   44 */     catch (NullPointerException exc) {
/*      */       
/*   46 */       if (isDetached())
/*      */       {
/*   48 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*   50 */       throw exc;
/*      */     }
/*   52 */     catch (Exception exc) {
/*      */       
/*   54 */       if (!isDetached())
/*      */       {
/*   56 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*   58 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Connection getConnection() throws SQLException {
/*      */     try {
/*   66 */       return this.proxyCon;
/*      */     }
/*   68 */     catch (NullPointerException exc) {
/*      */       
/*   70 */       if (isDetached())
/*      */       {
/*   72 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*   74 */       throw exc;
/*      */     }
/*   76 */     catch (Exception exc) {
/*      */       
/*   78 */       if (!isDetached())
/*      */       {
/*   80 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*   82 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isReadOnly() throws SQLException {
/*      */     try {
/*   90 */       return this.inner.isReadOnly();
/*      */     }
/*   92 */     catch (NullPointerException exc) {
/*      */       
/*   94 */       if (isDetached())
/*      */       {
/*   96 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*   98 */       throw exc;
/*      */     }
/*  100 */     catch (Exception exc) {
/*      */       
/*  102 */       if (!isDetached())
/*      */       {
/*  104 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  106 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getAttributes(String a, String b, String c, String d) throws SQLException {
/*      */     try {
/*  114 */       ResultSet innerResultSet = this.inner.getAttributes(a, b, c, d);
/*  115 */       if (innerResultSet == null) return null; 
/*  116 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/*  118 */     catch (NullPointerException exc) {
/*      */       
/*  120 */       if (isDetached())
/*      */       {
/*  122 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  124 */       throw exc;
/*      */     }
/*  126 */     catch (Exception exc) {
/*      */       
/*  128 */       if (!isDetached())
/*      */       {
/*  130 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  132 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetHoldability() throws SQLException {
/*      */     try {
/*  140 */       return this.inner.getResultSetHoldability();
/*      */     }
/*  142 */     catch (NullPointerException exc) {
/*      */       
/*  144 */       if (isDetached())
/*      */       {
/*  146 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  148 */       throw exc;
/*      */     }
/*  150 */     catch (Exception exc) {
/*      */       
/*  152 */       if (!isDetached())
/*      */       {
/*  154 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  156 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean allProceduresAreCallable() throws SQLException {
/*      */     try {
/*  164 */       return this.inner.allProceduresAreCallable();
/*      */     }
/*  166 */     catch (NullPointerException exc) {
/*      */       
/*  168 */       if (isDetached())
/*      */       {
/*  170 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  172 */       throw exc;
/*      */     }
/*  174 */     catch (Exception exc) {
/*      */       
/*  176 */       if (!isDetached())
/*      */       {
/*  178 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  180 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean allTablesAreSelectable() throws SQLException {
/*      */     try {
/*  188 */       return this.inner.allTablesAreSelectable();
/*      */     }
/*  190 */     catch (NullPointerException exc) {
/*      */       
/*  192 */       if (isDetached())
/*      */       {
/*  194 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  196 */       throw exc;
/*      */     }
/*  198 */     catch (Exception exc) {
/*      */       
/*  200 */       if (!isDetached())
/*      */       {
/*  202 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  204 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getUserName() throws SQLException {
/*      */     try {
/*  212 */       return this.inner.getUserName();
/*      */     }
/*  214 */     catch (NullPointerException exc) {
/*      */       
/*  216 */       if (isDetached())
/*      */       {
/*  218 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  220 */       throw exc;
/*      */     }
/*  222 */     catch (Exception exc) {
/*      */       
/*  224 */       if (!isDetached())
/*      */       {
/*  226 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  228 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean nullsAreSortedHigh() throws SQLException {
/*      */     try {
/*  236 */       return this.inner.nullsAreSortedHigh();
/*      */     }
/*  238 */     catch (NullPointerException exc) {
/*      */       
/*  240 */       if (isDetached())
/*      */       {
/*  242 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  244 */       throw exc;
/*      */     }
/*  246 */     catch (Exception exc) {
/*      */       
/*  248 */       if (!isDetached())
/*      */       {
/*  250 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  252 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean nullsAreSortedLow() throws SQLException {
/*      */     try {
/*  260 */       return this.inner.nullsAreSortedLow();
/*      */     }
/*  262 */     catch (NullPointerException exc) {
/*      */       
/*  264 */       if (isDetached())
/*      */       {
/*  266 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  268 */       throw exc;
/*      */     }
/*  270 */     catch (Exception exc) {
/*      */       
/*  272 */       if (!isDetached())
/*      */       {
/*  274 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  276 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean nullsAreSortedAtStart() throws SQLException {
/*      */     try {
/*  284 */       return this.inner.nullsAreSortedAtStart();
/*      */     }
/*  286 */     catch (NullPointerException exc) {
/*      */       
/*  288 */       if (isDetached())
/*      */       {
/*  290 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  292 */       throw exc;
/*      */     }
/*  294 */     catch (Exception exc) {
/*      */       
/*  296 */       if (!isDetached())
/*      */       {
/*  298 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  300 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean nullsAreSortedAtEnd() throws SQLException {
/*      */     try {
/*  308 */       return this.inner.nullsAreSortedAtEnd();
/*      */     }
/*  310 */     catch (NullPointerException exc) {
/*      */       
/*  312 */       if (isDetached())
/*      */       {
/*  314 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  316 */       throw exc;
/*      */     }
/*  318 */     catch (Exception exc) {
/*      */       
/*  320 */       if (!isDetached())
/*      */       {
/*  322 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  324 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getDatabaseProductName() throws SQLException {
/*      */     try {
/*  332 */       return this.inner.getDatabaseProductName();
/*      */     }
/*  334 */     catch (NullPointerException exc) {
/*      */       
/*  336 */       if (isDetached())
/*      */       {
/*  338 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  340 */       throw exc;
/*      */     }
/*  342 */     catch (Exception exc) {
/*      */       
/*  344 */       if (!isDetached())
/*      */       {
/*  346 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  348 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getDatabaseProductVersion() throws SQLException {
/*      */     try {
/*  356 */       return this.inner.getDatabaseProductVersion();
/*      */     }
/*  358 */     catch (NullPointerException exc) {
/*      */       
/*  360 */       if (isDetached())
/*      */       {
/*  362 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  364 */       throw exc;
/*      */     }
/*  366 */     catch (Exception exc) {
/*      */       
/*  368 */       if (!isDetached())
/*      */       {
/*  370 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  372 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getDriverName() throws SQLException {
/*      */     try {
/*  380 */       return this.inner.getDriverName();
/*      */     }
/*  382 */     catch (NullPointerException exc) {
/*      */       
/*  384 */       if (isDetached())
/*      */       {
/*  386 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  388 */       throw exc;
/*      */     }
/*  390 */     catch (Exception exc) {
/*      */       
/*  392 */       if (!isDetached())
/*      */       {
/*  394 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  396 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getDriverVersion() throws SQLException {
/*      */     try {
/*  404 */       return this.inner.getDriverVersion();
/*      */     }
/*  406 */     catch (NullPointerException exc) {
/*      */       
/*  408 */       if (isDetached())
/*      */       {
/*  410 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  412 */       throw exc;
/*      */     }
/*  414 */     catch (Exception exc) {
/*      */       
/*  416 */       if (!isDetached())
/*      */       {
/*  418 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  420 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDriverMajorVersion() {
/*  426 */     return this.inner.getDriverMajorVersion();
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDriverMinorVersion() {
/*  431 */     return this.inner.getDriverMinorVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean usesLocalFiles() throws SQLException {
/*      */     try {
/*  438 */       return this.inner.usesLocalFiles();
/*      */     }
/*  440 */     catch (NullPointerException exc) {
/*      */       
/*  442 */       if (isDetached())
/*      */       {
/*  444 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  446 */       throw exc;
/*      */     }
/*  448 */     catch (Exception exc) {
/*      */       
/*  450 */       if (!isDetached())
/*      */       {
/*  452 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  454 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean usesLocalFilePerTable() throws SQLException {
/*      */     try {
/*  462 */       return this.inner.usesLocalFilePerTable();
/*      */     }
/*  464 */     catch (NullPointerException exc) {
/*      */       
/*  466 */       if (isDetached())
/*      */       {
/*  468 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  470 */       throw exc;
/*      */     }
/*  472 */     catch (Exception exc) {
/*      */       
/*  474 */       if (!isDetached())
/*      */       {
/*  476 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  478 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsMixedCaseIdentifiers() throws SQLException {
/*      */     try {
/*  486 */       return this.inner.supportsMixedCaseIdentifiers();
/*      */     }
/*  488 */     catch (NullPointerException exc) {
/*      */       
/*  490 */       if (isDetached())
/*      */       {
/*  492 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  494 */       throw exc;
/*      */     }
/*  496 */     catch (Exception exc) {
/*      */       
/*  498 */       if (!isDetached())
/*      */       {
/*  500 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  502 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean storesUpperCaseIdentifiers() throws SQLException {
/*      */     try {
/*  510 */       return this.inner.storesUpperCaseIdentifiers();
/*      */     }
/*  512 */     catch (NullPointerException exc) {
/*      */       
/*  514 */       if (isDetached())
/*      */       {
/*  516 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  518 */       throw exc;
/*      */     }
/*  520 */     catch (Exception exc) {
/*      */       
/*  522 */       if (!isDetached())
/*      */       {
/*  524 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  526 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean storesLowerCaseIdentifiers() throws SQLException {
/*      */     try {
/*  534 */       return this.inner.storesLowerCaseIdentifiers();
/*      */     }
/*  536 */     catch (NullPointerException exc) {
/*      */       
/*  538 */       if (isDetached())
/*      */       {
/*  540 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  542 */       throw exc;
/*      */     }
/*  544 */     catch (Exception exc) {
/*      */       
/*  546 */       if (!isDetached())
/*      */       {
/*  548 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  550 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean storesMixedCaseIdentifiers() throws SQLException {
/*      */     try {
/*  558 */       return this.inner.storesMixedCaseIdentifiers();
/*      */     }
/*  560 */     catch (NullPointerException exc) {
/*      */       
/*  562 */       if (isDetached())
/*      */       {
/*  564 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  566 */       throw exc;
/*      */     }
/*  568 */     catch (Exception exc) {
/*      */       
/*  570 */       if (!isDetached())
/*      */       {
/*  572 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  574 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
/*      */     try {
/*  582 */       return this.inner.supportsMixedCaseQuotedIdentifiers();
/*      */     }
/*  584 */     catch (NullPointerException exc) {
/*      */       
/*  586 */       if (isDetached())
/*      */       {
/*  588 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  590 */       throw exc;
/*      */     }
/*  592 */     catch (Exception exc) {
/*      */       
/*  594 */       if (!isDetached())
/*      */       {
/*  596 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  598 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
/*      */     try {
/*  606 */       return this.inner.storesUpperCaseQuotedIdentifiers();
/*      */     }
/*  608 */     catch (NullPointerException exc) {
/*      */       
/*  610 */       if (isDetached())
/*      */       {
/*  612 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  614 */       throw exc;
/*      */     }
/*  616 */     catch (Exception exc) {
/*      */       
/*  618 */       if (!isDetached())
/*      */       {
/*  620 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  622 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
/*      */     try {
/*  630 */       return this.inner.storesLowerCaseQuotedIdentifiers();
/*      */     }
/*  632 */     catch (NullPointerException exc) {
/*      */       
/*  634 */       if (isDetached())
/*      */       {
/*  636 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  638 */       throw exc;
/*      */     }
/*  640 */     catch (Exception exc) {
/*      */       
/*  642 */       if (!isDetached())
/*      */       {
/*  644 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  646 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
/*      */     try {
/*  654 */       return this.inner.storesMixedCaseQuotedIdentifiers();
/*      */     }
/*  656 */     catch (NullPointerException exc) {
/*      */       
/*  658 */       if (isDetached())
/*      */       {
/*  660 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  662 */       throw exc;
/*      */     }
/*  664 */     catch (Exception exc) {
/*      */       
/*  666 */       if (!isDetached())
/*      */       {
/*  668 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  670 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getIdentifierQuoteString() throws SQLException {
/*      */     try {
/*  678 */       return this.inner.getIdentifierQuoteString();
/*      */     }
/*  680 */     catch (NullPointerException exc) {
/*      */       
/*  682 */       if (isDetached())
/*      */       {
/*  684 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  686 */       throw exc;
/*      */     }
/*  688 */     catch (Exception exc) {
/*      */       
/*  690 */       if (!isDetached())
/*      */       {
/*  692 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  694 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getSQLKeywords() throws SQLException {
/*      */     try {
/*  702 */       return this.inner.getSQLKeywords();
/*      */     }
/*  704 */     catch (NullPointerException exc) {
/*      */       
/*  706 */       if (isDetached())
/*      */       {
/*  708 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  710 */       throw exc;
/*      */     }
/*  712 */     catch (Exception exc) {
/*      */       
/*  714 */       if (!isDetached())
/*      */       {
/*  716 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  718 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNumericFunctions() throws SQLException {
/*      */     try {
/*  726 */       return this.inner.getNumericFunctions();
/*      */     }
/*  728 */     catch (NullPointerException exc) {
/*      */       
/*  730 */       if (isDetached())
/*      */       {
/*  732 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  734 */       throw exc;
/*      */     }
/*  736 */     catch (Exception exc) {
/*      */       
/*  738 */       if (!isDetached())
/*      */       {
/*  740 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  742 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getStringFunctions() throws SQLException {
/*      */     try {
/*  750 */       return this.inner.getStringFunctions();
/*      */     }
/*  752 */     catch (NullPointerException exc) {
/*      */       
/*  754 */       if (isDetached())
/*      */       {
/*  756 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  758 */       throw exc;
/*      */     }
/*  760 */     catch (Exception exc) {
/*      */       
/*  762 */       if (!isDetached())
/*      */       {
/*  764 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  766 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getSystemFunctions() throws SQLException {
/*      */     try {
/*  774 */       return this.inner.getSystemFunctions();
/*      */     }
/*  776 */     catch (NullPointerException exc) {
/*      */       
/*  778 */       if (isDetached())
/*      */       {
/*  780 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  782 */       throw exc;
/*      */     }
/*  784 */     catch (Exception exc) {
/*      */       
/*  786 */       if (!isDetached())
/*      */       {
/*  788 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  790 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getTimeDateFunctions() throws SQLException {
/*      */     try {
/*  798 */       return this.inner.getTimeDateFunctions();
/*      */     }
/*  800 */     catch (NullPointerException exc) {
/*      */       
/*  802 */       if (isDetached())
/*      */       {
/*  804 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  806 */       throw exc;
/*      */     }
/*  808 */     catch (Exception exc) {
/*      */       
/*  810 */       if (!isDetached())
/*      */       {
/*  812 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  814 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getSearchStringEscape() throws SQLException {
/*      */     try {
/*  822 */       return this.inner.getSearchStringEscape();
/*      */     }
/*  824 */     catch (NullPointerException exc) {
/*      */       
/*  826 */       if (isDetached())
/*      */       {
/*  828 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  830 */       throw exc;
/*      */     }
/*  832 */     catch (Exception exc) {
/*      */       
/*  834 */       if (!isDetached())
/*      */       {
/*  836 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  838 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getExtraNameCharacters() throws SQLException {
/*      */     try {
/*  846 */       return this.inner.getExtraNameCharacters();
/*      */     }
/*  848 */     catch (NullPointerException exc) {
/*      */       
/*  850 */       if (isDetached())
/*      */       {
/*  852 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  854 */       throw exc;
/*      */     }
/*  856 */     catch (Exception exc) {
/*      */       
/*  858 */       if (!isDetached())
/*      */       {
/*  860 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  862 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsAlterTableWithAddColumn() throws SQLException {
/*      */     try {
/*  870 */       return this.inner.supportsAlterTableWithAddColumn();
/*      */     }
/*  872 */     catch (NullPointerException exc) {
/*      */       
/*  874 */       if (isDetached())
/*      */       {
/*  876 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  878 */       throw exc;
/*      */     }
/*  880 */     catch (Exception exc) {
/*      */       
/*  882 */       if (!isDetached())
/*      */       {
/*  884 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  886 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsAlterTableWithDropColumn() throws SQLException {
/*      */     try {
/*  894 */       return this.inner.supportsAlterTableWithDropColumn();
/*      */     }
/*  896 */     catch (NullPointerException exc) {
/*      */       
/*  898 */       if (isDetached())
/*      */       {
/*  900 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  902 */       throw exc;
/*      */     }
/*  904 */     catch (Exception exc) {
/*      */       
/*  906 */       if (!isDetached())
/*      */       {
/*  908 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  910 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsColumnAliasing() throws SQLException {
/*      */     try {
/*  918 */       return this.inner.supportsColumnAliasing();
/*      */     }
/*  920 */     catch (NullPointerException exc) {
/*      */       
/*  922 */       if (isDetached())
/*      */       {
/*  924 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  926 */       throw exc;
/*      */     }
/*  928 */     catch (Exception exc) {
/*      */       
/*  930 */       if (!isDetached())
/*      */       {
/*  932 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  934 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean nullPlusNonNullIsNull() throws SQLException {
/*      */     try {
/*  942 */       return this.inner.nullPlusNonNullIsNull();
/*      */     }
/*  944 */     catch (NullPointerException exc) {
/*      */       
/*  946 */       if (isDetached())
/*      */       {
/*  948 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  950 */       throw exc;
/*      */     }
/*  952 */     catch (Exception exc) {
/*      */       
/*  954 */       if (!isDetached())
/*      */       {
/*  956 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  958 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsConvert() throws SQLException {
/*      */     try {
/*  966 */       return this.inner.supportsConvert();
/*      */     }
/*  968 */     catch (NullPointerException exc) {
/*      */       
/*  970 */       if (isDetached())
/*      */       {
/*  972 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  974 */       throw exc;
/*      */     }
/*  976 */     catch (Exception exc) {
/*      */       
/*  978 */       if (!isDetached())
/*      */       {
/*  980 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  982 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsConvert(int a, int b) throws SQLException {
/*      */     try {
/*  990 */       return this.inner.supportsConvert(a, b);
/*      */     }
/*  992 */     catch (NullPointerException exc) {
/*      */       
/*  994 */       if (isDetached())
/*      */       {
/*  996 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/*  998 */       throw exc;
/*      */     }
/* 1000 */     catch (Exception exc) {
/*      */       
/* 1002 */       if (!isDetached())
/*      */       {
/* 1004 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1006 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsTableCorrelationNames() throws SQLException {
/*      */     try {
/* 1014 */       return this.inner.supportsTableCorrelationNames();
/*      */     }
/* 1016 */     catch (NullPointerException exc) {
/*      */       
/* 1018 */       if (isDetached())
/*      */       {
/* 1020 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1022 */       throw exc;
/*      */     }
/* 1024 */     catch (Exception exc) {
/*      */       
/* 1026 */       if (!isDetached())
/*      */       {
/* 1028 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1030 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsDifferentTableCorrelationNames() throws SQLException {
/*      */     try {
/* 1038 */       return this.inner.supportsDifferentTableCorrelationNames();
/*      */     }
/* 1040 */     catch (NullPointerException exc) {
/*      */       
/* 1042 */       if (isDetached())
/*      */       {
/* 1044 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1046 */       throw exc;
/*      */     }
/* 1048 */     catch (Exception exc) {
/*      */       
/* 1050 */       if (!isDetached())
/*      */       {
/* 1052 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1054 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsExpressionsInOrderBy() throws SQLException {
/*      */     try {
/* 1062 */       return this.inner.supportsExpressionsInOrderBy();
/*      */     }
/* 1064 */     catch (NullPointerException exc) {
/*      */       
/* 1066 */       if (isDetached())
/*      */       {
/* 1068 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1070 */       throw exc;
/*      */     }
/* 1072 */     catch (Exception exc) {
/*      */       
/* 1074 */       if (!isDetached())
/*      */       {
/* 1076 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1078 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsOrderByUnrelated() throws SQLException {
/*      */     try {
/* 1086 */       return this.inner.supportsOrderByUnrelated();
/*      */     }
/* 1088 */     catch (NullPointerException exc) {
/*      */       
/* 1090 */       if (isDetached())
/*      */       {
/* 1092 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1094 */       throw exc;
/*      */     }
/* 1096 */     catch (Exception exc) {
/*      */       
/* 1098 */       if (!isDetached())
/*      */       {
/* 1100 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1102 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsGroupBy() throws SQLException {
/*      */     try {
/* 1110 */       return this.inner.supportsGroupBy();
/*      */     }
/* 1112 */     catch (NullPointerException exc) {
/*      */       
/* 1114 */       if (isDetached())
/*      */       {
/* 1116 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1118 */       throw exc;
/*      */     }
/* 1120 */     catch (Exception exc) {
/*      */       
/* 1122 */       if (!isDetached())
/*      */       {
/* 1124 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1126 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsGroupByUnrelated() throws SQLException {
/*      */     try {
/* 1134 */       return this.inner.supportsGroupByUnrelated();
/*      */     }
/* 1136 */     catch (NullPointerException exc) {
/*      */       
/* 1138 */       if (isDetached())
/*      */       {
/* 1140 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1142 */       throw exc;
/*      */     }
/* 1144 */     catch (Exception exc) {
/*      */       
/* 1146 */       if (!isDetached())
/*      */       {
/* 1148 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1150 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsGroupByBeyondSelect() throws SQLException {
/*      */     try {
/* 1158 */       return this.inner.supportsGroupByBeyondSelect();
/*      */     }
/* 1160 */     catch (NullPointerException exc) {
/*      */       
/* 1162 */       if (isDetached())
/*      */       {
/* 1164 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1166 */       throw exc;
/*      */     }
/* 1168 */     catch (Exception exc) {
/*      */       
/* 1170 */       if (!isDetached())
/*      */       {
/* 1172 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1174 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsLikeEscapeClause() throws SQLException {
/*      */     try {
/* 1182 */       return this.inner.supportsLikeEscapeClause();
/*      */     }
/* 1184 */     catch (NullPointerException exc) {
/*      */       
/* 1186 */       if (isDetached())
/*      */       {
/* 1188 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1190 */       throw exc;
/*      */     }
/* 1192 */     catch (Exception exc) {
/*      */       
/* 1194 */       if (!isDetached())
/*      */       {
/* 1196 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1198 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsMultipleResultSets() throws SQLException {
/*      */     try {
/* 1206 */       return this.inner.supportsMultipleResultSets();
/*      */     }
/* 1208 */     catch (NullPointerException exc) {
/*      */       
/* 1210 */       if (isDetached())
/*      */       {
/* 1212 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1214 */       throw exc;
/*      */     }
/* 1216 */     catch (Exception exc) {
/*      */       
/* 1218 */       if (!isDetached())
/*      */       {
/* 1220 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1222 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsMultipleTransactions() throws SQLException {
/*      */     try {
/* 1230 */       return this.inner.supportsMultipleTransactions();
/*      */     }
/* 1232 */     catch (NullPointerException exc) {
/*      */       
/* 1234 */       if (isDetached())
/*      */       {
/* 1236 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1238 */       throw exc;
/*      */     }
/* 1240 */     catch (Exception exc) {
/*      */       
/* 1242 */       if (!isDetached())
/*      */       {
/* 1244 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1246 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsNonNullableColumns() throws SQLException {
/*      */     try {
/* 1254 */       return this.inner.supportsNonNullableColumns();
/*      */     }
/* 1256 */     catch (NullPointerException exc) {
/*      */       
/* 1258 */       if (isDetached())
/*      */       {
/* 1260 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1262 */       throw exc;
/*      */     }
/* 1264 */     catch (Exception exc) {
/*      */       
/* 1266 */       if (!isDetached())
/*      */       {
/* 1268 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1270 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsMinimumSQLGrammar() throws SQLException {
/*      */     try {
/* 1278 */       return this.inner.supportsMinimumSQLGrammar();
/*      */     }
/* 1280 */     catch (NullPointerException exc) {
/*      */       
/* 1282 */       if (isDetached())
/*      */       {
/* 1284 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1286 */       throw exc;
/*      */     }
/* 1288 */     catch (Exception exc) {
/*      */       
/* 1290 */       if (!isDetached())
/*      */       {
/* 1292 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1294 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCoreSQLGrammar() throws SQLException {
/*      */     try {
/* 1302 */       return this.inner.supportsCoreSQLGrammar();
/*      */     }
/* 1304 */     catch (NullPointerException exc) {
/*      */       
/* 1306 */       if (isDetached())
/*      */       {
/* 1308 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1310 */       throw exc;
/*      */     }
/* 1312 */     catch (Exception exc) {
/*      */       
/* 1314 */       if (!isDetached())
/*      */       {
/* 1316 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1318 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsExtendedSQLGrammar() throws SQLException {
/*      */     try {
/* 1326 */       return this.inner.supportsExtendedSQLGrammar();
/*      */     }
/* 1328 */     catch (NullPointerException exc) {
/*      */       
/* 1330 */       if (isDetached())
/*      */       {
/* 1332 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1334 */       throw exc;
/*      */     }
/* 1336 */     catch (Exception exc) {
/*      */       
/* 1338 */       if (!isDetached())
/*      */       {
/* 1340 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1342 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsANSI92EntryLevelSQL() throws SQLException {
/*      */     try {
/* 1350 */       return this.inner.supportsANSI92EntryLevelSQL();
/*      */     }
/* 1352 */     catch (NullPointerException exc) {
/*      */       
/* 1354 */       if (isDetached())
/*      */       {
/* 1356 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1358 */       throw exc;
/*      */     }
/* 1360 */     catch (Exception exc) {
/*      */       
/* 1362 */       if (!isDetached())
/*      */       {
/* 1364 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1366 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsANSI92IntermediateSQL() throws SQLException {
/*      */     try {
/* 1374 */       return this.inner.supportsANSI92IntermediateSQL();
/*      */     }
/* 1376 */     catch (NullPointerException exc) {
/*      */       
/* 1378 */       if (isDetached())
/*      */       {
/* 1380 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1382 */       throw exc;
/*      */     }
/* 1384 */     catch (Exception exc) {
/*      */       
/* 1386 */       if (!isDetached())
/*      */       {
/* 1388 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1390 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsANSI92FullSQL() throws SQLException {
/*      */     try {
/* 1398 */       return this.inner.supportsANSI92FullSQL();
/*      */     }
/* 1400 */     catch (NullPointerException exc) {
/*      */       
/* 1402 */       if (isDetached())
/*      */       {
/* 1404 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1406 */       throw exc;
/*      */     }
/* 1408 */     catch (Exception exc) {
/*      */       
/* 1410 */       if (!isDetached())
/*      */       {
/* 1412 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1414 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsIntegrityEnhancementFacility() throws SQLException {
/*      */     try {
/* 1422 */       return this.inner.supportsIntegrityEnhancementFacility();
/*      */     }
/* 1424 */     catch (NullPointerException exc) {
/*      */       
/* 1426 */       if (isDetached())
/*      */       {
/* 1428 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1430 */       throw exc;
/*      */     }
/* 1432 */     catch (Exception exc) {
/*      */       
/* 1434 */       if (!isDetached())
/*      */       {
/* 1436 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1438 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsOuterJoins() throws SQLException {
/*      */     try {
/* 1446 */       return this.inner.supportsOuterJoins();
/*      */     }
/* 1448 */     catch (NullPointerException exc) {
/*      */       
/* 1450 */       if (isDetached())
/*      */       {
/* 1452 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1454 */       throw exc;
/*      */     }
/* 1456 */     catch (Exception exc) {
/*      */       
/* 1458 */       if (!isDetached())
/*      */       {
/* 1460 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1462 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsFullOuterJoins() throws SQLException {
/*      */     try {
/* 1470 */       return this.inner.supportsFullOuterJoins();
/*      */     }
/* 1472 */     catch (NullPointerException exc) {
/*      */       
/* 1474 */       if (isDetached())
/*      */       {
/* 1476 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1478 */       throw exc;
/*      */     }
/* 1480 */     catch (Exception exc) {
/*      */       
/* 1482 */       if (!isDetached())
/*      */       {
/* 1484 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1486 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsLimitedOuterJoins() throws SQLException {
/*      */     try {
/* 1494 */       return this.inner.supportsLimitedOuterJoins();
/*      */     }
/* 1496 */     catch (NullPointerException exc) {
/*      */       
/* 1498 */       if (isDetached())
/*      */       {
/* 1500 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1502 */       throw exc;
/*      */     }
/* 1504 */     catch (Exception exc) {
/*      */       
/* 1506 */       if (!isDetached())
/*      */       {
/* 1508 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1510 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getSchemaTerm() throws SQLException {
/*      */     try {
/* 1518 */       return this.inner.getSchemaTerm();
/*      */     }
/* 1520 */     catch (NullPointerException exc) {
/*      */       
/* 1522 */       if (isDetached())
/*      */       {
/* 1524 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1526 */       throw exc;
/*      */     }
/* 1528 */     catch (Exception exc) {
/*      */       
/* 1530 */       if (!isDetached())
/*      */       {
/* 1532 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1534 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getProcedureTerm() throws SQLException {
/*      */     try {
/* 1542 */       return this.inner.getProcedureTerm();
/*      */     }
/* 1544 */     catch (NullPointerException exc) {
/*      */       
/* 1546 */       if (isDetached())
/*      */       {
/* 1548 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1550 */       throw exc;
/*      */     }
/* 1552 */     catch (Exception exc) {
/*      */       
/* 1554 */       if (!isDetached())
/*      */       {
/* 1556 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1558 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getCatalogTerm() throws SQLException {
/*      */     try {
/* 1566 */       return this.inner.getCatalogTerm();
/*      */     }
/* 1568 */     catch (NullPointerException exc) {
/*      */       
/* 1570 */       if (isDetached())
/*      */       {
/* 1572 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1574 */       throw exc;
/*      */     }
/* 1576 */     catch (Exception exc) {
/*      */       
/* 1578 */       if (!isDetached())
/*      */       {
/* 1580 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1582 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCatalogAtStart() throws SQLException {
/*      */     try {
/* 1590 */       return this.inner.isCatalogAtStart();
/*      */     }
/* 1592 */     catch (NullPointerException exc) {
/*      */       
/* 1594 */       if (isDetached())
/*      */       {
/* 1596 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1598 */       throw exc;
/*      */     }
/* 1600 */     catch (Exception exc) {
/*      */       
/* 1602 */       if (!isDetached())
/*      */       {
/* 1604 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1606 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getCatalogSeparator() throws SQLException {
/*      */     try {
/* 1614 */       return this.inner.getCatalogSeparator();
/*      */     }
/* 1616 */     catch (NullPointerException exc) {
/*      */       
/* 1618 */       if (isDetached())
/*      */       {
/* 1620 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1622 */       throw exc;
/*      */     }
/* 1624 */     catch (Exception exc) {
/*      */       
/* 1626 */       if (!isDetached())
/*      */       {
/* 1628 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1630 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSchemasInDataManipulation() throws SQLException {
/*      */     try {
/* 1638 */       return this.inner.supportsSchemasInDataManipulation();
/*      */     }
/* 1640 */     catch (NullPointerException exc) {
/*      */       
/* 1642 */       if (isDetached())
/*      */       {
/* 1644 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1646 */       throw exc;
/*      */     }
/* 1648 */     catch (Exception exc) {
/*      */       
/* 1650 */       if (!isDetached())
/*      */       {
/* 1652 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1654 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSchemasInProcedureCalls() throws SQLException {
/*      */     try {
/* 1662 */       return this.inner.supportsSchemasInProcedureCalls();
/*      */     }
/* 1664 */     catch (NullPointerException exc) {
/*      */       
/* 1666 */       if (isDetached())
/*      */       {
/* 1668 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1670 */       throw exc;
/*      */     }
/* 1672 */     catch (Exception exc) {
/*      */       
/* 1674 */       if (!isDetached())
/*      */       {
/* 1676 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1678 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSchemasInTableDefinitions() throws SQLException {
/*      */     try {
/* 1686 */       return this.inner.supportsSchemasInTableDefinitions();
/*      */     }
/* 1688 */     catch (NullPointerException exc) {
/*      */       
/* 1690 */       if (isDetached())
/*      */       {
/* 1692 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1694 */       throw exc;
/*      */     }
/* 1696 */     catch (Exception exc) {
/*      */       
/* 1698 */       if (!isDetached())
/*      */       {
/* 1700 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1702 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSchemasInIndexDefinitions() throws SQLException {
/*      */     try {
/* 1710 */       return this.inner.supportsSchemasInIndexDefinitions();
/*      */     }
/* 1712 */     catch (NullPointerException exc) {
/*      */       
/* 1714 */       if (isDetached())
/*      */       {
/* 1716 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1718 */       throw exc;
/*      */     }
/* 1720 */     catch (Exception exc) {
/*      */       
/* 1722 */       if (!isDetached())
/*      */       {
/* 1724 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1726 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
/*      */     try {
/* 1734 */       return this.inner.supportsSchemasInPrivilegeDefinitions();
/*      */     }
/* 1736 */     catch (NullPointerException exc) {
/*      */       
/* 1738 */       if (isDetached())
/*      */       {
/* 1740 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1742 */       throw exc;
/*      */     }
/* 1744 */     catch (Exception exc) {
/*      */       
/* 1746 */       if (!isDetached())
/*      */       {
/* 1748 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1750 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCatalogsInDataManipulation() throws SQLException {
/*      */     try {
/* 1758 */       return this.inner.supportsCatalogsInDataManipulation();
/*      */     }
/* 1760 */     catch (NullPointerException exc) {
/*      */       
/* 1762 */       if (isDetached())
/*      */       {
/* 1764 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1766 */       throw exc;
/*      */     }
/* 1768 */     catch (Exception exc) {
/*      */       
/* 1770 */       if (!isDetached())
/*      */       {
/* 1772 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1774 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCatalogsInProcedureCalls() throws SQLException {
/*      */     try {
/* 1782 */       return this.inner.supportsCatalogsInProcedureCalls();
/*      */     }
/* 1784 */     catch (NullPointerException exc) {
/*      */       
/* 1786 */       if (isDetached())
/*      */       {
/* 1788 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1790 */       throw exc;
/*      */     }
/* 1792 */     catch (Exception exc) {
/*      */       
/* 1794 */       if (!isDetached())
/*      */       {
/* 1796 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1798 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCatalogsInTableDefinitions() throws SQLException {
/*      */     try {
/* 1806 */       return this.inner.supportsCatalogsInTableDefinitions();
/*      */     }
/* 1808 */     catch (NullPointerException exc) {
/*      */       
/* 1810 */       if (isDetached())
/*      */       {
/* 1812 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1814 */       throw exc;
/*      */     }
/* 1816 */     catch (Exception exc) {
/*      */       
/* 1818 */       if (!isDetached())
/*      */       {
/* 1820 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1822 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCatalogsInIndexDefinitions() throws SQLException {
/*      */     try {
/* 1830 */       return this.inner.supportsCatalogsInIndexDefinitions();
/*      */     }
/* 1832 */     catch (NullPointerException exc) {
/*      */       
/* 1834 */       if (isDetached())
/*      */       {
/* 1836 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1838 */       throw exc;
/*      */     }
/* 1840 */     catch (Exception exc) {
/*      */       
/* 1842 */       if (!isDetached())
/*      */       {
/* 1844 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1846 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
/*      */     try {
/* 1854 */       return this.inner.supportsCatalogsInPrivilegeDefinitions();
/*      */     }
/* 1856 */     catch (NullPointerException exc) {
/*      */       
/* 1858 */       if (isDetached())
/*      */       {
/* 1860 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1862 */       throw exc;
/*      */     }
/* 1864 */     catch (Exception exc) {
/*      */       
/* 1866 */       if (!isDetached())
/*      */       {
/* 1868 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1870 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsPositionedDelete() throws SQLException {
/*      */     try {
/* 1878 */       return this.inner.supportsPositionedDelete();
/*      */     }
/* 1880 */     catch (NullPointerException exc) {
/*      */       
/* 1882 */       if (isDetached())
/*      */       {
/* 1884 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1886 */       throw exc;
/*      */     }
/* 1888 */     catch (Exception exc) {
/*      */       
/* 1890 */       if (!isDetached())
/*      */       {
/* 1892 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1894 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsPositionedUpdate() throws SQLException {
/*      */     try {
/* 1902 */       return this.inner.supportsPositionedUpdate();
/*      */     }
/* 1904 */     catch (NullPointerException exc) {
/*      */       
/* 1906 */       if (isDetached())
/*      */       {
/* 1908 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1910 */       throw exc;
/*      */     }
/* 1912 */     catch (Exception exc) {
/*      */       
/* 1914 */       if (!isDetached())
/*      */       {
/* 1916 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1918 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSelectForUpdate() throws SQLException {
/*      */     try {
/* 1926 */       return this.inner.supportsSelectForUpdate();
/*      */     }
/* 1928 */     catch (NullPointerException exc) {
/*      */       
/* 1930 */       if (isDetached())
/*      */       {
/* 1932 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1934 */       throw exc;
/*      */     }
/* 1936 */     catch (Exception exc) {
/*      */       
/* 1938 */       if (!isDetached())
/*      */       {
/* 1940 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1942 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsStoredProcedures() throws SQLException {
/*      */     try {
/* 1950 */       return this.inner.supportsStoredProcedures();
/*      */     }
/* 1952 */     catch (NullPointerException exc) {
/*      */       
/* 1954 */       if (isDetached())
/*      */       {
/* 1956 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1958 */       throw exc;
/*      */     }
/* 1960 */     catch (Exception exc) {
/*      */       
/* 1962 */       if (!isDetached())
/*      */       {
/* 1964 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1966 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSubqueriesInComparisons() throws SQLException {
/*      */     try {
/* 1974 */       return this.inner.supportsSubqueriesInComparisons();
/*      */     }
/* 1976 */     catch (NullPointerException exc) {
/*      */       
/* 1978 */       if (isDetached())
/*      */       {
/* 1980 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 1982 */       throw exc;
/*      */     }
/* 1984 */     catch (Exception exc) {
/*      */       
/* 1986 */       if (!isDetached())
/*      */       {
/* 1988 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1990 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSubqueriesInExists() throws SQLException {
/*      */     try {
/* 1998 */       return this.inner.supportsSubqueriesInExists();
/*      */     }
/* 2000 */     catch (NullPointerException exc) {
/*      */       
/* 2002 */       if (isDetached())
/*      */       {
/* 2004 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2006 */       throw exc;
/*      */     }
/* 2008 */     catch (Exception exc) {
/*      */       
/* 2010 */       if (!isDetached())
/*      */       {
/* 2012 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2014 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSubqueriesInIns() throws SQLException {
/*      */     try {
/* 2022 */       return this.inner.supportsSubqueriesInIns();
/*      */     }
/* 2024 */     catch (NullPointerException exc) {
/*      */       
/* 2026 */       if (isDetached())
/*      */       {
/* 2028 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2030 */       throw exc;
/*      */     }
/* 2032 */     catch (Exception exc) {
/*      */       
/* 2034 */       if (!isDetached())
/*      */       {
/* 2036 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2038 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSubqueriesInQuantifieds() throws SQLException {
/*      */     try {
/* 2046 */       return this.inner.supportsSubqueriesInQuantifieds();
/*      */     }
/* 2048 */     catch (NullPointerException exc) {
/*      */       
/* 2050 */       if (isDetached())
/*      */       {
/* 2052 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2054 */       throw exc;
/*      */     }
/* 2056 */     catch (Exception exc) {
/*      */       
/* 2058 */       if (!isDetached())
/*      */       {
/* 2060 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2062 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsCorrelatedSubqueries() throws SQLException {
/*      */     try {
/* 2070 */       return this.inner.supportsCorrelatedSubqueries();
/*      */     }
/* 2072 */     catch (NullPointerException exc) {
/*      */       
/* 2074 */       if (isDetached())
/*      */       {
/* 2076 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2078 */       throw exc;
/*      */     }
/* 2080 */     catch (Exception exc) {
/*      */       
/* 2082 */       if (!isDetached())
/*      */       {
/* 2084 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2086 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsUnion() throws SQLException {
/*      */     try {
/* 2094 */       return this.inner.supportsUnion();
/*      */     }
/* 2096 */     catch (NullPointerException exc) {
/*      */       
/* 2098 */       if (isDetached())
/*      */       {
/* 2100 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2102 */       throw exc;
/*      */     }
/* 2104 */     catch (Exception exc) {
/*      */       
/* 2106 */       if (!isDetached())
/*      */       {
/* 2108 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2110 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsUnionAll() throws SQLException {
/*      */     try {
/* 2118 */       return this.inner.supportsUnionAll();
/*      */     }
/* 2120 */     catch (NullPointerException exc) {
/*      */       
/* 2122 */       if (isDetached())
/*      */       {
/* 2124 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2126 */       throw exc;
/*      */     }
/* 2128 */     catch (Exception exc) {
/*      */       
/* 2130 */       if (!isDetached())
/*      */       {
/* 2132 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2134 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsOpenCursorsAcrossCommit() throws SQLException {
/*      */     try {
/* 2142 */       return this.inner.supportsOpenCursorsAcrossCommit();
/*      */     }
/* 2144 */     catch (NullPointerException exc) {
/*      */       
/* 2146 */       if (isDetached())
/*      */       {
/* 2148 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2150 */       throw exc;
/*      */     }
/* 2152 */     catch (Exception exc) {
/*      */       
/* 2154 */       if (!isDetached())
/*      */       {
/* 2156 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2158 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsOpenCursorsAcrossRollback() throws SQLException {
/*      */     try {
/* 2166 */       return this.inner.supportsOpenCursorsAcrossRollback();
/*      */     }
/* 2168 */     catch (NullPointerException exc) {
/*      */       
/* 2170 */       if (isDetached())
/*      */       {
/* 2172 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2174 */       throw exc;
/*      */     }
/* 2176 */     catch (Exception exc) {
/*      */       
/* 2178 */       if (!isDetached())
/*      */       {
/* 2180 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2182 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsOpenStatementsAcrossCommit() throws SQLException {
/*      */     try {
/* 2190 */       return this.inner.supportsOpenStatementsAcrossCommit();
/*      */     }
/* 2192 */     catch (NullPointerException exc) {
/*      */       
/* 2194 */       if (isDetached())
/*      */       {
/* 2196 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2198 */       throw exc;
/*      */     }
/* 2200 */     catch (Exception exc) {
/*      */       
/* 2202 */       if (!isDetached())
/*      */       {
/* 2204 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2206 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsOpenStatementsAcrossRollback() throws SQLException {
/*      */     try {
/* 2214 */       return this.inner.supportsOpenStatementsAcrossRollback();
/*      */     }
/* 2216 */     catch (NullPointerException exc) {
/*      */       
/* 2218 */       if (isDetached())
/*      */       {
/* 2220 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2222 */       throw exc;
/*      */     }
/* 2224 */     catch (Exception exc) {
/*      */       
/* 2226 */       if (!isDetached())
/*      */       {
/* 2228 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2230 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxBinaryLiteralLength() throws SQLException {
/*      */     try {
/* 2238 */       return this.inner.getMaxBinaryLiteralLength();
/*      */     }
/* 2240 */     catch (NullPointerException exc) {
/*      */       
/* 2242 */       if (isDetached())
/*      */       {
/* 2244 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2246 */       throw exc;
/*      */     }
/* 2248 */     catch (Exception exc) {
/*      */       
/* 2250 */       if (!isDetached())
/*      */       {
/* 2252 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2254 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxCharLiteralLength() throws SQLException {
/*      */     try {
/* 2262 */       return this.inner.getMaxCharLiteralLength();
/*      */     }
/* 2264 */     catch (NullPointerException exc) {
/*      */       
/* 2266 */       if (isDetached())
/*      */       {
/* 2268 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2270 */       throw exc;
/*      */     }
/* 2272 */     catch (Exception exc) {
/*      */       
/* 2274 */       if (!isDetached())
/*      */       {
/* 2276 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2278 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxColumnNameLength() throws SQLException {
/*      */     try {
/* 2286 */       return this.inner.getMaxColumnNameLength();
/*      */     }
/* 2288 */     catch (NullPointerException exc) {
/*      */       
/* 2290 */       if (isDetached())
/*      */       {
/* 2292 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2294 */       throw exc;
/*      */     }
/* 2296 */     catch (Exception exc) {
/*      */       
/* 2298 */       if (!isDetached())
/*      */       {
/* 2300 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2302 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxColumnsInGroupBy() throws SQLException {
/*      */     try {
/* 2310 */       return this.inner.getMaxColumnsInGroupBy();
/*      */     }
/* 2312 */     catch (NullPointerException exc) {
/*      */       
/* 2314 */       if (isDetached())
/*      */       {
/* 2316 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2318 */       throw exc;
/*      */     }
/* 2320 */     catch (Exception exc) {
/*      */       
/* 2322 */       if (!isDetached())
/*      */       {
/* 2324 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2326 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxColumnsInIndex() throws SQLException {
/*      */     try {
/* 2334 */       return this.inner.getMaxColumnsInIndex();
/*      */     }
/* 2336 */     catch (NullPointerException exc) {
/*      */       
/* 2338 */       if (isDetached())
/*      */       {
/* 2340 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2342 */       throw exc;
/*      */     }
/* 2344 */     catch (Exception exc) {
/*      */       
/* 2346 */       if (!isDetached())
/*      */       {
/* 2348 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2350 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxColumnsInOrderBy() throws SQLException {
/*      */     try {
/* 2358 */       return this.inner.getMaxColumnsInOrderBy();
/*      */     }
/* 2360 */     catch (NullPointerException exc) {
/*      */       
/* 2362 */       if (isDetached())
/*      */       {
/* 2364 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2366 */       throw exc;
/*      */     }
/* 2368 */     catch (Exception exc) {
/*      */       
/* 2370 */       if (!isDetached())
/*      */       {
/* 2372 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2374 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxColumnsInSelect() throws SQLException {
/*      */     try {
/* 2382 */       return this.inner.getMaxColumnsInSelect();
/*      */     }
/* 2384 */     catch (NullPointerException exc) {
/*      */       
/* 2386 */       if (isDetached())
/*      */       {
/* 2388 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2390 */       throw exc;
/*      */     }
/* 2392 */     catch (Exception exc) {
/*      */       
/* 2394 */       if (!isDetached())
/*      */       {
/* 2396 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2398 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxColumnsInTable() throws SQLException {
/*      */     try {
/* 2406 */       return this.inner.getMaxColumnsInTable();
/*      */     }
/* 2408 */     catch (NullPointerException exc) {
/*      */       
/* 2410 */       if (isDetached())
/*      */       {
/* 2412 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2414 */       throw exc;
/*      */     }
/* 2416 */     catch (Exception exc) {
/*      */       
/* 2418 */       if (!isDetached())
/*      */       {
/* 2420 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2422 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxConnections() throws SQLException {
/*      */     try {
/* 2430 */       return this.inner.getMaxConnections();
/*      */     }
/* 2432 */     catch (NullPointerException exc) {
/*      */       
/* 2434 */       if (isDetached())
/*      */       {
/* 2436 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2438 */       throw exc;
/*      */     }
/* 2440 */     catch (Exception exc) {
/*      */       
/* 2442 */       if (!isDetached())
/*      */       {
/* 2444 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2446 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxCursorNameLength() throws SQLException {
/*      */     try {
/* 2454 */       return this.inner.getMaxCursorNameLength();
/*      */     }
/* 2456 */     catch (NullPointerException exc) {
/*      */       
/* 2458 */       if (isDetached())
/*      */       {
/* 2460 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2462 */       throw exc;
/*      */     }
/* 2464 */     catch (Exception exc) {
/*      */       
/* 2466 */       if (!isDetached())
/*      */       {
/* 2468 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2470 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxIndexLength() throws SQLException {
/*      */     try {
/* 2478 */       return this.inner.getMaxIndexLength();
/*      */     }
/* 2480 */     catch (NullPointerException exc) {
/*      */       
/* 2482 */       if (isDetached())
/*      */       {
/* 2484 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2486 */       throw exc;
/*      */     }
/* 2488 */     catch (Exception exc) {
/*      */       
/* 2490 */       if (!isDetached())
/*      */       {
/* 2492 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2494 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxSchemaNameLength() throws SQLException {
/*      */     try {
/* 2502 */       return this.inner.getMaxSchemaNameLength();
/*      */     }
/* 2504 */     catch (NullPointerException exc) {
/*      */       
/* 2506 */       if (isDetached())
/*      */       {
/* 2508 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2510 */       throw exc;
/*      */     }
/* 2512 */     catch (Exception exc) {
/*      */       
/* 2514 */       if (!isDetached())
/*      */       {
/* 2516 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2518 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxProcedureNameLength() throws SQLException {
/*      */     try {
/* 2526 */       return this.inner.getMaxProcedureNameLength();
/*      */     }
/* 2528 */     catch (NullPointerException exc) {
/*      */       
/* 2530 */       if (isDetached())
/*      */       {
/* 2532 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2534 */       throw exc;
/*      */     }
/* 2536 */     catch (Exception exc) {
/*      */       
/* 2538 */       if (!isDetached())
/*      */       {
/* 2540 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2542 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxCatalogNameLength() throws SQLException {
/*      */     try {
/* 2550 */       return this.inner.getMaxCatalogNameLength();
/*      */     }
/* 2552 */     catch (NullPointerException exc) {
/*      */       
/* 2554 */       if (isDetached())
/*      */       {
/* 2556 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2558 */       throw exc;
/*      */     }
/* 2560 */     catch (Exception exc) {
/*      */       
/* 2562 */       if (!isDetached())
/*      */       {
/* 2564 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2566 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxRowSize() throws SQLException {
/*      */     try {
/* 2574 */       return this.inner.getMaxRowSize();
/*      */     }
/* 2576 */     catch (NullPointerException exc) {
/*      */       
/* 2578 */       if (isDetached())
/*      */       {
/* 2580 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2582 */       throw exc;
/*      */     }
/* 2584 */     catch (Exception exc) {
/*      */       
/* 2586 */       if (!isDetached())
/*      */       {
/* 2588 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2590 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
/*      */     try {
/* 2598 */       return this.inner.doesMaxRowSizeIncludeBlobs();
/*      */     }
/* 2600 */     catch (NullPointerException exc) {
/*      */       
/* 2602 */       if (isDetached())
/*      */       {
/* 2604 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2606 */       throw exc;
/*      */     }
/* 2608 */     catch (Exception exc) {
/*      */       
/* 2610 */       if (!isDetached())
/*      */       {
/* 2612 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2614 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxStatementLength() throws SQLException {
/*      */     try {
/* 2622 */       return this.inner.getMaxStatementLength();
/*      */     }
/* 2624 */     catch (NullPointerException exc) {
/*      */       
/* 2626 */       if (isDetached())
/*      */       {
/* 2628 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2630 */       throw exc;
/*      */     }
/* 2632 */     catch (Exception exc) {
/*      */       
/* 2634 */       if (!isDetached())
/*      */       {
/* 2636 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2638 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxStatements() throws SQLException {
/*      */     try {
/* 2646 */       return this.inner.getMaxStatements();
/*      */     }
/* 2648 */     catch (NullPointerException exc) {
/*      */       
/* 2650 */       if (isDetached())
/*      */       {
/* 2652 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2654 */       throw exc;
/*      */     }
/* 2656 */     catch (Exception exc) {
/*      */       
/* 2658 */       if (!isDetached())
/*      */       {
/* 2660 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2662 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxTableNameLength() throws SQLException {
/*      */     try {
/* 2670 */       return this.inner.getMaxTableNameLength();
/*      */     }
/* 2672 */     catch (NullPointerException exc) {
/*      */       
/* 2674 */       if (isDetached())
/*      */       {
/* 2676 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2678 */       throw exc;
/*      */     }
/* 2680 */     catch (Exception exc) {
/*      */       
/* 2682 */       if (!isDetached())
/*      */       {
/* 2684 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2686 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxTablesInSelect() throws SQLException {
/*      */     try {
/* 2694 */       return this.inner.getMaxTablesInSelect();
/*      */     }
/* 2696 */     catch (NullPointerException exc) {
/*      */       
/* 2698 */       if (isDetached())
/*      */       {
/* 2700 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2702 */       throw exc;
/*      */     }
/* 2704 */     catch (Exception exc) {
/*      */       
/* 2706 */       if (!isDetached())
/*      */       {
/* 2708 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2710 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxUserNameLength() throws SQLException {
/*      */     try {
/* 2718 */       return this.inner.getMaxUserNameLength();
/*      */     }
/* 2720 */     catch (NullPointerException exc) {
/*      */       
/* 2722 */       if (isDetached())
/*      */       {
/* 2724 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2726 */       throw exc;
/*      */     }
/* 2728 */     catch (Exception exc) {
/*      */       
/* 2730 */       if (!isDetached())
/*      */       {
/* 2732 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2734 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDefaultTransactionIsolation() throws SQLException {
/*      */     try {
/* 2742 */       return this.inner.getDefaultTransactionIsolation();
/*      */     }
/* 2744 */     catch (NullPointerException exc) {
/*      */       
/* 2746 */       if (isDetached())
/*      */       {
/* 2748 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2750 */       throw exc;
/*      */     }
/* 2752 */     catch (Exception exc) {
/*      */       
/* 2754 */       if (!isDetached())
/*      */       {
/* 2756 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2758 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsTransactions() throws SQLException {
/*      */     try {
/* 2766 */       return this.inner.supportsTransactions();
/*      */     }
/* 2768 */     catch (NullPointerException exc) {
/*      */       
/* 2770 */       if (isDetached())
/*      */       {
/* 2772 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2774 */       throw exc;
/*      */     }
/* 2776 */     catch (Exception exc) {
/*      */       
/* 2778 */       if (!isDetached())
/*      */       {
/* 2780 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2782 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsTransactionIsolationLevel(int a) throws SQLException {
/*      */     try {
/* 2790 */       return this.inner.supportsTransactionIsolationLevel(a);
/*      */     }
/* 2792 */     catch (NullPointerException exc) {
/*      */       
/* 2794 */       if (isDetached())
/*      */       {
/* 2796 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2798 */       throw exc;
/*      */     }
/* 2800 */     catch (Exception exc) {
/*      */       
/* 2802 */       if (!isDetached())
/*      */       {
/* 2804 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2806 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
/*      */     try {
/* 2814 */       return this.inner.supportsDataDefinitionAndDataManipulationTransactions();
/*      */     }
/* 2816 */     catch (NullPointerException exc) {
/*      */       
/* 2818 */       if (isDetached())
/*      */       {
/* 2820 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2822 */       throw exc;
/*      */     }
/* 2824 */     catch (Exception exc) {
/*      */       
/* 2826 */       if (!isDetached())
/*      */       {
/* 2828 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2830 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsDataManipulationTransactionsOnly() throws SQLException {
/*      */     try {
/* 2838 */       return this.inner.supportsDataManipulationTransactionsOnly();
/*      */     }
/* 2840 */     catch (NullPointerException exc) {
/*      */       
/* 2842 */       if (isDetached())
/*      */       {
/* 2844 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2846 */       throw exc;
/*      */     }
/* 2848 */     catch (Exception exc) {
/*      */       
/* 2850 */       if (!isDetached())
/*      */       {
/* 2852 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2854 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean dataDefinitionCausesTransactionCommit() throws SQLException {
/*      */     try {
/* 2862 */       return this.inner.dataDefinitionCausesTransactionCommit();
/*      */     }
/* 2864 */     catch (NullPointerException exc) {
/*      */       
/* 2866 */       if (isDetached())
/*      */       {
/* 2868 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2870 */       throw exc;
/*      */     }
/* 2872 */     catch (Exception exc) {
/*      */       
/* 2874 */       if (!isDetached())
/*      */       {
/* 2876 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2878 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean dataDefinitionIgnoredInTransactions() throws SQLException {
/*      */     try {
/* 2886 */       return this.inner.dataDefinitionIgnoredInTransactions();
/*      */     }
/* 2888 */     catch (NullPointerException exc) {
/*      */       
/* 2890 */       if (isDetached())
/*      */       {
/* 2892 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2894 */       throw exc;
/*      */     }
/* 2896 */     catch (Exception exc) {
/*      */       
/* 2898 */       if (!isDetached())
/*      */       {
/* 2900 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2902 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getProcedures(String a, String b, String c) throws SQLException {
/*      */     try {
/* 2910 */       ResultSet innerResultSet = this.inner.getProcedures(a, b, c);
/* 2911 */       if (innerResultSet == null) return null; 
/* 2912 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 2914 */     catch (NullPointerException exc) {
/*      */       
/* 2916 */       if (isDetached())
/*      */       {
/* 2918 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2920 */       throw exc;
/*      */     }
/* 2922 */     catch (Exception exc) {
/*      */       
/* 2924 */       if (!isDetached())
/*      */       {
/* 2926 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2928 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getProcedureColumns(String a, String b, String c, String d) throws SQLException {
/*      */     try {
/* 2936 */       ResultSet innerResultSet = this.inner.getProcedureColumns(a, b, c, d);
/* 2937 */       if (innerResultSet == null) return null; 
/* 2938 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 2940 */     catch (NullPointerException exc) {
/*      */       
/* 2942 */       if (isDetached())
/*      */       {
/* 2944 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2946 */       throw exc;
/*      */     }
/* 2948 */     catch (Exception exc) {
/*      */       
/* 2950 */       if (!isDetached())
/*      */       {
/* 2952 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2954 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getTables(String a, String b, String c, String[] d) throws SQLException {
/*      */     try {
/* 2962 */       ResultSet innerResultSet = this.inner.getTables(a, b, c, d);
/* 2963 */       if (innerResultSet == null) return null; 
/* 2964 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 2966 */     catch (NullPointerException exc) {
/*      */       
/* 2968 */       if (isDetached())
/*      */       {
/* 2970 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2972 */       throw exc;
/*      */     }
/* 2974 */     catch (Exception exc) {
/*      */       
/* 2976 */       if (!isDetached())
/*      */       {
/* 2978 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2980 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getSchemas(String a, String b) throws SQLException {
/*      */     try {
/* 2988 */       ResultSet innerResultSet = this.inner.getSchemas(a, b);
/* 2989 */       if (innerResultSet == null) return null; 
/* 2990 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 2992 */     catch (NullPointerException exc) {
/*      */       
/* 2994 */       if (isDetached())
/*      */       {
/* 2996 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 2998 */       throw exc;
/*      */     }
/* 3000 */     catch (Exception exc) {
/*      */       
/* 3002 */       if (!isDetached())
/*      */       {
/* 3004 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3006 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getSchemas() throws SQLException {
/*      */     try {
/* 3014 */       ResultSet innerResultSet = this.inner.getSchemas();
/* 3015 */       if (innerResultSet == null) return null; 
/* 3016 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3018 */     catch (NullPointerException exc) {
/*      */       
/* 3020 */       if (isDetached())
/*      */       {
/* 3022 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3024 */       throw exc;
/*      */     }
/* 3026 */     catch (Exception exc) {
/*      */       
/* 3028 */       if (!isDetached())
/*      */       {
/* 3030 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3032 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getCatalogs() throws SQLException {
/*      */     try {
/* 3040 */       ResultSet innerResultSet = this.inner.getCatalogs();
/* 3041 */       if (innerResultSet == null) return null; 
/* 3042 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3044 */     catch (NullPointerException exc) {
/*      */       
/* 3046 */       if (isDetached())
/*      */       {
/* 3048 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3050 */       throw exc;
/*      */     }
/* 3052 */     catch (Exception exc) {
/*      */       
/* 3054 */       if (!isDetached())
/*      */       {
/* 3056 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3058 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getTableTypes() throws SQLException {
/*      */     try {
/* 3066 */       ResultSet innerResultSet = this.inner.getTableTypes();
/* 3067 */       if (innerResultSet == null) return null; 
/* 3068 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3070 */     catch (NullPointerException exc) {
/*      */       
/* 3072 */       if (isDetached())
/*      */       {
/* 3074 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3076 */       throw exc;
/*      */     }
/* 3078 */     catch (Exception exc) {
/*      */       
/* 3080 */       if (!isDetached())
/*      */       {
/* 3082 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3084 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getColumns(String a, String b, String c, String d) throws SQLException {
/*      */     try {
/* 3092 */       ResultSet innerResultSet = this.inner.getColumns(a, b, c, d);
/* 3093 */       if (innerResultSet == null) return null; 
/* 3094 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3096 */     catch (NullPointerException exc) {
/*      */       
/* 3098 */       if (isDetached())
/*      */       {
/* 3100 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3102 */       throw exc;
/*      */     }
/* 3104 */     catch (Exception exc) {
/*      */       
/* 3106 */       if (!isDetached())
/*      */       {
/* 3108 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3110 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getColumnPrivileges(String a, String b, String c, String d) throws SQLException {
/*      */     try {
/* 3118 */       ResultSet innerResultSet = this.inner.getColumnPrivileges(a, b, c, d);
/* 3119 */       if (innerResultSet == null) return null; 
/* 3120 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3122 */     catch (NullPointerException exc) {
/*      */       
/* 3124 */       if (isDetached())
/*      */       {
/* 3126 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3128 */       throw exc;
/*      */     }
/* 3130 */     catch (Exception exc) {
/*      */       
/* 3132 */       if (!isDetached())
/*      */       {
/* 3134 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3136 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getTablePrivileges(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3144 */       ResultSet innerResultSet = this.inner.getTablePrivileges(a, b, c);
/* 3145 */       if (innerResultSet == null) return null; 
/* 3146 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3148 */     catch (NullPointerException exc) {
/*      */       
/* 3150 */       if (isDetached())
/*      */       {
/* 3152 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3154 */       throw exc;
/*      */     }
/* 3156 */     catch (Exception exc) {
/*      */       
/* 3158 */       if (!isDetached())
/*      */       {
/* 3160 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3162 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getBestRowIdentifier(String a, String b, String c, int d, boolean e) throws SQLException {
/*      */     try {
/* 3170 */       ResultSet innerResultSet = this.inner.getBestRowIdentifier(a, b, c, d, e);
/* 3171 */       if (innerResultSet == null) return null; 
/* 3172 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3174 */     catch (NullPointerException exc) {
/*      */       
/* 3176 */       if (isDetached())
/*      */       {
/* 3178 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3180 */       throw exc;
/*      */     }
/* 3182 */     catch (Exception exc) {
/*      */       
/* 3184 */       if (!isDetached())
/*      */       {
/* 3186 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3188 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getVersionColumns(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3196 */       ResultSet innerResultSet = this.inner.getVersionColumns(a, b, c);
/* 3197 */       if (innerResultSet == null) return null; 
/* 3198 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3200 */     catch (NullPointerException exc) {
/*      */       
/* 3202 */       if (isDetached())
/*      */       {
/* 3204 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3206 */       throw exc;
/*      */     }
/* 3208 */     catch (Exception exc) {
/*      */       
/* 3210 */       if (!isDetached())
/*      */       {
/* 3212 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3214 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getPrimaryKeys(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3222 */       ResultSet innerResultSet = this.inner.getPrimaryKeys(a, b, c);
/* 3223 */       if (innerResultSet == null) return null; 
/* 3224 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3226 */     catch (NullPointerException exc) {
/*      */       
/* 3228 */       if (isDetached())
/*      */       {
/* 3230 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3232 */       throw exc;
/*      */     }
/* 3234 */     catch (Exception exc) {
/*      */       
/* 3236 */       if (!isDetached())
/*      */       {
/* 3238 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3240 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getImportedKeys(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3248 */       ResultSet innerResultSet = this.inner.getImportedKeys(a, b, c);
/* 3249 */       if (innerResultSet == null) return null; 
/* 3250 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3252 */     catch (NullPointerException exc) {
/*      */       
/* 3254 */       if (isDetached())
/*      */       {
/* 3256 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3258 */       throw exc;
/*      */     }
/* 3260 */     catch (Exception exc) {
/*      */       
/* 3262 */       if (!isDetached())
/*      */       {
/* 3264 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3266 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getExportedKeys(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3274 */       ResultSet innerResultSet = this.inner.getExportedKeys(a, b, c);
/* 3275 */       if (innerResultSet == null) return null; 
/* 3276 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3278 */     catch (NullPointerException exc) {
/*      */       
/* 3280 */       if (isDetached())
/*      */       {
/* 3282 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3284 */       throw exc;
/*      */     }
/* 3286 */     catch (Exception exc) {
/*      */       
/* 3288 */       if (!isDetached())
/*      */       {
/* 3290 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3292 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getCrossReference(String a, String b, String c, String d, String e, String f) throws SQLException {
/*      */     try {
/* 3300 */       ResultSet innerResultSet = this.inner.getCrossReference(a, b, c, d, e, f);
/* 3301 */       if (innerResultSet == null) return null; 
/* 3302 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3304 */     catch (NullPointerException exc) {
/*      */       
/* 3306 */       if (isDetached())
/*      */       {
/* 3308 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3310 */       throw exc;
/*      */     }
/* 3312 */     catch (Exception exc) {
/*      */       
/* 3314 */       if (!isDetached())
/*      */       {
/* 3316 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3318 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getTypeInfo() throws SQLException {
/*      */     try {
/* 3326 */       ResultSet innerResultSet = this.inner.getTypeInfo();
/* 3327 */       if (innerResultSet == null) return null; 
/* 3328 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3330 */     catch (NullPointerException exc) {
/*      */       
/* 3332 */       if (isDetached())
/*      */       {
/* 3334 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3336 */       throw exc;
/*      */     }
/* 3338 */     catch (Exception exc) {
/*      */       
/* 3340 */       if (!isDetached())
/*      */       {
/* 3342 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3344 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getIndexInfo(String a, String b, String c, boolean d, boolean e) throws SQLException {
/*      */     try {
/* 3352 */       ResultSet innerResultSet = this.inner.getIndexInfo(a, b, c, d, e);
/* 3353 */       if (innerResultSet == null) return null; 
/* 3354 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3356 */     catch (NullPointerException exc) {
/*      */       
/* 3358 */       if (isDetached())
/*      */       {
/* 3360 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3362 */       throw exc;
/*      */     }
/* 3364 */     catch (Exception exc) {
/*      */       
/* 3366 */       if (!isDetached())
/*      */       {
/* 3368 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3370 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsResultSetType(int a) throws SQLException {
/*      */     try {
/* 3378 */       return this.inner.supportsResultSetType(a);
/*      */     }
/* 3380 */     catch (NullPointerException exc) {
/*      */       
/* 3382 */       if (isDetached())
/*      */       {
/* 3384 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3386 */       throw exc;
/*      */     }
/* 3388 */     catch (Exception exc) {
/*      */       
/* 3390 */       if (!isDetached())
/*      */       {
/* 3392 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3394 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsResultSetConcurrency(int a, int b) throws SQLException {
/*      */     try {
/* 3402 */       return this.inner.supportsResultSetConcurrency(a, b);
/*      */     }
/* 3404 */     catch (NullPointerException exc) {
/*      */       
/* 3406 */       if (isDetached())
/*      */       {
/* 3408 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3410 */       throw exc;
/*      */     }
/* 3412 */     catch (Exception exc) {
/*      */       
/* 3414 */       if (!isDetached())
/*      */       {
/* 3416 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3418 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean ownUpdatesAreVisible(int a) throws SQLException {
/*      */     try {
/* 3426 */       return this.inner.ownUpdatesAreVisible(a);
/*      */     }
/* 3428 */     catch (NullPointerException exc) {
/*      */       
/* 3430 */       if (isDetached())
/*      */       {
/* 3432 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3434 */       throw exc;
/*      */     }
/* 3436 */     catch (Exception exc) {
/*      */       
/* 3438 */       if (!isDetached())
/*      */       {
/* 3440 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3442 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean ownDeletesAreVisible(int a) throws SQLException {
/*      */     try {
/* 3450 */       return this.inner.ownDeletesAreVisible(a);
/*      */     }
/* 3452 */     catch (NullPointerException exc) {
/*      */       
/* 3454 */       if (isDetached())
/*      */       {
/* 3456 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3458 */       throw exc;
/*      */     }
/* 3460 */     catch (Exception exc) {
/*      */       
/* 3462 */       if (!isDetached())
/*      */       {
/* 3464 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3466 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean ownInsertsAreVisible(int a) throws SQLException {
/*      */     try {
/* 3474 */       return this.inner.ownInsertsAreVisible(a);
/*      */     }
/* 3476 */     catch (NullPointerException exc) {
/*      */       
/* 3478 */       if (isDetached())
/*      */       {
/* 3480 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3482 */       throw exc;
/*      */     }
/* 3484 */     catch (Exception exc) {
/*      */       
/* 3486 */       if (!isDetached())
/*      */       {
/* 3488 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3490 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean othersUpdatesAreVisible(int a) throws SQLException {
/*      */     try {
/* 3498 */       return this.inner.othersUpdatesAreVisible(a);
/*      */     }
/* 3500 */     catch (NullPointerException exc) {
/*      */       
/* 3502 */       if (isDetached())
/*      */       {
/* 3504 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3506 */       throw exc;
/*      */     }
/* 3508 */     catch (Exception exc) {
/*      */       
/* 3510 */       if (!isDetached())
/*      */       {
/* 3512 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3514 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean othersDeletesAreVisible(int a) throws SQLException {
/*      */     try {
/* 3522 */       return this.inner.othersDeletesAreVisible(a);
/*      */     }
/* 3524 */     catch (NullPointerException exc) {
/*      */       
/* 3526 */       if (isDetached())
/*      */       {
/* 3528 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3530 */       throw exc;
/*      */     }
/* 3532 */     catch (Exception exc) {
/*      */       
/* 3534 */       if (!isDetached())
/*      */       {
/* 3536 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3538 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean othersInsertsAreVisible(int a) throws SQLException {
/*      */     try {
/* 3546 */       return this.inner.othersInsertsAreVisible(a);
/*      */     }
/* 3548 */     catch (NullPointerException exc) {
/*      */       
/* 3550 */       if (isDetached())
/*      */       {
/* 3552 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3554 */       throw exc;
/*      */     }
/* 3556 */     catch (Exception exc) {
/*      */       
/* 3558 */       if (!isDetached())
/*      */       {
/* 3560 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3562 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean updatesAreDetected(int a) throws SQLException {
/*      */     try {
/* 3570 */       return this.inner.updatesAreDetected(a);
/*      */     }
/* 3572 */     catch (NullPointerException exc) {
/*      */       
/* 3574 */       if (isDetached())
/*      */       {
/* 3576 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3578 */       throw exc;
/*      */     }
/* 3580 */     catch (Exception exc) {
/*      */       
/* 3582 */       if (!isDetached())
/*      */       {
/* 3584 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3586 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean deletesAreDetected(int a) throws SQLException {
/*      */     try {
/* 3594 */       return this.inner.deletesAreDetected(a);
/*      */     }
/* 3596 */     catch (NullPointerException exc) {
/*      */       
/* 3598 */       if (isDetached())
/*      */       {
/* 3600 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3602 */       throw exc;
/*      */     }
/* 3604 */     catch (Exception exc) {
/*      */       
/* 3606 */       if (!isDetached())
/*      */       {
/* 3608 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3610 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean insertsAreDetected(int a) throws SQLException {
/*      */     try {
/* 3618 */       return this.inner.insertsAreDetected(a);
/*      */     }
/* 3620 */     catch (NullPointerException exc) {
/*      */       
/* 3622 */       if (isDetached())
/*      */       {
/* 3624 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3626 */       throw exc;
/*      */     }
/* 3628 */     catch (Exception exc) {
/*      */       
/* 3630 */       if (!isDetached())
/*      */       {
/* 3632 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3634 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsBatchUpdates() throws SQLException {
/*      */     try {
/* 3642 */       return this.inner.supportsBatchUpdates();
/*      */     }
/* 3644 */     catch (NullPointerException exc) {
/*      */       
/* 3646 */       if (isDetached())
/*      */       {
/* 3648 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3650 */       throw exc;
/*      */     }
/* 3652 */     catch (Exception exc) {
/*      */       
/* 3654 */       if (!isDetached())
/*      */       {
/* 3656 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3658 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getUDTs(String a, String b, String c, int[] d) throws SQLException {
/*      */     try {
/* 3666 */       ResultSet innerResultSet = this.inner.getUDTs(a, b, c, d);
/* 3667 */       if (innerResultSet == null) return null; 
/* 3668 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3670 */     catch (NullPointerException exc) {
/*      */       
/* 3672 */       if (isDetached())
/*      */       {
/* 3674 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3676 */       throw exc;
/*      */     }
/* 3678 */     catch (Exception exc) {
/*      */       
/* 3680 */       if (!isDetached())
/*      */       {
/* 3682 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3684 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsSavepoints() throws SQLException {
/*      */     try {
/* 3692 */       return this.inner.supportsSavepoints();
/*      */     }
/* 3694 */     catch (NullPointerException exc) {
/*      */       
/* 3696 */       if (isDetached())
/*      */       {
/* 3698 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3700 */       throw exc;
/*      */     }
/* 3702 */     catch (Exception exc) {
/*      */       
/* 3704 */       if (!isDetached())
/*      */       {
/* 3706 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3708 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsNamedParameters() throws SQLException {
/*      */     try {
/* 3716 */       return this.inner.supportsNamedParameters();
/*      */     }
/* 3718 */     catch (NullPointerException exc) {
/*      */       
/* 3720 */       if (isDetached())
/*      */       {
/* 3722 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3724 */       throw exc;
/*      */     }
/* 3726 */     catch (Exception exc) {
/*      */       
/* 3728 */       if (!isDetached())
/*      */       {
/* 3730 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3732 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsMultipleOpenResults() throws SQLException {
/*      */     try {
/* 3740 */       return this.inner.supportsMultipleOpenResults();
/*      */     }
/* 3742 */     catch (NullPointerException exc) {
/*      */       
/* 3744 */       if (isDetached())
/*      */       {
/* 3746 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3748 */       throw exc;
/*      */     }
/* 3750 */     catch (Exception exc) {
/*      */       
/* 3752 */       if (!isDetached())
/*      */       {
/* 3754 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3756 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsGetGeneratedKeys() throws SQLException {
/*      */     try {
/* 3764 */       return this.inner.supportsGetGeneratedKeys();
/*      */     }
/* 3766 */     catch (NullPointerException exc) {
/*      */       
/* 3768 */       if (isDetached())
/*      */       {
/* 3770 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3772 */       throw exc;
/*      */     }
/* 3774 */     catch (Exception exc) {
/*      */       
/* 3776 */       if (!isDetached())
/*      */       {
/* 3778 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3780 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getSuperTypes(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3788 */       ResultSet innerResultSet = this.inner.getSuperTypes(a, b, c);
/* 3789 */       if (innerResultSet == null) return null; 
/* 3790 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3792 */     catch (NullPointerException exc) {
/*      */       
/* 3794 */       if (isDetached())
/*      */       {
/* 3796 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3798 */       throw exc;
/*      */     }
/* 3800 */     catch (Exception exc) {
/*      */       
/* 3802 */       if (!isDetached())
/*      */       {
/* 3804 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3806 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getSuperTables(String a, String b, String c) throws SQLException {
/*      */     try {
/* 3814 */       ResultSet innerResultSet = this.inner.getSuperTables(a, b, c);
/* 3815 */       if (innerResultSet == null) return null; 
/* 3816 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 3818 */     catch (NullPointerException exc) {
/*      */       
/* 3820 */       if (isDetached())
/*      */       {
/* 3822 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3824 */       throw exc;
/*      */     }
/* 3826 */     catch (Exception exc) {
/*      */       
/* 3828 */       if (!isDetached())
/*      */       {
/* 3830 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3832 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsResultSetHoldability(int a) throws SQLException {
/*      */     try {
/* 3840 */       return this.inner.supportsResultSetHoldability(a);
/*      */     }
/* 3842 */     catch (NullPointerException exc) {
/*      */       
/* 3844 */       if (isDetached())
/*      */       {
/* 3846 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3848 */       throw exc;
/*      */     }
/* 3850 */     catch (Exception exc) {
/*      */       
/* 3852 */       if (!isDetached())
/*      */       {
/* 3854 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3856 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDatabaseMajorVersion() throws SQLException {
/*      */     try {
/* 3864 */       return this.inner.getDatabaseMajorVersion();
/*      */     }
/* 3866 */     catch (NullPointerException exc) {
/*      */       
/* 3868 */       if (isDetached())
/*      */       {
/* 3870 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3872 */       throw exc;
/*      */     }
/* 3874 */     catch (Exception exc) {
/*      */       
/* 3876 */       if (!isDetached())
/*      */       {
/* 3878 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3880 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getDatabaseMinorVersion() throws SQLException {
/*      */     try {
/* 3888 */       return this.inner.getDatabaseMinorVersion();
/*      */     }
/* 3890 */     catch (NullPointerException exc) {
/*      */       
/* 3892 */       if (isDetached())
/*      */       {
/* 3894 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3896 */       throw exc;
/*      */     }
/* 3898 */     catch (Exception exc) {
/*      */       
/* 3900 */       if (!isDetached())
/*      */       {
/* 3902 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3904 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getJDBCMajorVersion() throws SQLException {
/*      */     try {
/* 3912 */       return this.inner.getJDBCMajorVersion();
/*      */     }
/* 3914 */     catch (NullPointerException exc) {
/*      */       
/* 3916 */       if (isDetached())
/*      */       {
/* 3918 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3920 */       throw exc;
/*      */     }
/* 3922 */     catch (Exception exc) {
/*      */       
/* 3924 */       if (!isDetached())
/*      */       {
/* 3926 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3928 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getJDBCMinorVersion() throws SQLException {
/*      */     try {
/* 3936 */       return this.inner.getJDBCMinorVersion();
/*      */     }
/* 3938 */     catch (NullPointerException exc) {
/*      */       
/* 3940 */       if (isDetached())
/*      */       {
/* 3942 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3944 */       throw exc;
/*      */     }
/* 3946 */     catch (Exception exc) {
/*      */       
/* 3948 */       if (!isDetached())
/*      */       {
/* 3950 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3952 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getSQLStateType() throws SQLException {
/*      */     try {
/* 3960 */       return this.inner.getSQLStateType();
/*      */     }
/* 3962 */     catch (NullPointerException exc) {
/*      */       
/* 3964 */       if (isDetached())
/*      */       {
/* 3966 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3968 */       throw exc;
/*      */     }
/* 3970 */     catch (Exception exc) {
/*      */       
/* 3972 */       if (!isDetached())
/*      */       {
/* 3974 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3976 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean locatorsUpdateCopy() throws SQLException {
/*      */     try {
/* 3984 */       return this.inner.locatorsUpdateCopy();
/*      */     }
/* 3986 */     catch (NullPointerException exc) {
/*      */       
/* 3988 */       if (isDetached())
/*      */       {
/* 3990 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 3992 */       throw exc;
/*      */     }
/* 3994 */     catch (Exception exc) {
/*      */       
/* 3996 */       if (!isDetached())
/*      */       {
/* 3998 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4000 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsStatementPooling() throws SQLException {
/*      */     try {
/* 4008 */       return this.inner.supportsStatementPooling();
/*      */     }
/* 4010 */     catch (NullPointerException exc) {
/*      */       
/* 4012 */       if (isDetached())
/*      */       {
/* 4014 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4016 */       throw exc;
/*      */     }
/* 4018 */     catch (Exception exc) {
/*      */       
/* 4020 */       if (!isDetached())
/*      */       {
/* 4022 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4024 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final RowIdLifetime getRowIdLifetime() throws SQLException {
/*      */     try {
/* 4032 */       return this.inner.getRowIdLifetime();
/*      */     }
/* 4034 */     catch (NullPointerException exc) {
/*      */       
/* 4036 */       if (isDetached())
/*      */       {
/* 4038 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4040 */       throw exc;
/*      */     }
/* 4042 */     catch (Exception exc) {
/*      */       
/* 4044 */       if (!isDetached())
/*      */       {
/* 4046 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4048 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/*      */     try {
/* 4056 */       return this.inner.supportsStoredFunctionsUsingCallSyntax();
/*      */     }
/* 4058 */     catch (NullPointerException exc) {
/*      */       
/* 4060 */       if (isDetached())
/*      */       {
/* 4062 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4064 */       throw exc;
/*      */     }
/* 4066 */     catch (Exception exc) {
/*      */       
/* 4068 */       if (!isDetached())
/*      */       {
/* 4070 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4072 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean autoCommitFailureClosesAllResultSets() throws SQLException {
/*      */     try {
/* 4080 */       return this.inner.autoCommitFailureClosesAllResultSets();
/*      */     }
/* 4082 */     catch (NullPointerException exc) {
/*      */       
/* 4084 */       if (isDetached())
/*      */       {
/* 4086 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4088 */       throw exc;
/*      */     }
/* 4090 */     catch (Exception exc) {
/*      */       
/* 4092 */       if (!isDetached())
/*      */       {
/* 4094 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4096 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getClientInfoProperties() throws SQLException {
/*      */     try {
/* 4104 */       ResultSet innerResultSet = this.inner.getClientInfoProperties();
/* 4105 */       if (innerResultSet == null) return null; 
/* 4106 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 4108 */     catch (NullPointerException exc) {
/*      */       
/* 4110 */       if (isDetached())
/*      */       {
/* 4112 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4114 */       throw exc;
/*      */     }
/* 4116 */     catch (Exception exc) {
/*      */       
/* 4118 */       if (!isDetached())
/*      */       {
/* 4120 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4122 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getFunctions(String a, String b, String c) throws SQLException {
/*      */     try {
/* 4130 */       ResultSet innerResultSet = this.inner.getFunctions(a, b, c);
/* 4131 */       if (innerResultSet == null) return null; 
/* 4132 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 4134 */     catch (NullPointerException exc) {
/*      */       
/* 4136 */       if (isDetached())
/*      */       {
/* 4138 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4140 */       throw exc;
/*      */     }
/* 4142 */     catch (Exception exc) {
/*      */       
/* 4144 */       if (!isDetached())
/*      */       {
/* 4146 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4148 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException {
/*      */     try {
/* 4156 */       ResultSet innerResultSet = this.inner.getFunctionColumns(a, b, c, d);
/* 4157 */       if (innerResultSet == null) return null; 
/* 4158 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 4160 */     catch (NullPointerException exc) {
/*      */       
/* 4162 */       if (isDetached())
/*      */       {
/* 4164 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4166 */       throw exc;
/*      */     }
/* 4168 */     catch (Exception exc) {
/*      */       
/* 4170 */       if (!isDetached())
/*      */       {
/* 4172 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4174 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getPseudoColumns(String a, String b, String c, String d) throws SQLException {
/*      */     try {
/* 4182 */       ResultSet innerResultSet = this.inner.getPseudoColumns(a, b, c, d);
/* 4183 */       if (innerResultSet == null) return null; 
/* 4184 */       return new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     }
/* 4186 */     catch (NullPointerException exc) {
/*      */       
/* 4188 */       if (isDetached())
/*      */       {
/* 4190 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4192 */       throw exc;
/*      */     }
/* 4194 */     catch (Exception exc) {
/*      */       
/* 4196 */       if (!isDetached())
/*      */       {
/* 4198 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4200 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean generatedKeyAlwaysReturned() throws SQLException {
/*      */     try {
/* 4208 */       return this.inner.generatedKeyAlwaysReturned();
/*      */     }
/* 4210 */     catch (NullPointerException exc) {
/*      */       
/* 4212 */       if (isDetached())
/*      */       {
/* 4214 */         throw SqlUtils.toSQLException("You can't operate on a closed DatabaseMetaData!!!", exc);
/*      */       }
/* 4216 */       throw exc;
/*      */     }
/* 4218 */     catch (Exception exc) {
/*      */       
/* 4220 */       if (!isDetached())
/*      */       {
/* 4222 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4224 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object unwrap(Class a) throws SQLException {
/* 4230 */     if (isWrapperFor(a)) return this.inner; 
/* 4231 */     throw new SQLException(this + " is not a wrapper for " + a.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWrapperFor(Class<DatabaseMetaData> a) throws SQLException {
/* 4236 */     return (DatabaseMetaData.class == a || a.isAssignableFrom(this.inner.getClass()));
/*      */   }
/*      */   
/* 4239 */   private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyDatabaseMetaData");
/*      */   
/*      */   volatile NewPooledConnection parentPooledConnection;
/*      */   
/* 4243 */   ConnectionEventListener cel = new ConnectionEventListener()
/*      */     {
/*      */       public void connectionErrorOccurred(ConnectionEvent evt) {}
/*      */ 
/*      */       
/*      */       public void connectionClosed(ConnectionEvent evt) {
/* 4249 */         NewProxyDatabaseMetaData.this.detach();
/*      */       }
/*      */     };
/*      */   
/*      */   void attach(NewPooledConnection parentPooledConnection) {
/* 4254 */     this.parentPooledConnection = parentPooledConnection;
/* 4255 */     parentPooledConnection.addConnectionEventListener(this.cel);
/*      */   }
/*      */   NewProxyConnection proxyCon;
/*      */   
/*      */   private void detach() {
/* 4260 */     this.parentPooledConnection.removeConnectionEventListener(this.cel);
/* 4261 */     this.parentPooledConnection = null;
/*      */   }
/*      */ 
/*      */   
/*      */   NewProxyDatabaseMetaData(DatabaseMetaData inner, NewPooledConnection parentPooledConnection) {
/* 4266 */     this(inner);
/* 4267 */     attach(parentPooledConnection);
/*      */   }
/*      */   
/*      */   boolean isDetached() {
/* 4271 */     return (this.parentPooledConnection == null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   NewProxyDatabaseMetaData(DatabaseMetaData inner, NewPooledConnection parentPooledConnection, NewProxyConnection proxyCon) {
/* 4277 */     this(inner, parentPooledConnection);
/* 4278 */     this.proxyCon = proxyCon;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewProxyDatabaseMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */