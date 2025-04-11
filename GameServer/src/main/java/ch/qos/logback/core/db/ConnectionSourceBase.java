package ch.qos.logback.core.db;

import ch.qos.logback.core.db.dialect.DBUtil;
import ch.qos.logback.core.db.dialect.SQLDialectCode;
import ch.qos.logback.core.spi.ContextAwareBase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public abstract class ConnectionSourceBase
        extends ContextAwareBase
        implements ConnectionSource {
    private boolean started;
    private String user = null;
    private String password = null;

    private SQLDialectCode dialectCode = SQLDialectCode.UNKNOWN_DIALECT;

    private boolean supportsGetGeneratedKeys = false;

    private boolean supportsBatchUpdates = false;

    public void discoverConnectionProperties() {
        Connection connection = null;
        try {
            connection = getConnection();
            if (connection == null) {
                addWarn("Could not get a connection");
                return;
            }
            DatabaseMetaData meta = connection.getMetaData();
            DBUtil util = new DBUtil();
            util.setContext(getContext());
            this.supportsGetGeneratedKeys = util.supportsGetGeneratedKeys(meta);
            this.supportsBatchUpdates = util.supportsBatchUpdates(meta);
            this.dialectCode = DBUtil.discoverSQLDialect(meta);
            addInfo("Driver name=" + meta.getDriverName());
            addInfo("Driver version=" + meta.getDriverVersion());
            addInfo("supportsGetGeneratedKeys=" + this.supportsGetGeneratedKeys);
        } catch (SQLException se) {
            addWarn("Could not discover the dialect to use.", se);
        } finally {
            DBHelper.closeConnection(connection);
        }
    }

    public final boolean supportsGetGeneratedKeys() {
        return this.supportsGetGeneratedKeys;
    }

    public final SQLDialectCode getSQLDialectCode() {
        return this.dialectCode;
    }

    public final String getPassword() {
        return this.password;
    }

    public final void setPassword(String password) {
        this.password = password;
    }

    public final String getUser() {
        return this.user;
    }

    public final void setUser(String username) {
        this.user = username;
    }

    public final boolean supportsBatchUpdates() {
        return this.supportsBatchUpdates;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void start() {
        this.started = true;
    }

    public void stop() {
        this.started = false;
    }
}

