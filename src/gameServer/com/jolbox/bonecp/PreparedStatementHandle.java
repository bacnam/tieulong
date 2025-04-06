/*      */ package com.jolbox.bonecp;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import org.slf4j.Logger;
/*      */ import org.slf4j.LoggerFactory;
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
/*      */ public class PreparedStatementHandle
/*      */   extends StatementHandle
/*      */   implements PreparedStatement
/*      */ {
/*      */   private PreparedStatement internalPreparedStatement;
/*   54 */   protected static Logger logger = LoggerFactory.getLogger(PreparedStatementHandle.class);
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
/*      */ 
/*      */   
/*      */   public PreparedStatementHandle(PreparedStatement internalPreparedStatement, String sql, ConnectionHandle connectionHandle, String cacheKey, IStatementCache cache) {
/*   71 */     super(internalPreparedStatement, sql, cache, connectionHandle, cacheKey, connectionHandle.isLogStatementsEnabled());
/*   72 */     this.internalPreparedStatement = internalPreparedStatement;
/*   73 */     this.connectionHandle = connectionHandle;
/*   74 */     this.sql = sql;
/*   75 */     this.cache = cache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBatch() throws SQLException {
/*   87 */     checkClosed();
/*      */     try {
/*   89 */       if (this.logStatementsEnabled) {
/*   90 */         this.batchSQL.append(this.sql);
/*      */       }
/*   92 */       this.internalPreparedStatement.addBatch();
/*   93 */     } catch (SQLException e) {
/*   94 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearParameters() throws SQLException {
/*  107 */     checkClosed();
/*      */     try {
/*  109 */       this.internalPreparedStatement.clearParameters();
/*  110 */       if (this.logStatementsEnabled) {
/*  111 */         this.logParams.clear();
/*      */       }
/*  113 */     } catch (SQLException e) {
/*  114 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean execute() throws SQLException {
/*  127 */     checkClosed();
/*      */     try {
/*  129 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  130 */         logger.debug(PoolUtil.fillLogParams(this.sql, this.logParams));
/*      */       }
/*  132 */       long queryStartTime = queryTimerStart();
/*      */       
/*  134 */       if (this.connectionHook != null) {
/*  135 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
/*      */       }
/*      */       
/*  138 */       boolean result = this.internalPreparedStatement.execute();
/*      */       
/*  140 */       if (this.connectionHook != null) {
/*  141 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
/*      */       }
/*      */ 
/*      */       
/*  145 */       queryTimerEnd(this.sql, queryStartTime);
/*      */       
/*  147 */       return result;
/*  148 */     } catch (SQLException e) {
/*  149 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public ResultSet executeQuery() throws SQLException {
/*  163 */     checkClosed();
/*      */     try {
/*  165 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  166 */         logger.debug(PoolUtil.fillLogParams(this.sql, this.logParams));
/*      */       }
/*  168 */       long queryStartTime = queryTimerStart();
/*  169 */       if (this.connectionHook != null) {
/*  170 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
/*      */       }
/*  172 */       ResultSet result = this.internalPreparedStatement.executeQuery();
/*  173 */       if (this.connectionHook != null) {
/*  174 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
/*      */       }
/*      */       
/*  177 */       queryTimerEnd(this.sql, queryStartTime);
/*      */       
/*  179 */       return result;
/*  180 */     } catch (SQLException e) {
/*  181 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int executeUpdate() throws SQLException {
/*  194 */     checkClosed();
/*      */     try {
/*  196 */       if (this.logStatementsEnabled && logger.isDebugEnabled()) {
/*  197 */         logger.debug(PoolUtil.fillLogParams(this.sql, this.logParams));
/*      */       }
/*  199 */       long queryStartTime = queryTimerStart();
/*  200 */       if (this.connectionHook != null) {
/*  201 */         this.connectionHook.onBeforeStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
/*      */       }
/*  203 */       int result = this.internalPreparedStatement.executeUpdate();
/*  204 */       if (this.connectionHook != null) {
/*  205 */         this.connectionHook.onAfterStatementExecute(this.connectionHandle, this, this.sql, this.logParams);
/*      */       }
/*      */       
/*  208 */       queryTimerEnd(this.sql, queryStartTime);
/*      */       
/*  210 */       return result;
/*  211 */     } catch (SQLException e) {
/*  212 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/*  225 */     checkClosed();
/*      */     try {
/*  227 */       return this.internalPreparedStatement.getMetaData();
/*  228 */     } catch (SQLException e) {
/*  229 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ParameterMetaData getParameterMetaData() throws SQLException {
/*  242 */     checkClosed();
/*      */     try {
/*  244 */       return this.internalPreparedStatement.getParameterMetaData();
/*  245 */     } catch (SQLException e) {
/*  246 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setArray(int parameterIndex, Array x) throws SQLException {
/*  259 */     checkClosed();
/*      */     try {
/*  261 */       this.internalPreparedStatement.setArray(parameterIndex, x);
/*  262 */       if (this.logStatementsEnabled) {
/*  263 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  265 */     } catch (SQLException e) {
/*  266 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
/*  276 */     checkClosed();
/*      */     try {
/*  278 */       this.internalPreparedStatement.setBinaryStream(parameterIndex, x);
/*  279 */       if (this.logStatementsEnabled) {
/*  280 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  282 */     } catch (SQLException e) {
/*  283 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
/*  292 */     checkClosed();
/*      */     try {
/*  294 */       this.internalPreparedStatement.setBinaryStream(parameterIndex, x, length);
/*  295 */       if (this.logStatementsEnabled) {
/*  296 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  298 */     } catch (SQLException e) {
/*  299 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
/*  308 */     checkClosed();
/*      */     try {
/*  310 */       this.internalPreparedStatement.setBlob(parameterIndex, inputStream);
/*  311 */       if (this.logStatementsEnabled) {
/*  312 */         this.logParams.put(Integer.valueOf(parameterIndex), inputStream);
/*      */       }
/*  314 */     } catch (SQLException e) {
/*  315 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
/*  324 */     checkClosed();
/*      */     try {
/*  326 */       this.internalPreparedStatement.setAsciiStream(parameterIndex, x, length);
/*  327 */       if (this.logStatementsEnabled) {
/*  328 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  330 */     } catch (SQLException e) {
/*  331 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader) throws SQLException {
/*  339 */     checkClosed();
/*      */     try {
/*  341 */       this.internalPreparedStatement.setClob(parameterIndex, reader);
/*  342 */       if (this.logStatementsEnabled) {
/*  343 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*  345 */     } catch (SQLException e) {
/*  346 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowId(int parameterIndex, RowId x) throws SQLException {
/*  354 */     checkClosed();
/*      */     try {
/*  356 */       this.internalPreparedStatement.setRowId(parameterIndex, x);
/*  357 */       if (this.logStatementsEnabled) {
/*  358 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  360 */     } catch (SQLException e) {
/*  361 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
/*  370 */     checkClosed();
/*      */     try {
/*  372 */       this.internalPreparedStatement.setSQLXML(parameterIndex, xmlObject);
/*  373 */       if (this.logStatementsEnabled) {
/*  374 */         this.logParams.put(Integer.valueOf(parameterIndex), xmlObject);
/*      */       }
/*  376 */     } catch (SQLException e) {
/*  377 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
/*  386 */     checkClosed();
/*      */     try {
/*  388 */       this.internalPreparedStatement.setClob(parameterIndex, reader, length);
/*  389 */       if (this.logStatementsEnabled) {
/*  390 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*      */     }
/*  393 */     catch (SQLException e) {
/*  394 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
/*  403 */     checkClosed();
/*      */     try {
/*  405 */       this.internalPreparedStatement.setNCharacterStream(parameterIndex, value);
/*  406 */       if (this.logStatementsEnabled) {
/*  407 */         this.logParams.put(Integer.valueOf(parameterIndex), value);
/*      */       }
/*      */     }
/*  410 */     catch (SQLException e) {
/*  411 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
/*  420 */     checkClosed();
/*      */     try {
/*  422 */       this.internalPreparedStatement.setNCharacterStream(parameterIndex, value, length);
/*  423 */       if (this.logStatementsEnabled) {
/*  424 */         this.logParams.put(Integer.valueOf(parameterIndex), value);
/*      */       }
/*  426 */     } catch (SQLException e) {
/*  427 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(int parameterIndex, NClob value) throws SQLException {
/*  435 */     checkClosed();
/*      */     try {
/*  437 */       this.internalPreparedStatement.setNClob(parameterIndex, value);
/*  438 */       if (this.logStatementsEnabled) {
/*  439 */         this.logParams.put(Integer.valueOf(parameterIndex), value);
/*      */       }
/*  441 */     } catch (SQLException e) {
/*  442 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader) throws SQLException {
/*  450 */     checkClosed();
/*      */     try {
/*  452 */       this.internalPreparedStatement.setNClob(parameterIndex, reader);
/*  453 */       if (this.logStatementsEnabled) {
/*  454 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*  456 */     } catch (SQLException e) {
/*  457 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
/*  466 */     checkClosed();
/*      */     try {
/*  468 */       this.internalPreparedStatement.setNClob(parameterIndex, reader, length);
/*  469 */       if (this.logStatementsEnabled) {
/*  470 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*  472 */     } catch (SQLException e) {
/*  473 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNString(int parameterIndex, String value) throws SQLException {
/*  482 */     checkClosed();
/*      */     try {
/*  484 */       this.internalPreparedStatement.setNString(parameterIndex, value);
/*  485 */       if (this.logStatementsEnabled) {
/*  486 */         this.logParams.put(Integer.valueOf(parameterIndex), value);
/*      */       }
/*  488 */     } catch (SQLException e) {
/*  489 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
/*  498 */     checkClosed();
/*      */     try {
/*  500 */       this.internalPreparedStatement.setAsciiStream(parameterIndex, x);
/*  501 */       if (this.logStatementsEnabled) {
/*  502 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  504 */     } catch (SQLException e) {
/*  505 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/*  513 */     checkClosed();
/*      */     try {
/*  515 */       this.internalPreparedStatement.setCharacterStream(parameterIndex, reader, length);
/*  516 */       if (this.logStatementsEnabled) {
/*  517 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*  519 */     } catch (SQLException e) {
/*  520 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
/*  529 */     checkClosed();
/*      */     try {
/*  531 */       this.internalPreparedStatement.setBlob(parameterIndex, inputStream, length);
/*  532 */       if (this.logStatementsEnabled) {
/*  533 */         this.logParams.put(Integer.valueOf(parameterIndex), inputStream);
/*      */       }
/*  535 */     } catch (SQLException e) {
/*  536 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
/*  545 */     checkClosed();
/*      */     try {
/*  547 */       this.internalPreparedStatement.setCharacterStream(parameterIndex, reader);
/*  548 */       if (this.logStatementsEnabled) {
/*  549 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*  551 */     } catch (SQLException e) {
/*  552 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
/*  569 */     checkClosed();
/*      */     try {
/*  571 */       this.internalPreparedStatement.setAsciiStream(parameterIndex, x, length);
/*  572 */       if (this.logStatementsEnabled) {
/*  573 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  575 */     } catch (SQLException e) {
/*  576 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
/*  590 */     checkClosed();
/*      */     try {
/*  592 */       this.internalPreparedStatement.setBigDecimal(parameterIndex, x);
/*  593 */       if (this.logStatementsEnabled) {
/*  594 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  596 */     } catch (SQLException e) {
/*  597 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
/*  613 */     checkClosed();
/*      */     try {
/*  615 */       this.internalPreparedStatement.setBinaryStream(parameterIndex, x, length);
/*  616 */       if (this.logStatementsEnabled) {
/*  617 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  619 */     } catch (SQLException e) {
/*  620 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(int parameterIndex, Blob x) throws SQLException {
/*  633 */     checkClosed();
/*      */     try {
/*  635 */       this.internalPreparedStatement.setBlob(parameterIndex, x);
/*  636 */       if (this.logStatementsEnabled) {
/*  637 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  639 */     } catch (SQLException e) {
/*  640 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setBoolean(int parameterIndex, boolean x) throws SQLException {
/*  654 */     checkClosed();
/*      */     try {
/*  656 */       this.internalPreparedStatement.setBoolean(parameterIndex, x);
/*  657 */       if (this.logStatementsEnabled) {
/*  658 */         this.logParams.put(Integer.valueOf(parameterIndex), Boolean.valueOf(x));
/*      */       }
/*  660 */     } catch (SQLException e) {
/*  661 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setByte(int parameterIndex, byte x) throws SQLException {
/*  674 */     checkClosed();
/*      */     try {
/*  676 */       this.internalPreparedStatement.setByte(parameterIndex, x);
/*  677 */       if (this.logStatementsEnabled) {
/*  678 */         this.logParams.put(Integer.valueOf(parameterIndex), Byte.valueOf(x));
/*      */       }
/*  680 */     } catch (SQLException e) {
/*  681 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBytes(int parameterIndex, byte[] x) throws SQLException {
/*  694 */     checkClosed();
/*      */     try {
/*  696 */       this.internalPreparedStatement.setBytes(parameterIndex, x);
/*  697 */       if (this.logStatementsEnabled) {
/*  698 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  700 */     } catch (SQLException e) {
/*  701 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
/*  717 */     checkClosed();
/*      */     try {
/*  719 */       this.internalPreparedStatement.setCharacterStream(parameterIndex, reader, length);
/*      */       
/*  721 */       if (this.logStatementsEnabled) {
/*  722 */         this.logParams.put(Integer.valueOf(parameterIndex), reader);
/*      */       }
/*  724 */     } catch (SQLException e) {
/*  725 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setClob(int parameterIndex, Clob x) throws SQLException {
/*  739 */     checkClosed();
/*      */     try {
/*  741 */       this.internalPreparedStatement.setClob(parameterIndex, x);
/*  742 */       if (this.logStatementsEnabled) {
/*  743 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  745 */     } catch (SQLException e) {
/*  746 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setDate(int parameterIndex, Date x) throws SQLException {
/*  760 */     checkClosed();
/*      */     try {
/*  762 */       this.internalPreparedStatement.setDate(parameterIndex, x);
/*  763 */       if (this.logStatementsEnabled) {
/*  764 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  766 */     } catch (SQLException e) {
/*  767 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
/*  782 */     checkClosed();
/*      */     try {
/*  784 */       this.internalPreparedStatement.setDate(parameterIndex, x, cal);
/*  785 */       if (this.logStatementsEnabled) {
/*  786 */         this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[] { x, ", cal=", cal }));
/*      */       }
/*  788 */     } catch (SQLException e) {
/*  789 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDouble(int parameterIndex, double x) throws SQLException {
/*  802 */     checkClosed();
/*      */     try {
/*  804 */       this.internalPreparedStatement.setDouble(parameterIndex, x);
/*  805 */       if (this.logStatementsEnabled) {
/*  806 */         this.logParams.put(Integer.valueOf(parameterIndex), Double.valueOf(x));
/*      */       }
/*  808 */     } catch (SQLException e) {
/*  809 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloat(int parameterIndex, float x) throws SQLException {
/*  822 */     checkClosed();
/*      */     try {
/*  824 */       this.internalPreparedStatement.setFloat(parameterIndex, x);
/*  825 */       if (this.logStatementsEnabled) {
/*  826 */         this.logParams.put(Integer.valueOf(parameterIndex), Float.valueOf(x));
/*      */       }
/*  828 */     } catch (SQLException e) {
/*  829 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInt(int parameterIndex, int x) throws SQLException {
/*  842 */     checkClosed();
/*      */     try {
/*  844 */       this.internalPreparedStatement.setInt(parameterIndex, x);
/*  845 */       if (this.logStatementsEnabled) {
/*  846 */         this.logParams.put(Integer.valueOf(parameterIndex), Integer.valueOf(x));
/*      */       }
/*  848 */     } catch (SQLException e) {
/*  849 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLong(int parameterIndex, long x) throws SQLException {
/*  862 */     checkClosed();
/*      */     try {
/*  864 */       this.internalPreparedStatement.setLong(parameterIndex, x);
/*  865 */       if (this.logStatementsEnabled) {
/*  866 */         this.logParams.put(Integer.valueOf(parameterIndex), Long.valueOf(x));
/*      */       }
/*  868 */     } catch (SQLException e) {
/*  869 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(int parameterIndex, int sqlType) throws SQLException {
/*  882 */     checkClosed();
/*      */     try {
/*  884 */       this.internalPreparedStatement.setNull(parameterIndex, sqlType);
/*  885 */       if (this.logStatementsEnabled) {
/*  886 */         this.logParams.put(Integer.valueOf(parameterIndex), "[SQL NULL of type " + sqlType + "]");
/*      */       }
/*  888 */     } catch (SQLException e) {
/*  889 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
/*  903 */     checkClosed();
/*      */     try {
/*  905 */       this.internalPreparedStatement.setNull(parameterIndex, sqlType, typeName);
/*  906 */       if (this.logStatementsEnabled) {
/*  907 */         this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[] { "[SQL NULL of type ", Integer.valueOf(sqlType), ", type = ", typeName, "]" }));
/*      */       }
/*  909 */     } catch (SQLException e) {
/*  910 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(int parameterIndex, Object x) throws SQLException {
/*  923 */     checkClosed();
/*      */     try {
/*  925 */       this.internalPreparedStatement.setObject(parameterIndex, x);
/*  926 */       if (this.logStatementsEnabled) {
/*  927 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  929 */     } catch (SQLException e) {
/*  930 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
/*  944 */     checkClosed();
/*      */     try {
/*  946 */       this.internalPreparedStatement.setObject(parameterIndex, x, targetSqlType);
/*  947 */       if (this.logStatementsEnabled) {
/*  948 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  950 */     } catch (SQLException e) {
/*  951 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
/*  966 */     checkClosed();
/*      */     try {
/*  968 */       this.internalPreparedStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
/*  969 */       if (this.logStatementsEnabled) {
/*  970 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  972 */     } catch (SQLException e) {
/*  973 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRef(int parameterIndex, Ref x) throws SQLException {
/*  986 */     checkClosed();
/*      */     try {
/*  988 */       this.internalPreparedStatement.setRef(parameterIndex, x);
/*  989 */       if (this.logStatementsEnabled) {
/*  990 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/*  992 */     } catch (SQLException e) {
/*  993 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setShort(int parameterIndex, short x) throws SQLException {
/* 1007 */     checkClosed();
/*      */     try {
/* 1009 */       this.internalPreparedStatement.setShort(parameterIndex, x);
/* 1010 */       if (this.logStatementsEnabled) {
/* 1011 */         this.logParams.put(Integer.valueOf(parameterIndex), Short.valueOf(x));
/*      */       }
/* 1013 */     } catch (SQLException e) {
/* 1014 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setString(int parameterIndex, String x) throws SQLException {
/* 1027 */     checkClosed();
/*      */     try {
/* 1029 */       this.internalPreparedStatement.setString(parameterIndex, x);
/* 1030 */       if (this.logStatementsEnabled) {
/* 1031 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/* 1033 */     } catch (SQLException e) {
/* 1034 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTime(int parameterIndex, Time x) throws SQLException {
/* 1047 */     checkClosed();
/*      */     try {
/* 1049 */       this.internalPreparedStatement.setTime(parameterIndex, x);
/* 1050 */       if (this.logStatementsEnabled) {
/* 1051 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/* 1053 */     } catch (SQLException e) {
/* 1054 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
/* 1069 */     checkClosed();
/*      */     try {
/* 1071 */       this.internalPreparedStatement.setTime(parameterIndex, x, cal);
/* 1072 */       if (this.logStatementsEnabled) {
/* 1073 */         this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[] { x, ", cal=", cal }));
/*      */       }
/* 1075 */     } catch (SQLException e) {
/* 1076 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
/* 1090 */     checkClosed();
/*      */     try {
/* 1092 */       this.internalPreparedStatement.setTimestamp(parameterIndex, x);
/* 1093 */       if (this.logStatementsEnabled) {
/* 1094 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/* 1096 */     } catch (SQLException e) {
/* 1097 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
/* 1112 */     checkClosed();
/*      */     try {
/* 1114 */       this.internalPreparedStatement.setTimestamp(parameterIndex, x, cal);
/* 1115 */       if (this.logStatementsEnabled) {
/* 1116 */         this.logParams.put(Integer.valueOf(parameterIndex), PoolUtil.safePrint(new Object[] { x, ", cal=", cal }));
/*      */       }
/* 1118 */     } catch (SQLException e) {
/* 1119 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setURL(int parameterIndex, URL x) throws SQLException {
/* 1132 */     checkClosed();
/*      */     try {
/* 1134 */       this.internalPreparedStatement.setURL(parameterIndex, x);
/* 1135 */       if (this.logStatementsEnabled) {
/* 1136 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/* 1138 */     } catch (SQLException e) {
/* 1139 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
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
/*      */   @Deprecated
/*      */   public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 1155 */     checkClosed();
/*      */     try {
/* 1157 */       this.internalPreparedStatement.setUnicodeStream(parameterIndex, x, length);
/* 1158 */       if (this.logStatementsEnabled) {
/* 1159 */         this.logParams.put(Integer.valueOf(parameterIndex), x);
/*      */       }
/* 1161 */     } catch (SQLException e) {
/* 1162 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PreparedStatement getInternalPreparedStatement() {
/* 1173 */     return this.internalPreparedStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInternalPreparedStatement(PreparedStatement internalPreparedStatement) {
/* 1181 */     this.internalPreparedStatement = internalPreparedStatement;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/PreparedStatementHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */