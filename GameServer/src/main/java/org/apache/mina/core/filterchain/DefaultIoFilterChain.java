package org.apache.mina.core.filterchain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.AbstractIoService;
import org.apache.mina.core.session.AbstractIoSession;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;
import org.apache.mina.core.write.WriteRequestQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultIoFilterChain
implements IoFilterChain
{
public static final AttributeKey SESSION_CREATED_FUTURE = new AttributeKey(DefaultIoFilterChain.class, "connectFuture");

private final AbstractIoSession session;

private final Map<String, IoFilterChain.Entry> name2entry = new ConcurrentHashMap<String, IoFilterChain.Entry>();

private final EntryImpl head;

private final EntryImpl tail;

private static final Logger LOGGER = LoggerFactory.getLogger(DefaultIoFilterChain.class);

public DefaultIoFilterChain(AbstractIoSession session) {
if (session == null) {
throw new IllegalArgumentException("session");
}

this.session = session;
this.head = new EntryImpl(null, null, "head", new HeadFilter());
this.tail = new EntryImpl(this.head, null, "tail", new TailFilter());
this.head.nextEntry = this.tail;
}

public IoSession getSession() {
return (IoSession)this.session;
}

public IoFilterChain.Entry getEntry(String name) {
IoFilterChain.Entry e = this.name2entry.get(name);

if (e == null) {
return null;
}

return e;
}

public IoFilterChain.Entry getEntry(IoFilter filter) {
EntryImpl e = this.head.nextEntry;

while (e != this.tail) {
if (e.getFilter() == filter) {
return e;
}

e = e.nextEntry;
} 

return null;
}

public IoFilterChain.Entry getEntry(Class<? extends IoFilter> filterType) {
EntryImpl e = this.head.nextEntry;

while (e != this.tail) {
if (filterType.isAssignableFrom(e.getFilter().getClass())) {
return e;
}

e = e.nextEntry;
} 

return null;
}

public IoFilter get(String name) {
IoFilterChain.Entry e = getEntry(name);

if (e == null) {
return null;
}

return e.getFilter();
}

public IoFilter get(Class<? extends IoFilter> filterType) {
IoFilterChain.Entry e = getEntry(filterType);

if (e == null) {
return null;
}

return e.getFilter();
}

public IoFilter.NextFilter getNextFilter(String name) {
IoFilterChain.Entry e = getEntry(name);

if (e == null) {
return null;
}

return e.getNextFilter();
}

public IoFilter.NextFilter getNextFilter(IoFilter filter) {
IoFilterChain.Entry e = getEntry(filter);

if (e == null) {
return null;
}

return e.getNextFilter();
}

public IoFilter.NextFilter getNextFilter(Class<? extends IoFilter> filterType) {
IoFilterChain.Entry e = getEntry(filterType);

if (e == null) {
return null;
}

return e.getNextFilter();
}

public synchronized void addFirst(String name, IoFilter filter) {
checkAddable(name);
register(this.head, name, filter);
}

public synchronized void addLast(String name, IoFilter filter) {
checkAddable(name);
register(this.tail.prevEntry, name, filter);
}

public synchronized void addBefore(String baseName, String name, IoFilter filter) {
EntryImpl baseEntry = checkOldName(baseName);
checkAddable(name);
register(baseEntry.prevEntry, name, filter);
}

public synchronized void addAfter(String baseName, String name, IoFilter filter) {
EntryImpl baseEntry = checkOldName(baseName);
checkAddable(name);
register(baseEntry, name, filter);
}

public synchronized IoFilter remove(String name) {
EntryImpl entry = checkOldName(name);
deregister(entry);
return entry.getFilter();
}

public synchronized void remove(IoFilter filter) {
EntryImpl e = this.head.nextEntry;

while (e != this.tail) {
if (e.getFilter() == filter) {
deregister(e);

return;
} 

e = e.nextEntry;
} 

throw new IllegalArgumentException("Filter not found: " + filter.getClass().getName());
}

public synchronized IoFilter remove(Class<? extends IoFilter> filterType) {
EntryImpl e = this.head.nextEntry;

while (e != this.tail) {
if (filterType.isAssignableFrom(e.getFilter().getClass())) {
IoFilter oldFilter = e.getFilter();
deregister(e);

return oldFilter;
} 

e = e.nextEntry;
} 

throw new IllegalArgumentException("Filter not found: " + filterType.getName());
}

public synchronized IoFilter replace(String name, IoFilter newFilter) {
EntryImpl entry = checkOldName(name);
IoFilter oldFilter = entry.getFilter();

try {
newFilter.onPreAdd(this, name, entry.getNextFilter());
} catch (Exception e) {
throw new IoFilterLifeCycleException("onPreAdd(): " + name + ':' + newFilter + " in " + getSession(), e);
} 

entry.setFilter(newFilter);

try {
newFilter.onPostAdd(this, name, entry.getNextFilter());
} catch (Exception e) {
entry.setFilter(oldFilter);
throw new IoFilterLifeCycleException("onPostAdd(): " + name + ':' + newFilter + " in " + getSession(), e);
} 

return oldFilter;
}

public synchronized void replace(IoFilter oldFilter, IoFilter newFilter) {
EntryImpl entry = this.head.nextEntry;

while (entry != this.tail) {
if (entry.getFilter() == oldFilter) {
String oldFilterName = null;

for (String name : this.name2entry.keySet()) {
if (entry == this.name2entry.get(name)) {
oldFilterName = name;

break;
} 
} 

try {
newFilter.onPreAdd(this, oldFilterName, entry.getNextFilter());
} catch (Exception e) {
throw new IoFilterLifeCycleException("onPreAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
} 

entry.setFilter(newFilter);

try {
newFilter.onPostAdd(this, oldFilterName, entry.getNextFilter());
} catch (Exception e) {
entry.setFilter(oldFilter);
throw new IoFilterLifeCycleException("onPostAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
} 

return;
} 

entry = entry.nextEntry;
} 

throw new IllegalArgumentException("Filter not found: " + oldFilter.getClass().getName());
}

public synchronized IoFilter replace(Class<? extends IoFilter> oldFilterType, IoFilter newFilter) {
EntryImpl entry = this.head.nextEntry;

while (entry != this.tail) {
if (oldFilterType.isAssignableFrom(entry.getFilter().getClass())) {
IoFilter oldFilter = entry.getFilter();

String oldFilterName = null;

for (String name : this.name2entry.keySet()) {
if (entry == this.name2entry.get(name)) {
oldFilterName = name;

break;
} 
} 

try {
newFilter.onPreAdd(this, oldFilterName, entry.getNextFilter());
} catch (Exception e) {
throw new IoFilterLifeCycleException("onPreAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
} 

entry.setFilter(newFilter);

try {
newFilter.onPostAdd(this, oldFilterName, entry.getNextFilter());
} catch (Exception e) {
entry.setFilter(oldFilter);
throw new IoFilterLifeCycleException("onPostAdd(): " + oldFilterName + ':' + newFilter + " in " + getSession(), e);
} 

return oldFilter;
} 

entry = entry.nextEntry;
} 

throw new IllegalArgumentException("Filter not found: " + oldFilterType.getName());
}

public synchronized void clear() throws Exception {
List<IoFilterChain.Entry> l = new ArrayList<IoFilterChain.Entry>(this.name2entry.values());

for (IoFilterChain.Entry entry : l) {
try {
deregister((EntryImpl)entry);
} catch (Exception e) {
throw new IoFilterLifeCycleException("clear(): " + entry.getName() + " in " + getSession(), e);
} 
} 
}

private void register(EntryImpl prevEntry, String name, IoFilter filter) {
EntryImpl newEntry = new EntryImpl(prevEntry, prevEntry.nextEntry, name, filter);

try {
filter.onPreAdd(this, name, newEntry.getNextFilter());
} catch (Exception e) {
throw new IoFilterLifeCycleException("onPreAdd(): " + name + ':' + filter + " in " + getSession(), e);
} 

prevEntry.nextEntry.prevEntry = newEntry;
prevEntry.nextEntry = newEntry;
this.name2entry.put(name, newEntry);

try {
filter.onPostAdd(this, name, newEntry.getNextFilter());
} catch (Exception e) {
deregister0(newEntry);
throw new IoFilterLifeCycleException("onPostAdd(): " + name + ':' + filter + " in " + getSession(), e);
} 
}

private void deregister(EntryImpl entry) {
IoFilter filter = entry.getFilter();

try {
filter.onPreRemove(this, entry.getName(), entry.getNextFilter());
} catch (Exception e) {
throw new IoFilterLifeCycleException("onPreRemove(): " + entry.getName() + ':' + filter + " in " + getSession(), e);
} 

deregister0(entry);

try {
filter.onPostRemove(this, entry.getName(), entry.getNextFilter());
} catch (Exception e) {
throw new IoFilterLifeCycleException("onPostRemove(): " + entry.getName() + ':' + filter + " in " + getSession(), e);
} 
}

private void deregister0(EntryImpl entry) {
EntryImpl prevEntry = entry.prevEntry;
EntryImpl nextEntry = entry.nextEntry;
prevEntry.nextEntry = nextEntry;
nextEntry.prevEntry = prevEntry;

this.name2entry.remove(entry.name);
}

private EntryImpl checkOldName(String baseName) {
EntryImpl e = (EntryImpl)this.name2entry.get(baseName);

if (e == null) {
throw new IllegalArgumentException("Filter not found:" + baseName);
}

return e;
}

private void checkAddable(String name) {
if (this.name2entry.containsKey(name)) {
throw new IllegalArgumentException("Other filter is using the same name '" + name + "'");
}
}

public void fireSessionCreated() {
callNextSessionCreated(this.head, (IoSession)this.session);
}

private void callNextSessionCreated(IoFilterChain.Entry entry, IoSession session) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.sessionCreated(nextFilter, session);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 
}

public void fireSessionOpened() {
callNextSessionOpened(this.head, (IoSession)this.session);
}

private void callNextSessionOpened(IoFilterChain.Entry entry, IoSession session) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.sessionOpened(nextFilter, session);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 
}

public void fireSessionClosed() {
try {
this.session.getCloseFuture().setClosed();
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 

callNextSessionClosed(this.head, (IoSession)this.session);
}

private void callNextSessionClosed(IoFilterChain.Entry entry, IoSession session) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.sessionClosed(nextFilter, session);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
} 
}

public void fireSessionIdle(IdleStatus status) {
this.session.increaseIdleCount(status, System.currentTimeMillis());
callNextSessionIdle(this.head, (IoSession)this.session, status);
}

private void callNextSessionIdle(IoFilterChain.Entry entry, IoSession session, IdleStatus status) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.sessionIdle(nextFilter, session, status);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 
}

