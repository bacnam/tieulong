/*      */ package com.mchange.v2.c3p0.impl;
/*      */ 
/*      */ import com.mchange.v2.c3p0.C3P0ProxyStatement;
/*      */ import com.mchange.v2.log.MLevel;
/*      */ import com.mchange.v2.log.MLog;
/*      */ import com.mchange.v2.log.MLogger;
/*      */ import com.mchange.v2.sql.SqlUtils;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.sql.Connection;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
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
/*      */ public final class NewProxyStatement
/*      */   implements Statement, C3P0ProxyStatement, ProxyResultSetDetachable
/*      */ {
/*      */   protected Statement inner;
/*      */   
/*      */   private void __setInner(Statement inner) {
/*   37 */     this.inner = inner;
/*      */   }
/*      */   
/*      */   NewProxyStatement(Statement inner) {
/*   41 */     __setInner(inner);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void close() throws SQLException {
/*      */     try {
/*   47 */       maybeDirtyTransaction();
/*      */       
/*   49 */       if (!isDetached())
/*      */       {
/*   51 */         synchronized (this.myProxyResultSets) {
/*      */           
/*   53 */           for (Iterator<ResultSet> ii = this.myProxyResultSets.iterator(); ii.hasNext(); ) {
/*      */             
/*   55 */             ResultSet closeMe = ii.next();
/*   56 */             ii.remove();
/*      */             try {
/*   58 */               closeMe.close();
/*   59 */             } catch (SQLException e) {
/*      */               
/*   61 */               if (logger.isLoggable(MLevel.WARNING))
/*   62 */                 logger.log(MLevel.WARNING, "Exception on close of apparently orphaned ResultSet.", e); 
/*      */             } 
/*   64 */             if (logger.isLoggable(MLevel.FINE)) {
/*   65 */               logger.log(MLevel.FINE, this + " closed orphaned ResultSet: " + closeMe);
/*      */             }
/*      */           } 
/*      */         } 
/*   69 */         if (this.is_cached) {
/*   70 */           this.parentPooledConnection.checkinStatement(this.inner);
/*      */         } else {
/*      */           
/*   73 */           this.parentPooledConnection.markInactiveUncachedStatement(this.inner); try {
/*   74 */             this.inner.close();
/*   75 */           } catch (Exception e) {
/*      */             
/*   77 */             if (logger.isLoggable(MLevel.WARNING))
/*   78 */               logger.log(MLevel.WARNING, "Exception on close of inner statement.", e); 
/*   79 */             SQLException sqle = SqlUtils.toSQLException(e);
/*   80 */             throw sqle;
/*      */           } 
/*      */         } 
/*      */         
/*   84 */         detach();
/*   85 */         this.inner = null;
/*   86 */         this.creatorProxy = null;
/*      */       }
/*      */     
/*   89 */     } catch (NullPointerException exc) {
/*      */       
/*   91 */       if (isDetached()) {
/*      */         
/*   93 */         if (logger.isLoggable(MLevel.FINE))
/*      */         {
/*   95 */           logger.log(MLevel.FINE, this + ": close() called more than once.");
/*      */         }
/*      */       } else {
/*   98 */         throw exc;
/*      */       } 
/*  100 */     } catch (Exception exc) {
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
/*      */   public final Connection getConnection() throws SQLException {
/*      */     try {
/*  114 */       maybeDirtyTransaction();
/*      */       
/*  116 */       if (!isDetached()) {
/*  117 */         return this.creatorProxy;
/*      */       }
/*  119 */       throw new SQLException("You cannot operate on a closed Statement!");
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
/*      */   public final SQLWarning getWarnings() throws SQLException {
/*      */     try {
/*  143 */       maybeDirtyTransaction();
/*      */       
/*  145 */       return this.inner.getWarnings();
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
/*      */   public final void clearWarnings() throws SQLException {
/*      */     try {
/*  169 */       maybeDirtyTransaction();
/*      */       
/*  171 */       this.inner.clearWarnings();
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
/*      */   public final boolean isClosed() throws SQLException {
/*      */     try {
/*  195 */       maybeDirtyTransaction();
/*      */       
/*  197 */       return isDetached();
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
/*      */   public final boolean execute(String a) throws SQLException {
/*      */     try {
/*  221 */       maybeDirtyTransaction();
/*      */       
/*  223 */       return this.inner.execute(a);
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
/*      */   public final boolean execute(String a, String[] b) throws SQLException {
/*      */     try {
/*  247 */       maybeDirtyTransaction();
/*      */       
/*  249 */       return this.inner.execute(a, b);
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
/*      */   public final boolean execute(String a, int[] b) throws SQLException {
/*      */     try {
/*  273 */       maybeDirtyTransaction();
/*      */       
/*  275 */       return this.inner.execute(a, b);
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
/*      */   public final boolean execute(String a, int b) throws SQLException {
/*      */     try {
/*  299 */       maybeDirtyTransaction();
/*      */       
/*  301 */       return this.inner.execute(a, b);
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
/*      */   public final ResultSet executeQuery(String a) throws SQLException {
/*      */     try {
/*  325 */       maybeDirtyTransaction();
/*      */       
/*  327 */       ResultSet innerResultSet = this.inner.executeQuery(a);
/*  328 */       if (innerResultSet == null) return null; 
/*  329 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/*  330 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*  331 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/*  332 */        return out;
/*      */     }
/*  334 */     catch (NullPointerException exc) {
/*      */       
/*  336 */       if (isDetached())
/*      */       {
/*  338 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final int executeUpdate(String a, int b) throws SQLException {
/*      */     try {
/*  356 */       maybeDirtyTransaction();
/*      */       
/*  358 */       return this.inner.executeUpdate(a, b);
/*      */     }
/*  360 */     catch (NullPointerException exc) {
/*      */       
/*  362 */       if (isDetached())
/*      */       {
/*  364 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  366 */       throw exc;
/*      */     }
/*  368 */     catch (Exception exc) {
/*      */       
/*  370 */       if (!isDetached())
/*      */       {
/*  372 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  374 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, int[] b) throws SQLException {
/*      */     try {
/*  382 */       maybeDirtyTransaction();
/*      */       
/*  384 */       return this.inner.executeUpdate(a, b);
/*      */     }
/*  386 */     catch (NullPointerException exc) {
/*      */       
/*  388 */       if (isDetached())
/*      */       {
/*  390 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  392 */       throw exc;
/*      */     }
/*  394 */     catch (Exception exc) {
/*      */       
/*  396 */       if (!isDetached())
/*      */       {
/*  398 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  400 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a, String[] b) throws SQLException {
/*      */     try {
/*  408 */       maybeDirtyTransaction();
/*      */       
/*  410 */       return this.inner.executeUpdate(a, b);
/*      */     }
/*  412 */     catch (NullPointerException exc) {
/*      */       
/*  414 */       if (isDetached())
/*      */       {
/*  416 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  418 */       throw exc;
/*      */     }
/*  420 */     catch (Exception exc) {
/*      */       
/*  422 */       if (!isDetached())
/*      */       {
/*  424 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  426 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int executeUpdate(String a) throws SQLException {
/*      */     try {
/*  434 */       maybeDirtyTransaction();
/*      */       
/*  436 */       return this.inner.executeUpdate(a);
/*      */     }
/*  438 */     catch (NullPointerException exc) {
/*      */       
/*  440 */       if (isDetached())
/*      */       {
/*  442 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  444 */       throw exc;
/*      */     }
/*  446 */     catch (Exception exc) {
/*      */       
/*  448 */       if (!isDetached())
/*      */       {
/*  450 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  452 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxFieldSize() throws SQLException {
/*      */     try {
/*  460 */       maybeDirtyTransaction();
/*      */       
/*  462 */       return this.inner.getMaxFieldSize();
/*      */     }
/*  464 */     catch (NullPointerException exc) {
/*      */       
/*  466 */       if (isDetached())
/*      */       {
/*  468 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
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
/*      */   public final void setMaxFieldSize(int a) throws SQLException {
/*      */     try {
/*  486 */       maybeDirtyTransaction();
/*      */       
/*  488 */       this.inner.setMaxFieldSize(a);
/*      */     }
/*  490 */     catch (NullPointerException exc) {
/*      */       
/*  492 */       if (isDetached())
/*      */       {
/*  494 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  496 */       throw exc;
/*      */     }
/*  498 */     catch (Exception exc) {
/*      */       
/*  500 */       if (!isDetached())
/*      */       {
/*  502 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  504 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getMaxRows() throws SQLException {
/*      */     try {
/*  512 */       maybeDirtyTransaction();
/*      */       
/*  514 */       return this.inner.getMaxRows();
/*      */     }
/*  516 */     catch (NullPointerException exc) {
/*      */       
/*  518 */       if (isDetached())
/*      */       {
/*  520 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  522 */       throw exc;
/*      */     }
/*  524 */     catch (Exception exc) {
/*      */       
/*  526 */       if (!isDetached())
/*      */       {
/*  528 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  530 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setMaxRows(int a) throws SQLException {
/*      */     try {
/*  538 */       maybeDirtyTransaction();
/*      */       
/*  540 */       this.inner.setMaxRows(a);
/*      */     }
/*  542 */     catch (NullPointerException exc) {
/*      */       
/*  544 */       if (isDetached())
/*      */       {
/*  546 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  548 */       throw exc;
/*      */     }
/*  550 */     catch (Exception exc) {
/*      */       
/*  552 */       if (!isDetached())
/*      */       {
/*  554 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  556 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setEscapeProcessing(boolean a) throws SQLException {
/*      */     try {
/*  564 */       maybeDirtyTransaction();
/*      */       
/*  566 */       this.inner.setEscapeProcessing(a);
/*      */     }
/*  568 */     catch (NullPointerException exc) {
/*      */       
/*  570 */       if (isDetached())
/*      */       {
/*  572 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  574 */       throw exc;
/*      */     }
/*  576 */     catch (Exception exc) {
/*      */       
/*  578 */       if (!isDetached())
/*      */       {
/*  580 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  582 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getQueryTimeout() throws SQLException {
/*      */     try {
/*  590 */       maybeDirtyTransaction();
/*      */       
/*  592 */       return this.inner.getQueryTimeout();
/*      */     }
/*  594 */     catch (NullPointerException exc) {
/*      */       
/*  596 */       if (isDetached())
/*      */       {
/*  598 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  600 */       throw exc;
/*      */     }
/*  602 */     catch (Exception exc) {
/*      */       
/*  604 */       if (!isDetached())
/*      */       {
/*  606 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  608 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setQueryTimeout(int a) throws SQLException {
/*      */     try {
/*  616 */       maybeDirtyTransaction();
/*      */       
/*  618 */       this.inner.setQueryTimeout(a);
/*      */     }
/*  620 */     catch (NullPointerException exc) {
/*      */       
/*  622 */       if (isDetached())
/*      */       {
/*  624 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  626 */       throw exc;
/*      */     }
/*  628 */     catch (Exception exc) {
/*      */       
/*  630 */       if (!isDetached())
/*      */       {
/*  632 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  634 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void cancel() throws SQLException {
/*      */     try {
/*  642 */       maybeDirtyTransaction();
/*      */       
/*  644 */       this.inner.cancel();
/*      */     }
/*  646 */     catch (NullPointerException exc) {
/*      */       
/*  648 */       if (isDetached())
/*      */       {
/*  650 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  652 */       throw exc;
/*      */     }
/*  654 */     catch (Exception exc) {
/*      */       
/*  656 */       if (!isDetached())
/*      */       {
/*  658 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  660 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setCursorName(String a) throws SQLException {
/*      */     try {
/*  668 */       maybeDirtyTransaction();
/*      */       
/*  670 */       this.inner.setCursorName(a);
/*      */     }
/*  672 */     catch (NullPointerException exc) {
/*      */       
/*  674 */       if (isDetached())
/*      */       {
/*  676 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  678 */       throw exc;
/*      */     }
/*  680 */     catch (Exception exc) {
/*      */       
/*  682 */       if (!isDetached())
/*      */       {
/*  684 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  686 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getResultSet() throws SQLException {
/*      */     try {
/*  694 */       maybeDirtyTransaction();
/*      */       
/*  696 */       ResultSet innerResultSet = this.inner.getResultSet();
/*  697 */       if (innerResultSet == null) return null; 
/*  698 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/*  699 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*  700 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/*  701 */        return out;
/*      */     }
/*  703 */     catch (NullPointerException exc) {
/*      */       
/*  705 */       if (isDetached())
/*      */       {
/*  707 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  709 */       throw exc;
/*      */     }
/*  711 */     catch (Exception exc) {
/*      */       
/*  713 */       if (!isDetached())
/*      */       {
/*  715 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  717 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getUpdateCount() throws SQLException {
/*      */     try {
/*  725 */       maybeDirtyTransaction();
/*      */       
/*  727 */       return this.inner.getUpdateCount();
/*      */     }
/*  729 */     catch (NullPointerException exc) {
/*      */       
/*  731 */       if (isDetached())
/*      */       {
/*  733 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  735 */       throw exc;
/*      */     }
/*  737 */     catch (Exception exc) {
/*      */       
/*  739 */       if (!isDetached())
/*      */       {
/*  741 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  743 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getMoreResults(int a) throws SQLException {
/*      */     try {
/*  751 */       maybeDirtyTransaction();
/*      */       
/*  753 */       return this.inner.getMoreResults(a);
/*      */     }
/*  755 */     catch (NullPointerException exc) {
/*      */       
/*  757 */       if (isDetached())
/*      */       {
/*  759 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  761 */       throw exc;
/*      */     }
/*  763 */     catch (Exception exc) {
/*      */       
/*  765 */       if (!isDetached())
/*      */       {
/*  767 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  769 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getMoreResults() throws SQLException {
/*      */     try {
/*  777 */       maybeDirtyTransaction();
/*      */       
/*  779 */       return this.inner.getMoreResults();
/*      */     }
/*  781 */     catch (NullPointerException exc) {
/*      */       
/*  783 */       if (isDetached())
/*      */       {
/*  785 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  787 */       throw exc;
/*      */     }
/*  789 */     catch (Exception exc) {
/*      */       
/*  791 */       if (!isDetached())
/*      */       {
/*  793 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  795 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchDirection(int a) throws SQLException {
/*      */     try {
/*  803 */       maybeDirtyTransaction();
/*      */       
/*  805 */       this.inner.setFetchDirection(a);
/*      */     }
/*  807 */     catch (NullPointerException exc) {
/*      */       
/*  809 */       if (isDetached())
/*      */       {
/*  811 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  813 */       throw exc;
/*      */     }
/*  815 */     catch (Exception exc) {
/*      */       
/*  817 */       if (!isDetached())
/*      */       {
/*  819 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  821 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchDirection() throws SQLException {
/*      */     try {
/*  829 */       maybeDirtyTransaction();
/*      */       
/*  831 */       return this.inner.getFetchDirection();
/*      */     }
/*  833 */     catch (NullPointerException exc) {
/*      */       
/*  835 */       if (isDetached())
/*      */       {
/*  837 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  839 */       throw exc;
/*      */     }
/*  841 */     catch (Exception exc) {
/*      */       
/*  843 */       if (!isDetached())
/*      */       {
/*  845 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  847 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setFetchSize(int a) throws SQLException {
/*      */     try {
/*  855 */       maybeDirtyTransaction();
/*      */       
/*  857 */       this.inner.setFetchSize(a);
/*      */     }
/*  859 */     catch (NullPointerException exc) {
/*      */       
/*  861 */       if (isDetached())
/*      */       {
/*  863 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  865 */       throw exc;
/*      */     }
/*  867 */     catch (Exception exc) {
/*      */       
/*  869 */       if (!isDetached())
/*      */       {
/*  871 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  873 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getFetchSize() throws SQLException {
/*      */     try {
/*  881 */       maybeDirtyTransaction();
/*      */       
/*  883 */       return this.inner.getFetchSize();
/*      */     }
/*  885 */     catch (NullPointerException exc) {
/*      */       
/*  887 */       if (isDetached())
/*      */       {
/*  889 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  891 */       throw exc;
/*      */     }
/*  893 */     catch (Exception exc) {
/*      */       
/*  895 */       if (!isDetached())
/*      */       {
/*  897 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  899 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetConcurrency() throws SQLException {
/*      */     try {
/*  907 */       maybeDirtyTransaction();
/*      */       
/*  909 */       return this.inner.getResultSetConcurrency();
/*      */     }
/*  911 */     catch (NullPointerException exc) {
/*      */       
/*  913 */       if (isDetached())
/*      */       {
/*  915 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  917 */       throw exc;
/*      */     }
/*  919 */     catch (Exception exc) {
/*      */       
/*  921 */       if (!isDetached())
/*      */       {
/*  923 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  925 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetType() throws SQLException {
/*      */     try {
/*  933 */       maybeDirtyTransaction();
/*      */       
/*  935 */       return this.inner.getResultSetType();
/*      */     }
/*  937 */     catch (NullPointerException exc) {
/*      */       
/*  939 */       if (isDetached())
/*      */       {
/*  941 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  943 */       throw exc;
/*      */     }
/*  945 */     catch (Exception exc) {
/*      */       
/*  947 */       if (!isDetached())
/*      */       {
/*  949 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  951 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addBatch(String a) throws SQLException {
/*      */     try {
/*  959 */       maybeDirtyTransaction();
/*      */       
/*  961 */       this.inner.addBatch(a);
/*      */     }
/*  963 */     catch (NullPointerException exc) {
/*      */       
/*  965 */       if (isDetached())
/*      */       {
/*  967 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  969 */       throw exc;
/*      */     }
/*  971 */     catch (Exception exc) {
/*      */       
/*  973 */       if (!isDetached())
/*      */       {
/*  975 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/*  977 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void clearBatch() throws SQLException {
/*      */     try {
/*  985 */       maybeDirtyTransaction();
/*      */       
/*  987 */       this.inner.clearBatch();
/*      */     }
/*  989 */     catch (NullPointerException exc) {
/*      */       
/*  991 */       if (isDetached())
/*      */       {
/*  993 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/*  995 */       throw exc;
/*      */     }
/*  997 */     catch (Exception exc) {
/*      */       
/*  999 */       if (!isDetached())
/*      */       {
/* 1001 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1003 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int[] executeBatch() throws SQLException {
/*      */     try {
/* 1011 */       maybeDirtyTransaction();
/*      */       
/* 1013 */       return this.inner.executeBatch();
/*      */     }
/* 1015 */     catch (NullPointerException exc) {
/*      */       
/* 1017 */       if (isDetached())
/*      */       {
/* 1019 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1021 */       throw exc;
/*      */     }
/* 1023 */     catch (Exception exc) {
/*      */       
/* 1025 */       if (!isDetached())
/*      */       {
/* 1027 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1029 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final ResultSet getGeneratedKeys() throws SQLException {
/*      */     try {
/* 1037 */       maybeDirtyTransaction();
/*      */       
/* 1039 */       ResultSet innerResultSet = this.inner.getGeneratedKeys();
/* 1040 */       if (innerResultSet == null) return null; 
/* 1041 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 1042 */       NewProxyResultSet out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/* 1043 */       synchronized (this.myProxyResultSets) { this.myProxyResultSets.add(out); }
/* 1044 */        return out;
/*      */     }
/* 1046 */     catch (NullPointerException exc) {
/*      */       
/* 1048 */       if (isDetached())
/*      */       {
/* 1050 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1052 */       throw exc;
/*      */     }
/* 1054 */     catch (Exception exc) {
/*      */       
/* 1056 */       if (!isDetached())
/*      */       {
/* 1058 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1060 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getResultSetHoldability() throws SQLException {
/*      */     try {
/* 1068 */       maybeDirtyTransaction();
/*      */       
/* 1070 */       return this.inner.getResultSetHoldability();
/*      */     }
/* 1072 */     catch (NullPointerException exc) {
/*      */       
/* 1074 */       if (isDetached())
/*      */       {
/* 1076 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1078 */       throw exc;
/*      */     }
/* 1080 */     catch (Exception exc) {
/*      */       
/* 1082 */       if (!isDetached())
/*      */       {
/* 1084 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1086 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void setPoolable(boolean a) throws SQLException {
/*      */     try {
/* 1094 */       maybeDirtyTransaction();
/*      */       
/* 1096 */       this.inner.setPoolable(a);
/*      */     }
/* 1098 */     catch (NullPointerException exc) {
/*      */       
/* 1100 */       if (isDetached())
/*      */       {
/* 1102 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1104 */       throw exc;
/*      */     }
/* 1106 */     catch (Exception exc) {
/*      */       
/* 1108 */       if (!isDetached())
/*      */       {
/* 1110 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1112 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isPoolable() throws SQLException {
/*      */     try {
/* 1120 */       maybeDirtyTransaction();
/*      */       
/* 1122 */       return this.inner.isPoolable();
/*      */     }
/* 1124 */     catch (NullPointerException exc) {
/*      */       
/* 1126 */       if (isDetached())
/*      */       {
/* 1128 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1130 */       throw exc;
/*      */     }
/* 1132 */     catch (Exception exc) {
/*      */       
/* 1134 */       if (!isDetached())
/*      */       {
/* 1136 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1138 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void closeOnCompletion() throws SQLException {
/*      */     try {
/* 1146 */       maybeDirtyTransaction();
/*      */       
/* 1148 */       this.inner.closeOnCompletion();
/*      */     }
/* 1150 */     catch (NullPointerException exc) {
/*      */       
/* 1152 */       if (isDetached())
/*      */       {
/* 1154 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1156 */       throw exc;
/*      */     }
/* 1158 */     catch (Exception exc) {
/*      */       
/* 1160 */       if (!isDetached())
/*      */       {
/* 1162 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1164 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isCloseOnCompletion() throws SQLException {
/*      */     try {
/* 1172 */       maybeDirtyTransaction();
/*      */       
/* 1174 */       return this.inner.isCloseOnCompletion();
/*      */     }
/* 1176 */     catch (NullPointerException exc) {
/*      */       
/* 1178 */       if (isDetached())
/*      */       {
/* 1180 */         throw SqlUtils.toSQLException("You can't operate on a closed Statement!!!", exc);
/*      */       }
/* 1182 */       throw exc;
/*      */     }
/* 1184 */     catch (Exception exc) {
/*      */       
/* 1186 */       if (!isDetached())
/*      */       {
/* 1188 */         throw this.parentPooledConnection.handleThrowable(exc);
/*      */       }
/* 1190 */       throw SqlUtils.toSQLException(exc);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final Object unwrap(Class a) throws SQLException {
/* 1196 */     if (isWrapperFor(a)) return this.inner; 
/* 1197 */     throw new SQLException(this + " is not a wrapper for " + a.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean isWrapperFor(Class<Statement> a) throws SQLException {
/* 1202 */     return (Statement.class == a || a.isAssignableFrom(this.inner.getClass()));
/*      */   }
/*      */   
/* 1205 */   private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyStatement");
/*      */   
/*      */   volatile NewPooledConnection parentPooledConnection;
/*      */   
/* 1209 */   ConnectionEventListener cel = new ConnectionEventListener()
/*      */     {
/*      */       public void connectionErrorOccurred(ConnectionEvent evt) {}
/*      */ 
/*      */       
/*      */       public void connectionClosed(ConnectionEvent evt) {
/* 1215 */         NewProxyStatement.this.detach();
/*      */       }
/*      */     };
/*      */   boolean is_cached;
/*      */   void attach(NewPooledConnection parentPooledConnection) {
/* 1220 */     this.parentPooledConnection = parentPooledConnection;
/* 1221 */     parentPooledConnection.addConnectionEventListener(this.cel);
/*      */   }
/*      */   NewProxyConnection creatorProxy;
/*      */   
/*      */   private void detach() {
/* 1226 */     this.parentPooledConnection.removeConnectionEventListener(this.cel);
/* 1227 */     this.parentPooledConnection = null;
/*      */   }
/*      */ 
/*      */   
/*      */   NewProxyStatement(Statement inner, NewPooledConnection parentPooledConnection) {
/* 1232 */     this(inner);
/* 1233 */     attach(parentPooledConnection);
/*      */   }
/*      */   
/*      */   boolean isDetached() {
/* 1237 */     return (this.parentPooledConnection == null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1246 */   HashSet myProxyResultSets = new HashSet();
/*      */ 
/*      */   
/*      */   public void detachProxyResultSet(ResultSet prs) {
/* 1250 */     synchronized (this.myProxyResultSets) { this.myProxyResultSets.remove(prs); }
/*      */   
/*      */   }
/*      */   
/*      */   NewProxyStatement(Statement inner, NewPooledConnection parentPooledConnection, boolean cached, NewProxyConnection cProxy) {
/* 1255 */     this(inner, parentPooledConnection);
/* 1256 */     this.is_cached = cached;
/* 1257 */     this.creatorProxy = cProxy;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object rawStatementOperation(Method m, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException, SQLException {
/* 1262 */     maybeDirtyTransaction();
/*      */     
/* 1264 */     if (target == C3P0ProxyStatement.RAW_STATEMENT) target = this.inner; 
/* 1265 */     for (int i = 0, len = args.length; i < len; i++) {
/* 1266 */       if (args[i] == C3P0ProxyStatement.RAW_STATEMENT) args[i] = this.inner; 
/* 1267 */     }  Object out = m.invoke(target, args);
/* 1268 */     if (out instanceof ResultSet) {
/*      */       
/* 1270 */       ResultSet innerResultSet = (ResultSet)out;
/* 1271 */       this.parentPooledConnection.markActiveResultSetForStatement(this.inner, innerResultSet);
/* 1272 */       out = new NewProxyResultSet(innerResultSet, this.parentPooledConnection, this.inner, this);
/*      */     } 
/*      */     
/* 1275 */     return out;
/*      */   }
/*      */   
/*      */   void maybeDirtyTransaction() {
/* 1279 */     if (this.creatorProxy != null) this.creatorProxy.maybeDirtyTransaction(); 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/NewProxyStatement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */