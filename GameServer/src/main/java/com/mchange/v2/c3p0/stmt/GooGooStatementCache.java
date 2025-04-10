package com.mchange.v2.c3p0.stmt;

import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.async.AsynchronousRunner;
import com.mchange.v2.io.IndentedWriter;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.sql.SqlUtils;
import com.mchange.v2.util.ResourceClosedException;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public abstract class GooGooStatementCache
{
private static final MLogger logger = MLog.getLogger(GooGooStatementCache.class);

private static final int DESTROY_NEVER = 0;

private static final int DESTROY_IF_CHECKED_IN = 1;

private static final int DESTROY_IF_CHECKED_OUT = 2;

private static final int DESTROY_ALWAYS = 3;

private static final boolean CULL_ONLY_FROM_UNUSED_CONNECTIONS = false;

ConnectionStatementManager cxnStmtMgr;

HashMap stmtToKey = new HashMap<Object, Object>();

HashMap keyToKeyRec = new HashMap<Object, Object>();

HashSet checkedOut = new HashSet();

AsynchronousRunner blockingTaskAsyncRunner;

HashSet removalPending = new HashSet();

StatementDestructionManager destructo;

public GooGooStatementCache(AsynchronousRunner blockingTaskAsyncRunner, AsynchronousRunner deferredStatementDestroyer) {
this.blockingTaskAsyncRunner = blockingTaskAsyncRunner;
this.cxnStmtMgr = createConnectionStatementManager();
this.destructo = (deferredStatementDestroyer != null) ? new CautiousStatementDestructionManager(deferredStatementDestroyer) : new IncautiousStatementDestructionManager(blockingTaskAsyncRunner);
}

public synchronized int getNumStatements() {
return isClosed() ? -1 : countCachedStatements();
}
public synchronized int getNumStatementsCheckedOut() {
return isClosed() ? -1 : this.checkedOut.size();
}
public synchronized int getNumConnectionsWithCachedStatements() {
return isClosed() ? -1 : this.cxnStmtMgr.getNumConnectionsWithCachedStatements();
}

public synchronized String dumpStatementCacheStatus() {
if (isClosed()) {
return this + "status: Closed.";
}

StringWriter sw = new StringWriter(2048);
IndentedWriter iw = new IndentedWriter(sw);

try {
iw.print(this);
iw.println(" status:");
iw.upIndent();
iw.println("core stats:");
iw.upIndent();
iw.print("num cached statements: ");
iw.println(countCachedStatements());
iw.print("num cached statements in use: ");
iw.println(this.checkedOut.size());
iw.print("num connections with cached statements: ");
iw.println(this.cxnStmtMgr.getNumConnectionsWithCachedStatements());
iw.downIndent();
iw.println("cached statement dump:");
iw.upIndent();
for (Iterator<Connection> ii = this.cxnStmtMgr.connectionSet().iterator(); ii.hasNext(); ) {

Connection pcon = ii.next();
iw.print(pcon);
iw.println(':');
iw.upIndent();
for (Iterator jj = this.cxnStmtMgr.statementSet(pcon).iterator(); jj.hasNext();)
iw.println(jj.next()); 
iw.downIndent();
} 

iw.downIndent();
iw.downIndent();
return sw.toString();
}
catch (IOException e) {

if (logger.isLoggable(MLevel.SEVERE))
logger.log(MLevel.SEVERE, "Huh? We've seen an IOException writing to s StringWriter?!", e); 
return e.toString();
} 
}

public void waitMarkConnectionInUse(Connection physicalConnection) throws InterruptedException {
this.destructo.waitMarkConnectionInUse(physicalConnection);
} public boolean tryMarkConnectionInUse(Connection physicalConnection) { return this.destructo.tryMarkConnectionInUse(physicalConnection); }
public void unmarkConnectionInUse(Connection physicalConnection) { this.destructo.unmarkConnectionInUse(physicalConnection); } public Boolean inUse(Connection physicalConnection) {
return this.destructo.tvlInUse(physicalConnection);
}
public int getStatementDestroyerNumConnectionsInUse() { return this.destructo.getNumConnectionsInUse(); }
public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements() { return this.destructo.getNumConnectionsWithDeferredDestroyStatements(); } public int getStatementDestroyerNumDeferredDestroyStatements() {
return this.destructo.getNumDeferredDestroyStatements();
}

public synchronized Object checkoutStatement(Connection physicalConnection, Method stmtProducingMethod, Object[] args) throws SQLException, ResourceClosedException {
try {
Object out = null;

StatementCacheKey key = StatementCacheKey.find(physicalConnection, stmtProducingMethod, args);

LinkedList l = checkoutQueue(key);
if (l == null || l.isEmpty()) {

out = acquireStatement(physicalConnection, stmtProducingMethod, args);

if (prepareAssimilateNewStatement(physicalConnection)) {
assimilateNewCheckedOutStatement(key, physicalConnection, out);

}

}
else {

logger.finest(getClass().getName() + " ----> CACHE HIT");

out = l.get(0);
l.remove(0);
if (!this.checkedOut.add(out)) {
throw new RuntimeException("Internal inconsistency: Checking out a statement marked as already checked out!");
}

removeStatementFromDeathmarches(out, physicalConnection);
} 

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("checkoutStatement: " + statsString());
}

return out;
}
catch (NullPointerException npe) {

if (this.checkedOut == null) {

if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, "A client attempted to work with a closed Statement cache, provoking a NullPointerException. c3p0 recovers, but this should be rare.", npe);
}

throw new ResourceClosedException(npe);
} 