public void fireMessageReceived(Object message) {
if (message instanceof IoBuffer) {
this.session.increaseReadBytes(((IoBuffer)message).remaining(), System.currentTimeMillis());
}

callNextMessageReceived(this.head, (IoSession)this.session, message);
}

private void callNextMessageReceived(IoFilterChain.Entry entry, IoSession session, Object message) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.messageReceived(nextFilter, session, message);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 
}

public void fireMessageSent(WriteRequest request) {
try {
request.getFuture().setWritten();
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 

if (!request.isEncoded()) {
callNextMessageSent(this.head, (IoSession)this.session, request);
}
}

private void callNextMessageSent(IoFilterChain.Entry entry, IoSession session, WriteRequest writeRequest) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.messageSent(nextFilter, session, writeRequest);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 
}

public void fireExceptionCaught(Throwable cause) {
callNextExceptionCaught(this.head, (IoSession)this.session, cause);
}

private void callNextExceptionCaught(IoFilterChain.Entry entry, IoSession session, Throwable cause) {
ConnectFuture future = (ConnectFuture)session.removeAttribute(SESSION_CREATED_FUTURE);
if (future == null) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.exceptionCaught(nextFilter, session, cause);
} catch (Throwable e) {
LOGGER.warn("Unexpected exception from exceptionCaught handler.", e);
}

} else {

session.close(true);
future.setException(cause);
} 
}

