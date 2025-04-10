package org.slf4j;

import java.io.Closeable;
import java.util.Map;
import org.slf4j.helpers.NOPMDCAdapter;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMDCBinder;
import org.slf4j.spi.MDCAdapter;

public class MDC
{
static final String NULL_MDCA_URL = "http:
static final String NO_STATIC_MDC_BINDER_URL = "http:
static MDCAdapter mdcAdapter;

public static class MDCCloseable
implements Closeable
{
private final String key;

private MDCCloseable(String key) {
this.key = key;
}

public void close() {
MDC.remove(this.key);
}
}

static {
try {
mdcAdapter = StaticMDCBinder.SINGLETON.getMDCA();
} catch (NoClassDefFoundError ncde) {
mdcAdapter = (MDCAdapter)new NOPMDCAdapter();
String msg = ncde.getMessage();
if (msg != null && msg.indexOf("StaticMDCBinder") != -1) {
Util.report("Failed to load class \"org.slf4j.impl.StaticMDCBinder\".");
Util.report("Defaulting to no-operation MDCAdapter implementation.");
Util.report("See http:
} else {
throw ncde;
} 
} catch (Exception e) {

Util.report("MDC binding unsuccessful.", e);
} 
}

public static void put(String key, String val) throws IllegalArgumentException {
if (key == null) {
throw new IllegalArgumentException("key parameter cannot be null");
}
if (mdcAdapter == null) {
throw new IllegalStateException("MDCAdapter cannot be null. See also http:
}
mdcAdapter.put(key, val);
}

public static MDCCloseable putCloseable(String key, String val) throws IllegalArgumentException {
put(key, val);
return new MDCCloseable(key);
}

public static String get(String key) throws IllegalArgumentException {
if (key == null) {
throw new IllegalArgumentException("key parameter cannot be null");
}

if (mdcAdapter == null) {
throw new IllegalStateException("MDCAdapter cannot be null. See also http:
}
return mdcAdapter.get(key);
}

public static void remove(String key) throws IllegalArgumentException {
if (key == null) {
throw new IllegalArgumentException("key parameter cannot be null");
}

if (mdcAdapter == null) {
throw new IllegalStateException("MDCAdapter cannot be null. See also http:
}
mdcAdapter.remove(key);
}

public static void clear() {
if (mdcAdapter == null) {
throw new IllegalStateException("MDCAdapter cannot be null. See also http:
}
mdcAdapter.clear();
}

public static Map<String, String> getCopyOfContextMap() {
if (mdcAdapter == null) {
throw new IllegalStateException("MDCAdapter cannot be null. See also http:
}
return mdcAdapter.getCopyOfContextMap();
}

public static void setContextMap(Map<String, String> contextMap) {
if (mdcAdapter == null) {
throw new IllegalStateException("MDCAdapter cannot be null. See also http:
}
mdcAdapter.setContextMap(contextMap);
}

public static MDCAdapter getMDCAdapter() {
return mdcAdapter;
}
}

