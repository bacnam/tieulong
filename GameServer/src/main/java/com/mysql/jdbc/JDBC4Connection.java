package com.mysql.jdbc;

import java.sql.*;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Properties;

public class JDBC4Connection
        extends ConnectionImpl {
    private JDBC4ClientInfoProvider infoProvider;

    public JDBC4Connection(String hostToConnectTo, int portToConnectTo, Properties info, String databaseToConnectTo, String url) throws SQLException {
        super(hostToConnectTo, portToConnectTo, info, databaseToConnectTo, url);
    }

    public SQLXML createSQLXML() throws SQLException {
        return new JDBC4MysqlSQLXML(getExceptionInterceptor());
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw SQLError.notImplemented();
    }

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw SQLError.notImplemented();
    }

    public Properties getClientInfo() throws SQLException {
        return getClientInfoProviderImpl().getClientInfo(this);
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        try {
            getClientInfoProviderImpl().setClientInfo(this, properties);
        } catch (SQLClientInfoException ciEx) {
            throw ciEx;
        } catch (SQLException sqlEx) {
            SQLClientInfoException clientInfoEx = new SQLClientInfoException();
            clientInfoEx.initCause(sqlEx);

            throw clientInfoEx;
        }
    }

    public String getClientInfo(String name) throws SQLException {
        return getClientInfoProviderImpl().getClientInfo(this, name);
    }

    public synchronized boolean isValid(int timeout) throws SQLException {
        if (isClosed()) {
            return false;
        }

        try {
            try {
                pingInternal(false, timeout * 1000);
            } catch (Throwable t) {
                try {
                    abortInternal();
                } catch (Throwable ignoreThrown) {
                }

                return false;
            }

        } catch (Throwable t) {
            return false;
        }

        return true;
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        try {
            getClientInfoProviderImpl().setClientInfo(this, name, value);
        } catch (SQLClientInfoException ciEx) {
            throw ciEx;
        } catch (SQLException sqlEx) {
            SQLClientInfoException clientInfoEx = new SQLClientInfoException();
            clientInfoEx.initCause(sqlEx);

            throw clientInfoEx;
        }
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        checkClosed();

        return iface.isInstance(this);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        try {
            return iface.cast(this);
        } catch (ClassCastException cce) {
            throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", getExceptionInterceptor());
        }
    }

    public Blob createBlob() {
        return new Blob(getExceptionInterceptor());
    }

    public Clob createClob() {
        return new Clob(getExceptionInterceptor());
    }

    public NClob createNClob() {
        return new JDBC4NClob(getExceptionInterceptor());
    }

    protected synchronized JDBC4ClientInfoProvider getClientInfoProviderImpl() throws SQLException {
        if (this.infoProvider == null) {
            try {
                try {
                    this.infoProvider = (JDBC4ClientInfoProvider) Util.getInstance(getClientInfoProvider(), new Class[0], new Object[0], getExceptionInterceptor());
                } catch (SQLException sqlEx) {
                    if (sqlEx.getCause() instanceof ClassCastException) {
                        this.infoProvider = (JDBC4ClientInfoProvider) Util.getInstance("com.mysql.jdbc." + getClientInfoProvider(), new Class[0], new Object[0], getExceptionInterceptor());
                    }
                }

            } catch (ClassCastException cce) {
                throw SQLError.createSQLException(Messages.getString("JDBC4Connection.ClientInfoNotImplemented", new Object[]{getClientInfoProvider()}), "S1009", getExceptionInterceptor());
            }

            this.infoProvider.initialize(this, this.props);
        }

        return this.infoProvider;
    }
}

