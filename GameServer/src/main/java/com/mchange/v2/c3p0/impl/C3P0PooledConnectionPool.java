package com.mchange.v2.c3p0.impl;

import com.mchange.v2.async.AsynchronousRunner;
import com.mchange.v2.async.ThreadPoolAsynchronousRunner;
import com.mchange.v2.c3p0.ConnectionCustomizer;
import com.mchange.v2.c3p0.ConnectionTester;
import com.mchange.v2.c3p0.stmt.DoubleMaxStatementCache;
import com.mchange.v2.c3p0.stmt.GlobalMaxOnlyStatementCache;
import com.mchange.v2.c3p0.stmt.GooGooStatementCache;
import com.mchange.v2.c3p0.stmt.PerConnectionMaxOnlyStatementCache;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;
import com.mchange.v2.resourcepool.*;
import com.mchange.v2.sql.SqlUtils;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;

public final class C3P0PooledConnectionPool {
    static final MLogger logger = MLog.getLogger(C3P0PooledConnectionPool.class);
    private static final boolean ASYNCHRONOUS_CONNECTION_EVENT_LISTENER = false;
    private static final Throwable[] EMPTY_THROWABLE_HOLDER = new Throwable[1];
    private static InUseLockFetcher RESOURCE_ITSELF_IN_USE_LOCK_FETCHER = new ResourceItselfInUseLockFetcher();
    private static InUseLockFetcher C3P0_POOLED_CONNECION_NESTED_LOCK_LOCK_FETCHER = new C3P0PooledConnectionNestedLockLockFetcher();
    final ResourcePool rp;
    final ConnectionEventListener cl = new ConnectionEventListenerImpl();
    final ConnectionTester connectionTester;
    final GooGooStatementCache scache;
    final boolean c3p0PooledConnections;
    final boolean effectiveStatementCache;
    final int checkoutTimeout;
    final AsynchronousRunner sharedTaskRunner;
    final AsynchronousRunner deferredStatementDestroyer;
    final ThrowableHolderPool thp = new ThrowableHolderPool();
    final InUseLockFetcher inUseLockFetcher;

    C3P0PooledConnectionPool(final ConnectionPoolDataSource cpds, final DbAuth auth, int min, int max, int start, int inc, int acq_retry_attempts, int acq_retry_delay, boolean break_after_acq_failure, int checkoutTimeout, int idleConnectionTestPeriod, int maxIdleTime, int maxIdleTimeExcessConnections, int maxConnectionAge, int propertyCycle, int unreturnedConnectionTimeout, boolean debugUnreturnedConnectionStackTraces, final boolean testConnectionOnCheckout, final boolean testConnectionOnCheckin, int maxStatements, int maxStatementsPerConnection, final ConnectionTester connectionTester, final ConnectionCustomizer connectionCustomizer, final String testQuery, ResourcePoolFactory fact, ThreadPoolAsynchronousRunner taskRunner, ThreadPoolAsynchronousRunner deferredStatementDestroyer, final String parentDataSourceIdentityToken) throws SQLException {
        try {
            if (maxStatements > 0 && maxStatementsPerConnection > 0) {
                this.scache = (GooGooStatementCache) new DoubleMaxStatementCache((AsynchronousRunner) taskRunner, (AsynchronousRunner) deferredStatementDestroyer, maxStatements, maxStatementsPerConnection);
            } else if (maxStatementsPerConnection > 0) {
                this.scache = (GooGooStatementCache) new PerConnectionMaxOnlyStatementCache((AsynchronousRunner) taskRunner, (AsynchronousRunner) deferredStatementDestroyer, maxStatementsPerConnection);
            } else if (maxStatements > 0) {
                this.scache = (GooGooStatementCache) new GlobalMaxOnlyStatementCache((AsynchronousRunner) taskRunner, (AsynchronousRunner) deferredStatementDestroyer, maxStatements);
            } else {
                this.scache = null;
            }
            this.connectionTester = connectionTester;

            this.checkoutTimeout = checkoutTimeout;

            this.sharedTaskRunner = (AsynchronousRunner) taskRunner;
            this.deferredStatementDestroyer = (AsynchronousRunner) deferredStatementDestroyer;

            this.c3p0PooledConnections = cpds instanceof com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
            this.effectiveStatementCache = (this.c3p0PooledConnections && this.scache != null);

            this.inUseLockFetcher = this.c3p0PooledConnections ? C3P0_POOLED_CONNECION_NESTED_LOCK_LOCK_FETCHER : RESOURCE_ITSELF_IN_USE_LOCK_FETCHER;

            ResourcePool.Manager manager = new PooledConnectionResourcePoolManager();

            synchronized (fact) {
                fact.setMin(min);
                fact.setMax(max);
                fact.setStart(start);
                fact.setIncrement(inc);
                fact.setIdleResourceTestPeriod((idleConnectionTestPeriod * 1000));
                fact.setResourceMaxIdleTime((maxIdleTime * 1000));
                fact.setExcessResourceMaxIdleTime((maxIdleTimeExcessConnections * 1000));
                fact.setResourceMaxAge((maxConnectionAge * 1000));
                fact.setExpirationEnforcementDelay((propertyCycle * 1000));
                fact.setDestroyOverdueResourceTime((unreturnedConnectionTimeout * 1000));
                fact.setDebugStoreCheckoutStackTrace(debugUnreturnedConnectionStackTraces);
                fact.setAcquisitionRetryAttempts(acq_retry_attempts);
                fact.setAcquisitionRetryDelay(acq_retry_delay);
                fact.setBreakOnAcquisitionFailure(break_after_acq_failure);
                this.rp = fact.createPool(manager);
            }

        } catch (ResourcePoolException e) {
            throw SqlUtils.toSQLException(e);
        }
    }

