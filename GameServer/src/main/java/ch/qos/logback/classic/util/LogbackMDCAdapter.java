package ch.qos.logback.classic.util;

import org.slf4j.spi.MDCAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class LogbackMDCAdapter
        implements MDCAdapter {
    private static final int WRITE_OPERATION = 1;
    private static final int READ_OPERATION = 2;
    final InheritableThreadLocal<Map<String, String>> copyOnInheritThreadLocal = new InheritableThreadLocal<Map<String, String>>();
    final ThreadLocal<Integer> lastOperation = new ThreadLocal<Integer>();

    private Integer getAndSetLastOperation(int op) {
        Integer lastOp = this.lastOperation.get();
        this.lastOperation.set(Integer.valueOf(op));
        return lastOp;
    }

    private boolean wasLastOpReadOrNull(Integer lastOp) {
        return (lastOp == null || lastOp.intValue() == 2);
    }

    private Map<String, String> duplicateAndInsertNewMap(Map<String, String> oldMap) {
        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        if (oldMap != null) {

            synchronized (oldMap) {
                newMap.putAll(oldMap);
            }
        }

        this.copyOnInheritThreadLocal.set(newMap);
        return newMap;
    }

    public void put(String key, String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        Map<String, String> oldMap = this.copyOnInheritThreadLocal.get();
        Integer lastOp = getAndSetLastOperation(1);

        if (wasLastOpReadOrNull(lastOp) || oldMap == null) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.put(key, val);
        } else {
            oldMap.put(key, val);
        }
    }

    public void remove(String key) {
        if (key == null) {
            return;
        }
        Map<String, String> oldMap = this.copyOnInheritThreadLocal.get();
        if (oldMap == null)
            return;
        Integer lastOp = getAndSetLastOperation(1);

        if (wasLastOpReadOrNull(lastOp)) {
            Map<String, String> newMap = duplicateAndInsertNewMap(oldMap);
            newMap.remove(key);
        } else {
            oldMap.remove(key);
        }
    }

    public void clear() {
        this.lastOperation.set(Integer.valueOf(1));
        this.copyOnInheritThreadLocal.remove();
    }

    public String get(String key) {
        Map<String, String> map = getPropertyMap();
        if (map != null && key != null) {
            return map.get(key);
        }
        return null;
    }

    public Map<String, String> getPropertyMap() {
        this.lastOperation.set(Integer.valueOf(2));
        return this.copyOnInheritThreadLocal.get();
    }

    public Set<String> getKeys() {
        Map<String, String> map = getPropertyMap();

        if (map != null) {
            return map.keySet();
        }
        return null;
    }

    public Map getCopyOfContextMap() {
        this.lastOperation.set(Integer.valueOf(2));
        Map<String, String> hashMap = this.copyOnInheritThreadLocal.get();
        if (hashMap == null) {
            return null;
        }
        return new HashMap<String, String>(hashMap);
    }

    public void setContextMap(Map<? extends String, ? extends String> contextMap) {
        this.lastOperation.set(Integer.valueOf(1));

        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        newMap.putAll(contextMap);

        this.copyOnInheritThreadLocal.set(newMap);
    }
}

