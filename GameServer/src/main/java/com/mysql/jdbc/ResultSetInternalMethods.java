package com.mysql.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public interface ResultSetInternalMethods extends ResultSet {
    ResultSetInternalMethods copy() throws SQLException;

    boolean reallyResult();

    Object getObjectStoredProc(int paramInt1, int paramInt2) throws SQLException;

    Object getObjectStoredProc(int paramInt1, Map<Object, Object> paramMap, int paramInt2) throws SQLException;

    Object getObjectStoredProc(String paramString, int paramInt) throws SQLException;

    Object getObjectStoredProc(String paramString, Map<Object, Object> paramMap, int paramInt) throws SQLException;

    String getServerInfo();

    long getUpdateCount();

    long getUpdateID();

    void realClose(boolean paramBoolean) throws SQLException;

    void setOwningStatement(StatementImpl paramStatementImpl);

    char getFirstCharOfQuery();

    void setFirstCharOfQuery(char paramChar);

    void clearNextResult();

    ResultSetInternalMethods getNextResultSet();

    void setStatementUsedForFetchingRows(PreparedStatement paramPreparedStatement);

    void setWrapperStatement(Statement paramStatement);

    void buildIndexMapping() throws SQLException;

    void initializeWithMetadata() throws SQLException;

    void redefineFieldsForDBMD(Field[] paramArrayOfField);

    void populateCachedMetaData(CachedResultSetMetaData paramCachedResultSetMetaData) throws SQLException;

    void initializeFromCachedMetaData(CachedResultSetMetaData paramCachedResultSetMetaData);

    int getBytesSize() throws SQLException;
}