throw npe;
} 
}

public synchronized void checkinStatement(Object pstmt) throws SQLException {
if (this.checkedOut == null) {

this.destructo.synchronousDestroyStatement(pstmt);

return;
} 
if (!this.checkedOut.remove(pstmt)) {

if (!ourResource(pstmt)) {
this.destructo.uncheckedDestroyStatement(pstmt);
}

return;
} 

try {
refreshStatement((PreparedStatement)pstmt);
} catch (Exception e) {

if (logger.isLoggable(MLevel.INFO)) {
logger.log(MLevel.INFO, "Problem with checked-in Statement, discarding.", e);
}

this.checkedOut.add(pstmt);

removeStatement(pstmt, 3);

return;
} 
StatementCacheKey key = (StatementCacheKey)this.stmtToKey.get(pstmt);
if (key == null) {
throw new RuntimeException("Internal inconsistency: A checked-out statement has no key associated with it!");
}

LinkedList<Object> l = checkoutQueue(key);
l.add(pstmt);
addStatementToDeathmarches(pstmt, key.physicalConnection);

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("checkinStatement(): " + statsString());
}
}

public synchronized void checkinAll(Connection pcon) throws SQLException {
Set stmtSet = this.cxnStmtMgr.statementSet(pcon);
if (stmtSet != null)
{
for (Iterator ii = stmtSet.iterator(); ii.hasNext(); ) {

Object stmt = ii.next();
if (this.checkedOut.contains(stmt)) {
checkinStatement(stmt);
}
} 
}

if (logger.isLoggable(MLevel.FINEST)) {
logger.log(MLevel.FINEST, "checkinAll(): " + statsString());
}
}

public void closeAll(Connection pcon) throws SQLException {
if (!isClosed()) {

if (logger.isLoggable(MLevel.FINEST))
{
logger.log(MLevel.FINEST, "ENTER METHOD: closeAll( " + pcon + " )! -- num_connections: " + this.cxnStmtMgr.getNumConnectionsWithCachedStatements());
}

Set stmtSet = null;
synchronized (this) {

Set<?> cSet = this.cxnStmtMgr.statementSet(pcon);

if (cSet != null) {

stmtSet = new HashSet(cSet);

for (Iterator ii = stmtSet.iterator(); ii.hasNext(); ) {

Object stmt = ii.next();

removeStatement(stmt, 0);
} 
} 
} 

if (stmtSet != null)
{
for (Iterator ii = stmtSet.iterator(); ii.hasNext(); ) {

Object stmt = ii.next();
this.destructo.synchronousDestroyStatement(stmt);
} 
}

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("closeAll(): " + statsString());
}
} 
}

