package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class RandomBalanceStrategy
implements BalanceStrategy
{
public void destroy() {}

public void init(Connection conn, Properties props) throws SQLException {}

public ConnectionImpl pickConnection(LoadBalancingConnectionProxy proxy, List<String> configuredHosts, Map<String, ConnectionImpl> liveConnections, long[] responseTimes, int numRetries) throws SQLException {
int numHosts = configuredHosts.size();

SQLException ex = null;

List<String> whiteList = new ArrayList<String>(numHosts);
whiteList.addAll(configuredHosts);

Map<String, Long> blackList = proxy.getGlobalBlacklist();

whiteList.removeAll(blackList.keySet());

Map<String, Integer> whiteListMap = getArrayIndexMap(whiteList);

for (int attempts = 0; attempts < numRetries; ) {
int random = (int)Math.floor(Math.random() * whiteList.size());
if (whiteList.size() == 0) {
throw SQLError.createSQLException("No hosts configured", null);
}

String hostPortSpec = whiteList.get(random);

ConnectionImpl conn = liveConnections.get(hostPortSpec);

if (conn == null) {
try {
conn = proxy.createConnectionForHost(hostPortSpec);
} catch (SQLException sqlEx) {
ex = sqlEx;

if (proxy.shouldExceptionTriggerFailover(sqlEx)) {

Integer whiteListIndex = whiteListMap.get(hostPortSpec);

if (whiteListIndex != null) {
whiteList.remove(whiteListIndex.intValue());
whiteListMap = getArrayIndexMap(whiteList);
} 
proxy.addToGlobalBlacklist(hostPortSpec);

if (whiteList.size() == 0) {
attempts++;
try {
Thread.sleep(250L);
} catch (InterruptedException e) {}

whiteListMap = new HashMap<String, Integer>(numHosts);
whiteList.addAll(configuredHosts);
blackList = proxy.getGlobalBlacklist();

whiteList.removeAll(blackList.keySet());
whiteListMap = getArrayIndexMap(whiteList);
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

private Map<String, Integer> getArrayIndexMap(List<String> l) {
Map<String, Integer> m = new HashMap<String, Integer>(l.size());
for (int i = 0; i < l.size(); i++) {
m.put(l.get(i), Integer.valueOf(i));
}
return m;
}
}

