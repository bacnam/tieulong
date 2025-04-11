package com.mysql.jdbc;

import com.mysql.jdbc.exceptions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

public class SQLError {
    public static final String SQL_STATE_BASE_TABLE_NOT_FOUND = "S0002";
    public static final String SQL_STATE_BASE_TABLE_OR_VIEW_ALREADY_EXISTS = "S0001";
    public static final String SQL_STATE_BASE_TABLE_OR_VIEW_NOT_FOUND = "42S02";
    public static final String SQL_STATE_COLUMN_ALREADY_EXISTS = "S0021";
    public static final String SQL_STATE_COLUMN_NOT_FOUND = "S0022";
    public static final String SQL_STATE_COMMUNICATION_LINK_FAILURE = "08S01";
    public static final String SQL_STATE_CONNECTION_FAIL_DURING_TX = "08007";
    public static final String SQL_STATE_CONNECTION_IN_USE = "08002";
    public static final String SQL_STATE_CONNECTION_NOT_OPEN = "08003";
    public static final String SQL_STATE_CONNECTION_REJECTED = "08004";
    public static final String SQL_STATE_DATE_TRUNCATED = "01004";
    public static final String SQL_STATE_DATETIME_FIELD_OVERFLOW = "22008";
    public static final String SQL_STATE_DEADLOCK = "41000";
    public static final String SQL_STATE_DISCONNECT_ERROR = "01002";
    public static final String SQL_STATE_DIVISION_BY_ZERO = "22012";
    public static final String SQL_STATE_DRIVER_NOT_CAPABLE = "S1C00";
    public static final String SQL_STATE_ERROR_IN_ROW = "01S01";
    public static final String SQL_STATE_GENERAL_ERROR = "S1000";
    public static final String SQL_STATE_ILLEGAL_ARGUMENT = "S1009";
    public static final String SQL_STATE_INDEX_ALREADY_EXISTS = "S0011";
    public static final String SQL_STATE_INDEX_NOT_FOUND = "S0012";
    public static final String SQL_STATE_INSERT_VALUE_LIST_NO_MATCH_COL_LIST = "21S01";
    public static final String SQL_STATE_INVALID_AUTH_SPEC = "28000";
    public static final String SQL_STATE_INVALID_CHARACTER_VALUE_FOR_CAST = "22018";
    public static final String SQL_STATE_INVALID_COLUMN_NUMBER = "S1002";
    public static final String SQL_STATE_INVALID_CONNECTION_ATTRIBUTE = "01S00";
    public static final String SQL_STATE_MEMORY_ALLOCATION_FAILURE = "S1001";
    public static final String SQL_STATE_MORE_THAN_ONE_ROW_UPDATED_OR_DELETED = "01S04";
    public static final String SQL_STATE_NO_DEFAULT_FOR_COLUMN = "S0023";
    public static final String SQL_STATE_NO_ROWS_UPDATED_OR_DELETED = "01S03";
    public static final String SQL_STATE_NUMERIC_VALUE_OUT_OF_RANGE = "22003";
    public static final String SQL_STATE_PRIVILEGE_NOT_REVOKED = "01006";
    public static final String SQL_STATE_SYNTAX_ERROR = "42000";
    public static final String SQL_STATE_TIMEOUT_EXPIRED = "S1T00";
    public static final String SQL_STATE_TRANSACTION_RESOLUTION_UNKNOWN = "08007";
    public static final String SQL_STATE_UNABLE_TO_CONNECT_TO_DATASOURCE = "08001";
    public static final String SQL_STATE_WRONG_NO_OF_PARAMETERS = "07001";
    public static final String SQL_STATE_INVALID_TRANSACTION_TERMINATION = "2D000";
    static final int ER_WARNING_NOT_COMPLETE_ROLLBACK = 1196;
    private static final long DEFAULT_WAIT_TIMEOUT_SECONDS = 28800L;
    private static final int DUE_TO_TIMEOUT_FALSE = 0;
    private static final int DUE_TO_TIMEOUT_MAYBE = 2;
    private static final int DUE_TO_TIMEOUT_TRUE = 1;
    private static final Constructor<?> JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR;
    private static Map<Integer, String> mysqlToSql99State;
    private static Map<Integer, String> mysqlToSqlState;
    private static Map<String, String> sqlStateMessages = new HashMap<String, String>();
    private static Method THROWABLE_INIT_CAUSE_METHOD;

