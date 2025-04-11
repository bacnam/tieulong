package com.mysql.jdbc;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Statement extends Statement {
    void enableStreamingResults() throws SQLException;

    void disableStreamingResults() throws SQLException;

    InputStream getLocalInfileInputStream();

    void setLocalInfileInputStream(InputStream paramInputStream);

    void setPingTarget(PingTarget paramPingTarget);

    ExceptionInterceptor getExceptionInterceptor();

    void removeOpenResultSet(ResultSet paramResultSet);

    int getOpenResultSetCount();

    void setHoldResultsOpenOverClose(boolean paramBoolean);
}

