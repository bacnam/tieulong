package com.mchange.v2.c3p0.test;

import com.mchange.v2.c3p0.C3P0ProxyConnection;
import com.mchange.v2.c3p0.C3P0ProxyStatement;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.lang.reflect.Method;

public final class RawConnectionOpTest
{
public static void main(String[] argv) {
ComboPooledDataSource cpds = null;

try {
String jdbc_url = null;
String username = null;
String password = null;

if (argv.length == 3) {

jdbc_url = argv[0];
username = argv[1];
password = argv[2];
}
else if (argv.length == 1) {

jdbc_url = argv[0];
username = null;
password = null;
} else {

usage();
} 
if (!jdbc_url.startsWith("jdbc:")) {
usage();
}
cpds = new ComboPooledDataSource();
cpds.setJdbcUrl(jdbc_url);
cpds.setUser(username);
cpds.setPassword(password);
cpds.setMaxPoolSize(10);

C3P0ProxyConnection conn = (C3P0ProxyConnection)cpds.getConnection();
Method toStringMethod = Object.class.getMethod("toString", new Class[0]);
Method identityHashCodeMethod = System.class.getMethod("identityHashCode", new Class[] { Object.class });
System.out.println("rawConnection.toString() -> " + conn.rawConnectionOperation(toStringMethod, C3P0ProxyConnection.RAW_CONNECTION, new Object[0]));

Integer ihc = (Integer)conn.rawConnectionOperation(identityHashCodeMethod, null, new Object[] { C3P0ProxyConnection.RAW_CONNECTION });
System.out.println("System.identityHashCode( rawConnection ) -> " + Integer.toHexString(ihc.intValue()));

C3P0ProxyStatement stmt = (C3P0ProxyStatement)conn.createStatement();
System.out.println("rawStatement.toString() -> " + stmt.rawStatementOperation(toStringMethod, C3P0ProxyStatement.RAW_STATEMENT, new Object[0]));

Integer ihc2 = (Integer)stmt.rawStatementOperation(identityHashCodeMethod, null, new Object[] { C3P0ProxyStatement.RAW_STATEMENT });
System.out.println("System.identityHashCode( rawStatement ) -> " + Integer.toHexString(ihc2.intValue()));

conn.close();

for (int i = 0; i < 10; i++)
{
C3P0ProxyConnection check = null;

}

}
catch (Exception e) {
e.printStackTrace();
} finally {
if (cpds != null) cpds.close(); 
} 
}

private static void usage() {
System.err.println("java " + RawConnectionOpTest.class.getName() + " \\");
System.err.println("\t<jdbc_driver_class> \\");
System.err.println("\t<jdbc_url> [<username> <password>]");
System.exit(-1);
}
}