    public int getStatementDestroyerNumConnectionsInUse() {
        return (this.scache == null) ? -1 : this.scache.getStatementDestroyerNumConnectionsInUse();
    }

    public int getStatementDestroyerNumConnectionsWithDeferredDestroyStatements() {
        return (this.scache == null) ? -1 : this.scache.getStatementDestroyerNumConnectionsWithDeferredDestroyStatements();
    }

    public int getStatementDestroyerNumDeferredDestroyStatements() {
        return (this.scache == null) ? -1 : this.scache.getStatementDestroyerNumDeferredDestroyStatements();
    }

    public PooledConnection checkoutPooledConnection() throws SQLException {
        try {
            PooledConnection pc = (PooledConnection) checkoutAndMarkConnectionInUse();
            pc.addConnectionEventListener(this.cl);
            return pc;
        } catch (TimeoutException e) {
            throw SqlUtils.toSQLException("An attempt by a client to checkout a Connection has timed out.", e);
        } catch (CannotAcquireResourceException e) {
            throw SqlUtils.toSQLException("Connections could not be acquired from the underlying database!", "08001", e);
        } catch (Exception e) {
            throw SqlUtils.toSQLException(e);
        }
    }

    private void waitMarkPhysicalConnectionInUse(Connection physicalConnection) throws InterruptedException {
        if (this.effectiveStatementCache)
            this.scache.waitMarkConnectionInUse(physicalConnection);
    }

    private boolean tryMarkPhysicalConnectionInUse(Connection physicalConnection) {
        return this.effectiveStatementCache ? this.scache.tryMarkConnectionInUse(physicalConnection) : true;
    }

    private void unmarkPhysicalConnectionInUse(Connection physicalConnection) {
        if (this.effectiveStatementCache) {
            this.scache.unmarkConnectionInUse(physicalConnection);
        }
    }

    private void waitMarkPooledConnectionInUse(PooledConnection pooledCon) throws InterruptedException {
        if (this.c3p0PooledConnections) {
            waitMarkPhysicalConnectionInUse(((AbstractC3P0PooledConnection) pooledCon).getPhysicalConnection());
        }
    }

    private boolean tryMarkPooledConnectionInUse(PooledConnection pooledCon) {
        if (this.c3p0PooledConnections) {
            return tryMarkPhysicalConnectionInUse(((AbstractC3P0PooledConnection) pooledCon).getPhysicalConnection());
        }
        return true;
    }

    private void unmarkPooledConnectionInUse(PooledConnection pooledCon) {
        if (this.c3p0PooledConnections) {
            unmarkPhysicalConnectionInUse(((AbstractC3P0PooledConnection) pooledCon).getPhysicalConnection());
        }
    }

    private Boolean physicalConnectionInUse(Connection physicalConnection) throws InterruptedException {
        if (physicalConnection != null && this.effectiveStatementCache) {
            return this.scache.inUse(physicalConnection);
        }
        return null;
    }

    private Boolean pooledConnectionInUse(PooledConnection pc) throws InterruptedException {
        if (pc != null && this.effectiveStatementCache) {
            return this.scache.inUse(((AbstractC3P0PooledConnection) pc).getPhysicalConnection());
        }
        return null;
    }

