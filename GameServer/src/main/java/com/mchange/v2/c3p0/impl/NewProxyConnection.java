package com.mchange.v2.c3p0.impl;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.SqlUtils;
import com.mchange.v2.util.ResourceClosedException;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public final class NewProxyConnection
        implements Connection, C3P0ProxyConnection {
    private static final MLogger logger = MLog.getLogger("com.mchange.v2.c3p0.impl.NewProxyConnection");
    protected Connection inner;
    boolean txn_known_resolved = true;
    DatabaseMetaData metaData = null;
    volatile NewPooledConnection parentPooledConnection;

    NewProxyConnection(Connection inner) {
        __setInner(inner);
    }

    NewProxyConnection(Connection inner, NewPooledConnection parentPooledConnection) {
        this(inner);
        attach(parentPooledConnection);
    }

    private void __setInner(Connection inner) {
        this.inner = inner;
    }

    public synchronized void close() throws SQLException {
        try {
            if (!isDetached()) {
                NewPooledConnection npc = this.parentPooledConnection;
                detach();
                npc.markClosedProxyConnection(this, this.txn_known_resolved);
                this.inner = null;
            } else if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, this + ": close() called more than once.");
            }

        } catch (NullPointerException exc) {

            if (isDetached()) {

                if (logger.isLoggable(MLevel.FINE)) {
                    logger.log(MLevel.FINE, this + ": close() called more than once.");
                }
            } else {
                throw exc;
            }
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized boolean isReadOnly() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.isReadOnly();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setReadOnly(boolean a) throws SQLException {
        try {
            this.inner.setReadOnly(a);
            this.parentPooledConnection.markNewReadOnly(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void abort(Executor a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            this.inner.abort(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Statement createStatement(int a, int b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            Statement innerStmt = this.inner.createStatement(a, b);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Statement createStatement(int a, int b, int c) throws SQLException {
        try {
            this.txn_known_resolved = false;

            Statement innerStmt = this.inner.createStatement(a, b, c);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Statement createStatement() throws SQLException {
        try {
            this.txn_known_resolved = false;

            Statement innerStmt = this.inner.createStatement();
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized PreparedStatement prepareStatement(String a, int b, int c) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, int.class, int.class};

                    Method method = Connection.class.getMethod("prepareStatement", argTypes);

                    Object[] args = {a, new Integer(b), new Integer(c)};

                    PreparedStatement preparedStatement = (PreparedStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    PreparedStatement preparedStatement = this.inner.prepareStatement(a, b, c);
                    this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
                }
            }

            PreparedStatement innerStmt = this.inner.prepareStatement(a, b, c);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized PreparedStatement prepareStatement(String a, int[] b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, int[].class};

                    Method method = Connection.class.getMethod("prepareStatement", argTypes);

                    Object[] args = {a, b};

                    PreparedStatement preparedStatement = (PreparedStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    PreparedStatement preparedStatement = this.inner.prepareStatement(a, b);
                    this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
                }
            }

            PreparedStatement innerStmt = this.inner.prepareStatement(a, b);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized PreparedStatement prepareStatement(String a, String[] b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, String[].class};

                    Method method = Connection.class.getMethod("prepareStatement", argTypes);

                    Object[] args = {a, b};

                    PreparedStatement preparedStatement = (PreparedStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    PreparedStatement preparedStatement = this.inner.prepareStatement(a, b);
                    this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
                }
            }

            PreparedStatement innerStmt = this.inner.prepareStatement(a, b);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized PreparedStatement prepareStatement(String a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class};

                    Method method = Connection.class.getMethod("prepareStatement", argTypes);

                    Object[] args = {a};

                    PreparedStatement preparedStatement = (PreparedStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    PreparedStatement preparedStatement = this.inner.prepareStatement(a);
                    this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
                }
            }

            PreparedStatement innerStmt = this.inner.prepareStatement(a);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized PreparedStatement prepareStatement(String a, int b, int c, int d) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, int.class, int.class, int.class};

                    Method method = Connection.class.getMethod("prepareStatement", argTypes);

                    Object[] args = {a, new Integer(b), new Integer(c), new Integer(d)};

                    PreparedStatement preparedStatement = (PreparedStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    PreparedStatement preparedStatement = this.inner.prepareStatement(a, b, c, d);
                    this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
                }
            }

            PreparedStatement innerStmt = this.inner.prepareStatement(a, b, c, d);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized PreparedStatement prepareStatement(String a, int b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, int.class};

                    Method method = Connection.class.getMethod("prepareStatement", argTypes);

                    Object[] args = {a, new Integer(b)};

                    PreparedStatement preparedStatement = (PreparedStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    PreparedStatement preparedStatement = this.inner.prepareStatement(a, b);
                    this.parentPooledConnection.markActiveUncachedStatement(preparedStatement);
                    return new NewProxyPreparedStatement(preparedStatement, this.parentPooledConnection, false, this);
                }
            }

            PreparedStatement innerStmt = this.inner.prepareStatement(a, b);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized CallableStatement prepareCall(String a, int b, int c) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, int.class, int.class};

                    Method method = Connection.class.getMethod("prepareCall", argTypes);

                    Object[] args = {a, new Integer(b), new Integer(c)};

                    CallableStatement callableStatement = (CallableStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    CallableStatement callableStatement = this.inner.prepareCall(a, b, c);
                    this.parentPooledConnection.markActiveUncachedStatement(callableStatement);
                    return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, false, this);
                }
            }

            CallableStatement innerStmt = this.inner.prepareCall(a, b, c);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized CallableStatement prepareCall(String a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class};

                    Method method = Connection.class.getMethod("prepareCall", argTypes);

                    Object[] args = {a};

                    CallableStatement callableStatement = (CallableStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    CallableStatement callableStatement = this.inner.prepareCall(a);
                    this.parentPooledConnection.markActiveUncachedStatement(callableStatement);
                    return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, false, this);
                }
            }

            CallableStatement innerStmt = this.inner.prepareCall(a);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized CallableStatement prepareCall(String a, int b, int c, int d) throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.parentPooledConnection.isStatementCaching()) {

                try {

                    Class[] argTypes = {String.class, int.class, int.class, int.class};

                    Method method = Connection.class.getMethod("prepareCall", argTypes);

                    Object[] args = {a, new Integer(b), new Integer(c), new Integer(d)};

                    CallableStatement callableStatement = (CallableStatement) this.parentPooledConnection.checkoutStatement(method, args);
                    return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, true, this);
                } catch (ResourceClosedException e) {

                    if (logger.isLoggable(MLevel.FINE))
                        logger.log(MLevel.FINE, "A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.", (Throwable) e);
                    CallableStatement callableStatement = this.inner.prepareCall(a, b, c, d);
                    this.parentPooledConnection.markActiveUncachedStatement(callableStatement);
                    return new NewProxyCallableStatement(callableStatement, this.parentPooledConnection, false, this);
                }
            }

            CallableStatement innerStmt = this.inner.prepareCall(a, b, c, d);
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            return new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);

        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized DatabaseMetaData getMetaData() throws SQLException {
        try {
            this.txn_known_resolved = false;

            if (this.metaData == null) {

                DatabaseMetaData innerMetaData = this.inner.getMetaData();
                this.metaData = new NewProxyDatabaseMetaData(innerMetaData, this.parentPooledConnection, this);
            }
            return this.metaData;
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized SQLWarning getWarnings() throws SQLException {
        try {
            return this.inner.getWarnings();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void clearWarnings() throws SQLException {
        try {
            this.inner.clearWarnings();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized boolean isClosed() throws SQLException {
        try {
            return isDetached();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void commit() throws SQLException {
        try {
            this.inner.commit();

            this.txn_known_resolved = true;
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void rollback() throws SQLException {
        try {
            this.inner.rollback();

            this.txn_known_resolved = true;
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void rollback(Savepoint a) throws SQLException {
        try {
            this.inner.rollback(a);

            this.txn_known_resolved = true;
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setClientInfo(String a, String b) throws SQLClientInfoException {
        try {
            try {
                this.txn_known_resolved = false;

                this.inner.setClientInfo(a, b);
            } catch (NullPointerException exc) {

                if (isDetached()) {
                    throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
                }
                throw exc;
            } catch (Exception exc) {

                if (!isDetached()) {
                    throw this.parentPooledConnection.handleThrowable(exc);
                }
                throw SqlUtils.toSQLException(exc);
            }

        } catch (Exception e) {
            throw SqlUtils.toSQLClientInfoException(e);
        }
    }

    public synchronized boolean isValid(int a) throws SQLException {
        try {
            if (isDetached()) return false;
            return this.inner.isValid(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized String nativeSQL(String a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.nativeSQL(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized boolean getAutoCommit() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getAutoCommit();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setAutoCommit(boolean a) throws SQLException {
        try {
            this.inner.setAutoCommit(a);

            this.txn_known_resolved = true;
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized String getCatalog() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getCatalog();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setCatalog(String a) throws SQLException {
        try {
            this.inner.setCatalog(a);
            this.parentPooledConnection.markNewCatalog(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized int getTransactionIsolation() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getTransactionIsolation();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setTransactionIsolation(int a) throws SQLException {
        try {
            this.inner.setTransactionIsolation(a);
            this.parentPooledConnection.markNewTxnIsolation(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Map getTypeMap() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getTypeMap();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setTypeMap(Map<String, Class<?>> a) throws SQLException {
        try {
            this.inner.setTypeMap(a);
            this.parentPooledConnection.markNewTypeMap(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized int getHoldability() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getHoldability();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setHoldability(int a) throws SQLException {
        try {
            this.inner.setHoldability(a);
            this.parentPooledConnection.markNewHoldability(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Savepoint setSavepoint(String a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.setSavepoint(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Savepoint setSavepoint() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.setSavepoint();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void releaseSavepoint(Savepoint a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            this.inner.releaseSavepoint(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Clob createClob() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.createClob();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Blob createBlob() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.createBlob();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized NClob createNClob() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.createNClob();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized SQLXML createSQLXML() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.createSQLXML();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Properties getClientInfo() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getClientInfo();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setClientInfo(Properties a) throws SQLClientInfoException {
        try {
            try {
                this.txn_known_resolved = false;

                this.inner.setClientInfo(a);
            } catch (NullPointerException exc) {

                if (isDetached()) {
                    throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
                }
                throw exc;
            } catch (Exception exc) {

                if (!isDetached()) {
                    throw this.parentPooledConnection.handleThrowable(exc);
                }
                throw SqlUtils.toSQLException(exc);
            }

        } catch (Exception e) {
            throw SqlUtils.toSQLClientInfoException(e);
        }
    }

    public synchronized String getClientInfo(String a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getClientInfo(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Array createArrayOf(String a, Object[] b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.createArrayOf(a, b);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Struct createStruct(String a, Object[] b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.createStruct(a, b);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized String getSchema() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getSchema();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setSchema(String a) throws SQLException {
        try {
            this.txn_known_resolved = false;

            this.inner.setSchema(a);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized void setNetworkTimeout(Executor a, int b) throws SQLException {
        try {
            this.txn_known_resolved = false;

            this.inner.setNetworkTimeout(a, b);
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized int getNetworkTimeout() throws SQLException {
        try {
            this.txn_known_resolved = false;

            return this.inner.getNetworkTimeout();
        } catch (NullPointerException exc) {

            if (isDetached()) {
                throw SqlUtils.toSQLException("You can't operate on a closed Connection!!!", exc);
            }
            throw exc;
        } catch (Exception exc) {

            if (!isDetached()) {
                throw this.parentPooledConnection.handleThrowable(exc);
            }
            throw SqlUtils.toSQLException(exc);
        }
    }

    public synchronized Object unwrap(Class a) throws SQLException {
        if (isWrapperFor(a)) return this.inner;
        throw new SQLException(this + " is not a wrapper for " + a.getName());
    }

    public synchronized boolean isWrapperFor(Class<Connection> a) throws SQLException {
        return (Connection.class == a || a.isAssignableFrom(this.inner.getClass()));
    }

    public Object rawConnectionOperation(Method m, Object target, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {
        maybeDirtyTransaction();

        if (this.inner == null)
            throw new SQLException("You cannot operate on a closed Connection!");
        if (target == C3P0ProxyConnection.RAW_CONNECTION)
            target = this.inner;
        for (int i = 0, len = args.length; i < len; i++) {
            if (args[i] == C3P0ProxyConnection.RAW_CONNECTION)
                args[i] = this.inner;
        }
        Object out = m.invoke(target, args);

        if (out instanceof CallableStatement) {

            CallableStatement innerStmt = (CallableStatement) out;
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            out = new NewProxyCallableStatement(innerStmt, this.parentPooledConnection, false, this);
        } else if (out instanceof PreparedStatement) {

            PreparedStatement innerStmt = (PreparedStatement) out;
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            out = new NewProxyPreparedStatement(innerStmt, this.parentPooledConnection, false, this);
        } else if (out instanceof Statement) {

            Statement innerStmt = (Statement) out;
            this.parentPooledConnection.markActiveUncachedStatement(innerStmt);
            out = new NewProxyStatement(innerStmt, this.parentPooledConnection, false, this);
        } else if (out instanceof ResultSet) {

            ResultSet innerRs = (ResultSet) out;
            this.parentPooledConnection.markActiveRawConnectionResultSet(innerRs);
            out = new NewProxyResultSet(innerRs, this.parentPooledConnection, this.inner, this);
        } else if (out instanceof DatabaseMetaData) {
            out = new NewProxyDatabaseMetaData((DatabaseMetaData) out, this.parentPooledConnection);
        }
        return out;
    }

    synchronized void maybeDirtyTransaction() {
        this.txn_known_resolved = false;
    }    ConnectionEventListener cel = new ConnectionEventListener() {
        public void connectionErrorOccurred(ConnectionEvent evt) {
        }

        public void connectionClosed(ConnectionEvent evt) {
            NewProxyConnection.this.detach();
        }
    };

    void attach(NewPooledConnection parentPooledConnection) {
        this.parentPooledConnection = parentPooledConnection;
        parentPooledConnection.addConnectionEventListener(this.cel);
    }

    private void detach() {
        this.parentPooledConnection.removeConnectionEventListener(this.cel);
        this.parentPooledConnection = null;
    }

    boolean isDetached() {
        return (this.parentPooledConnection == null);
    }


}

