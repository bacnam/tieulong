package com.jolbox.bonecp;

import com.google.common.collect.ImmutableSet;
import com.jolbox.bonecp.hooks.AcquireFailConfig;
import com.jolbox.bonecp.hooks.ConnectionHook;
import com.jolbox.bonecp.proxy.CallableStatementProxy;
import com.jolbox.bonecp.proxy.ConnectionProxy;
import com.jolbox.bonecp.proxy.PreparedStatementProxy;
import com.jolbox.bonecp.proxy.StatementProxy;
import com.jolbox.bonecp.proxy.TransactionRecoveryResult;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemorizeTransactionProxy
implements InvocationHandler
{
private Object target;
private WeakReference<ConnectionHandle> connectionHandle;
private static final ImmutableSet<String> clearLogConditions = ImmutableSet.of("rollback", "commit", "close");

private static final Logger logger = LoggerFactory.getLogger(MemorizeTransactionProxy.class);

public MemorizeTransactionProxy() {}

protected static Connection memorize(Connection target, ConnectionHandle connectionHandle) {
return (Connection)Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[] { ConnectionProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
}

protected static Statement memorize(Statement target, ConnectionHandle connectionHandle) {
return (Statement)Proxy.newProxyInstance(StatementProxy.class.getClassLoader(), new Class[] { StatementProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
}

protected static PreparedStatement memorize(PreparedStatement target, ConnectionHandle connectionHandle) {
return (PreparedStatement)Proxy.newProxyInstance(PreparedStatementProxy.class.getClassLoader(), new Class[] { PreparedStatementProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
}

protected static CallableStatement memorize(CallableStatement target, ConnectionHandle connectionHandle) {
return (CallableStatement)Proxy.newProxyInstance(CallableStatementProxy.class.getClassLoader(), new Class[] { CallableStatementProxy.class }, new MemorizeTransactionProxy(target, connectionHandle));
}

private MemorizeTransactionProxy(Object target, ConnectionHandle connectionHandle) {
this.target = target;
this.connectionHandle = new WeakReference<ConnectionHandle>(connectionHandle);
}

public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
Object result = null;
ConnectionHandle con = this.connectionHandle.get();
if (con != null) {

if (method.getName().equals("getProxyTarget")) {
return this.target;
}

if (con.isInReplayMode()) {
try {
return method.invoke(this.target, args);
} catch (InvocationTargetException t) {
throw t.getCause();
} 
}

if (con.recoveryResult != null) {
Object remap = con.recoveryResult.getReplaceTarget().get(this.target);
if (remap != null) {
this.target = remap;
}
remap = con.recoveryResult.getReplaceTarget().get(con);
if (remap != null) {
con = (ConnectionHandle)remap;
}
} 

if (!con.isInReplayMode() && !method.getName().equals("hashCode") && !method.getName().equals("equals") && !method.getName().equals("toString"))
{
con.getReplayLog().add(new ReplayLog(this.target, method, args));
}

try {
result = runWithPossibleProxySwap(method, this.target, args);

if (!con.isInReplayMode() && this.target instanceof Connection && clearLogConditions.contains(method.getName())) {
con.getReplayLog().clear();

}

}
catch (Throwable t) {

List<ReplayLog> oldReplayLog = con.getReplayLog();
con.setInReplayMode(true);

if (t instanceof SQLException || (t.getCause() != null && t.getCause() instanceof SQLException)) {
con.markPossiblyBroken((SQLException)t.getCause());
}

if (!con.isPossiblyBroken()) {
con.setInReplayMode(false);
con.getReplayLog().clear();
} else {
logger.error("Connection failed. Attempting to recover transaction on Thread #" + Thread.currentThread().getId());

try {
con.recoveryResult = attemptRecovery(oldReplayLog);
con.setReplayLog(oldReplayLog);
con.setInReplayMode(false);
logger.error("Recovery succeeded on Thread #" + Thread.currentThread().getId());
con.possiblyBroken = false;

return con.recoveryResult.getResult();
} catch (Throwable t2) {
con.setInReplayMode(false);
con.getReplayLog().clear();
throw new SQLException("Could not recover transaction. Original exception follows." + t.getCause());
} 
} 

throw t.getCause();
} 
} 

return result;
}

private Object runWithPossibleProxySwap(Method method, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
Object result;
if (method.getName().equals("createStatement")) {
result = memorize((Statement)method.invoke(target, args), this.connectionHandle.get());
}
else if (method.getName().equals("prepareStatement")) {
result = memorize((PreparedStatement)method.invoke(target, args), this.connectionHandle.get());
}
else if (method.getName().equals("prepareCall")) {
result = memorize((CallableStatement)method.invoke(target, args), this.connectionHandle.get());
} else {
result = method.invoke(target, args);
}  return result;
}

private TransactionRecoveryResult attemptRecovery(List<ReplayLog> oldReplayLog) throws SQLException {
boolean tryAgain = false;
Throwable failedThrowable = null;

ConnectionHandle con = this.connectionHandle.get();
TransactionRecoveryResult recoveryResult = con.recoveryResult;
ConnectionHook connectionHook = con.getPool().getConfig().getConnectionHook();

int acquireRetryAttempts = con.getPool().getConfig().getAcquireRetryAttempts();
long acquireRetryDelay = con.getPool().getConfig().getAcquireRetryDelayInMs();
AcquireFailConfig acquireConfig = new AcquireFailConfig();
acquireConfig.setAcquireRetryAttempts(new AtomicInteger(acquireRetryAttempts));
acquireConfig.setAcquireRetryDelayInMs(acquireRetryDelay);
acquireConfig.setLogMessage("Failed to replay transaction");

Map<Object, Object> replaceTarget = new HashMap<Object, Object>();
while (true) {
replaceTarget.clear();

for (Map.Entry<Object, Object> entry : (Iterable<Map.Entry<Object, Object>>)recoveryResult.getReplaceTarget().entrySet()) {
replaceTarget.put(entry.getKey(), entry.getValue());
}

List<PreparedStatement> prepStatementTarget = new ArrayList<PreparedStatement>();
List<CallableStatement> callableStatementTarget = new ArrayList<CallableStatement>();
List<Statement> statementTarget = new ArrayList<Statement>();
Object result = null;
tryAgain = false;

con.setInReplayMode(true);
try {
con.clearStatementCaches(true);
con.getInternalConnection().close();
} catch (Throwable t) {}

con.setInternalConnection(memorize(con.obtainInternalConnection(), con));
con.getOriginatingPartition().trackConnectionFinalizer(con);

Iterator<ReplayLog> i$ = oldReplayLog.iterator(); while (true) { if (i$.hasNext()) { ReplayLog replay = i$.next();

if (replay.getTarget() instanceof Connection) {
replaceTarget.put(replay.getTarget(), con.getInternalConnection());
} else if (replay.getTarget() instanceof CallableStatement) {
if (replaceTarget.get(replay.getTarget()) == null) {
replaceTarget.put(replay.getTarget(), callableStatementTarget.remove(0));
}
} else if (replay.getTarget() instanceof PreparedStatement) {
if (replaceTarget.get(replay.getTarget()) == null) {
replaceTarget.put(replay.getTarget(), prepStatementTarget.remove(0));
}
} else if (replay.getTarget() instanceof Statement && 
replaceTarget.get(replay.getTarget()) == null) {
replaceTarget.put(replay.getTarget(), statementTarget.remove(0));
} 

try {
result = runWithPossibleProxySwap(replay.getMethod(), replaceTarget.get(replay.getTarget()), replay.getArgs());

recoveryResult.setResult(result);

if (result instanceof CallableStatement) {
callableStatementTarget.add((CallableStatement)result); continue;
}  if (result instanceof PreparedStatement) {
prepStatementTarget.add((PreparedStatement)result); continue;
}  if (result instanceof Statement)
statementTarget.add((Statement)result); 
continue;
} catch (Throwable t) {

if (connectionHook != null) {
tryAgain = connectionHook.onAcquireFail(t, acquireConfig);
} else {

logger.error("Failed to replay transaction. Sleeping for " + acquireRetryDelay + "ms and trying again. Attempts left: " + acquireRetryAttempts + ". Exception: " + t.getCause());

try {
Thread.sleep(acquireRetryDelay);
if (acquireRetryAttempts > 0) {
tryAgain = (--acquireRetryAttempts != 0);
}
} catch (InterruptedException e) {
tryAgain = false;
} 
} 
if (!tryAgain)
failedThrowable = t; 
}  }
else
{ break; }

if (!tryAgain) break;  }  if (!tryAgain)
break; 
} 
for (Map.Entry<Object, Object> entry : replaceTarget.entrySet()) {
recoveryResult.getReplaceTarget().put(entry.getKey(), entry.getValue());
}

for (ReplayLog replay : oldReplayLog) {
replay.setTarget(replaceTarget.get(replay.getTarget()));
}

if (failedThrowable != null)
{
throw new SQLException(failedThrowable.getMessage(), failedThrowable);
}

return recoveryResult;
}
}