    private Object checkoutAndMarkConnectionInUse() throws TimeoutException, CannotAcquireResourceException, ResourcePoolException, InterruptedException {
        Object out = null;
        boolean success = false;
        while (!success) {

            try {
                out = this.rp.checkoutResource(this.checkoutTimeout);
                if (out instanceof AbstractC3P0PooledConnection) {

                    AbstractC3P0PooledConnection acpc = (AbstractC3P0PooledConnection) out;
                    Connection physicalConnection = acpc.getPhysicalConnection();
                    success = tryMarkPhysicalConnectionInUse(physicalConnection);
                } else {

                    success = true;
                }
            } finally {

                try {
                    if (!success && out != null) this.rp.checkinResource(out);
                } catch (Exception e) {
                    logger.log(MLevel.WARNING, "Failed to check in a Connection that was unusable due to pending Statement closes.", e);
                }

            }
        }
        return out;
    }

    private void unmarkConnectionInUseAndCheckin(PooledConnection pcon) throws ResourcePoolException {
        if (this.effectiveStatementCache) {

            try {

                AbstractC3P0PooledConnection acpc = (AbstractC3P0PooledConnection) pcon;
                Connection physicalConnection = acpc.getPhysicalConnection();
                unmarkPhysicalConnectionInUse(physicalConnection);
            } catch (ClassCastException e) {

                if (logger.isLoggable(MLevel.SEVERE)) {
                    logger.log(MLevel.SEVERE, "You are checking a non-c3p0 PooledConnection implementation intoa c3p0 PooledConnectionPool instance that expects only c3p0-generated PooledConnections.This isn't good, and may indicate a c3p0 bug, or an unusual (and unspported) use of the c3p0 library.", e);
                }
            }
        }

        this.rp.checkinResource(pcon);
    }

    public void checkinPooledConnection(PooledConnection pcon) throws SQLException {
        try {
            pcon.removeConnectionEventListener(this.cl);
            unmarkConnectionInUseAndCheckin(pcon);
        } catch (ResourcePoolException e) {
            throw SqlUtils.toSQLException(e);
        }
    }

    public float getEffectivePropertyCycle() throws SQLException {
        try {
            return (float) this.rp.getEffectiveExpirationEnforcementDelay() / 1000.0F;
        } catch (ResourcePoolException e) {
            throw SqlUtils.toSQLException(e);
        }
    }

    public int getNumThreadsAwaitingCheckout() throws SQLException {
        try {
            return this.rp.getNumCheckoutWaiters();
        } catch (ResourcePoolException e) {
            throw SqlUtils.toSQLException(e);
        }
    }

    public int getStatementCacheNumStatements() {
        return (this.scache == null) ? 0 : this.scache.getNumStatements();
    }

    public int getStatementCacheNumCheckedOut() {
        return (this.scache == null) ? 0 : this.scache.getNumStatementsCheckedOut();
    }

    public int getStatementCacheNumConnectionsWithCachedStatements() {
        return (this.scache == null) ? 0 : this.scache.getNumConnectionsWithCachedStatements();
    }

    public String dumpStatementCacheStatus() {
        return (this.scache == null) ? "Statement caching disabled." : this.scache.dumpStatementCacheStatus();
    }

    public void close() throws SQLException {
        close(true);
    }

    public void close(boolean close_outstanding_connections) throws SQLException {
        ResourcePoolException resourcePoolException;
        Exception throwMe = null;
        try {
            if (this.scache != null) this.scache.close();
        } catch (SQLException e) {
            throwMe = e;
        }
        try {
            this.rp.close(close_outstanding_connections);
        } catch (ResourcePoolException e) {

            if (throwMe != null && logger.isLoggable(MLevel.WARNING))
                logger.log(MLevel.WARNING, "An Exception occurred while closing the StatementCache.", throwMe);
            resourcePoolException = e;
        }

        if (resourcePoolException != null) {
            throw SqlUtils.toSQLException(resourcePoolException);
        }
    }