public synchronized void close() throws SQLException {
if (!isClosed()) {

for (Iterator ii = this.stmtToKey.keySet().iterator(); ii.hasNext();)
this.destructo.synchronousDestroyStatement(ii.next()); 
this.destructo.close();

this.cxnStmtMgr = null;
this.stmtToKey = null;
this.keyToKeyRec = null;
this.checkedOut = null;

}
else if (logger.isLoggable(MLevel.FINE)) {
logger.log(MLevel.FINE, this + ": duplicate call to close() [not harmful! -- debug only!]", new Exception("DUPLICATE CLOSE DEBUG STACK TRACE."));
} 
}

public synchronized boolean isClosed() {
return (this.cxnStmtMgr == null);
}

final int countCachedStatements() {
return this.stmtToKey.size();
}

private void assimilateNewCheckedOutStatement(StatementCacheKey key, Connection pConn, Object ps) {
this.stmtToKey.put(ps, key);
HashSet ks = keySet(key);
if (ks == null) {
this.keyToKeyRec.put(key, new KeyRec());
}
else {

if (logger.isLoggable(MLevel.INFO))
logger.info("Multiply-cached PreparedStatement: " + key.stmtText); 
if (logger.isLoggable(MLevel.FINE)) {
logger.fine("(The same statement has already been prepared by this Connection, and that other instance has not yet been closed, so the statement pool has to prepare a second PreparedStatement object rather than reusing the previously-cached Statement. The new Statement will be cached, in case you frequently need multiple copies of this Statement.)");
}
} 

keySet(key).add(ps);
this.cxnStmtMgr.addStatementForConnection(ps, pConn);

if (logger.isLoggable(MLevel.FINEST)) {
logger.finest("cxnStmtMgr.statementSet( " + pConn + " ).size(): " + this.cxnStmtMgr.statementSet(pConn).size());
}

this.checkedOut.add(ps);
}

private void removeStatement(Object ps, int destruction_policy) {
synchronized (this.removalPending) {

if (this.removalPending.contains(ps)) {
return;
}
this.removalPending.add(ps);
} 

StatementCacheKey sck = (StatementCacheKey)this.stmtToKey.remove(ps);
removeFromKeySet(sck, ps);
Connection pConn = sck.physicalConnection;

boolean checked_in = !this.checkedOut.contains(ps);

if (checked_in) {

removeStatementFromDeathmarches(ps, pConn);
removeFromCheckoutQueue(sck, ps);
if ((destruction_policy & 0x1) != 0) {
this.destructo.deferredDestroyStatement(pConn, ps);
}
} else {

this.checkedOut.remove(ps);
if ((destruction_policy & 0x2) != 0) {
this.destructo.deferredDestroyStatement(pConn, ps);
}
} 

boolean check = this.cxnStmtMgr.removeStatementForConnection(ps, pConn);
if (!check)
{

if (logger.isLoggable(MLevel.WARNING)) {
logger.log(MLevel.WARNING, this + " removed a statement that apparently wasn't in a statement set!!!", new Exception("LOG STACK TRACE"));
}
}

synchronized (this.removalPending) {
this.removalPending.remove(ps);
} 
}

private Object acquireStatement(final Connection pConn, final Method stmtProducingMethod, final Object[] args) throws SQLException {
try {
final Object[] outHolder = new Object[1];
final SQLException[] exceptionHolder = new SQLException[1];

Runnable r = new StmtAcquireTask();
this.blockingTaskAsyncRunner.postRunnable(r);

while (outHolder[0] == null && exceptionHolder[0] == null)
wait(); 
if (exceptionHolder[0] != null) {
throw exceptionHolder[0];
}

Object out = outHolder[0];
return out;

}
catch (InterruptedException e) {
throw SqlUtils.toSQLException(e);
} 
}
private KeyRec keyRec(StatementCacheKey key) {
return (KeyRec)this.keyToKeyRec.get(key);
}

