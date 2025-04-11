package com.jolbox.bonecp;

import com.google.common.base.Function;
import com.google.common.collect.MapMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BoneCPDataSource
        extends BoneCPConfig
        implements DataSource, ObjectFactory {
    private static final long serialVersionUID = -1561804548443209469L;
    private static final Logger logger = LoggerFactory.getLogger(BoneCPDataSource.class);
    private PrintWriter logWriter = null;
    private volatile transient BoneCP pool = null;
    private ReadWriteLock rwl = new ReentrantReadWriteLock();
    private String driverClass;
    private Map<UsernamePassword, BoneCPDataSource> multiDataSource = (new MapMaker()).makeComputingMap(new Function<UsernamePassword, BoneCPDataSource>() {
        public BoneCPDataSource apply(UsernamePassword key) {
            BoneCPDataSource ds = null;
            ds = new BoneCPDataSource(BoneCPDataSource.this.getConfig());

            ds.setUsername(key.getUsername());
            ds.setPassword(key.getPassword());

            return ds;
        }
    });

    public BoneCPDataSource() {
    }

    public BoneCPDataSource(BoneCPConfig config) {
        Field[] fields = BoneCPConfig.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                field.set(this, field.get(config));
            } catch (Exception e) {
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.pool == null) {
            maybeInit();
        }
        return this.pool.getConnection();
    }

    public void close() {
        if (this.pool != null) {
            this.pool.shutdown();
        }
    }

    private void maybeInit() throws SQLException {
        this.rwl.readLock().lock();
        if (this.pool == null) {
            this.rwl.readLock().unlock();
            this.rwl.writeLock().lock();
            if (this.pool == null) {
                try {
                    if (getDriverClass() != null) {
                        loadClass(getDriverClass());
                    }
                } catch (ClassNotFoundException e) {
                    throw new SQLException(PoolUtil.stringifyException(e));
                }

                logger.debug(toString());

                this.pool = new BoneCP(this);
            }

            this.rwl.writeLock().unlock();
        } else {
            this.rwl.readLock().unlock();
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return ((BoneCPDataSource) this.multiDataSource.get(new UsernamePassword(username, password))).getConnection();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return this.logWriter;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("getLoginTimeout is unsupported.");
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout is unsupported.");
    }

    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    public Object unwrap(Class arg0) throws SQLException {
        return null;
    }

    public String getDriverClass() {
        return this.driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getTotalLeased() {
        return this.pool.getTotalLeased();
    }

    public BoneCPConfig getConfig() {
        return this;
    }

    public Object getObjectInstance(Object object, Name name, Context context, Hashtable<?, ?> table) throws Exception {
        Reference ref = (Reference) object;
        Enumeration<RefAddr> addrs = ref.getAll();
        Properties props = new Properties();
        while (addrs.hasMoreElements()) {
            RefAddr addr = addrs.nextElement();
            if (addr.getType().equals("driverClassName")) {
                Class.forName((String) addr.getContent());
                continue;
            }
            props.put(addr.getType(), addr.getContent());
        }

        BoneCPConfig config = new BoneCPConfig(props);

        return new BoneCPDataSource(config);
    }
}

