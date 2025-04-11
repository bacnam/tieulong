package com.notnoop.apns.internal;

import com.notnoop.apns.ReconnectPolicy;

public final class ReconnectPolicies {
    public static class Never
            implements ReconnectPolicy {
        public void reconnected() {
        }

        public boolean shouldReconnect() {
            return false;
        }

        public Never copy() {
            return this;
        }
    }

    public static class Always implements ReconnectPolicy {
        public boolean shouldReconnect() {
            return true;
        }

        public Always copy() {
            return this;
        }

        public void reconnected() {
        }
    }

    public static class EveryHalfHour implements ReconnectPolicy {
        private static final long PERIOD = 1800000L;
        private long lastRunning = 0L;

        public boolean shouldReconnect() {
            return (System.currentTimeMillis() - this.lastRunning > 1800000L);
        }

        public void reconnected() {
            this.lastRunning = System.currentTimeMillis();
        }

        public EveryHalfHour copy() {
            return new EveryHalfHour();
        }
    }

}

