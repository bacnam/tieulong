package com.facebook.fb303;

import java.util.AbstractMap;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FacebookBase
        implements FacebookService.Iface {
    private final ConcurrentHashMap<String, Long> counters_ = new ConcurrentHashMap<String, Long>();
    private final ConcurrentHashMap<String, String> options_ = new ConcurrentHashMap<String, String>();
    private String name_;
    private long alive_;

    protected FacebookBase(String name) {
        this.name_ = name;
        this.alive_ = System.currentTimeMillis() / 1000L;
    }

    public String getName() {
        return this.name_;
    }

    public String getStatusDetails() {
        return "";
    }

    public void deleteCounter(String key) {
        this.counters_.remove(key);
    }

    public void resetCounter(String key) {
        this.counters_.put(key, Long.valueOf(0L));
    }

    public long incrementCounter(String key) {
        long val = getCounter(key) + 1L;
        this.counters_.put(key, Long.valueOf(val));
        return val;
    }

    public AbstractMap<String, Long> getCounters() {
        return this.counters_;
    }

    public long getCounter(String key) {
        Long val = this.counters_.get(key);
        if (val == null) {
            return 0L;
        }
        return val.longValue();
    }

    public void setOption(String key, String value) {
        this.options_.put(key, value);
    }

    public String getOption(String key) {
        return this.options_.get(key);
    }

    public AbstractMap<String, String> getOptions() {
        return this.options_;
    }

    public long aliveSince() {
        return this.alive_;
    }

    public String getCpuProfile() {
        return "";
    }

    public void reinitialize() {
    }

    public void shutdown() {
    }

    public abstract fb_status getStatus();
}

