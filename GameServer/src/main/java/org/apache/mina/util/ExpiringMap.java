package org.apache.mina.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExpiringMap<K, V>
implements Map<K, V>
{
public static final int DEFAULT_TIME_TO_LIVE = 60;
public static final int DEFAULT_EXPIRATION_INTERVAL = 1;
private static volatile int expirerCount = 1;

private final ConcurrentHashMap<K, ExpiringObject> delegate;

private final CopyOnWriteArrayList<ExpirationListener<V>> expirationListeners;

private final Expirer expirer;

public ExpiringMap() {
this(60, 1);
}

public ExpiringMap(int timeToLive) {
this(timeToLive, 1);
}

public ExpiringMap(int timeToLive, int expirationInterval) {
this(new ConcurrentHashMap<K, ExpiringObject>(), new CopyOnWriteArrayList<ExpirationListener<V>>(), timeToLive, expirationInterval);
}

private ExpiringMap(ConcurrentHashMap<K, ExpiringObject> delegate, CopyOnWriteArrayList<ExpirationListener<V>> expirationListeners, int timeToLive, int expirationInterval) {
this.delegate = delegate;
this.expirationListeners = expirationListeners;

this.expirer = new Expirer();
this.expirer.setTimeToLive(timeToLive);
this.expirer.setExpirationInterval(expirationInterval);
}

public V put(K key, V value) {
ExpiringObject answer = this.delegate.put(key, new ExpiringObject(key, value, System.currentTimeMillis()));
if (answer == null) {
return null;
}

return answer.getValue();
}

public V get(Object key) {
ExpiringObject object = this.delegate.get(key);

if (object != null) {
object.setLastAccessTime(System.currentTimeMillis());

return object.getValue();
} 

return null;
}

public V remove(Object key) {
ExpiringObject answer = this.delegate.remove(key);
if (answer == null) {
return null;
}

return answer.getValue();
}

public boolean containsKey(Object key) {
return this.delegate.containsKey(key);
}

public boolean containsValue(Object value) {
return this.delegate.containsValue(value);
}

public int size() {
return this.delegate.size();
}

public boolean isEmpty() {
return this.delegate.isEmpty();
}

public void clear() {
this.delegate.clear();
}

public int hashCode() {
return this.delegate.hashCode();
}

public Set<K> keySet() {
return this.delegate.keySet();
}

public boolean equals(Object obj) {
return this.delegate.equals(obj);
}

public void putAll(Map<? extends K, ? extends V> inMap) {
for (Map.Entry<? extends K, ? extends V> e : inMap.entrySet()) {
put(e.getKey(), e.getValue());
}
}

public Collection<V> values() {
throw new UnsupportedOperationException();
}

public Set<Map.Entry<K, V>> entrySet() {
throw new UnsupportedOperationException();
}

public void addExpirationListener(ExpirationListener<V> listener) {
this.expirationListeners.add(listener);
}

public void removeExpirationListener(ExpirationListener<V> listener) {
this.expirationListeners.remove(listener);
}

public Expirer getExpirer() {
return this.expirer;
}

public int getExpirationInterval() {
return this.expirer.getExpirationInterval();
}

public int getTimeToLive() {
return this.expirer.getTimeToLive();
}

public void setExpirationInterval(int expirationInterval) {
this.expirer.setExpirationInterval(expirationInterval);
}

public void setTimeToLive(int timeToLive) {
this.expirer.setTimeToLive(timeToLive);
}

private class ExpiringObject
{
private K key;

private V value;
private long lastAccessTime;
private final ReadWriteLock lastAccessTimeLock = new ReentrantReadWriteLock();

ExpiringObject(K key, V value, long lastAccessTime) {
if (value == null) {
throw new IllegalArgumentException("An expiring object cannot be null.");
}

this.key = key;
this.value = value;
this.lastAccessTime = lastAccessTime;
}

public long getLastAccessTime() {
this.lastAccessTimeLock.readLock().lock();

try {
return this.lastAccessTime;
} finally {
this.lastAccessTimeLock.readLock().unlock();
} 
}

public void setLastAccessTime(long lastAccessTime) {
this.lastAccessTimeLock.writeLock().lock();

try {
this.lastAccessTime = lastAccessTime;
} finally {
this.lastAccessTimeLock.writeLock().unlock();
} 
}

public K getKey() {
return this.key;
}

public V getValue() {
return this.value;
}

public boolean equals(Object obj) {
return this.value.equals(obj);
}

public int hashCode() {
return this.value.hashCode();
}
}

public class Expirer
implements Runnable
{
private final ReadWriteLock stateLock = new ReentrantReadWriteLock();

private long timeToLiveMillis;

private long expirationIntervalMillis;

private boolean running = false;

private final Thread expirerThread;

public Expirer() {
this.expirerThread = new Thread(this, "ExpiringMapExpirer-" + ExpiringMap.expirerCount++);
this.expirerThread.setDaemon(true);
}

public void run() {
while (this.running) {
processExpires();

try {
Thread.sleep(this.expirationIntervalMillis);
} catch (InterruptedException e) {}
} 
}

private void processExpires() {
long timeNow = System.currentTimeMillis();

for (ExpiringMap<K, V>.ExpiringObject o : ExpiringMap.this.delegate.values()) {

if (this.timeToLiveMillis <= 0L) {
continue;
}

long timeIdle = timeNow - o.getLastAccessTime();

if (timeIdle >= this.timeToLiveMillis) {
ExpiringMap.this.delegate.remove(o.getKey());

for (ExpirationListener<V> listener : (Iterable<ExpirationListener<V>>)ExpiringMap.this.expirationListeners) {
listener.expired(o.getValue());
}
} 
} 
}

public void startExpiring() {
this.stateLock.writeLock().lock();

try {
if (!this.running) {
this.running = true;
this.expirerThread.start();
} 
} finally {
this.stateLock.writeLock().unlock();
} 
}

public void startExpiringIfNotStarted() {
this.stateLock.readLock().lock();
try {
if (this.running) {
return;
}
} finally {
this.stateLock.readLock().unlock();
} 

this.stateLock.writeLock().lock();
try {
if (!this.running) {
this.running = true;
this.expirerThread.start();
} 
} finally {
this.stateLock.writeLock().unlock();
} 
}

public void stopExpiring() {
this.stateLock.writeLock().lock();

try {
if (this.running) {
this.running = false;
this.expirerThread.interrupt();
} 
} finally {
this.stateLock.writeLock().unlock();
} 
}

public boolean isRunning() {
this.stateLock.readLock().lock();

try {
return this.running;
} finally {
this.stateLock.readLock().unlock();
} 
}

public int getTimeToLive() {
this.stateLock.readLock().lock();

try {
return (int)this.timeToLiveMillis / 1000;
} finally {
this.stateLock.readLock().unlock();
} 
}

public void setTimeToLive(long timeToLive) {
this.stateLock.writeLock().lock();

try {
this.timeToLiveMillis = timeToLive * 1000L;
} finally {
this.stateLock.writeLock().unlock();
} 
}

public int getExpirationInterval() {
this.stateLock.readLock().lock();

try {
return (int)this.expirationIntervalMillis / 1000;
} finally {
this.stateLock.readLock().unlock();
} 
}

public void setExpirationInterval(long expirationInterval) {
this.stateLock.writeLock().lock();

try {
this.expirationIntervalMillis = expirationInterval * 1000L;
} finally {
this.stateLock.writeLock().unlock();
} 
}
}
}

