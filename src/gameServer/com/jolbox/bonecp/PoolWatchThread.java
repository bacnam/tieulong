package com.jolbox.bonecp;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolWatchThread
implements Runnable
{
private ConnectionPartition partition;
private BoneCP pool;
private boolean signalled;
private long acquireRetryDelayInMs = 1000L;

private boolean lazyInit;

private int poolAvailabilityThreshold;

private static Logger logger = LoggerFactory.getLogger(PoolWatchThread.class);

public PoolWatchThread(ConnectionPartition connectionPartition, BoneCP pool) {
this.partition = connectionPartition;
this.pool = pool;
this.lazyInit = this.pool.getConfig().isLazyInit();
this.acquireRetryDelayInMs = this.pool.getConfig().getAcquireRetryDelayInMs();
this.poolAvailabilityThreshold = this.pool.getConfig().getPoolAvailabilityThreshold();
}

public void run() {
while (!this.signalled) {
int maxNewConnections = 0;

try {
if (this.lazyInit) {
this.lazyInit = false;
this.partition.getPoolWatchThreadSignalQueue().take();
} 

maxNewConnections = this.partition.getMaxConnections() - this.partition.getCreatedConnections();

while (maxNewConnections == 0 || this.partition.getAvailableConnections() * 100 / this.partition.getMaxConnections() > this.poolAvailabilityThreshold) {
if (maxNewConnections == 0) {
this.partition.setUnableToCreateMoreTransactions(true);
}
this.partition.getPoolWatchThreadSignalQueue().take();
maxNewConnections = this.partition.getMaxConnections() - this.partition.getCreatedConnections();
} 

if (maxNewConnections > 0 && !this.lazyInit) {
fillConnections(Math.min(maxNewConnections, this.partition.getAcquireIncrement()));

}
}
catch (InterruptedException e) {
return;
} 
} 
}

private void fillConnections(int connectionsToCreate) throws InterruptedException {
try {
for (int i = 0; i < connectionsToCreate; i++) {
this.partition.addFreeConnection(new ConnectionHandle(this.partition.getUrl(), this.partition.getUsername(), this.partition.getPassword(), this.pool));
}
} catch (SQLException e) {
logger.error("Error in trying to obtain a connection. Retrying in " + this.acquireRetryDelayInMs + "ms", e);
Thread.sleep(this.acquireRetryDelayInMs);
} 
}
}