public void fireInputClosed() {
IoFilterChain.Entry head = this.head;
callNextInputClosed(head, (IoSession)this.session);
}

private void callNextInputClosed(IoFilterChain.Entry entry, IoSession session) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.inputClosed(nextFilter, session);
} catch (Throwable e) {
fireExceptionCaught(e);
} 
}

public void fireFilterWrite(WriteRequest writeRequest) {
callPreviousFilterWrite(this.tail, (IoSession)this.session, writeRequest);
}

private void callPreviousFilterWrite(IoFilterChain.Entry entry, IoSession session, WriteRequest writeRequest) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.filterWrite(nextFilter, session, writeRequest);
} catch (Exception e) {
writeRequest.getFuture().setException(e);
fireExceptionCaught(e);
} catch (Error e) {
writeRequest.getFuture().setException(e);
fireExceptionCaught(e);
throw e;
} 
}

public void fireFilterClose() {
callPreviousFilterClose(this.tail, (IoSession)this.session);
}

private void callPreviousFilterClose(IoFilterChain.Entry entry, IoSession session) {
try {
IoFilter filter = entry.getFilter();
IoFilter.NextFilter nextFilter = entry.getNextFilter();
filter.filterClose(nextFilter, session);
} catch (Exception e) {
fireExceptionCaught(e);
} catch (Error e) {
fireExceptionCaught(e);
throw e;
} 
}