    public int getNumConnections() throws SQLException {
        try {
            return this.rp.getPoolSize();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public int getNumIdleConnections() throws SQLException {
        try {
            return this.rp.getAvailableCount();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public int getNumBusyConnections() throws SQLException {
        try {
            synchronized (this.rp) {
                return this.rp.getAwaitingCheckinCount() - this.rp.getExcludedCount();
            }
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public int getNumUnclosedOrphanedConnections() throws SQLException {
        try {
            return this.rp.getExcludedCount();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public long getStartTime() throws SQLException {
        try {
            return this.rp.getStartTime();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public long getUpTime() throws SQLException {
        try {
            return this.rp.getUpTime();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public long getNumFailedCheckins() throws SQLException {
        try {
            return this.rp.getNumFailedCheckins();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public long getNumFailedCheckouts() throws SQLException {
        try {
            return this.rp.getNumFailedCheckouts();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public long getNumFailedIdleTests() throws SQLException {
        try {
            return this.rp.getNumFailedIdleTests();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public Throwable getLastCheckinFailure() throws SQLException {
        try {
            return this.rp.getLastCheckinFailure();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public Throwable getLastCheckoutFailure() throws SQLException {
        try {
            return this.rp.getLastCheckoutFailure();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public Throwable getLastIdleTestFailure() throws SQLException {
        try {
            return this.rp.getLastIdleCheckFailure();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public Throwable getLastConnectionTestFailure() throws SQLException {
        try {
            return this.rp.getLastResourceTestFailure();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public Throwable getLastAcquisitionFailure() throws SQLException {
        try {
            return this.rp.getLastAcquisitionFailure();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    public void reset() throws SQLException {
        try {
            this.rp.resetPool();
        } catch (Exception e) {

            logger.log(MLevel.WARNING, null, e);
            throw SqlUtils.toSQLException(e);
        }
    }

    private static interface InUseLockFetcher {
        Object getInUseLock(Object param1Object);
    }

    private static class ResourceItselfInUseLockFetcher
            implements InUseLockFetcher {
        private ResourceItselfInUseLockFetcher() {
        }

        public Object getInUseLock(Object resc) {
            return resc;
        }
    }

    private static class C3P0PooledConnectionNestedLockLockFetcher implements InUseLockFetcher {
        private C3P0PooledConnectionNestedLockLockFetcher() {
        }

        public Object getInUseLock(Object resc) {
            return ((AbstractC3P0PooledConnection) resc).inInternalUseLock;
        }
    }

    static final class ThrowableHolderPool {
        LinkedList l = new LinkedList();

        synchronized Throwable[] getThrowableHolder() {
            if (this.l.size() == 0) {
                return new Throwable[1];
            }
            return this.l.remove(0);
        }

        synchronized void returnThrowableHolder(Throwable[] th) {
            th[0] = null;
            this.l.add(th);
        }
    }

    class ConnectionEventListenerImpl
            implements ConnectionEventListener {
        public void connectionClosed(ConnectionEvent evt) {
            doCheckinResource(evt);
        }

        private void doCheckinResource(ConnectionEvent evt) {
            try {
                C3P0PooledConnectionPool.this.checkinPooledConnection((PooledConnection) evt.getSource());
            } catch (Exception e) {

                C3P0PooledConnectionPool.logger.log(MLevel.WARNING, "An Exception occurred while trying to check a PooledConection into a ResourcePool.", e);
            }
        }

        public void connectionErrorOccurred(ConnectionEvent evt) {
            int status;
            if (C3P0PooledConnectionPool.logger.isLoggable(MLevel.FINE)) {
                C3P0PooledConnectionPool.logger.fine("CONNECTION ERROR OCCURRED!");
            }
            PooledConnection pc = (PooledConnection) evt.getSource();

            if (pc instanceof C3P0PooledConnection) {
                status = ((C3P0PooledConnection) pc).getConnectionStatus();
            } else if (pc instanceof NewPooledConnection) {
                status = ((NewPooledConnection) pc).getConnectionStatus();
            } else {
                status = -1;
            }
            int final_status = status;

            doMarkPoolStatus(pc, final_status);
        }

        private void doMarkPoolStatus(PooledConnection pc, int status) {
            try {
                switch (status) {

                    case 0:
                        throw new RuntimeException("connectionErrorOcccurred() should only be called for errors fatal to the Connection.");

                    case -1:
                        C3P0PooledConnectionPool.this.rp.markBroken(pc);
                        return;
                    case -8:
                        if (C3P0PooledConnectionPool.logger.isLoggable(MLevel.WARNING)) {
                            C3P0PooledConnectionPool.logger.warning("A ConnectionTest has failed, reporting that all previously acquired Connections are likely invalid. The pool will be reset.");
                        }
                        C3P0PooledConnectionPool.this.rp.resetPool();
                        return;
                }
                throw new RuntimeException("Bad Connection Tester (" + C3P0PooledConnectionPool.this.connectionTester + ") " + "returned invalid status (" + status + ").");

            } catch (ResourcePoolException e) {

                C3P0PooledConnectionPool.logger.log(MLevel.WARNING, "Uh oh... our resource pool is probably broken!", (Throwable) e);
            }
        }
    }
}

