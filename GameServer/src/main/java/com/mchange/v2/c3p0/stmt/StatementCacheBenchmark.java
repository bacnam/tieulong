package com.mchange.v2.c3p0.stmt;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.c3p0.DriverManagerDataSourceFactory;
import com.mchange.v2.c3p0.PoolBackedDataSourceFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

public final class StatementCacheBenchmark
{
static final String EMPTY_TABLE_CREATE = "CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))";
static final String EMPTY_TABLE_SELECT = "SELECT * FROM emptyyukyuk";
static final String EMPTY_TABLE_DROP = "DROP TABLE emptyyukyuk";
static final String EMPTY_TABLE_CONDITIONAL_SELECT = "SELECT * FROM emptyyukyuk where a = ?";
static final int NUM_ITERATIONS = 2000;

public static void main(String[] argv) {
DataSource ds_unpooled = null;
DataSource ds_pooled = null;

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
ds_unpooled = DriverManagerDataSourceFactory.create(jdbc_url, username, password);
ds_pooled = PoolBackedDataSourceFactory.create(jdbc_url, username, password, 5, 20, 5, 0, 100);

create(ds_pooled);

perform(ds_pooled, "pooled");
perform(ds_unpooled, "unpooled");
}
catch (Exception e) {
e.printStackTrace();
} finally {
try {
drop(ds_pooled);
} catch (Exception e) {
e.printStackTrace();
} 
} 
}

private static void perform(DataSource ds, String name) throws SQLException {
Connection c = null;
PreparedStatement ps = null;

try {
c = ds.getConnection();
long start = System.currentTimeMillis();
for (int i = 0; i < 2000; i++) {

PreparedStatement test = c.prepareStatement("SELECT * FROM emptyyukyuk where a = ?");

test.close();
} 
long end = System.currentTimeMillis();
System.err.println(name + " --> " + ((float)(end - start) / 2000.0F) + " [" + 'ߐ' + " iterations]");

}
finally {

StatementUtils.attemptClose(ps);
ConnectionUtils.attemptClose(c);
} 
}

private static void usage() {
System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + StatementCacheBenchmark.class.getName() + " <jdbc_url> [<username> <password>]");

System.exit(-1);
}

static void create(DataSource ds) throws SQLException {
System.err.println("Creating test schema.");
Connection con = null;
PreparedStatement ps1 = null;

try {
con = ds.getConnection();
ps1 = con.prepareStatement("CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))");
ps1.executeUpdate();
System.err.println("Test schema created.");
}
finally {

StatementUtils.attemptClose(ps1);
ConnectionUtils.attemptClose(con);
} 
}

static void drop(DataSource ds) throws SQLException {
Connection con = null;
PreparedStatement ps1 = null;

try {
con = ds.getConnection();
ps1 = con.prepareStatement("DROP TABLE emptyyukyuk");
ps1.executeUpdate();
}
finally {

StatementUtils.attemptClose(ps1);
ConnectionUtils.attemptClose(con);
} 
System.err.println("Test schema dropped.");
}
}