public List<IoFilterChain.Entry> getAll() {
List<IoFilterChain.Entry> list = new ArrayList<IoFilterChain.Entry>();
EntryImpl e = this.head.nextEntry;

while (e != this.tail) {
list.add(e);
e = e.nextEntry;
} 

return list;
}

public List<IoFilterChain.Entry> getAllReversed() {
List<IoFilterChain.Entry> list = new ArrayList<IoFilterChain.Entry>();
EntryImpl e = this.tail.prevEntry;

while (e != this.head) {
list.add(e);
e = e.prevEntry;
} 

return list;
}

public boolean contains(String name) {
return (getEntry(name) != null);
}

public boolean contains(IoFilter filter) {
return (getEntry(filter) != null);
}

public boolean contains(Class<? extends IoFilter> filterType) {
return (getEntry(filterType) != null);
}

public String toString() {
StringBuilder buf = new StringBuilder();
buf.append("{ ");

boolean empty = true;

EntryImpl e = this.head.nextEntry;

while (e != this.tail) {
if (!empty) {
buf.append(", ");
} else {
empty = false;
} 

buf.append('(');
buf.append(e.getName());
buf.append(':');
buf.append(e.getFilter());
buf.append(')');

e = e.nextEntry;
} 

if (empty) {
buf.append("empty");
}

buf.append(" }");

return buf.toString();
}

private class HeadFilter
extends IoFilterAdapter
{
public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
AbstractIoSession s = (AbstractIoSession)session;

if (writeRequest.getMessage() instanceof IoBuffer) {
IoBuffer buffer = (IoBuffer)writeRequest.getMessage();

buffer.mark();
int remaining = buffer.remaining();

if (remaining > 0) {
s.increaseScheduledWriteBytes(remaining);
}
} else {
s.increaseScheduledWriteMessages();
} 

WriteRequestQueue writeRequestQueue = s.getWriteRequestQueue();

if (!s.isWriteSuspended()) {
if (writeRequestQueue.isEmpty(session)) {

s.getProcessor().write((IoSession)s, writeRequest);
} else {
s.getWriteRequestQueue().offer((IoSession)s, writeRequest);
s.getProcessor().flush((IoSession)s);
} 
} else {
s.getWriteRequestQueue().offer((IoSession)s, writeRequest);
} 
}

private HeadFilter() {}

public void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
((AbstractIoSession)session).getProcessor().remove(session);
}
}

private static class TailFilter
extends IoFilterAdapter {
public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
try {
session.getHandler().sessionCreated(session);
} finally {

ConnectFuture future = (ConnectFuture)session.removeAttribute(DefaultIoFilterChain.SESSION_CREATED_FUTURE);

if (future != null)
future.setSession(session); 
} 
}

private TailFilter() {}

public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
session.getHandler().sessionOpened(session);
}

public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
AbstractIoSession s = (AbstractIoSession)session;

try {
s.getHandler().sessionClosed(session);
} finally {
try {
s.getWriteRequestQueue().dispose(session);
} finally {
try {
s.getAttributeMap().dispose(session);
} finally {

try {
session.getFilterChain().clear();
} finally {
if (s.getConfig().isUseReadOperation()) {
s.offerClosedReadFuture();
}
} 
} 
} 
} 
}

public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
session.getHandler().sessionIdle(session, status);
}

public void exceptionCaught(IoFilter.NextFilter nextFilter, IoSession session, Throwable cause) throws Exception {
AbstractIoSession s = (AbstractIoSession)session;

try {
s.getHandler().exceptionCaught((IoSession)s, cause);
} finally {
if (s.getConfig().isUseReadOperation()) {
s.offerFailedReadFuture(cause);
}
} 
}

public void inputClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
session.getHandler().inputClosed(session);
}

public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
AbstractIoSession s = (AbstractIoSession)session;

if (!(message instanceof IoBuffer)) {
s.increaseReadMessages(System.currentTimeMillis());
} else if (!((IoBuffer)message).hasRemaining()) {
s.increaseReadMessages(System.currentTimeMillis());
} 

