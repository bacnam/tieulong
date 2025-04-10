package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class BestResponseTimeBalanceStrategy
implements BalanceStrategy
{
public void destroy() {}

public void init(Connection conn, Properties props) throws SQLException {}

public ConnectionImpl pickConnection(LoadBalancingConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
Map<String, Long> blackList = proxy.getGlobalBlacklist();

SQLException ex = null;

for (int attempts = 0; attempts < numRetries; ) {
long minResponseTime = Long.MAX_VALUE;

int bestHostIndex = 0;

if (blackList.size() == configuredHosts.size()) {
blackList = proxy.getGlobalBlacklist();
}

for (int i = 0; i < responseTimes.length; i++) {
long candidateResponseTime = responseTimes[i];

if (candidateResponseTime < minResponseTime && !blackList.containsKey(configuredHosts.get(i))) {

if (candidateResponseTime == 0L) {
bestHostIndex = i;

break;
} 

bestHostIndex = i;
minResponseTime = candidateResponseTime;
} 
} 

String bestHost = configuredHosts.get(bestHostIndex);

ConnectionImpl conn = liveConnections.get(bestHost);

if (conn == null) {
try {
conn = proxy.createConnectionForHost(bestHost);
} catch (SQLException sqlEx) {
ex = sqlEx;

if (proxy.shouldExceptionTriggerFailover(sqlEx)) {
proxy.addToGlobalBlacklist(bestHost);
blackList.put(bestHost, null);

if (blackList.size() == configuredHosts.size()) {
attempts++;
try {
Thread.sleep(250L);
} catch (InterruptedException e) {}

blackList = proxy.getGlobalBlacklist();
} 

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

