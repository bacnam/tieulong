package ch.qos.logback.core.db;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JNDIConnectionSource
        extends ConnectionSourceBase {
    private String jndiLocation = null;
    private DataSource dataSource = null;

    public void start() {
        if (this.jndiLocation == null) {
            addError("No JNDI location specified for JNDIConnectionSource.");
        }
        discoverConnectionProperties();
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            if (this.dataSource == null) {
                this.dataSource = lookupDataSource();
            }
            if (getUser() != null) {
                addWarn("Ignoring property [user] with value [" + getUser() + "] for obtaining a connection from a DataSource.");
            }
            conn = this.dataSource.getConnection();
        } catch (NamingException ne) {
            addError("Error while getting data source", ne);
            throw new SQLException("NamingException while looking up DataSource: " + ne.getMessage());
        } catch (ClassCastException cce) {
            addError("ClassCastException while looking up DataSource.", cce);
            throw new SQLException("ClassCastException while looking up DataSource: " + cce.getMessage());
        }

        return conn;
    }

    public String getJndiLocation() {
        return this.jndiLocation;
    }

    public void setJndiLocation(String jndiLocation) {
        this.jndiLocation = jndiLocation;
    }

    private DataSource lookupDataSource() throws NamingException, SQLException {
        addInfo("Looking up [" + this.jndiLocation + "] in JNDI");

        Context initialContext = new InitialContext();
        Object obj = initialContext.lookup(this.jndiLocation);

        DataSource ds = (DataSource) obj;

        if (ds == null) {
            throw new SQLException("Failed to obtain data source from JNDI location " + this.jndiLocation);
        }

        return ds;
    }
}

