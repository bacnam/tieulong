package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SequentialBalanceStrategy
implements BalanceStrategy
{
private int currentHostIndex = -1;

public void destroy() {}

public void init(Connection conn, Properties props) throws SQLException {}

public ConnectionImpl pickConnection(LoadBalancingConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
int numHosts = configuredHosts.size();

SQLException ex = null;

Map<String, Long> blackList = proxy.getGlobalBlacklist();

for (int attempts = 0; attempts < numRetries; ) {
if (numHosts == 1) {
this.currentHostIndex = 0;
} else if (this.currentHostIndex == -1) {
int random = (int)Math.floor(Math.random() * numHosts);
int i;
for (i = random; i < numHosts; i++) {
if (!blackList.containsKey(configuredHosts.get(i))) {
this.currentHostIndex = i;

break;
} 
} 
if (this.currentHostIndex == -1) {
for (i = 0; i < random; i++) {
if (!blackList.containsKey(configuredHosts.get(i))) {
this.currentHostIndex = i;

break;
} 
} 
}
if (this.currentHostIndex == -1) {
blackList = proxy.getGlobalBlacklist();

try {
Thread.sleep(250L);
} catch (InterruptedException e) {}

continue;
} 
} else {
int i = this.currentHostIndex + 1;
boolean foundGoodHost = false;

for (; i < numHosts; i++) {
if (!blackList.containsKey(configuredHosts.get(i))) {
this.currentHostIndex = i;
foundGoodHost = true;

break;
} 
} 
if (!foundGoodHost) {
for (i = 0; i < this.currentHostIndex; i++) {
if (!blackList.containsKey(configuredHosts.get(i))) {
this.currentHostIndex = i;
foundGoodHost = true;

break;
} 
} 
}
if (!foundGoodHost) {
blackList = proxy.getGlobalBlacklist();

try {
Thread.sleep(250L);
} catch (InterruptedException e) {}

continue;
} 
} 

String hostPortSpec = configuredHosts.get(this.currentHostIndex);

ConnectionImpl conn = liveConnections.get(hostPortSpec);

if (conn == null) {
try {
conn = proxy.createConnectionForHost(hostPortSpec);
} catch (SQLException sqlEx) {
ex = sqlEx;

if (sqlEx instanceof CommunicationsException || "08S01".equals(sqlEx.getSQLState())) {

proxy.addToGlobalBlacklist(hostPortSpec);

try {
Thread.sleep(250L);
} catch (InterruptedException e) {}

continue;
} 

throw sqlEx;
} 
}

return conn;
} 

if (ex != null) {
throw ex;
}

return null;
}
}

