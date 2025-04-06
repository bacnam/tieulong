/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v2.c3p0.C3P0ProxyStatement;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
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
/*      */ public final class NewProxyPreparedStatement
/*      */   implements PreparedStatement, C3P0ProxyStatement, ProxyResultSetDetachable
/*      */ {
/*      */   protected PreparedStatement inner;
/*      */   
/*      */   private void __setInner(PreparedStatement inner) {
/*   54 */     this.inner = inner;
/*      */   }
/*      */   
/*      */   NewProxyPreparedStatement(PreparedStatement inner) {
/*   58 */     __setInner(inner);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setBoolean(int a, boolean b) throws SQLException {
/*      */     try {
/*   64 */       maybeDirtyTransaction();
/*      */       
/*   66 */       this.inner.setBoolean(a, b);
/*      */     }
/*   68 */     catch (NullPointerException exc) {
/*      */       
/*   70 */       if (isDetached())
/*      */       {
/*   72 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void setByte(int a, byte b) throws SQLException {
/*      */     try {
/*   90 */       maybeDirtyTransaction();
/*      */       
/*   92 */       this.inner.setByte(a, b);
/*      */     }
/*   94 */     catch (NullPointerException exc) {
/*      */       
/*   96 */       if (isDetached())
/*      */       {
/*   98 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  100 */       throw exc;
/*      */     }
/*  102 */     catch (Exception exc) {
/*      */       
/*  104 */       if (!isDetached())
/*      */       {
/*  106 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  108 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setShort(int a, short b) throws SQLException {
/*      */     try {
/*  116 */       maybeDirtyTransaction();
/*      */       
/*  118 */       this.inner.setShort(a, b);
/*      */     }
/*  120 */     catch (NullPointerException exc) {
/*      */       
/*  122 */       if (isDetached())
/*      */       {
/*  124 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  126 */       throw exc;
/*      */     }
/*  128 */     catch (Exception exc) {
/*      */       
/*  130 */       if (!isDetached())
/*      */       {
/*  132 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  134 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setInt(int a, int b) throws SQLException {
/*      */     try {
/*  142 */       maybeDirtyTransaction();
/*      */       
/*  144 */       this.inner.setInt(a, b);
/*      */     }
/*  146 */     catch (NullPointerException exc) {
/*      */       
/*  148 */       if (isDetached())
/*      */       {
/*  150 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  152 */       throw exc;
/*      */     }
/*  154 */     catch (Exception exc) {
/*      */       
/*  156 */       if (!isDetached())
/*      */       {
/*  158 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  160 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLong(int a, long b) throws SQLException {
/*      */     try {
/*  168 */       maybeDirtyTransaction();
/*      */       
/*  170 */       this.inner.setLong(a, b);
/*      */     }
/*  172 */     catch (NullPointerException exc) {
/*      */       
/*  174 */       if (isDetached())
/*      */       {
/*  176 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  178 */       throw exc;
/*      */     }
/*  180 */     catch (Exception exc) {
/*      */       
/*  182 */       if (!isDetached())
/*      */       {
/*  184 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  186 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFloat(int a, float b) throws SQLException {
/*      */     try {
/*  194 */       maybeDirtyTransaction();
/*      */       
/*  196 */       this.inner.setFloat(a, b);
/*      */     }
/*  198 */     catch (NullPointerException exc) {
/*      */       
/*  200 */       if (isDetached())
/*      */       {
/*  202 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  204 */       throw exc;
/*      */     }
/*  206 */     catch (Exception exc) {
/*      */       
/*  208 */       if (!isDetached())
/*      */       {
/*  210 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  212 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDouble(int a, double b) throws SQLException {
/*      */     try {
/*  220 */       maybeDirtyTransaction();
/*      */       
/*  222 */       this.inner.setDouble(a, b);
/*      */     }
/*  224 */     catch (NullPointerException exc) {
/*      */       
/*  226 */       if (isDetached())
/*      */       {
/*  228 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  230 */       throw exc;
/*      */     }
/*  232 */     catch (Exception exc) {
/*      */       
/*  234 */       if (!isDetached())
/*      */       {
/*  236 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  238 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTimestamp(int a, Timestamp b) throws SQLException {
/*      */     try {
/*  246 */       maybeDirtyTransaction();
/*      */       
/*  248 */       this.inner.setTimestamp(a, b);
/*      */     }
/*  250 */     catch (NullPointerException exc) {
/*      */       
/*  252 */       if (isDetached())
/*      */       {
/*  254 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  256 */       throw exc;
/*      */     }
/*  258 */     catch (Exception exc) {
/*      */       
/*  260 */       if (!isDetached())
/*      */       {
/*  262 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  264 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTimestamp(int a, Timestamp b, Calendar c) throws SQLException {
/*      */     try {
/*  272 */       maybeDirtyTransaction();
/*      */       
/*  274 */       this.inner.setTimestamp(a, b, c);
/*      */     }
/*  276 */     catch (NullPointerException exc) {
/*      */       
/*  278 */       if (isDetached())
/*      */       {
/*  280 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  282 */       throw exc;
/*      */     }
/*  284 */     catch (Exception exc) {
/*      */       
/*  286 */       if (!isDetached())
/*      */       {
/*  288 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  290 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setURL(int a, URL b) throws SQLException {
/*      */     try {
/*  298 */       maybeDirtyTransaction();
/*      */       
/*  300 */       this.inner.setURL(a, b);
/*      */     }
/*  302 */     catch (NullPointerException exc) {
/*      */       
/*  304 */       if (isDetached())
/*      */       {
/*  306 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  308 */       throw exc;
/*      */     }
/*  310 */     catch (Exception exc) {
/*      */       
/*  312 */       if (!isDetached())
/*      */       {
/*  314 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  316 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTime(int a, Time b, Calendar c) throws SQLException {
/*      */     try {
/*  324 */       maybeDirtyTransaction();
/*      */       
/*  326 */       this.inner.setTime(a, b, c);
/*      */     }
/*  328 */     catch (NullPointerException exc) {
/*      */       
/*  330 */       if (isDetached())
/*      */       {
/*  332 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  334 */       throw exc;
/*      */     }
/*  336 */     catch (Exception exc) {
/*      */       
/*  338 */       if (!isDetached())
/*      */       {
/*  340 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  342 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTime(int a, Time b) throws SQLException {
/*      */     try {
/*  350 */       maybeDirtyTransaction();
/*      */       
/*  352 */       this.inner.setTime(a, b);
/*      */     }
/*  354 */     catch (NullPointerException exc) {
/*      */       
/*  356 */       if (isDetached())
/*      */       {
/*  358 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  360 */       throw exc;
/*      */     }
/*  362 */     catch (Exception exc) {
/*      */       
/*  364 */       if (!isDetached())
/*      */       {
/*  366 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  368 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSetMetaData getMetaData() throws SQLException {
/*      */     try {
/*  376 */       maybeDirtyTransaction();
/*      */       
/*  378 */       return this.inner.getMetaData();
/*      */     }
/*  380 */     catch (NullPointerException exc) {
/*      */       
/*  382 */       if (isDetached())
/*      */       {
/*  384 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  386 */       throw exc;
/*      */     }
/*  388 */     catch (Exception exc) {
/*      */       
/*  390 */       if (!isDetached())
/*      */       {
/*  392 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  394 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute() throws SQLException {
/*      */     try {
/*  402 */       maybeDirtyTransaction();
/*      */       
/*  404 */       return this.inner.execute();
/*      */     }
/*  406 */     catch (NullPointerException exc) {
/*      */       
/*  408 */       if (isDetached())
/*      */       {
/*  410 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   
/*      */   public final ResultSet executeQuery() throws SQLException {
/*      */     try {
/*  428 */       maybeDirtyTransaction();
/*      */       
/*  430 */       ResultSet innerResultSet = this.inner.executeQuery();
/*  431 */       if (innerResultSet == null) return null; 
/*  432 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/*  433 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*  434 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/*  435 */        return out;
/*      */     }
/*  437 */     catch (NullPointerException exc) {
/*      */       
/*  439 */       if (isDetached())
/*      */       {
/*  441 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  443 */       throw exc;
/*      */     }
/*  445 */     catch (Exception exc) {
/*      */       
/*  447 */       if (!isDetached())
/*      */       {
/*  449 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  451 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate() throws SQLException {
/*      */     try {
/*  459 */       maybeDirtyTransaction();
/*      */       
/*  461 */       return this.inner.executeUpdate();
/*      */     }
/*  463 */     catch (NullPointerException exc) {
/*      */       
/*  465 */       if (isDetached())
/*      */       {
/*  467 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  469 */       throw exc;
/*      */     }
/*  471 */     catch (Exception exc) {
/*      */       
/*  473 */       if (!isDetached())
/*      */       {
/*  475 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  477 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addBatch() throws SQLException {
/*      */     try {
/*  485 */       maybeDirtyTransaction();
/*      */       
/*  487 */       this.inner.addBatch();
/*      */     }
/*  489 */     catch (NullPointerException exc) {
/*      */       
/*  491 */       if (isDetached())
/*      */       {
/*  493 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  495 */       throw exc;
/*      */     }
/*  497 */     catch (Exception exc) {
/*      */       
/*  499 */       if (!isDetached())
/*      */       {
/*  501 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  503 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNull(int a, int b) throws SQLException {
/*      */     try {
/*  511 */       maybeDirtyTransaction();
/*      */       
/*  513 */       this.inner.setNull(a, b);
/*      */     }
/*  515 */     catch (NullPointerException exc) {
/*      */       
/*  517 */       if (isDetached())
/*      */       {
/*  519 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  521 */       throw exc;
/*      */     }
/*  523 */     catch (Exception exc) {
/*      */       
/*  525 */       if (!isDetached())
/*      */       {
/*  527 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  529 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNull(int a, int b, String c) throws SQLException {
/*      */     try {
/*  537 */       maybeDirtyTransaction();
/*      */       
/*  539 */       this.inner.setNull(a, b, c);
/*      */     }
/*  541 */     catch (NullPointerException exc) {
/*      */       
/*  543 */       if (isDetached())
/*      */       {
/*  545 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  547 */       throw exc;
/*      */     }
/*  549 */     catch (Exception exc) {
/*      */       
/*  551 */       if (!isDetached())
/*      */       {
/*  553 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  555 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBigDecimal(int a, BigDecimal b) throws SQLException {
/*      */     try {
/*  563 */       maybeDirtyTransaction();
/*      */       
/*  565 */       this.inner.setBigDecimal(a, b);
/*      */     }
/*  567 */     catch (NullPointerException exc) {
/*      */       
/*  569 */       if (isDetached())
/*      */       {
/*  571 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  573 */       throw exc;
/*      */     }
/*  575 */     catch (Exception exc) {
/*      */       
/*  577 */       if (!isDetached())
/*      */       {
/*  579 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  581 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setString(int a, String b) throws SQLException {
/*      */     try {
/*  589 */       maybeDirtyTransaction();
/*      */       
/*  591 */       this.inner.setString(a, b);
/*      */     }
/*  593 */     catch (NullPointerException exc) {
/*      */       
/*  595 */       if (isDetached())
/*      */       {
/*  597 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  599 */       throw exc;
/*      */     }
/*  601 */     catch (Exception exc) {
/*      */       
/*  603 */       if (!isDetached())
/*      */       {
/*  605 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  607 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBytes(int a, byte[] b) throws SQLException {
/*      */     try {
/*  615 */       maybeDirtyTransaction();
/*      */       
/*  617 */       this.inner.setBytes(a, b);
/*      */     }
/*  619 */     catch (NullPointerException exc) {
/*      */       
/*  621 */       if (isDetached())
/*      */       {
/*  623 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  625 */       throw exc;
/*      */     }
/*  627 */     catch (Exception exc) {
/*      */       
/*  629 */       if (!isDetached())
/*      */       {
/*  631 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  633 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDate(int a, Date b) throws SQLException {
/*      */     try {
/*  641 */       maybeDirtyTransaction();
/*      */       
/*  643 */       this.inner.setDate(a, b);
/*      */     }
/*  645 */     catch (NullPointerException exc) {
/*      */       
/*  647 */       if (isDetached())
/*      */       {
/*  649 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  651 */       throw exc;
/*      */     }
/*  653 */     catch (Exception exc) {
/*      */       
/*  655 */       if (!isDetached())
/*      */       {
/*  657 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  659 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDate(int a, Date b, Calendar c) throws SQLException {
/*      */     try {
/*  667 */       maybeDirtyTransaction();
/*      */       
/*  669 */       this.inner.setDate(a, b, c);
/*      */     }
/*  671 */     catch (NullPointerException exc) {
/*      */       
/*  673 */       if (isDetached())
/*      */       {
/*  675 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  677 */       throw exc;
/*      */     }
/*  679 */     catch (Exception exc) {
/*      */       
/*  681 */       if (!isDetached())
/*      */       {
/*  683 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  685 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(int a, InputStream b) throws SQLException {
/*      */     try {
/*  693 */       maybeDirtyTransaction();
/*      */       
/*  695 */       this.inner.setAsciiStream(a, b);
/*      */     }
/*  697 */     catch (NullPointerException exc) {
/*      */       
/*  699 */       if (isDetached())
/*      */       {
/*  701 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  703 */       throw exc;
/*      */     }
/*  705 */     catch (Exception exc) {
/*      */       
/*  707 */       if (!isDetached())
/*      */       {
/*  709 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  711 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/*  719 */       maybeDirtyTransaction();
/*      */       
/*  721 */       this.inner.setAsciiStream(a, b, c);
/*      */     }
/*  723 */     catch (NullPointerException exc) {
/*      */       
/*  725 */       if (isDetached())
/*      */       {
/*  727 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  729 */       throw exc;
/*      */     }
/*  731 */     catch (Exception exc) {
/*      */       
/*  733 */       if (!isDetached())
/*      */       {
/*  735 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  737 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/*  745 */       maybeDirtyTransaction();
/*      */       
/*  747 */       this.inner.setAsciiStream(a, b, c);
/*      */     }
/*  749 */     catch (NullPointerException exc) {
/*      */       
/*  751 */       if (isDetached())
/*      */       {
/*  753 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  755 */       throw exc;
/*      */     }
/*  757 */     catch (Exception exc) {
/*      */       
/*  759 */       if (!isDetached())
/*      */       {
/*  761 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  763 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setUnicodeStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/*  771 */       maybeDirtyTransaction();
/*      */       
/*  773 */       this.inner.setUnicodeStream(a, b, c);
/*      */     }
/*  775 */     catch (NullPointerException exc) {
/*      */       
/*  777 */       if (isDetached())
/*      */       {
/*  779 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  781 */       throw exc;
/*      */     }
/*  783 */     catch (Exception exc) {
/*      */       
/*  785 */       if (!isDetached())
/*      */       {
/*  787 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  789 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/*  797 */       maybeDirtyTransaction();
/*      */       
/*  799 */       this.inner.setBinaryStream(a, b, c);
/*      */     }
/*  801 */     catch (NullPointerException exc) {
/*      */       
/*  803 */       if (isDetached())
/*      */       {
/*  805 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  807 */       throw exc;
/*      */     }
/*  809 */     catch (Exception exc) {
/*      */       
/*  811 */       if (!isDetached())
/*      */       {
/*  813 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  815 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/*  823 */       maybeDirtyTransaction();
/*      */       
/*  825 */       this.inner.setBinaryStream(a, b, c);
/*      */     }
/*  827 */     catch (NullPointerException exc) {
/*      */       
/*  829 */       if (isDetached())
/*      */       {
/*  831 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  833 */       throw exc;
/*      */     }
/*  835 */     catch (Exception exc) {
/*      */       
/*  837 */       if (!isDetached())
/*      */       {
/*  839 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  841 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(int a, InputStream b) throws SQLException {
/*      */     try {
/*  849 */       maybeDirtyTransaction();
/*      */       
/*  851 */       this.inner.setBinaryStream(a, b);
/*      */     }
/*  853 */     catch (NullPointerException exc) {
/*      */       
/*  855 */       if (isDetached())
/*      */       {
/*  857 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  859 */       throw exc;
/*      */     }
/*  861 */     catch (Exception exc) {
/*      */       
/*  863 */       if (!isDetached())
/*      */       {
/*  865 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  867 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearParameters() throws SQLException {
/*      */     try {
/*  875 */       maybeDirtyTransaction();
/*      */       
/*  877 */       this.inner.clearParameters();
/*      */     }
/*  879 */     catch (NullPointerException exc) {
/*      */       
/*  881 */       if (isDetached())
/*      */       {
/*  883 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  885 */       throw exc;
/*      */     }
/*  887 */     catch (Exception exc) {
/*      */       
/*  889 */       if (!isDetached())
/*      */       {
/*  891 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  893 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(int a, Object b, int c, int d) throws SQLException {
/*      */     try {
/*  901 */       maybeDirtyTransaction();
/*      */       
/*  903 */       this.inner.setObject(a, b, c, d);
/*      */     }
/*  905 */     catch (NullPointerException exc) {
/*      */       
/*  907 */       if (isDetached())
/*      */       {
/*  909 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  911 */       throw exc;
/*      */     }
/*  913 */     catch (Exception exc) {
/*      */       
/*  915 */       if (!isDetached())
/*      */       {
/*  917 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  919 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(int a, Object b, int c) throws SQLException {
/*      */     try {
/*  927 */       maybeDirtyTransaction();
/*      */       
/*  929 */       this.inner.setObject(a, b, c);
/*      */     }
/*  931 */     catch (NullPointerException exc) {
/*      */       
/*  933 */       if (isDetached())
/*      */       {
/*  935 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  937 */       throw exc;
/*      */     }
/*  939 */     catch (Exception exc) {
/*      */       
/*  941 */       if (!isDetached())
/*      */       {
/*  943 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  945 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(int a, Object b) throws SQLException {
/*      */     try {
/*  953 */       maybeDirtyTransaction();
/*      */       
/*  955 */       this.inner.setObject(a, b);
/*      */     }
/*  957 */     catch (NullPointerException exc) {
/*      */       
/*  959 */       if (isDetached())
/*      */       {
/*  961 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  963 */       throw exc;
/*      */     }
/*  965 */     catch (Exception exc) {
/*      */       
/*  967 */       if (!isDetached())
/*      */       {
/*  969 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  971 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(int a, Reader b, long c) throws SQLException {
/*      */     try {
/*  979 */       maybeDirtyTransaction();
/*      */       
/*  981 */       this.inner.setCharacterStream(a, b, c);
/*      */     }
/*  983 */     catch (NullPointerException exc) {
/*      */       
/*  985 */       if (isDetached())
/*      */       {
/*  987 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  989 */       throw exc;
/*      */     }
/*  991 */     catch (Exception exc) {
/*      */       
/*  993 */       if (!isDetached())
/*      */       {
/*  995 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  997 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(int a, Reader b, int c) throws SQLException {
/*      */     try {
/* 1005 */       maybeDirtyTransaction();
/*      */       
/* 1007 */       this.inner.setCharacterStream(a, b, c);
/*      */     }
/* 1009 */     catch (NullPointerException exc) {
/*      */       
/* 1011 */       if (isDetached())
/*      */       {
/* 1013 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1015 */       throw exc;
/*      */     }
/* 1017 */     catch (Exception exc) {
/*      */       
/* 1019 */       if (!isDetached())
/*      */       {
/* 1021 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1023 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(int a, Reader b) throws SQLException {
/*      */     try {
/* 1031 */       maybeDirtyTransaction();
/*      */       
/* 1033 */       this.inner.setCharacterStream(a, b);
/*      */     }
/* 1035 */     catch (NullPointerException exc) {
/*      */       
/* 1037 */       if (isDetached())
/*      */       {
/* 1039 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1041 */       throw exc;
/*      */     }
/* 1043 */     catch (Exception exc) {
/*      */       
/* 1045 */       if (!isDetached())
/*      */       {
/* 1047 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1049 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRef(int a, Ref b) throws SQLException {
/*      */     try {
/* 1057 */       maybeDirtyTransaction();
/*      */       
/* 1059 */       this.inner.setRef(a, b);
/*      */     }
/* 1061 */     catch (NullPointerException exc) {
/*      */       
/* 1063 */       if (isDetached())
/*      */       {
/* 1065 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1067 */       throw exc;
/*      */     }
/* 1069 */     catch (Exception exc) {
/*      */       
/* 1071 */       if (!isDetached())
/*      */       {
/* 1073 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1075 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 1083 */       maybeDirtyTransaction();
/*      */       
/* 1085 */       this.inner.setBlob(a, b, c);
/*      */     }
/* 1087 */     catch (NullPointerException exc) {
/*      */       
/* 1089 */       if (isDetached())
/*      */       {
/* 1091 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1093 */       throw exc;
/*      */     }
/* 1095 */     catch (Exception exc) {
/*      */       
/* 1097 */       if (!isDetached())
/*      */       {
/* 1099 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1101 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(int a, InputStream b) throws SQLException {
/*      */     try {
/* 1109 */       maybeDirtyTransaction();
/*      */       
/* 1111 */       this.inner.setBlob(a, b);
/*      */     }
/* 1113 */     catch (NullPointerException exc) {
/*      */       
/* 1115 */       if (isDetached())
/*      */       {
/* 1117 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1119 */       throw exc;
/*      */     }
/* 1121 */     catch (Exception exc) {
/*      */       
/* 1123 */       if (!isDetached())
/*      */       {
/* 1125 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1127 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(int a, Blob b) throws SQLException {
/*      */     try {
/* 1135 */       maybeDirtyTransaction();
/*      */       
/* 1137 */       this.inner.setBlob(a, b);
/*      */     }
/* 1139 */     catch (NullPointerException exc) {
/*      */       
/* 1141 */       if (isDetached())
/*      */       {
/* 1143 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1145 */       throw exc;
/*      */     }
/* 1147 */     catch (Exception exc) {
/*      */       
/* 1149 */       if (!isDetached())
/*      */       {
/* 1151 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1153 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(int a, Reader b) throws SQLException {
/*      */     try {
/* 1161 */       maybeDirtyTransaction();
/*      */       
/* 1163 */       this.inner.setClob(a, b);
/*      */     }
/* 1165 */     catch (NullPointerException exc) {
/*      */       
/* 1167 */       if (isDetached())
/*      */       {
/* 1169 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1171 */       throw exc;
/*      */     }
/* 1173 */     catch (Exception exc) {
/*      */       
/* 1175 */       if (!isDetached())
/*      */       {
/* 1177 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1179 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 1187 */       maybeDirtyTransaction();
/*      */       
/* 1189 */       this.inner.setClob(a, b, c);
/*      */     }
/* 1191 */     catch (NullPointerException exc) {
/*      */       
/* 1193 */       if (isDetached())
/*      */       {
/* 1195 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1197 */       throw exc;
/*      */     }
/* 1199 */     catch (Exception exc) {
/*      */       
/* 1201 */       if (!isDetached())
/*      */       {
/* 1203 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1205 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(int a, Clob b) throws SQLException {
/*      */     try {
/* 1213 */       maybeDirtyTransaction();
/*      */       
/* 1215 */       this.inner.setClob(a, b);
/*      */     }
/* 1217 */     catch (NullPointerException exc) {
/*      */       
/* 1219 */       if (isDetached())
/*      */       {
/* 1221 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1223 */       throw exc;
/*      */     }
/* 1225 */     catch (Exception exc) {
/*      */       
/* 1227 */       if (!isDetached())
/*      */       {
/* 1229 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1231 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setArray(int a, Array b) throws SQLException {
/*      */     try {
/* 1239 */       maybeDirtyTransaction();
/*      */       
/* 1241 */       this.inner.setArray(a, b);
/*      */     }
/* 1243 */     catch (NullPointerException exc) {
/*      */       
/* 1245 */       if (isDetached())
/*      */       {
/* 1247 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1249 */       throw exc;
/*      */     }
/* 1251 */     catch (Exception exc) {
/*      */       
/* 1253 */       if (!isDetached())
/*      */       {
/* 1255 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1257 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParameterMetaData getParameterMetaData() throws SQLException {
/*      */     try {
/* 1265 */       maybeDirtyTransaction();
/*      */       
/* 1267 */       return this.inner.getParameterMetaData();
/*      */     }
/* 1269 */     catch (NullPointerException exc) {
/*      */       
/* 1271 */       if (isDetached())
/*      */       {
/* 1273 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1275 */       throw exc;
/*      */     }
/* 1277 */     catch (Exception exc) {
/*      */       
/* 1279 */       if (!isDetached())
/*      */       {
/* 1281 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1283 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRowId(int a, RowId b) throws SQLException {
/*      */     try {
/* 1291 */       maybeDirtyTransaction();
/*      */       
/* 1293 */       this.inner.setRowId(a, b);
/*      */     }
/* 1295 */     catch (NullPointerException exc) {
/*      */       
/* 1297 */       if (isDetached())
/*      */       {
/* 1299 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1301 */       throw exc;
/*      */     }
/* 1303 */     catch (Exception exc) {
/*      */       
/* 1305 */       if (!isDetached())
/*      */       {
/* 1307 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1309 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNString(int a, String b) throws SQLException {
/*      */     try {
/* 1317 */       maybeDirtyTransaction();
/*      */       
/* 1319 */       this.inner.setNString(a, b);
/*      */     }
/* 1321 */     catch (NullPointerException exc) {
/*      */       
/* 1323 */       if (isDetached())
/*      */       {
/* 1325 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1327 */       throw exc;
/*      */     }
/* 1329 */     catch (Exception exc) {
/*      */       
/* 1331 */       if (!isDetached())
/*      */       {
/* 1333 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1335 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNCharacterStream(int a, Reader b) throws SQLException {
/*      */     try {
/* 1343 */       maybeDirtyTransaction();
/*      */       
/* 1345 */       this.inner.setNCharacterStream(a, b);
/*      */     }
/* 1347 */     catch (NullPointerException exc) {
/*      */       
/* 1349 */       if (isDetached())
/*      */       {
/* 1351 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1353 */       throw exc;
/*      */     }
/* 1355 */     catch (Exception exc) {
/*      */       
/* 1357 */       if (!isDetached())
/*      */       {
/* 1359 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1361 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNCharacterStream(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 1369 */       maybeDirtyTransaction();
/*      */       
/* 1371 */       this.inner.setNCharacterStream(a, b, c);
/*      */     }
/* 1373 */     catch (NullPointerException exc) {
/*      */       
/* 1375 */       if (isDetached())
/*      */       {
/* 1377 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1379 */       throw exc;
/*      */     }
/* 1381 */     catch (Exception exc) {
/*      */       
/* 1383 */       if (!isDetached())
/*      */       {
/* 1385 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1387 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 1395 */       maybeDirtyTransaction();
/*      */       
/* 1397 */       this.inner.setNClob(a, b, c);
/*      */     }
/* 1399 */     catch (NullPointerException exc) {
/*      */       
/* 1401 */       if (isDetached())
/*      */       {
/* 1403 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1405 */       throw exc;
/*      */     }
/* 1407 */     catch (Exception exc) {
/*      */       
/* 1409 */       if (!isDetached())
/*      */       {
/* 1411 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1413 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(int a, NClob b) throws SQLException {
/*      */     try {
/* 1421 */       maybeDirtyTransaction();
/*      */       
/* 1423 */       this.inner.setNClob(a, b);
/*      */     }
/* 1425 */     catch (NullPointerException exc) {
/*      */       
/* 1427 */       if (isDetached())
/*      */       {
/* 1429 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1431 */       throw exc;
/*      */     }
/* 1433 */     catch (Exception exc) {
/*      */       
/* 1435 */       if (!isDetached())
/*      */       {
/* 1437 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1439 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(int a, Reader b) throws SQLException {
/*      */     try {
/* 1447 */       maybeDirtyTransaction();
/*      */       
/* 1449 */       this.inner.setNClob(a, b);
/*      */     }
/* 1451 */     catch (NullPointerException exc) {
/*      */       
/* 1453 */       if (isDetached())
/*      */       {
/* 1455 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1457 */       throw exc;
/*      */     }
/* 1459 */     catch (Exception exc) {
/*      */       
/* 1461 */       if (!isDetached())
/*      */       {
/* 1463 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1465 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSQLXML(int a, SQLXML b) throws SQLException {
/*      */     try {
/* 1473 */       maybeDirtyTransaction();
/*      */       
/* 1475 */       this.inner.setSQLXML(a, b);
/*      */     }
/* 1477 */     catch (NullPointerException exc) {
/*      */       
/* 1479 */       if (isDetached())
/*      */       {
/* 1481 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1483 */       throw exc;
/*      */     }
/* 1485 */     catch (Exception exc) {
/*      */       
/* 1487 */       if (!isDetached())
/*      */       {
/* 1489 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1491 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void close() throws SQLException {
/*      */     try {
/* 1499 */       maybeDirtyTransaction();
/*      */       
/* 1501 */       if (!isDetached())
/*      */       {
/* 1503 */         synchronized (this.myProxyResultSets) {
/*      */           
/* 1505 */           for (Iterator<ResultSet> ii = this.myProxyResultSets.iterator(); ii.hasNext(); ) {
/*      */             
/* 1507 */             ResultSet closeMe = ii.next();
/* 1508 */             ii.remove();
/*      */             try {
/* 1510 */               closeMe.close();
/* 1511 */             } catch (SQLException e) {
/*      */               
/* 1513 */               if (logger.isLoggable(MLevel.WARNING))
/* 1514 */                 logger.log(MLevel.WARNING, "Exception on close of apparently orphaned ResultSet.", e); 
/*      */             } 
/* 1516 */             if (logger.isLoggable(MLevel.FINE)) {
/* 1517 */               logger.log(MLevel.FINE, this + " closed orphaned ResultSet: " + closeMe);
/*      */             }
/*      */           } 
/*      */         } 
/* 1521 */         if (this.is_cached) {
/* 1522 */           this.parentPooledConnection.checkinStatement(this.inner);
/*      */         } else {
/*      */           
/* 1525 */           this.parentPooledConnection.markInactiveUncachedStatement(this.inner); try {
/* 1526 */             this.inner.close();
/* 1527 */           } catch (Exception e) {
/*      */             
/* 1529 */             if (logger.isLoggable(MLevel.WARNING))
/* 1530 */               logger.log(MLevel.WARNING, "Exception on close of inner statement.", e); 
/* 1531 */             SQLException sqle = SqlUtils.toSQLException(e);
/* 1532 */             throw sqle;
/*      */           } 
/*      */         } 
/*      */         
/* 1536 */         detach();
/* 1537 */         this.inner = null;
/* 1538 */         this.creatorProxy = null;
/*      */       }
/*      */     
/* 1541 */     } catch (NullPointerException exc) {
/*      */       
/* 1543 */       if (isDetached()) {
/*      */         
/* 1545 */         if (logger.isLoggable(MLevel.FINE))
/*      */         {
/* 1547 */           logger.log(MLevel.FINE, this + ": close() called more than once.");
/*      */         }
/*      */       } else {
/* 1550 */         throw exc;
/*      */       } 
/* 1552 */     } catch (Exception exc) {
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
/*      */   public final Connection getConnection() throws SQLException {
/*      */     try {
/* 1566 */       maybeDirtyTransaction();
/*      */       
/* 1568 */       if (!isDetached()) {
/* 1569 */         return this.creatorProxy;
/*      */       }
/* 1571 */       throw new SQLException("You cannot operate on a closed Statement!");
/*      */     }
/* 1573 */     catch (NullPointerException exc) {
/*      */       
/* 1575 */       if (isDetached())
/*      */       {
/* 1577 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1579 */       throw exc;
/*      */     }
/* 1581 */     catch (Exception exc) {
/*      */       
/* 1583 */       if (!isDetached())
/*      */       {
/* 1585 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1587 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLWarning getWarnings() throws SQLException {
/*      */     try {
/* 1595 */       maybeDirtyTransaction();
/*      */       
/* 1597 */       return this.inner.getWarnings();
/*      */     }
/* 1599 */     catch (NullPointerException exc) {
/*      */       
/* 1601 */       if (isDetached())
/*      */       {
/* 1603 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1605 */       throw exc;
/*      */     }
/* 1607 */     catch (Exception exc) {
/*      */       
/* 1609 */       if (!isDetached())
/*      */       {
/* 1611 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1613 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearWarnings() throws SQLException {
/*      */     try {
/* 1621 */       maybeDirtyTransaction();
/*      */       
/* 1623 */       this.inner.clearWarnings();
/*      */     }
/* 1625 */     catch (NullPointerException exc) {
/*      */       
/* 1627 */       if (isDetached())
/*      */       {
/* 1629 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1631 */       throw exc;
/*      */     }
/* 1633 */     catch (Exception exc) {
/*      */       
/* 1635 */       if (!isDetached())
/*      */       {
/* 1637 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1639 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isClosed() throws SQLException {
/*      */     try {
/* 1647 */       maybeDirtyTransaction();
/*      */       
/* 1649 */       return isDetached();
/*      */     }
/* 1651 */     catch (NullPointerException exc) {
/*      */       
/* 1653 */       if (isDetached())
/*      */       {
/* 1655 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1657 */       throw exc;
/*      */     }
/* 1659 */     catch (Exception exc) {
/*      */       
/* 1661 */       if (!isDetached())
/*      */       {
/* 1663 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1665 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a) throws SQLException {
/*      */     try {
/* 1673 */       maybeDirtyTransaction();
/*      */       
/* 1675 */       return this.inner.execute(a);
/*      */     }
/* 1677 */     catch (NullPointerException exc) {
/*      */       
/* 1679 */       if (isDetached())
/*      */       {
/* 1681 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1683 */       throw exc;
/*      */     }
/* 1685 */     catch (Exception exc) {
/*      */       
/* 1687 */       if (!isDetached())
/*      */       {
/* 1689 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1691 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a, String[] b) throws SQLException {
/*      */     try {
/* 1699 */       maybeDirtyTransaction();
/*      */       
/* 1701 */       return this.inner.execute(a, b);
/*      */     }
/* 1703 */     catch (NullPointerException exc) {
/*      */       
/* 1705 */       if (isDetached())
/*      */       {
/* 1707 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1709 */       throw exc;
/*      */     }
/* 1711 */     catch (Exception exc) {
/*      */       
/* 1713 */       if (!isDetached())
/*      */       {
/* 1715 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1717 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a, int[] b) throws SQLException {
/*      */     try {
/* 1725 */       maybeDirtyTransaction();
/*      */       
/* 1727 */       return this.inner.execute(a, b);
/*      */     }
/* 1729 */     catch (NullPointerException exc) {
/*      */       
/* 1731 */       if (isDetached())
/*      */       {
/* 1733 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1735 */       throw exc;
/*      */     }
/* 1737 */     catch (Exception exc) {
/*      */       
/* 1739 */       if (!isDetached())
/*      */       {
/* 1741 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1743 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a, int b) throws SQLException {
/*      */     try {
/* 1751 */       maybeDirtyTransaction();
/*      */       
/* 1753 */       return this.inner.execute(a, b);
/*      */     }
/* 1755 */     catch (NullPointerException exc) {
/*      */       
/* 1757 */       if (isDetached())
/*      */       {
/* 1759 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1761 */       throw exc;
/*      */     }
/* 1763 */     catch (Exception exc) {
/*      */       
/* 1765 */       if (!isDetached())
/*      */       {
/* 1767 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1769 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet executeQuery(String a) throws SQLException {
/*      */     try {
/* 1777 */       maybeDirtyTransaction();
/*      */       
/* 1779 */       ResultSet innerResultSet = this.inner.executeQuery(a);
/* 1780 */       if (innerResultSet == null) return null; 
/* 1781 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 1782 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 1783 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 1784 */        return out;
/*      */     }
/* 1786 */     catch (NullPointerException exc) {
/*      */       
/* 1788 */       if (isDetached())
/*      */       {
/* 1790 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1792 */       throw exc;
/*      */     }
/* 1794 */     catch (Exception exc) {
/*      */       
/* 1796 */       if (!isDetached())
/*      */       {
/* 1798 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1800 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, int b) throws SQLException {
/*      */     try {
/* 1808 */       maybeDirtyTransaction();
/*      */       
/* 1810 */       return this.inner.executeUpdate(a, b);
/*      */     }
/* 1812 */     catch (NullPointerException exc) {
/*      */       
/* 1814 */       if (isDetached())
/*      */       {
/* 1816 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1818 */       throw exc;
/*      */     }
/* 1820 */     catch (Exception exc) {
/*      */       
/* 1822 */       if (!isDetached())
/*      */       {
/* 1824 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1826 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, int[] b) throws SQLException {
/*      */     try {
/* 1834 */       maybeDirtyTransaction();
/*      */       
/* 1836 */       return this.inner.executeUpdate(a, b);
/*      */     }
/* 1838 */     catch (NullPointerException exc) {
/*      */       
/* 1840 */       if (isDetached())
/*      */       {
/* 1842 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1844 */       throw exc;
/*      */     }
/* 1846 */     catch (Exception exc) {
/*      */       
/* 1848 */       if (!isDetached())
/*      */       {
/* 1850 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1852 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, String[] b) throws SQLException {
/*      */     try {
/* 1860 */       maybeDirtyTransaction();
/*      */       
/* 1862 */       return this.inner.executeUpdate(a, b);
/*      */     }
/* 1864 */     catch (NullPointerException exc) {
/*      */       
/* 1866 */       if (isDetached())
/*      */       {
/* 1868 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1870 */       throw exc;
/*      */     }
/* 1872 */     catch (Exception exc) {
/*      */       
/* 1874 */       if (!isDetached())
/*      */       {
/* 1876 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1878 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a) throws SQLException {
/*      */     try {
/* 1886 */       maybeDirtyTransaction();
/*      */       
/* 1888 */       return this.inner.executeUpdate(a);
/*      */     }
/* 1890 */     catch (NullPointerException exc) {
/*      */       
/* 1892 */       if (isDetached())
/*      */       {
/* 1894 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1896 */       throw exc;
/*      */     }
/* 1898 */     catch (Exception exc) {
/*      */       
/* 1900 */       if (!isDetached())
/*      */       {
/* 1902 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1904 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxFieldSize() throws SQLException {
/*      */     try {
/* 1912 */       maybeDirtyTransaction();
/*      */       
/* 1914 */       return this.inner.getMaxFieldSize();
/*      */     }
/* 1916 */     catch (NullPointerException exc) {
/*      */       
/* 1918 */       if (isDetached())
/*      */       {
/* 1920 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1922 */       throw exc;
/*      */     }
/* 1924 */     catch (Exception exc) {
/*      */       
/* 1926 */       if (!isDetached())
/*      */       {
/* 1928 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1930 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMaxFieldSize(int a) throws SQLException {
/*      */     try {
/* 1938 */       maybeDirtyTransaction();
/*      */       
/* 1940 */       this.inner.setMaxFieldSize(a);
/*      */     }
/* 1942 */     catch (NullPointerException exc) {
/*      */       
/* 1944 */       if (isDetached())
/*      */       {
/* 1946 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1948 */       throw exc;
/*      */     }
/* 1950 */     catch (Exception exc) {
/*      */       
/* 1952 */       if (!isDetached())
/*      */       {
/* 1954 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1956 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxRows() throws SQLException {
/*      */     try {
/* 1964 */       maybeDirtyTransaction();
/*      */       
/* 1966 */       return this.inner.getMaxRows();
/*      */     }
/* 1968 */     catch (NullPointerException exc) {
/*      */       
/* 1970 */       if (isDetached())
/*      */       {
/* 1972 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1974 */       throw exc;
/*      */     }
/* 1976 */     catch (Exception exc) {
/*      */       
/* 1978 */       if (!isDetached())
/*      */       {
/* 1980 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1982 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMaxRows(int a) throws SQLException {
/*      */     try {
/* 1990 */       maybeDirtyTransaction();
/*      */       
/* 1992 */       this.inner.setMaxRows(a);
/*      */     }
/* 1994 */     catch (NullPointerException exc) {
/*      */       
/* 1996 */       if (isDetached())
/*      */       {
/* 1998 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2000 */       throw exc;
/*      */     }
/* 2002 */     catch (Exception exc) {
/*      */       
/* 2004 */       if (!isDetached())
/*      */       {
/* 2006 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2008 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setEscapeProcessing(boolean a) throws SQLException {
/*      */     try {
/* 2016 */       maybeDirtyTransaction();
/*      */       
/* 2018 */       this.inner.setEscapeProcessing(a);
/*      */     }
/* 2020 */     catch (NullPointerException exc) {
/*      */       
/* 2022 */       if (isDetached())
/*      */       {
/* 2024 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2026 */       throw exc;
/*      */     }
/* 2028 */     catch (Exception exc) {
/*      */       
/* 2030 */       if (!isDetached())
/*      */       {
/* 2032 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2034 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getQueryTimeout() throws SQLException {
/*      */     try {
/* 2042 */       maybeDirtyTransaction();
/*      */       
/* 2044 */       return this.inner.getQueryTimeout();
/*      */     }
/* 2046 */     catch (NullPointerException exc) {
/*      */       
/* 2048 */       if (isDetached())
/*      */       {
/* 2050 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2052 */       throw exc;
/*      */     }
/* 2054 */     catch (Exception exc) {
/*      */       
/* 2056 */       if (!isDetached())
/*      */       {
/* 2058 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2060 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setQueryTimeout(int a) throws SQLException {
/*      */     try {
/* 2068 */       maybeDirtyTransaction();
/*      */       
/* 2070 */       this.inner.setQueryTimeout(a);
/*      */     }
/* 2072 */     catch (NullPointerException exc) {
/*      */       
/* 2074 */       if (isDetached())
/*      */       {
/* 2076 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void cancel() throws SQLException {
/*      */     try {
/* 2094 */       maybeDirtyTransaction();
/*      */       
/* 2096 */       this.inner.cancel();
/*      */     }
/* 2098 */     catch (NullPointerException exc) {
/*      */       
/* 2100 */       if (isDetached())
/*      */       {
/* 2102 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2104 */       throw exc;
/*      */     }
/* 2106 */     catch (Exception exc) {
/*      */       
/* 2108 */       if (!isDetached())
/*      */       {
/* 2110 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2112 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCursorName(String a) throws SQLException {
/*      */     try {
/* 2120 */       maybeDirtyTransaction();
/*      */       
/* 2122 */       this.inner.setCursorName(a);
/*      */     }
/* 2124 */     catch (NullPointerException exc) {
/*      */       
/* 2126 */       if (isDetached())
/*      */       {
/* 2128 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2130 */       throw exc;
/*      */     }
/* 2132 */     catch (Exception exc) {
/*      */       
/* 2134 */       if (!isDetached())
/*      */       {
/* 2136 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2138 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getResultSet() throws SQLException {
/*      */     try {
/* 2146 */       maybeDirtyTransaction();
/*      */       
/* 2148 */       ResultSet innerResultSet = this.inner.getResultSet();
/* 2149 */       if (innerResultSet == null) return null; 
/* 2150 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 2151 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 2152 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 2153 */        return out;
/*      */     }
/* 2155 */     catch (NullPointerException exc) {
/*      */       
/* 2157 */       if (isDetached())
/*      */       {
/* 2159 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2161 */       throw exc;
/*      */     }
/* 2163 */     catch (Exception exc) {
/*      */       
/* 2165 */       if (!isDetached())
/*      */       {
/* 2167 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2169 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getUpdateCount() throws SQLException {
/*      */     try {
/* 2177 */       maybeDirtyTransaction();
/*      */       
/* 2179 */       return this.inner.getUpdateCount();
/*      */     }
/* 2181 */     catch (NullPointerException exc) {
/*      */       
/* 2183 */       if (isDetached())
/*      */       {
/* 2185 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2187 */       throw exc;
/*      */     }
/* 2189 */     catch (Exception exc) {
/*      */       
/* 2191 */       if (!isDetached())
/*      */       {
/* 2193 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2195 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getMoreResults(int a) throws SQLException {
/*      */     try {
/* 2203 */       maybeDirtyTransaction();
/*      */       
/* 2205 */       return this.inner.getMoreResults(a);
/*      */     }
/* 2207 */     catch (NullPointerException exc) {
/*      */       
/* 2209 */       if (isDetached())
/*      */       {
/* 2211 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2213 */       throw exc;
/*      */     }
/* 2215 */     catch (Exception exc) {
/*      */       
/* 2217 */       if (!isDetached())
/*      */       {
/* 2219 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2221 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getMoreResults() throws SQLException {
/*      */     try {
/* 2229 */       maybeDirtyTransaction();
/*      */       
/* 2231 */       return this.inner.getMoreResults();
/*      */     }
/* 2233 */     catch (NullPointerException exc) {
/*      */       
/* 2235 */       if (isDetached())
/*      */       {
/* 2237 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2239 */       throw exc;
/*      */     }
/* 2241 */     catch (Exception exc) {
/*      */       
/* 2243 */       if (!isDetached())
/*      */       {
/* 2245 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2247 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchDirection(int a) throws SQLException {
/*      */     try {
/* 2255 */       maybeDirtyTransaction();
/*      */       
/* 2257 */       this.inner.setFetchDirection(a);
/*      */     }
/* 2259 */     catch (NullPointerException exc) {
/*      */       
/* 2261 */       if (isDetached())
/*      */       {
/* 2263 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2265 */       throw exc;
/*      */     }
/* 2267 */     catch (Exception exc) {
/*      */       
/* 2269 */       if (!isDetached())
/*      */       {
/* 2271 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2273 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchDirection() throws SQLException {
/*      */     try {
/* 2281 */       maybeDirtyTransaction();
/*      */       
/* 2283 */       return this.inner.getFetchDirection();
/*      */     }
/* 2285 */     catch (NullPointerException exc) {
/*      */       
/* 2287 */       if (isDetached())
/*      */       {
/* 2289 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2291 */       throw exc;
/*      */     }
/* 2293 */     catch (Exception exc) {
/*      */       
/* 2295 */       if (!isDetached())
/*      */       {
/* 2297 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2299 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchSize(int a) throws SQLException {
/*      */     try {
/* 2307 */       maybeDirtyTransaction();
/*      */       
/* 2309 */       this.inner.setFetchSize(a);
/*      */     }
/* 2311 */     catch (NullPointerException exc) {
/*      */       
/* 2313 */       if (isDetached())
/*      */       {
/* 2315 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2317 */       throw exc;
/*      */     }
/* 2319 */     catch (Exception exc) {
/*      */       
/* 2321 */       if (!isDetached())
/*      */       {
/* 2323 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2325 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchSize() throws SQLException {
/*      */     try {
/* 2333 */       maybeDirtyTransaction();
/*      */       
/* 2335 */       return this.inner.getFetchSize();
/*      */     }
/* 2337 */     catch (NullPointerException exc) {
/*      */       
/* 2339 */       if (isDetached())
/*      */       {
/* 2341 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2343 */       throw exc;
/*      */     }
/* 2345 */     catch (Exception exc) {
/*      */       
/* 2347 */       if (!isDetached())
/*      */       {
/* 2349 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2351 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetConcurrency() throws SQLException {
/*      */     try {
/* 2359 */       maybeDirtyTransaction();
/*      */       
/* 2361 */       return this.inner.getResultSetConcurrency();
/*      */     }
/* 2363 */     catch (NullPointerException exc) {
/*      */       
/* 2365 */       if (isDetached())
/*      */       {
/* 2367 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2369 */       throw exc;
/*      */     }
/* 2371 */     catch (Exception exc) {
/*      */       
/* 2373 */       if (!isDetached())
/*      */       {
/* 2375 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2377 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetType() throws SQLException {
/*      */     try {
/* 2385 */       maybeDirtyTransaction();
/*      */       
/* 2387 */       return this.inner.getResultSetType();
/*      */     }
/* 2389 */     catch (NullPointerException exc) {
/*      */       
/* 2391 */       if (isDetached())
/*      */       {
/* 2393 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2395 */       throw exc;
/*      */     }
/* 2397 */     catch (Exception exc) {
/*      */       
/* 2399 */       if (!isDetached())
/*      */       {
/* 2401 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2403 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addBatch(String a) throws SQLException {
/*      */     try {
/* 2411 */       maybeDirtyTransaction();
/*      */       
/* 2413 */       this.inner.addBatch(a);
/*      */     }
/* 2415 */     catch (NullPointerException exc) {
/*      */       
/* 2417 */       if (isDetached())
/*      */       {
/* 2419 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2421 */       throw exc;
/*      */     }
/* 2423 */     catch (Exception exc) {
/*      */       
/* 2425 */       if (!isDetached())
/*      */       {
/* 2427 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2429 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearBatch() throws SQLException {
/*      */     try {
/* 2437 */       maybeDirtyTransaction();
/*      */       
/* 2439 */       this.inner.clearBatch();
/*      */     }
/* 2441 */     catch (NullPointerException exc) {
/*      */       
/* 2443 */       if (isDetached())
/*      */       {
/* 2445 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2447 */       throw exc;
/*      */     }
/* 2449 */     catch (Exception exc) {
/*      */       
/* 2451 */       if (!isDetached())
/*      */       {
/* 2453 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2455 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] executeBatch() throws SQLException {
/*      */     try {
/* 2463 */       maybeDirtyTransaction();
/*      */       
/* 2465 */       return this.inner.executeBatch();
/*      */     }
/* 2467 */     catch (NullPointerException exc) {
/*      */       
/* 2469 */       if (isDetached())
/*      */       {
/* 2471 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2473 */       throw exc;
/*      */     }
/* 2475 */     catch (Exception exc) {
/*      */       
/* 2477 */       if (!isDetached())
/*      */       {
/* 2479 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2481 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getGeneratedKeys() throws SQLException {
/*      */     try {
/* 2489 */       maybeDirtyTransaction();
/*      */       
/* 2491 */       ResultSet innerResultSet = this.inner.getGeneratedKeys();
/* 2492 */       if (innerResultSet == null) return null; 
/* 2493 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 2494 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 2495 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 2496 */        return out;
/*      */     }
/* 2498 */     catch (NullPointerException exc) {
/*      */       
/* 2500 */       if (isDetached())
/*      */       {
/* 2502 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2504 */       throw exc;
/*      */     }
/* 2506 */     catch (Exception exc) {
/*      */       
/* 2508 */       if (!isDetached())
/*      */       {
/* 2510 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2512 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetHoldability() throws SQLException {
/*      */     try {
/* 2520 */       maybeDirtyTransaction();
/*      */       
/* 2522 */       return this.inner.getResultSetHoldability();
/*      */     }
/* 2524 */     catch (NullPointerException exc) {
/*      */       
/* 2526 */       if (isDetached())
/*      */       {
/* 2528 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2530 */       throw exc;
/*      */     }
/* 2532 */     catch (Exception exc) {
/*      */       
/* 2534 */       if (!isDetached())
/*      */       {
/* 2536 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2538 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPoolable(boolean a) throws SQLException {
/*      */     try {
/* 2546 */       maybeDirtyTransaction();
/*      */       
/* 2548 */       this.inner.setPoolable(a);
/*      */     }
/* 2550 */     catch (NullPointerException exc) {
/*      */       
/* 2552 */       if (isDetached())
/*      */       {
/* 2554 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2556 */       throw exc;
/*      */     }
/* 2558 */     catch (Exception exc) {
/*      */       
/* 2560 */       if (!isDetached())
/*      */       {
/* 2562 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2564 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPoolable() throws SQLException {
/*      */     try {
/* 2572 */       maybeDirtyTransaction();
/*      */       
/* 2574 */       return this.inner.isPoolable();
/*      */     }
/* 2576 */     catch (NullPointerException exc) {
/*      */       
/* 2578 */       if (isDetached())
/*      */       {
/* 2580 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void closeOnCompletion() throws SQLException {
/*      */     try {
/* 2598 */       maybeDirtyTransaction();
/*      */       
/* 2600 */       this.inner.closeOnCompletion();
/*      */     }
/* 2602 */     catch (NullPointerException exc) {
/*      */       
/* 2604 */       if (isDetached())
/*      */       {
/* 2606 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2608 */       throw exc;
/*      */     }
/* 2610 */     catch (Exception exc) {
/*      */       
/* 2612 */       if (!isDetached())
/*      */       {
/* 2614 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2616 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCloseOnCompletion() throws SQLException {
/*      */     try {
/* 2624 */       maybeDirtyTransaction();
/*      */       
/* 2626 */       return this.inner.isCloseOnCompletion();
/*      */     }
/* 2628 */     catch (NullPointerException exc) {
/*      */       
/* 2630 */       if (isDetached())
/*      */       {
/* 2632 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2634 */       throw exc;
/*      */     }
/* 2636 */     catch (Exception exc) {
/*      */       
/* 2638 */       if (!isDetached())
/*      */       {
/* 2640 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2642 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object unwrap(Class a) throws SQLException {
/* 2648 */     if (isWrapperFor(a)) return this.inner; 
/* 2649 */     throw new SQLException(this + " is not a wrapper for " + a.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWrapperFor(Class<PreparedStatement> a) throws SQLException {
/* 2654 */     return (PreparedStatement.class == a || a.isAssignableFrom(this.inner.getClass()));
/*      */   }
/*      */   
/* 2657 */   private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyPreparedStatement");
/*      */   
/*      */   volatile NewPooledConnection parentPooledConnection;
/*      */   
/* 2661 */   ConnectionEventListener cel = new ConnectionEventListener()
/*      */     {
/*      */       public void connectionErrorOccurred(ConnectionEvent evt) {}
/*      */ 
/*      */       
/*      */       public void connectionClosed(ConnectionEvent evt) {
/* 2667 */         NewProxyPreparedStatement.this.detach();
/*      */       }
/*      */     };
/*      */   boolean is_cached;
/*      */   void attach(NewPooledConnection parentPooledConnection) {
/* 2672 */     this.parentPooledConnection = parentPooledConnection;
/* 2673 */     parentPooledConnection.addConnectionEventListener(this.cel);
/*      */   }
/*      */   NewProxyConnection creatorProxy;
/*      */   
/*      */   private void detach() {
/* 2678 */     this.parentPooledConnection.removeConnectionEventListener(this.cel);
/* 2679 */     this.parentPooledConnection = null;
/*      */   }
/*      */ 
/*      */   
/*      */   NewProxyPreparedStatement(PreparedStatement inner, NewPooledConnection parentPooledConnection) {
/* 2684 */     this(inner);
/* 2685 */     attach(parentPooledConnection);
/*      */   }
/*      */   
/*      */   boolean isDetached() {
/* 2689 */     return (this.parentPooledConnection == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2698 */   HashSet myProxyResultSets = new HashSet();
/*      */ 
/*      */   
/*      */   public void detachProxyResultSet(ResultSet prs) {
/* 2702 */     synchronized (this.myProxyResultSets) { this.myProxyResultSets.remove(prs); }
/*      */   
/*      */   }
/*      */   
/*      */   NewProxyPreparedStatement(PreparedStatement inner, NewPooledConnection parentPooledConnection, boolean cached, NewProxyConnection cProxy) {
/* 2707 */     this(inner, parentPooledConnection);
/* 2708 */     this.is_cached = cached;
/* 2709 */     this.creatorProxy = cProxy;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object rawStatementOperation(Method m, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException, SQLException {
/* 2714 */     maybeDirtyTransaction();
/*      */     
/* 2716 */     if (target == C3P0ProxyStatement.RAW_STATEMENT) target = this.inner; 
/* 2717 */     for (int i = 0, len = args.length; i < len; i++) {
/* 2718 */       if (args[i] == C3P0ProxyStatement.RAW_STATEMENT) args[i] = this.inner; 
/* 2719 */     }  Object out = m.invoke(target, args);
/* 2720 */     if (out instanceof ResultSet) {
/*      */       
/* 2722 */       ResultSet innerResultSet = (ResultSet)out;
/* 2723 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 2724 */       out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     } 
/*      */     
/* 2727 */     return out;
/*      */   }
/*      */   
/*      */   void maybeDirtyTransaction() {
/* 2731 */     if (this.creatorProxy != null) this.creatorProxy.maybeDirtyTransaction(); 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewProxyPreparedStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */