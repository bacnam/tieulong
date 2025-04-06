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
/*      */ public abstract class FilterResultSet
/*      */   implements ResultSet
/*      */ {
/*      */   protected ResultSet inner;
/*      */   
/*      */   private void __setInner(ResultSet paramResultSet) {
/*   73 */     this.inner = paramResultSet;
/*      */   }
/*      */   
/*      */   public FilterResultSet(ResultSet paramResultSet) {
/*   77 */     __setInner(paramResultSet);
/*      */   }
/*      */   
/*      */   public FilterResultSet() {}
/*      */   
/*      */   public void setInner(ResultSet paramResultSet) {
/*   83 */     __setInner(paramResultSet);
/*      */   }
/*      */   public ResultSet getInner() {
/*   86 */     return this.inner;
/*      */   }
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/*   90 */     this.inner.clearWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHoldability() throws SQLException {
/*   95 */     return this.inner.getHoldability();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/*  100 */     return this.inner.getMetaData();
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLWarning getWarnings() throws SQLException {
/*  105 */     return this.inner.getWarnings();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isClosed() throws SQLException {
/*  110 */     return this.inner.isClosed();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBigDecimal(int paramInt, BigDecimal paramBigDecimal) throws SQLException {
/*  115 */     this.inner.updateBigDecimal(paramInt, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBigDecimal(String paramString, BigDecimal paramBigDecimal) throws SQLException {
/*  120 */     this.inner.updateBigDecimal(paramString, paramBigDecimal);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean absolute(int paramInt) throws SQLException {
/*  125 */     return this.inner.absolute(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void afterLast() throws SQLException {
/*  130 */     this.inner.afterLast();
/*      */   }
/*      */ 
/*      */   
/*      */   public void beforeFirst() throws SQLException {
/*  135 */     this.inner.beforeFirst();
/*      */   }
/*      */ 
/*      */   
/*      */   public void cancelRowUpdates() throws SQLException {
/*  140 */     this.inner.cancelRowUpdates();
/*      */   }
/*      */ 
/*      */   
/*      */   public void deleteRow() throws SQLException {
/*  145 */     this.inner.deleteRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public int findColumn(String paramString) throws SQLException {
/*  150 */     return this.inner.findColumn(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean first() throws SQLException {
/*  155 */     return this.inner.first();
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getAsciiStream(int paramInt) throws SQLException {
/*  160 */     return this.inner.getAsciiStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getAsciiStream(String paramString) throws SQLException {
/*  165 */     return this.inner.getAsciiStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String paramString, int paramInt) throws SQLException {
/*  170 */     return this.inner.getBigDecimal(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(String paramString) throws SQLException {
/*  175 */     return this.inner.getBigDecimal(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int paramInt) throws SQLException {
/*  180 */     return this.inner.getBigDecimal(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public BigDecimal getBigDecimal(int paramInt1, int paramInt2) throws SQLException {
/*  185 */     return this.inner.getBigDecimal(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(String paramString) throws SQLException {
/*  190 */     return this.inner.getBinaryStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getBinaryStream(int paramInt) throws SQLException {
/*  195 */     return this.inner.getBinaryStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Blob getBlob(String paramString) throws SQLException {
/*  200 */     return this.inner.getBlob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Blob getBlob(int paramInt) throws SQLException {
/*  205 */     return this.inner.getBlob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(int paramInt) throws SQLException {
/*  210 */     return this.inner.getCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getCharacterStream(String paramString) throws SQLException {
/*  215 */     return this.inner.getCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Clob getClob(int paramInt) throws SQLException {
/*  220 */     return this.inner.getClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Clob getClob(String paramString) throws SQLException {
/*  225 */     return this.inner.getClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getConcurrency() throws SQLException {
/*  230 */     return this.inner.getConcurrency();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCursorName() throws SQLException {
/*  235 */     return this.inner.getCursorName();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFetchDirection() throws SQLException {
/*  240 */     return this.inner.getFetchDirection();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFetchSize() throws SQLException {
/*  245 */     return this.inner.getFetchSize();
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getNCharacterStream(int paramInt) throws SQLException {
/*  250 */     return this.inner.getNCharacterStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Reader getNCharacterStream(String paramString) throws SQLException {
/*  255 */     return this.inner.getNCharacterStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public NClob getNClob(String paramString) throws SQLException {
/*  260 */     return this.inner.getNClob(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public NClob getNClob(int paramInt) throws SQLException {
/*  265 */     return this.inner.getNClob(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNString(int paramInt) throws SQLException {
/*  270 */     return this.inner.getNString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getNString(String paramString) throws SQLException {
/*  275 */     return this.inner.getNString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getRow() throws SQLException {
/*  280 */     return this.inner.getRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public RowId getRowId(int paramInt) throws SQLException {
/*  285 */     return this.inner.getRowId(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public RowId getRowId(String paramString) throws SQLException {
/*  290 */     return this.inner.getRowId(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLXML getSQLXML(String paramString) throws SQLException {
/*  295 */     return this.inner.getSQLXML(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public SQLXML getSQLXML(int paramInt) throws SQLException {
/*  300 */     return this.inner.getSQLXML(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Statement getStatement() throws SQLException {
/*  305 */     return this.inner.getStatement();
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getUnicodeStream(int paramInt) throws SQLException {
/*  310 */     return this.inner.getUnicodeStream(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public InputStream getUnicodeStream(String paramString) throws SQLException {
/*  315 */     return this.inner.getUnicodeStream(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void insertRow() throws SQLException {
/*  320 */     this.inner.insertRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAfterLast() throws SQLException {
/*  325 */     return this.inner.isAfterLast();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBeforeFirst() throws SQLException {
/*  330 */     return this.inner.isBeforeFirst();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFirst() throws SQLException {
/*  335 */     return this.inner.isFirst();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLast() throws SQLException {
/*  340 */     return this.inner.isLast();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean last() throws SQLException {
/*  345 */     return this.inner.last();
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveToCurrentRow() throws SQLException {
/*  350 */     this.inner.moveToCurrentRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveToInsertRow() throws SQLException {
/*  355 */     this.inner.moveToInsertRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public void refreshRow() throws SQLException {
/*  360 */     this.inner.refreshRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean relative(int paramInt) throws SQLException {
/*  365 */     return this.inner.relative(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean rowDeleted() throws SQLException {
/*  370 */     return this.inner.rowDeleted();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean rowInserted() throws SQLException {
/*  375 */     return this.inner.rowInserted();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean rowUpdated() throws SQLException {
/*  380 */     return this.inner.rowUpdated();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFetchDirection(int paramInt) throws SQLException {
/*  385 */     this.inner.setFetchDirection(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFetchSize(int paramInt) throws SQLException {
/*  390 */     this.inner.setFetchSize(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateArray(String paramString, Array paramArray) throws SQLException {
/*  395 */     this.inner.updateArray(paramString, paramArray);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateArray(int paramInt, Array paramArray) throws SQLException {
/*  400 */     this.inner.updateArray(paramInt, paramArray);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  405 */     this.inner.updateAsciiStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  410 */     this.inner.updateAsciiStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  415 */     this.inner.updateAsciiStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  420 */     this.inner.updateAsciiStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  425 */     this.inner.updateAsciiStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateAsciiStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  430 */     this.inner.updateAsciiStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  435 */     this.inner.updateBinaryStream(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream) throws SQLException {
/*  440 */     this.inner.updateBinaryStream(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(int paramInt, InputStream paramInputStream) throws SQLException {
/*  445 */     this.inner.updateBinaryStream(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  450 */     this.inner.updateBinaryStream(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(int paramInt1, InputStream paramInputStream, int paramInt2) throws SQLException {
/*  455 */     this.inner.updateBinaryStream(paramInt1, paramInputStream, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBinaryStream(String paramString, InputStream paramInputStream, int paramInt) throws SQLException {
/*  460 */     this.inner.updateBinaryStream(paramString, paramInputStream, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlob(int paramInt, Blob paramBlob) throws SQLException {
/*  465 */     this.inner.updateBlob(paramInt, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlob(String paramString, Blob paramBlob) throws SQLException {
/*  470 */     this.inner.updateBlob(paramString, paramBlob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlob(String paramString, InputStream paramInputStream) throws SQLException {
/*  475 */     this.inner.updateBlob(paramString, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlob(String paramString, InputStream paramInputStream, long paramLong) throws SQLException {
/*  480 */     this.inner.updateBlob(paramString, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlob(int paramInt, InputStream paramInputStream, long paramLong) throws SQLException {
/*  485 */     this.inner.updateBlob(paramInt, paramInputStream, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBlob(int paramInt, InputStream paramInputStream) throws SQLException {
/*  490 */     this.inner.updateBlob(paramInt, paramInputStream);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBoolean(String paramString, boolean paramBoolean) throws SQLException {
/*  495 */     this.inner.updateBoolean(paramString, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBoolean(int paramInt, boolean paramBoolean) throws SQLException {
/*  500 */     this.inner.updateBoolean(paramInt, paramBoolean);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateByte(String paramString, byte paramByte) throws SQLException {
/*  505 */     this.inner.updateByte(paramString, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateByte(int paramInt, byte paramByte) throws SQLException {
/*  510 */     this.inner.updateByte(paramInt, paramByte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBytes(String paramString, byte[] paramArrayOfbyte) throws SQLException {
/*  515 */     this.inner.updateBytes(paramString, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateBytes(int paramInt, byte[] paramArrayOfbyte) throws SQLException {
/*  520 */     this.inner.updateBytes(paramInt, paramArrayOfbyte);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  525 */     this.inner.updateCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, int paramInt) throws SQLException {
/*  530 */     this.inner.updateCharacterStream(paramString, paramReader, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  535 */     this.inner.updateCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  540 */     this.inner.updateCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  545 */     this.inner.updateCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateCharacterStream(int paramInt1, Reader paramReader, int paramInt2) throws SQLException {
/*  550 */     this.inner.updateCharacterStream(paramInt1, paramReader, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  555 */     this.inner.updateClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  560 */     this.inner.updateClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClob(String paramString, Reader paramReader) throws SQLException {
/*  565 */     this.inner.updateClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClob(int paramInt, Reader paramReader) throws SQLException {
/*  570 */     this.inner.updateClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClob(int paramInt, Clob paramClob) throws SQLException {
/*  575 */     this.inner.updateClob(paramInt, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateClob(String paramString, Clob paramClob) throws SQLException {
/*  580 */     this.inner.updateClob(paramString, paramClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDate(int paramInt, Date paramDate) throws SQLException {
/*  585 */     this.inner.updateDate(paramInt, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDate(String paramString, Date paramDate) throws SQLException {
/*  590 */     this.inner.updateDate(paramString, paramDate);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDouble(int paramInt, double paramDouble) throws SQLException {
/*  595 */     this.inner.updateDouble(paramInt, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateDouble(String paramString, double paramDouble) throws SQLException {
/*  600 */     this.inner.updateDouble(paramString, paramDouble);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFloat(String paramString, float paramFloat) throws SQLException {
/*  605 */     this.inner.updateFloat(paramString, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateFloat(int paramInt, float paramFloat) throws SQLException {
/*  610 */     this.inner.updateFloat(paramInt, paramFloat);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateInt(String paramString, int paramInt) throws SQLException {
/*  615 */     this.inner.updateInt(paramString, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateInt(int paramInt1, int paramInt2) throws SQLException {
/*  620 */     this.inner.updateInt(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateLong(String paramString, long paramLong) throws SQLException {
/*  625 */     this.inner.updateLong(paramString, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateLong(int paramInt, long paramLong) throws SQLException {
/*  630 */     this.inner.updateLong(paramInt, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNCharacterStream(int paramInt, Reader paramReader) throws SQLException {
/*  635 */     this.inner.updateNCharacterStream(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader) throws SQLException {
/*  640 */     this.inner.updateNCharacterStream(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNCharacterStream(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  645 */     this.inner.updateNCharacterStream(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNCharacterStream(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  650 */     this.inner.updateNCharacterStream(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNClob(int paramInt, Reader paramReader) throws SQLException {
/*  655 */     this.inner.updateNClob(paramInt, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNClob(String paramString, Reader paramReader) throws SQLException {
/*  660 */     this.inner.updateNClob(paramString, paramReader);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNClob(int paramInt, Reader paramReader, long paramLong) throws SQLException {
/*  665 */     this.inner.updateNClob(paramInt, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNClob(int paramInt, NClob paramNClob) throws SQLException {
/*  670 */     this.inner.updateNClob(paramInt, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNClob(String paramString, Reader paramReader, long paramLong) throws SQLException {
/*  675 */     this.inner.updateNClob(paramString, paramReader, paramLong);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNClob(String paramString, NClob paramNClob) throws SQLException {
/*  680 */     this.inner.updateNClob(paramString, paramNClob);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNString(String paramString1, String paramString2) throws SQLException {
/*  685 */     this.inner.updateNString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNString(int paramInt, String paramString) throws SQLException {
/*  690 */     this.inner.updateNString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNull(int paramInt) throws SQLException {
/*  695 */     this.inner.updateNull(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateNull(String paramString) throws SQLException {
/*  700 */     this.inner.updateNull(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateObject(int paramInt, Object paramObject) throws SQLException {
/*  705 */     this.inner.updateObject(paramInt, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateObject(String paramString, Object paramObject) throws SQLException {
/*  710 */     this.inner.updateObject(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateObject(String paramString, Object paramObject, int paramInt) throws SQLException {
/*  715 */     this.inner.updateObject(paramString, paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateObject(int paramInt1, Object paramObject, int paramInt2) throws SQLException {
/*  720 */     this.inner.updateObject(paramInt1, paramObject, paramInt2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRef(int paramInt, Ref paramRef) throws SQLException {
/*  725 */     this.inner.updateRef(paramInt, paramRef);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRef(String paramString, Ref paramRef) throws SQLException {
/*  730 */     this.inner.updateRef(paramString, paramRef);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRow() throws SQLException {
/*  735 */     this.inner.updateRow();
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRowId(int paramInt, RowId paramRowId) throws SQLException {
/*  740 */     this.inner.updateRowId(paramInt, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRowId(String paramString, RowId paramRowId) throws SQLException {
/*  745 */     this.inner.updateRowId(paramString, paramRowId);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateSQLXML(int paramInt, SQLXML paramSQLXML) throws SQLException {
/*  750 */     this.inner.updateSQLXML(paramInt, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateSQLXML(String paramString, SQLXML paramSQLXML) throws SQLException {
/*  755 */     this.inner.updateSQLXML(paramString, paramSQLXML);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateShort(String paramString, short paramShort) throws SQLException {
/*  760 */     this.inner.updateShort(paramString, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateShort(int paramInt, short paramShort) throws SQLException {
/*  765 */     this.inner.updateShort(paramInt, paramShort);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateString(String paramString1, String paramString2) throws SQLException {
/*  770 */     this.inner.updateString(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateString(int paramInt, String paramString) throws SQLException {
/*  775 */     this.inner.updateString(paramInt, paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTime(String paramString, Time paramTime) throws SQLException {
/*  780 */     this.inner.updateTime(paramString, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTime(int paramInt, Time paramTime) throws SQLException {
/*  785 */     this.inner.updateTime(paramInt, paramTime);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTimestamp(String paramString, Timestamp paramTimestamp) throws SQLException {
/*  790 */     this.inner.updateTimestamp(paramString, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateTimestamp(int paramInt, Timestamp paramTimestamp) throws SQLException {
/*  795 */     this.inner.updateTimestamp(paramInt, paramTimestamp);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean wasNull() throws SQLException {
/*  800 */     return this.inner.wasNull();
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(int paramInt, Class<?> paramClass) throws SQLException {
/*  805 */     return this.inner.getObject(paramInt, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(String paramString) throws SQLException {
/*  810 */     return this.inner.getObject(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(String paramString, Class<?> paramClass) throws SQLException {
/*  815 */     return this.inner.getObject(paramString, paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(int paramInt, Map<String, Class<?>> paramMap) throws SQLException {
/*  820 */     return this.inner.getObject(paramInt, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(String paramString, Map<String, Class<?>> paramMap) throws SQLException {
/*  825 */     return this.inner.getObject(paramString, paramMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getObject(int paramInt) throws SQLException {
/*  830 */     return this.inner.getObject(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(String paramString) throws SQLException {
/*  835 */     return this.inner.getBoolean(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int paramInt) throws SQLException {
/*  840 */     return this.inner.getBoolean(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int paramInt) throws SQLException {
/*  845 */     return this.inner.getByte(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(String paramString) throws SQLException {
/*  850 */     return this.inner.getByte(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(String paramString) throws SQLException {
/*  855 */     return this.inner.getShort(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int paramInt) throws SQLException {
/*  860 */     return this.inner.getShort(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(String paramString) throws SQLException {
/*  865 */     return this.inner.getInt(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int paramInt) throws SQLException {
/*  870 */     return this.inner.getInt(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(String paramString) throws SQLException {
/*  875 */     return this.inner.getLong(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int paramInt) throws SQLException {
/*  880 */     return this.inner.getLong(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int paramInt) throws SQLException {
/*  885 */     return this.inner.getFloat(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(String paramString) throws SQLException {
/*  890 */     return this.inner.getFloat(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int paramInt) throws SQLException {
/*  895 */     return this.inner.getDouble(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(String paramString) throws SQLException {
/*  900 */     return this.inner.getDouble(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getBytes(String paramString) throws SQLException {
/*  905 */     return this.inner.getBytes(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getBytes(int paramInt) throws SQLException {
/*  910 */     return this.inner.getBytes(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Array getArray(int paramInt) throws SQLException {
/*  915 */     return this.inner.getArray(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Array getArray(String paramString) throws SQLException {
/*  920 */     return this.inner.getArray(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean next() throws SQLException {
/*  925 */     return this.inner.next();
/*      */   }
/*      */ 
/*      */   
/*      */   public URL getURL(int paramInt) throws SQLException {
/*  930 */     return this.inner.getURL(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public URL getURL(String paramString) throws SQLException {
/*  935 */     return this.inner.getURL(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {
/*  940 */     this.inner.close();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getType() throws SQLException {
/*  945 */     return this.inner.getType();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean previous() throws SQLException {
/*  950 */     return this.inner.previous();
/*      */   }
/*      */ 
/*      */   
/*      */   public Ref getRef(String paramString) throws SQLException {
/*  955 */     return this.inner.getRef(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Ref getRef(int paramInt) throws SQLException {
/*  960 */     return this.inner.getRef(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(int paramInt) throws SQLException {
/*  965 */     return this.inner.getString(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getString(String paramString) throws SQLException {
/*  970 */     return this.inner.getString(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(int paramInt, Calendar paramCalendar) throws SQLException {
/*  975 */     return this.inner.getDate(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(String paramString, Calendar paramCalendar) throws SQLException {
/*  980 */     return this.inner.getDate(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(String paramString) throws SQLException {
/*  985 */     return this.inner.getDate(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Date getDate(int paramInt) throws SQLException {
/*  990 */     return this.inner.getDate(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(int paramInt) throws SQLException {
/*  995 */     return this.inner.getTime(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(String paramString, Calendar paramCalendar) throws SQLException {
/* 1000 */     return this.inner.getTime(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(String paramString) throws SQLException {
/* 1005 */     return this.inner.getTime(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Time getTime(int paramInt, Calendar paramCalendar) throws SQLException {
/* 1010 */     return this.inner.getTime(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int paramInt) throws SQLException {
/* 1015 */     return this.inner.getTimestamp(paramInt);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String paramString) throws SQLException {
/* 1020 */     return this.inner.getTimestamp(paramString);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(String paramString, Calendar paramCalendar) throws SQLException {
/* 1025 */     return this.inner.getTimestamp(paramString, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public Timestamp getTimestamp(int paramInt, Calendar paramCalendar) throws SQLException {
/* 1030 */     return this.inner.getTimestamp(paramInt, paramCalendar);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/* 1035 */     return this.inner.isWrapperFor(paramClass);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object unwrap(Class<?> paramClass) throws SQLException {
/* 1040 */     return this.inner.unwrap(paramClass);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/sql/filter/FilterResultSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */