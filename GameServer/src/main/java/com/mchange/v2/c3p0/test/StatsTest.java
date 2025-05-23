package com.mchange.v2.c3p0.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Iterator;

public final class StatsTest
{
static void display(ComboPooledDataSource cpds) throws Exception {
System.err.println("numConnections: " + cpds.getNumConnections());
System.err.println("numBusyConnections: " + cpds.getNumBusyConnections());
System.err.println("numIdleConnections: " + cpds.getNumIdleConnections());
System.err.println("numUnclosedOrphanedConnections: " + cpds.getNumUnclosedOrphanedConnections());
System.err.println();
}

public static void main(String[] argv) {
try {
ComboPooledDataSource cpds = new ComboPooledDataSource();
cpds.setJdbcUrl(argv[0]);
cpds.setUser(argv[1]);
cpds.setPassword(argv[2]);
cpds.setMinPoolSize(5);
cpds.setAcquireIncrement(5);
cpds.setMaxPoolSize(20);

System.err.println("Initial...");
display(cpds);
Thread.sleep(2000L);

HashSet<Connection> hs = new HashSet();
for (int i = 0; i < 20; i++) {

Connection c = cpds.getConnection();
hs.add(c);
System.err.println("Adding (" + (i + 1) + ") " + c);
display(cpds);
Thread.sleep(1000L);
} 

int count = 0;
for (Iterator<Connection> ii = hs.iterator(); ii.hasNext(); ) {

Connection c = ii.next();
System.err.println("Removing " + ++count);
ii.remove(); try {
c.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
} catch (Exception e) {

System.err.println(e);
System.err.println();

continue;
} finally {
c.close();
}  Thread.sleep(2000L);
display(cpds);
} 

System.err.println("Closing data source, \"forcing\" garbage collection, and sleeping for 5 seconds...");
cpds.close();
System.gc();
System.err.println("Main Thread: Sleeping for five seconds!");
Thread.sleep(5000L);

System.err.println("Bye!");
}
catch (Exception e) {
e.printStackTrace();
} 
}
}

