package com.mchange.v2.c3p0.impl;

import com.mchange.v1.db.sql.ResultSetUtils;
import com.mchange.v1.db.sql.StatementUtils;
import com.mchange.v2.c3p0.AbstractConnectionTester;
import com.mchange.v2.c3p0.cfg.C3P0Config;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultConnectionTester
        extends AbstractConnectionTester {
    static final MLogger logger = MLog.getLogger(DefaultConnectionTester.class);

    static final int IS_VALID_TIMEOUT = 0;

    static final String CONNECTION_TESTING_URL = "http:";
    static final int HASH_CODE = DefaultConnectionTester.class.getName().hashCode();

    static final Set INVALID_DB_STATES;

    static final QuerylessTestRunner METADATA_TABLESEARCH = new QuerylessTestRunner() {

        public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder) {
            ResultSet rs = null;

            try {
                rs = c.getMetaData().getTables(null, null, "PROBABLYNOT", new String[]{"TABLE"});

                return 0;
            } catch (SQLException e) {

                if (DefaultConnectionTester.logger.isLoggable(MLevel.FINE)) {
                    DefaultConnectionTester.logger.log(MLevel.FINE, "Connection " + c + " failed default system-table Connection test with an Exception!", e);
                }
                if (rootCauseOutParamHolder != null) {
                    rootCauseOutParamHolder[0] = e;
                }
                String state = e.getSQLState();
                if (DefaultConnectionTester.INVALID_DB_STATES.contains(state)) {

                    if (DefaultConnectionTester.logger.isLoggable(MLevel.WARNING)) {
                        DefaultConnectionTester.logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception which occurred during a Connection test (fallback DatabaseMetaData test) implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", e);
                    }

                    return -8;
                }

                return -1;
            } catch (Exception e) {

                if (DefaultConnectionTester.logger.isLoggable(MLevel.FINE)) {
                    DefaultConnectionTester.logger.log(MLevel.FINE, "Connection " + c + " failed default system-table Connection test with an Exception!", e);
                }
                if (rootCauseOutParamHolder != null) {
                    rootCauseOutParamHolder[0] = e;
                }
                return -1;
            } finally {

                ResultSetUtils.attemptClose(rs);
            }
        }
    };

    static final QuerylessTestRunner IS_VALID = new QuerylessTestRunner() {

        public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder) {
            try {
                boolean okay = c.isValid(0);
                if (okay) {
                    return 0;
                }

                if (rootCauseOutParamHolder != null)
                    rootCauseOutParamHolder[0] = new SQLException("Connection.isValid(0) returned false.");
                return -1;

            } catch (SQLException e) {

                if (rootCauseOutParamHolder != null) {
                    rootCauseOutParamHolder[0] = e;
                }
                String state = e.getSQLState();
                if (DefaultConnectionTester.INVALID_DB_STATES.contains(state)) {

                    if (DefaultConnectionTester.logger.isLoggable(MLevel.WARNING)) {
                        DefaultConnectionTester.logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception which occurred during a Connection test (fallback DatabaseMetaData test) implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", e);
                    }

                    return -8;
                }

                return -1;
            } catch (Exception e) {

                if (rootCauseOutParamHolder != null) {
                    rootCauseOutParamHolder[0] = e;
                }
                return -1;
            }
        }
    };

    static final QuerylessTestRunner SWITCH = new QuerylessTestRunner() {
        public int activeCheckConnectionNoQuery(Connection c, Throwable[] rootCauseOutParamHolder) {
            int i;
            try {
                i = DefaultConnectionTester.IS_VALID.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
            } catch (AbstractMethodError e) {
                i = DefaultConnectionTester.METADATA_TABLESEARCH.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
            }
            return i;
        }
    };

    static final QuerylessTestRunner THREAD_LOCAL = new ThreadLocalQuerylessTestRunner();

    private static final String PROP_KEY = "com.mchange.v2.c3p0.impl.DefaultConnectionTester.querylessTestRunner";

    static {
        Set<String> temp = new HashSet();
        temp.add("08001");
        temp.add("08007");

        INVALID_DB_STATES = Collections.unmodifiableSet(temp);
    }

    private final QuerylessTestRunner querylessTestRunner;

    public DefaultConnectionTester() {
        QuerylessTestRunner defaultQuerylessTestRunner = SWITCH;

        String prop = C3P0Config.getMultiPropertiesConfig().getProperty("com.mchange.v2.c3p0.impl.DefaultConnectionTester.querylessTestRunner");
        if (prop == null) {
            this.querylessTestRunner = defaultQuerylessTestRunner;
        } else {

            QuerylessTestRunner reflected = reflectTestRunner(prop.trim());
            this.querylessTestRunner = (reflected != null) ? reflected : defaultQuerylessTestRunner;
        }
    }

    private static QuerylessTestRunner reflectTestRunner(String propval) {
        try {
            if (propval.indexOf('.') >= 0) {
                return (QuerylessTestRunner) Class.forName(propval).newInstance();
            }

            Field staticField = DefaultConnectionTester.class.getDeclaredField(propval);
            return (QuerylessTestRunner) staticField.get(null);

        } catch (Exception e) {

            if (logger.isLoggable(MLevel.WARNING))
                logger.log(MLevel.WARNING, "Specified QuerylessTestRunner '" + propval + "' could not be found or instantiated. Reverting to default 'SWITCH'", e);
            return null;
        }
    }

    private static String queryInfo(String query) {
        return (query == null) ? "[using Connection.isValid(...) if supported, or else traditional default query]" : ("[query=" + query + "]");
    }

    public int activeCheckConnection(Connection c, String query, Throwable[] rootCauseOutParamHolder) {
        if (query == null) {
            return this.querylessTestRunner.activeCheckConnectionNoQuery(c, rootCauseOutParamHolder);
        }

        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = c.createStatement();
            rs = stmt.executeQuery(query);

            return 0;
        } catch (SQLException e) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "Connection " + c + " failed Connection test with an Exception! [query=" + query + "]", e);
            }
            if (rootCauseOutParamHolder != null) {
                rootCauseOutParamHolder[0] = e;
            }
            String state = e.getSQLState();
            if (INVALID_DB_STATES.contains(state)) {

                if (logger.isLoggable(MLevel.WARNING)) {
                    logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception which occurred during a Connection test (test with query '" + query + "') implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", e);
                }

                return -8;
            }

            return -1;
        } catch (Exception e) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "Connection " + c + " failed Connection test with an Exception!", e);
            }
            if (rootCauseOutParamHolder != null) {
                rootCauseOutParamHolder[0] = e;
            }
            return -1;
        } finally {

            ResultSetUtils.attemptClose(rs);
            StatementUtils.attemptClose(stmt);
        }
    }

    public int statusOnException(Connection c, Throwable t, String query, Throwable[] rootCauseOutParamHolder) {
        if (logger.isLoggable(MLevel.FINER)) {
            logger.log(MLevel.FINER, "Testing a Connection in response to an Exception:", t);
        }

        try {
            if (t instanceof SQLException) {

                String state = ((SQLException) t).getSQLState();
                if (INVALID_DB_STATES.contains(state)) {

                    if (logger.isLoggable(MLevel.WARNING)) {
                        logger.log(MLevel.WARNING, "SQL State '" + state + "' of Exception tested by statusOnException() implies that the database is invalid, " + "and the pool should refill itself with fresh Connections.", t);
                    }

                    return -8;
                }

                return activeCheckConnection(c, query, rootCauseOutParamHolder);
            }

            if (logger.isLoggable(MLevel.FINE))
                logger.log(MLevel.FINE, "Connection test failed because test-provoking Throwable is an unexpected, non-SQLException.", t);
            if (rootCauseOutParamHolder != null)
                rootCauseOutParamHolder[0] = t;
            return -1;

        } catch (Exception e) {

            if (logger.isLoggable(MLevel.FINE)) {
                logger.log(MLevel.FINE, "Connection " + c + " failed Connection test with an Exception!", e);
            }
            if (rootCauseOutParamHolder != null) {
                rootCauseOutParamHolder[0] = e;
            }
            return -1;
        } finally {
        }
    }

    public boolean equals(Object o) {
        return (o != null && o.getClass() == DefaultConnectionTester.class);
    }

    public int hashCode() {
        return HASH_CODE;
    }

    public static interface QuerylessTestRunner {
        int activeCheckConnectionNoQuery(Connection param1Connection, Throwable[] param1ArrayOfThrowable);
    }
}

