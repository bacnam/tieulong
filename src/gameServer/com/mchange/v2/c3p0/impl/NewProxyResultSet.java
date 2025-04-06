/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
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
/*      */ public final class NewProxyResultSet
/*      */   implements ResultSet
/*      */ {
/*      */   protected ResultSet inner;
/*      */   
/*      */   private void __setInner(ResultSet inner) {
/*   48 */     this.inner = inner;
/*      */   }
/*      */   
/*      */   NewProxyResultSet(ResultSet inner) {
/*   52 */     __setInner(inner);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object getObject(String a, Map<String, Class<?>> b) throws SQLException {
/*      */     try {
/*   58 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*   60 */       return this.inner.getObject(a, b);
/*      */     }
/*   62 */     catch (NullPointerException exc) {
/*      */       
/*   64 */       if (isDetached())
/*      */       {
/*   66 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*   68 */       throw exc;
/*      */     }
/*   70 */     catch (Exception exc) {
/*      */       
/*   72 */       if (!isDetached())
/*      */       {
/*   74 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*   76 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(int a, Map<String, Class<?>> b) throws SQLException {
/*      */     try {
/*   84 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*   86 */       return this.inner.getObject(a, b);
/*      */     }
/*   88 */     catch (NullPointerException exc) {
/*      */       
/*   90 */       if (isDetached())
/*      */       {
/*   92 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*   94 */       throw exc;
/*      */     }
/*   96 */     catch (Exception exc) {
/*      */       
/*   98 */       if (!isDetached())
/*      */       {
/*  100 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  102 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(int a) throws SQLException {
/*      */     try {
/*  110 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  112 */       return this.inner.getObject(a);
/*      */     }
/*  114 */     catch (NullPointerException exc) {
/*      */       
/*  116 */       if (isDetached())
/*      */       {
/*  118 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  120 */       throw exc;
/*      */     }
/*  122 */     catch (Exception exc) {
/*      */       
/*  124 */       if (!isDetached())
/*      */       {
/*  126 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  128 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(String a) throws SQLException {
/*      */     try {
/*  136 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  138 */       return this.inner.getObject(a);
/*      */     }
/*  140 */     catch (NullPointerException exc) {
/*      */       
/*  142 */       if (isDetached())
/*      */       {
/*  144 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  146 */       throw exc;
/*      */     }
/*  148 */     catch (Exception exc) {
/*      */       
/*  150 */       if (!isDetached())
/*      */       {
/*  152 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  154 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(int a, Class<?> b) throws SQLException {
/*      */     try {
/*  162 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  164 */       return this.inner.getObject(a, b);
/*      */     }
/*  166 */     catch (NullPointerException exc) {
/*      */       
/*  168 */       if (isDetached())
/*      */       {
/*  170 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final Object getObject(String a, Class<?> b) throws SQLException {
/*      */     try {
/*  188 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  190 */       return this.inner.getObject(a, b);
/*      */     }
/*  192 */     catch (NullPointerException exc) {
/*      */       
/*  194 */       if (isDetached())
/*      */       {
/*  196 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  198 */       throw exc;
/*      */     }
/*  200 */     catch (Exception exc) {
/*      */       
/*  202 */       if (!isDetached())
/*      */       {
/*  204 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  206 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getBoolean(String a) throws SQLException {
/*      */     try {
/*  214 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  216 */       return this.inner.getBoolean(a);
/*      */     }
/*  218 */     catch (NullPointerException exc) {
/*      */       
/*  220 */       if (isDetached())
/*      */       {
/*  222 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  224 */       throw exc;
/*      */     }
/*  226 */     catch (Exception exc) {
/*      */       
/*  228 */       if (!isDetached())
/*      */       {
/*  230 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  232 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getBoolean(int a) throws SQLException {
/*      */     try {
/*  240 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  242 */       return this.inner.getBoolean(a);
/*      */     }
/*  244 */     catch (NullPointerException exc) {
/*      */       
/*  246 */       if (isDetached())
/*      */       {
/*  248 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  250 */       throw exc;
/*      */     }
/*  252 */     catch (Exception exc) {
/*      */       
/*  254 */       if (!isDetached())
/*      */       {
/*  256 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  258 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getByte(String a) throws SQLException {
/*      */     try {
/*  266 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  268 */       return this.inner.getByte(a);
/*      */     }
/*  270 */     catch (NullPointerException exc) {
/*      */       
/*  272 */       if (isDetached())
/*      */       {
/*  274 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  276 */       throw exc;
/*      */     }
/*  278 */     catch (Exception exc) {
/*      */       
/*  280 */       if (!isDetached())
/*      */       {
/*  282 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  284 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getByte(int a) throws SQLException {
/*      */     try {
/*  292 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  294 */       return this.inner.getByte(a);
/*      */     }
/*  296 */     catch (NullPointerException exc) {
/*      */       
/*  298 */       if (isDetached())
/*      */       {
/*  300 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  302 */       throw exc;
/*      */     }
/*  304 */     catch (Exception exc) {
/*      */       
/*  306 */       if (!isDetached())
/*      */       {
/*  308 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  310 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getShort(int a) throws SQLException {
/*      */     try {
/*  318 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  320 */       return this.inner.getShort(a);
/*      */     }
/*  322 */     catch (NullPointerException exc) {
/*      */       
/*  324 */       if (isDetached())
/*      */       {
/*  326 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  328 */       throw exc;
/*      */     }
/*  330 */     catch (Exception exc) {
/*      */       
/*  332 */       if (!isDetached())
/*      */       {
/*  334 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  336 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getShort(String a) throws SQLException {
/*      */     try {
/*  344 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  346 */       return this.inner.getShort(a);
/*      */     }
/*  348 */     catch (NullPointerException exc) {
/*      */       
/*  350 */       if (isDetached())
/*      */       {
/*  352 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  354 */       throw exc;
/*      */     }
/*  356 */     catch (Exception exc) {
/*      */       
/*  358 */       if (!isDetached())
/*      */       {
/*  360 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  362 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getInt(String a) throws SQLException {
/*      */     try {
/*  370 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  372 */       return this.inner.getInt(a);
/*      */     }
/*  374 */     catch (NullPointerException exc) {
/*      */       
/*  376 */       if (isDetached())
/*      */       {
/*  378 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  380 */       throw exc;
/*      */     }
/*  382 */     catch (Exception exc) {
/*      */       
/*  384 */       if (!isDetached())
/*      */       {
/*  386 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  388 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getInt(int a) throws SQLException {
/*      */     try {
/*  396 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  398 */       return this.inner.getInt(a);
/*      */     }
/*  400 */     catch (NullPointerException exc) {
/*      */       
/*  402 */       if (isDetached())
/*      */       {
/*  404 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  406 */       throw exc;
/*      */     }
/*  408 */     catch (Exception exc) {
/*      */       
/*  410 */       if (!isDetached())
/*      */       {
/*  412 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  414 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLong(String a) throws SQLException {
/*      */     try {
/*  422 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  424 */       return this.inner.getLong(a);
/*      */     }
/*  426 */     catch (NullPointerException exc) {
/*      */       
/*  428 */       if (isDetached())
/*      */       {
/*  430 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  432 */       throw exc;
/*      */     }
/*  434 */     catch (Exception exc) {
/*      */       
/*  436 */       if (!isDetached())
/*      */       {
/*  438 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  440 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLong(int a) throws SQLException {
/*      */     try {
/*  448 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  450 */       return this.inner.getLong(a);
/*      */     }
/*  452 */     catch (NullPointerException exc) {
/*      */       
/*  454 */       if (isDetached())
/*      */       {
/*  456 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  458 */       throw exc;
/*      */     }
/*  460 */     catch (Exception exc) {
/*      */       
/*  462 */       if (!isDetached())
/*      */       {
/*  464 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  466 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFloat(int a) throws SQLException {
/*      */     try {
/*  474 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  476 */       return this.inner.getFloat(a);
/*      */     }
/*  478 */     catch (NullPointerException exc) {
/*      */       
/*  480 */       if (isDetached())
/*      */       {
/*  482 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  484 */       throw exc;
/*      */     }
/*  486 */     catch (Exception exc) {
/*      */       
/*  488 */       if (!isDetached())
/*      */       {
/*  490 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  492 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFloat(String a) throws SQLException {
/*      */     try {
/*  500 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  502 */       return this.inner.getFloat(a);
/*      */     }
/*  504 */     catch (NullPointerException exc) {
/*      */       
/*  506 */       if (isDetached())
/*      */       {
/*  508 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  510 */       throw exc;
/*      */     }
/*  512 */     catch (Exception exc) {
/*      */       
/*  514 */       if (!isDetached())
/*      */       {
/*  516 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  518 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDouble(String a) throws SQLException {
/*      */     try {
/*  526 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  528 */       return this.inner.getDouble(a);
/*      */     }
/*  530 */     catch (NullPointerException exc) {
/*      */       
/*  532 */       if (isDetached())
/*      */       {
/*  534 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  536 */       throw exc;
/*      */     }
/*  538 */     catch (Exception exc) {
/*      */       
/*  540 */       if (!isDetached())
/*      */       {
/*  542 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  544 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDouble(int a) throws SQLException {
/*      */     try {
/*  552 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  554 */       return this.inner.getDouble(a);
/*      */     }
/*  556 */     catch (NullPointerException exc) {
/*      */       
/*  558 */       if (isDetached())
/*      */       {
/*  560 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  562 */       throw exc;
/*      */     }
/*  564 */     catch (Exception exc) {
/*      */       
/*  566 */       if (!isDetached())
/*      */       {
/*  568 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  570 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] getBytes(int a) throws SQLException {
/*      */     try {
/*  578 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  580 */       return this.inner.getBytes(a);
/*      */     }
/*  582 */     catch (NullPointerException exc) {
/*      */       
/*  584 */       if (isDetached())
/*      */       {
/*  586 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  588 */       throw exc;
/*      */     }
/*  590 */     catch (Exception exc) {
/*      */       
/*  592 */       if (!isDetached())
/*      */       {
/*  594 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  596 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] getBytes(String a) throws SQLException {
/*      */     try {
/*  604 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  606 */       return this.inner.getBytes(a);
/*      */     }
/*  608 */     catch (NullPointerException exc) {
/*      */       
/*  610 */       if (isDetached())
/*      */       {
/*  612 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final Array getArray(String a) throws SQLException {
/*      */     try {
/*  630 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  632 */       return this.inner.getArray(a);
/*      */     }
/*  634 */     catch (NullPointerException exc) {
/*      */       
/*  636 */       if (isDetached())
/*      */       {
/*  638 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  640 */       throw exc;
/*      */     }
/*  642 */     catch (Exception exc) {
/*      */       
/*  644 */       if (!isDetached())
/*      */       {
/*  646 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  648 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Array getArray(int a) throws SQLException {
/*      */     try {
/*  656 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  658 */       return this.inner.getArray(a);
/*      */     }
/*  660 */     catch (NullPointerException exc) {
/*      */       
/*  662 */       if (isDetached())
/*      */       {
/*  664 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  666 */       throw exc;
/*      */     }
/*  668 */     catch (Exception exc) {
/*      */       
/*  670 */       if (!isDetached())
/*      */       {
/*  672 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  674 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean next() throws SQLException {
/*      */     try {
/*  682 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  684 */       return this.inner.next();
/*      */     }
/*  686 */     catch (NullPointerException exc) {
/*      */       
/*  688 */       if (isDetached())
/*      */       {
/*  690 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  692 */       throw exc;
/*      */     }
/*  694 */     catch (Exception exc) {
/*      */       
/*  696 */       if (!isDetached())
/*      */       {
/*  698 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  700 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final URL getURL(String a) throws SQLException {
/*      */     try {
/*  708 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  710 */       return this.inner.getURL(a);
/*      */     }
/*  712 */     catch (NullPointerException exc) {
/*      */       
/*  714 */       if (isDetached())
/*      */       {
/*  716 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  718 */       throw exc;
/*      */     }
/*  720 */     catch (Exception exc) {
/*      */       
/*  722 */       if (!isDetached())
/*      */       {
/*  724 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  726 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final URL getURL(int a) throws SQLException {
/*      */     try {
/*  734 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  736 */       return this.inner.getURL(a);
/*      */     }
/*  738 */     catch (NullPointerException exc) {
/*      */       
/*  740 */       if (isDetached())
/*      */       {
/*  742 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  744 */       throw exc;
/*      */     }
/*  746 */     catch (Exception exc) {
/*      */       
/*  748 */       if (!isDetached())
/*      */       {
/*  750 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  752 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void close() throws SQLException {
/*      */     try {
/*  760 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  762 */       if (!isDetached())
/*      */       {
/*  764 */         if (this.creator instanceof Statement)
/*  765 */         { this.parentPooledConnection.markInactiveResultSetForStatement((Statement)this.creator, this.inner); }
/*  766 */         else if (this.creator instanceof java.sql.DatabaseMetaData)
/*  767 */         { this.parentPooledConnection.markInactiveMetaDataResultSet(this.inner); }
/*  768 */         else if (this.creator instanceof java.sql.Connection)
/*  769 */         { this.parentPooledConnection.markInactiveRawConnectionResultSet(this.inner); }
/*  770 */         else { throw new InternalError("Must be Statement or DatabaseMetaData -- Bad Creator: " + this.creator); }
/*  771 */          if (this.creatorProxy instanceof ProxyResultSetDetachable) ((ProxyResultSetDetachable)this.creatorProxy).detachProxyResultSet(this); 
/*  772 */         detach();
/*  773 */         this.inner.close();
/*  774 */         this.inner = null;
/*      */       }
/*      */     
/*  777 */     } catch (NullPointerException exc) {
/*      */       
/*  779 */       if (isDetached()) {
/*      */         
/*  781 */         if (logger.isLoggable(MLevel.FINE))
/*      */         {
/*  783 */           logger.log(MLevel.FINE, this + ": close() called more than once.");
/*      */         }
/*      */       } else {
/*  786 */         throw exc;
/*      */       } 
/*  788 */     } catch (Exception exc) {
/*      */       
/*  790 */       if (!isDetached())
/*      */       {
/*  792 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  794 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getType() throws SQLException {
/*      */     try {
/*  802 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  804 */       return this.inner.getType();
/*      */     }
/*  806 */     catch (NullPointerException exc) {
/*      */       
/*  808 */       if (isDetached())
/*      */       {
/*  810 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  812 */       throw exc;
/*      */     }
/*  814 */     catch (Exception exc) {
/*      */       
/*  816 */       if (!isDetached())
/*      */       {
/*  818 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  820 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean previous() throws SQLException {
/*      */     try {
/*  828 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  830 */       return this.inner.previous();
/*      */     }
/*  832 */     catch (NullPointerException exc) {
/*      */       
/*  834 */       if (isDetached())
/*      */       {
/*  836 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  838 */       throw exc;
/*      */     }
/*  840 */     catch (Exception exc) {
/*      */       
/*  842 */       if (!isDetached())
/*      */       {
/*  844 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  846 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Ref getRef(String a) throws SQLException {
/*      */     try {
/*  854 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  856 */       return this.inner.getRef(a);
/*      */     }
/*  858 */     catch (NullPointerException exc) {
/*      */       
/*  860 */       if (isDetached())
/*      */       {
/*  862 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  864 */       throw exc;
/*      */     }
/*  866 */     catch (Exception exc) {
/*      */       
/*  868 */       if (!isDetached())
/*      */       {
/*  870 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  872 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Ref getRef(int a) throws SQLException {
/*      */     try {
/*  880 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  882 */       return this.inner.getRef(a);
/*      */     }
/*  884 */     catch (NullPointerException exc) {
/*      */       
/*  886 */       if (isDetached())
/*      */       {
/*  888 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  890 */       throw exc;
/*      */     }
/*  892 */     catch (Exception exc) {
/*      */       
/*  894 */       if (!isDetached())
/*      */       {
/*  896 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  898 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getString(String a) throws SQLException {
/*      */     try {
/*  906 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  908 */       return this.inner.getString(a);
/*      */     }
/*  910 */     catch (NullPointerException exc) {
/*      */       
/*  912 */       if (isDetached())
/*      */       {
/*  914 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  916 */       throw exc;
/*      */     }
/*  918 */     catch (Exception exc) {
/*      */       
/*  920 */       if (!isDetached())
/*      */       {
/*  922 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  924 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getString(int a) throws SQLException {
/*      */     try {
/*  932 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  934 */       return this.inner.getString(a);
/*      */     }
/*  936 */     catch (NullPointerException exc) {
/*      */       
/*  938 */       if (isDetached())
/*      */       {
/*  940 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  942 */       throw exc;
/*      */     }
/*  944 */     catch (Exception exc) {
/*      */       
/*  946 */       if (!isDetached())
/*      */       {
/*  948 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  950 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(int a, Calendar b) throws SQLException {
/*      */     try {
/*  958 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  960 */       return this.inner.getTime(a, b);
/*      */     }
/*  962 */     catch (NullPointerException exc) {
/*      */       
/*  964 */       if (isDetached())
/*      */       {
/*  966 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  968 */       throw exc;
/*      */     }
/*  970 */     catch (Exception exc) {
/*      */       
/*  972 */       if (!isDetached())
/*      */       {
/*  974 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  976 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(int a) throws SQLException {
/*      */     try {
/*  984 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/*  986 */       return this.inner.getTime(a);
/*      */     }
/*  988 */     catch (NullPointerException exc) {
/*      */       
/*  990 */       if (isDetached())
/*      */       {
/*  992 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/*  994 */       throw exc;
/*      */     }
/*  996 */     catch (Exception exc) {
/*      */       
/*  998 */       if (!isDetached())
/*      */       {
/* 1000 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1002 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(String a) throws SQLException {
/*      */     try {
/* 1010 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1012 */       return this.inner.getTime(a);
/*      */     }
/* 1014 */     catch (NullPointerException exc) {
/*      */       
/* 1016 */       if (isDetached())
/*      */       {
/* 1018 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1020 */       throw exc;
/*      */     }
/* 1022 */     catch (Exception exc) {
/*      */       
/* 1024 */       if (!isDetached())
/*      */       {
/* 1026 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1028 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(String a, Calendar b) throws SQLException {
/*      */     try {
/* 1036 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1038 */       return this.inner.getTime(a, b);
/*      */     }
/* 1040 */     catch (NullPointerException exc) {
/*      */       
/* 1042 */       if (isDetached())
/*      */       {
/* 1044 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final Date getDate(String a, Calendar b) throws SQLException {
/*      */     try {
/* 1062 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1064 */       return this.inner.getDate(a, b);
/*      */     }
/* 1066 */     catch (NullPointerException exc) {
/*      */       
/* 1068 */       if (isDetached())
/*      */       {
/* 1070 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1072 */       throw exc;
/*      */     }
/* 1074 */     catch (Exception exc) {
/*      */       
/* 1076 */       if (!isDetached())
/*      */       {
/* 1078 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1080 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(int a, Calendar b) throws SQLException {
/*      */     try {
/* 1088 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1090 */       return this.inner.getDate(a, b);
/*      */     }
/* 1092 */     catch (NullPointerException exc) {
/*      */       
/* 1094 */       if (isDetached())
/*      */       {
/* 1096 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1098 */       throw exc;
/*      */     }
/* 1100 */     catch (Exception exc) {
/*      */       
/* 1102 */       if (!isDetached())
/*      */       {
/* 1104 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1106 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(int a) throws SQLException {
/*      */     try {
/* 1114 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1116 */       return this.inner.getDate(a);
/*      */     }
/* 1118 */     catch (NullPointerException exc) {
/*      */       
/* 1120 */       if (isDetached())
/*      */       {
/* 1122 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1124 */       throw exc;
/*      */     }
/* 1126 */     catch (Exception exc) {
/*      */       
/* 1128 */       if (!isDetached())
/*      */       {
/* 1130 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1132 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(String a) throws SQLException {
/*      */     try {
/* 1140 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1142 */       return this.inner.getDate(a);
/*      */     }
/* 1144 */     catch (NullPointerException exc) {
/*      */       
/* 1146 */       if (isDetached())
/*      */       {
/* 1148 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1150 */       throw exc;
/*      */     }
/* 1152 */     catch (Exception exc) {
/*      */       
/* 1154 */       if (!isDetached())
/*      */       {
/* 1156 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1158 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean first() throws SQLException {
/*      */     try {
/* 1166 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1168 */       return this.inner.first();
/*      */     }
/* 1170 */     catch (NullPointerException exc) {
/*      */       
/* 1172 */       if (isDetached())
/*      */       {
/* 1174 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1176 */       throw exc;
/*      */     }
/* 1178 */     catch (Exception exc) {
/*      */       
/* 1180 */       if (!isDetached())
/*      */       {
/* 1182 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1184 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean last() throws SQLException {
/*      */     try {
/* 1192 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1194 */       return this.inner.last();
/*      */     }
/* 1196 */     catch (NullPointerException exc) {
/*      */       
/* 1198 */       if (isDetached())
/*      */       {
/* 1200 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1202 */       throw exc;
/*      */     }
/* 1204 */     catch (Exception exc) {
/*      */       
/* 1206 */       if (!isDetached())
/*      */       {
/* 1208 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1210 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSetMetaData getMetaData() throws SQLException {
/*      */     try {
/* 1218 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1220 */       return this.inner.getMetaData();
/*      */     }
/* 1222 */     catch (NullPointerException exc) {
/*      */       
/* 1224 */       if (isDetached())
/*      */       {
/* 1226 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1228 */       throw exc;
/*      */     }
/* 1230 */     catch (Exception exc) {
/*      */       
/* 1232 */       if (!isDetached())
/*      */       {
/* 1234 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1236 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLWarning getWarnings() throws SQLException {
/*      */     try {
/* 1244 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1246 */       return this.inner.getWarnings();
/*      */     }
/* 1248 */     catch (NullPointerException exc) {
/*      */       
/* 1250 */       if (isDetached())
/*      */       {
/* 1252 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1254 */       throw exc;
/*      */     }
/* 1256 */     catch (Exception exc) {
/*      */       
/* 1258 */       if (!isDetached())
/*      */       {
/* 1260 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1262 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearWarnings() throws SQLException {
/*      */     try {
/* 1270 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1272 */       this.inner.clearWarnings();
/*      */     }
/* 1274 */     catch (NullPointerException exc) {
/*      */       
/* 1276 */       if (isDetached())
/*      */       {
/* 1278 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1280 */       throw exc;
/*      */     }
/* 1282 */     catch (Exception exc) {
/*      */       
/* 1284 */       if (!isDetached())
/*      */       {
/* 1286 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1288 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isClosed() throws SQLException {
/*      */     try {
/* 1296 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1298 */       return isDetached();
/*      */     }
/* 1300 */     catch (NullPointerException exc) {
/*      */       
/* 1302 */       if (isDetached())
/*      */       {
/* 1304 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1306 */       throw exc;
/*      */     }
/* 1308 */     catch (Exception exc) {
/*      */       
/* 1310 */       if (!isDetached())
/*      */       {
/* 1312 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1314 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Statement getStatement() throws SQLException {
/*      */     try {
/* 1322 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1324 */       if (this.creator instanceof Statement)
/* 1325 */         return (Statement)this.creatorProxy; 
/* 1326 */       if (this.creator instanceof java.sql.DatabaseMetaData)
/* 1327 */         return null; 
/* 1328 */       throw new InternalError("Must be Statement or DatabaseMetaData -- Bad Creator: " + this.creator);
/*      */     }
/* 1330 */     catch (NullPointerException exc) {
/*      */       
/* 1332 */       if (isDetached())
/*      */       {
/* 1334 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1336 */       throw exc;
/*      */     }
/* 1338 */     catch (Exception exc) {
/*      */       
/* 1340 */       if (!isDetached())
/*      */       {
/* 1342 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1344 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final InputStream getUnicodeStream(int a) throws SQLException {
/*      */     try {
/* 1352 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1354 */       return this.inner.getUnicodeStream(a);
/*      */     }
/* 1356 */     catch (NullPointerException exc) {
/*      */       
/* 1358 */       if (isDetached())
/*      */       {
/* 1360 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1362 */       throw exc;
/*      */     }
/* 1364 */     catch (Exception exc) {
/*      */       
/* 1366 */       if (!isDetached())
/*      */       {
/* 1368 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1370 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final InputStream getUnicodeStream(String a) throws SQLException {
/*      */     try {
/* 1378 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1380 */       return this.inner.getUnicodeStream(a);
/*      */     }
/* 1382 */     catch (NullPointerException exc) {
/*      */       
/* 1384 */       if (isDetached())
/*      */       {
/* 1386 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1388 */       throw exc;
/*      */     }
/* 1390 */     catch (Exception exc) {
/*      */       
/* 1392 */       if (!isDetached())
/*      */       {
/* 1394 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1396 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getCursorName() throws SQLException {
/*      */     try {
/* 1404 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1406 */       return this.inner.getCursorName();
/*      */     }
/* 1408 */     catch (NullPointerException exc) {
/*      */       
/* 1410 */       if (isDetached())
/*      */       {
/* 1412 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1414 */       throw exc;
/*      */     }
/* 1416 */     catch (Exception exc) {
/*      */       
/* 1418 */       if (!isDetached())
/*      */       {
/* 1420 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1422 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int findColumn(String a) throws SQLException {
/*      */     try {
/* 1430 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1432 */       return this.inner.findColumn(a);
/*      */     }
/* 1434 */     catch (NullPointerException exc) {
/*      */       
/* 1436 */       if (isDetached())
/*      */       {
/* 1438 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1440 */       throw exc;
/*      */     }
/* 1442 */     catch (Exception exc) {
/*      */       
/* 1444 */       if (!isDetached())
/*      */       {
/* 1446 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1448 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isBeforeFirst() throws SQLException {
/*      */     try {
/* 1456 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1458 */       return this.inner.isBeforeFirst();
/*      */     }
/* 1460 */     catch (NullPointerException exc) {
/*      */       
/* 1462 */       if (isDetached())
/*      */       {
/* 1464 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1466 */       throw exc;
/*      */     }
/* 1468 */     catch (Exception exc) {
/*      */       
/* 1470 */       if (!isDetached())
/*      */       {
/* 1472 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1474 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isAfterLast() throws SQLException {
/*      */     try {
/* 1482 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1484 */       return this.inner.isAfterLast();
/*      */     }
/* 1486 */     catch (NullPointerException exc) {
/*      */       
/* 1488 */       if (isDetached())
/*      */       {
/* 1490 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1492 */       throw exc;
/*      */     }
/* 1494 */     catch (Exception exc) {
/*      */       
/* 1496 */       if (!isDetached())
/*      */       {
/* 1498 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1500 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isFirst() throws SQLException {
/*      */     try {
/* 1508 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1510 */       return this.inner.isFirst();
/*      */     }
/* 1512 */     catch (NullPointerException exc) {
/*      */       
/* 1514 */       if (isDetached())
/*      */       {
/* 1516 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1518 */       throw exc;
/*      */     }
/* 1520 */     catch (Exception exc) {
/*      */       
/* 1522 */       if (!isDetached())
/*      */       {
/* 1524 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1526 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isLast() throws SQLException {
/*      */     try {
/* 1534 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1536 */       return this.inner.isLast();
/*      */     }
/* 1538 */     catch (NullPointerException exc) {
/*      */       
/* 1540 */       if (isDetached())
/*      */       {
/* 1542 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1544 */       throw exc;
/*      */     }
/* 1546 */     catch (Exception exc) {
/*      */       
/* 1548 */       if (!isDetached())
/*      */       {
/* 1550 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1552 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void beforeFirst() throws SQLException {
/*      */     try {
/* 1560 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1562 */       this.inner.beforeFirst();
/*      */     }
/* 1564 */     catch (NullPointerException exc) {
/*      */       
/* 1566 */       if (isDetached())
/*      */       {
/* 1568 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1570 */       throw exc;
/*      */     }
/* 1572 */     catch (Exception exc) {
/*      */       
/* 1574 */       if (!isDetached())
/*      */       {
/* 1576 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1578 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void afterLast() throws SQLException {
/*      */     try {
/* 1586 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1588 */       this.inner.afterLast();
/*      */     }
/* 1590 */     catch (NullPointerException exc) {
/*      */       
/* 1592 */       if (isDetached())
/*      */       {
/* 1594 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1596 */       throw exc;
/*      */     }
/* 1598 */     catch (Exception exc) {
/*      */       
/* 1600 */       if (!isDetached())
/*      */       {
/* 1602 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1604 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getRow() throws SQLException {
/*      */     try {
/* 1612 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1614 */       return this.inner.getRow();
/*      */     }
/* 1616 */     catch (NullPointerException exc) {
/*      */       
/* 1618 */       if (isDetached())
/*      */       {
/* 1620 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final boolean absolute(int a) throws SQLException {
/*      */     try {
/* 1638 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1640 */       return this.inner.absolute(a);
/*      */     }
/* 1642 */     catch (NullPointerException exc) {
/*      */       
/* 1644 */       if (isDetached())
/*      */       {
/* 1646 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1648 */       throw exc;
/*      */     }
/* 1650 */     catch (Exception exc) {
/*      */       
/* 1652 */       if (!isDetached())
/*      */       {
/* 1654 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1656 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean relative(int a) throws SQLException {
/*      */     try {
/* 1664 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1666 */       return this.inner.relative(a);
/*      */     }
/* 1668 */     catch (NullPointerException exc) {
/*      */       
/* 1670 */       if (isDetached())
/*      */       {
/* 1672 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1674 */       throw exc;
/*      */     }
/* 1676 */     catch (Exception exc) {
/*      */       
/* 1678 */       if (!isDetached())
/*      */       {
/* 1680 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1682 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getConcurrency() throws SQLException {
/*      */     try {
/* 1690 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1692 */       return this.inner.getConcurrency();
/*      */     }
/* 1694 */     catch (NullPointerException exc) {
/*      */       
/* 1696 */       if (isDetached())
/*      */       {
/* 1698 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1700 */       throw exc;
/*      */     }
/* 1702 */     catch (Exception exc) {
/*      */       
/* 1704 */       if (!isDetached())
/*      */       {
/* 1706 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1708 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean rowUpdated() throws SQLException {
/*      */     try {
/* 1716 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1718 */       return this.inner.rowUpdated();
/*      */     }
/* 1720 */     catch (NullPointerException exc) {
/*      */       
/* 1722 */       if (isDetached())
/*      */       {
/* 1724 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1726 */       throw exc;
/*      */     }
/* 1728 */     catch (Exception exc) {
/*      */       
/* 1730 */       if (!isDetached())
/*      */       {
/* 1732 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1734 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean rowInserted() throws SQLException {
/*      */     try {
/* 1742 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1744 */       return this.inner.rowInserted();
/*      */     }
/* 1746 */     catch (NullPointerException exc) {
/*      */       
/* 1748 */       if (isDetached())
/*      */       {
/* 1750 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1752 */       throw exc;
/*      */     }
/* 1754 */     catch (Exception exc) {
/*      */       
/* 1756 */       if (!isDetached())
/*      */       {
/* 1758 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1760 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean rowDeleted() throws SQLException {
/*      */     try {
/* 1768 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1770 */       return this.inner.rowDeleted();
/*      */     }
/* 1772 */     catch (NullPointerException exc) {
/*      */       
/* 1774 */       if (isDetached())
/*      */       {
/* 1776 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1778 */       throw exc;
/*      */     }
/* 1780 */     catch (Exception exc) {
/*      */       
/* 1782 */       if (!isDetached())
/*      */       {
/* 1784 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1786 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNull(int a) throws SQLException {
/*      */     try {
/* 1794 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1796 */       this.inner.updateNull(a);
/*      */     }
/* 1798 */     catch (NullPointerException exc) {
/*      */       
/* 1800 */       if (isDetached())
/*      */       {
/* 1802 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1804 */       throw exc;
/*      */     }
/* 1806 */     catch (Exception exc) {
/*      */       
/* 1808 */       if (!isDetached())
/*      */       {
/* 1810 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1812 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNull(String a) throws SQLException {
/*      */     try {
/* 1820 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1822 */       this.inner.updateNull(a);
/*      */     }
/* 1824 */     catch (NullPointerException exc) {
/*      */       
/* 1826 */       if (isDetached())
/*      */       {
/* 1828 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1830 */       throw exc;
/*      */     }
/* 1832 */     catch (Exception exc) {
/*      */       
/* 1834 */       if (!isDetached())
/*      */       {
/* 1836 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1838 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBoolean(int a, boolean b) throws SQLException {
/*      */     try {
/* 1846 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1848 */       this.inner.updateBoolean(a, b);
/*      */     }
/* 1850 */     catch (NullPointerException exc) {
/*      */       
/* 1852 */       if (isDetached())
/*      */       {
/* 1854 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1856 */       throw exc;
/*      */     }
/* 1858 */     catch (Exception exc) {
/*      */       
/* 1860 */       if (!isDetached())
/*      */       {
/* 1862 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1864 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBoolean(String a, boolean b) throws SQLException {
/*      */     try {
/* 1872 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1874 */       this.inner.updateBoolean(a, b);
/*      */     }
/* 1876 */     catch (NullPointerException exc) {
/*      */       
/* 1878 */       if (isDetached())
/*      */       {
/* 1880 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1882 */       throw exc;
/*      */     }
/* 1884 */     catch (Exception exc) {
/*      */       
/* 1886 */       if (!isDetached())
/*      */       {
/* 1888 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1890 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateByte(String a, byte b) throws SQLException {
/*      */     try {
/* 1898 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1900 */       this.inner.updateByte(a, b);
/*      */     }
/* 1902 */     catch (NullPointerException exc) {
/*      */       
/* 1904 */       if (isDetached())
/*      */       {
/* 1906 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1908 */       throw exc;
/*      */     }
/* 1910 */     catch (Exception exc) {
/*      */       
/* 1912 */       if (!isDetached())
/*      */       {
/* 1914 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1916 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateByte(int a, byte b) throws SQLException {
/*      */     try {
/* 1924 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1926 */       this.inner.updateByte(a, b);
/*      */     }
/* 1928 */     catch (NullPointerException exc) {
/*      */       
/* 1930 */       if (isDetached())
/*      */       {
/* 1932 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateShort(String a, short b) throws SQLException {
/*      */     try {
/* 1950 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1952 */       this.inner.updateShort(a, b);
/*      */     }
/* 1954 */     catch (NullPointerException exc) {
/*      */       
/* 1956 */       if (isDetached())
/*      */       {
/* 1958 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1960 */       throw exc;
/*      */     }
/* 1962 */     catch (Exception exc) {
/*      */       
/* 1964 */       if (!isDetached())
/*      */       {
/* 1966 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1968 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateShort(int a, short b) throws SQLException {
/*      */     try {
/* 1976 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 1978 */       this.inner.updateShort(a, b);
/*      */     }
/* 1980 */     catch (NullPointerException exc) {
/*      */       
/* 1982 */       if (isDetached())
/*      */       {
/* 1984 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 1986 */       throw exc;
/*      */     }
/* 1988 */     catch (Exception exc) {
/*      */       
/* 1990 */       if (!isDetached())
/*      */       {
/* 1992 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1994 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateInt(String a, int b) throws SQLException {
/*      */     try {
/* 2002 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2004 */       this.inner.updateInt(a, b);
/*      */     }
/* 2006 */     catch (NullPointerException exc) {
/*      */       
/* 2008 */       if (isDetached())
/*      */       {
/* 2010 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2012 */       throw exc;
/*      */     }
/* 2014 */     catch (Exception exc) {
/*      */       
/* 2016 */       if (!isDetached())
/*      */       {
/* 2018 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2020 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateInt(int a, int b) throws SQLException {
/*      */     try {
/* 2028 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2030 */       this.inner.updateInt(a, b);
/*      */     }
/* 2032 */     catch (NullPointerException exc) {
/*      */       
/* 2034 */       if (isDetached())
/*      */       {
/* 2036 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2038 */       throw exc;
/*      */     }
/* 2040 */     catch (Exception exc) {
/*      */       
/* 2042 */       if (!isDetached())
/*      */       {
/* 2044 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2046 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateLong(int a, long b) throws SQLException {
/*      */     try {
/* 2054 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2056 */       this.inner.updateLong(a, b);
/*      */     }
/* 2058 */     catch (NullPointerException exc) {
/*      */       
/* 2060 */       if (isDetached())
/*      */       {
/* 2062 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2064 */       throw exc;
/*      */     }
/* 2066 */     catch (Exception exc) {
/*      */       
/* 2068 */       if (!isDetached())
/*      */       {
/* 2070 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2072 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateLong(String a, long b) throws SQLException {
/*      */     try {
/* 2080 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2082 */       this.inner.updateLong(a, b);
/*      */     }
/* 2084 */     catch (NullPointerException exc) {
/*      */       
/* 2086 */       if (isDetached())
/*      */       {
/* 2088 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2090 */       throw exc;
/*      */     }
/* 2092 */     catch (Exception exc) {
/*      */       
/* 2094 */       if (!isDetached())
/*      */       {
/* 2096 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2098 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateFloat(int a, float b) throws SQLException {
/*      */     try {
/* 2106 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2108 */       this.inner.updateFloat(a, b);
/*      */     }
/* 2110 */     catch (NullPointerException exc) {
/*      */       
/* 2112 */       if (isDetached())
/*      */       {
/* 2114 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2116 */       throw exc;
/*      */     }
/* 2118 */     catch (Exception exc) {
/*      */       
/* 2120 */       if (!isDetached())
/*      */       {
/* 2122 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2124 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateFloat(String a, float b) throws SQLException {
/*      */     try {
/* 2132 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2134 */       this.inner.updateFloat(a, b);
/*      */     }
/* 2136 */     catch (NullPointerException exc) {
/*      */       
/* 2138 */       if (isDetached())
/*      */       {
/* 2140 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2142 */       throw exc;
/*      */     }
/* 2144 */     catch (Exception exc) {
/*      */       
/* 2146 */       if (!isDetached())
/*      */       {
/* 2148 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2150 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateDouble(String a, double b) throws SQLException {
/*      */     try {
/* 2158 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2160 */       this.inner.updateDouble(a, b);
/*      */     }
/* 2162 */     catch (NullPointerException exc) {
/*      */       
/* 2164 */       if (isDetached())
/*      */       {
/* 2166 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2168 */       throw exc;
/*      */     }
/* 2170 */     catch (Exception exc) {
/*      */       
/* 2172 */       if (!isDetached())
/*      */       {
/* 2174 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2176 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateDouble(int a, double b) throws SQLException {
/*      */     try {
/* 2184 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2186 */       this.inner.updateDouble(a, b);
/*      */     }
/* 2188 */     catch (NullPointerException exc) {
/*      */       
/* 2190 */       if (isDetached())
/*      */       {
/* 2192 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2194 */       throw exc;
/*      */     }
/* 2196 */     catch (Exception exc) {
/*      */       
/* 2198 */       if (!isDetached())
/*      */       {
/* 2200 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2202 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBigDecimal(String a, BigDecimal b) throws SQLException {
/*      */     try {
/* 2210 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2212 */       this.inner.updateBigDecimal(a, b);
/*      */     }
/* 2214 */     catch (NullPointerException exc) {
/*      */       
/* 2216 */       if (isDetached())
/*      */       {
/* 2218 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2220 */       throw exc;
/*      */     }
/* 2222 */     catch (Exception exc) {
/*      */       
/* 2224 */       if (!isDetached())
/*      */       {
/* 2226 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2228 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBigDecimal(int a, BigDecimal b) throws SQLException {
/*      */     try {
/* 2236 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2238 */       this.inner.updateBigDecimal(a, b);
/*      */     }
/* 2240 */     catch (NullPointerException exc) {
/*      */       
/* 2242 */       if (isDetached())
/*      */       {
/* 2244 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateString(int a, String b) throws SQLException {
/*      */     try {
/* 2262 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2264 */       this.inner.updateString(a, b);
/*      */     }
/* 2266 */     catch (NullPointerException exc) {
/*      */       
/* 2268 */       if (isDetached())
/*      */       {
/* 2270 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2272 */       throw exc;
/*      */     }
/* 2274 */     catch (Exception exc) {
/*      */       
/* 2276 */       if (!isDetached())
/*      */       {
/* 2278 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2280 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateString(String a, String b) throws SQLException {
/*      */     try {
/* 2288 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2290 */       this.inner.updateString(a, b);
/*      */     }
/* 2292 */     catch (NullPointerException exc) {
/*      */       
/* 2294 */       if (isDetached())
/*      */       {
/* 2296 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2298 */       throw exc;
/*      */     }
/* 2300 */     catch (Exception exc) {
/*      */       
/* 2302 */       if (!isDetached())
/*      */       {
/* 2304 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2306 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBytes(int a, byte[] b) throws SQLException {
/*      */     try {
/* 2314 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2316 */       this.inner.updateBytes(a, b);
/*      */     }
/* 2318 */     catch (NullPointerException exc) {
/*      */       
/* 2320 */       if (isDetached())
/*      */       {
/* 2322 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2324 */       throw exc;
/*      */     }
/* 2326 */     catch (Exception exc) {
/*      */       
/* 2328 */       if (!isDetached())
/*      */       {
/* 2330 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2332 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBytes(String a, byte[] b) throws SQLException {
/*      */     try {
/* 2340 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2342 */       this.inner.updateBytes(a, b);
/*      */     }
/* 2344 */     catch (NullPointerException exc) {
/*      */       
/* 2346 */       if (isDetached())
/*      */       {
/* 2348 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2350 */       throw exc;
/*      */     }
/* 2352 */     catch (Exception exc) {
/*      */       
/* 2354 */       if (!isDetached())
/*      */       {
/* 2356 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2358 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateDate(String a, Date b) throws SQLException {
/*      */     try {
/* 2366 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2368 */       this.inner.updateDate(a, b);
/*      */     }
/* 2370 */     catch (NullPointerException exc) {
/*      */       
/* 2372 */       if (isDetached())
/*      */       {
/* 2374 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2376 */       throw exc;
/*      */     }
/* 2378 */     catch (Exception exc) {
/*      */       
/* 2380 */       if (!isDetached())
/*      */       {
/* 2382 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2384 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateDate(int a, Date b) throws SQLException {
/*      */     try {
/* 2392 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2394 */       this.inner.updateDate(a, b);
/*      */     }
/* 2396 */     catch (NullPointerException exc) {
/*      */       
/* 2398 */       if (isDetached())
/*      */       {
/* 2400 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2402 */       throw exc;
/*      */     }
/* 2404 */     catch (Exception exc) {
/*      */       
/* 2406 */       if (!isDetached())
/*      */       {
/* 2408 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2410 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateTime(String a, Time b) throws SQLException {
/*      */     try {
/* 2418 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2420 */       this.inner.updateTime(a, b);
/*      */     }
/* 2422 */     catch (NullPointerException exc) {
/*      */       
/* 2424 */       if (isDetached())
/*      */       {
/* 2426 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2428 */       throw exc;
/*      */     }
/* 2430 */     catch (Exception exc) {
/*      */       
/* 2432 */       if (!isDetached())
/*      */       {
/* 2434 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2436 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateTime(int a, Time b) throws SQLException {
/*      */     try {
/* 2444 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2446 */       this.inner.updateTime(a, b);
/*      */     }
/* 2448 */     catch (NullPointerException exc) {
/*      */       
/* 2450 */       if (isDetached())
/*      */       {
/* 2452 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2454 */       throw exc;
/*      */     }
/* 2456 */     catch (Exception exc) {
/*      */       
/* 2458 */       if (!isDetached())
/*      */       {
/* 2460 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2462 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateTimestamp(String a, Timestamp b) throws SQLException {
/*      */     try {
/* 2470 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2472 */       this.inner.updateTimestamp(a, b);
/*      */     }
/* 2474 */     catch (NullPointerException exc) {
/*      */       
/* 2476 */       if (isDetached())
/*      */       {
/* 2478 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2480 */       throw exc;
/*      */     }
/* 2482 */     catch (Exception exc) {
/*      */       
/* 2484 */       if (!isDetached())
/*      */       {
/* 2486 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2488 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateTimestamp(int a, Timestamp b) throws SQLException {
/*      */     try {
/* 2496 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2498 */       this.inner.updateTimestamp(a, b);
/*      */     }
/* 2500 */     catch (NullPointerException exc) {
/*      */       
/* 2502 */       if (isDetached())
/*      */       {
/* 2504 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2506 */       throw exc;
/*      */     }
/* 2508 */     catch (Exception exc) {
/*      */       
/* 2510 */       if (!isDetached())
/*      */       {
/* 2512 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2514 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateAsciiStream(String a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 2522 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2524 */       this.inner.updateAsciiStream(a, b, c);
/*      */     }
/* 2526 */     catch (NullPointerException exc) {
/*      */       
/* 2528 */       if (isDetached())
/*      */       {
/* 2530 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2532 */       throw exc;
/*      */     }
/* 2534 */     catch (Exception exc) {
/*      */       
/* 2536 */       if (!isDetached())
/*      */       {
/* 2538 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2540 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateAsciiStream(int a, InputStream b) throws SQLException {
/*      */     try {
/* 2548 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2550 */       this.inner.updateAsciiStream(a, b);
/*      */     }
/* 2552 */     catch (NullPointerException exc) {
/*      */       
/* 2554 */       if (isDetached())
/*      */       {
/* 2556 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateAsciiStream(String a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 2574 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2576 */       this.inner.updateAsciiStream(a, b, c);
/*      */     }
/* 2578 */     catch (NullPointerException exc) {
/*      */       
/* 2580 */       if (isDetached())
/*      */       {
/* 2582 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2584 */       throw exc;
/*      */     }
/* 2586 */     catch (Exception exc) {
/*      */       
/* 2588 */       if (!isDetached())
/*      */       {
/* 2590 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2592 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateAsciiStream(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 2600 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2602 */       this.inner.updateAsciiStream(a, b, c);
/*      */     }
/* 2604 */     catch (NullPointerException exc) {
/*      */       
/* 2606 */       if (isDetached())
/*      */       {
/* 2608 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2610 */       throw exc;
/*      */     }
/* 2612 */     catch (Exception exc) {
/*      */       
/* 2614 */       if (!isDetached())
/*      */       {
/* 2616 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2618 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateAsciiStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 2626 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2628 */       this.inner.updateAsciiStream(a, b, c);
/*      */     }
/* 2630 */     catch (NullPointerException exc) {
/*      */       
/* 2632 */       if (isDetached())
/*      */       {
/* 2634 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2636 */       throw exc;
/*      */     }
/* 2638 */     catch (Exception exc) {
/*      */       
/* 2640 */       if (!isDetached())
/*      */       {
/* 2642 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2644 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateAsciiStream(String a, InputStream b) throws SQLException {
/*      */     try {
/* 2652 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2654 */       this.inner.updateAsciiStream(a, b);
/*      */     }
/* 2656 */     catch (NullPointerException exc) {
/*      */       
/* 2658 */       if (isDetached())
/*      */       {
/* 2660 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2662 */       throw exc;
/*      */     }
/* 2664 */     catch (Exception exc) {
/*      */       
/* 2666 */       if (!isDetached())
/*      */       {
/* 2668 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2670 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBinaryStream(String a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 2678 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2680 */       this.inner.updateBinaryStream(a, b, c);
/*      */     }
/* 2682 */     catch (NullPointerException exc) {
/*      */       
/* 2684 */       if (isDetached())
/*      */       {
/* 2686 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2688 */       throw exc;
/*      */     }
/* 2690 */     catch (Exception exc) {
/*      */       
/* 2692 */       if (!isDetached())
/*      */       {
/* 2694 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2696 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBinaryStream(int a, InputStream b) throws SQLException {
/*      */     try {
/* 2704 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2706 */       this.inner.updateBinaryStream(a, b);
/*      */     }
/* 2708 */     catch (NullPointerException exc) {
/*      */       
/* 2710 */       if (isDetached())
/*      */       {
/* 2712 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2714 */       throw exc;
/*      */     }
/* 2716 */     catch (Exception exc) {
/*      */       
/* 2718 */       if (!isDetached())
/*      */       {
/* 2720 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2722 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBinaryStream(String a, InputStream b) throws SQLException {
/*      */     try {
/* 2730 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2732 */       this.inner.updateBinaryStream(a, b);
/*      */     }
/* 2734 */     catch (NullPointerException exc) {
/*      */       
/* 2736 */       if (isDetached())
/*      */       {
/* 2738 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2740 */       throw exc;
/*      */     }
/* 2742 */     catch (Exception exc) {
/*      */       
/* 2744 */       if (!isDetached())
/*      */       {
/* 2746 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2748 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBinaryStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 2756 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2758 */       this.inner.updateBinaryStream(a, b, c);
/*      */     }
/* 2760 */     catch (NullPointerException exc) {
/*      */       
/* 2762 */       if (isDetached())
/*      */       {
/* 2764 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2766 */       throw exc;
/*      */     }
/* 2768 */     catch (Exception exc) {
/*      */       
/* 2770 */       if (!isDetached())
/*      */       {
/* 2772 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2774 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBinaryStream(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 2782 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2784 */       this.inner.updateBinaryStream(a, b, c);
/*      */     }
/* 2786 */     catch (NullPointerException exc) {
/*      */       
/* 2788 */       if (isDetached())
/*      */       {
/* 2790 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2792 */       throw exc;
/*      */     }
/* 2794 */     catch (Exception exc) {
/*      */       
/* 2796 */       if (!isDetached())
/*      */       {
/* 2798 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2800 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBinaryStream(String a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 2808 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2810 */       this.inner.updateBinaryStream(a, b, c);
/*      */     }
/* 2812 */     catch (NullPointerException exc) {
/*      */       
/* 2814 */       if (isDetached())
/*      */       {
/* 2816 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2818 */       throw exc;
/*      */     }
/* 2820 */     catch (Exception exc) {
/*      */       
/* 2822 */       if (!isDetached())
/*      */       {
/* 2824 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2826 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateCharacterStream(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 2834 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2836 */       this.inner.updateCharacterStream(a, b, c);
/*      */     }
/* 2838 */     catch (NullPointerException exc) {
/*      */       
/* 2840 */       if (isDetached())
/*      */       {
/* 2842 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2844 */       throw exc;
/*      */     }
/* 2846 */     catch (Exception exc) {
/*      */       
/* 2848 */       if (!isDetached())
/*      */       {
/* 2850 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2852 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateCharacterStream(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 2860 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2862 */       this.inner.updateCharacterStream(a, b, c);
/*      */     }
/* 2864 */     catch (NullPointerException exc) {
/*      */       
/* 2866 */       if (isDetached())
/*      */       {
/* 2868 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateCharacterStream(int a, Reader b) throws SQLException {
/*      */     try {
/* 2886 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2888 */       this.inner.updateCharacterStream(a, b);
/*      */     }
/* 2890 */     catch (NullPointerException exc) {
/*      */       
/* 2892 */       if (isDetached())
/*      */       {
/* 2894 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2896 */       throw exc;
/*      */     }
/* 2898 */     catch (Exception exc) {
/*      */       
/* 2900 */       if (!isDetached())
/*      */       {
/* 2902 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2904 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateCharacterStream(String a, Reader b) throws SQLException {
/*      */     try {
/* 2912 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2914 */       this.inner.updateCharacterStream(a, b);
/*      */     }
/* 2916 */     catch (NullPointerException exc) {
/*      */       
/* 2918 */       if (isDetached())
/*      */       {
/* 2920 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2922 */       throw exc;
/*      */     }
/* 2924 */     catch (Exception exc) {
/*      */       
/* 2926 */       if (!isDetached())
/*      */       {
/* 2928 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2930 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateCharacterStream(String a, Reader b, int c) throws SQLException {
/*      */     try {
/* 2938 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2940 */       this.inner.updateCharacterStream(a, b, c);
/*      */     }
/* 2942 */     catch (NullPointerException exc) {
/*      */       
/* 2944 */       if (isDetached())
/*      */       {
/* 2946 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2948 */       throw exc;
/*      */     }
/* 2950 */     catch (Exception exc) {
/*      */       
/* 2952 */       if (!isDetached())
/*      */       {
/* 2954 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2956 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateCharacterStream(int a, Reader b, int c) throws SQLException {
/*      */     try {
/* 2964 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2966 */       this.inner.updateCharacterStream(a, b, c);
/*      */     }
/* 2968 */     catch (NullPointerException exc) {
/*      */       
/* 2970 */       if (isDetached())
/*      */       {
/* 2972 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 2974 */       throw exc;
/*      */     }
/* 2976 */     catch (Exception exc) {
/*      */       
/* 2978 */       if (!isDetached())
/*      */       {
/* 2980 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2982 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateObject(int a, Object b) throws SQLException {
/*      */     try {
/* 2990 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 2992 */       this.inner.updateObject(a, b);
/*      */     }
/* 2994 */     catch (NullPointerException exc) {
/*      */       
/* 2996 */       if (isDetached())
/*      */       {
/* 2998 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3000 */       throw exc;
/*      */     }
/* 3002 */     catch (Exception exc) {
/*      */       
/* 3004 */       if (!isDetached())
/*      */       {
/* 3006 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3008 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateObject(int a, Object b, int c) throws SQLException {
/*      */     try {
/* 3016 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3018 */       this.inner.updateObject(a, b, c);
/*      */     }
/* 3020 */     catch (NullPointerException exc) {
/*      */       
/* 3022 */       if (isDetached())
/*      */       {
/* 3024 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3026 */       throw exc;
/*      */     }
/* 3028 */     catch (Exception exc) {
/*      */       
/* 3030 */       if (!isDetached())
/*      */       {
/* 3032 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3034 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateObject(String a, Object b, int c) throws SQLException {
/*      */     try {
/* 3042 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3044 */       this.inner.updateObject(a, b, c);
/*      */     }
/* 3046 */     catch (NullPointerException exc) {
/*      */       
/* 3048 */       if (isDetached())
/*      */       {
/* 3050 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3052 */       throw exc;
/*      */     }
/* 3054 */     catch (Exception exc) {
/*      */       
/* 3056 */       if (!isDetached())
/*      */       {
/* 3058 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3060 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateObject(String a, Object b) throws SQLException {
/*      */     try {
/* 3068 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3070 */       this.inner.updateObject(a, b);
/*      */     }
/* 3072 */     catch (NullPointerException exc) {
/*      */       
/* 3074 */       if (isDetached())
/*      */       {
/* 3076 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3078 */       throw exc;
/*      */     }
/* 3080 */     catch (Exception exc) {
/*      */       
/* 3082 */       if (!isDetached())
/*      */       {
/* 3084 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3086 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void insertRow() throws SQLException {
/*      */     try {
/* 3094 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3096 */       this.inner.insertRow();
/*      */     }
/* 3098 */     catch (NullPointerException exc) {
/*      */       
/* 3100 */       if (isDetached())
/*      */       {
/* 3102 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3104 */       throw exc;
/*      */     }
/* 3106 */     catch (Exception exc) {
/*      */       
/* 3108 */       if (!isDetached())
/*      */       {
/* 3110 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3112 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateRow() throws SQLException {
/*      */     try {
/* 3120 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3122 */       this.inner.updateRow();
/*      */     }
/* 3124 */     catch (NullPointerException exc) {
/*      */       
/* 3126 */       if (isDetached())
/*      */       {
/* 3128 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3130 */       throw exc;
/*      */     }
/* 3132 */     catch (Exception exc) {
/*      */       
/* 3134 */       if (!isDetached())
/*      */       {
/* 3136 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3138 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void deleteRow() throws SQLException {
/*      */     try {
/* 3146 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3148 */       this.inner.deleteRow();
/*      */     }
/* 3150 */     catch (NullPointerException exc) {
/*      */       
/* 3152 */       if (isDetached())
/*      */       {
/* 3154 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3156 */       throw exc;
/*      */     }
/* 3158 */     catch (Exception exc) {
/*      */       
/* 3160 */       if (!isDetached())
/*      */       {
/* 3162 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3164 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void refreshRow() throws SQLException {
/*      */     try {
/* 3172 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3174 */       this.inner.refreshRow();
/*      */     }
/* 3176 */     catch (NullPointerException exc) {
/*      */       
/* 3178 */       if (isDetached())
/*      */       {
/* 3180 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3182 */       throw exc;
/*      */     }
/* 3184 */     catch (Exception exc) {
/*      */       
/* 3186 */       if (!isDetached())
/*      */       {
/* 3188 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3190 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void cancelRowUpdates() throws SQLException {
/*      */     try {
/* 3198 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3200 */       this.inner.cancelRowUpdates();
/*      */     }
/* 3202 */     catch (NullPointerException exc) {
/*      */       
/* 3204 */       if (isDetached())
/*      */       {
/* 3206 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3208 */       throw exc;
/*      */     }
/* 3210 */     catch (Exception exc) {
/*      */       
/* 3212 */       if (!isDetached())
/*      */       {
/* 3214 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3216 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void moveToInsertRow() throws SQLException {
/*      */     try {
/* 3224 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3226 */       this.inner.moveToInsertRow();
/*      */     }
/* 3228 */     catch (NullPointerException exc) {
/*      */       
/* 3230 */       if (isDetached())
/*      */       {
/* 3232 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3234 */       throw exc;
/*      */     }
/* 3236 */     catch (Exception exc) {
/*      */       
/* 3238 */       if (!isDetached())
/*      */       {
/* 3240 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3242 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void moveToCurrentRow() throws SQLException {
/*      */     try {
/* 3250 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3252 */       this.inner.moveToCurrentRow();
/*      */     }
/* 3254 */     catch (NullPointerException exc) {
/*      */       
/* 3256 */       if (isDetached())
/*      */       {
/* 3258 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3260 */       throw exc;
/*      */     }
/* 3262 */     catch (Exception exc) {
/*      */       
/* 3264 */       if (!isDetached())
/*      */       {
/* 3266 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3268 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateRef(String a, Ref b) throws SQLException {
/*      */     try {
/* 3276 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3278 */       this.inner.updateRef(a, b);
/*      */     }
/* 3280 */     catch (NullPointerException exc) {
/*      */       
/* 3282 */       if (isDetached())
/*      */       {
/* 3284 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3286 */       throw exc;
/*      */     }
/* 3288 */     catch (Exception exc) {
/*      */       
/* 3290 */       if (!isDetached())
/*      */       {
/* 3292 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3294 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateRef(int a, Ref b) throws SQLException {
/*      */     try {
/* 3302 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3304 */       this.inner.updateRef(a, b);
/*      */     }
/* 3306 */     catch (NullPointerException exc) {
/*      */       
/* 3308 */       if (isDetached())
/*      */       {
/* 3310 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3312 */       throw exc;
/*      */     }
/* 3314 */     catch (Exception exc) {
/*      */       
/* 3316 */       if (!isDetached())
/*      */       {
/* 3318 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3320 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBlob(int a, Blob b) throws SQLException {
/*      */     try {
/* 3328 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3330 */       this.inner.updateBlob(a, b);
/*      */     }
/* 3332 */     catch (NullPointerException exc) {
/*      */       
/* 3334 */       if (isDetached())
/*      */       {
/* 3336 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3338 */       throw exc;
/*      */     }
/* 3340 */     catch (Exception exc) {
/*      */       
/* 3342 */       if (!isDetached())
/*      */       {
/* 3344 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3346 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBlob(String a, Blob b) throws SQLException {
/*      */     try {
/* 3354 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3356 */       this.inner.updateBlob(a, b);
/*      */     }
/* 3358 */     catch (NullPointerException exc) {
/*      */       
/* 3360 */       if (isDetached())
/*      */       {
/* 3362 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3364 */       throw exc;
/*      */     }
/* 3366 */     catch (Exception exc) {
/*      */       
/* 3368 */       if (!isDetached())
/*      */       {
/* 3370 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3372 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBlob(String a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 3380 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3382 */       this.inner.updateBlob(a, b, c);
/*      */     }
/* 3384 */     catch (NullPointerException exc) {
/*      */       
/* 3386 */       if (isDetached())
/*      */       {
/* 3388 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3390 */       throw exc;
/*      */     }
/* 3392 */     catch (Exception exc) {
/*      */       
/* 3394 */       if (!isDetached())
/*      */       {
/* 3396 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3398 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBlob(String a, InputStream b) throws SQLException {
/*      */     try {
/* 3406 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3408 */       this.inner.updateBlob(a, b);
/*      */     }
/* 3410 */     catch (NullPointerException exc) {
/*      */       
/* 3412 */       if (isDetached())
/*      */       {
/* 3414 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3416 */       throw exc;
/*      */     }
/* 3418 */     catch (Exception exc) {
/*      */       
/* 3420 */       if (!isDetached())
/*      */       {
/* 3422 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3424 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBlob(int a, InputStream b) throws SQLException {
/*      */     try {
/* 3432 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3434 */       this.inner.updateBlob(a, b);
/*      */     }
/* 3436 */     catch (NullPointerException exc) {
/*      */       
/* 3438 */       if (isDetached())
/*      */       {
/* 3440 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3442 */       throw exc;
/*      */     }
/* 3444 */     catch (Exception exc) {
/*      */       
/* 3446 */       if (!isDetached())
/*      */       {
/* 3448 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3450 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateBlob(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 3458 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3460 */       this.inner.updateBlob(a, b, c);
/*      */     }
/* 3462 */     catch (NullPointerException exc) {
/*      */       
/* 3464 */       if (isDetached())
/*      */       {
/* 3466 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3468 */       throw exc;
/*      */     }
/* 3470 */     catch (Exception exc) {
/*      */       
/* 3472 */       if (!isDetached())
/*      */       {
/* 3474 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3476 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateClob(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 3484 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3486 */       this.inner.updateClob(a, b, c);
/*      */     }
/* 3488 */     catch (NullPointerException exc) {
/*      */       
/* 3490 */       if (isDetached())
/*      */       {
/* 3492 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3494 */       throw exc;
/*      */     }
/* 3496 */     catch (Exception exc) {
/*      */       
/* 3498 */       if (!isDetached())
/*      */       {
/* 3500 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3502 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateClob(int a, Clob b) throws SQLException {
/*      */     try {
/* 3510 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3512 */       this.inner.updateClob(a, b);
/*      */     }
/* 3514 */     catch (NullPointerException exc) {
/*      */       
/* 3516 */       if (isDetached())
/*      */       {
/* 3518 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3520 */       throw exc;
/*      */     }
/* 3522 */     catch (Exception exc) {
/*      */       
/* 3524 */       if (!isDetached())
/*      */       {
/* 3526 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3528 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateClob(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 3536 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3538 */       this.inner.updateClob(a, b, c);
/*      */     }
/* 3540 */     catch (NullPointerException exc) {
/*      */       
/* 3542 */       if (isDetached())
/*      */       {
/* 3544 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3546 */       throw exc;
/*      */     }
/* 3548 */     catch (Exception exc) {
/*      */       
/* 3550 */       if (!isDetached())
/*      */       {
/* 3552 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3554 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateClob(String a, Clob b) throws SQLException {
/*      */     try {
/* 3562 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3564 */       this.inner.updateClob(a, b);
/*      */     }
/* 3566 */     catch (NullPointerException exc) {
/*      */       
/* 3568 */       if (isDetached())
/*      */       {
/* 3570 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3572 */       throw exc;
/*      */     }
/* 3574 */     catch (Exception exc) {
/*      */       
/* 3576 */       if (!isDetached())
/*      */       {
/* 3578 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3580 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateClob(int a, Reader b) throws SQLException {
/*      */     try {
/* 3588 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3590 */       this.inner.updateClob(a, b);
/*      */     }
/* 3592 */     catch (NullPointerException exc) {
/*      */       
/* 3594 */       if (isDetached())
/*      */       {
/* 3596 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3598 */       throw exc;
/*      */     }
/* 3600 */     catch (Exception exc) {
/*      */       
/* 3602 */       if (!isDetached())
/*      */       {
/* 3604 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3606 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateClob(String a, Reader b) throws SQLException {
/*      */     try {
/* 3614 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3616 */       this.inner.updateClob(a, b);
/*      */     }
/* 3618 */     catch (NullPointerException exc) {
/*      */       
/* 3620 */       if (isDetached())
/*      */       {
/* 3622 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3624 */       throw exc;
/*      */     }
/* 3626 */     catch (Exception exc) {
/*      */       
/* 3628 */       if (!isDetached())
/*      */       {
/* 3630 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3632 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateArray(String a, Array b) throws SQLException {
/*      */     try {
/* 3640 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3642 */       this.inner.updateArray(a, b);
/*      */     }
/* 3644 */     catch (NullPointerException exc) {
/*      */       
/* 3646 */       if (isDetached())
/*      */       {
/* 3648 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateArray(int a, Array b) throws SQLException {
/*      */     try {
/* 3666 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3668 */       this.inner.updateArray(a, b);
/*      */     }
/* 3670 */     catch (NullPointerException exc) {
/*      */       
/* 3672 */       if (isDetached())
/*      */       {
/* 3674 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateRowId(int a, RowId b) throws SQLException {
/*      */     try {
/* 3692 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3694 */       this.inner.updateRowId(a, b);
/*      */     }
/* 3696 */     catch (NullPointerException exc) {
/*      */       
/* 3698 */       if (isDetached())
/*      */       {
/* 3700 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3702 */       throw exc;
/*      */     }
/* 3704 */     catch (Exception exc) {
/*      */       
/* 3706 */       if (!isDetached())
/*      */       {
/* 3708 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3710 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateRowId(String a, RowId b) throws SQLException {
/*      */     try {
/* 3718 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3720 */       this.inner.updateRowId(a, b);
/*      */     }
/* 3722 */     catch (NullPointerException exc) {
/*      */       
/* 3724 */       if (isDetached())
/*      */       {
/* 3726 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3728 */       throw exc;
/*      */     }
/* 3730 */     catch (Exception exc) {
/*      */       
/* 3732 */       if (!isDetached())
/*      */       {
/* 3734 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3736 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNString(String a, String b) throws SQLException {
/*      */     try {
/* 3744 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3746 */       this.inner.updateNString(a, b);
/*      */     }
/* 3748 */     catch (NullPointerException exc) {
/*      */       
/* 3750 */       if (isDetached())
/*      */       {
/* 3752 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3754 */       throw exc;
/*      */     }
/* 3756 */     catch (Exception exc) {
/*      */       
/* 3758 */       if (!isDetached())
/*      */       {
/* 3760 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3762 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNString(int a, String b) throws SQLException {
/*      */     try {
/* 3770 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3772 */       this.inner.updateNString(a, b);
/*      */     }
/* 3774 */     catch (NullPointerException exc) {
/*      */       
/* 3776 */       if (isDetached())
/*      */       {
/* 3778 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3780 */       throw exc;
/*      */     }
/* 3782 */     catch (Exception exc) {
/*      */       
/* 3784 */       if (!isDetached())
/*      */       {
/* 3786 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3788 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNClob(String a, NClob b) throws SQLException {
/*      */     try {
/* 3796 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3798 */       this.inner.updateNClob(a, b);
/*      */     }
/* 3800 */     catch (NullPointerException exc) {
/*      */       
/* 3802 */       if (isDetached())
/*      */       {
/* 3804 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3806 */       throw exc;
/*      */     }
/* 3808 */     catch (Exception exc) {
/*      */       
/* 3810 */       if (!isDetached())
/*      */       {
/* 3812 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3814 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNClob(int a, NClob b) throws SQLException {
/*      */     try {
/* 3822 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3824 */       this.inner.updateNClob(a, b);
/*      */     }
/* 3826 */     catch (NullPointerException exc) {
/*      */       
/* 3828 */       if (isDetached())
/*      */       {
/* 3830 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3832 */       throw exc;
/*      */     }
/* 3834 */     catch (Exception exc) {
/*      */       
/* 3836 */       if (!isDetached())
/*      */       {
/* 3838 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3840 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNClob(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 3848 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3850 */       this.inner.updateNClob(a, b, c);
/*      */     }
/* 3852 */     catch (NullPointerException exc) {
/*      */       
/* 3854 */       if (isDetached())
/*      */       {
/* 3856 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3858 */       throw exc;
/*      */     }
/* 3860 */     catch (Exception exc) {
/*      */       
/* 3862 */       if (!isDetached())
/*      */       {
/* 3864 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3866 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNClob(int a, Reader b) throws SQLException {
/*      */     try {
/* 3874 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3876 */       this.inner.updateNClob(a, b);
/*      */     }
/* 3878 */     catch (NullPointerException exc) {
/*      */       
/* 3880 */       if (isDetached())
/*      */       {
/* 3882 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3884 */       throw exc;
/*      */     }
/* 3886 */     catch (Exception exc) {
/*      */       
/* 3888 */       if (!isDetached())
/*      */       {
/* 3890 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3892 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNClob(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 3900 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3902 */       this.inner.updateNClob(a, b, c);
/*      */     }
/* 3904 */     catch (NullPointerException exc) {
/*      */       
/* 3906 */       if (isDetached())
/*      */       {
/* 3908 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3910 */       throw exc;
/*      */     }
/* 3912 */     catch (Exception exc) {
/*      */       
/* 3914 */       if (!isDetached())
/*      */       {
/* 3916 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3918 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNClob(String a, Reader b) throws SQLException {
/*      */     try {
/* 3926 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3928 */       this.inner.updateNClob(a, b);
/*      */     }
/* 3930 */     catch (NullPointerException exc) {
/*      */       
/* 3932 */       if (isDetached())
/*      */       {
/* 3934 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3936 */       throw exc;
/*      */     }
/* 3938 */     catch (Exception exc) {
/*      */       
/* 3940 */       if (!isDetached())
/*      */       {
/* 3942 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3944 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateSQLXML(int a, SQLXML b) throws SQLException {
/*      */     try {
/* 3952 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3954 */       this.inner.updateSQLXML(a, b);
/*      */     }
/* 3956 */     catch (NullPointerException exc) {
/*      */       
/* 3958 */       if (isDetached())
/*      */       {
/* 3960 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3962 */       throw exc;
/*      */     }
/* 3964 */     catch (Exception exc) {
/*      */       
/* 3966 */       if (!isDetached())
/*      */       {
/* 3968 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3970 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateSQLXML(String a, SQLXML b) throws SQLException {
/*      */     try {
/* 3978 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 3980 */       this.inner.updateSQLXML(a, b);
/*      */     }
/* 3982 */     catch (NullPointerException exc) {
/*      */       
/* 3984 */       if (isDetached())
/*      */       {
/* 3986 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 3988 */       throw exc;
/*      */     }
/* 3990 */     catch (Exception exc) {
/*      */       
/* 3992 */       if (!isDetached())
/*      */       {
/* 3994 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3996 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNCharacterStream(int a, Reader b) throws SQLException {
/*      */     try {
/* 4004 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4006 */       this.inner.updateNCharacterStream(a, b);
/*      */     }
/* 4008 */     catch (NullPointerException exc) {
/*      */       
/* 4010 */       if (isDetached())
/*      */       {
/* 4012 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4014 */       throw exc;
/*      */     }
/* 4016 */     catch (Exception exc) {
/*      */       
/* 4018 */       if (!isDetached())
/*      */       {
/* 4020 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4022 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNCharacterStream(String a, Reader b) throws SQLException {
/*      */     try {
/* 4030 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4032 */       this.inner.updateNCharacterStream(a, b);
/*      */     }
/* 4034 */     catch (NullPointerException exc) {
/*      */       
/* 4036 */       if (isDetached())
/*      */       {
/* 4038 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
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
/*      */   public final void updateNCharacterStream(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 4056 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4058 */       this.inner.updateNCharacterStream(a, b, c);
/*      */     }
/* 4060 */     catch (NullPointerException exc) {
/*      */       
/* 4062 */       if (isDetached())
/*      */       {
/* 4064 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4066 */       throw exc;
/*      */     }
/* 4068 */     catch (Exception exc) {
/*      */       
/* 4070 */       if (!isDetached())
/*      */       {
/* 4072 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4074 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void updateNCharacterStream(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 4082 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4084 */       this.inner.updateNCharacterStream(a, b, c);
/*      */     }
/* 4086 */     catch (NullPointerException exc) {
/*      */       
/* 4088 */       if (isDetached())
/*      */       {
/* 4090 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4092 */       throw exc;
/*      */     }
/* 4094 */     catch (Exception exc) {
/*      */       
/* 4096 */       if (!isDetached())
/*      */       {
/* 4098 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4100 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(int a) throws SQLException {
/*      */     try {
/* 4108 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4110 */       return this.inner.getTimestamp(a);
/*      */     }
/* 4112 */     catch (NullPointerException exc) {
/*      */       
/* 4114 */       if (isDetached())
/*      */       {
/* 4116 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4118 */       throw exc;
/*      */     }
/* 4120 */     catch (Exception exc) {
/*      */       
/* 4122 */       if (!isDetached())
/*      */       {
/* 4124 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4126 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(String a, Calendar b) throws SQLException {
/*      */     try {
/* 4134 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4136 */       return this.inner.getTimestamp(a, b);
/*      */     }
/* 4138 */     catch (NullPointerException exc) {
/*      */       
/* 4140 */       if (isDetached())
/*      */       {
/* 4142 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4144 */       throw exc;
/*      */     }
/* 4146 */     catch (Exception exc) {
/*      */       
/* 4148 */       if (!isDetached())
/*      */       {
/* 4150 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4152 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(String a) throws SQLException {
/*      */     try {
/* 4160 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4162 */       return this.inner.getTimestamp(a);
/*      */     }
/* 4164 */     catch (NullPointerException exc) {
/*      */       
/* 4166 */       if (isDetached())
/*      */       {
/* 4168 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4170 */       throw exc;
/*      */     }
/* 4172 */     catch (Exception exc) {
/*      */       
/* 4174 */       if (!isDetached())
/*      */       {
/* 4176 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4178 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(int a, Calendar b) throws SQLException {
/*      */     try {
/* 4186 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4188 */       return this.inner.getTimestamp(a, b);
/*      */     }
/* 4190 */     catch (NullPointerException exc) {
/*      */       
/* 4192 */       if (isDetached())
/*      */       {
/* 4194 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4196 */       throw exc;
/*      */     }
/* 4198 */     catch (Exception exc) {
/*      */       
/* 4200 */       if (!isDetached())
/*      */       {
/* 4202 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4204 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchDirection(int a) throws SQLException {
/*      */     try {
/* 4212 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4214 */       this.inner.setFetchDirection(a);
/*      */     }
/* 4216 */     catch (NullPointerException exc) {
/*      */       
/* 4218 */       if (isDetached())
/*      */       {
/* 4220 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4222 */       throw exc;
/*      */     }
/* 4224 */     catch (Exception exc) {
/*      */       
/* 4226 */       if (!isDetached())
/*      */       {
/* 4228 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4230 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchDirection() throws SQLException {
/*      */     try {
/* 4238 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4240 */       return this.inner.getFetchDirection();
/*      */     }
/* 4242 */     catch (NullPointerException exc) {
/*      */       
/* 4244 */       if (isDetached())
/*      */       {
/* 4246 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4248 */       throw exc;
/*      */     }
/* 4250 */     catch (Exception exc) {
/*      */       
/* 4252 */       if (!isDetached())
/*      */       {
/* 4254 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4256 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchSize(int a) throws SQLException {
/*      */     try {
/* 4264 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4266 */       this.inner.setFetchSize(a);
/*      */     }
/* 4268 */     catch (NullPointerException exc) {
/*      */       
/* 4270 */       if (isDetached())
/*      */       {
/* 4272 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4274 */       throw exc;
/*      */     }
/* 4276 */     catch (Exception exc) {
/*      */       
/* 4278 */       if (!isDetached())
/*      */       {
/* 4280 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4282 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchSize() throws SQLException {
/*      */     try {
/* 4290 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4292 */       return this.inner.getFetchSize();
/*      */     }
/* 4294 */     catch (NullPointerException exc) {
/*      */       
/* 4296 */       if (isDetached())
/*      */       {
/* 4298 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4300 */       throw exc;
/*      */     }
/* 4302 */     catch (Exception exc) {
/*      */       
/* 4304 */       if (!isDetached())
/*      */       {
/* 4306 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4308 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getHoldability() throws SQLException {
/*      */     try {
/* 4316 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4318 */       return this.inner.getHoldability();
/*      */     }
/* 4320 */     catch (NullPointerException exc) {
/*      */       
/* 4322 */       if (isDetached())
/*      */       {
/* 4324 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4326 */       throw exc;
/*      */     }
/* 4328 */     catch (Exception exc) {
/*      */       
/* 4330 */       if (!isDetached())
/*      */       {
/* 4332 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4334 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean wasNull() throws SQLException {
/*      */     try {
/* 4342 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4344 */       return this.inner.wasNull();
/*      */     }
/* 4346 */     catch (NullPointerException exc) {
/*      */       
/* 4348 */       if (isDetached())
/*      */       {
/* 4350 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4352 */       throw exc;
/*      */     }
/* 4354 */     catch (Exception exc) {
/*      */       
/* 4356 */       if (!isDetached())
/*      */       {
/* 4358 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4360 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(int a) throws SQLException {
/*      */     try {
/* 4368 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4370 */       return this.inner.getBigDecimal(a);
/*      */     }
/* 4372 */     catch (NullPointerException exc) {
/*      */       
/* 4374 */       if (isDetached())
/*      */       {
/* 4376 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4378 */       throw exc;
/*      */     }
/* 4380 */     catch (Exception exc) {
/*      */       
/* 4382 */       if (!isDetached())
/*      */       {
/* 4384 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4386 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(String a, int b) throws SQLException {
/*      */     try {
/* 4394 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4396 */       return this.inner.getBigDecimal(a, b);
/*      */     }
/* 4398 */     catch (NullPointerException exc) {
/*      */       
/* 4400 */       if (isDetached())
/*      */       {
/* 4402 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4404 */       throw exc;
/*      */     }
/* 4406 */     catch (Exception exc) {
/*      */       
/* 4408 */       if (!isDetached())
/*      */       {
/* 4410 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4412 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(int a, int b) throws SQLException {
/*      */     try {
/* 4420 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4422 */       return this.inner.getBigDecimal(a, b);
/*      */     }
/* 4424 */     catch (NullPointerException exc) {
/*      */       
/* 4426 */       if (isDetached())
/*      */       {
/* 4428 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4430 */       throw exc;
/*      */     }
/* 4432 */     catch (Exception exc) {
/*      */       
/* 4434 */       if (!isDetached())
/*      */       {
/* 4436 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4438 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(String a) throws SQLException {
/*      */     try {
/* 4446 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4448 */       return this.inner.getBigDecimal(a);
/*      */     }
/* 4450 */     catch (NullPointerException exc) {
/*      */       
/* 4452 */       if (isDetached())
/*      */       {
/* 4454 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4456 */       throw exc;
/*      */     }
/* 4458 */     catch (Exception exc) {
/*      */       
/* 4460 */       if (!isDetached())
/*      */       {
/* 4462 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4464 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Blob getBlob(String a) throws SQLException {
/*      */     try {
/* 4472 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4474 */       return this.inner.getBlob(a);
/*      */     }
/* 4476 */     catch (NullPointerException exc) {
/*      */       
/* 4478 */       if (isDetached())
/*      */       {
/* 4480 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4482 */       throw exc;
/*      */     }
/* 4484 */     catch (Exception exc) {
/*      */       
/* 4486 */       if (!isDetached())
/*      */       {
/* 4488 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4490 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Blob getBlob(int a) throws SQLException {
/*      */     try {
/* 4498 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4500 */       return this.inner.getBlob(a);
/*      */     }
/* 4502 */     catch (NullPointerException exc) {
/*      */       
/* 4504 */       if (isDetached())
/*      */       {
/* 4506 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4508 */       throw exc;
/*      */     }
/* 4510 */     catch (Exception exc) {
/*      */       
/* 4512 */       if (!isDetached())
/*      */       {
/* 4514 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4516 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Clob getClob(String a) throws SQLException {
/*      */     try {
/* 4524 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4526 */       return this.inner.getClob(a);
/*      */     }
/* 4528 */     catch (NullPointerException exc) {
/*      */       
/* 4530 */       if (isDetached())
/*      */       {
/* 4532 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4534 */       throw exc;
/*      */     }
/* 4536 */     catch (Exception exc) {
/*      */       
/* 4538 */       if (!isDetached())
/*      */       {
/* 4540 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4542 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Clob getClob(int a) throws SQLException {
/*      */     try {
/* 4550 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4552 */       return this.inner.getClob(a);
/*      */     }
/* 4554 */     catch (NullPointerException exc) {
/*      */       
/* 4556 */       if (isDetached())
/*      */       {
/* 4558 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4560 */       throw exc;
/*      */     }
/* 4562 */     catch (Exception exc) {
/*      */       
/* 4564 */       if (!isDetached())
/*      */       {
/* 4566 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4568 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final RowId getRowId(String a) throws SQLException {
/*      */     try {
/* 4576 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4578 */       return this.inner.getRowId(a);
/*      */     }
/* 4580 */     catch (NullPointerException exc) {
/*      */       
/* 4582 */       if (isDetached())
/*      */       {
/* 4584 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4586 */       throw exc;
/*      */     }
/* 4588 */     catch (Exception exc) {
/*      */       
/* 4590 */       if (!isDetached())
/*      */       {
/* 4592 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4594 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final RowId getRowId(int a) throws SQLException {
/*      */     try {
/* 4602 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4604 */       return this.inner.getRowId(a);
/*      */     }
/* 4606 */     catch (NullPointerException exc) {
/*      */       
/* 4608 */       if (isDetached())
/*      */       {
/* 4610 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4612 */       throw exc;
/*      */     }
/* 4614 */     catch (Exception exc) {
/*      */       
/* 4616 */       if (!isDetached())
/*      */       {
/* 4618 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4620 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final NClob getNClob(int a) throws SQLException {
/*      */     try {
/* 4628 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4630 */       return this.inner.getNClob(a);
/*      */     }
/* 4632 */     catch (NullPointerException exc) {
/*      */       
/* 4634 */       if (isDetached())
/*      */       {
/* 4636 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4638 */       throw exc;
/*      */     }
/* 4640 */     catch (Exception exc) {
/*      */       
/* 4642 */       if (!isDetached())
/*      */       {
/* 4644 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4646 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final NClob getNClob(String a) throws SQLException {
/*      */     try {
/* 4654 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4656 */       return this.inner.getNClob(a);
/*      */     }
/* 4658 */     catch (NullPointerException exc) {
/*      */       
/* 4660 */       if (isDetached())
/*      */       {
/* 4662 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4664 */       throw exc;
/*      */     }
/* 4666 */     catch (Exception exc) {
/*      */       
/* 4668 */       if (!isDetached())
/*      */       {
/* 4670 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4672 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLXML getSQLXML(int a) throws SQLException {
/*      */     try {
/* 4680 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4682 */       return this.inner.getSQLXML(a);
/*      */     }
/* 4684 */     catch (NullPointerException exc) {
/*      */       
/* 4686 */       if (isDetached())
/*      */       {
/* 4688 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4690 */       throw exc;
/*      */     }
/* 4692 */     catch (Exception exc) {
/*      */       
/* 4694 */       if (!isDetached())
/*      */       {
/* 4696 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4698 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLXML getSQLXML(String a) throws SQLException {
/*      */     try {
/* 4706 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4708 */       return this.inner.getSQLXML(a);
/*      */     }
/* 4710 */     catch (NullPointerException exc) {
/*      */       
/* 4712 */       if (isDetached())
/*      */       {
/* 4714 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4716 */       throw exc;
/*      */     }
/* 4718 */     catch (Exception exc) {
/*      */       
/* 4720 */       if (!isDetached())
/*      */       {
/* 4722 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4724 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNString(String a) throws SQLException {
/*      */     try {
/* 4732 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4734 */       return this.inner.getNString(a);
/*      */     }
/* 4736 */     catch (NullPointerException exc) {
/*      */       
/* 4738 */       if (isDetached())
/*      */       {
/* 4740 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4742 */       throw exc;
/*      */     }
/* 4744 */     catch (Exception exc) {
/*      */       
/* 4746 */       if (!isDetached())
/*      */       {
/* 4748 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4750 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNString(int a) throws SQLException {
/*      */     try {
/* 4758 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4760 */       return this.inner.getNString(a);
/*      */     }
/* 4762 */     catch (NullPointerException exc) {
/*      */       
/* 4764 */       if (isDetached())
/*      */       {
/* 4766 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4768 */       throw exc;
/*      */     }
/* 4770 */     catch (Exception exc) {
/*      */       
/* 4772 */       if (!isDetached())
/*      */       {
/* 4774 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4776 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getNCharacterStream(int a) throws SQLException {
/*      */     try {
/* 4784 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4786 */       return this.inner.getNCharacterStream(a);
/*      */     }
/* 4788 */     catch (NullPointerException exc) {
/*      */       
/* 4790 */       if (isDetached())
/*      */       {
/* 4792 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4794 */       throw exc;
/*      */     }
/* 4796 */     catch (Exception exc) {
/*      */       
/* 4798 */       if (!isDetached())
/*      */       {
/* 4800 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4802 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getNCharacterStream(String a) throws SQLException {
/*      */     try {
/* 4810 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4812 */       return this.inner.getNCharacterStream(a);
/*      */     }
/* 4814 */     catch (NullPointerException exc) {
/*      */       
/* 4816 */       if (isDetached())
/*      */       {
/* 4818 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4820 */       throw exc;
/*      */     }
/* 4822 */     catch (Exception exc) {
/*      */       
/* 4824 */       if (!isDetached())
/*      */       {
/* 4826 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4828 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getCharacterStream(String a) throws SQLException {
/*      */     try {
/* 4836 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4838 */       return this.inner.getCharacterStream(a);
/*      */     }
/* 4840 */     catch (NullPointerException exc) {
/*      */       
/* 4842 */       if (isDetached())
/*      */       {
/* 4844 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4846 */       throw exc;
/*      */     }
/* 4848 */     catch (Exception exc) {
/*      */       
/* 4850 */       if (!isDetached())
/*      */       {
/* 4852 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4854 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getCharacterStream(int a) throws SQLException {
/*      */     try {
/* 4862 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4864 */       return this.inner.getCharacterStream(a);
/*      */     }
/* 4866 */     catch (NullPointerException exc) {
/*      */       
/* 4868 */       if (isDetached())
/*      */       {
/* 4870 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4872 */       throw exc;
/*      */     }
/* 4874 */     catch (Exception exc) {
/*      */       
/* 4876 */       if (!isDetached())
/*      */       {
/* 4878 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4880 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final InputStream getAsciiStream(String a) throws SQLException {
/*      */     try {
/* 4888 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4890 */       return this.inner.getAsciiStream(a);
/*      */     }
/* 4892 */     catch (NullPointerException exc) {
/*      */       
/* 4894 */       if (isDetached())
/*      */       {
/* 4896 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4898 */       throw exc;
/*      */     }
/* 4900 */     catch (Exception exc) {
/*      */       
/* 4902 */       if (!isDetached())
/*      */       {
/* 4904 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4906 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final InputStream getAsciiStream(int a) throws SQLException {
/*      */     try {
/* 4914 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4916 */       return this.inner.getAsciiStream(a);
/*      */     }
/* 4918 */     catch (NullPointerException exc) {
/*      */       
/* 4920 */       if (isDetached())
/*      */       {
/* 4922 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4924 */       throw exc;
/*      */     }
/* 4926 */     catch (Exception exc) {
/*      */       
/* 4928 */       if (!isDetached())
/*      */       {
/* 4930 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4932 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final InputStream getBinaryStream(int a) throws SQLException {
/*      */     try {
/* 4940 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4942 */       return this.inner.getBinaryStream(a);
/*      */     }
/* 4944 */     catch (NullPointerException exc) {
/*      */       
/* 4946 */       if (isDetached())
/*      */       {
/* 4948 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4950 */       throw exc;
/*      */     }
/* 4952 */     catch (Exception exc) {
/*      */       
/* 4954 */       if (!isDetached())
/*      */       {
/* 4956 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4958 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final InputStream getBinaryStream(String a) throws SQLException {
/*      */     try {
/* 4966 */       if (this.proxyConn != null) this.proxyConn.maybeDirtyTransaction();
/*      */       
/* 4968 */       return this.inner.getBinaryStream(a);
/*      */     }
/* 4970 */     catch (NullPointerException exc) {
/*      */       
/* 4972 */       if (isDetached())
/*      */       {
/* 4974 */         throw SqlUtils.toSQLException("You can't operate on a closed ResultSet!!!", exc);
/*      */       }
/* 4976 */       throw exc;
/*      */     }
/* 4978 */     catch (Exception exc) {
/*      */       
/* 4980 */       if (!isDetached())
/*      */       {
/* 4982 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4984 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object unwrap(Class a) throws SQLException {
/* 4990 */     if (isWrapperFor(a)) return this.inner; 
/* 4991 */     throw new SQLException(this + " is not a wrapper for " + a.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWrapperFor(Class<ResultSet> a) throws SQLException {
/* 4996 */     return (ResultSet.class == a || a.isAssignableFrom(this.inner.getClass()));
/*      */   }
/*      */   
/* 4999 */   private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyResultSet");
/*      */   
/*      */   volatile NewPooledConnection parentPooledConnection;
/*      */   
/* 5003 */   ConnectionEventListener cel = new ConnectionEventListener()
/*      */     {
/*      */       public void connectionErrorOccurred(ConnectionEvent evt) {}
/*      */ 
/*      */       
/*      */       public void connectionClosed(ConnectionEvent evt) {
/* 5009 */         NewProxyResultSet.this.detach();
/*      */       }
/*      */     };
/*      */   Object creator;
/*      */   void attach(NewPooledConnection parentPooledConnection) {
/* 5014 */     this.parentPooledConnection = parentPooledConnection;
/* 5015 */     parentPooledConnection.addConnectionEventListener(this.cel);
/*      */   }
/*      */   Object creatorProxy; NewProxyConnection proxyConn;
/*      */   
/*      */   private void detach() {
/* 5020 */     this.parentPooledConnection.removeConnectionEventListener(this.cel);
/* 5021 */     this.parentPooledConnection = null;
/*      */   }
/*      */ 
/*      */   
/*      */   NewProxyResultSet(ResultSet inner, NewPooledConnection parentPooledConnection) {
/* 5026 */     this(inner);
/* 5027 */     attach(parentPooledConnection);
/*      */   }
/*      */   
/*      */   boolean isDetached() {
/* 5031 */     return (this.parentPooledConnection == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   NewProxyResultSet(ResultSet inner, NewPooledConnection parentPooledConnection, Object c, Object cProxy) {
/* 5039 */     this(inner, parentPooledConnection);
/* 5040 */     this.creator = c;
/* 5041 */     this.creatorProxy = cProxy;
/* 5042 */     if (this.creatorProxy instanceof NewProxyConnection) this.proxyConn = (NewProxyConnection)cProxy; 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewProxyResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */