package ch.qos.logback.core.db;

import ch.qos.logback.core.db.dialect.SQLDialectCode;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceConnectionSource
        extends ConnectionSourceBase {
    private DataSource dataSource;

    public void start() {
        if (this.dataSource == null) {
            addWarn("WARNING: No data source specified");
        } else {
            discoverConnectionProperties();
            if (!supportsGetGeneratedKeys() && getSQLDialectCode() == SQLDialectCode.UNKNOWN_DIALECT) {
                addWarn("Connection does not support GetGeneratedKey method and could not discover the dialect.");
            }
        }
        super.start();
    }

    public Connection getConnection() throws SQLException {
        if (this.dataSource == null) {
            addError("WARNING: No data source specified");
            return null;
        }

        if (getUser() == null) {
            return this.dataSource.getConnection();
        }
        return this.dataSource.getConnection(getUser(), getPassword());
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}

