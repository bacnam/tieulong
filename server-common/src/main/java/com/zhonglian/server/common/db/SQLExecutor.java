package com.zhonglian.server.common.db;

import BaseCommon.CommLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLExecutor {
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
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
        } catch (SQLException var27) {
            CommLog.error("SQLExecutor.insertBO('{}') error:{}", SQL, var27.getMessage(), var27);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        if (id > 0L) {
            bo.setId(id);
        }

        return dealCount > 0;
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
            return stmt.executeUpdate(SQL);
        } catch (SQLException var10) {
            CommLog.error("SQLExecutor.ReplaceBO('{}') error:{}", SQL, var10.getMessage());
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return -1;
    }

    public static <T extends IBaseBO> List<T> getListByCondition(IDBConnectionFactory con_pool, Conditions condition, T vo) {
        List<T> res = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String SQL = null;

        Object var10;
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

            return res;
        } catch (Throwable var13) {
            CommLog.error("SQLExecutor.getListByCondition('{}') error:{}", SQL, var13.getMessage());
            var10 = res;
        } finally {
            SQLUtil.close(rs, stmt, conn);
        }

        return (List<T>)var10;
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
                SQL = SQL + " where " + conditionString;
            }

            SQL = SQL + ";";
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

            return stmt.executeUpdate();
        } catch (SQLException var14) {
            CommLog.error("SQLExecutor.updateByCondition('{}') error:{}", SQL, var14.getMessage(), var14);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return -1;
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
            return stmt.executeUpdate(SQL);
        } catch (SQLException var10) {
            CommLog.error("SQLExecutor.delByCondition('{}') error:{}", SQL, var10.getMessage(), var10);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return -1;
    }

    public static String GetUpdateSql(Conditions condition, String updateString) {
        return "update " + condition.getTablesName() + " set " + updateString;
    }

    public static <T extends IBaseBO> String GetInsertSql(List<T> bos) {
        if (bos.size() == 0) {
            return "";
        } else {
            StringBuilder sBuilder = new StringBuilder();
            IBaseBO bo = bos.get(0);
            sBuilder.append(String.format("insert into %s (%s) values (%s)", bo.getTableName(), bo.getItemsName(), bo.getItemsValue()));

            for (int index = 1; index < bos.size(); index++) {
                sBuilder.append(String.format(", (%s)", bos.get(index).getItemsValue()));
            }

            sBuilder.append(";");
            return sBuilder.toString();
        }
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
            return true;
        } catch (SQLException var8) {
            LogSQLError(var8, sql);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return false;
    }

    public static int executeUpdate(String sql, IDBConnectionFactory con_pool) {
        int dealCount = 0;
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = con_pool.getConnection();
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException var9) {
            LogSQLError(var9, sql);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return -1;
    }

    public static int executeInsert(String sql, IDBConnectionFactory con_pool) {
        int lastID = 0;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = con_pool.getConnection();
            stmt = conn.prepareStatement(sql, 1);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    lastID = rs.getInt(1);
                }

                return lastID;
            }
        } catch (SQLException var23) {
            LogSQLError(var23, sql);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return -1;
    }

    public static List<Integer> getIDList(String sql, IDBConnectionFactory con_pool) {
        List<Integer> lstID = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = con_pool.getConnection();
            stmt = conn.createStatement();
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    lstID.add(rs.getInt(1));
                }

                return lstID;
            }
        } catch (SQLException var23) {
            LogSQLError(var23, sql);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }

        return null;
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

            return listResult;
        } catch (Throwable var11) {
            LogSQLError(var11, sql);
        } finally {
            SQLUtil.close(rs, stmt, conn);
        }

        return null;
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
        } catch (SQLException var10) {
            LogSQLError(var10, sql);
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
        } catch (SQLException var8) {
            LogSQLError(var8, sql);
        } finally {
            SQLUtil.close(null, stmt, conn);
        }
    }
}