private HashSet keySet(StatementCacheKey key) {
KeyRec rec = keyRec(key);
return (rec == null) ? null : rec.allStmts;
}

private boolean removeFromKeySet(StatementCacheKey key, Object pstmt) {
HashSet stmtSet = keySet(key);
boolean out = stmtSet.remove(pstmt);
if (stmtSet.isEmpty() && checkoutQueue(key).isEmpty())
this.keyToKeyRec.remove(key); 
return out;
}

private LinkedList checkoutQueue(StatementCacheKey key) {
KeyRec rec = keyRec(key);
return (rec == null) ? null : rec.checkoutQueue;
}

private boolean removeFromCheckoutQueue(StatementCacheKey key, Object pstmt) {
LinkedList q = checkoutQueue(key);
boolean out = q.remove(pstmt);
if (q.isEmpty() && keySet(key).isEmpty())
this.keyToKeyRec.remove(key); 
return out;
}

private boolean ourResource(Object ps) {
return this.stmtToKey.keySet().contains(ps);
}

private void refreshStatement(PreparedStatement ps) throws Exception {
ps.clearParameters();
ps.clearBatch();
}

private void printStats() {
int total_size = countCachedStatements();
int checked_out_size = this.checkedOut.size();
int num_connections = this.cxnStmtMgr.getNumConnectionsWithCachedStatements();
int num_keys = this.keyToKeyRec.size();
System.err.print(getClass().getName() + " stats -- ");
System.err.print("total size: " + total_size);
System.err.print("; checked out: " + checked_out_size);
System.err.print("; num connections: " + num_connections);
System.err.println("; num keys: " + num_keys);
}

private String statsString() {
int total_size = countCachedStatements();
int checked_out_size = this.checkedOut.size();
int num_connections = this.cxnStmtMgr.getNumConnectionsWithCachedStatements();
int num_keys = this.keyToKeyRec.size();

StringBuffer sb = new StringBuffer(255);
sb.append(getClass().getName());
sb.append(" stats -- ");
sb.append("total size: ");
sb.append(total_size);
sb.append("; checked out: ");
sb.append(checked_out_size);
sb.append("; num connections: ");
sb.append(num_connections);
int in_use = this.destructo.countConnectionsInUse();
if (in_use >= 0) {

sb.append("; num connections in use: ");
sb.append(in_use);
} 
sb.append("; num keys: ");
sb.append(num_keys);
return sb.toString();
}
abstract ConnectionStatementManager createConnectionStatementManager();
abstract boolean prepareAssimilateNewStatement(Connection paramConnection);
abstract void addStatementToDeathmarches(Object paramObject, Connection paramConnection);
abstract void removeStatementFromDeathmarches(Object paramObject, Connection paramConnection);
private static class KeyRec { HashSet allStmts = new HashSet(); private KeyRec() {}
LinkedList checkoutQueue = new LinkedList(); }

