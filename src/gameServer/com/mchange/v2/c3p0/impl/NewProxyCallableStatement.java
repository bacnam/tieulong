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
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Connection;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.ParameterMetaData;
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
/*      */ public final class NewProxyCallableStatement
/*      */   implements CallableStatement, C3P0ProxyStatement, ProxyResultSetDetachable
/*      */ {
/*      */   protected CallableStatement inner;
/*      */   
/*      */   private void __setInner(CallableStatement inner) {
/*   55 */     this.inner = inner;
/*      */   }
/*      */   
/*      */   NewProxyCallableStatement(CallableStatement inner) {
/*   59 */     __setInner(inner);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object getObject(String a) throws SQLException {
/*      */     try {
/*   65 */       maybeDirtyTransaction();
/*      */       
/*   67 */       return this.inner.getObject(a);
/*      */     }
/*   69 */     catch (NullPointerException exc) {
/*      */       
/*   71 */       if (isDetached())
/*      */       {
/*   73 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*   75 */       throw exc;
/*      */     }
/*   77 */     catch (Exception exc) {
/*      */       
/*   79 */       if (!isDetached())
/*      */       {
/*   81 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*   83 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(int a, Map<String, Class<?>> b) throws SQLException {
/*      */     try {
/*   91 */       maybeDirtyTransaction();
/*      */       
/*   93 */       return this.inner.getObject(a, b);
/*      */     }
/*   95 */     catch (NullPointerException exc) {
/*      */       
/*   97 */       if (isDetached())
/*      */       {
/*   99 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  101 */       throw exc;
/*      */     }
/*  103 */     catch (Exception exc) {
/*      */       
/*  105 */       if (!isDetached())
/*      */       {
/*  107 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  109 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(int a) throws SQLException {
/*      */     try {
/*  117 */       maybeDirtyTransaction();
/*      */       
/*  119 */       return this.inner.getObject(a);
/*      */     }
/*  121 */     catch (NullPointerException exc) {
/*      */       
/*  123 */       if (isDetached())
/*      */       {
/*  125 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  127 */       throw exc;
/*      */     }
/*  129 */     catch (Exception exc) {
/*      */       
/*  131 */       if (!isDetached())
/*      */       {
/*  133 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  135 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(String a, Map<String, Class<?>> b) throws SQLException {
/*      */     try {
/*  143 */       maybeDirtyTransaction();
/*      */       
/*  145 */       return this.inner.getObject(a, b);
/*      */     }
/*  147 */     catch (NullPointerException exc) {
/*      */       
/*  149 */       if (isDetached())
/*      */       {
/*  151 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  153 */       throw exc;
/*      */     }
/*  155 */     catch (Exception exc) {
/*      */       
/*  157 */       if (!isDetached())
/*      */       {
/*  159 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  161 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(String a, Class<?> b) throws SQLException {
/*      */     try {
/*  169 */       maybeDirtyTransaction();
/*      */       
/*  171 */       return this.inner.getObject(a, b);
/*      */     }
/*  173 */     catch (NullPointerException exc) {
/*      */       
/*  175 */       if (isDetached())
/*      */       {
/*  177 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  179 */       throw exc;
/*      */     }
/*  181 */     catch (Exception exc) {
/*      */       
/*  183 */       if (!isDetached())
/*      */       {
/*  185 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  187 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Object getObject(int a, Class<?> b) throws SQLException {
/*      */     try {
/*  195 */       maybeDirtyTransaction();
/*      */       
/*  197 */       return this.inner.getObject(a, b);
/*      */     }
/*  199 */     catch (NullPointerException exc) {
/*      */       
/*  201 */       if (isDetached())
/*      */       {
/*  203 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  205 */       throw exc;
/*      */     }
/*  207 */     catch (Exception exc) {
/*      */       
/*  209 */       if (!isDetached())
/*      */       {
/*  211 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  213 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getBoolean(String a) throws SQLException {
/*      */     try {
/*  221 */       maybeDirtyTransaction();
/*      */       
/*  223 */       return this.inner.getBoolean(a);
/*      */     }
/*  225 */     catch (NullPointerException exc) {
/*      */       
/*  227 */       if (isDetached())
/*      */       {
/*  229 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  231 */       throw exc;
/*      */     }
/*  233 */     catch (Exception exc) {
/*      */       
/*  235 */       if (!isDetached())
/*      */       {
/*  237 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  239 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getBoolean(int a) throws SQLException {
/*      */     try {
/*  247 */       maybeDirtyTransaction();
/*      */       
/*  249 */       return this.inner.getBoolean(a);
/*      */     }
/*  251 */     catch (NullPointerException exc) {
/*      */       
/*  253 */       if (isDetached())
/*      */       {
/*  255 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  257 */       throw exc;
/*      */     }
/*  259 */     catch (Exception exc) {
/*      */       
/*  261 */       if (!isDetached())
/*      */       {
/*  263 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  265 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getByte(int a) throws SQLException {
/*      */     try {
/*  273 */       maybeDirtyTransaction();
/*      */       
/*  275 */       return this.inner.getByte(a);
/*      */     }
/*  277 */     catch (NullPointerException exc) {
/*      */       
/*  279 */       if (isDetached())
/*      */       {
/*  281 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  283 */       throw exc;
/*      */     }
/*  285 */     catch (Exception exc) {
/*      */       
/*  287 */       if (!isDetached())
/*      */       {
/*  289 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  291 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte getByte(String a) throws SQLException {
/*      */     try {
/*  299 */       maybeDirtyTransaction();
/*      */       
/*  301 */       return this.inner.getByte(a);
/*      */     }
/*  303 */     catch (NullPointerException exc) {
/*      */       
/*  305 */       if (isDetached())
/*      */       {
/*  307 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  309 */       throw exc;
/*      */     }
/*  311 */     catch (Exception exc) {
/*      */       
/*  313 */       if (!isDetached())
/*      */       {
/*  315 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  317 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getShort(String a) throws SQLException {
/*      */     try {
/*  325 */       maybeDirtyTransaction();
/*      */       
/*  327 */       return this.inner.getShort(a);
/*      */     }
/*  329 */     catch (NullPointerException exc) {
/*      */       
/*  331 */       if (isDetached())
/*      */       {
/*  333 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  335 */       throw exc;
/*      */     }
/*  337 */     catch (Exception exc) {
/*      */       
/*  339 */       if (!isDetached())
/*      */       {
/*  341 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  343 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final short getShort(int a) throws SQLException {
/*      */     try {
/*  351 */       maybeDirtyTransaction();
/*      */       
/*  353 */       return this.inner.getShort(a);
/*      */     }
/*  355 */     catch (NullPointerException exc) {
/*      */       
/*  357 */       if (isDetached())
/*      */       {
/*  359 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  361 */       throw exc;
/*      */     }
/*  363 */     catch (Exception exc) {
/*      */       
/*  365 */       if (!isDetached())
/*      */       {
/*  367 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  369 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getInt(int a) throws SQLException {
/*      */     try {
/*  377 */       maybeDirtyTransaction();
/*      */       
/*  379 */       return this.inner.getInt(a);
/*      */     }
/*  381 */     catch (NullPointerException exc) {
/*      */       
/*  383 */       if (isDetached())
/*      */       {
/*  385 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  387 */       throw exc;
/*      */     }
/*  389 */     catch (Exception exc) {
/*      */       
/*  391 */       if (!isDetached())
/*      */       {
/*  393 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  395 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getInt(String a) throws SQLException {
/*      */     try {
/*  403 */       maybeDirtyTransaction();
/*      */       
/*  405 */       return this.inner.getInt(a);
/*      */     }
/*  407 */     catch (NullPointerException exc) {
/*      */       
/*  409 */       if (isDetached())
/*      */       {
/*  411 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  413 */       throw exc;
/*      */     }
/*  415 */     catch (Exception exc) {
/*      */       
/*  417 */       if (!isDetached())
/*      */       {
/*  419 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  421 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLong(String a) throws SQLException {
/*      */     try {
/*  429 */       maybeDirtyTransaction();
/*      */       
/*  431 */       return this.inner.getLong(a);
/*      */     }
/*  433 */     catch (NullPointerException exc) {
/*      */       
/*  435 */       if (isDetached())
/*      */       {
/*  437 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  439 */       throw exc;
/*      */     }
/*  441 */     catch (Exception exc) {
/*      */       
/*  443 */       if (!isDetached())
/*      */       {
/*  445 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  447 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getLong(int a) throws SQLException {
/*      */     try {
/*  455 */       maybeDirtyTransaction();
/*      */       
/*  457 */       return this.inner.getLong(a);
/*      */     }
/*  459 */     catch (NullPointerException exc) {
/*      */       
/*  461 */       if (isDetached())
/*      */       {
/*  463 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  465 */       throw exc;
/*      */     }
/*  467 */     catch (Exception exc) {
/*      */       
/*  469 */       if (!isDetached())
/*      */       {
/*  471 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  473 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFloat(int a) throws SQLException {
/*      */     try {
/*  481 */       maybeDirtyTransaction();
/*      */       
/*  483 */       return this.inner.getFloat(a);
/*      */     }
/*  485 */     catch (NullPointerException exc) {
/*      */       
/*  487 */       if (isDetached())
/*      */       {
/*  489 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  491 */       throw exc;
/*      */     }
/*  493 */     catch (Exception exc) {
/*      */       
/*  495 */       if (!isDetached())
/*      */       {
/*  497 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  499 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final float getFloat(String a) throws SQLException {
/*      */     try {
/*  507 */       maybeDirtyTransaction();
/*      */       
/*  509 */       return this.inner.getFloat(a);
/*      */     }
/*  511 */     catch (NullPointerException exc) {
/*      */       
/*  513 */       if (isDetached())
/*      */       {
/*  515 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  517 */       throw exc;
/*      */     }
/*  519 */     catch (Exception exc) {
/*      */       
/*  521 */       if (!isDetached())
/*      */       {
/*  523 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  525 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDouble(String a) throws SQLException {
/*      */     try {
/*  533 */       maybeDirtyTransaction();
/*      */       
/*  535 */       return this.inner.getDouble(a);
/*      */     }
/*  537 */     catch (NullPointerException exc) {
/*      */       
/*  539 */       if (isDetached())
/*      */       {
/*  541 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  543 */       throw exc;
/*      */     }
/*  545 */     catch (Exception exc) {
/*      */       
/*  547 */       if (!isDetached())
/*      */       {
/*  549 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  551 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final double getDouble(int a) throws SQLException {
/*      */     try {
/*  559 */       maybeDirtyTransaction();
/*      */       
/*  561 */       return this.inner.getDouble(a);
/*      */     }
/*  563 */     catch (NullPointerException exc) {
/*      */       
/*  565 */       if (isDetached())
/*      */       {
/*  567 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  569 */       throw exc;
/*      */     }
/*  571 */     catch (Exception exc) {
/*      */       
/*  573 */       if (!isDetached())
/*      */       {
/*  575 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  577 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] getBytes(int a) throws SQLException {
/*      */     try {
/*  585 */       maybeDirtyTransaction();
/*      */       
/*  587 */       return this.inner.getBytes(a);
/*      */     }
/*  589 */     catch (NullPointerException exc) {
/*      */       
/*  591 */       if (isDetached())
/*      */       {
/*  593 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  595 */       throw exc;
/*      */     }
/*  597 */     catch (Exception exc) {
/*      */       
/*  599 */       if (!isDetached())
/*      */       {
/*  601 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  603 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final byte[] getBytes(String a) throws SQLException {
/*      */     try {
/*  611 */       maybeDirtyTransaction();
/*      */       
/*  613 */       return this.inner.getBytes(a);
/*      */     }
/*  615 */     catch (NullPointerException exc) {
/*      */       
/*  617 */       if (isDetached())
/*      */       {
/*  619 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  621 */       throw exc;
/*      */     }
/*  623 */     catch (Exception exc) {
/*      */       
/*  625 */       if (!isDetached())
/*      */       {
/*  627 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  629 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Array getArray(String a) throws SQLException {
/*      */     try {
/*  637 */       maybeDirtyTransaction();
/*      */       
/*  639 */       return this.inner.getArray(a);
/*      */     }
/*  641 */     catch (NullPointerException exc) {
/*      */       
/*  643 */       if (isDetached())
/*      */       {
/*  645 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  647 */       throw exc;
/*      */     }
/*  649 */     catch (Exception exc) {
/*      */       
/*  651 */       if (!isDetached())
/*      */       {
/*  653 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  655 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Array getArray(int a) throws SQLException {
/*      */     try {
/*  663 */       maybeDirtyTransaction();
/*      */       
/*  665 */       return this.inner.getArray(a);
/*      */     }
/*  667 */     catch (NullPointerException exc) {
/*      */       
/*  669 */       if (isDetached())
/*      */       {
/*  671 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  673 */       throw exc;
/*      */     }
/*  675 */     catch (Exception exc) {
/*      */       
/*  677 */       if (!isDetached())
/*      */       {
/*  679 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  681 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final URL getURL(String a) throws SQLException {
/*      */     try {
/*  689 */       maybeDirtyTransaction();
/*      */       
/*  691 */       return this.inner.getURL(a);
/*      */     }
/*  693 */     catch (NullPointerException exc) {
/*      */       
/*  695 */       if (isDetached())
/*      */       {
/*  697 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  699 */       throw exc;
/*      */     }
/*  701 */     catch (Exception exc) {
/*      */       
/*  703 */       if (!isDetached())
/*      */       {
/*  705 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  707 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final URL getURL(int a) throws SQLException {
/*      */     try {
/*  715 */       maybeDirtyTransaction();
/*      */       
/*  717 */       return this.inner.getURL(a);
/*      */     }
/*  719 */     catch (NullPointerException exc) {
/*      */       
/*  721 */       if (isDetached())
/*      */       {
/*  723 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  725 */       throw exc;
/*      */     }
/*  727 */     catch (Exception exc) {
/*      */       
/*  729 */       if (!isDetached())
/*      */       {
/*  731 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  733 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBoolean(String a, boolean b) throws SQLException {
/*      */     try {
/*  741 */       maybeDirtyTransaction();
/*      */       
/*  743 */       this.inner.setBoolean(a, b);
/*      */     }
/*  745 */     catch (NullPointerException exc) {
/*      */       
/*  747 */       if (isDetached())
/*      */       {
/*  749 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  751 */       throw exc;
/*      */     }
/*  753 */     catch (Exception exc) {
/*      */       
/*  755 */       if (!isDetached())
/*      */       {
/*  757 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  759 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setByte(String a, byte b) throws SQLException {
/*      */     try {
/*  767 */       maybeDirtyTransaction();
/*      */       
/*  769 */       this.inner.setByte(a, b);
/*      */     }
/*  771 */     catch (NullPointerException exc) {
/*      */       
/*  773 */       if (isDetached())
/*      */       {
/*  775 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  777 */       throw exc;
/*      */     }
/*  779 */     catch (Exception exc) {
/*      */       
/*  781 */       if (!isDetached())
/*      */       {
/*  783 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  785 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setShort(String a, short b) throws SQLException {
/*      */     try {
/*  793 */       maybeDirtyTransaction();
/*      */       
/*  795 */       this.inner.setShort(a, b);
/*      */     }
/*  797 */     catch (NullPointerException exc) {
/*      */       
/*  799 */       if (isDetached())
/*      */       {
/*  801 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  803 */       throw exc;
/*      */     }
/*  805 */     catch (Exception exc) {
/*      */       
/*  807 */       if (!isDetached())
/*      */       {
/*  809 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  811 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setInt(String a, int b) throws SQLException {
/*      */     try {
/*  819 */       maybeDirtyTransaction();
/*      */       
/*  821 */       this.inner.setInt(a, b);
/*      */     }
/*  823 */     catch (NullPointerException exc) {
/*      */       
/*  825 */       if (isDetached())
/*      */       {
/*  827 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  829 */       throw exc;
/*      */     }
/*  831 */     catch (Exception exc) {
/*      */       
/*  833 */       if (!isDetached())
/*      */       {
/*  835 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  837 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLong(String a, long b) throws SQLException {
/*      */     try {
/*  845 */       maybeDirtyTransaction();
/*      */       
/*  847 */       this.inner.setLong(a, b);
/*      */     }
/*  849 */     catch (NullPointerException exc) {
/*      */       
/*  851 */       if (isDetached())
/*      */       {
/*  853 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  855 */       throw exc;
/*      */     }
/*  857 */     catch (Exception exc) {
/*      */       
/*  859 */       if (!isDetached())
/*      */       {
/*  861 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  863 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFloat(String a, float b) throws SQLException {
/*      */     try {
/*  871 */       maybeDirtyTransaction();
/*      */       
/*  873 */       this.inner.setFloat(a, b);
/*      */     }
/*  875 */     catch (NullPointerException exc) {
/*      */       
/*  877 */       if (isDetached())
/*      */       {
/*  879 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  881 */       throw exc;
/*      */     }
/*  883 */     catch (Exception exc) {
/*      */       
/*  885 */       if (!isDetached())
/*      */       {
/*  887 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  889 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDouble(String a, double b) throws SQLException {
/*      */     try {
/*  897 */       maybeDirtyTransaction();
/*      */       
/*  899 */       this.inner.setDouble(a, b);
/*      */     }
/*  901 */     catch (NullPointerException exc) {
/*      */       
/*  903 */       if (isDetached())
/*      */       {
/*  905 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  907 */       throw exc;
/*      */     }
/*  909 */     catch (Exception exc) {
/*      */       
/*  911 */       if (!isDetached())
/*      */       {
/*  913 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  915 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTimestamp(String a, Timestamp b) throws SQLException {
/*      */     try {
/*  923 */       maybeDirtyTransaction();
/*      */       
/*  925 */       this.inner.setTimestamp(a, b);
/*      */     }
/*  927 */     catch (NullPointerException exc) {
/*      */       
/*  929 */       if (isDetached())
/*      */       {
/*  931 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  933 */       throw exc;
/*      */     }
/*  935 */     catch (Exception exc) {
/*      */       
/*  937 */       if (!isDetached())
/*      */       {
/*  939 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  941 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTimestamp(String a, Timestamp b, Calendar c) throws SQLException {
/*      */     try {
/*  949 */       maybeDirtyTransaction();
/*      */       
/*  951 */       this.inner.setTimestamp(a, b, c);
/*      */     }
/*  953 */     catch (NullPointerException exc) {
/*      */       
/*  955 */       if (isDetached())
/*      */       {
/*  957 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  959 */       throw exc;
/*      */     }
/*  961 */     catch (Exception exc) {
/*      */       
/*  963 */       if (!isDetached())
/*      */       {
/*  965 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  967 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Ref getRef(String a) throws SQLException {
/*      */     try {
/*  975 */       maybeDirtyTransaction();
/*      */       
/*  977 */       return this.inner.getRef(a);
/*      */     }
/*  979 */     catch (NullPointerException exc) {
/*      */       
/*  981 */       if (isDetached())
/*      */       {
/*  983 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  985 */       throw exc;
/*      */     }
/*  987 */     catch (Exception exc) {
/*      */       
/*  989 */       if (!isDetached())
/*      */       {
/*  991 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  993 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Ref getRef(int a) throws SQLException {
/*      */     try {
/* 1001 */       maybeDirtyTransaction();
/*      */       
/* 1003 */       return this.inner.getRef(a);
/*      */     }
/* 1005 */     catch (NullPointerException exc) {
/*      */       
/* 1007 */       if (isDetached())
/*      */       {
/* 1009 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1011 */       throw exc;
/*      */     }
/* 1013 */     catch (Exception exc) {
/*      */       
/* 1015 */       if (!isDetached())
/*      */       {
/* 1017 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1019 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getString(String a) throws SQLException {
/*      */     try {
/* 1027 */       maybeDirtyTransaction();
/*      */       
/* 1029 */       return this.inner.getString(a);
/*      */     }
/* 1031 */     catch (NullPointerException exc) {
/*      */       
/* 1033 */       if (isDetached())
/*      */       {
/* 1035 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1037 */       throw exc;
/*      */     }
/* 1039 */     catch (Exception exc) {
/*      */       
/* 1041 */       if (!isDetached())
/*      */       {
/* 1043 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1045 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getString(int a) throws SQLException {
/*      */     try {
/* 1053 */       maybeDirtyTransaction();
/*      */       
/* 1055 */       return this.inner.getString(a);
/*      */     }
/* 1057 */     catch (NullPointerException exc) {
/*      */       
/* 1059 */       if (isDetached())
/*      */       {
/* 1061 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1063 */       throw exc;
/*      */     }
/* 1065 */     catch (Exception exc) {
/*      */       
/* 1067 */       if (!isDetached())
/*      */       {
/* 1069 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1071 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setURL(String a, URL b) throws SQLException {
/*      */     try {
/* 1079 */       maybeDirtyTransaction();
/*      */       
/* 1081 */       this.inner.setURL(a, b);
/*      */     }
/* 1083 */     catch (NullPointerException exc) {
/*      */       
/* 1085 */       if (isDetached())
/*      */       {
/* 1087 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1089 */       throw exc;
/*      */     }
/* 1091 */     catch (Exception exc) {
/*      */       
/* 1093 */       if (!isDetached())
/*      */       {
/* 1095 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1097 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTime(String a, Time b, Calendar c) throws SQLException {
/*      */     try {
/* 1105 */       maybeDirtyTransaction();
/*      */       
/* 1107 */       this.inner.setTime(a, b, c);
/*      */     }
/* 1109 */     catch (NullPointerException exc) {
/*      */       
/* 1111 */       if (isDetached())
/*      */       {
/* 1113 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1115 */       throw exc;
/*      */     }
/* 1117 */     catch (Exception exc) {
/*      */       
/* 1119 */       if (!isDetached())
/*      */       {
/* 1121 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1123 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTime(String a, Time b) throws SQLException {
/*      */     try {
/* 1131 */       maybeDirtyTransaction();
/*      */       
/* 1133 */       this.inner.setTime(a, b);
/*      */     }
/* 1135 */     catch (NullPointerException exc) {
/*      */       
/* 1137 */       if (isDetached())
/*      */       {
/* 1139 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1141 */       throw exc;
/*      */     }
/* 1143 */     catch (Exception exc) {
/*      */       
/* 1145 */       if (!isDetached())
/*      */       {
/* 1147 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1149 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(int a, Calendar b) throws SQLException {
/*      */     try {
/* 1157 */       maybeDirtyTransaction();
/*      */       
/* 1159 */       return this.inner.getTime(a, b);
/*      */     }
/* 1161 */     catch (NullPointerException exc) {
/*      */       
/* 1163 */       if (isDetached())
/*      */       {
/* 1165 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1167 */       throw exc;
/*      */     }
/* 1169 */     catch (Exception exc) {
/*      */       
/* 1171 */       if (!isDetached())
/*      */       {
/* 1173 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1175 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(int a) throws SQLException {
/*      */     try {
/* 1183 */       maybeDirtyTransaction();
/*      */       
/* 1185 */       return this.inner.getTime(a);
/*      */     }
/* 1187 */     catch (NullPointerException exc) {
/*      */       
/* 1189 */       if (isDetached())
/*      */       {
/* 1191 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1193 */       throw exc;
/*      */     }
/* 1195 */     catch (Exception exc) {
/*      */       
/* 1197 */       if (!isDetached())
/*      */       {
/* 1199 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1201 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(String a) throws SQLException {
/*      */     try {
/* 1209 */       maybeDirtyTransaction();
/*      */       
/* 1211 */       return this.inner.getTime(a);
/*      */     }
/* 1213 */     catch (NullPointerException exc) {
/*      */       
/* 1215 */       if (isDetached())
/*      */       {
/* 1217 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1219 */       throw exc;
/*      */     }
/* 1221 */     catch (Exception exc) {
/*      */       
/* 1223 */       if (!isDetached())
/*      */       {
/* 1225 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1227 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Time getTime(String a, Calendar b) throws SQLException {
/*      */     try {
/* 1235 */       maybeDirtyTransaction();
/*      */       
/* 1237 */       return this.inner.getTime(a, b);
/*      */     }
/* 1239 */     catch (NullPointerException exc) {
/*      */       
/* 1241 */       if (isDetached())
/*      */       {
/* 1243 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1245 */       throw exc;
/*      */     }
/* 1247 */     catch (Exception exc) {
/*      */       
/* 1249 */       if (!isDetached())
/*      */       {
/* 1251 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1253 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(int a, Calendar b) throws SQLException {
/*      */     try {
/* 1261 */       maybeDirtyTransaction();
/*      */       
/* 1263 */       return this.inner.getDate(a, b);
/*      */     }
/* 1265 */     catch (NullPointerException exc) {
/*      */       
/* 1267 */       if (isDetached())
/*      */       {
/* 1269 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1271 */       throw exc;
/*      */     }
/* 1273 */     catch (Exception exc) {
/*      */       
/* 1275 */       if (!isDetached())
/*      */       {
/* 1277 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1279 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(String a) throws SQLException {
/*      */     try {
/* 1287 */       maybeDirtyTransaction();
/*      */       
/* 1289 */       return this.inner.getDate(a);
/*      */     }
/* 1291 */     catch (NullPointerException exc) {
/*      */       
/* 1293 */       if (isDetached())
/*      */       {
/* 1295 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1297 */       throw exc;
/*      */     }
/* 1299 */     catch (Exception exc) {
/*      */       
/* 1301 */       if (!isDetached())
/*      */       {
/* 1303 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1305 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(int a) throws SQLException {
/*      */     try {
/* 1313 */       maybeDirtyTransaction();
/*      */       
/* 1315 */       return this.inner.getDate(a);
/*      */     }
/* 1317 */     catch (NullPointerException exc) {
/*      */       
/* 1319 */       if (isDetached())
/*      */       {
/* 1321 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1323 */       throw exc;
/*      */     }
/* 1325 */     catch (Exception exc) {
/*      */       
/* 1327 */       if (!isDetached())
/*      */       {
/* 1329 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1331 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Date getDate(String a, Calendar b) throws SQLException {
/*      */     try {
/* 1339 */       maybeDirtyTransaction();
/*      */       
/* 1341 */       return this.inner.getDate(a, b);
/*      */     }
/* 1343 */     catch (NullPointerException exc) {
/*      */       
/* 1345 */       if (isDetached())
/*      */       {
/* 1347 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1349 */       throw exc;
/*      */     }
/* 1351 */     catch (Exception exc) {
/*      */       
/* 1353 */       if (!isDetached())
/*      */       {
/* 1355 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1357 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(int a, Calendar b) throws SQLException {
/*      */     try {
/* 1365 */       maybeDirtyTransaction();
/*      */       
/* 1367 */       return this.inner.getTimestamp(a, b);
/*      */     }
/* 1369 */     catch (NullPointerException exc) {
/*      */       
/* 1371 */       if (isDetached())
/*      */       {
/* 1373 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1375 */       throw exc;
/*      */     }
/* 1377 */     catch (Exception exc) {
/*      */       
/* 1379 */       if (!isDetached())
/*      */       {
/* 1381 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1383 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(String a, Calendar b) throws SQLException {
/*      */     try {
/* 1391 */       maybeDirtyTransaction();
/*      */       
/* 1393 */       return this.inner.getTimestamp(a, b);
/*      */     }
/* 1395 */     catch (NullPointerException exc) {
/*      */       
/* 1397 */       if (isDetached())
/*      */       {
/* 1399 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1401 */       throw exc;
/*      */     }
/* 1403 */     catch (Exception exc) {
/*      */       
/* 1405 */       if (!isDetached())
/*      */       {
/* 1407 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1409 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(String a) throws SQLException {
/*      */     try {
/* 1417 */       maybeDirtyTransaction();
/*      */       
/* 1419 */       return this.inner.getTimestamp(a);
/*      */     }
/* 1421 */     catch (NullPointerException exc) {
/*      */       
/* 1423 */       if (isDetached())
/*      */       {
/* 1425 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1427 */       throw exc;
/*      */     }
/* 1429 */     catch (Exception exc) {
/*      */       
/* 1431 */       if (!isDetached())
/*      */       {
/* 1433 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1435 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Timestamp getTimestamp(int a) throws SQLException {
/*      */     try {
/* 1443 */       maybeDirtyTransaction();
/*      */       
/* 1445 */       return this.inner.getTimestamp(a);
/*      */     }
/* 1447 */     catch (NullPointerException exc) {
/*      */       
/* 1449 */       if (isDetached())
/*      */       {
/* 1451 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1453 */       throw exc;
/*      */     }
/* 1455 */     catch (Exception exc) {
/*      */       
/* 1457 */       if (!isDetached())
/*      */       {
/* 1459 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1461 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNull(String a, int b, String c) throws SQLException {
/*      */     try {
/* 1469 */       maybeDirtyTransaction();
/*      */       
/* 1471 */       this.inner.setNull(a, b, c);
/*      */     }
/* 1473 */     catch (NullPointerException exc) {
/*      */       
/* 1475 */       if (isDetached())
/*      */       {
/* 1477 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1479 */       throw exc;
/*      */     }
/* 1481 */     catch (Exception exc) {
/*      */       
/* 1483 */       if (!isDetached())
/*      */       {
/* 1485 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1487 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNull(String a, int b) throws SQLException {
/*      */     try {
/* 1495 */       maybeDirtyTransaction();
/*      */       
/* 1497 */       this.inner.setNull(a, b);
/*      */     }
/* 1499 */     catch (NullPointerException exc) {
/*      */       
/* 1501 */       if (isDetached())
/*      */       {
/* 1503 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1505 */       throw exc;
/*      */     }
/* 1507 */     catch (Exception exc) {
/*      */       
/* 1509 */       if (!isDetached())
/*      */       {
/* 1511 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1513 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBigDecimal(String a, BigDecimal b) throws SQLException {
/*      */     try {
/* 1521 */       maybeDirtyTransaction();
/*      */       
/* 1523 */       this.inner.setBigDecimal(a, b);
/*      */     }
/* 1525 */     catch (NullPointerException exc) {
/*      */       
/* 1527 */       if (isDetached())
/*      */       {
/* 1529 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1531 */       throw exc;
/*      */     }
/* 1533 */     catch (Exception exc) {
/*      */       
/* 1535 */       if (!isDetached())
/*      */       {
/* 1537 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1539 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setString(String a, String b) throws SQLException {
/*      */     try {
/* 1547 */       maybeDirtyTransaction();
/*      */       
/* 1549 */       this.inner.setString(a, b);
/*      */     }
/* 1551 */     catch (NullPointerException exc) {
/*      */       
/* 1553 */       if (isDetached())
/*      */       {
/* 1555 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1557 */       throw exc;
/*      */     }
/* 1559 */     catch (Exception exc) {
/*      */       
/* 1561 */       if (!isDetached())
/*      */       {
/* 1563 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1565 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBytes(String a, byte[] b) throws SQLException {
/*      */     try {
/* 1573 */       maybeDirtyTransaction();
/*      */       
/* 1575 */       this.inner.setBytes(a, b);
/*      */     }
/* 1577 */     catch (NullPointerException exc) {
/*      */       
/* 1579 */       if (isDetached())
/*      */       {
/* 1581 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1583 */       throw exc;
/*      */     }
/* 1585 */     catch (Exception exc) {
/*      */       
/* 1587 */       if (!isDetached())
/*      */       {
/* 1589 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1591 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDate(String a, Date b) throws SQLException {
/*      */     try {
/* 1599 */       maybeDirtyTransaction();
/*      */       
/* 1601 */       this.inner.setDate(a, b);
/*      */     }
/* 1603 */     catch (NullPointerException exc) {
/*      */       
/* 1605 */       if (isDetached())
/*      */       {
/* 1607 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1609 */       throw exc;
/*      */     }
/* 1611 */     catch (Exception exc) {
/*      */       
/* 1613 */       if (!isDetached())
/*      */       {
/* 1615 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1617 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDate(String a, Date b, Calendar c) throws SQLException {
/*      */     try {
/* 1625 */       maybeDirtyTransaction();
/*      */       
/* 1627 */       this.inner.setDate(a, b, c);
/*      */     }
/* 1629 */     catch (NullPointerException exc) {
/*      */       
/* 1631 */       if (isDetached())
/*      */       {
/* 1633 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1635 */       throw exc;
/*      */     }
/* 1637 */     catch (Exception exc) {
/*      */       
/* 1639 */       if (!isDetached())
/*      */       {
/* 1641 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1643 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(String a, InputStream b) throws SQLException {
/*      */     try {
/* 1651 */       maybeDirtyTransaction();
/*      */       
/* 1653 */       this.inner.setAsciiStream(a, b);
/*      */     }
/* 1655 */     catch (NullPointerException exc) {
/*      */       
/* 1657 */       if (isDetached())
/*      */       {
/* 1659 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1661 */       throw exc;
/*      */     }
/* 1663 */     catch (Exception exc) {
/*      */       
/* 1665 */       if (!isDetached())
/*      */       {
/* 1667 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1669 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(String a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 1677 */       maybeDirtyTransaction();
/*      */       
/* 1679 */       this.inner.setAsciiStream(a, b, c);
/*      */     }
/* 1681 */     catch (NullPointerException exc) {
/*      */       
/* 1683 */       if (isDetached())
/*      */       {
/* 1685 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1687 */       throw exc;
/*      */     }
/* 1689 */     catch (Exception exc) {
/*      */       
/* 1691 */       if (!isDetached())
/*      */       {
/* 1693 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1695 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(String a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 1703 */       maybeDirtyTransaction();
/*      */       
/* 1705 */       this.inner.setAsciiStream(a, b, c);
/*      */     }
/* 1707 */     catch (NullPointerException exc) {
/*      */       
/* 1709 */       if (isDetached())
/*      */       {
/* 1711 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1713 */       throw exc;
/*      */     }
/* 1715 */     catch (Exception exc) {
/*      */       
/* 1717 */       if (!isDetached())
/*      */       {
/* 1719 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1721 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(String a, InputStream b) throws SQLException {
/*      */     try {
/* 1729 */       maybeDirtyTransaction();
/*      */       
/* 1731 */       this.inner.setBinaryStream(a, b);
/*      */     }
/* 1733 */     catch (NullPointerException exc) {
/*      */       
/* 1735 */       if (isDetached())
/*      */       {
/* 1737 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1739 */       throw exc;
/*      */     }
/* 1741 */     catch (Exception exc) {
/*      */       
/* 1743 */       if (!isDetached())
/*      */       {
/* 1745 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1747 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(String a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 1755 */       maybeDirtyTransaction();
/*      */       
/* 1757 */       this.inner.setBinaryStream(a, b, c);
/*      */     }
/* 1759 */     catch (NullPointerException exc) {
/*      */       
/* 1761 */       if (isDetached())
/*      */       {
/* 1763 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1765 */       throw exc;
/*      */     }
/* 1767 */     catch (Exception exc) {
/*      */       
/* 1769 */       if (!isDetached())
/*      */       {
/* 1771 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1773 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(String a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 1781 */       maybeDirtyTransaction();
/*      */       
/* 1783 */       this.inner.setBinaryStream(a, b, c);
/*      */     }
/* 1785 */     catch (NullPointerException exc) {
/*      */       
/* 1787 */       if (isDetached())
/*      */       {
/* 1789 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1791 */       throw exc;
/*      */     }
/* 1793 */     catch (Exception exc) {
/*      */       
/* 1795 */       if (!isDetached())
/*      */       {
/* 1797 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1799 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(String a, Object b, int c, int d) throws SQLException {
/*      */     try {
/* 1807 */       maybeDirtyTransaction();
/*      */       
/* 1809 */       this.inner.setObject(a, b, c, d);
/*      */     }
/* 1811 */     catch (NullPointerException exc) {
/*      */       
/* 1813 */       if (isDetached())
/*      */       {
/* 1815 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1817 */       throw exc;
/*      */     }
/* 1819 */     catch (Exception exc) {
/*      */       
/* 1821 */       if (!isDetached())
/*      */       {
/* 1823 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1825 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(String a, Object b) throws SQLException {
/*      */     try {
/* 1833 */       maybeDirtyTransaction();
/*      */       
/* 1835 */       this.inner.setObject(a, b);
/*      */     }
/* 1837 */     catch (NullPointerException exc) {
/*      */       
/* 1839 */       if (isDetached())
/*      */       {
/* 1841 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1843 */       throw exc;
/*      */     }
/* 1845 */     catch (Exception exc) {
/*      */       
/* 1847 */       if (!isDetached())
/*      */       {
/* 1849 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1851 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(String a, Object b, int c) throws SQLException {
/*      */     try {
/* 1859 */       maybeDirtyTransaction();
/*      */       
/* 1861 */       this.inner.setObject(a, b, c);
/*      */     }
/* 1863 */     catch (NullPointerException exc) {
/*      */       
/* 1865 */       if (isDetached())
/*      */       {
/* 1867 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1869 */       throw exc;
/*      */     }
/* 1871 */     catch (Exception exc) {
/*      */       
/* 1873 */       if (!isDetached())
/*      */       {
/* 1875 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1877 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 1885 */       maybeDirtyTransaction();
/*      */       
/* 1887 */       this.inner.setCharacterStream(a, b, c);
/*      */     }
/* 1889 */     catch (NullPointerException exc) {
/*      */       
/* 1891 */       if (isDetached())
/*      */       {
/* 1893 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1895 */       throw exc;
/*      */     }
/* 1897 */     catch (Exception exc) {
/*      */       
/* 1899 */       if (!isDetached())
/*      */       {
/* 1901 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1903 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(String a, Reader b, int c) throws SQLException {
/*      */     try {
/* 1911 */       maybeDirtyTransaction();
/*      */       
/* 1913 */       this.inner.setCharacterStream(a, b, c);
/*      */     }
/* 1915 */     catch (NullPointerException exc) {
/*      */       
/* 1917 */       if (isDetached())
/*      */       {
/* 1919 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1921 */       throw exc;
/*      */     }
/* 1923 */     catch (Exception exc) {
/*      */       
/* 1925 */       if (!isDetached())
/*      */       {
/* 1927 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1929 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(String a, Reader b) throws SQLException {
/*      */     try {
/* 1937 */       maybeDirtyTransaction();
/*      */       
/* 1939 */       this.inner.setCharacterStream(a, b);
/*      */     }
/* 1941 */     catch (NullPointerException exc) {
/*      */       
/* 1943 */       if (isDetached())
/*      */       {
/* 1945 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1947 */       throw exc;
/*      */     }
/* 1949 */     catch (Exception exc) {
/*      */       
/* 1951 */       if (!isDetached())
/*      */       {
/* 1953 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1955 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(String a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 1963 */       maybeDirtyTransaction();
/*      */       
/* 1965 */       this.inner.setBlob(a, b, c);
/*      */     }
/* 1967 */     catch (NullPointerException exc) {
/*      */       
/* 1969 */       if (isDetached())
/*      */       {
/* 1971 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1973 */       throw exc;
/*      */     }
/* 1975 */     catch (Exception exc) {
/*      */       
/* 1977 */       if (!isDetached())
/*      */       {
/* 1979 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1981 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(String a, InputStream b) throws SQLException {
/*      */     try {
/* 1989 */       maybeDirtyTransaction();
/*      */       
/* 1991 */       this.inner.setBlob(a, b);
/*      */     }
/* 1993 */     catch (NullPointerException exc) {
/*      */       
/* 1995 */       if (isDetached())
/*      */       {
/* 1997 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1999 */       throw exc;
/*      */     }
/* 2001 */     catch (Exception exc) {
/*      */       
/* 2003 */       if (!isDetached())
/*      */       {
/* 2005 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2007 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(String a, Blob b) throws SQLException {
/*      */     try {
/* 2015 */       maybeDirtyTransaction();
/*      */       
/* 2017 */       this.inner.setBlob(a, b);
/*      */     }
/* 2019 */     catch (NullPointerException exc) {
/*      */       
/* 2021 */       if (isDetached())
/*      */       {
/* 2023 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2025 */       throw exc;
/*      */     }
/* 2027 */     catch (Exception exc) {
/*      */       
/* 2029 */       if (!isDetached())
/*      */       {
/* 2031 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2033 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(String a, Clob b) throws SQLException {
/*      */     try {
/* 2041 */       maybeDirtyTransaction();
/*      */       
/* 2043 */       this.inner.setClob(a, b);
/*      */     }
/* 2045 */     catch (NullPointerException exc) {
/*      */       
/* 2047 */       if (isDetached())
/*      */       {
/* 2049 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2051 */       throw exc;
/*      */     }
/* 2053 */     catch (Exception exc) {
/*      */       
/* 2055 */       if (!isDetached())
/*      */       {
/* 2057 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2059 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(String a, Reader b) throws SQLException {
/*      */     try {
/* 2067 */       maybeDirtyTransaction();
/*      */       
/* 2069 */       this.inner.setClob(a, b);
/*      */     }
/* 2071 */     catch (NullPointerException exc) {
/*      */       
/* 2073 */       if (isDetached())
/*      */       {
/* 2075 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2077 */       throw exc;
/*      */     }
/* 2079 */     catch (Exception exc) {
/*      */       
/* 2081 */       if (!isDetached())
/*      */       {
/* 2083 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2085 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 2093 */       maybeDirtyTransaction();
/*      */       
/* 2095 */       this.inner.setClob(a, b, c);
/*      */     }
/* 2097 */     catch (NullPointerException exc) {
/*      */       
/* 2099 */       if (isDetached())
/*      */       {
/* 2101 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2103 */       throw exc;
/*      */     }
/* 2105 */     catch (Exception exc) {
/*      */       
/* 2107 */       if (!isDetached())
/*      */       {
/* 2109 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2111 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRowId(String a, RowId b) throws SQLException {
/*      */     try {
/* 2119 */       maybeDirtyTransaction();
/*      */       
/* 2121 */       this.inner.setRowId(a, b);
/*      */     }
/* 2123 */     catch (NullPointerException exc) {
/*      */       
/* 2125 */       if (isDetached())
/*      */       {
/* 2127 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2129 */       throw exc;
/*      */     }
/* 2131 */     catch (Exception exc) {
/*      */       
/* 2133 */       if (!isDetached())
/*      */       {
/* 2135 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2137 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNString(String a, String b) throws SQLException {
/*      */     try {
/* 2145 */       maybeDirtyTransaction();
/*      */       
/* 2147 */       this.inner.setNString(a, b);
/*      */     }
/* 2149 */     catch (NullPointerException exc) {
/*      */       
/* 2151 */       if (isDetached())
/*      */       {
/* 2153 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2155 */       throw exc;
/*      */     }
/* 2157 */     catch (Exception exc) {
/*      */       
/* 2159 */       if (!isDetached())
/*      */       {
/* 2161 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2163 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNCharacterStream(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 2171 */       maybeDirtyTransaction();
/*      */       
/* 2173 */       this.inner.setNCharacterStream(a, b, c);
/*      */     }
/* 2175 */     catch (NullPointerException exc) {
/*      */       
/* 2177 */       if (isDetached())
/*      */       {
/* 2179 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2181 */       throw exc;
/*      */     }
/* 2183 */     catch (Exception exc) {
/*      */       
/* 2185 */       if (!isDetached())
/*      */       {
/* 2187 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2189 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNCharacterStream(String a, Reader b) throws SQLException {
/*      */     try {
/* 2197 */       maybeDirtyTransaction();
/*      */       
/* 2199 */       this.inner.setNCharacterStream(a, b);
/*      */     }
/* 2201 */     catch (NullPointerException exc) {
/*      */       
/* 2203 */       if (isDetached())
/*      */       {
/* 2205 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2207 */       throw exc;
/*      */     }
/* 2209 */     catch (Exception exc) {
/*      */       
/* 2211 */       if (!isDetached())
/*      */       {
/* 2213 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2215 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(String a, Reader b, long c) throws SQLException {
/*      */     try {
/* 2223 */       maybeDirtyTransaction();
/*      */       
/* 2225 */       this.inner.setNClob(a, b, c);
/*      */     }
/* 2227 */     catch (NullPointerException exc) {
/*      */       
/* 2229 */       if (isDetached())
/*      */       {
/* 2231 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2233 */       throw exc;
/*      */     }
/* 2235 */     catch (Exception exc) {
/*      */       
/* 2237 */       if (!isDetached())
/*      */       {
/* 2239 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2241 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(String a, NClob b) throws SQLException {
/*      */     try {
/* 2249 */       maybeDirtyTransaction();
/*      */       
/* 2251 */       this.inner.setNClob(a, b);
/*      */     }
/* 2253 */     catch (NullPointerException exc) {
/*      */       
/* 2255 */       if (isDetached())
/*      */       {
/* 2257 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2259 */       throw exc;
/*      */     }
/* 2261 */     catch (Exception exc) {
/*      */       
/* 2263 */       if (!isDetached())
/*      */       {
/* 2265 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2267 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(String a, Reader b) throws SQLException {
/*      */     try {
/* 2275 */       maybeDirtyTransaction();
/*      */       
/* 2277 */       this.inner.setNClob(a, b);
/*      */     }
/* 2279 */     catch (NullPointerException exc) {
/*      */       
/* 2281 */       if (isDetached())
/*      */       {
/* 2283 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2285 */       throw exc;
/*      */     }
/* 2287 */     catch (Exception exc) {
/*      */       
/* 2289 */       if (!isDetached())
/*      */       {
/* 2291 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2293 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSQLXML(String a, SQLXML b) throws SQLException {
/*      */     try {
/* 2301 */       maybeDirtyTransaction();
/*      */       
/* 2303 */       this.inner.setSQLXML(a, b);
/*      */     }
/* 2305 */     catch (NullPointerException exc) {
/*      */       
/* 2307 */       if (isDetached())
/*      */       {
/* 2309 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2311 */       throw exc;
/*      */     }
/* 2313 */     catch (Exception exc) {
/*      */       
/* 2315 */       if (!isDetached())
/*      */       {
/* 2317 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2319 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void registerOutParameter(int a, int b) throws SQLException {
/*      */     try {
/* 2327 */       maybeDirtyTransaction();
/*      */       
/* 2329 */       this.inner.registerOutParameter(a, b);
/*      */     }
/* 2331 */     catch (NullPointerException exc) {
/*      */       
/* 2333 */       if (isDetached())
/*      */       {
/* 2335 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2337 */       throw exc;
/*      */     }
/* 2339 */     catch (Exception exc) {
/*      */       
/* 2341 */       if (!isDetached())
/*      */       {
/* 2343 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2345 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void registerOutParameter(int a, int b, int c) throws SQLException {
/*      */     try {
/* 2353 */       maybeDirtyTransaction();
/*      */       
/* 2355 */       this.inner.registerOutParameter(a, b, c);
/*      */     }
/* 2357 */     catch (NullPointerException exc) {
/*      */       
/* 2359 */       if (isDetached())
/*      */       {
/* 2361 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2363 */       throw exc;
/*      */     }
/* 2365 */     catch (Exception exc) {
/*      */       
/* 2367 */       if (!isDetached())
/*      */       {
/* 2369 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2371 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void registerOutParameter(String a, int b) throws SQLException {
/*      */     try {
/* 2379 */       maybeDirtyTransaction();
/*      */       
/* 2381 */       this.inner.registerOutParameter(a, b);
/*      */     }
/* 2383 */     catch (NullPointerException exc) {
/*      */       
/* 2385 */       if (isDetached())
/*      */       {
/* 2387 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2389 */       throw exc;
/*      */     }
/* 2391 */     catch (Exception exc) {
/*      */       
/* 2393 */       if (!isDetached())
/*      */       {
/* 2395 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2397 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void registerOutParameter(String a, int b, String c) throws SQLException {
/*      */     try {
/* 2405 */       maybeDirtyTransaction();
/*      */       
/* 2407 */       this.inner.registerOutParameter(a, b, c);
/*      */     }
/* 2409 */     catch (NullPointerException exc) {
/*      */       
/* 2411 */       if (isDetached())
/*      */       {
/* 2413 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2415 */       throw exc;
/*      */     }
/* 2417 */     catch (Exception exc) {
/*      */       
/* 2419 */       if (!isDetached())
/*      */       {
/* 2421 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2423 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void registerOutParameter(int a, int b, String c) throws SQLException {
/*      */     try {
/* 2431 */       maybeDirtyTransaction();
/*      */       
/* 2433 */       this.inner.registerOutParameter(a, b, c);
/*      */     }
/* 2435 */     catch (NullPointerException exc) {
/*      */       
/* 2437 */       if (isDetached())
/*      */       {
/* 2439 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2441 */       throw exc;
/*      */     }
/* 2443 */     catch (Exception exc) {
/*      */       
/* 2445 */       if (!isDetached())
/*      */       {
/* 2447 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2449 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void registerOutParameter(String a, int b, int c) throws SQLException {
/*      */     try {
/* 2457 */       maybeDirtyTransaction();
/*      */       
/* 2459 */       this.inner.registerOutParameter(a, b, c);
/*      */     }
/* 2461 */     catch (NullPointerException exc) {
/*      */       
/* 2463 */       if (isDetached())
/*      */       {
/* 2465 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2467 */       throw exc;
/*      */     }
/* 2469 */     catch (Exception exc) {
/*      */       
/* 2471 */       if (!isDetached())
/*      */       {
/* 2473 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2475 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean wasNull() throws SQLException {
/*      */     try {
/* 2483 */       maybeDirtyTransaction();
/*      */       
/* 2485 */       return this.inner.wasNull();
/*      */     }
/* 2487 */     catch (NullPointerException exc) {
/*      */       
/* 2489 */       if (isDetached())
/*      */       {
/* 2491 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2493 */       throw exc;
/*      */     }
/* 2495 */     catch (Exception exc) {
/*      */       
/* 2497 */       if (!isDetached())
/*      */       {
/* 2499 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2501 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(int a, int b) throws SQLException {
/*      */     try {
/* 2509 */       maybeDirtyTransaction();
/*      */       
/* 2511 */       return this.inner.getBigDecimal(a, b);
/*      */     }
/* 2513 */     catch (NullPointerException exc) {
/*      */       
/* 2515 */       if (isDetached())
/*      */       {
/* 2517 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2519 */       throw exc;
/*      */     }
/* 2521 */     catch (Exception exc) {
/*      */       
/* 2523 */       if (!isDetached())
/*      */       {
/* 2525 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2527 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(String a) throws SQLException {
/*      */     try {
/* 2535 */       maybeDirtyTransaction();
/*      */       
/* 2537 */       return this.inner.getBigDecimal(a);
/*      */     }
/* 2539 */     catch (NullPointerException exc) {
/*      */       
/* 2541 */       if (isDetached())
/*      */       {
/* 2543 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2545 */       throw exc;
/*      */     }
/* 2547 */     catch (Exception exc) {
/*      */       
/* 2549 */       if (!isDetached())
/*      */       {
/* 2551 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2553 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final BigDecimal getBigDecimal(int a) throws SQLException {
/*      */     try {
/* 2561 */       maybeDirtyTransaction();
/*      */       
/* 2563 */       return this.inner.getBigDecimal(a);
/*      */     }
/* 2565 */     catch (NullPointerException exc) {
/*      */       
/* 2567 */       if (isDetached())
/*      */       {
/* 2569 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2571 */       throw exc;
/*      */     }
/* 2573 */     catch (Exception exc) {
/*      */       
/* 2575 */       if (!isDetached())
/*      */       {
/* 2577 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2579 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Blob getBlob(int a) throws SQLException {
/*      */     try {
/* 2587 */       maybeDirtyTransaction();
/*      */       
/* 2589 */       return this.inner.getBlob(a);
/*      */     }
/* 2591 */     catch (NullPointerException exc) {
/*      */       
/* 2593 */       if (isDetached())
/*      */       {
/* 2595 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2597 */       throw exc;
/*      */     }
/* 2599 */     catch (Exception exc) {
/*      */       
/* 2601 */       if (!isDetached())
/*      */       {
/* 2603 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2605 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Blob getBlob(String a) throws SQLException {
/*      */     try {
/* 2613 */       maybeDirtyTransaction();
/*      */       
/* 2615 */       return this.inner.getBlob(a);
/*      */     }
/* 2617 */     catch (NullPointerException exc) {
/*      */       
/* 2619 */       if (isDetached())
/*      */       {
/* 2621 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2623 */       throw exc;
/*      */     }
/* 2625 */     catch (Exception exc) {
/*      */       
/* 2627 */       if (!isDetached())
/*      */       {
/* 2629 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2631 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Clob getClob(String a) throws SQLException {
/*      */     try {
/* 2639 */       maybeDirtyTransaction();
/*      */       
/* 2641 */       return this.inner.getClob(a);
/*      */     }
/* 2643 */     catch (NullPointerException exc) {
/*      */       
/* 2645 */       if (isDetached())
/*      */       {
/* 2647 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2649 */       throw exc;
/*      */     }
/* 2651 */     catch (Exception exc) {
/*      */       
/* 2653 */       if (!isDetached())
/*      */       {
/* 2655 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2657 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Clob getClob(int a) throws SQLException {
/*      */     try {
/* 2665 */       maybeDirtyTransaction();
/*      */       
/* 2667 */       return this.inner.getClob(a);
/*      */     }
/* 2669 */     catch (NullPointerException exc) {
/*      */       
/* 2671 */       if (isDetached())
/*      */       {
/* 2673 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2675 */       throw exc;
/*      */     }
/* 2677 */     catch (Exception exc) {
/*      */       
/* 2679 */       if (!isDetached())
/*      */       {
/* 2681 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2683 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final RowId getRowId(int a) throws SQLException {
/*      */     try {
/* 2691 */       maybeDirtyTransaction();
/*      */       
/* 2693 */       return this.inner.getRowId(a);
/*      */     }
/* 2695 */     catch (NullPointerException exc) {
/*      */       
/* 2697 */       if (isDetached())
/*      */       {
/* 2699 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2701 */       throw exc;
/*      */     }
/* 2703 */     catch (Exception exc) {
/*      */       
/* 2705 */       if (!isDetached())
/*      */       {
/* 2707 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2709 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final RowId getRowId(String a) throws SQLException {
/*      */     try {
/* 2717 */       maybeDirtyTransaction();
/*      */       
/* 2719 */       return this.inner.getRowId(a);
/*      */     }
/* 2721 */     catch (NullPointerException exc) {
/*      */       
/* 2723 */       if (isDetached())
/*      */       {
/* 2725 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2727 */       throw exc;
/*      */     }
/* 2729 */     catch (Exception exc) {
/*      */       
/* 2731 */       if (!isDetached())
/*      */       {
/* 2733 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2735 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final NClob getNClob(int a) throws SQLException {
/*      */     try {
/* 2743 */       maybeDirtyTransaction();
/*      */       
/* 2745 */       return this.inner.getNClob(a);
/*      */     }
/* 2747 */     catch (NullPointerException exc) {
/*      */       
/* 2749 */       if (isDetached())
/*      */       {
/* 2751 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2753 */       throw exc;
/*      */     }
/* 2755 */     catch (Exception exc) {
/*      */       
/* 2757 */       if (!isDetached())
/*      */       {
/* 2759 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2761 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final NClob getNClob(String a) throws SQLException {
/*      */     try {
/* 2769 */       maybeDirtyTransaction();
/*      */       
/* 2771 */       return this.inner.getNClob(a);
/*      */     }
/* 2773 */     catch (NullPointerException exc) {
/*      */       
/* 2775 */       if (isDetached())
/*      */       {
/* 2777 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2779 */       throw exc;
/*      */     }
/* 2781 */     catch (Exception exc) {
/*      */       
/* 2783 */       if (!isDetached())
/*      */       {
/* 2785 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2787 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLXML getSQLXML(String a) throws SQLException {
/*      */     try {
/* 2795 */       maybeDirtyTransaction();
/*      */       
/* 2797 */       return this.inner.getSQLXML(a);
/*      */     }
/* 2799 */     catch (NullPointerException exc) {
/*      */       
/* 2801 */       if (isDetached())
/*      */       {
/* 2803 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2805 */       throw exc;
/*      */     }
/* 2807 */     catch (Exception exc) {
/*      */       
/* 2809 */       if (!isDetached())
/*      */       {
/* 2811 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2813 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLXML getSQLXML(int a) throws SQLException {
/*      */     try {
/* 2821 */       maybeDirtyTransaction();
/*      */       
/* 2823 */       return this.inner.getSQLXML(a);
/*      */     }
/* 2825 */     catch (NullPointerException exc) {
/*      */       
/* 2827 */       if (isDetached())
/*      */       {
/* 2829 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2831 */       throw exc;
/*      */     }
/* 2833 */     catch (Exception exc) {
/*      */       
/* 2835 */       if (!isDetached())
/*      */       {
/* 2837 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2839 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNString(int a) throws SQLException {
/*      */     try {
/* 2847 */       maybeDirtyTransaction();
/*      */       
/* 2849 */       return this.inner.getNString(a);
/*      */     }
/* 2851 */     catch (NullPointerException exc) {
/*      */       
/* 2853 */       if (isDetached())
/*      */       {
/* 2855 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2857 */       throw exc;
/*      */     }
/* 2859 */     catch (Exception exc) {
/*      */       
/* 2861 */       if (!isDetached())
/*      */       {
/* 2863 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2865 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getNString(String a) throws SQLException {
/*      */     try {
/* 2873 */       maybeDirtyTransaction();
/*      */       
/* 2875 */       return this.inner.getNString(a);
/*      */     }
/* 2877 */     catch (NullPointerException exc) {
/*      */       
/* 2879 */       if (isDetached())
/*      */       {
/* 2881 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2883 */       throw exc;
/*      */     }
/* 2885 */     catch (Exception exc) {
/*      */       
/* 2887 */       if (!isDetached())
/*      */       {
/* 2889 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2891 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getNCharacterStream(String a) throws SQLException {
/*      */     try {
/* 2899 */       maybeDirtyTransaction();
/*      */       
/* 2901 */       return this.inner.getNCharacterStream(a);
/*      */     }
/* 2903 */     catch (NullPointerException exc) {
/*      */       
/* 2905 */       if (isDetached())
/*      */       {
/* 2907 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2909 */       throw exc;
/*      */     }
/* 2911 */     catch (Exception exc) {
/*      */       
/* 2913 */       if (!isDetached())
/*      */       {
/* 2915 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2917 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getNCharacterStream(int a) throws SQLException {
/*      */     try {
/* 2925 */       maybeDirtyTransaction();
/*      */       
/* 2927 */       return this.inner.getNCharacterStream(a);
/*      */     }
/* 2929 */     catch (NullPointerException exc) {
/*      */       
/* 2931 */       if (isDetached())
/*      */       {
/* 2933 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2935 */       throw exc;
/*      */     }
/* 2937 */     catch (Exception exc) {
/*      */       
/* 2939 */       if (!isDetached())
/*      */       {
/* 2941 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2943 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getCharacterStream(int a) throws SQLException {
/*      */     try {
/* 2951 */       maybeDirtyTransaction();
/*      */       
/* 2953 */       return this.inner.getCharacterStream(a);
/*      */     }
/* 2955 */     catch (NullPointerException exc) {
/*      */       
/* 2957 */       if (isDetached())
/*      */       {
/* 2959 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2961 */       throw exc;
/*      */     }
/* 2963 */     catch (Exception exc) {
/*      */       
/* 2965 */       if (!isDetached())
/*      */       {
/* 2967 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2969 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Reader getCharacterStream(String a) throws SQLException {
/*      */     try {
/* 2977 */       maybeDirtyTransaction();
/*      */       
/* 2979 */       return this.inner.getCharacterStream(a);
/*      */     }
/* 2981 */     catch (NullPointerException exc) {
/*      */       
/* 2983 */       if (isDetached())
/*      */       {
/* 2985 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 2987 */       throw exc;
/*      */     }
/* 2989 */     catch (Exception exc) {
/*      */       
/* 2991 */       if (!isDetached())
/*      */       {
/* 2993 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 2995 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBoolean(int a, boolean b) throws SQLException {
/*      */     try {
/* 3003 */       maybeDirtyTransaction();
/*      */       
/* 3005 */       this.inner.setBoolean(a, b);
/*      */     }
/* 3007 */     catch (NullPointerException exc) {
/*      */       
/* 3009 */       if (isDetached())
/*      */       {
/* 3011 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3013 */       throw exc;
/*      */     }
/* 3015 */     catch (Exception exc) {
/*      */       
/* 3017 */       if (!isDetached())
/*      */       {
/* 3019 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3021 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setByte(int a, byte b) throws SQLException {
/*      */     try {
/* 3029 */       maybeDirtyTransaction();
/*      */       
/* 3031 */       this.inner.setByte(a, b);
/*      */     }
/* 3033 */     catch (NullPointerException exc) {
/*      */       
/* 3035 */       if (isDetached())
/*      */       {
/* 3037 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3039 */       throw exc;
/*      */     }
/* 3041 */     catch (Exception exc) {
/*      */       
/* 3043 */       if (!isDetached())
/*      */       {
/* 3045 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3047 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setShort(int a, short b) throws SQLException {
/*      */     try {
/* 3055 */       maybeDirtyTransaction();
/*      */       
/* 3057 */       this.inner.setShort(a, b);
/*      */     }
/* 3059 */     catch (NullPointerException exc) {
/*      */       
/* 3061 */       if (isDetached())
/*      */       {
/* 3063 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3065 */       throw exc;
/*      */     }
/* 3067 */     catch (Exception exc) {
/*      */       
/* 3069 */       if (!isDetached())
/*      */       {
/* 3071 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3073 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setInt(int a, int b) throws SQLException {
/*      */     try {
/* 3081 */       maybeDirtyTransaction();
/*      */       
/* 3083 */       this.inner.setInt(a, b);
/*      */     }
/* 3085 */     catch (NullPointerException exc) {
/*      */       
/* 3087 */       if (isDetached())
/*      */       {
/* 3089 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3091 */       throw exc;
/*      */     }
/* 3093 */     catch (Exception exc) {
/*      */       
/* 3095 */       if (!isDetached())
/*      */       {
/* 3097 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3099 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setLong(int a, long b) throws SQLException {
/*      */     try {
/* 3107 */       maybeDirtyTransaction();
/*      */       
/* 3109 */       this.inner.setLong(a, b);
/*      */     }
/* 3111 */     catch (NullPointerException exc) {
/*      */       
/* 3113 */       if (isDetached())
/*      */       {
/* 3115 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3117 */       throw exc;
/*      */     }
/* 3119 */     catch (Exception exc) {
/*      */       
/* 3121 */       if (!isDetached())
/*      */       {
/* 3123 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3125 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFloat(int a, float b) throws SQLException {
/*      */     try {
/* 3133 */       maybeDirtyTransaction();
/*      */       
/* 3135 */       this.inner.setFloat(a, b);
/*      */     }
/* 3137 */     catch (NullPointerException exc) {
/*      */       
/* 3139 */       if (isDetached())
/*      */       {
/* 3141 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3143 */       throw exc;
/*      */     }
/* 3145 */     catch (Exception exc) {
/*      */       
/* 3147 */       if (!isDetached())
/*      */       {
/* 3149 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3151 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDouble(int a, double b) throws SQLException {
/*      */     try {
/* 3159 */       maybeDirtyTransaction();
/*      */       
/* 3161 */       this.inner.setDouble(a, b);
/*      */     }
/* 3163 */     catch (NullPointerException exc) {
/*      */       
/* 3165 */       if (isDetached())
/*      */       {
/* 3167 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3169 */       throw exc;
/*      */     }
/* 3171 */     catch (Exception exc) {
/*      */       
/* 3173 */       if (!isDetached())
/*      */       {
/* 3175 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3177 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTimestamp(int a, Timestamp b) throws SQLException {
/*      */     try {
/* 3185 */       maybeDirtyTransaction();
/*      */       
/* 3187 */       this.inner.setTimestamp(a, b);
/*      */     }
/* 3189 */     catch (NullPointerException exc) {
/*      */       
/* 3191 */       if (isDetached())
/*      */       {
/* 3193 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3195 */       throw exc;
/*      */     }
/* 3197 */     catch (Exception exc) {
/*      */       
/* 3199 */       if (!isDetached())
/*      */       {
/* 3201 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3203 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTimestamp(int a, Timestamp b, Calendar c) throws SQLException {
/*      */     try {
/* 3211 */       maybeDirtyTransaction();
/*      */       
/* 3213 */       this.inner.setTimestamp(a, b, c);
/*      */     }
/* 3215 */     catch (NullPointerException exc) {
/*      */       
/* 3217 */       if (isDetached())
/*      */       {
/* 3219 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3221 */       throw exc;
/*      */     }
/* 3223 */     catch (Exception exc) {
/*      */       
/* 3225 */       if (!isDetached())
/*      */       {
/* 3227 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3229 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setURL(int a, URL b) throws SQLException {
/*      */     try {
/* 3237 */       maybeDirtyTransaction();
/*      */       
/* 3239 */       this.inner.setURL(a, b);
/*      */     }
/* 3241 */     catch (NullPointerException exc) {
/*      */       
/* 3243 */       if (isDetached())
/*      */       {
/* 3245 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3247 */       throw exc;
/*      */     }
/* 3249 */     catch (Exception exc) {
/*      */       
/* 3251 */       if (!isDetached())
/*      */       {
/* 3253 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3255 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTime(int a, Time b, Calendar c) throws SQLException {
/*      */     try {
/* 3263 */       maybeDirtyTransaction();
/*      */       
/* 3265 */       this.inner.setTime(a, b, c);
/*      */     }
/* 3267 */     catch (NullPointerException exc) {
/*      */       
/* 3269 */       if (isDetached())
/*      */       {
/* 3271 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3273 */       throw exc;
/*      */     }
/* 3275 */     catch (Exception exc) {
/*      */       
/* 3277 */       if (!isDetached())
/*      */       {
/* 3279 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3281 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setTime(int a, Time b) throws SQLException {
/*      */     try {
/* 3289 */       maybeDirtyTransaction();
/*      */       
/* 3291 */       this.inner.setTime(a, b);
/*      */     }
/* 3293 */     catch (NullPointerException exc) {
/*      */       
/* 3295 */       if (isDetached())
/*      */       {
/* 3297 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3299 */       throw exc;
/*      */     }
/* 3301 */     catch (Exception exc) {
/*      */       
/* 3303 */       if (!isDetached())
/*      */       {
/* 3305 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3307 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSetMetaData getMetaData() throws SQLException {
/*      */     try {
/* 3315 */       maybeDirtyTransaction();
/*      */       
/* 3317 */       return this.inner.getMetaData();
/*      */     }
/* 3319 */     catch (NullPointerException exc) {
/*      */       
/* 3321 */       if (isDetached())
/*      */       {
/* 3323 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3325 */       throw exc;
/*      */     }
/* 3327 */     catch (Exception exc) {
/*      */       
/* 3329 */       if (!isDetached())
/*      */       {
/* 3331 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3333 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute() throws SQLException {
/*      */     try {
/* 3341 */       maybeDirtyTransaction();
/*      */       
/* 3343 */       return this.inner.execute();
/*      */     }
/* 3345 */     catch (NullPointerException exc) {
/*      */       
/* 3347 */       if (isDetached())
/*      */       {
/* 3349 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3351 */       throw exc;
/*      */     }
/* 3353 */     catch (Exception exc) {
/*      */       
/* 3355 */       if (!isDetached())
/*      */       {
/* 3357 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3359 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet executeQuery() throws SQLException {
/*      */     try {
/* 3367 */       maybeDirtyTransaction();
/*      */       
/* 3369 */       ResultSet innerResultSet = this.inner.executeQuery();
/* 3370 */       if (innerResultSet == null) return null; 
/* 3371 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 3372 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 3373 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 3374 */        return out;
/*      */     }
/* 3376 */     catch (NullPointerException exc) {
/*      */       
/* 3378 */       if (isDetached())
/*      */       {
/* 3380 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3382 */       throw exc;
/*      */     }
/* 3384 */     catch (Exception exc) {
/*      */       
/* 3386 */       if (!isDetached())
/*      */       {
/* 3388 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3390 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate() throws SQLException {
/*      */     try {
/* 3398 */       maybeDirtyTransaction();
/*      */       
/* 3400 */       return this.inner.executeUpdate();
/*      */     }
/* 3402 */     catch (NullPointerException exc) {
/*      */       
/* 3404 */       if (isDetached())
/*      */       {
/* 3406 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3408 */       throw exc;
/*      */     }
/* 3410 */     catch (Exception exc) {
/*      */       
/* 3412 */       if (!isDetached())
/*      */       {
/* 3414 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3416 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addBatch() throws SQLException {
/*      */     try {
/* 3424 */       maybeDirtyTransaction();
/*      */       
/* 3426 */       this.inner.addBatch();
/*      */     }
/* 3428 */     catch (NullPointerException exc) {
/*      */       
/* 3430 */       if (isDetached())
/*      */       {
/* 3432 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void setNull(int a, int b) throws SQLException {
/*      */     try {
/* 3450 */       maybeDirtyTransaction();
/*      */       
/* 3452 */       this.inner.setNull(a, b);
/*      */     }
/* 3454 */     catch (NullPointerException exc) {
/*      */       
/* 3456 */       if (isDetached())
/*      */       {
/* 3458 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3460 */       throw exc;
/*      */     }
/* 3462 */     catch (Exception exc) {
/*      */       
/* 3464 */       if (!isDetached())
/*      */       {
/* 3466 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3468 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNull(int a, int b, String c) throws SQLException {
/*      */     try {
/* 3476 */       maybeDirtyTransaction();
/*      */       
/* 3478 */       this.inner.setNull(a, b, c);
/*      */     }
/* 3480 */     catch (NullPointerException exc) {
/*      */       
/* 3482 */       if (isDetached())
/*      */       {
/* 3484 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3486 */       throw exc;
/*      */     }
/* 3488 */     catch (Exception exc) {
/*      */       
/* 3490 */       if (!isDetached())
/*      */       {
/* 3492 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3494 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBigDecimal(int a, BigDecimal b) throws SQLException {
/*      */     try {
/* 3502 */       maybeDirtyTransaction();
/*      */       
/* 3504 */       this.inner.setBigDecimal(a, b);
/*      */     }
/* 3506 */     catch (NullPointerException exc) {
/*      */       
/* 3508 */       if (isDetached())
/*      */       {
/* 3510 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3512 */       throw exc;
/*      */     }
/* 3514 */     catch (Exception exc) {
/*      */       
/* 3516 */       if (!isDetached())
/*      */       {
/* 3518 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3520 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setString(int a, String b) throws SQLException {
/*      */     try {
/* 3528 */       maybeDirtyTransaction();
/*      */       
/* 3530 */       this.inner.setString(a, b);
/*      */     }
/* 3532 */     catch (NullPointerException exc) {
/*      */       
/* 3534 */       if (isDetached())
/*      */       {
/* 3536 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3538 */       throw exc;
/*      */     }
/* 3540 */     catch (Exception exc) {
/*      */       
/* 3542 */       if (!isDetached())
/*      */       {
/* 3544 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3546 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBytes(int a, byte[] b) throws SQLException {
/*      */     try {
/* 3554 */       maybeDirtyTransaction();
/*      */       
/* 3556 */       this.inner.setBytes(a, b);
/*      */     }
/* 3558 */     catch (NullPointerException exc) {
/*      */       
/* 3560 */       if (isDetached())
/*      */       {
/* 3562 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3564 */       throw exc;
/*      */     }
/* 3566 */     catch (Exception exc) {
/*      */       
/* 3568 */       if (!isDetached())
/*      */       {
/* 3570 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3572 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDate(int a, Date b) throws SQLException {
/*      */     try {
/* 3580 */       maybeDirtyTransaction();
/*      */       
/* 3582 */       this.inner.setDate(a, b);
/*      */     }
/* 3584 */     catch (NullPointerException exc) {
/*      */       
/* 3586 */       if (isDetached())
/*      */       {
/* 3588 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3590 */       throw exc;
/*      */     }
/* 3592 */     catch (Exception exc) {
/*      */       
/* 3594 */       if (!isDetached())
/*      */       {
/* 3596 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3598 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setDate(int a, Date b, Calendar c) throws SQLException {
/*      */     try {
/* 3606 */       maybeDirtyTransaction();
/*      */       
/* 3608 */       this.inner.setDate(a, b, c);
/*      */     }
/* 3610 */     catch (NullPointerException exc) {
/*      */       
/* 3612 */       if (isDetached())
/*      */       {
/* 3614 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3616 */       throw exc;
/*      */     }
/* 3618 */     catch (Exception exc) {
/*      */       
/* 3620 */       if (!isDetached())
/*      */       {
/* 3622 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3624 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(int a, InputStream b) throws SQLException {
/*      */     try {
/* 3632 */       maybeDirtyTransaction();
/*      */       
/* 3634 */       this.inner.setAsciiStream(a, b);
/*      */     }
/* 3636 */     catch (NullPointerException exc) {
/*      */       
/* 3638 */       if (isDetached())
/*      */       {
/* 3640 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3642 */       throw exc;
/*      */     }
/* 3644 */     catch (Exception exc) {
/*      */       
/* 3646 */       if (!isDetached())
/*      */       {
/* 3648 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3650 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 3658 */       maybeDirtyTransaction();
/*      */       
/* 3660 */       this.inner.setAsciiStream(a, b, c);
/*      */     }
/* 3662 */     catch (NullPointerException exc) {
/*      */       
/* 3664 */       if (isDetached())
/*      */       {
/* 3666 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3668 */       throw exc;
/*      */     }
/* 3670 */     catch (Exception exc) {
/*      */       
/* 3672 */       if (!isDetached())
/*      */       {
/* 3674 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3676 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setAsciiStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 3684 */       maybeDirtyTransaction();
/*      */       
/* 3686 */       this.inner.setAsciiStream(a, b, c);
/*      */     }
/* 3688 */     catch (NullPointerException exc) {
/*      */       
/* 3690 */       if (isDetached())
/*      */       {
/* 3692 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3694 */       throw exc;
/*      */     }
/* 3696 */     catch (Exception exc) {
/*      */       
/* 3698 */       if (!isDetached())
/*      */       {
/* 3700 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3702 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setUnicodeStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 3710 */       maybeDirtyTransaction();
/*      */       
/* 3712 */       this.inner.setUnicodeStream(a, b, c);
/*      */     }
/* 3714 */     catch (NullPointerException exc) {
/*      */       
/* 3716 */       if (isDetached())
/*      */       {
/* 3718 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3720 */       throw exc;
/*      */     }
/* 3722 */     catch (Exception exc) {
/*      */       
/* 3724 */       if (!isDetached())
/*      */       {
/* 3726 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3728 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 3736 */       maybeDirtyTransaction();
/*      */       
/* 3738 */       this.inner.setBinaryStream(a, b, c);
/*      */     }
/* 3740 */     catch (NullPointerException exc) {
/*      */       
/* 3742 */       if (isDetached())
/*      */       {
/* 3744 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3746 */       throw exc;
/*      */     }
/* 3748 */     catch (Exception exc) {
/*      */       
/* 3750 */       if (!isDetached())
/*      */       {
/* 3752 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3754 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBinaryStream(int a, InputStream b, int c) throws SQLException {
/*      */     try {
/* 3762 */       maybeDirtyTransaction();
/*      */       
/* 3764 */       this.inner.setBinaryStream(a, b, c);
/*      */     }
/* 3766 */     catch (NullPointerException exc) {
/*      */       
/* 3768 */       if (isDetached())
/*      */       {
/* 3770 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void setBinaryStream(int a, InputStream b) throws SQLException {
/*      */     try {
/* 3788 */       maybeDirtyTransaction();
/*      */       
/* 3790 */       this.inner.setBinaryStream(a, b);
/*      */     }
/* 3792 */     catch (NullPointerException exc) {
/*      */       
/* 3794 */       if (isDetached())
/*      */       {
/* 3796 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void clearParameters() throws SQLException {
/*      */     try {
/* 3814 */       maybeDirtyTransaction();
/*      */       
/* 3816 */       this.inner.clearParameters();
/*      */     }
/* 3818 */     catch (NullPointerException exc) {
/*      */       
/* 3820 */       if (isDetached())
/*      */       {
/* 3822 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void setObject(int a, Object b, int c, int d) throws SQLException {
/*      */     try {
/* 3840 */       maybeDirtyTransaction();
/*      */       
/* 3842 */       this.inner.setObject(a, b, c, d);
/*      */     }
/* 3844 */     catch (NullPointerException exc) {
/*      */       
/* 3846 */       if (isDetached())
/*      */       {
/* 3848 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3850 */       throw exc;
/*      */     }
/* 3852 */     catch (Exception exc) {
/*      */       
/* 3854 */       if (!isDetached())
/*      */       {
/* 3856 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3858 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(int a, Object b, int c) throws SQLException {
/*      */     try {
/* 3866 */       maybeDirtyTransaction();
/*      */       
/* 3868 */       this.inner.setObject(a, b, c);
/*      */     }
/* 3870 */     catch (NullPointerException exc) {
/*      */       
/* 3872 */       if (isDetached())
/*      */       {
/* 3874 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3876 */       throw exc;
/*      */     }
/* 3878 */     catch (Exception exc) {
/*      */       
/* 3880 */       if (!isDetached())
/*      */       {
/* 3882 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3884 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setObject(int a, Object b) throws SQLException {
/*      */     try {
/* 3892 */       maybeDirtyTransaction();
/*      */       
/* 3894 */       this.inner.setObject(a, b);
/*      */     }
/* 3896 */     catch (NullPointerException exc) {
/*      */       
/* 3898 */       if (isDetached())
/*      */       {
/* 3900 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3902 */       throw exc;
/*      */     }
/* 3904 */     catch (Exception exc) {
/*      */       
/* 3906 */       if (!isDetached())
/*      */       {
/* 3908 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3910 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 3918 */       maybeDirtyTransaction();
/*      */       
/* 3920 */       this.inner.setCharacterStream(a, b, c);
/*      */     }
/* 3922 */     catch (NullPointerException exc) {
/*      */       
/* 3924 */       if (isDetached())
/*      */       {
/* 3926 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3928 */       throw exc;
/*      */     }
/* 3930 */     catch (Exception exc) {
/*      */       
/* 3932 */       if (!isDetached())
/*      */       {
/* 3934 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3936 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(int a, Reader b, int c) throws SQLException {
/*      */     try {
/* 3944 */       maybeDirtyTransaction();
/*      */       
/* 3946 */       this.inner.setCharacterStream(a, b, c);
/*      */     }
/* 3948 */     catch (NullPointerException exc) {
/*      */       
/* 3950 */       if (isDetached())
/*      */       {
/* 3952 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3954 */       throw exc;
/*      */     }
/* 3956 */     catch (Exception exc) {
/*      */       
/* 3958 */       if (!isDetached())
/*      */       {
/* 3960 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3962 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCharacterStream(int a, Reader b) throws SQLException {
/*      */     try {
/* 3970 */       maybeDirtyTransaction();
/*      */       
/* 3972 */       this.inner.setCharacterStream(a, b);
/*      */     }
/* 3974 */     catch (NullPointerException exc) {
/*      */       
/* 3976 */       if (isDetached())
/*      */       {
/* 3978 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 3980 */       throw exc;
/*      */     }
/* 3982 */     catch (Exception exc) {
/*      */       
/* 3984 */       if (!isDetached())
/*      */       {
/* 3986 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 3988 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRef(int a, Ref b) throws SQLException {
/*      */     try {
/* 3996 */       maybeDirtyTransaction();
/*      */       
/* 3998 */       this.inner.setRef(a, b);
/*      */     }
/* 4000 */     catch (NullPointerException exc) {
/*      */       
/* 4002 */       if (isDetached())
/*      */       {
/* 4004 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4006 */       throw exc;
/*      */     }
/* 4008 */     catch (Exception exc) {
/*      */       
/* 4010 */       if (!isDetached())
/*      */       {
/* 4012 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4014 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(int a, InputStream b, long c) throws SQLException {
/*      */     try {
/* 4022 */       maybeDirtyTransaction();
/*      */       
/* 4024 */       this.inner.setBlob(a, b, c);
/*      */     }
/* 4026 */     catch (NullPointerException exc) {
/*      */       
/* 4028 */       if (isDetached())
/*      */       {
/* 4030 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4032 */       throw exc;
/*      */     }
/* 4034 */     catch (Exception exc) {
/*      */       
/* 4036 */       if (!isDetached())
/*      */       {
/* 4038 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4040 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(int a, InputStream b) throws SQLException {
/*      */     try {
/* 4048 */       maybeDirtyTransaction();
/*      */       
/* 4050 */       this.inner.setBlob(a, b);
/*      */     }
/* 4052 */     catch (NullPointerException exc) {
/*      */       
/* 4054 */       if (isDetached())
/*      */       {
/* 4056 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4058 */       throw exc;
/*      */     }
/* 4060 */     catch (Exception exc) {
/*      */       
/* 4062 */       if (!isDetached())
/*      */       {
/* 4064 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4066 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setBlob(int a, Blob b) throws SQLException {
/*      */     try {
/* 4074 */       maybeDirtyTransaction();
/*      */       
/* 4076 */       this.inner.setBlob(a, b);
/*      */     }
/* 4078 */     catch (NullPointerException exc) {
/*      */       
/* 4080 */       if (isDetached())
/*      */       {
/* 4082 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4084 */       throw exc;
/*      */     }
/* 4086 */     catch (Exception exc) {
/*      */       
/* 4088 */       if (!isDetached())
/*      */       {
/* 4090 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4092 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(int a, Reader b) throws SQLException {
/*      */     try {
/* 4100 */       maybeDirtyTransaction();
/*      */       
/* 4102 */       this.inner.setClob(a, b);
/*      */     }
/* 4104 */     catch (NullPointerException exc) {
/*      */       
/* 4106 */       if (isDetached())
/*      */       {
/* 4108 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4110 */       throw exc;
/*      */     }
/* 4112 */     catch (Exception exc) {
/*      */       
/* 4114 */       if (!isDetached())
/*      */       {
/* 4116 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4118 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 4126 */       maybeDirtyTransaction();
/*      */       
/* 4128 */       this.inner.setClob(a, b, c);
/*      */     }
/* 4130 */     catch (NullPointerException exc) {
/*      */       
/* 4132 */       if (isDetached())
/*      */       {
/* 4134 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4136 */       throw exc;
/*      */     }
/* 4138 */     catch (Exception exc) {
/*      */       
/* 4140 */       if (!isDetached())
/*      */       {
/* 4142 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4144 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setClob(int a, Clob b) throws SQLException {
/*      */     try {
/* 4152 */       maybeDirtyTransaction();
/*      */       
/* 4154 */       this.inner.setClob(a, b);
/*      */     }
/* 4156 */     catch (NullPointerException exc) {
/*      */       
/* 4158 */       if (isDetached())
/*      */       {
/* 4160 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4162 */       throw exc;
/*      */     }
/* 4164 */     catch (Exception exc) {
/*      */       
/* 4166 */       if (!isDetached())
/*      */       {
/* 4168 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4170 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setArray(int a, Array b) throws SQLException {
/*      */     try {
/* 4178 */       maybeDirtyTransaction();
/*      */       
/* 4180 */       this.inner.setArray(a, b);
/*      */     }
/* 4182 */     catch (NullPointerException exc) {
/*      */       
/* 4184 */       if (isDetached())
/*      */       {
/* 4186 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4188 */       throw exc;
/*      */     }
/* 4190 */     catch (Exception exc) {
/*      */       
/* 4192 */       if (!isDetached())
/*      */       {
/* 4194 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4196 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ParameterMetaData getParameterMetaData() throws SQLException {
/*      */     try {
/* 4204 */       maybeDirtyTransaction();
/*      */       
/* 4206 */       return this.inner.getParameterMetaData();
/*      */     }
/* 4208 */     catch (NullPointerException exc) {
/*      */       
/* 4210 */       if (isDetached())
/*      */       {
/* 4212 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4214 */       throw exc;
/*      */     }
/* 4216 */     catch (Exception exc) {
/*      */       
/* 4218 */       if (!isDetached())
/*      */       {
/* 4220 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4222 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setRowId(int a, RowId b) throws SQLException {
/*      */     try {
/* 4230 */       maybeDirtyTransaction();
/*      */       
/* 4232 */       this.inner.setRowId(a, b);
/*      */     }
/* 4234 */     catch (NullPointerException exc) {
/*      */       
/* 4236 */       if (isDetached())
/*      */       {
/* 4238 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4240 */       throw exc;
/*      */     }
/* 4242 */     catch (Exception exc) {
/*      */       
/* 4244 */       if (!isDetached())
/*      */       {
/* 4246 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4248 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNString(int a, String b) throws SQLException {
/*      */     try {
/* 4256 */       maybeDirtyTransaction();
/*      */       
/* 4258 */       this.inner.setNString(a, b);
/*      */     }
/* 4260 */     catch (NullPointerException exc) {
/*      */       
/* 4262 */       if (isDetached())
/*      */       {
/* 4264 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4266 */       throw exc;
/*      */     }
/* 4268 */     catch (Exception exc) {
/*      */       
/* 4270 */       if (!isDetached())
/*      */       {
/* 4272 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4274 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNCharacterStream(int a, Reader b) throws SQLException {
/*      */     try {
/* 4282 */       maybeDirtyTransaction();
/*      */       
/* 4284 */       this.inner.setNCharacterStream(a, b);
/*      */     }
/* 4286 */     catch (NullPointerException exc) {
/*      */       
/* 4288 */       if (isDetached())
/*      */       {
/* 4290 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4292 */       throw exc;
/*      */     }
/* 4294 */     catch (Exception exc) {
/*      */       
/* 4296 */       if (!isDetached())
/*      */       {
/* 4298 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4300 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNCharacterStream(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 4308 */       maybeDirtyTransaction();
/*      */       
/* 4310 */       this.inner.setNCharacterStream(a, b, c);
/*      */     }
/* 4312 */     catch (NullPointerException exc) {
/*      */       
/* 4314 */       if (isDetached())
/*      */       {
/* 4316 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4318 */       throw exc;
/*      */     }
/* 4320 */     catch (Exception exc) {
/*      */       
/* 4322 */       if (!isDetached())
/*      */       {
/* 4324 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4326 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(int a, Reader b, long c) throws SQLException {
/*      */     try {
/* 4334 */       maybeDirtyTransaction();
/*      */       
/* 4336 */       this.inner.setNClob(a, b, c);
/*      */     }
/* 4338 */     catch (NullPointerException exc) {
/*      */       
/* 4340 */       if (isDetached())
/*      */       {
/* 4342 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4344 */       throw exc;
/*      */     }
/* 4346 */     catch (Exception exc) {
/*      */       
/* 4348 */       if (!isDetached())
/*      */       {
/* 4350 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4352 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(int a, NClob b) throws SQLException {
/*      */     try {
/* 4360 */       maybeDirtyTransaction();
/*      */       
/* 4362 */       this.inner.setNClob(a, b);
/*      */     }
/* 4364 */     catch (NullPointerException exc) {
/*      */       
/* 4366 */       if (isDetached())
/*      */       {
/* 4368 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4370 */       throw exc;
/*      */     }
/* 4372 */     catch (Exception exc) {
/*      */       
/* 4374 */       if (!isDetached())
/*      */       {
/* 4376 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4378 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setNClob(int a, Reader b) throws SQLException {
/*      */     try {
/* 4386 */       maybeDirtyTransaction();
/*      */       
/* 4388 */       this.inner.setNClob(a, b);
/*      */     }
/* 4390 */     catch (NullPointerException exc) {
/*      */       
/* 4392 */       if (isDetached())
/*      */       {
/* 4394 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4396 */       throw exc;
/*      */     }
/* 4398 */     catch (Exception exc) {
/*      */       
/* 4400 */       if (!isDetached())
/*      */       {
/* 4402 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4404 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setSQLXML(int a, SQLXML b) throws SQLException {
/*      */     try {
/* 4412 */       maybeDirtyTransaction();
/*      */       
/* 4414 */       this.inner.setSQLXML(a, b);
/*      */     }
/* 4416 */     catch (NullPointerException exc) {
/*      */       
/* 4418 */       if (isDetached())
/*      */       {
/* 4420 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4422 */       throw exc;
/*      */     }
/* 4424 */     catch (Exception exc) {
/*      */       
/* 4426 */       if (!isDetached())
/*      */       {
/* 4428 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4430 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void close() throws SQLException {
/*      */     try {
/* 4438 */       maybeDirtyTransaction();
/*      */       
/* 4440 */       if (!isDetached())
/*      */       {
/* 4442 */         synchronized (this.myProxyResultSets) {
/*      */           
/* 4444 */           for (Iterator<ResultSet> ii = this.myProxyResultSets.iterator(); ii.hasNext(); ) {
/*      */             
/* 4446 */             ResultSet closeMe = ii.next();
/* 4447 */             ii.remove();
/*      */             try {
/* 4449 */               closeMe.close();
/* 4450 */             } catch (SQLException e) {
/*      */               
/* 4452 */               if (logger.isLoggable(MLevel.WARNING))
/* 4453 */                 logger.log(MLevel.WARNING, "Exception on close of apparently orphaned ResultSet.", e); 
/*      */             } 
/* 4455 */             if (logger.isLoggable(MLevel.FINE)) {
/* 4456 */               logger.log(MLevel.FINE, this + " closed orphaned ResultSet: " + closeMe);
/*      */             }
/*      */           } 
/*      */         } 
/* 4460 */         if (this.is_cached) {
/* 4461 */           this.parentPooledConnection.checkinStatement(this.inner);
/*      */         } else {
/*      */           
/* 4464 */           this.parentPooledConnection.markInactiveUncachedStatement(this.inner); try {
/* 4465 */             this.inner.close();
/* 4466 */           } catch (Exception e) {
/*      */             
/* 4468 */             if (logger.isLoggable(MLevel.WARNING))
/* 4469 */               logger.log(MLevel.WARNING, "Exception on close of inner statement.", e); 
/* 4470 */             SQLException sqle = SqlUtils.toSQLException(e);
/* 4471 */             throw sqle;
/*      */           } 
/*      */         } 
/*      */         
/* 4475 */         detach();
/* 4476 */         this.inner = null;
/* 4477 */         this.creatorProxy = null;
/*      */       }
/*      */     
/* 4480 */     } catch (NullPointerException exc) {
/*      */       
/* 4482 */       if (isDetached()) {
/*      */         
/* 4484 */         if (logger.isLoggable(MLevel.FINE))
/*      */         {
/* 4486 */           logger.log(MLevel.FINE, this + ": close() called more than once.");
/*      */         }
/*      */       } else {
/* 4489 */         throw exc;
/*      */       } 
/* 4491 */     } catch (Exception exc) {
/*      */       
/* 4493 */       if (!isDetached())
/*      */       {
/* 4495 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4497 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final Connection getConnection() throws SQLException {
/*      */     try {
/* 4505 */       maybeDirtyTransaction();
/*      */       
/* 4507 */       if (!isDetached()) {
/* 4508 */         return this.creatorProxy;
/*      */       }
/* 4510 */       throw new SQLException("You cannot operate on a closed Statement!");
/*      */     }
/* 4512 */     catch (NullPointerException exc) {
/*      */       
/* 4514 */       if (isDetached())
/*      */       {
/* 4516 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4518 */       throw exc;
/*      */     }
/* 4520 */     catch (Exception exc) {
/*      */       
/* 4522 */       if (!isDetached())
/*      */       {
/* 4524 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4526 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final SQLWarning getWarnings() throws SQLException {
/*      */     try {
/* 4534 */       maybeDirtyTransaction();
/*      */       
/* 4536 */       return this.inner.getWarnings();
/*      */     }
/* 4538 */     catch (NullPointerException exc) {
/*      */       
/* 4540 */       if (isDetached())
/*      */       {
/* 4542 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4544 */       throw exc;
/*      */     }
/* 4546 */     catch (Exception exc) {
/*      */       
/* 4548 */       if (!isDetached())
/*      */       {
/* 4550 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4552 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearWarnings() throws SQLException {
/*      */     try {
/* 4560 */       maybeDirtyTransaction();
/*      */       
/* 4562 */       this.inner.clearWarnings();
/*      */     }
/* 4564 */     catch (NullPointerException exc) {
/*      */       
/* 4566 */       if (isDetached())
/*      */       {
/* 4568 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4570 */       throw exc;
/*      */     }
/* 4572 */     catch (Exception exc) {
/*      */       
/* 4574 */       if (!isDetached())
/*      */       {
/* 4576 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4578 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isClosed() throws SQLException {
/*      */     try {
/* 4586 */       maybeDirtyTransaction();
/*      */       
/* 4588 */       return isDetached();
/*      */     }
/* 4590 */     catch (NullPointerException exc) {
/*      */       
/* 4592 */       if (isDetached())
/*      */       {
/* 4594 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4596 */       throw exc;
/*      */     }
/* 4598 */     catch (Exception exc) {
/*      */       
/* 4600 */       if (!isDetached())
/*      */       {
/* 4602 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4604 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a) throws SQLException {
/*      */     try {
/* 4612 */       maybeDirtyTransaction();
/*      */       
/* 4614 */       return this.inner.execute(a);
/*      */     }
/* 4616 */     catch (NullPointerException exc) {
/*      */       
/* 4618 */       if (isDetached())
/*      */       {
/* 4620 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4622 */       throw exc;
/*      */     }
/* 4624 */     catch (Exception exc) {
/*      */       
/* 4626 */       if (!isDetached())
/*      */       {
/* 4628 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4630 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a, String[] b) throws SQLException {
/*      */     try {
/* 4638 */       maybeDirtyTransaction();
/*      */       
/* 4640 */       return this.inner.execute(a, b);
/*      */     }
/* 4642 */     catch (NullPointerException exc) {
/*      */       
/* 4644 */       if (isDetached())
/*      */       {
/* 4646 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4648 */       throw exc;
/*      */     }
/* 4650 */     catch (Exception exc) {
/*      */       
/* 4652 */       if (!isDetached())
/*      */       {
/* 4654 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4656 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a, int[] b) throws SQLException {
/*      */     try {
/* 4664 */       maybeDirtyTransaction();
/*      */       
/* 4666 */       return this.inner.execute(a, b);
/*      */     }
/* 4668 */     catch (NullPointerException exc) {
/*      */       
/* 4670 */       if (isDetached())
/*      */       {
/* 4672 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4674 */       throw exc;
/*      */     }
/* 4676 */     catch (Exception exc) {
/*      */       
/* 4678 */       if (!isDetached())
/*      */       {
/* 4680 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4682 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean execute(String a, int b) throws SQLException {
/*      */     try {
/* 4690 */       maybeDirtyTransaction();
/*      */       
/* 4692 */       return this.inner.execute(a, b);
/*      */     }
/* 4694 */     catch (NullPointerException exc) {
/*      */       
/* 4696 */       if (isDetached())
/*      */       {
/* 4698 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4700 */       throw exc;
/*      */     }
/* 4702 */     catch (Exception exc) {
/*      */       
/* 4704 */       if (!isDetached())
/*      */       {
/* 4706 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4708 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet executeQuery(String a) throws SQLException {
/*      */     try {
/* 4716 */       maybeDirtyTransaction();
/*      */       
/* 4718 */       ResultSet innerResultSet = this.inner.executeQuery(a);
/* 4719 */       if (innerResultSet == null) return null; 
/* 4720 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 4721 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 4722 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 4723 */        return out;
/*      */     }
/* 4725 */     catch (NullPointerException exc) {
/*      */       
/* 4727 */       if (isDetached())
/*      */       {
/* 4729 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4731 */       throw exc;
/*      */     }
/* 4733 */     catch (Exception exc) {
/*      */       
/* 4735 */       if (!isDetached())
/*      */       {
/* 4737 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4739 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, int b) throws SQLException {
/*      */     try {
/* 4747 */       maybeDirtyTransaction();
/*      */       
/* 4749 */       return this.inner.executeUpdate(a, b);
/*      */     }
/* 4751 */     catch (NullPointerException exc) {
/*      */       
/* 4753 */       if (isDetached())
/*      */       {
/* 4755 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4757 */       throw exc;
/*      */     }
/* 4759 */     catch (Exception exc) {
/*      */       
/* 4761 */       if (!isDetached())
/*      */       {
/* 4763 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4765 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, int[] b) throws SQLException {
/*      */     try {
/* 4773 */       maybeDirtyTransaction();
/*      */       
/* 4775 */       return this.inner.executeUpdate(a, b);
/*      */     }
/* 4777 */     catch (NullPointerException exc) {
/*      */       
/* 4779 */       if (isDetached())
/*      */       {
/* 4781 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4783 */       throw exc;
/*      */     }
/* 4785 */     catch (Exception exc) {
/*      */       
/* 4787 */       if (!isDetached())
/*      */       {
/* 4789 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4791 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, String[] b) throws SQLException {
/*      */     try {
/* 4799 */       maybeDirtyTransaction();
/*      */       
/* 4801 */       return this.inner.executeUpdate(a, b);
/*      */     }
/* 4803 */     catch (NullPointerException exc) {
/*      */       
/* 4805 */       if (isDetached())
/*      */       {
/* 4807 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4809 */       throw exc;
/*      */     }
/* 4811 */     catch (Exception exc) {
/*      */       
/* 4813 */       if (!isDetached())
/*      */       {
/* 4815 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4817 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a) throws SQLException {
/*      */     try {
/* 4825 */       maybeDirtyTransaction();
/*      */       
/* 4827 */       return this.inner.executeUpdate(a);
/*      */     }
/* 4829 */     catch (NullPointerException exc) {
/*      */       
/* 4831 */       if (isDetached())
/*      */       {
/* 4833 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4835 */       throw exc;
/*      */     }
/* 4837 */     catch (Exception exc) {
/*      */       
/* 4839 */       if (!isDetached())
/*      */       {
/* 4841 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4843 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxFieldSize() throws SQLException {
/*      */     try {
/* 4851 */       maybeDirtyTransaction();
/*      */       
/* 4853 */       return this.inner.getMaxFieldSize();
/*      */     }
/* 4855 */     catch (NullPointerException exc) {
/*      */       
/* 4857 */       if (isDetached())
/*      */       {
/* 4859 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4861 */       throw exc;
/*      */     }
/* 4863 */     catch (Exception exc) {
/*      */       
/* 4865 */       if (!isDetached())
/*      */       {
/* 4867 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4869 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMaxFieldSize(int a) throws SQLException {
/*      */     try {
/* 4877 */       maybeDirtyTransaction();
/*      */       
/* 4879 */       this.inner.setMaxFieldSize(a);
/*      */     }
/* 4881 */     catch (NullPointerException exc) {
/*      */       
/* 4883 */       if (isDetached())
/*      */       {
/* 4885 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4887 */       throw exc;
/*      */     }
/* 4889 */     catch (Exception exc) {
/*      */       
/* 4891 */       if (!isDetached())
/*      */       {
/* 4893 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4895 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxRows() throws SQLException {
/*      */     try {
/* 4903 */       maybeDirtyTransaction();
/*      */       
/* 4905 */       return this.inner.getMaxRows();
/*      */     }
/* 4907 */     catch (NullPointerException exc) {
/*      */       
/* 4909 */       if (isDetached())
/*      */       {
/* 4911 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4913 */       throw exc;
/*      */     }
/* 4915 */     catch (Exception exc) {
/*      */       
/* 4917 */       if (!isDetached())
/*      */       {
/* 4919 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4921 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMaxRows(int a) throws SQLException {
/*      */     try {
/* 4929 */       maybeDirtyTransaction();
/*      */       
/* 4931 */       this.inner.setMaxRows(a);
/*      */     }
/* 4933 */     catch (NullPointerException exc) {
/*      */       
/* 4935 */       if (isDetached())
/*      */       {
/* 4937 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4939 */       throw exc;
/*      */     }
/* 4941 */     catch (Exception exc) {
/*      */       
/* 4943 */       if (!isDetached())
/*      */       {
/* 4945 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4947 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setEscapeProcessing(boolean a) throws SQLException {
/*      */     try {
/* 4955 */       maybeDirtyTransaction();
/*      */       
/* 4957 */       this.inner.setEscapeProcessing(a);
/*      */     }
/* 4959 */     catch (NullPointerException exc) {
/*      */       
/* 4961 */       if (isDetached())
/*      */       {
/* 4963 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4965 */       throw exc;
/*      */     }
/* 4967 */     catch (Exception exc) {
/*      */       
/* 4969 */       if (!isDetached())
/*      */       {
/* 4971 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4973 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getQueryTimeout() throws SQLException {
/*      */     try {
/* 4981 */       maybeDirtyTransaction();
/*      */       
/* 4983 */       return this.inner.getQueryTimeout();
/*      */     }
/* 4985 */     catch (NullPointerException exc) {
/*      */       
/* 4987 */       if (isDetached())
/*      */       {
/* 4989 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 4991 */       throw exc;
/*      */     }
/* 4993 */     catch (Exception exc) {
/*      */       
/* 4995 */       if (!isDetached())
/*      */       {
/* 4997 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 4999 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setQueryTimeout(int a) throws SQLException {
/*      */     try {
/* 5007 */       maybeDirtyTransaction();
/*      */       
/* 5009 */       this.inner.setQueryTimeout(a);
/*      */     }
/* 5011 */     catch (NullPointerException exc) {
/*      */       
/* 5013 */       if (isDetached())
/*      */       {
/* 5015 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5017 */       throw exc;
/*      */     }
/* 5019 */     catch (Exception exc) {
/*      */       
/* 5021 */       if (!isDetached())
/*      */       {
/* 5023 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5025 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void cancel() throws SQLException {
/*      */     try {
/* 5033 */       maybeDirtyTransaction();
/*      */       
/* 5035 */       this.inner.cancel();
/*      */     }
/* 5037 */     catch (NullPointerException exc) {
/*      */       
/* 5039 */       if (isDetached())
/*      */       {
/* 5041 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5043 */       throw exc;
/*      */     }
/* 5045 */     catch (Exception exc) {
/*      */       
/* 5047 */       if (!isDetached())
/*      */       {
/* 5049 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5051 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCursorName(String a) throws SQLException {
/*      */     try {
/* 5059 */       maybeDirtyTransaction();
/*      */       
/* 5061 */       this.inner.setCursorName(a);
/*      */     }
/* 5063 */     catch (NullPointerException exc) {
/*      */       
/* 5065 */       if (isDetached())
/*      */       {
/* 5067 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5069 */       throw exc;
/*      */     }
/* 5071 */     catch (Exception exc) {
/*      */       
/* 5073 */       if (!isDetached())
/*      */       {
/* 5075 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5077 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getResultSet() throws SQLException {
/*      */     try {
/* 5085 */       maybeDirtyTransaction();
/*      */       
/* 5087 */       ResultSet innerResultSet = this.inner.getResultSet();
/* 5088 */       if (innerResultSet == null) return null; 
/* 5089 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 5090 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 5091 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 5092 */        return out;
/*      */     }
/* 5094 */     catch (NullPointerException exc) {
/*      */       
/* 5096 */       if (isDetached())
/*      */       {
/* 5098 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5100 */       throw exc;
/*      */     }
/* 5102 */     catch (Exception exc) {
/*      */       
/* 5104 */       if (!isDetached())
/*      */       {
/* 5106 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5108 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getUpdateCount() throws SQLException {
/*      */     try {
/* 5116 */       maybeDirtyTransaction();
/*      */       
/* 5118 */       return this.inner.getUpdateCount();
/*      */     }
/* 5120 */     catch (NullPointerException exc) {
/*      */       
/* 5122 */       if (isDetached())
/*      */       {
/* 5124 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5126 */       throw exc;
/*      */     }
/* 5128 */     catch (Exception exc) {
/*      */       
/* 5130 */       if (!isDetached())
/*      */       {
/* 5132 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5134 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getMoreResults(int a) throws SQLException {
/*      */     try {
/* 5142 */       maybeDirtyTransaction();
/*      */       
/* 5144 */       return this.inner.getMoreResults(a);
/*      */     }
/* 5146 */     catch (NullPointerException exc) {
/*      */       
/* 5148 */       if (isDetached())
/*      */       {
/* 5150 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5152 */       throw exc;
/*      */     }
/* 5154 */     catch (Exception exc) {
/*      */       
/* 5156 */       if (!isDetached())
/*      */       {
/* 5158 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5160 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getMoreResults() throws SQLException {
/*      */     try {
/* 5168 */       maybeDirtyTransaction();
/*      */       
/* 5170 */       return this.inner.getMoreResults();
/*      */     }
/* 5172 */     catch (NullPointerException exc) {
/*      */       
/* 5174 */       if (isDetached())
/*      */       {
/* 5176 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5178 */       throw exc;
/*      */     }
/* 5180 */     catch (Exception exc) {
/*      */       
/* 5182 */       if (!isDetached())
/*      */       {
/* 5184 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5186 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchDirection(int a) throws SQLException {
/*      */     try {
/* 5194 */       maybeDirtyTransaction();
/*      */       
/* 5196 */       this.inner.setFetchDirection(a);
/*      */     }
/* 5198 */     catch (NullPointerException exc) {
/*      */       
/* 5200 */       if (isDetached())
/*      */       {
/* 5202 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5204 */       throw exc;
/*      */     }
/* 5206 */     catch (Exception exc) {
/*      */       
/* 5208 */       if (!isDetached())
/*      */       {
/* 5210 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5212 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchDirection() throws SQLException {
/*      */     try {
/* 5220 */       maybeDirtyTransaction();
/*      */       
/* 5222 */       return this.inner.getFetchDirection();
/*      */     }
/* 5224 */     catch (NullPointerException exc) {
/*      */       
/* 5226 */       if (isDetached())
/*      */       {
/* 5228 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5230 */       throw exc;
/*      */     }
/* 5232 */     catch (Exception exc) {
/*      */       
/* 5234 */       if (!isDetached())
/*      */       {
/* 5236 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5238 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchSize(int a) throws SQLException {
/*      */     try {
/* 5246 */       maybeDirtyTransaction();
/*      */       
/* 5248 */       this.inner.setFetchSize(a);
/*      */     }
/* 5250 */     catch (NullPointerException exc) {
/*      */       
/* 5252 */       if (isDetached())
/*      */       {
/* 5254 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5256 */       throw exc;
/*      */     }
/* 5258 */     catch (Exception exc) {
/*      */       
/* 5260 */       if (!isDetached())
/*      */       {
/* 5262 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5264 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchSize() throws SQLException {
/*      */     try {
/* 5272 */       maybeDirtyTransaction();
/*      */       
/* 5274 */       return this.inner.getFetchSize();
/*      */     }
/* 5276 */     catch (NullPointerException exc) {
/*      */       
/* 5278 */       if (isDetached())
/*      */       {
/* 5280 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5282 */       throw exc;
/*      */     }
/* 5284 */     catch (Exception exc) {
/*      */       
/* 5286 */       if (!isDetached())
/*      */       {
/* 5288 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5290 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetConcurrency() throws SQLException {
/*      */     try {
/* 5298 */       maybeDirtyTransaction();
/*      */       
/* 5300 */       return this.inner.getResultSetConcurrency();
/*      */     }
/* 5302 */     catch (NullPointerException exc) {
/*      */       
/* 5304 */       if (isDetached())
/*      */       {
/* 5306 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5308 */       throw exc;
/*      */     }
/* 5310 */     catch (Exception exc) {
/*      */       
/* 5312 */       if (!isDetached())
/*      */       {
/* 5314 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5316 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetType() throws SQLException {
/*      */     try {
/* 5324 */       maybeDirtyTransaction();
/*      */       
/* 5326 */       return this.inner.getResultSetType();
/*      */     }
/* 5328 */     catch (NullPointerException exc) {
/*      */       
/* 5330 */       if (isDetached())
/*      */       {
/* 5332 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5334 */       throw exc;
/*      */     }
/* 5336 */     catch (Exception exc) {
/*      */       
/* 5338 */       if (!isDetached())
/*      */       {
/* 5340 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5342 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addBatch(String a) throws SQLException {
/*      */     try {
/* 5350 */       maybeDirtyTransaction();
/*      */       
/* 5352 */       this.inner.addBatch(a);
/*      */     }
/* 5354 */     catch (NullPointerException exc) {
/*      */       
/* 5356 */       if (isDetached())
/*      */       {
/* 5358 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5360 */       throw exc;
/*      */     }
/* 5362 */     catch (Exception exc) {
/*      */       
/* 5364 */       if (!isDetached())
/*      */       {
/* 5366 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5368 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearBatch() throws SQLException {
/*      */     try {
/* 5376 */       maybeDirtyTransaction();
/*      */       
/* 5378 */       this.inner.clearBatch();
/*      */     }
/* 5380 */     catch (NullPointerException exc) {
/*      */       
/* 5382 */       if (isDetached())
/*      */       {
/* 5384 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5386 */       throw exc;
/*      */     }
/* 5388 */     catch (Exception exc) {
/*      */       
/* 5390 */       if (!isDetached())
/*      */       {
/* 5392 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5394 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] executeBatch() throws SQLException {
/*      */     try {
/* 5402 */       maybeDirtyTransaction();
/*      */       
/* 5404 */       return this.inner.executeBatch();
/*      */     }
/* 5406 */     catch (NullPointerException exc) {
/*      */       
/* 5408 */       if (isDetached())
/*      */       {
/* 5410 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5412 */       throw exc;
/*      */     }
/* 5414 */     catch (Exception exc) {
/*      */       
/* 5416 */       if (!isDetached())
/*      */       {
/* 5418 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5420 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getGeneratedKeys() throws SQLException {
/*      */     try {
/* 5428 */       maybeDirtyTransaction();
/*      */       
/* 5430 */       ResultSet innerResultSet = this.inner.getGeneratedKeys();
/* 5431 */       if (innerResultSet == null) return null; 
/* 5432 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 5433 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 5434 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 5435 */        return out;
/*      */     }
/* 5437 */     catch (NullPointerException exc) {
/*      */       
/* 5439 */       if (isDetached())
/*      */       {
/* 5441 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5443 */       throw exc;
/*      */     }
/* 5445 */     catch (Exception exc) {
/*      */       
/* 5447 */       if (!isDetached())
/*      */       {
/* 5449 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5451 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetHoldability() throws SQLException {
/*      */     try {
/* 5459 */       maybeDirtyTransaction();
/*      */       
/* 5461 */       return this.inner.getResultSetHoldability();
/*      */     }
/* 5463 */     catch (NullPointerException exc) {
/*      */       
/* 5465 */       if (isDetached())
/*      */       {
/* 5467 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5469 */       throw exc;
/*      */     }
/* 5471 */     catch (Exception exc) {
/*      */       
/* 5473 */       if (!isDetached())
/*      */       {
/* 5475 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5477 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPoolable(boolean a) throws SQLException {
/*      */     try {
/* 5485 */       maybeDirtyTransaction();
/*      */       
/* 5487 */       this.inner.setPoolable(a);
/*      */     }
/* 5489 */     catch (NullPointerException exc) {
/*      */       
/* 5491 */       if (isDetached())
/*      */       {
/* 5493 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5495 */       throw exc;
/*      */     }
/* 5497 */     catch (Exception exc) {
/*      */       
/* 5499 */       if (!isDetached())
/*      */       {
/* 5501 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5503 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPoolable() throws SQLException {
/*      */     try {
/* 5511 */       maybeDirtyTransaction();
/*      */       
/* 5513 */       return this.inner.isPoolable();
/*      */     }
/* 5515 */     catch (NullPointerException exc) {
/*      */       
/* 5517 */       if (isDetached())
/*      */       {
/* 5519 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5521 */       throw exc;
/*      */     }
/* 5523 */     catch (Exception exc) {
/*      */       
/* 5525 */       if (!isDetached())
/*      */       {
/* 5527 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5529 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void closeOnCompletion() throws SQLException {
/*      */     try {
/* 5537 */       maybeDirtyTransaction();
/*      */       
/* 5539 */       this.inner.closeOnCompletion();
/*      */     }
/* 5541 */     catch (NullPointerException exc) {
/*      */       
/* 5543 */       if (isDetached())
/*      */       {
/* 5545 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5547 */       throw exc;
/*      */     }
/* 5549 */     catch (Exception exc) {
/*      */       
/* 5551 */       if (!isDetached())
/*      */       {
/* 5553 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5555 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCloseOnCompletion() throws SQLException {
/*      */     try {
/* 5563 */       maybeDirtyTransaction();
/*      */       
/* 5565 */       return this.inner.isCloseOnCompletion();
/*      */     }
/* 5567 */     catch (NullPointerException exc) {
/*      */       
/* 5569 */       if (isDetached())
/*      */       {
/* 5571 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 5573 */       throw exc;
/*      */     }
/* 5575 */     catch (Exception exc) {
/*      */       
/* 5577 */       if (!isDetached())
/*      */       {
/* 5579 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 5581 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object unwrap(Class a) throws SQLException {
/* 5587 */     if (isWrapperFor(a)) return this.inner; 
/* 5588 */     throw new SQLException(this + " is not a wrapper for " + a.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWrapperFor(Class<CallableStatement> a) throws SQLException {
/* 5593 */     return (CallableStatement.class == a || a.isAssignableFrom(this.inner.getClass()));
/*      */   }
/*      */   
/* 5596 */   private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyCallableStatement");
/*      */   
/*      */   volatile NewPooledConnection parentPooledConnection;
/*      */   
/* 5600 */   ConnectionEventListener cel = new ConnectionEventListener()
/*      */     {
/*      */       public void connectionErrorOccurred(ConnectionEvent evt) {}
/*      */ 
/*      */       
/*      */       public void connectionClosed(ConnectionEvent evt) {
/* 5606 */         NewProxyCallableStatement.this.detach();
/*      */       }
/*      */     };
/*      */   boolean is_cached;
/*      */   void attach(NewPooledConnection parentPooledConnection) {
/* 5611 */     this.parentPooledConnection = parentPooledConnection;
/* 5612 */     parentPooledConnection.addConnectionEventListener(this.cel);
/*      */   }
/*      */   NewProxyConnection creatorProxy;
/*      */   
/*      */   private void detach() {
/* 5617 */     this.parentPooledConnection.removeConnectionEventListener(this.cel);
/* 5618 */     this.parentPooledConnection = null;
/*      */   }
/*      */ 
/*      */   
/*      */   NewProxyCallableStatement(CallableStatement inner, NewPooledConnection parentPooledConnection) {
/* 5623 */     this(inner);
/* 5624 */     attach(parentPooledConnection);
/*      */   }
/*      */   
/*      */   boolean isDetached() {
/* 5628 */     return (this.parentPooledConnection == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 5637 */   HashSet myProxyResultSets = new HashSet();
/*      */ 
/*      */   
/*      */   public void detachProxyResultSet(ResultSet prs) {
/* 5641 */     synchronized (this.myProxyResultSets) { this.myProxyResultSets.remove(prs); }
/*      */   
/*      */   }
/*      */   
/*      */   NewProxyCallableStatement(CallableStatement inner, NewPooledConnection parentPooledConnection, boolean cached, NewProxyConnection cProxy) {
/* 5646 */     this(inner, parentPooledConnection);
/* 5647 */     this.is_cached = cached;
/* 5648 */     this.creatorProxy = cProxy;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object rawStatementOperation(Method m, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException, SQLException {
/* 5653 */     maybeDirtyTransaction();
/*      */     
/* 5655 */     if (target == C3P0ProxyStatement.RAW_STATEMENT) target = this.inner; 
/* 5656 */     for (int i = 0, len = args.length; i < len; i++) {
/* 5657 */       if (args[i] == C3P0ProxyStatement.RAW_STATEMENT) args[i] = this.inner; 
/* 5658 */     }  Object out = m.invoke(target, args);
/* 5659 */     if (out instanceof ResultSet) {
/*      */       
/* 5661 */       ResultSet innerResultSet = (ResultSet)out;
/* 5662 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 5663 */       out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     } 
/*      */     
/* 5666 */     return out;
/*      */   }
/*      */   
/*      */   void maybeDirtyTransaction() {
/* 5670 */     if (this.creatorProxy != null) this.creatorProxy.maybeDirtyTransaction(); 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewProxyCallableStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */