/*      */ package com.jolbox.bonecp;
/*      */ 
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.URL;
/*      */ import java.sql.Array;
/*      */ import java.sql.Blob;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Clob;
/*      */ import java.sql.Date;
/*      */ import java.sql.NClob;
/*      */ import java.sql.Ref;
/*      */ import java.sql.RowId;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLXML;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.util.Calendar;
/*      */ import java.util.Map;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CallableStatementHandle
/*      */   extends PreparedStatementHandle
/*      */   implements CallableStatement
/*      */ {
/*      */   private CallableStatement internalCallableStatement;
/*      */   
/*      */   public CallableStatementHandle(CallableStatement internalCallableStatement, String sql, ConnectionHandle connectionHandle, String cacheKey, IStatementCache cache) {
/*   61 */     super(internalCallableStatement, sql, connectionHandle, cacheKey, cache);
/*   62 */     this.internalCallableStatement = internalCallableStatement;
/*   63 */     this.connectionHandle = connectionHandle;
/*   64 */     this.sql = sql;
/*   65 */     this.cache = cache;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Array getArray(int parameterIndex) throws SQLException {
/*   75 */     checkClosed();
/*      */     try {
/*   77 */       return this.internalCallableStatement.getArray(parameterIndex);
/*   78 */     } catch (SQLException e) {
/*   79 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Array getArray(String parameterName) throws SQLException {
/*   91 */     checkClosed();
/*      */     try {
/*   93 */       return this.internalCallableStatement.getArray(parameterName);
/*   94 */     } catch (SQLException e) {
/*   95 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
/*  108 */     checkClosed();
/*      */     try {
/*  110 */       return this.internalCallableStatement.getBigDecimal(parameterIndex);
/*  111 */     } catch (SQLException e) {
/*  112 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public BigDecimal getBigDecimal(String parameterName) throws SQLException {
/*  125 */     checkClosed();
/*      */     try {
/*  127 */       return this.internalCallableStatement.getBigDecimal(parameterName);
/*  128 */     } catch (SQLException e) {
/*  129 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   @Deprecated
/*      */   public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
/*  144 */     checkClosed();
/*      */     try {
/*  146 */       return this.internalCallableStatement.getBigDecimal(parameterIndex, scale);
/*      */     }
/*  148 */     catch (SQLException e) {
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
/*      */   public Blob getBlob(int parameterIndex) throws SQLException {
/*  162 */     checkClosed();
/*      */     try {
/*  164 */       return this.internalCallableStatement.getBlob(parameterIndex);
/*  165 */     } catch (SQLException e) {
/*  166 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Blob getBlob(String parameterName) throws SQLException {
/*  179 */     checkClosed();
/*      */     try {
/*  181 */       return this.internalCallableStatement.getBlob(parameterName);
/*  182 */     } catch (SQLException e) {
/*  183 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public boolean getBoolean(int parameterIndex) throws SQLException {
/*  196 */     checkClosed();
/*      */     try {
/*  198 */       return this.internalCallableStatement.getBoolean(parameterIndex);
/*  199 */     } catch (SQLException e) {
/*  200 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public boolean getBoolean(String parameterName) throws SQLException {
/*  213 */     checkClosed();
/*      */     try {
/*  215 */       return this.internalCallableStatement.getBoolean(parameterName);
/*  216 */     } catch (SQLException e) {
/*  217 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public byte getByte(int parameterIndex) throws SQLException {
/*  230 */     checkClosed();
/*      */     try {
/*  232 */       return this.internalCallableStatement.getByte(parameterIndex);
/*  233 */     } catch (SQLException e) {
/*  234 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public byte getByte(String parameterName) throws SQLException {
/*  247 */     checkClosed();
/*      */     try {
/*  249 */       return this.internalCallableStatement.getByte(parameterName);
/*  250 */     } catch (SQLException e) {
/*  251 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public byte[] getBytes(int parameterIndex) throws SQLException {
/*  264 */     checkClosed();
/*      */     try {
/*  266 */       return this.internalCallableStatement.getBytes(parameterIndex);
/*  267 */     } catch (SQLException e) {
/*  268 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public byte[] getBytes(String parameterName) throws SQLException {
/*  281 */     checkClosed();
/*      */     try {
/*  283 */       return this.internalCallableStatement.getBytes(parameterName);
/*  284 */     } catch (SQLException e) {
/*  285 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(int parameterIndex) throws SQLException {
/*  294 */     checkClosed();
/*      */     try {
/*  296 */       return this.internalCallableStatement.getCharacterStream(parameterIndex);
/*  297 */     } catch (SQLException e) {
/*  298 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(String parameterName) throws SQLException {
/*  306 */     checkClosed();
/*      */     try {
/*  308 */       return this.internalCallableStatement.getCharacterStream(parameterName);
/*  309 */     } catch (SQLException e) {
/*  310 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getNCharacterStream(int parameterIndex) throws SQLException {
/*  317 */     checkClosed();
/*      */     try {
/*  319 */       return this.internalCallableStatement.getNCharacterStream(parameterIndex);
/*      */     }
/*  321 */     catch (SQLException e) {
/*  322 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Reader getNCharacterStream(String parameterName) throws SQLException {
/*  330 */     checkClosed();
/*      */     try {
/*  332 */       return this.internalCallableStatement.getNCharacterStream(parameterName);
/*      */     }
/*  334 */     catch (SQLException e) {
/*  335 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NClob getNClob(int parameterIndex) throws SQLException {
/*  343 */     checkClosed();
/*      */     try {
/*  345 */       return this.internalCallableStatement.getNClob(parameterIndex);
/*  346 */     } catch (SQLException e) {
/*  347 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NClob getNClob(String parameterName) throws SQLException {
/*  355 */     checkClosed();
/*      */     try {
/*  357 */       return this.internalCallableStatement.getNClob(parameterName);
/*  358 */     } catch (SQLException e) {
/*  359 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNString(int parameterIndex) throws SQLException {
/*  367 */     checkClosed();
/*      */     try {
/*  369 */       return this.internalCallableStatement.getNString(parameterIndex);
/*  370 */     } catch (SQLException e) {
/*  371 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNString(String parameterName) throws SQLException {
/*  379 */     checkClosed();
/*      */     try {
/*  381 */       return this.internalCallableStatement.getNString(parameterName);
/*  382 */     } catch (SQLException e) {
/*  383 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public RowId getRowId(int parameterIndex) throws SQLException {
/*  390 */     checkClosed();
/*      */     try {
/*  392 */       return this.internalCallableStatement.getRowId(parameterIndex);
/*  393 */     } catch (SQLException e) {
/*  394 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RowId getRowId(String parameterName) throws SQLException {
/*  402 */     checkClosed();
/*      */     try {
/*  404 */       return this.internalCallableStatement.getRowId(parameterName);
/*  405 */     } catch (SQLException e) {
/*  406 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLXML getSQLXML(int parameterIndex) throws SQLException {
/*  413 */     checkClosed();
/*      */     try {
/*  415 */       return this.internalCallableStatement.getSQLXML(parameterIndex);
/*  416 */     } catch (SQLException e) {
/*  417 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public SQLXML getSQLXML(String parameterName) throws SQLException {
/*  425 */     checkClosed();
/*      */     try {
/*  427 */       return this.internalCallableStatement.getSQLXML(parameterName);
/*  428 */     } catch (SQLException e) {
/*  429 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
/*  439 */     checkClosed();
/*      */     try {
/*  441 */       this.internalCallableStatement.setAsciiStream(parameterName, x);
/*  442 */       if (this.logStatementsEnabled) {
/*  443 */         this.logParams.put(parameterName, x);
/*      */       }
/*  445 */     } catch (SQLException e) {
/*  446 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
/*  454 */     checkClosed();
/*      */     try {
/*  456 */       this.internalCallableStatement.setAsciiStream(parameterName, x, length);
/*  457 */       if (this.logStatementsEnabled) {
/*  458 */         this.logParams.put(parameterName, x);
/*      */       }
/*  460 */     } catch (SQLException e) {
/*  461 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
/*  470 */     checkClosed();
/*      */     try {
/*  472 */       this.internalCallableStatement.setBinaryStream(parameterName, x);
/*  473 */       if (this.logStatementsEnabled) {
/*  474 */         this.logParams.put(parameterName, x);
/*      */       }
/*  476 */     } catch (SQLException e) {
/*  477 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
/*  485 */     checkClosed();
/*      */     try {
/*  487 */       this.internalCallableStatement.setBinaryStream(parameterName, x, length);
/*  488 */       if (this.logStatementsEnabled) {
/*  489 */         this.logParams.put(parameterName, x);
/*      */       }
/*  491 */     } catch (SQLException e) {
/*  492 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(String parameterName, Blob x) throws SQLException {
/*  500 */     checkClosed();
/*      */     try {
/*  502 */       this.internalCallableStatement.setBlob(parameterName, x);
/*  503 */       if (this.logStatementsEnabled) {
/*  504 */         this.logParams.put(parameterName, x);
/*      */       }
/*  506 */     } catch (SQLException e) {
/*  507 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
/*  516 */     checkClosed();
/*      */     try {
/*  518 */       this.internalCallableStatement.setBlob(parameterName, inputStream);
/*  519 */       if (this.logStatementsEnabled) {
/*  520 */         this.logParams.put(parameterName, inputStream);
/*      */       }
/*  522 */     } catch (SQLException e) {
/*  523 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
/*  532 */     checkClosed();
/*      */     try {
/*  534 */       this.internalCallableStatement.setBlob(parameterName, inputStream, length);
/*  535 */       if (this.logStatementsEnabled) {
/*  536 */         this.logParams.put(parameterName, inputStream);
/*      */       }
/*  538 */     } catch (SQLException e) {
/*  539 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
/*  549 */     checkClosed();
/*      */     try {
/*  551 */       this.internalCallableStatement.setCharacterStream(parameterName, reader);
/*  552 */       if (this.logStatementsEnabled) {
/*  553 */         this.logParams.put(parameterName, reader);
/*      */       }
/*  555 */     } catch (SQLException e) {
/*  556 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
/*  564 */     checkClosed();
/*      */     try {
/*  566 */       this.internalCallableStatement.setCharacterStream(parameterName, reader, length);
/*  567 */       if (this.logStatementsEnabled) {
/*  568 */         this.logParams.put(parameterName, reader);
/*      */       }
/*  570 */     } catch (SQLException e) {
/*  571 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(String parameterName, Clob x) throws SQLException {
/*  578 */     checkClosed();
/*      */     try {
/*  580 */       this.internalCallableStatement.setClob(parameterName, x);
/*  581 */       if (this.logStatementsEnabled) {
/*  582 */         this.logParams.put(parameterName, x);
/*      */       }
/*  584 */     } catch (SQLException e) {
/*  585 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(String parameterName, Reader reader) throws SQLException {
/*  593 */     checkClosed();
/*      */     try {
/*  595 */       this.internalCallableStatement.setClob(parameterName, reader);
/*  596 */       if (this.logStatementsEnabled) {
/*  597 */         this.logParams.put(parameterName, reader);
/*      */       }
/*  599 */     } catch (SQLException e) {
/*  600 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(String parameterName, Reader reader, long length) throws SQLException {
/*  608 */     checkClosed();
/*      */     try {
/*  610 */       this.internalCallableStatement.setClob(parameterName, reader, length);
/*  611 */       if (this.logStatementsEnabled) {
/*  612 */         this.logParams.put(parameterName, reader);
/*      */       }
/*  614 */     } catch (SQLException e) {
/*  615 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
/*  622 */     checkClosed();
/*      */     try {
/*  624 */       this.internalCallableStatement.setNCharacterStream(parameterName, value);
/*  625 */       if (this.logStatementsEnabled) {
/*  626 */         this.logParams.put(parameterName, value);
/*      */       }
/*  628 */     } catch (SQLException e) {
/*  629 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
/*  637 */     checkClosed();
/*      */     try {
/*  639 */       this.internalCallableStatement.setNCharacterStream(parameterName, value, length);
/*  640 */       if (this.logStatementsEnabled) {
/*  641 */         this.logParams.put(parameterName, value);
/*      */       }
/*  643 */     } catch (SQLException e) {
/*  644 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(String parameterName, NClob value) throws SQLException {
/*  651 */     checkClosed();
/*      */     try {
/*  653 */       this.internalCallableStatement.setNClob(parameterName, value);
/*  654 */       if (this.logStatementsEnabled) {
/*  655 */         this.logParams.put(parameterName, value);
/*      */       }
/*  657 */     } catch (SQLException e) {
/*  658 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(String parameterName, Reader reader) throws SQLException {
/*  666 */     checkClosed();
/*      */     try {
/*  668 */       this.internalCallableStatement.setNClob(parameterName, reader);
/*  669 */       if (this.logStatementsEnabled) {
/*  670 */         this.logParams.put(parameterName, reader);
/*      */       }
/*  672 */     } catch (SQLException e) {
/*  673 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
/*  681 */     checkClosed();
/*      */     try {
/*  683 */       this.internalCallableStatement.setNClob(parameterName, reader, length);
/*  684 */       if (this.logStatementsEnabled) {
/*  685 */         this.logParams.put(parameterName, reader);
/*      */       }
/*  687 */     } catch (SQLException e) {
/*  688 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNString(String parameterName, String value) throws SQLException {
/*  696 */     checkClosed();
/*      */     try {
/*  698 */       this.internalCallableStatement.setNString(parameterName, value);
/*  699 */       if (this.logStatementsEnabled) {
/*  700 */         this.logParams.put(parameterName, value);
/*      */       }
/*  702 */     } catch (SQLException e) {
/*  703 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setRowId(String parameterName, RowId x) throws SQLException {
/*  710 */     checkClosed();
/*      */     try {
/*  712 */       this.internalCallableStatement.setRowId(parameterName, x);
/*  713 */       if (this.logStatementsEnabled) {
/*  714 */         this.logParams.put(parameterName, x);
/*      */       }
/*  716 */     } catch (SQLException e) {
/*  717 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSQLXML(String parameterName, SQLXML xmlObject) throws SQLException {
/*  726 */     checkClosed();
/*      */     try {
/*  728 */       this.internalCallableStatement.setSQLXML(parameterName, xmlObject);
/*  729 */       if (this.logStatementsEnabled) {
/*  730 */         this.logParams.put(parameterName, xmlObject);
/*      */       }
/*  732 */     } catch (SQLException e) {
/*  733 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Clob getClob(int parameterIndex) throws SQLException {
/*  747 */     checkClosed();
/*      */     try {
/*  749 */       return this.internalCallableStatement.getClob(parameterIndex);
/*  750 */     } catch (SQLException e) {
/*  751 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Clob getClob(String parameterName) throws SQLException {
/*  764 */     checkClosed();
/*      */     try {
/*  766 */       return this.internalCallableStatement.getClob(parameterName);
/*  767 */     } catch (SQLException e) {
/*  768 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Date getDate(int parameterIndex) throws SQLException {
/*  781 */     checkClosed();
/*      */     try {
/*  783 */       return this.internalCallableStatement.getDate(parameterIndex);
/*  784 */     } catch (SQLException e) {
/*  785 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Date getDate(String parameterName) throws SQLException {
/*  798 */     checkClosed();
/*      */     try {
/*  800 */       return this.internalCallableStatement.getDate(parameterName);
/*  801 */     } catch (SQLException e) {
/*  802 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
/*  815 */     checkClosed();
/*      */     try {
/*  817 */       return this.internalCallableStatement.getDate(parameterIndex, cal);
/*  818 */     } catch (SQLException e) {
/*  819 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Date getDate(String parameterName, Calendar cal) throws SQLException {
/*  833 */     checkClosed();
/*      */     try {
/*  835 */       return this.internalCallableStatement.getDate(parameterName, cal);
/*  836 */     } catch (SQLException e) {
/*  837 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public double getDouble(int parameterIndex) throws SQLException {
/*  850 */     checkClosed();
/*      */     try {
/*  852 */       return this.internalCallableStatement.getDouble(parameterIndex);
/*  853 */     } catch (SQLException e) {
/*  854 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public double getDouble(String parameterName) throws SQLException {
/*  867 */     checkClosed();
/*      */     try {
/*  869 */       return this.internalCallableStatement.getDouble(parameterName);
/*  870 */     } catch (SQLException e) {
/*  871 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public float getFloat(int parameterIndex) throws SQLException {
/*  884 */     checkClosed();
/*      */     try {
/*  886 */       return this.internalCallableStatement.getFloat(parameterIndex);
/*  887 */     } catch (SQLException e) {
/*  888 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public float getFloat(String parameterName) throws SQLException {
/*  901 */     checkClosed();
/*      */     try {
/*  903 */       return this.internalCallableStatement.getFloat(parameterName);
/*  904 */     } catch (SQLException e) {
/*  905 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public int getInt(int parameterIndex) throws SQLException {
/*  918 */     checkClosed();
/*      */     try {
/*  920 */       return this.internalCallableStatement.getInt(parameterIndex);
/*  921 */     } catch (SQLException e) {
/*  922 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public int getInt(String parameterName) throws SQLException {
/*  935 */     checkClosed();
/*      */     try {
/*  937 */       return this.internalCallableStatement.getInt(parameterName);
/*  938 */     } catch (SQLException e) {
/*  939 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public long getLong(int parameterIndex) throws SQLException {
/*  952 */     checkClosed();
/*      */     try {
/*  954 */       return this.internalCallableStatement.getLong(parameterIndex);
/*  955 */     } catch (SQLException e) {
/*  956 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public long getLong(String parameterName) throws SQLException {
/*  969 */     checkClosed();
/*      */     try {
/*  971 */       return this.internalCallableStatement.getLong(parameterName);
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
/*      */   
/*      */   public Object getObject(int parameterIndex) throws SQLException {
/*  987 */     checkClosed();
/*      */     try {
/*  989 */       return this.internalCallableStatement.getObject(parameterIndex);
/*  990 */     } catch (SQLException e) {
/*  991 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Object getObject(String parameterName) throws SQLException {
/* 1004 */     checkClosed();
/*      */     try {
/* 1006 */       return this.internalCallableStatement.getObject(parameterName);
/* 1007 */     } catch (SQLException e) {
/* 1008 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
/* 1022 */     checkClosed();
/*      */     try {
/* 1024 */       return this.internalCallableStatement.getObject(parameterIndex, map);
/* 1025 */     } catch (SQLException e) {
/* 1026 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
/* 1041 */     checkClosed();
/*      */     try {
/* 1043 */       return this.internalCallableStatement.getObject(parameterName, map);
/* 1044 */     } catch (SQLException e) {
/* 1045 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Ref getRef(int parameterIndex) throws SQLException {
/* 1058 */     checkClosed();
/*      */     try {
/* 1060 */       return this.internalCallableStatement.getRef(parameterIndex);
/* 1061 */     } catch (SQLException e) {
/* 1062 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Ref getRef(String parameterName) throws SQLException {
/* 1075 */     checkClosed();
/*      */     try {
/* 1077 */       return this.internalCallableStatement.getRef(parameterName);
/* 1078 */     } catch (SQLException e) {
/* 1079 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public short getShort(int parameterIndex) throws SQLException {
/* 1093 */     checkClosed();
/*      */     try {
/* 1095 */       return this.internalCallableStatement.getShort(parameterIndex);
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
/*      */   public short getShort(String parameterName) throws SQLException {
/* 1110 */     checkClosed();
/*      */     try {
/* 1112 */       return this.internalCallableStatement.getShort(parameterName);
/* 1113 */     } catch (SQLException e) {
/* 1114 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public String getString(int parameterIndex) throws SQLException {
/* 1127 */     checkClosed();
/*      */     try {
/* 1129 */       return this.internalCallableStatement.getString(parameterIndex);
/* 1130 */     } catch (SQLException e) {
/* 1131 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public String getString(String parameterName) throws SQLException {
/* 1144 */     checkClosed();
/*      */     try {
/* 1146 */       return this.internalCallableStatement.getString(parameterName);
/* 1147 */     } catch (SQLException e) {
/* 1148 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Time getTime(int parameterIndex) throws SQLException {
/* 1161 */     checkClosed();
/*      */     try {
/* 1163 */       return this.internalCallableStatement.getTime(parameterIndex);
/* 1164 */     } catch (SQLException e) {
/* 1165 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Time getTime(String parameterName) throws SQLException {
/* 1178 */     checkClosed();
/*      */     try {
/* 1180 */       return this.internalCallableStatement.getTime(parameterName);
/* 1181 */     } catch (SQLException e) {
/* 1182 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
/* 1195 */     checkClosed();
/*      */     try {
/* 1197 */       return this.internalCallableStatement.getTime(parameterIndex, cal);
/* 1198 */     } catch (SQLException e) {
/* 1199 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Time getTime(String parameterName, Calendar cal) throws SQLException {
/* 1213 */     checkClosed();
/*      */     try {
/* 1215 */       return this.internalCallableStatement.getTime(parameterName, cal);
/* 1216 */     } catch (SQLException e) {
/* 1217 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Timestamp getTimestamp(int parameterIndex) throws SQLException {
/* 1230 */     checkClosed();
/*      */     try {
/* 1232 */       return this.internalCallableStatement.getTimestamp(parameterIndex);
/* 1233 */     } catch (SQLException e) {
/* 1234 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Timestamp getTimestamp(String parameterName) throws SQLException {
/* 1247 */     checkClosed();
/*      */     try {
/* 1249 */       return this.internalCallableStatement.getTimestamp(parameterName);
/* 1250 */     } catch (SQLException e) {
/* 1251 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
/* 1265 */     checkClosed();
/*      */     try {
/* 1267 */       return this.internalCallableStatement.getTimestamp(parameterIndex, cal);
/*      */     }
/* 1269 */     catch (SQLException e) {
/* 1270 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
/* 1285 */     checkClosed();
/*      */     try {
/* 1287 */       return this.internalCallableStatement.getTimestamp(parameterName, cal);
/*      */     }
/* 1289 */     catch (SQLException e) {
/* 1290 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public URL getURL(int parameterIndex) throws SQLException {
/* 1303 */     checkClosed();
/*      */     try {
/* 1305 */       return this.internalCallableStatement.getURL(parameterIndex);
/* 1306 */     } catch (SQLException e) {
/* 1307 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public URL getURL(String parameterName) throws SQLException {
/* 1320 */     checkClosed();
/*      */     try {
/* 1322 */       return this.internalCallableStatement.getURL(parameterName);
/* 1323 */     } catch (SQLException e) {
/* 1324 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
/* 1338 */     checkClosed();
/*      */     try {
/* 1340 */       this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType);
/* 1341 */     } catch (SQLException e) {
/* 1342 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
/* 1357 */     checkClosed();
/*      */     try {
/* 1359 */       this.internalCallableStatement.registerOutParameter(parameterName, sqlType);
/* 1360 */     } catch (SQLException e) {
/* 1361 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
/* 1375 */     checkClosed();
/*      */     try {
/* 1377 */       this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType, scale);
/* 1378 */     } catch (SQLException e) {
/* 1379 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
/* 1394 */     checkClosed();
/*      */     try {
/* 1396 */       this.internalCallableStatement.registerOutParameter(parameterIndex, sqlType, typeName);
/* 1397 */     } catch (SQLException e) {
/* 1398 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
/* 1413 */     checkClosed();
/*      */     try {
/* 1415 */       this.internalCallableStatement.registerOutParameter(parameterName, sqlType, scale);
/* 1416 */     } catch (SQLException e) {
/* 1417 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
/* 1432 */     checkClosed();
/*      */     try {
/* 1434 */       this.internalCallableStatement.registerOutParameter(parameterName, sqlType, typeName);
/* 1435 */     } catch (SQLException e) {
/* 1436 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
/* 1451 */     checkClosed();
/*      */     try {
/* 1453 */       this.internalCallableStatement.setAsciiStream(parameterName, x, length);
/* 1454 */       if (this.logStatementsEnabled) {
/* 1455 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1457 */     } catch (SQLException e) {
/* 1458 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
/* 1474 */     checkClosed();
/*      */     try {
/* 1476 */       this.internalCallableStatement.setBigDecimal(parameterName, x);
/* 1477 */       if (this.logStatementsEnabled) {
/* 1478 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1480 */     } catch (SQLException e) {
/* 1481 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
/* 1497 */     checkClosed();
/*      */     try {
/* 1499 */       this.internalCallableStatement.setBinaryStream(parameterName, x, length);
/* 1500 */       if (this.logStatementsEnabled) {
/* 1501 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1503 */     } catch (SQLException e) {
/* 1504 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setBoolean(String parameterName, boolean x) throws SQLException {
/* 1516 */     checkClosed();
/*      */     try {
/* 1518 */       this.internalCallableStatement.setBoolean(parameterName, x);
/* 1519 */       if (this.logStatementsEnabled) {
/* 1520 */         this.logParams.put(parameterName, Boolean.valueOf(x));
/*      */       }
/* 1522 */     } catch (SQLException e) {
/* 1523 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setByte(String parameterName, byte x) throws SQLException {
/* 1536 */     checkClosed();
/*      */     try {
/* 1538 */       this.internalCallableStatement.setByte(parameterName, x);
/* 1539 */       if (this.logStatementsEnabled) {
/* 1540 */         this.logParams.put(parameterName, Byte.valueOf(x));
/*      */       }
/* 1542 */     } catch (SQLException e) {
/* 1543 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setBytes(String parameterName, byte[] x) throws SQLException {
/* 1556 */     checkClosed();
/*      */     try {
/* 1558 */       this.internalCallableStatement.setBytes(parameterName, x);
/* 1559 */       if (this.logStatementsEnabled) {
/* 1560 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1562 */     } catch (SQLException e) {
/* 1563 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
/* 1579 */     checkClosed();
/*      */     try {
/* 1581 */       this.internalCallableStatement.setCharacterStream(parameterName, reader, length);
/* 1582 */       if (this.logStatementsEnabled) {
/* 1583 */         this.logParams.put(parameterName, reader);
/*      */       }
/* 1585 */     } catch (SQLException e) {
/* 1586 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setDate(String parameterName, Date x) throws SQLException {
/* 1599 */     checkClosed();
/*      */     try {
/* 1601 */       this.internalCallableStatement.setDate(parameterName, x);
/* 1602 */       if (this.logStatementsEnabled) {
/* 1603 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1605 */     } catch (SQLException e) {
/* 1606 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
/* 1620 */     checkClosed();
/*      */     try {
/* 1622 */       this.internalCallableStatement.setDate(parameterName, x, cal);
/* 1623 */       if (this.logStatementsEnabled) {
/* 1624 */         this.logParams.put(parameterName, PoolUtil.safePrint(new Object[] { x, ", cal=", cal }));
/*      */       }
/* 1626 */     } catch (SQLException e) {
/* 1627 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setDouble(String parameterName, double x) throws SQLException {
/* 1639 */     checkClosed();
/*      */     try {
/* 1641 */       this.internalCallableStatement.setDouble(parameterName, x);
/* 1642 */       if (this.logStatementsEnabled) {
/* 1643 */         this.logParams.put(parameterName, Double.valueOf(x));
/*      */       }
/* 1645 */     } catch (SQLException e) {
/* 1646 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setFloat(String parameterName, float x) throws SQLException {
/* 1658 */     checkClosed();
/*      */     try {
/* 1660 */       this.internalCallableStatement.setFloat(parameterName, x);
/* 1661 */       if (this.logStatementsEnabled) {
/* 1662 */         this.logParams.put(parameterName, Float.valueOf(x));
/*      */       }
/* 1664 */     } catch (SQLException e) {
/* 1665 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setInt(String parameterName, int x) throws SQLException {
/* 1677 */     checkClosed();
/*      */     try {
/* 1679 */       this.internalCallableStatement.setInt(parameterName, x);
/* 1680 */       if (this.logStatementsEnabled) {
/* 1681 */         this.logParams.put(parameterName, Integer.valueOf(x));
/*      */       }
/* 1683 */     } catch (SQLException e) {
/* 1684 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setLong(String parameterName, long x) throws SQLException {
/* 1696 */     checkClosed();
/*      */     try {
/* 1698 */       this.internalCallableStatement.setLong(parameterName, x);
/* 1699 */       if (this.logStatementsEnabled) {
/* 1700 */         this.logParams.put(parameterName, Long.valueOf(x));
/*      */       }
/* 1702 */     } catch (SQLException e) {
/* 1703 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setNull(String parameterName, int sqlType) throws SQLException {
/* 1715 */     checkClosed();
/*      */     try {
/* 1717 */       this.internalCallableStatement.setNull(parameterName, sqlType);
/* 1718 */       if (this.logStatementsEnabled) {
/* 1719 */         this.logParams.put(parameterName, PoolUtil.safePrint(new Object[] { "[SQL NULL type ", Integer.valueOf(sqlType), "]" }));
/*      */       }
/* 1721 */     } catch (SQLException e) {
/* 1722 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
/* 1736 */     checkClosed();
/*      */     try {
/* 1738 */       this.internalCallableStatement.setNull(parameterName, sqlType, typeName);
/* 1739 */       if (this.logStatementsEnabled) {
/* 1740 */         this.logParams.put(parameterName, PoolUtil.safePrint(new Object[] { "[SQL NULL type ", Integer.valueOf(sqlType), ", type=", typeName + "]" }));
/*      */       }
/* 1742 */     } catch (SQLException e) {
/* 1743 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setObject(String parameterName, Object x) throws SQLException {
/* 1756 */     checkClosed();
/*      */     try {
/* 1758 */       this.internalCallableStatement.setObject(parameterName, x);
/* 1759 */       if (this.logStatementsEnabled) {
/* 1760 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1762 */     } catch (SQLException e) {
/* 1763 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
/* 1777 */     checkClosed();
/*      */     try {
/* 1779 */       this.internalCallableStatement.setObject(parameterName, x, targetSqlType);
/* 1780 */       if (this.logStatementsEnabled) {
/* 1781 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1783 */     } catch (SQLException e) {
/* 1784 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
/* 1798 */     checkClosed();
/*      */     try {
/* 1800 */       this.internalCallableStatement.setObject(parameterName, x, targetSqlType, scale);
/* 1801 */       if (this.logStatementsEnabled) {
/* 1802 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1804 */     } catch (SQLException e) {
/* 1805 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShort(String parameterName, short x) throws SQLException {
/* 1812 */     checkClosed();
/*      */     try {
/* 1814 */       this.internalCallableStatement.setShort(parameterName, x);
/* 1815 */       if (this.logStatementsEnabled) {
/* 1816 */         this.logParams.put(parameterName, Short.valueOf(x));
/*      */       }
/* 1818 */     } catch (SQLException e) {
/* 1819 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setString(String parameterName, String x) throws SQLException {
/* 1832 */     checkClosed();
/*      */     try {
/* 1834 */       this.internalCallableStatement.setString(parameterName, x);
/* 1835 */       if (this.logStatementsEnabled) {
/* 1836 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1838 */     } catch (SQLException e) {
/* 1839 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setTime(String parameterName, Time x) throws SQLException {
/* 1851 */     checkClosed();
/*      */     try {
/* 1853 */       this.internalCallableStatement.setTime(parameterName, x);
/* 1854 */       if (this.logStatementsEnabled) {
/* 1855 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1857 */     } catch (SQLException e) {
/* 1858 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
/* 1872 */     checkClosed();
/*      */     try {
/* 1874 */       this.internalCallableStatement.setTime(parameterName, x, cal);
/* 1875 */       if (this.logStatementsEnabled) {
/* 1876 */         this.logParams.put(parameterName, PoolUtil.safePrint(new Object[] { x, ", cal=", cal }));
/*      */       }
/* 1878 */     } catch (SQLException e) {
/* 1879 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
/* 1893 */     checkClosed();
/*      */     try {
/* 1895 */       this.internalCallableStatement.setTimestamp(parameterName, x);
/* 1896 */       if (this.logStatementsEnabled) {
/* 1897 */         this.logParams.put(parameterName, x);
/*      */       }
/* 1899 */     } catch (SQLException e) {
/* 1900 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
/* 1914 */     checkClosed();
/*      */     try {
/* 1916 */       this.internalCallableStatement.setTimestamp(parameterName, x, cal);
/* 1917 */       if (this.logStatementsEnabled) {
/* 1918 */         this.logParams.put(parameterName, PoolUtil.safePrint(new Object[] { x, ", cal=", cal }));
/*      */       }
/* 1920 */     } catch (SQLException e) {
/* 1921 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public void setURL(String parameterName, URL val) throws SQLException {
/* 1933 */     checkClosed();
/*      */     try {
/* 1935 */       this.internalCallableStatement.setURL(parameterName, val);
/* 1936 */       if (this.logStatementsEnabled) {
/* 1937 */         this.logParams.put(parameterName, val);
/*      */       }
/* 1939 */     } catch (SQLException e) {
/* 1940 */       throw this.connectionHandle.markPossiblyBroken(e);
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
/*      */   public boolean wasNull() throws SQLException {
/* 1952 */     checkClosed();
/*      */     try {
/* 1954 */       return this.internalCallableStatement.wasNull();
/* 1955 */     } catch (SQLException e) {
/* 1956 */       throw this.connectionHandle.markPossiblyBroken(e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CallableStatement getInternalCallableStatement() {
/* 1965 */     return this.internalCallableStatement;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInternalCallableStatement(CallableStatement internalCallableStatement) {
/* 1973 */     this.internalCallableStatement = internalCallableStatement;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/CallableStatementHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */