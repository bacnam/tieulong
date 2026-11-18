package com.zhonglian.server.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLUtil {
    public static Exception close(AutoCloseable rs) {
        try {
            if (rs != null) {
                rs.close();
            }

            return null;
        } catch (Exception var2) {
            return var2;
        }
    }

    public static void close(ResultSet rs, Statement pstm, Connection con) {
        close(rs);
        close(pstm);
        close(con);
    }
}
