package com.mchange.v2.c3p0.util;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.sql.SqlUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

public final class TestUtils {
    private static final Method OBJECT_EQUALS;
    private static final Method IDENTITY_HASHCODE;
    private static final Method IPCFP;

    static {
        try {
            OBJECT_EQUALS = Object.class.getMethod("equals", new Class[]{Object.class});
            IDENTITY_HASHCODE = System.class.getMethod("identityHashCode", new Class[]{Object.class});

            IPCFP = TestUtils.class.getMethod("isPhysicalConnectionForProxy", new Class[]{Connection.class, C3P0ProxyConnection.class});
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("Huh? Can't reflectively get ahold of expected methods?");
        }
    }

    public static boolean samePhysicalConnection(C3P0ProxyConnection con1, C3P0ProxyConnection con2) throws SQLException {
        try {
            Object out = con1.rawConnectionOperation(IPCFP, null, new Object[]{C3P0ProxyConnection.RAW_CONNECTION, con2});
            return ((Boolean) out).booleanValue();
        } catch (Exception e) {

            e.printStackTrace();
            throw SqlUtils.toSQLException(e);
        }
    }

    public static boolean isPhysicalConnectionForProxy(Connection physicalConnection, C3P0ProxyConnection proxy) throws SQLException {
        try {
            Object out = proxy.rawConnectionOperation(OBJECT_EQUALS, physicalConnection, new Object[]{C3P0ProxyConnection.RAW_CONNECTION});
            return ((Boolean) out).booleanValue();
        } catch (Exception e) {

            e.printStackTrace();
            throw SqlUtils.toSQLException(e);
        }
    }

    public static int physicalConnectionIdentityHashCode(C3P0ProxyConnection conn) throws SQLException {
        try {
            Object out = conn.rawConnectionOperation(IDENTITY_HASHCODE, null, new Object[]{C3P0ProxyConnection.RAW_CONNECTION});
            return ((Integer) out).intValue();
        } catch (Exception e) {

            e.printStackTrace();
            throw SqlUtils.toSQLException(e);
        }
    }

    public static DataSource unreliableCommitDataSource(DataSource ds) throws Exception {
        return (DataSource) Proxy.newProxyInstance(TestUtils.class.getClassLoader(), new Class[]{DataSource.class}, new StupidDataSourceInvocationHandler(ds));
    }

    static class StupidDataSourceInvocationHandler
            implements InvocationHandler {
        DataSource ds;

        Random r = new Random();

        StupidDataSourceInvocationHandler(DataSource ds) {
            this.ds = ds;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("getConnection".equals(method.getName())) {

                Connection conn = (Connection) method.invoke(this.ds, args);
                return Proxy.newProxyInstance(TestUtils.class.getClassLoader(), new Class[]{Connection.class}, new TestUtils.StupidConnectionInvocationHandler(conn));
            }

            return method.invoke(this.ds, args);
        }
    }

    static class StupidConnectionInvocationHandler
            implements InvocationHandler {
        Connection conn;
        Random r = new Random();

        boolean invalid = false;

        StupidConnectionInvocationHandler(Connection conn) {
            this.conn = conn;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if ("close".equals(method.getName())) {

                if (this.invalid) {

                    (new Exception("Duplicate close() called on Connection!!!")).printStackTrace();

                } else {

                    this.invalid = true;
                }
                return null;
            }
            if (this.invalid)
                throw new SQLException("Connection closed -- cannot " + method.getName());
            if ("commit".equals(method.getName()) && this.r.nextInt(100) == 0) {

                this.conn.rollback();
                throw new SQLException("Random commit exception!!!");
            }
            if (this.r.nextInt(200) == 0) {

                this.conn.rollback();
                this.conn.close();
                throw new SQLException("Random Fatal Exception Occurred!!!");
            }

            return method.invoke(this.conn, args);
        }
    }
}

