package com.mchange.v2.c3p0.test;

import com.mchange.v1.db.sql.ConnectionUtils;
import com.mchange.v1.db.sql.ResultSetUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public final class LoadPoolBackedDataSource {
    static final int NUM_THREADS = 100;
    static final int ITERATIONS_PER_THREAD = 1000;
    static DataSource ds;

    public static void main(String[] argv) {
        if (argv.length > 0) {

            System.err.println(LoadPoolBackedDataSource.class.getName() + " now requires no args. Please set everything in standard c3p0 config files.");

            return;
        }
        String jdbc_url = null;
        String username = null;
        String password = null;

        try {
            DataSource ds_unpooled = DataSources.unpooledDataSource();
            ds = DataSources.pooledDataSource(ds_unpooled);

            Connection connection1 = null;
            Statement statement1 = null;

            try {
                connection1 = ds.getConnection();
                statement1 = connection1.createStatement();
                statement1.executeUpdate("CREATE TABLE testpbds ( a varchar(16), b varchar(16) )");
                System.err.println("LoadPoolBackedDataSource -- TEST SCHEMA CREATED");
            } catch (SQLException e) {

                e.printStackTrace();
                System.err.println("relation testpbds already exists, or something bad happened.");

            } finally {

                StatementUtils.attemptClose(statement1);
                ConnectionUtils.attemptClose(connection1);
            }

            Thread[] threads = new Thread[100];
            int i;
            for (i = 0; i < 100; i++) {

                Thread t = new ChurnThread(i);
                threads[i] = t;
                t.start();
                System.out.println("THREAD MADE [" + i + "]");
                Thread.sleep(500L);
            }
            for (i = 0; i < 100; i++) {
                threads[i].join();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            Connection con = null;
            Statement stmt = null;

            try {
                con = ds.getConnection();
                stmt = con.createStatement();
                stmt.executeUpdate("DROP TABLE testpbds");
                System.err.println("LoadPoolBackedDataSource -- TEST SCHEMA DROPPED");
            } catch (Exception e) {

                e.printStackTrace();
            } finally {

                StatementUtils.attemptClose(stmt);
                ConnectionUtils.attemptClose(con);
            }
        }
    }

    static void executeInsert(Connection con, Random random) throws SQLException {
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO testpbds VALUES ('" + random.nextInt() + "', '" + random.nextInt() + "')");

            System.out.println("INSERTION");
        } finally {

            StatementUtils.attemptClose(stmt);
        }
    }

    static void executeDelete(Connection con) throws SQLException {
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate("DELETE FROM testpbds;");
            System.out.println("DELETION");
        } finally {

            StatementUtils.attemptClose(stmt);
        }
    }

    static void executeSelect(Connection con) throws SQLException {
        long l = System.currentTimeMillis();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT count(*) FROM testpbds");
            rs.next();
            System.out.println("SELECT [count=" + rs.getInt(1) + ", time=" + (System.currentTimeMillis() - l) + " msecs]");

        } finally {

            ResultSetUtils.attemptClose(rs);
            StatementUtils.attemptClose(stmt);
        }
    }

    private static void usage() {
        System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + LoadPoolBackedDataSource.class.getName() + " <jdbc_url> [<username> <password>]");

        System.exit(-1);
    }

    static class ChurnThread
            extends Thread {
        Random random = new Random();

        int num;

        public ChurnThread(int num) {
            this.num = num;
        }

        public void run() {
            try {
                for (int i = 0; i < 1000; i++) {
                    Connection con = null;

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

