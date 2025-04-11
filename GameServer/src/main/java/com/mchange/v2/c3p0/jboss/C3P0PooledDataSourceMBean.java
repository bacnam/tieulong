package com.mchange.v2.c3p0.jboss;

import javax.naming.NamingException;
import java.beans.PropertyVetoException;
import java.sql.SQLException;

public interface C3P0PooledDataSourceMBean {
    String getJndiName();

    void setJndiName(String paramString) throws NamingException;

    String getDescription();

    void setDescription(String paramString) throws NamingException;

    String getDriverClass();

    void setDriverClass(String paramString) throws PropertyVetoException, NamingException;

    String getJdbcUrl();

    void setJdbcUrl(String paramString) throws NamingException;

    String getUser();

    void setUser(String paramString) throws NamingException;

    String getPassword();

    void setPassword(String paramString) throws NamingException;

    int getUnreturnedConnectionTimeout();

    void setUnreturnedConnectionTimeout(int paramInt) throws NamingException;

    boolean isDebugUnreturnedConnectionStackTraces();

    void setDebugUnreturnedConnectionStackTraces(boolean paramBoolean) throws NamingException;

    String getConnectionCustomizerClassName();

    void setConnectionCustomizerClassName(String paramString) throws NamingException;

    int getMaxConnectionAge();

    void setMaxConnectionAge(int paramInt) throws NamingException;

    int getMaxIdleTimeExcessConnections();

    void setMaxIdleTimeExcessConnections(int paramInt) throws NamingException;

    int getMaxAdministrativeTaskTime();

    void setMaxAdministrativeTaskTime(int paramInt) throws NamingException;

    int getCheckoutTimeout();

    void setCheckoutTimeout(int paramInt) throws NamingException;

    int getAcquireIncrement();

    void setAcquireIncrement(int paramInt) throws NamingException;

    int getAcquireRetryAttempts();

    void setAcquireRetryAttempts(int paramInt) throws NamingException;

    int getAcquireRetryDelay();

    void setAcquireRetryDelay(int paramInt) throws NamingException;

    boolean isAutoCommitOnClose();

    void setAutoCommitOnClose(boolean paramBoolean) throws NamingException;

    String getConnectionTesterClassName();

    void setConnectionTesterClassName(String paramString) throws PropertyVetoException, NamingException;

    String getAutomaticTestTable();

    void setAutomaticTestTable(String paramString) throws NamingException;

    boolean isForceIgnoreUnresolvedTransactions();

    void setForceIgnoreUnresolvedTransactions(boolean paramBoolean) throws NamingException;

    int getIdleConnectionTestPeriod();

    void setIdleConnectionTestPeriod(int paramInt) throws NamingException;

    int getInitialPoolSize();

    void setInitialPoolSize(int paramInt) throws NamingException;

    int getMaxIdleTime();

    void setMaxIdleTime(int paramInt) throws NamingException;

    int getMaxPoolSize();

    void setMaxPoolSize(int paramInt) throws NamingException;

    int getMaxStatements();

    void setMaxStatements(int paramInt) throws NamingException;

    int getMaxStatementsPerConnection();

    void setMaxStatementsPerConnection(int paramInt) throws NamingException;

    int getMinPoolSize();

    void setMinPoolSize(int paramInt) throws NamingException;

    int getPropertyCycle();

    void setPropertyCycle(int paramInt) throws NamingException;

    boolean isBreakAfterAcquireFailure();

    void setBreakAfterAcquireFailure(boolean paramBoolean) throws NamingException;

    boolean isTestConnectionOnCheckout();

    void setTestConnectionOnCheckout(boolean paramBoolean) throws NamingException;

    boolean isTestConnectionOnCheckin();

    void setTestConnectionOnCheckin(boolean paramBoolean) throws NamingException;

    boolean isUsesTraditionalReflectiveProxies();

    void setUsesTraditionalReflectiveProxies(boolean paramBoolean) throws NamingException;

    String getPreferredTestQuery();

    void setPreferredTestQuery(String paramString) throws NamingException;

    int getNumHelperThreads();

    void setNumHelperThreads(int paramInt) throws NamingException;

    String getFactoryClassLocation();

    void setFactoryClassLocation(String paramString) throws NamingException;

    int getNumUserPools() throws SQLException;

    int getNumConnectionsDefaultUser() throws SQLException;

    int getNumIdleConnectionsDefaultUser() throws SQLException;

    int getNumBusyConnectionsDefaultUser() throws SQLException;

    int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException;

    int getNumConnections(String paramString1, String paramString2) throws SQLException;

    int getNumIdleConnections(String paramString1, String paramString2) throws SQLException;

    int getNumBusyConnections(String paramString1, String paramString2) throws SQLException;

    int getNumUnclosedOrphanedConnections(String paramString1, String paramString2) throws SQLException;

    float getEffectivePropertyCycle(String paramString1, String paramString2) throws SQLException;

    int getNumBusyConnectionsAllUsers() throws SQLException;

    int getNumIdleConnectionsAllUsers() throws SQLException;

    int getNumConnectionsAllUsers() throws SQLException;

    int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException;

    float getEffectivePropertyCycleDefaultUser() throws SQLException;

    void softResetDefaultUser() throws SQLException;

    void softReset(String paramString1, String paramString2) throws SQLException;

    void softResetAllUsers() throws SQLException;

    void hardReset() throws SQLException;

    void close() throws SQLException;

    void create() throws Exception;

    void start() throws Exception;

    void stop();

    void destroy();
}

