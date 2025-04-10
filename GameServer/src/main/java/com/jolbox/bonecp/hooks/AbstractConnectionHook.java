package com.jolbox.bonecp.hooks;

import com.jolbox.bonecp.ConnectionHandle;
import com.jolbox.bonecp.PoolUtil;
import com.jolbox.bonecp.StatementHandle;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConnectionHook
implements ConnectionHook
{
private static final Logger logger = LoggerFactory.getLogger(AbstractConnectionHook.class);

public void onAcquire(ConnectionHandle connection) {}

public void onCheckIn(ConnectionHandle connection) {}

public void onCheckOut(ConnectionHandle connection) {}

public void onDestroy(ConnectionHandle connection) {}

public boolean onAcquireFail(Throwable t, AcquireFailConfig acquireConfig) {
boolean tryAgain = false;
String log = acquireConfig.getLogMessage();
logger.error(log + " Sleeping for " + acquireConfig.getAcquireRetryDelayInMs() + "ms and trying again. Attempts left: " + acquireConfig.getAcquireRetryAttempts() + ". Exception: " + t.getCause());

try {
Thread.sleep(acquireConfig.getAcquireRetryDelayInMs());
if (acquireConfig.getAcquireRetryAttempts().get() > 0) {
tryAgain = (acquireConfig.getAcquireRetryAttempts().decrementAndGet() > 0);
}
} catch (Exception e) {
tryAgain = false;
} 

return tryAgain;
}

public void onQueryExecuteTimeLimitExceeded(ConnectionHandle handle, Statement statement, String sql, Map<Object, Object> logParams, long timeElapsedInNs) {
onQueryExecuteTimeLimitExceeded(handle, statement, sql, logParams);
}

@Deprecated
public void onQueryExecuteTimeLimitExceeded(ConnectionHandle handle, Statement statement, String sql, Map<Object, Object> logParams) {
onQueryExecuteTimeLimitExceeded(sql, logParams);
}

@Deprecated
public void onQueryExecuteTimeLimitExceeded(String sql, Map<Object, Object> logParams) {
StringBuilder sb = new StringBuilder("Query execute time limit exceeded. Query: ");
sb.append(PoolUtil.fillLogParams(sql, logParams));
logger.warn(sb.toString());
}

public boolean onConnectionException(ConnectionHandle connection, String state, Throwable t) {
return true;
}

public void onBeforeStatementExecute(ConnectionHandle conn, StatementHandle statement, String sql, Map<Object, Object> params) {}

public void onAfterStatementExecute(ConnectionHandle conn, StatementHandle statement, String sql, Map<Object, Object> params) {}

public ConnectionState onMarkPossiblyBroken(ConnectionHandle connection, String state, SQLException e) {
return ConnectionState.NOP;
}
}

