package com.mchange.v2.c3p0.test;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
import com.mchange.v2.naming.ReferenceableUtils;
import com.mchange.v2.ser.SerializableUtils;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;

public final class TestRefSerStuff
{
static void create(DataSource ds) throws SQLException {
Connection con = null;
Statement stmt = null;

try {
con = ds.getConnection();
stmt = con.createStatement();
stmt.executeUpdate("CREATE TABLE TRSS_TABLE ( a_col VARCHAR(16) )");
}
finally {

StatementUtils.attemptClose(stmt);
ConnectionUtils.attemptClose(con);
} 
}

static void drop(DataSource ds) throws SQLException {
Connection con = null;
Statement stmt = null;

try {
con = ds.getConnection();
stmt = con.createStatement();
stmt.executeUpdate("DROP TABLE TRSS_TABLE");
}
finally {

StatementUtils.attemptClose(stmt);
ConnectionUtils.attemptClose(con);
} 
}

static void doSomething(DataSource ds) throws SQLException {
Connection con = null;
Statement stmt = null;

try {
con = ds.getConnection();
stmt = con.createStatement();
int i = stmt.executeUpdate("INSERT INTO TRSS_TABLE VALUES ('" + System.currentTimeMillis() + "')");

if (i != 1) {
throw new SQLException("Insert failed somehow strange!");
}
} finally {

StatementUtils.attemptClose(stmt);
ConnectionUtils.attemptClose(con);
} 
}

static void doTest(DataSource checkMe) throws Exception {
doSomething(checkMe);
System.err.println("\tcreated:   " + checkMe);
DataSource afterSer = (DataSource)SerializableUtils.testSerializeDeserialize(checkMe);
doSomething(afterSer);
System.err.println("\tafter ser: " + afterSer);
Reference ref = ((Referenceable)checkMe).getReference();

DataSource afterRef = (DataSource)ReferenceableUtils.referenceToObject(ref, null, null, null);

doSomething(afterRef);
System.err.println("\tafter ref: " + afterRef);
}

public static void main(String[] argv) {
if (argv.length > 0) {

System.err.println(TestRefSerStuff.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");

return;
} 

try {
DriverManagerDataSource dmds = new DriverManagerDataSource();

try {
drop((DataSource)dmds);
} catch (Exception e) {}

create((DataSource)dmds);

System.err.println("DriverManagerDataSource:");
doTest((DataSource)dmds);

WrapperConnectionPoolDataSource wcpds = new WrapperConnectionPoolDataSource();
wcpds.setNestedDataSource((DataSource)dmds);
PoolBackedDataSource pbds = new PoolBackedDataSource();
pbds.setConnectionPoolDataSource((ConnectionPoolDataSource)wcpds);

System.err.println("PoolBackedDataSource:");
doTest((DataSource)pbds);

ComboPooledDataSource cpds = new ComboPooledDataSource();
doTest((DataSource)cpds);
}
catch (Exception e) {
e.printStackTrace();
} 
}
}

