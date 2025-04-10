package com.jolbox.bonecp;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

public class ConnectionReleaseHelperThread
implements Runnable
{
private BlockingQueue<ConnectionHandle> queue;
private BoneCP pool;

public ConnectionReleaseHelperThread(BlockingQueue<ConnectionHandle> queue, BoneCP pool) {
this.queue = queue;
this.pool = pool;
}

public void run() {
boolean interrupted = false;
while (!interrupted) {
try {
ConnectionHandle connection = this.queue.take();
this.pool.internalReleaseConnection(connection);
} catch (SQLException e) {
interrupted = true;
} catch (InterruptedException e) {
if (this.pool.poolShuttingDown) {
ConnectionHandle connection;

while ((connection = this.queue.poll()) != null) {
try {
this.pool.internalReleaseConnection(connection);
} catch (Exception e1) {}
} 
} 

interrupted = true;
} 
} 
}
}

