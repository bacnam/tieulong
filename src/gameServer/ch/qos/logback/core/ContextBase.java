package ch.qos.logback.core;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.LogbackLock;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class ContextBase
implements Context, LifeCycle
{
private long birthTime = System.currentTimeMillis();

private String name;
private StatusManager sm = new BasicStatusManager();

Map<String, String> propertyMap = new HashMap<String, String>();
Map<String, Object> objectMap = new HashMap<String, Object>();

LogbackLock configurationLock = new LogbackLock();

private volatile ExecutorService executorService;
private LifeCycleManager lifeCycleManager;
private boolean started;

public StatusManager getStatusManager() {
return this.sm;
}

public void setStatusManager(StatusManager statusManager) {
if (statusManager == null) {
throw new IllegalArgumentException("null StatusManager not allowed");
}
this.sm = statusManager;
}

public Map<String, String> getCopyOfPropertyMap() {
return new HashMap<String, String>(this.propertyMap);
}

public void putProperty(String key, String val) {
this.propertyMap.put(key, val);
}

public String getProperty(String key) {
if ("CONTEXT_NAME".equals(key)) {
return getName();
}
return this.propertyMap.get(key);
}

public Object getObject(String key) {
return this.objectMap.get(key);
}

public void putObject(String key, Object value) {
this.objectMap.put(key, value);
}

public void removeObject(String key) {
this.objectMap.remove(key);
}

public String getName() {
return this.name;
}

public void start() {
this.started = true;
}

public void stop() {
stopExecutorService();
this.started = false;
}

public boolean isStarted() {
return this.started;
}

public void reset() {
removeShutdownHook();
getLifeCycleManager().reset();
this.propertyMap.clear();
this.objectMap.clear();
}

public void setName(String name) throws IllegalStateException {
if (name != null && name.equals(this.name)) {
return;
}
if (this.name == null || "default".equals(this.name)) {

this.name = name;
} else {
throw new IllegalStateException("Context has been already given a name");
} 
}

public long getBirthTime() {
return this.birthTime;
}

public Object getConfigurationLock() {
return this.configurationLock;
}

public ExecutorService getExecutorService() {
if (this.executorService == null) {
synchronized (this) {
if (this.executorService == null) {
this.executorService = ExecutorServiceUtil.newExecutorService();
}
} 
}
return this.executorService;
}

private synchronized void stopExecutorService() {
if (this.executorService != null) {
ExecutorServiceUtil.shutdown(this.executorService);
this.executorService = null;
} 
}

private void removeShutdownHook() {
Thread hook = (Thread)getObject("SHUTDOWN_HOOK");
if (hook != null) {
removeObject("SHUTDOWN_HOOK");
try {
Runtime.getRuntime().removeShutdownHook(hook);
} catch (IllegalStateException e) {}
} 
}

public void register(LifeCycle component) {
getLifeCycleManager().register(component);
}

synchronized LifeCycleManager getLifeCycleManager() {
if (this.lifeCycleManager == null) {
this.lifeCycleManager = new LifeCycleManager();
}
return this.lifeCycleManager;
}

public String toString() {
return this.name;
}
}

