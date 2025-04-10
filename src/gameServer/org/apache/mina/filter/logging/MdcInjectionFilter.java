package org.apache.mina.filter.logging;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.filterchain.IoFilterEvent;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.util.CommonEventFilter;
import org.slf4j.MDC;

public class MdcInjectionFilter
extends CommonEventFilter
{
public enum MdcKey
{
handlerClass, remoteAddress, localAddress, remoteIp, remotePort, localIp, localPort;
}

private static final AttributeKey CONTEXT_KEY = new AttributeKey(MdcInjectionFilter.class, "context");

private ThreadLocal<Integer> callDepth = new ThreadLocal<Integer>()
{
protected Integer initialValue() {
return Integer.valueOf(0);
}
};

private EnumSet<MdcKey> mdcKeys;

public MdcInjectionFilter(EnumSet<MdcKey> keys) {
this.mdcKeys = keys.clone();
}

public MdcInjectionFilter(MdcKey... keys) {
Set<MdcKey> keySet = new HashSet<MdcKey>(Arrays.asList(keys));
this.mdcKeys = EnumSet.copyOf(keySet);
}

public MdcInjectionFilter() {
this.mdcKeys = EnumSet.allOf(MdcKey.class);
}

protected void filter(IoFilterEvent event) throws Exception {
int currentCallDepth = ((Integer)this.callDepth.get()).intValue();
this.callDepth.set(Integer.valueOf(currentCallDepth + 1));
Map<String, String> context = getAndFillContext(event.getSession());

if (currentCallDepth == 0)
{
for (Map.Entry<String, String> e : context.entrySet()) {
MDC.put(e.getKey(), e.getValue());
}
}

try {
event.fire();
} finally {
if (currentCallDepth == 0) {

for (String key : context.keySet()) {
MDC.remove(key);
}
this.callDepth.remove();
} else {
this.callDepth.set(Integer.valueOf(currentCallDepth));
} 
} 
}

private Map<String, String> getAndFillContext(IoSession session) {
Map<String, String> context = getContext(session);
if (context.isEmpty()) {
fillContext(session, context);
}
return context;
}

private static Map<String, String> getContext(IoSession session) {
Map<String, String> context = (Map<String, String>)session.getAttribute(CONTEXT_KEY);
if (context == null) {
context = new ConcurrentHashMap<String, String>();
session.setAttribute(CONTEXT_KEY, context);
} 
return context;
}

protected void fillContext(IoSession session, Map<String, String> context) {
if (this.mdcKeys.contains(MdcKey.handlerClass)) {
context.put(MdcKey.handlerClass.name(), session.getHandler().getClass().getName());
}
if (this.mdcKeys.contains(MdcKey.remoteAddress)) {
context.put(MdcKey.remoteAddress.name(), session.getRemoteAddress().toString());
}
if (this.mdcKeys.contains(MdcKey.localAddress)) {
context.put(MdcKey.localAddress.name(), session.getLocalAddress().toString());
}
if (session.getTransportMetadata().getAddressType() == InetSocketAddress.class) {
InetSocketAddress remoteAddress = (InetSocketAddress)session.getRemoteAddress();
InetSocketAddress localAddress = (InetSocketAddress)session.getLocalAddress();

if (this.mdcKeys.contains(MdcKey.remoteIp)) {
context.put(MdcKey.remoteIp.name(), remoteAddress.getAddress().getHostAddress());
}
if (this.mdcKeys.contains(MdcKey.remotePort)) {
context.put(MdcKey.remotePort.name(), String.valueOf(remoteAddress.getPort()));
}
if (this.mdcKeys.contains(MdcKey.localIp)) {
context.put(MdcKey.localIp.name(), localAddress.getAddress().getHostAddress());
}
if (this.mdcKeys.contains(MdcKey.localPort)) {
context.put(MdcKey.localPort.name(), String.valueOf(localAddress.getPort()));
}
} 
}

public static String getProperty(IoSession session, String key) {
if (key == null) {
throw new IllegalArgumentException("key should not be null");
}

Map<String, String> context = getContext(session);
String answer = context.get(key);
if (answer != null) {
return answer;
}

return MDC.get(key);
}

public static void setProperty(IoSession session, String key, String value) {
if (key == null) {
throw new IllegalArgumentException("key should not be null");
}
if (value == null) {
removeProperty(session, key);
}
Map<String, String> context = getContext(session);
context.put(key, value);
MDC.put(key, value);
}

public static void removeProperty(IoSession session, String key) {
if (key == null) {
throw new IllegalArgumentException("key should not be null");
}
Map<String, String> context = getContext(session);
context.remove(key);
MDC.remove(key);
}
}