protected class Deathmarch
{
TreeMap longsToStmts = new TreeMap<Object, Object>();
HashMap stmtsToLongs = new HashMap<Object, Object>();

long last_long = -1L;

public void deathmarchStatement(Object ps) {
assert Thread.holdsLock(GooGooStatementCache.this);

Long old = (Long)this.stmtsToLongs.get(ps);
if (old != null) {
throw new RuntimeException("Internal inconsistency: A statement is being double-deathmatched. no checked-out statements should be in a deathmarch already; no already checked-in statement should be deathmarched!");
}

Long youth = getNextLong();
this.stmtsToLongs.put(ps, youth);
this.longsToStmts.put(youth, ps);
}

public void undeathmarchStatement(Object ps) {
assert Thread.holdsLock(GooGooStatementCache.this);

Long old = (Long)this.stmtsToLongs.remove(ps);
if (old == null) {
throw new RuntimeException("Internal inconsistency: A (not new) checking-out statement is not in deathmarch.");
}
Object check = this.longsToStmts.remove(old);
if (old == null) {
throw new RuntimeException("Internal inconsistency: A (not new) checking-out statement is not in deathmarch.");
}
}

public boolean cullNext() {
assert Thread.holdsLock(GooGooStatementCache.this);

Object cullMeStmt = null;
StatementCacheKey sck = null;

if (!this.longsToStmts.isEmpty()) {

Long l = (Long)this.longsToStmts.firstKey();
cullMeStmt = this.longsToStmts.get(l);
} 

if (cullMeStmt == null) {
return false;
}

if (sck == null)
sck = (StatementCacheKey)GooGooStatementCache.this.stmtToKey.get(cullMeStmt); 
if (GooGooStatementCache.logger.isLoggable(MLevel.FINEST)) {
GooGooStatementCache.logger.finest("CULLING: " + sck.stmtText);
}

GooGooStatementCache.this.removeStatement(cullMeStmt, 3);
if (contains(cullMeStmt)) {
throw new RuntimeException("Inconsistency!!! Statement culled from deathmarch failed to be removed by removeStatement( ... )!");
}
return true;
}

public boolean contains(Object ps) {
return this.stmtsToLongs.keySet().contains(ps);
}
public int size() {
return this.longsToStmts.size();
}
private Long getNextLong() {
return new Long(++this.last_long);
}
}

protected static abstract class ConnectionStatementManager {
Map cxnToStmtSets = new HashMap<Object, Object>();

public int getNumConnectionsWithCachedStatements() {
return this.cxnToStmtSets.size();
}
public Set connectionSet() {
return this.cxnToStmtSets.keySet();
}
public Set statementSet(Connection pcon) {
return (Set)this.cxnToStmtSets.get(pcon);
}

public int getNumStatementsForConnection(Connection pcon) {
Set stmtSet = statementSet(pcon);
return (stmtSet == null) ? 0 : stmtSet.size();
}

public void addStatementForConnection(Object ps, Connection pcon) {
Set<Object> stmtSet = statementSet(pcon);
if (stmtSet == null) {

stmtSet = new HashSet();
this.cxnToStmtSets.put(pcon, stmtSet);
} 
stmtSet.add(ps);
}

public boolean removeStatementForConnection(Object ps, Connection pcon) {
boolean out;
Set stmtSet = statementSet(pcon);
if (stmtSet != null) {

out = stmtSet.remove(ps);
if (stmtSet.isEmpty()) {
this.cxnToStmtSets.remove(pcon);
}
} else {
out = false;
} 
return out;
}
}

protected static final class SimpleConnectionStatementManager
extends ConnectionStatementManager {}

protected final class DeathmarchConnectionStatementManager
extends ConnectionStatementManager
{
Map cxnsToDms = new HashMap<Object, Object>();

public void addStatementForConnection(Object ps, Connection pcon) {
super.addStatementForConnection(ps, pcon);
GooGooStatementCache.Deathmarch dm = (GooGooStatementCache.Deathmarch)this.cxnsToDms.get(pcon);
if (dm == null) {

dm = new GooGooStatementCache.Deathmarch();
this.cxnsToDms.put(pcon, dm);
} 
}

public boolean removeStatementForConnection(Object ps, Connection pcon) {
boolean out = super.removeStatementForConnection(ps, pcon);
if (out)
{
if (statementSet(pcon) == null)
this.cxnsToDms.remove(pcon); 
}
return out;
}

public GooGooStatementCache.Deathmarch getDeathmarch(Connection pcon) {
return (GooGooStatementCache.Deathmarch)this.cxnsToDms.get(pcon);
}
}

