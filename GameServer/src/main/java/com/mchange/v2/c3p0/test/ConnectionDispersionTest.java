package com.mchange.v2.c3p0.test;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public final class ConnectionDispersionTest {
    private static final int DELAY_TIME = 120000;
    private static final int NUM_THREADS = 600;
    private static final Integer ZERO = new Integer(0);

    private static boolean should_go = false;

    private static DataSource cpds;

    private static int ready_count = 0;

    private static synchronized DataSource getDataSource() {
        return cpds;
    }

    private static synchronized void setDataSource(DataSource ds) {
        cpds = ds;
    }

    private static synchronized int ready() {
        return ++ready_count;
    }

    private static synchronized boolean isReady() {
        return (ready_count == 600);
    }

    private static synchronized void start() {
        should_go = true;
        ConnectionDispersionTest.class.notifyAll();
    }

    private static synchronized void stop() {
        should_go = false;
        ConnectionDispersionTest.class.notifyAll();
    }

    private static synchronized boolean shouldGo() {
        return should_go;
    }

    public static void main(String[] argv) {
        String jdbc_url = null;
        String username = null;
        String password = null;
        if (argv.length == 3) {

            jdbc_url = argv[0];
            username = argv[1];
            password = argv[2];
        } else if (argv.length == 1) {

            jdbc_url = argv[0];
            username = null;
            password = null;
        } else {

            usage();
        }
        if (!jdbc_url.startsWith("jdbc:")) {
            usage();
        }

        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setJdbcUrl(jdbc_url);
            ds.setUser(username);
            ds.setPassword(password);
            setDataSource((DataSource) ds);

            ds.getConnection().close();

            System.err.println("Generating thread list...");
            List<Thread> threads = new ArrayList(600);
            int i;
            for (i = 0; i < 600; i++) {

                Thread t = new CompeteThread();
                t.start();
                threads.add(t);
                Thread.currentThread();
                Thread.yield();
            }
            System.err.println("Thread list generated.");

            synchronized (ConnectionDispersionTest.class) {
                for (; !isReady(); ConnectionDispersionTest.class.wait()) ;
            }
            System.err.println("Starting the race.");
            start();

            System.err.println("Sleeping 120.0 seconds to let the race run");

            Thread.sleep(120000L);
            System.err.println("Stopping the race.");
            stop();

            System.err.println("Waiting for Threads to complete.");
            for (i = 0; i < 600; i++) {
                ((Thread) threads.get(i)).join();
            }
            Map<Object, Object> outcomeMap = new TreeMap<Object, Object>();
            for (int j = 0; j < 600; j++) {

                Integer outcome = new Integer(((CompeteThread) threads.get(j)).getCount());
                Integer old = (Integer) outcomeMap.get(outcome);
                if (old == null)
                    old = ZERO;
                outcomeMap.put(outcome, new Integer(old.intValue() + 1));
            }

            int last = 0;
            for (Iterator<Integer> ii = outcomeMap.keySet().iterator(); ii.hasNext(); ) {
                Integer outcome = ii.next();
                Integer count = (Integer) outcomeMap.get(outcome);
                int oc = outcome.intValue();
                int c = count.intValue();
                for (; last < oc; last++)
                    System.out.println(String.valueOf(10000 + last).substring(1) + ": ");
                last++;
                System.out.print(String.valueOf(10000 + oc).substring(1) + ": ");

                for (int k = 0; k < c; k++)
                    System.out.print('*');
                System.out.println();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void usage() {
        System.err.println("java -Djdbc.drivers=<comma_sep_list_of_drivers> " + ConnectionDispersionTest.class.getName() + " <jdbc_url> [<username> <password>]");

        System.exit(-1);
    }

    static class CompeteThread extends Thread {
        DataSource ds;
        int count;

        synchronized void increment() {
            this.count++;
        }

        synchronized int getCount() {
            return this.count;
        }

        public void run() {
            try {
                this.ds = ConnectionDispersionTest.getDataSource();
                synchronized (ConnectionDispersionTest.class) {

                    ConnectionDispersionTest.ready();
                    ConnectionDispersionTest.class.wait();
                }

                while (ConnectionDispersionTest.shouldGo()) {

                    Connection c = null;
                    ResultSet rs = null;

                    try {
                        c = this.ds.getConnection();
                        increment();
                        rs = c.getMetaData().getTables(null, null, "PROBABLYNOT", new String[]{"TABLE"});

                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (rs != null) rs.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (c != null) c.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

