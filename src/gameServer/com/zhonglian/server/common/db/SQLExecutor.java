package com.zhonglian.server.common.db;

import BaseCommon.CommLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLExecutor
{
private static final byte[] emptyBinary = new byte[0];

public static boolean InsertBO(IDBConnectionFactory con_pool, IBaseBO bo) {
return InsertBO(con_pool, bo, null);
}

public static boolean InsertBO(IDBConnectionFactory con_pool, IBaseBO bo, ArrayList<byte[]> _binaryList) {
Connection conn = null;
PreparedStatement stmt = null;
String SQL = null;
long id = 0L;
int dealCount = 0;
try {
Exception exception2;
conn = con_pool.getConnection();
SQL = "insert into " + bo.getTableName() + "(" + bo.getItemsName() + ") values (" + bo.getItemsValue() + ");";

stmt = conn.prepareStatement(SQL, 1);

if (_binaryList != null) {
for (int i = 0; i < _binaryList.size(); i++) {
byte[] binaryData = _binaryList.get(i);
if (binaryData == null) {
binaryData = emptyBinary;
}
stmt.setBytes(i + 1, binaryData);
} 
}

dealCount = stmt.executeUpdate();

Exception exception1 = null;

}
catch (SQLException ex) {
CommLog.error("SQLExecutor.insertBO('{}') error:{}", new Object[] { SQL, ex.getMessage(), ex });
} finally {
SQLUtil.close(null, stmt, conn);
} 

if (id > 0L) {
bo.setId(id);
}

return (dealCount > 0);
}

public static int ReplaceBO(IDBConnectionFactory con_pool, IBaseBO bo) {
Connection conn = null;
Statement stmt = null;
String SQL = null;
int resultCount = 0;

try {
conn = con_pool.getConnection();
stmt = conn.createStatement();

SQL = GetReplaceSql(bo);

resultCount = stmt.executeUpdate(SQL);
} catch (SQLException ex) {
CommLog.error("SQLExecutor.ReplaceBO('{}') error:{}", SQL, ex.getMessage());
return -1;
} finally {
SQLUtil.close(null, stmt, conn);
} 

return resultCount;
}

public static <T extends IBaseBO> List<T> getListByCondition(IDBConnectionFactory con_pool, Conditions condition, T vo) {
List<T> res = new ArrayList<>();
Connection conn = null;
Statement stmt = null;
ResultSet rs = null;
String SQL = null;

try {
conn = con_pool.getConnection();
stmt = conn.createStatement();

condition.setTablesName(vo.getTableName());
condition.setItemsName(vo.getItemsName());
SQL = condition.getSQL();

rs = stmt.executeQuery(SQL);
while (rs.next()) {
vo.getFromResultSet(rs, res);
}
} catch (Throwable ex) {
CommLog.error("SQLExecutor.getListByCondition('{}') error:{}", SQL, ex.getMessage());
return res;
} finally {
SQLUtil.close(rs, stmt, conn);
} 

return res;
}

public static int updateByCondition(IDBConnectionFactory con_pool, Conditions condition, String updateString) {
return updateByCondition(con_pool, condition, updateString, null);
}

public static int updateByCondition(IDBConnectionFactory con_pool, Conditions condition, String updateString, ArrayList<byte[]> _binaryList) {
Connection conn = null;
PreparedStatement stmt = null;
String SQL = null;

int resultCount = 0;

try {
conn = con_pool.getConnection();

SQL = GetUpdateSql(condition, updateString);
String conditionString = condition.getCondition();
if (conditionString.trim().length() > 0) {
SQL = String.valueOf(SQL) + " where " + conditionString;
}
SQL = String.valueOf(SQL) + ";";

stmt = conn.prepareStatement(SQL);

if (_binaryList != null) {
for (int i = 0; i < _binaryList.size(); i++) {
byte[] binaryData = _binaryList.get(i);
if (binaryData == null) {
binaryData = emptyBinary;
}

stmt.setBytes(i + 1, binaryData);
} 
}

resultCount = stmt.executeUpdate();
} catch (SQLException ex) {
CommLog.error("SQLExecutor.updateByCondition('{}') error:{}", new Object[] { SQL, ex.getMessage(), ex });
return -1;
} finally {
SQLUtil.close(null, stmt, conn);
} 

return resultCount;
}

public static int delByCondition(IDBConnectionFactory con_pool, Conditions condition) {
Connection conn = null;
Statement stmt = null;
String SQL = null;

int resultCount = 0;

try {
conn = con_pool.getConnection();
stmt = conn.createStatement();

SQL = "delete " + condition.getConditionAndTables() + ";";

resultCount = stmt.executeUpdate(SQL);
} catch (SQLException ex) {
CommLog.error("SQLExecutor.delByCondition('{}') error:{}", new Object[] { SQL, ex.getMessage(), ex });
return -1;
} finally {
SQLUtil.close(null, stmt, conn);
} 

return resultCount;
}

public static String GetUpdateSql(Conditions condition, String updateString) {
return "update " + condition.getTablesName() + " set " + updateString;
}

public static <T extends IBaseBO> String GetInsertSql(List<T> bos) {
if (bos.size() == 0) {
return "";
}
StringBuilder sBuilder = new StringBuilder();
IBaseBO bo = (IBaseBO)bos.get(0);
sBuilder.append(String.format("insert into %s (%s) values (%s)", new Object[] { bo.getTableName(), bo.getItemsName(), bo.getItemsValue() }));

for (int index = 1; index < bos.size(); index++) {
sBuilder.append(String.format(", (%s)", new Object[] { ((IBaseBO)bos.get(index)).getItemsValue() }));
} 
sBuilder.append(";");
return sBuilder.toString();
}

public static String GetReplaceSql(IBaseBO bo) {
return "replace into " + bo.getTableName() + "(" + bo.getItemsName() + ") values (" + bo.getItemsValue() + ");";
}

static void LogSQLError(Throwable e, String sql) {
CommLog.error("SQLExecutor-SQL Error: [ '{}' ] {}", sql, e.getMessage());
}

public static boolean execute(String sql, IDBConnectionFactory con_pool) {
Connection conn = null;
Statement stmt = null;
try {
conn = con_pool.getConnection();
stmt = conn.createStatement();
stmt.execute(sql);
} catch (SQLException e) {
LogSQLError(e, sql);
return false;
} finally {
SQLUtil.close(null, stmt, conn);
} 
return true;
}

public static int executeUpdate(String sql, IDBConnectionFactory con_pool) {
int dealCount = 0;
Connection conn = null;
Statement stmt = null;
try {
conn = con_pool.getConnection();
stmt = conn.createStatement();
dealCount = stmt.executeUpdate(sql);
} catch (SQLException e) {
LogSQLError(e, sql);
return -1;
} finally {
SQLUtil.close(null, stmt, conn);
} 
return dealCount;
}

public static int executeInsert(String sql, IDBConnectionFactory con_pool) {
int lastID = 0;

Connection conn = null;
PreparedStatement stmt = null; try {
Exception exception2;
conn = con_pool.getConnection();
stmt = conn.prepareStatement(sql, 1);
stmt.executeUpdate();

Exception exception1 = null;

}
catch (SQLException e) {
LogSQLError(e, sql);
return -1;
} finally {
SQLUtil.close(null, stmt, conn);
} 

return lastID;
}

public static List<Integer> getIDList(String sql, IDBConnectionFactory con_pool) {
List<Integer> lstID = new ArrayList<>();

Connection conn = null;
Statement stmt = null;
try {
Exception exception2;
conn = con_pool.getConnection();
stmt = conn.createStatement();
Exception exception1 = null;

}
catch (SQLException e) {
LogSQLError(e, sql);
return null;
} finally {
SQLUtil.close(null, stmt, conn);
} 

return lstID;
}

public static <T extends IBaseBO> List<T> executeQuery(String sql, IDBConnectionFactory con_pool, T bo) {
List<T> listResult = new ArrayList<>();

Statement stmt = null;
ResultSet rs = null;
Connection conn = null;

try {
conn = con_pool.getConnection();
stmt = conn.createStatement();
rs = stmt.executeQuery(sql);

while (rs.next()) {
bo.getFromResultSet(rs, listResult);
}
} catch (Throwable e) {
LogSQLError(e, sql);
return null;
} finally {
SQLUtil.close(rs, stmt, conn);
} 

return listResult;
}

public static int executeQueryCount(String sql, IDBConnectionFactory con_pool) {
Statement stmt = null;
ResultSet rs = null;
Connection conn = null;

int nRet = 0;
try {
conn = con_pool.getConnection();
stmt = conn.createStatement();
rs = stmt.executeQuery(sql);

if (rs != null && rs.next()) {
nRet = rs.getInt(1);
}
} catch (SQLException e) {
LogSQLError(e, sql);
} finally {
SQLUtil.close(rs, stmt, conn);
} 

return nRet;
}

public static void setUTF8(IDBConnectionFactory con_pool) {
String sql = "SET NAMES 'utf8';";
Connection conn = null;
Statement stmt = null;
try {
conn = con_pool.getConnection();
stmt = conn.createStatement();
stmt.executeUpdate(sql);
} catch (SQLException e) {
LogSQLError(e, sql);
} finally {
SQLUtil.close(null, stmt, conn);
} 
}
}

