package com.mchange.v2.c3p0.management;

import com.mchange.v2.c3p0.PooledDataSource;
import java.sql.SQLException;
import java.util.Collection;

public class PooledDataSourceManager
implements PooledDataSourceManagerMBean
{
PooledDataSource pds;

public PooledDataSourceManager(PooledDataSource pds) {
this.pds = pds;
}
public String getIdentityToken() {
return this.pds.getIdentityToken();
}
public String getDataSourceName() {
return this.pds.getDataSourceName();
}
public void setDataSourceName(String dataSourceName) {
this.pds.setDataSourceName(dataSourceName);
}
public int getNumConnectionsDefaultUser() throws SQLException {
return this.pds.getNumConnectionsDefaultUser();
}
public int getNumIdleConnectionsDefaultUser() throws SQLException {
return this.pds.getNumIdleConnectionsDefaultUser();
}
public int getNumBusyConnectionsDefaultUser() throws SQLException {
return this.pds.getNumBusyConnectionsDefaultUser();
}
public int getNumUnclosedOrphanedConnectionsDefaultUser() throws SQLException {
return this.pds.getNumUnclosedOrphanedConnectionsDefaultUser();
}
public float getEffectivePropertyCycleDefaultUser() throws SQLException {
return this.pds.getEffectivePropertyCycleDefaultUser();
}
public int getThreadPoolSize() throws SQLException {
return this.pds.getThreadPoolSize();
}
public int getThreadPoolNumActiveThreads() throws SQLException {
return this.pds.getThreadPoolNumActiveThreads();
}
public int getThreadPoolNumIdleThreads() throws SQLException {
return this.pds.getThreadPoolNumIdleThreads();
}
public int getThreadPoolNumTasksPending() throws SQLException {
return this.pds.getThreadPoolNumTasksPending();
}
public String sampleThreadPoolStackTraces() throws SQLException {
return this.pds.sampleThreadPoolStackTraces();
}
public String sampleThreadPoolStatus() throws SQLException {
return this.pds.sampleThreadPoolStatus();
}
public void softResetDefaultUser() throws SQLException {
this.pds.softResetDefaultUser();
}
public int getNumConnections(String username, String password) throws SQLException {
return this.pds.getNumConnections(username, password);
}
public int getNumIdleConnections(String username, String password) throws SQLException {
return this.pds.getNumIdleConnections(username, password);
}
public int getNumBusyConnections(String username, String password) throws SQLException {
return this.pds.getNumBusyConnections(username, password);
}
public int getNumUnclosedOrphanedConnections(String username, String password) throws SQLException {
return this.pds.getNumUnclosedOrphanedConnections(username, password);
}
public float getEffectivePropertyCycle(String username, String password) throws SQLException {
return this.pds.getEffectivePropertyCycle(username, password);
}
public void softReset(String username, String password) throws SQLException {
this.pds.softReset(username, password);
}
public int getNumBusyConnectionsAllUsers() throws SQLException {
return this.pds.getNumBusyConnectionsAllUsers();
}
public int getNumIdleConnectionsAllUsers() throws SQLException {
return this.pds.getNumIdleConnectionsAllUsers();
}
public int getNumConnectionsAllUsers() throws SQLException {
return this.pds.getNumConnectionsAllUsers();
}
public int getNumUnclosedOrphanedConnectionsAllUsers() throws SQLException {
return this.pds.getNumUnclosedOrphanedConnectionsAllUsers();
}
public void softResetAllUsers() throws SQLException {
this.pds.softResetAllUsers();
}
public int getNumUserPools() throws SQLException {
return this.pds.getNumUserPools();
}
public Collection getAllUsers() throws SQLException {
return this.pds.getAllUsers();
}
public void hardReset() throws SQLException {
this.pds.hardReset();
}
public void close() throws SQLException {
this.pds.close();
}
}