private abstract class StatementDestructionManager
{
AsynchronousRunner runner;

StatementDestructionManager(AsynchronousRunner runner) {
this.runner = runner;
}

abstract void waitMarkConnectionInUse(Connection param1Connection) throws InterruptedException;

abstract boolean tryMarkConnectionInUse(Connection param1Connection);

abstract void unmarkConnectionInUse(Connection param1Connection);

abstract void deferredDestroyStatement(Object param1Object1, Object param1Object2);

abstract int countConnectionsInUse();

abstract boolean knownInUse(Connection param1Connection);

abstract Boolean tvlInUse(Connection param1Connection);

abstract int getNumConnectionsInUse();

abstract int getNumConnectionsWithDeferredDestroyStatements();

abstract int getNumDeferredDestroyStatements();

abstract void close();

final void uncheckedDestroyStatement(final Object pstmt) {
class UncheckedStatementCloseTask
implements Runnable
{
public void run() {
StatementUtils.attemptClose((PreparedStatement)pstmt);
}
};
Runnable r = new UncheckedStatementCloseTask();

this.runner.postRunnable(r);
}

final void synchronousDestroyStatement(Object pstmt) {
StatementUtils.attemptClose((PreparedStatement)pstmt);
}
}

private final class IncautiousStatementDestructionManager
extends StatementDestructionManager
{
IncautiousStatementDestructionManager(AsynchronousRunner runner) {
super(runner);
} void waitMarkConnectionInUse(Connection physicalConnection) throws InterruptedException {}
boolean tryMarkConnectionInUse(Connection physicalConnection) {
return true;
} void unmarkConnectionInUse(Connection physicalConnection) {} void deferredDestroyStatement(Object parentConnection, Object pstmt) {
uncheckedDestroyStatement(pstmt);
}
void close() {}
int countConnectionsInUse() {
return -1;
}

boolean knownInUse(Connection pCon) {
return false;
} Boolean tvlInUse(Connection pCon) {
return null;
}
int getNumConnectionsInUse() { return -1; }
int getNumConnectionsWithDeferredDestroyStatements() { return -1; } int getNumDeferredDestroyStatements() {
return -1;
}
}