if (session.getService() instanceof AbstractIoService) {
((AbstractIoService)session.getService()).getStatistics().updateThroughput(System.currentTimeMillis());
}

try {
session.getHandler().messageReceived((IoSession)s, message);
} finally {
if (s.getConfig().isUseReadOperation()) {
s.offerReadFuture(message);
}
} 
}

public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
((AbstractIoSession)session).increaseWrittenMessages(writeRequest, System.currentTimeMillis());

if (session.getService() instanceof AbstractIoService) {
((AbstractIoService)session.getService()).getStatistics().updateThroughput(System.currentTimeMillis());
}

session.getHandler().messageSent(session, writeRequest.getMessage());
}

public void filterWrite(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
nextFilter.filterWrite(session, writeRequest);
}

public void filterClose(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
nextFilter.filterClose(session);
}
}

private class EntryImpl
implements IoFilterChain.Entry
{
private EntryImpl prevEntry;

private EntryImpl nextEntry;
private final String name;
private IoFilter filter;
private final IoFilter.NextFilter nextFilter;

private EntryImpl(EntryImpl prevEntry, EntryImpl nextEntry, String name, IoFilter filter) {
if (filter == null) {
throw new IllegalArgumentException("filter");
}

if (name == null) {
throw new IllegalArgumentException("name");
}

this.prevEntry = prevEntry;
this.nextEntry = nextEntry;
this.name = name;
this.filter = filter;
this.nextFilter = new IoFilter.NextFilter() {
public void sessionCreated(IoSession session) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextSessionCreated(nextEntry, session);
}

public void sessionOpened(IoSession session) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextSessionOpened(nextEntry, session);
}

public void sessionClosed(IoSession session) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextSessionClosed(nextEntry, session);
}

public void sessionIdle(IoSession session, IdleStatus status) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextSessionIdle(nextEntry, session, status);
}

public void exceptionCaught(IoSession session, Throwable cause) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextExceptionCaught(nextEntry, session, cause);
}

public void inputClosed(IoSession session) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextInputClosed(nextEntry, session);
}

public void messageReceived(IoSession session, Object message) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextMessageReceived(nextEntry, session, message);
}

public void messageSent(IoSession session, WriteRequest writeRequest) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.nextEntry;
DefaultIoFilterChain.this.callNextMessageSent(nextEntry, session, writeRequest);
}

public void filterWrite(IoSession session, WriteRequest writeRequest) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.prevEntry;
DefaultIoFilterChain.this.callPreviousFilterWrite(nextEntry, session, writeRequest);
}

public void filterClose(IoSession session) {
IoFilterChain.Entry nextEntry = DefaultIoFilterChain.EntryImpl.this.prevEntry;
DefaultIoFilterChain.this.callPreviousFilterClose(nextEntry, session);
}

public String toString() {
return DefaultIoFilterChain.EntryImpl.this.nextEntry.name;
}
};
}

public String getName() {
return this.name;
}

public IoFilter getFilter() {
return this.filter;
}

private void setFilter(IoFilter filter) {
if (filter == null) {
throw new IllegalArgumentException("filter");
}

this.filter = filter;
}

public IoFilter.NextFilter getNextFilter() {
return this.nextFilter;
}

public String toString() {
StringBuilder sb = new StringBuilder();

sb.append("('").append(getName()).append('\'');

sb.append(", prev: '");

if (this.prevEntry != null) {
sb.append(this.prevEntry.name);
sb.append(':');
sb.append(this.prevEntry.getFilter().getClass().getSimpleName());
} else {
sb.append("null");
} 

sb.append("', next: '");

if (this.nextEntry != null) {
sb.append(this.nextEntry.name);
sb.append(':');
sb.append(this.nextEntry.getFilter().getClass().getSimpleName());
} else {
sb.append("null");
} 

sb.append("')");

return sb.toString();
}

public void addAfter(String name, IoFilter filter) {
DefaultIoFilterChain.this.addAfter(getName(), name, filter);
}

public void addBefore(String name, IoFilter filter) {
DefaultIoFilterChain.this.addBefore(getName(), name, filter);
}

public void remove() {
DefaultIoFilterChain.this.remove(getName());
}

public void replace(IoFilter newFilter) {
DefaultIoFilterChain.this.replace(getName(), newFilter);
}
}
}

