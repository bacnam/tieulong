package com.mchange.v2.resourcepool;

import com.mchange.v1.util.ClosableResource;

public interface ResourcePool extends ClosableResource {
  public static final int KNOWN_AND_AVAILABLE = 0;

  public static final int KNOWN_AND_CHECKED_OUT = 1;

  public static final int UNKNOWN_OR_PURGED = -1;

  Object checkoutResource() throws ResourcePoolException, InterruptedException;

  Object checkoutResource(long paramLong) throws TimeoutException, ResourcePoolException, InterruptedException;

  void checkinResource(Object paramObject) throws ResourcePoolException;

  void checkinAll() throws ResourcePoolException;

  int statusInPool(Object paramObject) throws ResourcePoolException;

  void markBroken(Object paramObject) throws ResourcePoolException;

  int getMinPoolSize() throws ResourcePoolException;

  int getMaxPoolSize() throws ResourcePoolException;

  int getPoolSize() throws ResourcePoolException;

  void setPoolSize(int paramInt) throws ResourcePoolException;

  int getAvailableCount() throws ResourcePoolException;

  int getExcludedCount() throws ResourcePoolException;

  int getAwaitingCheckinCount() throws ResourcePoolException;

  long getEffectiveExpirationEnforcementDelay() throws ResourcePoolException;

  long getStartTime() throws ResourcePoolException;

  long getUpTime() throws ResourcePoolException;

  long getNumFailedCheckins() throws ResourcePoolException;

  long getNumFailedCheckouts() throws ResourcePoolException;

  long getNumFailedIdleTests() throws ResourcePoolException;

  int getNumCheckoutWaiters() throws ResourcePoolException;

  Throwable getLastAcquisitionFailure() throws ResourcePoolException;

  Throwable getLastCheckinFailure() throws ResourcePoolException;

  Throwable getLastCheckoutFailure() throws ResourcePoolException;

  Throwable getLastIdleCheckFailure() throws ResourcePoolException;

  Throwable getLastResourceTestFailure() throws ResourcePoolException;

  void resetPool() throws ResourcePoolException;

  void close() throws ResourcePoolException;

  void close(boolean paramBoolean) throws ResourcePoolException;

  public static interface Manager {
    Object acquireResource() throws Exception;

    void refurbishIdleResource(Object param1Object) throws Exception;

    void refurbishResourceOnCheckout(Object param1Object) throws Exception;

    void refurbishResourceOnCheckin(Object param1Object) throws Exception;

    void destroyResource(Object param1Object, boolean param1Boolean) throws Exception;
  }
}

