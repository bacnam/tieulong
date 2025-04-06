/*      */ package com.mchange.v2.sql.filter;
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
/*      */ public abstract class SynchronizedFilterResultSet
/*      */   implements ResultSet
/*      */ {
/*      */   protected ResultSet inner;
/*      */   
/*      */   private void __setInner(ResultSet paramResultSet) {
/*   73 */     this.inner = paramResultSet;
/*      */   }
/*      */   
/*      */   public SynchronizedFilterResultSet(ResultSet paramResultSet) {
/*   77 */     __setInner(paramResultSet);
/*      */   }
/*      */   
/*      */   public SynchronizedFilterResultSet() {}
/*      */   
/*      */   public synchronized void setInner(ResultSet paramResultSet) {
/*   83 */     __setInner(paramResultSet);
/*      */   }
/*      */   public synchronized ResultSet getInner() {
/*   86 */     return this.inner;
/*      */   }
/*      */   
/*      */   public synchronized void clearWarnings() throws SQLException {
/*   90 */     this.inner.clearWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getHoldability() throws SQLException {
/*   95 */     return this.inner.getHoldability();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized ResultSetMetaData getMetaData() throws SQLException {
/*  100 */     return this.inner.getMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized SQLWarning getWarnings() throws SQLException {
/*  105 */     return this.inner.getWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isClosed() throws SQLException {
/*  110 */     return this.inner.isClosed();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
/*  115 */     this.inner.updateBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
/*  120 */     this.inner.updateBigDecimal(paramString, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean absolute(int paramInt) throws SQLException {
/*  125 */     return this.inner.absolute(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void afterLast() throws SQLException {
/*  130 */     this.inner.afterLast();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void beforeFirst() throws SQLException {
/*  135 */     this.inner.beforeFirst();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void cancelRowUpdates() throws SQLException {
/*  140 */     this.inner.cancelRowUpdates();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void deleteRow() throws SQLException {
/*  145 */     this.inner.deleteRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int findColumn(String paramString) throws SQLException {
/*  150 */     return this.inner.findColumn(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean first() throws SQLException {
/*  155 */     return this.inner.first();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized InputStream getAsciiStream(int paramInt) throws SQLException {
/*  160 */     return this.inner.getAsciiStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized InputStream getAsciiStream(String paramString) throws SQLException {
/*  165 */     return this.inner.getAsciiStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(String paramString, int paramInt) throws SQLException {
/*  170 */     return this.inner.getBigDecimal(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(String paramString) throws SQLException {
/*  175 */     return this.inner.getBigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(int paramInt) throws SQLException {
/*  180 */     return this.inner.getBigDecimal(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
/*  185 */     return this.inner.getBigDecimal(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized InputStream getBinaryStream(String paramString) throws SQLException {
/*  190 */     return this.inner.getBinaryStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized InputStream getBinaryStream(int paramInt) throws SQLException {
/*  195 */     return this.inner.getBinaryStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Blob getBlob(String paramString) throws SQLException {
/*  200 */     return this.inner.getBlob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Blob getBlob(int paramInt) throws SQLException {
/*  205 */     return this.inner.getBlob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getCharacterStream(int paramInt) throws SQLException {
/*  210 */     return this.inner.getCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getCharacterStream(String paramString) throws SQLException {
/*  215 */     return this.inner.getCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Clob getClob(int paramInt) throws SQLException {
/*  220 */     return this.inner.getClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Clob getClob(String paramString) throws SQLException {
/*  225 */     return this.inner.getClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getConcurrency() throws SQLException {
/*  230 */     return this.inner.getConcurrency();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getCursorName() throws SQLException {
/*  235 */     return this.inner.getCursorName();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getFetchDirection() throws SQLException {
/*  240 */     return this.inner.getFetchDirection();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getFetchSize() throws SQLException {
/*  245 */     return this.inner.getFetchSize();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getNCharacterStream(int paramInt) throws SQLException {
/*  250 */     return this.inner.getNCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Reader getNCharacterStream(String paramString) throws SQLException {
/*  255 */     return this.inner.getNCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized NClob getNClob(String paramString) throws SQLException {
/*  260 */     return this.inner.getNClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized NClob getNClob(int paramInt) throws SQLException {
/*  265 */     return this.inner.getNClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getNString(int paramInt) throws SQLException {
/*  270 */     return this.inner.getNString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getNString(String paramString) throws SQLException {
/*  275 */     return this.inner.getNString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getRow() throws SQLException {
/*  280 */     return this.inner.getRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized RowId getRowId(int paramInt) throws SQLException {
/*  285 */     return this.inner.getRowId(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized RowId getRowId(String paramString) throws SQLException {
/*  290 */     return this.inner.getRowId(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized SQLXML getSQLXML(String paramString) throws SQLException {
/*  295 */     return this.inner.getSQLXML(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized SQLXML getSQLXML(int paramInt) throws SQLException {
/*  300 */     return this.inner.getSQLXML(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Statement getStatement() throws SQLException {
/*  305 */     return this.inner.getStatement();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized InputStream getUnicodeStream(int paramInt) throws SQLException {
/*  310 */     return this.inner.getUnicodeStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized InputStream getUnicodeStream(String paramString) throws SQLException {
/*  315 */     return this.inner.getUnicodeStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void insertRow() throws SQLException {
/*  320 */     this.inner.insertRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isAfterLast() throws SQLException {
/*  325 */     return this.inner.isAfterLast();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isBeforeFirst() throws SQLException {
/*  330 */     return this.inner.isBeforeFirst();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isFirst() throws SQLException {
/*  335 */     return this.inner.isFirst();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isLast() throws SQLException {
/*  340 */     return this.inner.isLast();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean last() throws SQLException {
/*  345 */     return this.inner.last();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void moveToCurrentRow() throws SQLException {
/*  350 */     this.inner.moveToCurrentRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void moveToInsertRow() throws SQLException {
/*  355 */     this.inner.moveToInsertRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void refreshRow() throws SQLException {
/*  360 */     this.inner.refreshRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean relative(int paramInt) throws SQLException {
/*  365 */     return this.inner.relative(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean rowDeleted() throws SQLException {
/*  370 */     return this.inner.rowDeleted();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean rowInserted() throws SQLException {
/*  375 */     return this.inner.rowInserted();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean rowUpdated() throws SQLException {
/*  380 */     return this.inner.rowUpdated();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setFetchDirection(int paramInt) throws SQLException {
/*  385 */     this.inner.setFetchDirection(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void setFetchSize(int paramInt) throws SQLException {
/*  390 */     this.inner.setFetchSize(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateArray(String paramString, Array paramArray) throws SQLException {
/*  395 */     this.inner.updateArray(paramString, paramArray);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateArray(int paramInt, Array paramArray) throws SQLException {
/*  400 */     this.inner.updateArray(paramInt, paramArray);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  405 */     this.inner.updateAsciiStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  410 */     this.inner.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  415 */     this.inner.updateAsciiStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  420 */     this.inner.updateAsciiStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  425 */     this.inner.updateAsciiStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  430 */     this.inner.updateAsciiStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  435 */     this.inner.updateBinaryStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  440 */     this.inner.updateBinaryStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  445 */     this.inner.updateBinaryStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  450 */     this.inner.updateBinaryStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  455 */     this.inner.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  460 */     this.inner.updateBinaryStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
/*  465 */     this.inner.updateBlob(paramInt, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(String paramString, Blob paramBlob) throws SQLException {
/*  470 */     this.inner.updateBlob(paramString, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(String paramString, InputStream paramInputStream) throws SQLException {
/*  475 */     this.inner.updateBlob(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  480 */     this.inner.updateBlob(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  485 */     this.inner.updateBlob(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBlob(int paramInt, InputStream paramInputStream) throws SQLException {
/*  490 */     this.inner.updateBlob(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBoolean(String paramString, boolean paramBoolean) throws SQLException {
/*  495 */     this.inner.updateBoolean(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException {
/*  500 */     this.inner.updateBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateByte(String paramString, byte paramByte) throws SQLException {
/*  505 */     this.inner.updateByte(paramString, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateByte(int paramInt, byte paramByte) throws SQLException {
/*  510 */     this.inner.updateByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
/*  515 */     this.inner.updateBytes(paramString, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
/*  520 */     this.inner.updateBytes(paramInt, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  525 */     this.inner.updateCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
/*  530 */     this.inner.updateCharacterStream(paramString, paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  535 */     this.inner.updateCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  540 */     this.inner.updateCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  545 */     this.inner.updateCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
/*  550 */     this.inner.updateCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  555 */     this.inner.updateClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  560 */     this.inner.updateClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateClob(String paramString, Reader paramReader) throws SQLException {
/*  565 */     this.inner.updateClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateClob(int paramInt, Reader paramReader) throws SQLException {
/*  570 */     this.inner.updateClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateClob(int paramInt, Clob paramClob) throws SQLException {
/*  575 */     this.inner.updateClob(paramInt, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateClob(String paramString, Clob paramClob) throws SQLException {
/*  580 */     this.inner.updateClob(paramString, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateDate(int paramInt, Date paramDate) throws SQLException {
/*  585 */     this.inner.updateDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateDate(String paramString, Date paramDate) throws SQLException {
/*  590 */     this.inner.updateDate(paramString, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateDouble(int paramInt, double paramDouble) throws SQLException {
/*  595 */     this.inner.updateDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateDouble(String paramString, double paramDouble) throws SQLException {
/*  600 */     this.inner.updateDouble(paramString, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateFloat(String paramString, float paramFloat) throws SQLException {
/*  605 */     this.inner.updateFloat(paramString, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateFloat(int paramInt, float paramFloat) throws SQLException {
/*  610 */     this.inner.updateFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateInt(String paramString, int paramInt) throws SQLException {
/*  615 */     this.inner.updateInt(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateInt(int paramInt1, int paramInt2) throws SQLException {
/*  620 */     this.inner.updateInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateLong(String paramString, long paramLong) throws SQLException {
/*  625 */     this.inner.updateLong(paramString, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateLong(int paramInt, long paramLong) throws SQLException {
/*  630 */     this.inner.updateLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  635 */     this.inner.updateNCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  640 */     this.inner.updateNCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  645 */     this.inner.updateNCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  650 */     this.inner.updateNCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNClob(int paramInt, Reader paramReader) throws SQLException {
/*  655 */     this.inner.updateNClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNClob(String paramString, Reader paramReader) throws SQLException {
/*  660 */     this.inner.updateNClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  665 */     this.inner.updateNClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNClob(int paramInt, NClob paramNClob) throws SQLException {
/*  670 */     this.inner.updateNClob(paramInt, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  675 */     this.inner.updateNClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNClob(String paramString, NClob paramNClob) throws SQLException {
/*  680 */     this.inner.updateNClob(paramString, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNString(String paramString1, String paramString2) throws SQLException {
/*  685 */     this.inner.updateNString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNString(int paramInt, String paramString) throws SQLException {
/*  690 */     this.inner.updateNString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNull(int paramInt) throws SQLException {
/*  695 */     this.inner.updateNull(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateNull(String paramString) throws SQLException {
/*  700 */     this.inner.updateNull(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateObject(int paramInt, Object paramObject) throws SQLException {
/*  705 */     this.inner.updateObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateObject(String paramString, Object paramObject) throws SQLException {
/*  710 */     this.inner.updateObject(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
/*  715 */     this.inner.updateObject(paramString, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
/*  720 */     this.inner.updateObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRef(int paramInt, Ref paramRef) throws SQLException {
/*  725 */     this.inner.updateRef(paramInt, paramRef);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRef(String paramString, Ref paramRef) throws SQLException {
/*  730 */     this.inner.updateRef(paramString, paramRef);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRow() throws SQLException {
/*  735 */     this.inner.updateRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRowId(int paramInt, RowId paramRowId) throws SQLException {
/*  740 */     this.inner.updateRowId(paramInt, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateRowId(String paramString, RowId paramRowId) throws SQLException {
/*  745 */     this.inner.updateRowId(paramString, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
/*  750 */     this.inner.updateSQLXML(paramInt, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
/*  755 */     this.inner.updateSQLXML(paramString, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateShort(String paramString, short paramShort) throws SQLException {
/*  760 */     this.inner.updateShort(paramString, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateShort(int paramInt, short paramShort) throws SQLException {
/*  765 */     this.inner.updateShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateString(String paramString1, String paramString2) throws SQLException {
/*  770 */     this.inner.updateString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateString(int paramInt, String paramString) throws SQLException {
/*  775 */     this.inner.updateString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateTime(String paramString, Time paramTime) throws SQLException {
/*  780 */     this.inner.updateTime(paramString, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateTime(int paramInt, Time paramTime) throws SQLException {
/*  785 */     this.inner.updateTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
/*  790 */     this.inner.updateTimestamp(paramString, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
/*  795 */     this.inner.updateTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean wasNull() throws SQLException {
/*  800 */     return this.inner.wasNull();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
/*  805 */     return this.inner.getObject(paramInt, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String paramString) throws SQLException {
/*  810 */     return this.inner.getObject(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String paramString, Class<?> paramClass) throws SQLException {
/*  815 */     return this.inner.getObject(paramString, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
/*  820 */     return this.inner.getObject(paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
/*  825 */     return this.inner.getObject(paramString, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object getObject(int paramInt) throws SQLException {
/*  830 */     return this.inner.getObject(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean getBoolean(String paramString) throws SQLException {
/*  835 */     return this.inner.getBoolean(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean getBoolean(int paramInt) throws SQLException {
/*  840 */     return this.inner.getBoolean(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte getByte(int paramInt) throws SQLException {
/*  845 */     return this.inner.getByte(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte getByte(String paramString) throws SQLException {
/*  850 */     return this.inner.getByte(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized short getShort(String paramString) throws SQLException {
/*  855 */     return this.inner.getShort(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized short getShort(int paramInt) throws SQLException {
/*  860 */     return this.inner.getShort(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getInt(String paramString) throws SQLException {
/*  865 */     return this.inner.getInt(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getInt(int paramInt) throws SQLException {
/*  870 */     return this.inner.getInt(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized long getLong(String paramString) throws SQLException {
/*  875 */     return this.inner.getLong(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized long getLong(int paramInt) throws SQLException {
/*  880 */     return this.inner.getLong(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized float getFloat(int paramInt) throws SQLException {
/*  885 */     return this.inner.getFloat(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized float getFloat(String paramString) throws SQLException {
/*  890 */     return this.inner.getFloat(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized double getDouble(int paramInt) throws SQLException {
/*  895 */     return this.inner.getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized double getDouble(String paramString) throws SQLException {
/*  900 */     return this.inner.getDouble(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte[] getBytes(String paramString) throws SQLException {
/*  905 */     return this.inner.getBytes(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized byte[] getBytes(int paramInt) throws SQLException {
/*  910 */     return this.inner.getBytes(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Array getArray(int paramInt) throws SQLException {
/*  915 */     return this.inner.getArray(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Array getArray(String paramString) throws SQLException {
/*  920 */     return this.inner.getArray(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean next() throws SQLException {
/*  925 */     return this.inner.next();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized URL getURL(int paramInt) throws SQLException {
/*  930 */     return this.inner.getURL(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized URL getURL(String paramString) throws SQLException {
/*  935 */     return this.inner.getURL(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized void close() throws SQLException {
/*  940 */     this.inner.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized int getType() throws SQLException {
/*  945 */     return this.inner.getType();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean previous() throws SQLException {
/*  950 */     return this.inner.previous();
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Ref getRef(String paramString) throws SQLException {
/*  955 */     return this.inner.getRef(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Ref getRef(int paramInt) throws SQLException {
/*  960 */     return this.inner.getRef(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getString(int paramInt) throws SQLException {
/*  965 */     return this.inner.getString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized String getString(String paramString) throws SQLException {
/*  970 */     return this.inner.getString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
/*  975 */     return this.inner.getDate(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
/*  980 */     return this.inner.getDate(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(String paramString) throws SQLException {
/*  985 */     return this.inner.getDate(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Date getDate(int paramInt) throws SQLException {
/*  990 */     return this.inner.getDate(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(int paramInt) throws SQLException {
/*  995 */     return this.inner.getTime(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
/* 1000 */     return this.inner.getTime(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(String paramString) throws SQLException {
/* 1005 */     return this.inner.getTime(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
/* 1010 */     return this.inner.getTime(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(int paramInt) throws SQLException {
/* 1015 */     return this.inner.getTimestamp(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(String paramString) throws SQLException {
/* 1020 */     return this.inner.getTimestamp(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
/* 1025 */     return this.inner.getTimestamp(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
/* 1030 */     return this.inner.getTimestamp(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 1035 */     return this.inner.isWrapperFor(paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public synchronized Object unwrap(Class<?> paramClass) throws SQLException {
/* 1040 */     return this.inner.unwrap(paramClass);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/SynchronizedFilterResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */