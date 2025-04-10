package com.mysql.jdbc;

import java.lang.ref.Reference;

public class AbandonedConnectionCleanupThread
extends Thread
{
private static boolean running = true;
private static Thread threadRef = null;

public AbandonedConnectionCleanupThread() {
super("Abandoned connection cleanup thread");
}

public void run() {
threadRef = this;
threadRef.setContextClassLoader(null);
while (running) {
try {
Reference<? extends ConnectionImpl> ref = NonRegisteringDriver.refQueue.remove(100L);
if (ref != null) {
try {
((NonRegisteringDriver.ConnectionPhantomReference)ref).cleanup();
} finally {
NonRegisteringDriver.connectionPhantomRefs.remove(ref);
}

}
} catch (Exception ex) {}
} 
}

public static void shutdown() throws InterruptedException {
running = false;
if (threadRef != null) {
threadRef.interrupt();
threadRef.join();
threadRef = null;
} 
}
}

