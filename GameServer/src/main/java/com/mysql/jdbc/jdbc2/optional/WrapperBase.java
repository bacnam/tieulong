package com.mysql.jdbc.jdbc2.optional;

import com.mysql.jdbc.ExceptionInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.Map;

abstract class WrapperBase {
    protected MysqlPooledConnection pooledConnection;
    protected Map unwrappedInterfaces = null;
    protected ExceptionInterceptor exceptionInterceptor;
    protected WrapperBase(MysqlPooledConnection pooledConnection) {
        this.pooledConnection = pooledConnection;
        this.exceptionInterceptor = this.pooledConnection.getExceptionInterceptor();
    }

    protected void checkAndFireConnectionError(SQLException sqlEx) throws SQLException {
        if (this.pooledConnection != null &&
                "08S01".equals(sqlEx.getSQLState())) {
            this.pooledConnection.callConnectionEventListeners(1, sqlEx);
        }

        throw sqlEx;
    }

    protected class ConnectionErrorFiringInvocationHandler implements InvocationHandler {
        Object invokeOn = null;

        public ConnectionErrorFiringInvocationHandler(Object toInvokeOn) {
            this.invokeOn = toInvokeOn;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Object result = null;

            try {
                result = method.invoke(this.invokeOn, args);

                if (result != null) {
                    result = proxyIfInterfaceIsJdbc(result, result.getClass());
                }
            } catch (InvocationTargetException e) {
                if (e.getTargetException() instanceof SQLException) {
                    WrapperBase.this.checkAndFireConnectionError((SQLException) e.getTargetException());
                } else {

                    throw e;
                }
            }

            return result;
        }

        private Object proxyIfInterfaceIsJdbc(Object toProxy, Class<?> clazz) {
            Class<?>[] interfaces = clazz.getInterfaces();

            Class[] arr$ = interfaces;
            int len$ = arr$.length, i$ = 0;
            if (i$ < len$) {
                Class<?> iclass = arr$[i$];
                String packageName = iclass.getPackage().getName();

                if ("java.sql".equals(packageName) || "javax.sql".equals(packageName)) {
                    return Proxy.newProxyInstance(toProxy.getClass().getClassLoader(), interfaces, new ConnectionErrorFiringInvocationHandler(toProxy));
                }

                return proxyIfInterfaceIsJdbc(toProxy, iclass);
            }

            return toProxy;
        }
    }
}

