package com.mchange.v2.c3p0.test;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.ResultSetUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.mchange.v2.c3p0.DriverManagerDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public final class C3P0BenchmarkApp
{
static final String EMPTY_TABLE_CREATE = "CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))";
static final String EMPTY_TABLE_SELECT = "SELECT * FROM emptyyukyuk";
static final String EMPTY_TABLE_DROP = "DROP TABLE emptyyukyuk";
static final String EMPTY_TABLE_CONDITIONAL_SELECT = "SELECT * FROM emptyyukyuk where a = ?";
static final String N_ENTRY_TABLE_CREATE = "CREATE TABLE n_entryyukyuk (a INTEGER)";
static final String N_ENTRY_TABLE_INSERT = "INSERT INTO n_entryyukyuk VALUES ( ? )";
static final String N_ENTRY_TABLE_SELECT = "SELECT * FROM n_entryyukyuk";
static final String N_ENTRY_TABLE_DROP = "DROP TABLE n_entryyukyuk";
static final int NUM_ITERATIONS = 2000;

public static void main(String[] argv) {
DriverManagerDataSource driverManagerDataSource;
ComboPooledDataSource comboPooledDataSource;
if (argv.length > 0) {

System.err.println(C3P0BenchmarkApp.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");

return;
} 

DataSource ds_unpooled = null;
DataSource ds_pooled = null;

try {
driverManagerDataSource = new DriverManagerDataSource();

ComboPooledDataSource cpds = new ComboPooledDataSource();

comboPooledDataSource = cpds;

comboPooledDataSource.getParentLogger().info("Pooled DataSource created.");

create((DataSource)comboPooledDataSource);

System.out.println("Please wait. Tests can be very slow.");
List<ConnectionAcquisitionTest> l = new ArrayList();
l.add(new ConnectionAcquisitionTest());
l.add(new StatementCreateTest());
l.add(new StatementEmptyTableSelectTest());

l.add(new PreparedStatementEmptyTableSelectTest());
l.add(new PreparedStatementAcquireTest());
l.add(new ResultSetReadTest());
l.add(new FiveThreadPSQueryTestTest());
for (int i = 0, len = l.size(); i < len; i++) {
((Test)l.get(i)).perform((DataSource)driverManagerDataSource, (DataSource)comboPooledDataSource, 2000);
}
} catch (Throwable t) {

System.err.print("Aborting tests on Throwable -- ");
t.printStackTrace();

if (t instanceof Error) {
throw (Error)t;
}
} finally {

try {
drop((DataSource)comboPooledDataSource);
} catch (Exception e) {
e.printStackTrace();
}  try {
DataSources.destroy((DataSource)comboPooledDataSource);
} catch (Exception e) {
e.printStackTrace();
}  try {
DataSources.destroy((DataSource)driverManagerDataSource);
} catch (Exception e) {
e.printStackTrace();
} 
} 
}

static void create(DataSource ds) throws SQLException {
System.err.println("Creating test schema.");
Connection con = null;
PreparedStatement ps1 = null;
PreparedStatement ps2 = null;
PreparedStatement ps3 = null;

try {
con = ds.getConnection();
ps1 = con.prepareStatement("CREATE TABLE emptyyukyuk (a varchar(8), b varchar(8))");
ps2 = con.prepareStatement("CREATE TABLE n_entryyukyuk (a INTEGER)");
ps3 = con.prepareStatement("INSERT INTO n_entryyukyuk VALUES ( ? )");

ps1.executeUpdate();
ps2.executeUpdate();

for (int i = 0; i < 2000; i++) {

ps3.setInt(1, i);
ps3.executeUpdate();
System.err.print('.');
} 
System.err.println();
System.err.println("Test schema created.");
}
finally {

StatementUtils.attemptClose(ps1);
StatementUtils.attemptClose(ps2);
StatementUtils.attemptClose(ps3);
ConnectionUtils.attemptClose(con);
} 
}

static void drop(DataSource ds) throws SQLException {
Connection con = null;
PreparedStatement ps1 = null;
PreparedStatement ps2 = null;

try {
con = ds.getConnection();
ps1 = con.prepareStatement("DROP TABLE emptyyukyuk");
ps2 = con.prepareStatement("DROP TABLE n_entryyukyuk");

ps1.executeUpdate();
ps2.executeUpdate();

System.err.println("Test schema dropped.");
}
finally {

StatementUtils.attemptClose(ps1);
StatementUtils.attemptClose(ps2);
ConnectionUtils.attemptClose(con);
} 
}

static abstract class Test
{
String name;

Test(String name) {
this.name = name;
}

public void perform(DataSource unpooled, DataSource pooled, int iterations) throws Exception {
double msecs_unpooled = test(unpooled, iterations) / iterations;
double msecs_pooled = test(pooled, iterations) / iterations;
System.out.println(this.name + " [ " + iterations + " iterations ]:");
System.out.println("\tunpooled: " + msecs_unpooled + " msecs");
System.out.println("\t  pooled: " + msecs_pooled + " msecs");
System.out.println("\tspeed-up factor: " + (msecs_unpooled / msecs_pooled) + " times");
System.out.println("\tspeed-up absolute: " + (msecs_unpooled - msecs_pooled) + " msecs");

System.out.println();
}

protected abstract long test(DataSource param1DataSource, int param1Int) throws Exception;
}

static class ConnectionAcquisitionTest
extends Test
{
ConnectionAcquisitionTest() {
super("Connection Acquisition and Cleanup");
}

protected long test(DataSource ds, int n) throws Exception {
long start = System.currentTimeMillis();
for (int i = 0; i < n; i++)
{
Connection con = null;
}

long end = System.currentTimeMillis();
return end - start;
}
}

static class StatementCreateTest
extends Test {
StatementCreateTest() {
super("Statement Creation and Cleanup");
}

protected long test(DataSource ds, int n) throws SQLException {
Connection con = null;

try {
con = ds.getConnection();
return test(con, n);
} finally {

ConnectionUtils.attemptClose(con);
} 
}

long test(Connection con, int n) throws SQLException {
Statement stmt = null;
long start = System.currentTimeMillis();
for (int i = 0; i < n; i++) {

try { stmt = con.createStatement();

StatementUtils.attemptClose(stmt); } finally { StatementUtils.attemptClose(stmt); }

}  long end = System.currentTimeMillis();
return end - start;
}
}

static class StatementEmptyTableSelectTest
extends Test
{
StatementEmptyTableSelectTest() {
super("Empty Table Statement Select (on a single Statement)");
}

protected long test(DataSource ds, int n) throws SQLException {
Connection con = null;
Statement stmt = null;

try {
con = ds.getConnection();
stmt = con.createStatement();

return test(stmt, n);
}
finally {

StatementUtils.attemptClose(stmt);
ConnectionUtils.attemptClose(con);
} 
}

long test(Statement stmt, int n) throws SQLException {
long start = System.currentTimeMillis();
for (int i = 0; i < n; i++)
stmt.executeQuery("SELECT * FROM emptyyukyuk").close(); 
long end = System.currentTimeMillis();
return end - start;
}
}

static class DataBaseMetaDataListNonexistentTablesTest
extends Test {
DataBaseMetaDataListNonexistentTablesTest() {
super("DataBaseMetaDataListNonexistentTablesTest");
}

protected long test(DataSource ds, int n) throws SQLException {
Connection con = null;
Statement stmt = null;

try {
con = ds.getConnection();
return test(con, n);
}
finally {

StatementUtils.attemptClose(stmt);
ConnectionUtils.attemptClose(con);
} 
}

long test(Connection con, int n) throws SQLException {
ResultSet rs = null;

try {
long start = System.currentTimeMillis();
for (int i = 0; i < n; i++) {
rs = con.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
} 

long end = System.currentTimeMillis();
return end - start;
} finally {

ResultSetUtils.attemptClose(rs);
} 
}
}

static class PreparedStatementAcquireTest extends Test {
PreparedStatementAcquireTest() {
super("Acquire and Cleanup a PreparedStatement (same statement, many times)");
}

protected long test(DataSource ds, int n) throws SQLException {
Connection con = null;
PreparedStatement pstmt = null;

try {
con = ds.getConnection();
long start = System.currentTimeMillis();
for (int i = 0; i < n; i++) {

try { pstmt = con.prepareStatement("SELECT * FROM emptyyukyuk where a = ?");

StatementUtils.attemptClose(pstmt); } finally { StatementUtils.attemptClose(pstmt); }

}  long end = System.currentTimeMillis();
return end - start;
} finally {

ConnectionUtils.attemptClose(con);
} 
}
}

static class PreparedStatementEmptyTableSelectTest extends Test {
PreparedStatementEmptyTableSelectTest() {
super("Empty Table PreparedStatement Select (on a single PreparedStatement)");
}

protected long test(DataSource ds, int n) throws SQLException {
Connection con = null;
PreparedStatement pstmt = null;

try {
con = ds.getConnection();
pstmt = con.prepareStatement("SELECT * FROM emptyyukyuk");

return test(pstmt, n);
}
finally {

StatementUtils.attemptClose(pstmt);
ConnectionUtils.attemptClose(con);
} 
}

long test(PreparedStatement pstmt, int n) throws SQLException {
long start = System.currentTimeMillis();
for (int i = 0; i < n; i++)
pstmt.executeQuery().close(); 
long end = System.currentTimeMillis();
return end - start;
}
}

static class ResultSetReadTest
extends Test {
ResultSetReadTest() {
super("Reading one row / one entry from a result set");
}

protected long test(DataSource ds, int n) throws SQLException {
if (n > 10000) {
throw new IllegalArgumentException("10K max.");
}

Connection con = null;
PreparedStatement pstmt = null;
ResultSet rs = null;

try {
con = ds.getConnection();
pstmt = con.prepareStatement("SELECT * FROM n_entryyukyuk");
rs = pstmt.executeQuery();

long start = System.currentTimeMillis();
for (int i = 0; i < n; i++) {

if (!rs.next())
System.err.println("huh?"); 
rs.getInt(1);
} 
long end = System.currentTimeMillis();
return end - start;
}
finally {

ResultSetUtils.attemptClose(rs);
StatementUtils.attemptClose(pstmt);
ConnectionUtils.attemptClose(con);
} 
}
}

static class FiveThreadPSQueryTestTest
extends Test
{
FiveThreadPSQueryTestTest() {
super("Five threads getting a connection, executing a query, " + System.getProperty("line.separator") + "and retrieving results concurrently via a prepared statement (in a transaction).");
}

protected long test(final DataSource ds, final int n) throws Exception {
class QueryThread
extends Thread
{
QueryThread(int num) {
super("QueryThread-" + num);
}

public void run() {
Connection con = null;
PreparedStatement pstmt = null;
ResultSet rs = null;

for (int i = 0; i < n / 5; i++) {

try { con = ds.getConnection();

con.setAutoCommit(false);

pstmt = con.prepareStatement("SELECT * FROM emptyyukyuk where a = ?");

pstmt.setString(1, "boo");
rs = pstmt.executeQuery();
while (rs.next()) {
System.err.println("Huh?? Empty table has values?");
}

con.commit();

ResultSetUtils.attemptClose(rs);
StatementUtils.attemptClose(pstmt);
ConnectionUtils.attemptClose(con);
con = null; } catch (Exception e) { System.err.print("FiveThreadPSQueryTestTest exception -- "); e.printStackTrace(); try { if (con != null) con.rollback();  } catch (SQLException e2) { System.err.print("Rollback on exception failed! -- "); e2.printStackTrace(); }  } finally { ResultSetUtils.attemptClose(rs); StatementUtils.attemptClose(pstmt); ConnectionUtils.attemptClose(con); con = null; }

} 
}
};

long start = System.currentTimeMillis();

Thread[] ts = new Thread[5]; int i;
for (i = 0; i < 5; i++) {

ts[i] = new QueryThread(i);
ts[i].start();
} 
for (i = 0; i < 5; i++) {
ts[i].join();
}
return System.currentTimeMillis() - start;
}
}
}