    static {
        if (Util.isJdbc4()) {
            try {
                JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR = Class.forName("com.mysql.jdbc.exceptions.jdbc4.CommunicationsException").getConstructor(new Class[]{MySQLConnection.class, long.class, long.class, Exception.class});

            } catch (SecurityException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR = null;
        }

        try {
            THROWABLE_INIT_CAUSE_METHOD = Throwable.class.getMethod("initCause", new Class[]{Throwable.class});
        } catch (Throwable t) {

            THROWABLE_INIT_CAUSE_METHOD = null;
        }
    }

    static {
        sqlStateMessages.put("01002", Messages.getString("SQLError.35"));

        sqlStateMessages.put("01004", Messages.getString("SQLError.36"));

        sqlStateMessages.put("01006", Messages.getString("SQLError.37"));

        sqlStateMessages.put("01S00", Messages.getString("SQLError.38"));

        sqlStateMessages.put("01S01", Messages.getString("SQLError.39"));

        sqlStateMessages.put("01S03", Messages.getString("SQLError.40"));

        sqlStateMessages.put("01S04", Messages.getString("SQLError.41"));

        sqlStateMessages.put("07001", Messages.getString("SQLError.42"));

        sqlStateMessages.put("08001", Messages.getString("SQLError.43"));

        sqlStateMessages.put("08002", Messages.getString("SQLError.44"));

        sqlStateMessages.put("08003", Messages.getString("SQLError.45"));

        sqlStateMessages.put("08004", Messages.getString("SQLError.46"));

        sqlStateMessages.put("08007", Messages.getString("SQLError.47"));

        sqlStateMessages.put("08S01", Messages.getString("SQLError.48"));

        sqlStateMessages.put("21S01", Messages.getString("SQLError.49"));

        sqlStateMessages.put("22003", Messages.getString("SQLError.50"));

        sqlStateMessages.put("22008", Messages.getString("SQLError.51"));

        sqlStateMessages.put("22012", Messages.getString("SQLError.52"));

        sqlStateMessages.put("41000", Messages.getString("SQLError.53"));

        sqlStateMessages.put("28000", Messages.getString("SQLError.54"));

        sqlStateMessages.put("42000", Messages.getString("SQLError.55"));

        sqlStateMessages.put("42S02", Messages.getString("SQLError.56"));

        sqlStateMessages.put("S0001", Messages.getString("SQLError.57"));

        sqlStateMessages.put("S0002", Messages.getString("SQLError.58"));

        sqlStateMessages.put("S0011", Messages.getString("SQLError.59"));

        sqlStateMessages.put("S0012", Messages.getString("SQLError.60"));

        sqlStateMessages.put("S0021", Messages.getString("SQLError.61"));

        sqlStateMessages.put("S0022", Messages.getString("SQLError.62"));

        sqlStateMessages.put("S0023", Messages.getString("SQLError.63"));

        sqlStateMessages.put("S1000", Messages.getString("SQLError.64"));

        sqlStateMessages.put("S1001", Messages.getString("SQLError.65"));

        sqlStateMessages.put("S1002", Messages.getString("SQLError.66"));

        sqlStateMessages.put("S1009", Messages.getString("SQLError.67"));

        sqlStateMessages.put("S1C00", Messages.getString("SQLError.68"));

        sqlStateMessages.put("S1T00", Messages.getString("SQLError.69"));

        mysqlToSqlState = new Hashtable<Integer, String>();

        mysqlToSqlState.put(Integer.valueOf(1040), "08004");
        mysqlToSqlState.put(Integer.valueOf(1042), "08004");
        mysqlToSqlState.put(Integer.valueOf(1043), "08004");
        mysqlToSqlState.put(Integer.valueOf(1047), "08S01");

        mysqlToSqlState.put(Integer.valueOf(1081), "08S01");

        mysqlToSqlState.put(Integer.valueOf(1129), "08004");
        mysqlToSqlState.put(Integer.valueOf(1130), "08004");

        mysqlToSqlState.put(Integer.valueOf(1045), "28000");

        mysqlToSqlState.put(Integer.valueOf(1037), "S1001");

        mysqlToSqlState.put(Integer.valueOf(1038), "S1001");

        mysqlToSqlState.put(Integer.valueOf(1064), "42000");
        mysqlToSqlState.put(Integer.valueOf(1065), "42000");

        mysqlToSqlState.put(Integer.valueOf(1055), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1056), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1057), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1059), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1060), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1061), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1062), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1063), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1066), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1067), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1068), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1069), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1070), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1071), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1072), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1073), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1074), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1075), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1082), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1083), "S1009");
        mysqlToSqlState.put(Integer.valueOf(1084), "S1009");

        mysqlToSqlState.put(Integer.valueOf(1058), "21S01");

        mysqlToSqlState.put(Integer.valueOf(1051), "42S02");

        mysqlToSqlState.put(Integer.valueOf(1054), "S0022");

        mysqlToSqlState.put(Integer.valueOf(1205), "41000");
        mysqlToSqlState.put(Integer.valueOf(1213), "41000");

        mysqlToSql99State = new HashMap<Integer, String>();

        mysqlToSql99State.put(Integer.valueOf(1205), "41000");
        mysqlToSql99State.put(Integer.valueOf(1213), "41000");
        mysqlToSql99State.put(Integer.valueOf(1022), "23000");

        mysqlToSql99State.put(Integer.valueOf(1037), "HY001");

        mysqlToSql99State.put(Integer.valueOf(1038), "HY001");

        mysqlToSql99State.put(Integer.valueOf(1040), "08004");

        mysqlToSql99State.put(Integer.valueOf(1042), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1043), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1044), "42000");

        mysqlToSql99State.put(Integer.valueOf(1045), "28000");

        mysqlToSql99State.put(Integer.valueOf(1050), "42S01");

        mysqlToSql99State.put(Integer.valueOf(1051), "42S02");

        mysqlToSql99State.put(Integer.valueOf(1052), "23000");

        mysqlToSql99State.put(Integer.valueOf(1053), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1054), "42S22");

        mysqlToSql99State.put(Integer.valueOf(1055), "42000");

        mysqlToSql99State.put(Integer.valueOf(1056), "42000");

        mysqlToSql99State.put(Integer.valueOf(1057), "42000");

        mysqlToSql99State.put(Integer.valueOf(1058), "21S01");

        mysqlToSql99State.put(Integer.valueOf(1059), "42000");

        mysqlToSql99State.put(Integer.valueOf(1060), "42S21");

        mysqlToSql99State.put(Integer.valueOf(1061), "42000");

        mysqlToSql99State.put(Integer.valueOf(1062), "23000");

        mysqlToSql99State.put(Integer.valueOf(1063), "42000");

        mysqlToSql99State.put(Integer.valueOf(1064), "42000");

        mysqlToSql99State.put(Integer.valueOf(1065), "42000");

        mysqlToSql99State.put(Integer.valueOf(1066), "42000");

        mysqlToSql99State.put(Integer.valueOf(1067), "42000");

        mysqlToSql99State.put(Integer.valueOf(1068), "42000");

        mysqlToSql99State.put(Integer.valueOf(1069), "42000");

        mysqlToSql99State.put(Integer.valueOf(1070), "42000");

        mysqlToSql99State.put(Integer.valueOf(1071), "42000");

        mysqlToSql99State.put(Integer.valueOf(1072), "42000");

        mysqlToSql99State.put(Integer.valueOf(1073), "42000");

        mysqlToSql99State.put(Integer.valueOf(1074), "42000");

        mysqlToSql99State.put(Integer.valueOf(1075), "42000");

        mysqlToSql99State.put(Integer.valueOf(1080), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1081), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1082), "42S12");

        mysqlToSql99State.put(Integer.valueOf(1083), "42000");

        mysqlToSql99State.put(Integer.valueOf(1084), "42000");

        mysqlToSql99State.put(Integer.valueOf(1090), "42000");

        mysqlToSql99State.put(Integer.valueOf(1091), "42000");

        mysqlToSql99State.put(Integer.valueOf(1101), "42000");

        mysqlToSql99State.put(Integer.valueOf(1102), "42000");

        mysqlToSql99State.put(Integer.valueOf(1103), "42000");

        mysqlToSql99State.put(Integer.valueOf(1104), "42000");

        mysqlToSql99State.put(Integer.valueOf(1106), "42000");

        mysqlToSql99State.put(Integer.valueOf(1107), "42000");

        mysqlToSql99State.put(Integer.valueOf(1109), "42S02");

        mysqlToSql99State.put(Integer.valueOf(1110), "42000");

        mysqlToSql99State.put(Integer.valueOf(1112), "42000");

        mysqlToSql99State.put(Integer.valueOf(1113), "42000");

        mysqlToSql99State.put(Integer.valueOf(1115), "42000");

        mysqlToSql99State.put(Integer.valueOf(1118), "42000");

        mysqlToSql99State.put(Integer.valueOf(1120), "42000");

        mysqlToSql99State.put(Integer.valueOf(1121), "42000");

        mysqlToSql99State.put(Integer.valueOf(1131), "42000");

        mysqlToSql99State.put(Integer.valueOf(1132), "42000");

        mysqlToSql99State.put(Integer.valueOf(1133), "42000");

        mysqlToSql99State.put(Integer.valueOf(1136), "21S01");

        mysqlToSql99State.put(Integer.valueOf(1138), "42000");

        mysqlToSql99State.put(Integer.valueOf(1139), "42000");

        mysqlToSql99State.put(Integer.valueOf(1140), "42000");

        mysqlToSql99State.put(Integer.valueOf(1141), "42000");

        mysqlToSql99State.put(Integer.valueOf(1142), "42000");

        mysqlToSql99State.put(Integer.valueOf(1143), "42000");

        mysqlToSql99State.put(Integer.valueOf(1144), "42000");

        mysqlToSql99State.put(Integer.valueOf(1145), "42000");

        mysqlToSql99State.put(Integer.valueOf(1146), "42S02");

        mysqlToSql99State.put(Integer.valueOf(1147), "42000");

        mysqlToSql99State.put(Integer.valueOf(1148), "42000");

        mysqlToSql99State.put(Integer.valueOf(1149), "42000");

        mysqlToSql99State.put(Integer.valueOf(1152), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1153), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1154), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1155), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1156), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1157), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1158), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1159), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1160), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1161), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1162), "42000");

        mysqlToSql99State.put(Integer.valueOf(1163), "42000");

        mysqlToSql99State.put(Integer.valueOf(1164), "42000");

        mysqlToSql99State.put(Integer.valueOf(1166), "42000");

        mysqlToSql99State.put(Integer.valueOf(1167), "42000");

        mysqlToSql99State.put(Integer.valueOf(1169), "23000");

        mysqlToSql99State.put(Integer.valueOf(1170), "42000");

        mysqlToSql99State.put(Integer.valueOf(1171), "42000");

        mysqlToSql99State.put(Integer.valueOf(1172), "42000");

        mysqlToSql99State.put(Integer.valueOf(1173), "42000");

        mysqlToSql99State.put(Integer.valueOf(1177), "42000");

        mysqlToSql99State.put(Integer.valueOf(1178), "42000");

        mysqlToSql99State.put(Integer.valueOf(1179), "25000");

        mysqlToSql99State.put(Integer.valueOf(1184), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1189), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1190), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1203), "42000");

        mysqlToSql99State.put(Integer.valueOf(1207), "25000");

        mysqlToSql99State.put(Integer.valueOf(1211), "42000");

        mysqlToSql99State.put(Integer.valueOf(1213), "40001");

        mysqlToSql99State.put(Integer.valueOf(1216), "23000");

        mysqlToSql99State.put(Integer.valueOf(1217), "23000");

        mysqlToSql99State.put(Integer.valueOf(1218), "08S01");

        mysqlToSql99State.put(Integer.valueOf(1222), "21000");

        mysqlToSql99State.put(Integer.valueOf(1226), "42000");

        mysqlToSql99State.put(Integer.valueOf(1230), "42000");

        mysqlToSql99State.put(Integer.valueOf(1231), "42000");

        mysqlToSql99State.put(Integer.valueOf(1232), "42000");

        mysqlToSql99State.put(Integer.valueOf(1234), "42000");

        mysqlToSql99State.put(Integer.valueOf(1235), "42000");

        mysqlToSql99State.put(Integer.valueOf(1239), "42000");

        mysqlToSql99State.put(Integer.valueOf(1241), "21000");

        mysqlToSql99State.put(Integer.valueOf(1242), "21000");

        mysqlToSql99State.put(Integer.valueOf(1247), "42S22");

        mysqlToSql99State.put(Integer.valueOf(1248), "42000");

        mysqlToSql99State.put(Integer.valueOf(1249), "01000");

        mysqlToSql99State.put(Integer.valueOf(1250), "42000");

        mysqlToSql99State.put(Integer.valueOf(1251), "08004");

        mysqlToSql99State.put(Integer.valueOf(1252), "42000");

        mysqlToSql99State.put(Integer.valueOf(1253), "42000");

        mysqlToSql99State.put(Integer.valueOf(1261), "01000");

        mysqlToSql99State.put(Integer.valueOf(1262), "01000");

        mysqlToSql99State.put(Integer.valueOf(1263), "01000");

        mysqlToSql99State.put(Integer.valueOf(1264), "01000");

        mysqlToSql99State.put(Integer.valueOf(1265), "01000");

        mysqlToSql99State.put(Integer.valueOf(1280), "42000");

        mysqlToSql99State.put(Integer.valueOf(1281), "42000");

        mysqlToSql99State.put(Integer.valueOf(1286), "42000");
    }

    static SQLWarning convertShowWarningsToSQLWarnings(Connection connection) throws SQLException {
        return convertShowWarningsToSQLWarnings(connection, 0, false);
    }

    static SQLWarning convertShowWarningsToSQLWarnings(Connection connection, int warningCountIfKnown, boolean forTruncationOnly) throws SQLException {
        Statement stmt = null;
        ResultSet warnRs = null;

        SQLWarning currentWarning = null;

        try {
            if (warningCountIfKnown < 100) {
                stmt = connection.createStatement();

                if (stmt.getMaxRows() != 0) {
                    stmt.setMaxRows(0);
                }
            } else {

                stmt = connection.createStatement(1003, 1007);

                stmt.setFetchSize(-2147483648);
            }

            warnRs = stmt.executeQuery("SHOW WARNINGS");

            while (warnRs.next()) {
                int code = warnRs.getInt("Code");

                if (forTruncationOnly) {
                    if (code == 1265 || code == 1264) {
                        DataTruncation newTruncation = new MysqlDataTruncation(warnRs.getString("Message"), 0, false, false, 0, 0, code);

                        if (currentWarning == null) {
                            currentWarning = newTruncation;
                            continue;
                        }
                        currentWarning.setNextWarning(newTruncation);
                    }

                    continue;
                }
                String message = warnRs.getString("Message");

                SQLWarning newWarning = new SQLWarning(message, mysqlToSqlState(code, connection.getUseSqlStateCodes()), code);

                if (currentWarning == null) {
                    currentWarning = newWarning;
                    continue;
                }
                currentWarning.setNextWarning(newWarning);
            }

            if (forTruncationOnly && currentWarning != null) {
                throw currentWarning;
            }

            return currentWarning;
        } finally {
            SQLException reThrow = null;

            if (warnRs != null) {
                try {
                    warnRs.close();
                } catch (SQLException sqlEx) {
                    reThrow = sqlEx;
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {

                    reThrow = sqlEx;
                }
            }

            if (reThrow != null) {
                throw reThrow;
            }
        }
    }

    public static void dumpSqlStatesMappingsAsXml() throws Exception {
        TreeMap<Integer, Integer> allErrorNumbers = new TreeMap<Integer, Integer>();
        Map<Object, String> mysqlErrorNumbersToNames = new HashMap<Object, String>();

        for (Integer errorNumber : mysqlToSql99State.keySet()) {
            allErrorNumbers.put(errorNumber, errorNumber);
        }

        for (Integer errorNumber : mysqlToSqlState.keySet()) {
            allErrorNumbers.put(errorNumber, errorNumber);
        }

        Field[] possibleFields = MysqlErrorNumbers.class.getDeclaredFields();

        for (int i = 0; i < possibleFields.length; i++) {
            String fieldName = possibleFields[i].getName();

            if (fieldName.startsWith("ER_")) {
                mysqlErrorNumbersToNames.put(possibleFields[i].get(null), fieldName);
            }
        }

        System.out.println("<ErrorMappings>");

        for (Integer errorNumber : allErrorNumbers.keySet()) {
            String sql92State = mysqlToSql99(errorNumber.intValue());
            String oldSqlState = mysqlToXOpen(errorNumber.intValue());

            System.out.println("   <ErrorMapping mysqlErrorNumber=\"" + errorNumber + "\" mysqlErrorName=\"" + (String) mysqlErrorNumbersToNames.get(errorNumber) + "\" legacySqlState=\"" + ((oldSqlState == null) ? "" : oldSqlState) + "\" sql92SqlState=\"" + ((sql92State == null) ? "" : sql92State) + "\"/>");
        }

        System.out.println("</ErrorMappings>");
    }

    static String get(String stateCode) {
        return sqlStateMessages.get(stateCode);
    }

    private static String mysqlToSql99(int errno) {
        Integer err = Integer.valueOf(errno);

        if (mysqlToSql99State.containsKey(err)) {
            return mysqlToSql99State.get(err);
        }

        return "HY000";
    }

    static String mysqlToSqlState(int errno, boolean useSql92States) {
        if (useSql92States) {
            return mysqlToSql99(errno);
        }

        return mysqlToXOpen(errno);
    }

    private static String mysqlToXOpen(int errno) {
        Integer err = Integer.valueOf(errno);

        if (mysqlToSqlState.containsKey(err)) {
            return mysqlToSqlState.get(err);
        }

        return "S1000";
    }

    public static SQLException createSQLException(String message, String sqlState, ExceptionInterceptor interceptor) {
        return createSQLException(message, sqlState, 0, interceptor);
    }

    public static SQLException createSQLException(String message, ExceptionInterceptor interceptor) {
        return createSQLException(message, interceptor, (Connection) null);
    }

    public static SQLException createSQLException(String message, ExceptionInterceptor interceptor, Connection conn) {
        SQLException sqlEx = new SQLException(message);

        if (interceptor != null) {
            SQLException interceptedEx = interceptor.interceptException(sqlEx, conn);

            if (interceptedEx != null) {
                return interceptedEx;
            }
        }

        return sqlEx;
    }

    public static SQLException createSQLException(String message, String sqlState, Throwable cause, ExceptionInterceptor interceptor) {
        return createSQLException(message, sqlState, cause, interceptor, (Connection) null);
    }

    public static SQLException createSQLException(String message, String sqlState, Throwable cause, ExceptionInterceptor interceptor, Connection conn) {
        if (THROWABLE_INIT_CAUSE_METHOD == null &&
                cause != null) {
            message = message + " due to " + cause.toString();
        }

        SQLException sqlEx = createSQLException(message, sqlState, interceptor);

        if (cause != null && THROWABLE_INIT_CAUSE_METHOD != null) {
            try {
                THROWABLE_INIT_CAUSE_METHOD.invoke(sqlEx, new Object[]{cause});
            } catch (Throwable t) {
            }
        }

        if (interceptor != null) {
            SQLException interceptedEx = interceptor.interceptException(sqlEx, conn);

            if (interceptedEx != null) {
                return interceptedEx;
            }
        }

        return sqlEx;
    }

    public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode, ExceptionInterceptor interceptor) {
        return createSQLException(message, sqlState, vendorErrorCode, false, interceptor);
    }

    public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode, boolean isTransient, ExceptionInterceptor interceptor) {
        return createSQLException(message, sqlState, vendorErrorCode, false, interceptor, null);
    }

    public static SQLException createSQLException(String message, String sqlState, int vendorErrorCode, boolean isTransient, ExceptionInterceptor interceptor, Connection conn) {
        try {
            SQLException sqlEx = null;

            if (sqlState != null) {
                if (sqlState.startsWith("08")) {
                    if (isTransient) {
                        if (!Util.isJdbc4()) {
                            MySQLTransientConnectionException mySQLTransientConnectionException = new MySQLTransientConnectionException(message, sqlState, vendorErrorCode);
                        } else {

                            sqlEx = (SQLException) Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLTransientConnectionException", new Class[]{String.class, String.class, int.class}, new Object[]{message, sqlState, Integer.valueOf(vendorErrorCode)}, interceptor);

                        }

                    } else if (!Util.isJdbc4()) {
                        MySQLNonTransientConnectionException mySQLNonTransientConnectionException = new MySQLNonTransientConnectionException(message, sqlState, vendorErrorCode);
                    } else {

                        sqlEx = (SQLException) Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLNonTransientConnectionException", new Class[]{String.class, String.class, int.class}, new Object[]{message, sqlState, Integer.valueOf(vendorErrorCode)}, interceptor);

                    }

                } else if (sqlState.startsWith("22")) {
                    if (!Util.isJdbc4()) {
                        MySQLDataException mySQLDataException = new MySQLDataException(message, sqlState, vendorErrorCode);
                    } else {

                        sqlEx = (SQLException) Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLDataException", new Class[]{String.class, String.class, int.class}, new Object[]{message, sqlState, Integer.valueOf(vendorErrorCode)}, interceptor);

                    }

                } else if (sqlState.startsWith("23")) {

                    if (!Util.isJdbc4()) {
                        MySQLIntegrityConstraintViolationException mySQLIntegrityConstraintViolationException = new MySQLIntegrityConstraintViolationException(message, sqlState, vendorErrorCode);
                    } else {

                        sqlEx = (SQLException) Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException", new Class[]{String.class, String.class, int.class}, new Object[]{message, sqlState, Integer.valueOf(vendorErrorCode)}, interceptor);

                    }

                } else if (sqlState.startsWith("42")) {
                    if (!Util.isJdbc4()) {
                        MySQLSyntaxErrorException mySQLSyntaxErrorException = new MySQLSyntaxErrorException(message, sqlState, vendorErrorCode);
                    } else {

                        sqlEx = (SQLException) Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException", new Class[]{String.class, String.class, int.class}, new Object[]{message, sqlState, Integer.valueOf(vendorErrorCode)}, interceptor);

                    }

                } else if (sqlState.startsWith("40")) {
                    if (!Util.isJdbc4()) {
                        MySQLTransactionRollbackException mySQLTransactionRollbackException = new MySQLTransactionRollbackException(message, sqlState, vendorErrorCode);
                    } else {

                        sqlEx = (SQLException) Util.getInstance("com.mysql.jdbc.exceptions.jdbc4.MySQLTransactionRollbackException", new Class[]{String.class, String.class, int.class}, new Object[]{message, sqlState, Integer.valueOf(vendorErrorCode)}, interceptor);

                    }

                } else {

                    sqlEx = new SQLException(message, sqlState, vendorErrorCode);
                }
            } else {
                sqlEx = new SQLException(message, sqlState, vendorErrorCode);
            }

            if (interceptor != null) {
                SQLException interceptedEx = interceptor.interceptException(sqlEx, conn);

                if (interceptedEx != null) {
                    return interceptedEx;
                }
            }

            return sqlEx;
        } catch (SQLException sqlEx) {
            SQLException unexpectedEx = new SQLException("Unable to create correct SQLException class instance, error class/codes may be incorrect. Reason: " + Util.stackTraceToString(sqlEx), "S1000");

            if (interceptor != null) {
                SQLException interceptedEx = interceptor.interceptException(unexpectedEx, conn);

                if (interceptedEx != null) {
                    return interceptedEx;
                }
            }

            return unexpectedEx;
        }
    }

    public static SQLException createCommunicationsException(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException, ExceptionInterceptor interceptor) {
        SQLException exToReturn = null;

        if (!Util.isJdbc4()) {
            exToReturn = new CommunicationsException(conn, lastPacketSentTimeMs, lastPacketReceivedTimeMs, underlyingException);
        } else {

            try {
                exToReturn = (SQLException) Util.handleNewInstance(JDBC_4_COMMUNICATIONS_EXCEPTION_CTOR, new Object[]{conn, Long.valueOf(lastPacketSentTimeMs), Long.valueOf(lastPacketReceivedTimeMs), underlyingException}, interceptor);
            } catch (SQLException sqlEx) {

                return sqlEx;
            }
        }

        if (THROWABLE_INIT_CAUSE_METHOD != null && underlyingException != null) {
            try {
                THROWABLE_INIT_CAUSE_METHOD.invoke(exToReturn, new Object[]{underlyingException});
            } catch (Throwable t) {
            }
        }

        if (interceptor != null) {
            SQLException interceptedEx = interceptor.interceptException(exToReturn, conn);

            if (interceptedEx != null) {
                return interceptedEx;
            }
        }

        return exToReturn;
    }

    public static String createLinkFailureMessageBasedOnHeuristics(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException, boolean streamingResultSetInPlay) {
        long serverTimeoutSeconds = 0L;
        boolean isInteractiveClient = false;

        if (conn != null) {
            isInteractiveClient = conn.getInteractiveClient();

            String serverTimeoutSecondsStr = null;

            if (isInteractiveClient) {
                serverTimeoutSecondsStr = conn.getServerVariable("interactive_timeout");
            } else {

                serverTimeoutSecondsStr = conn.getServerVariable("wait_timeout");
            }

            if (serverTimeoutSecondsStr != null) {
                try {
                    serverTimeoutSeconds = Long.parseLong(serverTimeoutSecondsStr);
                } catch (NumberFormatException nfe) {
                    serverTimeoutSeconds = 0L;
                }
            }
        }

        StringBuffer exceptionMessageBuf = new StringBuffer();

        long nowMs = System.currentTimeMillis();

        if (lastPacketSentTimeMs == 0L) {
            lastPacketSentTimeMs = nowMs;
        }

        long timeSinceLastPacketSentMs = nowMs - lastPacketSentTimeMs;
        long timeSinceLastPacketSeconds = timeSinceLastPacketSentMs / 1000L;

        long timeSinceLastPacketReceivedMs = nowMs - lastPacketReceivedTimeMs;

        int dueToTimeout = 0;

        StringBuffer timeoutMessageBuf = null;

        if (streamingResultSetInPlay) {
            exceptionMessageBuf.append(Messages.getString("CommunicationsException.ClientWasStreaming"));
        } else {

            if (serverTimeoutSeconds != 0L) {
                if (timeSinceLastPacketSeconds > serverTimeoutSeconds) {
                    dueToTimeout = 1;

                    timeoutMessageBuf = new StringBuffer();

                    timeoutMessageBuf.append(Messages.getString("CommunicationsException.2"));

                    if (!isInteractiveClient) {
                        timeoutMessageBuf.append(Messages.getString("CommunicationsException.3"));
                    } else {

                        timeoutMessageBuf.append(Messages.getString("CommunicationsException.4"));
                    }

                }

            } else if (timeSinceLastPacketSeconds > 28800L) {
                dueToTimeout = 2;

                timeoutMessageBuf = new StringBuffer();

                timeoutMessageBuf.append(Messages.getString("CommunicationsException.5"));

                timeoutMessageBuf.append(Messages.getString("CommunicationsException.6"));

                timeoutMessageBuf.append(Messages.getString("CommunicationsException.7"));

                timeoutMessageBuf.append(Messages.getString("CommunicationsException.8"));
            }

            if (dueToTimeout == 1 || dueToTimeout == 2) {

                if (lastPacketReceivedTimeMs != 0L) {
                    Object[] timingInfo = {Long.valueOf(timeSinceLastPacketReceivedMs), Long.valueOf(timeSinceLastPacketSentMs)};

                    exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfo", timingInfo));
                } else {

                    exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfoNoRecv", new Object[]{Long.valueOf(timeSinceLastPacketSentMs)}));
                }

                if (timeoutMessageBuf != null) {
                    exceptionMessageBuf.append(timeoutMessageBuf);
                }

                exceptionMessageBuf.append(Messages.getString("CommunicationsException.11"));

                exceptionMessageBuf.append(Messages.getString("CommunicationsException.12"));

                exceptionMessageBuf.append(Messages.getString("CommunicationsException.13"));

            } else if (underlyingException instanceof java.net.BindException) {
                if (conn.getLocalSocketAddress() != null && !Util.interfaceExists(conn.getLocalSocketAddress())) {

                    exceptionMessageBuf.append(Messages.getString("CommunicationsException.LocalSocketAddressNotAvailable"));
                } else {

                    exceptionMessageBuf.append(Messages.getString("CommunicationsException.TooManyClientConnections"));
                }
            }
        }

        if (exceptionMessageBuf.length() == 0) {

            exceptionMessageBuf.append(Messages.getString("CommunicationsException.20"));

            if (THROWABLE_INIT_CAUSE_METHOD == null && underlyingException != null) {

                exceptionMessageBuf.append(Messages.getString("CommunicationsException.21"));

                exceptionMessageBuf.append(Util.stackTraceToString(underlyingException));
            }

            if (conn != null && conn.getMaintainTimeStats() && !conn.getParanoid()) {

                exceptionMessageBuf.append("\n\n");
                if (lastPacketReceivedTimeMs != 0L) {
                    Object[] timingInfo = {Long.valueOf(timeSinceLastPacketReceivedMs), Long.valueOf(timeSinceLastPacketSentMs)};

                    exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfo", timingInfo));
                } else {

                    exceptionMessageBuf.append(Messages.getString("CommunicationsException.ServerPacketTimingInfoNoRecv", new Object[]{Long.valueOf(timeSinceLastPacketSentMs)}));
                }
            }
        }

        return exceptionMessageBuf.toString();
    }

    public static SQLException notImplemented() {
        if (Util.isJdbc4()) {
            try {
                return (SQLException) Class.forName("java.sql.SQLFeatureNotSupportedException").newInstance();

            } catch (Throwable t) {
            }
        }

        return new NotImplemented();
    }
}

