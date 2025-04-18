package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConnectionGroup
{
private String groupName;
private long connections = 0L;
private long activeConnections = 0L;
private HashMap<Long, LoadBalancingConnectionProxy> connectionProxies = new HashMap<Long, LoadBalancingConnectionProxy>();
private Set<String> hostList = new HashSet<String>();
private boolean isInitialized = false;
private long closedProxyTotalPhysicalConnections = 0L;
private long closedProxyTotalTransactions = 0L;
private int activeHosts = 0;
private Set<String> closedHosts = new HashSet<String>();

ConnectionGroup(String groupName) {
this.groupName = groupName;
}

public long registerConnectionProxy(LoadBalancingConnectionProxy proxy, List<String> localHostList) {
long currentConnectionId;
synchronized (this) {
if (!this.isInitialized) {
this.hostList.addAll(localHostList);
this.isInitialized = true;
this.activeHosts = localHostList.size();
} 
currentConnectionId = ++this.connections;
this.connectionProxies.put(Long.valueOf(currentConnectionId), proxy);
} 
this.activeConnections++;

return currentConnectionId;
}

public String getGroupName() {
return this.groupName;
}

public Collection<String> getInitialHosts() {
return this.hostList;
}

public int getActiveHostCount() {
return this.activeHosts;
}

public Collection<String> getClosedHosts() {
return this.closedHosts;
}

public long getTotalLogicalConnectionCount() {
return this.connections;
}

public long getActiveLogicalConnectionCount() {
return this.activeConnections;
}

public long getActivePhysicalConnectionCount() {
long result = 0L;
Map<Long, LoadBalancingConnectionProxy> proxyMap = new HashMap<Long, LoadBalancingConnectionProxy>();
synchronized (this.connectionProxies) {
proxyMap.putAll(this.connectionProxies);
} 
Iterator<Map.Entry<Long, LoadBalancingConnectionProxy>> i = proxyMap.entrySet().iterator();
while (i.hasNext()) {
LoadBalancingConnectionProxy proxy = (LoadBalancingConnectionProxy)((Map.Entry)i.next()).getValue();
result += proxy.getActivePhysicalConnectionCount();
} 

return result;
}

public long getTotalPhysicalConnectionCount() {
long allConnections = this.closedProxyTotalPhysicalConnections;
Map<Long, LoadBalancingConnectionProxy> proxyMap = new HashMap<Long, LoadBalancingConnectionProxy>();
synchronized (this.connectionProxies) {
proxyMap.putAll(this.connectionProxies);
} 
Iterator<Map.Entry<Long, LoadBalancingConnectionProxy>> i = proxyMap.entrySet().iterator();
while (i.hasNext()) {
LoadBalancingConnectionProxy proxy = (LoadBalancingConnectionProxy)((Map.Entry)i.next()).getValue();
allConnections += proxy.getTotalPhysicalConnectionCount();
} 

return allConnections;
}

public long getTotalTransactionCount() {
long transactions = this.closedProxyTotalTransactions;
Map<Long, LoadBalancingConnectionProxy> proxyMap = new HashMap<Long, LoadBalancingConnectionProxy>();
synchronized (this.connectionProxies) {
proxyMap.putAll(this.connectionProxies);
} 
Iterator<Map.Entry<Long, LoadBalancingConnectionProxy>> i = proxyMap.entrySet().iterator();
while (i.hasNext()) {
LoadBalancingConnectionProxy proxy = (LoadBalancingConnectionProxy)((Map.Entry)i.next()).getValue();
transactions += proxy.getTransactionCount();
} 

return transactions;
}

public void closeConnectionProxy(LoadBalancingConnectionProxy proxy) {
this.activeConnections--;
this.connectionProxies.remove(Long.valueOf(proxy.getConnectionGroupProxyID()));
this.closedProxyTotalPhysicalConnections += proxy.getTotalPhysicalConnectionCount();
this.closedProxyTotalTransactions += proxy.getTransactionCount();
}

public void removeHost(String host) throws SQLException {
removeHost(host, false);
}

public void removeHost(String host, boolean killExistingConnections) throws SQLException {
removeHost(host, killExistingConnections, true);
}

public synchronized void removeHost(String host, boolean killExistingConnections, boolean waitForGracefulFailover) throws SQLException {
if (this.activeHosts == 1) {
throw SQLError.createSQLException("Cannot remove host, only one configured host active.", null);
}

if (this.hostList.remove(host)) {
this.activeHosts--;
} else {
throw SQLError.createSQLException("Host is not configured: " + host, null);
} 

if (killExistingConnections) {

Map<Long, LoadBalancingConnectionProxy> proxyMap = new HashMap<Long, LoadBalancingConnectionProxy>();
synchronized (this.connectionProxies) {
proxyMap.putAll(this.connectionProxies);
} 

Iterator<Map.Entry<Long, LoadBalancingConnectionProxy>> i = proxyMap.entrySet().iterator();
while (i.hasNext()) {
LoadBalancingConnectionProxy proxy = (LoadBalancingConnectionProxy)((Map.Entry)i.next()).getValue();
if (waitForGracefulFailover) {
proxy.removeHostWhenNotInUse(host); continue;
} 
proxy.removeHost(host);
} 
} 

this.closedHosts.add(host);
}

public void addHost(String host) {
addHost(host, false);
}

public void addHost(String host, boolean forExisting) {
synchronized (this) {
if (this.hostList.add(host)) {
this.activeHosts++;
}
} 

if (!forExisting) {
return;
}

Map<Long, LoadBalancingConnectionProxy> proxyMap = new HashMap<Long, LoadBalancingConnectionProxy>();
synchronized (this.connectionProxies) {
proxyMap.putAll(this.connectionProxies);
} 

Iterator<Map.Entry<Long, LoadBalancingConnectionProxy>> i = proxyMap.entrySet().iterator();
while (i.hasNext()) {
LoadBalancingConnectionProxy proxy = (LoadBalancingConnectionProxy)((Map.Entry)i.next()).getValue();
proxy.addHost(host);
} 
}
}

