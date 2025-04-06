package com.jolbox.bonecp.hooks;

import com.jolbox.bonecp.ConnectionHandle;
import com.jolbox.bonecp.StatementHandle;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public interface ConnectionHook {
  void onAcquire(ConnectionHandle paramConnectionHandle);
  
  void onCheckIn(ConnectionHandle paramConnectionHandle);
  
  void onCheckOut(ConnectionHandle paramConnectionHandle);
  
  void onDestroy(ConnectionHandle paramConnectionHandle);
  
  boolean onAcquireFail(Throwable paramThrowable, AcquireFailConfig paramAcquireFailConfig);
  
  void onQueryExecuteTimeLimitExceeded(ConnectionHandle paramConnectionHandle, Statement paramStatement, String paramString, Map<Object, Object> paramMap, long paramLong);
  
  @Deprecated
  void onQueryExecuteTimeLimitExceeded(ConnectionHandle paramConnectionHandle, Statement paramStatement, String paramString, Map<Object, Object> paramMap);
  
  @Deprecated
  void onQueryExecuteTimeLimitExceeded(String paramString, Map<Object, Object> paramMap);
  
  void onBeforeStatementExecute(ConnectionHandle paramConnectionHandle, StatementHandle paramStatementHandle, String paramString, Map<Object, Object> paramMap);
  
  void onAfterStatementExecute(ConnectionHandle paramConnectionHandle, StatementHandle paramStatementHandle, String paramString, Map<Object, Object> paramMap);
  
  boolean onConnectionException(ConnectionHandle paramConnectionHandle, String paramString, Throwable paramThrowable);
  
  ConnectionState onMarkPossiblyBroken(ConnectionHandle paramConnectionHandle, String paramString, SQLException paramSQLException);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/hooks/ConnectionHook.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */