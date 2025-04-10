package ch.qos.logback.core.spi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractComponentTracker<C>
implements ComponentTracker<C>
{
private static final boolean ACCESS_ORDERED = true;
public static final long LINGERING_TIMEOUT = 10000L;
public static final long WAIT_BETWEEN_SUCCESSIVE_REMOVAL_ITERATIONS = 1000L;
protected int maxComponents = Integer.MAX_VALUE;
protected long timeout = 1800000L;

LinkedHashMap<String, Entry<C>> liveMap = new LinkedHashMap<String, Entry<C>>(32, 0.75F, true);

LinkedHashMap<String, Entry<C>> lingerersMap = new LinkedHashMap<String, Entry<C>>(16, 0.75F, true);
long lastCheck = 0L;

public int getComponentCount() {
return this.liveMap.size() + this.lingerersMap.size();
}

private Entry<C> getFromEitherMap(String key) {
Entry<C> entry = this.liveMap.get(key);
if (entry != null) {
return entry;
}
return this.lingerersMap.get(key);
}

public synchronized C find(String key) {
Entry<C> entry = getFromEitherMap(key);
if (entry == null) return null; 
return entry.component;
}

public synchronized C getOrCreate(String key, long timestamp) {
Entry<C> entry = getFromEitherMap(key);
if (entry == null) {
C c = buildComponent(key);
entry = new Entry<C>(key, c, timestamp);

this.liveMap.put(key, entry);
} else {
entry.setTimestamp(timestamp);
} 
return entry.component;
}

public void endOfLife(String key) {
Entry<C> entry = this.liveMap.remove(key);
if (entry == null)
return; 
this.lingerersMap.put(key, entry);
}

public synchronized void removeStaleComponents(long now) {
if (isTooSoonForRemovalIteration(now))
return;  removeExcedentComponents();
removeStaleComponentsFromMainMap(now);
removeStaleComponentsFromLingerersMap(now);
}

private void removeExcedentComponents() {
genericStaleComponentRemover(this.liveMap, 0L, this.byExcedent);
}

private void removeStaleComponentsFromMainMap(long now) {
genericStaleComponentRemover(this.liveMap, now, this.byTimeout);
}

private void removeStaleComponentsFromLingerersMap(long now) {
genericStaleComponentRemover(this.lingerersMap, now, this.byLingering);
}

private void genericStaleComponentRemover(LinkedHashMap<String, Entry<C>> map, long now, RemovalPredicator<C> removalPredicator) {
Iterator<Map.Entry<String, Entry<C>>> iter = map.entrySet().iterator();
while (iter.hasNext()) {
Map.Entry<String, Entry<C>> mapEntry = iter.next();
Entry<C> entry = mapEntry.getValue();
if (removalPredicator.isSlatedForRemoval(entry, now)) {
iter.remove();
C c = entry.component;
processPriorToRemoval(c);
} 
} 
}

private RemovalPredicator<C> byExcedent = new RemovalPredicator<C>() {
public boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> entry, long timestamp) {
return (AbstractComponentTracker.this.liveMap.size() > AbstractComponentTracker.this.maxComponents);
}
};

private RemovalPredicator<C> byTimeout = new RemovalPredicator<C>() {
public boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> entry, long timestamp) {
return AbstractComponentTracker.this.isEntryStale(entry, timestamp);
}
};
private RemovalPredicator<C> byLingering = new RemovalPredicator<C>() {
public boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> entry, long timestamp) {
return AbstractComponentTracker.this.isEntryDoneLingering(entry, timestamp);
}
};

private boolean isTooSoonForRemovalIteration(long now) {
if (this.lastCheck + 1000L > now) {
return true;
}
this.lastCheck = now;
return false;
}

private boolean isEntryStale(Entry<C> entry, long now) {
C c = entry.component;
if (isComponentStale(c)) {
return true;
}
return (entry.timestamp + this.timeout < now);
}

private boolean isEntryDoneLingering(Entry entry, long now) {
return (entry.timestamp + 10000L < now);
}

public Set<String> allKeys() {
HashSet<String> allKeys = new HashSet<String>(this.liveMap.keySet());
allKeys.addAll(this.lingerersMap.keySet());
return allKeys;
}

public Collection<C> allComponents() {
List<C> allComponents = new ArrayList<C>();
for (Entry<C> e : this.liveMap.values())
allComponents.add(e.component); 
for (Entry<C> e : this.lingerersMap.values()) {
allComponents.add(e.component);
}
return allComponents;
}

public long getTimeout() {
return this.timeout;
}

public void setTimeout(long timeout) {
this.timeout = timeout;
}

public int getMaxComponents() {
return this.maxComponents;
}

public void setMaxComponents(int maxComponents) {
this.maxComponents = maxComponents;
}
protected abstract void processPriorToRemoval(C paramC);

protected abstract C buildComponent(String paramString);

protected abstract boolean isComponentStale(C paramC);

private static interface RemovalPredicator<C> {
boolean isSlatedForRemoval(AbstractComponentTracker.Entry<C> param1Entry, long param1Long); }

private static class Entry<C> { String key;

Entry(String k, C c, long timestamp) {
this.key = k;
this.component = c;
this.timestamp = timestamp;
}
C component; long timestamp;
public void setTimestamp(long timestamp) {
this.timestamp = timestamp;
}

public int hashCode() {
return this.key.hashCode();
}

public boolean equals(Object obj) {
if (this == obj)
return true; 
if (obj == null)
return false; 
if (getClass() != obj.getClass())
return false; 
Entry other = (Entry)obj;
if (this.key == null) {
if (other.key != null)
return false; 
} else if (!this.key.equals(other.key)) {
return false;
}  if (this.component == null) {
if (other.component != null)
return false; 
} else if (!this.component.equals(other.component)) {
return false;
}  return true;
}

public String toString() {
return "(" + this.key + ", " + this.component + ")";
} }

}