private final class CautiousStatementDestructionManager
extends StatementDestructionManager
{
HashSet inUseConnections = new HashSet();

HashMap connectionsToZombieStatementSets = new HashMap<Object, Object>();

AsynchronousRunner deferredStatementDestroyer;

boolean closed = false;

synchronized void close() {
this.closed = true;
}

CautiousStatementDestructionManager(AsynchronousRunner deferredStatementDestroyer) {
super(deferredStatementDestroyer);
this.deferredStatementDestroyer = deferredStatementDestroyer;
}

private String trace() {
Set keys = this.connectionsToZombieStatementSets.keySet();
int sum = 0;
for (Iterator ii = keys.iterator(); ii.hasNext(); ) {

Object con = ii.next();
Set stmts = (Set)this.connectionsToZombieStatementSets.get(con);
synchronized (stmts) {
sum += (stmts == null) ? 0 : stmts.size();
} 
}  return getClass().getName() + " [connections in use: " + this.inUseConnections.size() + "; connections with deferred statements: " + keys.size() + "; statements to destroy: " + sum + "]";
}

private void printAllStats() {
GooGooStatementCache.this.printStats();
System.err.println(trace());
}

synchronized void waitMarkConnectionInUse(Connection physicalConnection) throws InterruptedException {
if (!this.closed) {

Set stmts = statementsUnderDestruction(physicalConnection);
if (stmts != null) {

if (GooGooStatementCache.logger.isLoggable(MLevel.FINE))
{

GooGooStatementCache.logger.log(MLevel.FINE, "A connection is waiting to be accepted by the Statement cache because " + stmts.size() + " cached Statements are still being destroyed.");
}

while (!stmts.isEmpty())
wait(); 
} 
this.inUseConnections.add(physicalConnection);
} 
}

synchronized boolean tryMarkConnectionInUse(Connection physicalConnection) {
if (!this.closed) {

Set stmts = statementsUnderDestruction(physicalConnection);
if (stmts != null) {

int sz = stmts.size();
if (GooGooStatementCache.logger.isLoggable(MLevel.FINE))
{
GooGooStatementCache.logger.log(MLevel.FINE, "A connection could not be accepted by the Statement cache because " + sz + " cached Statements are still being destroyed.");
}

return false;
} 

this.inUseConnections.add(physicalConnection);
return true;
} 

return true;
}

synchronized void unmarkConnectionInUse(Connection physicalConnection) {
boolean unmarked = this.inUseConnections.remove(physicalConnection);

Set zombieStatements = (Set)this.connectionsToZombieStatementSets.get(physicalConnection);

if (zombieStatements != null)
{

destroyAllTrackedStatements(physicalConnection);
}
}

synchronized void deferredDestroyStatement(Object parentConnection, Object pstmt) {
if (!this.closed) {

if (this.inUseConnections.contains(parentConnection)) {

Set<?> s = (Set)this.connectionsToZombieStatementSets.get(parentConnection);
if (s == null) {

s = Collections.synchronizedSet(new HashSet());
this.connectionsToZombieStatementSets.put(parentConnection, s);
} 
s.add(pstmt);
}
else {

uncheckedDestroyStatement(pstmt);
} 
} else {

uncheckedDestroyStatement(pstmt);
} 
}

synchronized int countConnectionsInUse() {
return this.inUseConnections.size();
}

synchronized boolean knownInUse(Connection pCon) {
return this.inUseConnections.contains(pCon);
}

Boolean tvlInUse(Connection pCon) {
return Boolean.valueOf(knownInUse(pCon));
}
synchronized int getNumConnectionsInUse() {
return this.inUseConnections.size();
}
synchronized int getNumConnectionsWithDeferredDestroyStatements() {
return this.connectionsToZombieStatementSets.keySet().size();
}

synchronized int getNumDeferredDestroyStatements() {
Set keys = this.connectionsToZombieStatementSets.keySet();
int sum = 0;
for (Iterator ii = keys.iterator(); ii.hasNext(); ) {

Object con = ii.next();
Set stmts = (Set)this.connectionsToZombieStatementSets.get(con);
synchronized (stmts) {
sum += (stmts == null) ? 0 : stmts.size();
} 
}  return sum;
}

private void trackedDestroyStatement(final Object parentConnection, final Object pstmt) {
final class TrackedStatementCloseTask
implements Runnable
{
public void run() {
synchronized (GooGooStatementCache.CautiousStatementDestructionManager.this) {

Set stmts = (Set)GooGooStatementCache.CautiousStatementDestructionManager.this.connectionsToZombieStatementSets.get(parentConnection);
if (stmts != null) {

StatementUtils.attemptClose((PreparedStatement)pstmt);

boolean removed1 = stmts.remove(pstmt);
assert removed1;
if (stmts.isEmpty()) {

Object removed2 = GooGooStatementCache.CautiousStatementDestructionManager.this.connectionsToZombieStatementSets.remove(parentConnection);

assert removed2 == stmts;
GooGooStatementCache.CautiousStatementDestructionManager.this.notifyAll();
} 
} 
} 
}
};

Runnable r = new TrackedStatementCloseTask();

if (!this.closed) {

this.deferredStatementDestroyer.postRunnable(r);

}
else {

r.run();
} 
}

private void destroyAllTrackedStatements(final Object parentConnection) {
final class TrackedDestroyAllStatementsTask
implements Runnable
{
public void run() {
synchronized (GooGooStatementCache.CautiousStatementDestructionManager.this) {

Set stmts = (Set)GooGooStatementCache.CautiousStatementDestructionManager.this.connectionsToZombieStatementSets.remove(parentConnection);
if (stmts != null) {

for (Iterator<PreparedStatement> ii = stmts.iterator(); ii.hasNext(); ) {

PreparedStatement pstmt = ii.next();
StatementUtils.attemptClose(pstmt);
ii.remove();
} 
GooGooStatementCache.CautiousStatementDestructionManager.this.notifyAll();
} 
} 
}
};

Runnable r = new TrackedDestroyAllStatementsTask();

if (!this.closed) {

this.deferredStatementDestroyer.postRunnable(r);

}
else {

r.run();
} 
}

private Set statementsUnderDestruction(Object parentConnection) {
assert Thread.holdsLock(this);

return (Set)this.connectionsToZombieStatementSets.get(parentConnection);
}
}
}

