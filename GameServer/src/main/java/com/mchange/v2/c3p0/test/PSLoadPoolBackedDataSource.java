package com.mchange.v2.c3p0.test;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.ResultSetUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Random;

public final class PSLoadPoolBackedDataSource {
    static final String INSERT_STMT = "INSERT INTO testpbds VALUES ( ? , ? )";
    static final String SELECT_STMT = "SELECT count(*) FROM testpbds";
    static final String DELETE_STMT = "DELETE FROM testpbds";
    static DataSource ds;

    public static void main(String[] argv) {
        if (argv.length > 0) {

            System.err.println(PSLoadPoolBackedDataSource.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");

            return;
        }

        String jdbc_url = null;
        String username = null;
        String password = null;

        try {
            DataSource ds_unpooled = DataSources.unpooledDataSource();
            ds = DataSources.pooledDataSource(ds_unpooled);

            Connection con = null;
            Statement stmt = null;

            try {
                con = ds_unpooled.getConnection();
                stmt = con.createStatement();
                stmt.executeUpdate("CREATE TABLE testpbds ( a varchar(16), b varchar(16) )");
            } catch (SQLException e) {

                e.printStackTrace();
                System.err.println("relation testpbds already exists, or something bad happened.");

            } finally {

                StatementUtils.attemptClose(stmt);
                ConnectionUtils.attemptClose(con);
            }

            for (int i = 0; i < 100; i++) {
                Thread t = new ChurnThread();
                t.start();
                System.out.println("THREAD MADE [" + i + "]");
                Thread.sleep(500L);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void executeInsert(Connection con, Random random) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement("INSERT INTO testpbds VALUES ( ? , ? )");
            pstmt.setInt(1, random.nextInt());
            pstmt.setInt(2, random.nextInt());
            pstmt.executeUpdate();
            System.out.println("INSERTION");

        } finally {

            StatementUtils.attemptClose(pstmt);
        }
    }

    static void executeSelect(Connection con, Random random) throws SQLException {
        long l = System.currentTimeMillis();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement("SELECT count(*) FROM testpbds");
            rs = pstmt.executeQuery();
            rs.next();
            System.out.println("SELECT [count=" + rs.getInt(1) + ", time=" + (System.currentTimeMillis() - l) + " msecs]");

        } finally {

            ResultSetUtils.attemptClose(rs);
            StatementUtils.attemptClose(pstmt);
        }
    }

    static void executeDelete(Connection con, Random random) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement("DELETE FROM testpbds");
            int deleted = pstmt.executeUpdate();
            System.out.println("DELETE [" + deleted + " rows]");
        } finally {

            ResultSetUtils.attemptClose(rs);
            StatementUtils.attemptClose(pstmt);
        }
    }

    static class ChurnThread extends Thread {
        Random random = new Random();

        public void run() {
            try {
                while (true) {
                    Connection con = null;

                    try {
                        con = PSLoadPoolBackedDataSource.ds.getConnection();
                        int select = this.random.nextInt(3);
                        switch (select) {

                            case 0:
                                PSLoadPoolBackedDataSource.executeSelect(con, this.random);
                                break;
                            case 1:
                                PSLoadPoolBackedDataSource.executeInsert(con, this.random);
                                break;
                            case 2:
                                PSLoadPoolBackedDataSource.executeDelete(con, this.random);
                                break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ConnectionUtils.attemptClose(con);
                    }

                    Thread.sleep(1L);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }
}

