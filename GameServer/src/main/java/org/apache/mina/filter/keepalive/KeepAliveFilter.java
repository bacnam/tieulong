package org.apache.mina.filter.keepalive;

import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilterChain;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

public class KeepAliveFilter
extends IoFilterAdapter
{
private final AttributeKey WAITING_FOR_RESPONSE = new AttributeKey(getClass(), "waitingForResponse");

private final AttributeKey IGNORE_READER_IDLE_ONCE = new AttributeKey(getClass(), "ignoreReaderIdleOnce");

private final KeepAliveMessageFactory messageFactory;

private final IdleStatus interestedIdleStatus;

private volatile KeepAliveRequestTimeoutHandler requestTimeoutHandler;

private volatile int requestInterval;

private volatile int requestTimeout;

private volatile boolean forwardEvent;

public KeepAliveFilter(KeepAliveMessageFactory messageFactory) {
this(messageFactory, IdleStatus.READER_IDLE, KeepAliveRequestTimeoutHandler.CLOSE);
}

public KeepAliveFilter(KeepAliveMessageFactory messageFactory, IdleStatus interestedIdleStatus) {
this(messageFactory, interestedIdleStatus, KeepAliveRequestTimeoutHandler.CLOSE, 60, 30);
}

public KeepAliveFilter(KeepAliveMessageFactory messageFactory, KeepAliveRequestTimeoutHandler policy) {
this(messageFactory, IdleStatus.READER_IDLE, policy, 60, 30);
}

public KeepAliveFilter(KeepAliveMessageFactory messageFactory, IdleStatus interestedIdleStatus, KeepAliveRequestTimeoutHandler policy) {
this(messageFactory, interestedIdleStatus, policy, 60, 30);
}

public KeepAliveFilter(KeepAliveMessageFactory messageFactory, IdleStatus interestedIdleStatus, KeepAliveRequestTimeoutHandler policy, int keepAliveRequestInterval, int keepAliveRequestTimeout) {
if (messageFactory == null) {
throw new IllegalArgumentException("messageFactory");
}
if (interestedIdleStatus == null) {
throw new IllegalArgumentException("interestedIdleStatus");
}
if (policy == null) {
throw new IllegalArgumentException("policy");
}

this.messageFactory = messageFactory;
this.interestedIdleStatus = interestedIdleStatus;
this.requestTimeoutHandler = policy;

setRequestInterval(keepAliveRequestInterval);
setRequestTimeout(keepAliveRequestTimeout);
}

public IdleStatus getInterestedIdleStatus() {
return this.interestedIdleStatus;
}

public KeepAliveRequestTimeoutHandler getRequestTimeoutHandler() {
return this.requestTimeoutHandler;
}

public void setRequestTimeoutHandler(KeepAliveRequestTimeoutHandler timeoutHandler) {
if (timeoutHandler == null) {
throw new IllegalArgumentException("timeoutHandler");
}
this.requestTimeoutHandler = timeoutHandler;
}

public int getRequestInterval() {
return this.requestInterval;
}

public void setRequestInterval(int keepAliveRequestInterval) {
if (keepAliveRequestInterval <= 0) {
throw new IllegalArgumentException("keepAliveRequestInterval must be a positive integer: " + keepAliveRequestInterval);
}

this.requestInterval = keepAliveRequestInterval;
}

public int getRequestTimeout() {
return this.requestTimeout;
}

public void setRequestTimeout(int keepAliveRequestTimeout) {
if (keepAliveRequestTimeout <= 0) {
throw new IllegalArgumentException("keepAliveRequestTimeout must be a positive integer: " + keepAliveRequestTimeout);
}

this.requestTimeout = keepAliveRequestTimeout;
}

public KeepAliveMessageFactory getMessageFactory() {
return this.messageFactory;
}

public boolean isForwardEvent() {
return this.forwardEvent;
}

public void setForwardEvent(boolean forwardEvent) {
this.forwardEvent = forwardEvent;
}

public void onPreAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
if (parent.contains((IoFilter)this)) {
throw new IllegalArgumentException("You can't add the same filter instance more than once. Create another instance and add it.");
}
}

public void onPostAdd(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
resetStatus(parent.getSession());
}

public void onPostRemove(IoFilterChain parent, String name, IoFilter.NextFilter nextFilter) throws Exception {
resetStatus(parent.getSession());
}

public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) throws Exception {
try {
if (this.messageFactory.isRequest(session, message)) {
Object pongMessage = this.messageFactory.getResponse(session, message);

if (pongMessage != null) {
nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(pongMessage));
}
} 

if (this.messageFactory.isResponse(session, message)) {
resetStatus(session);
}
} finally {
if (!isKeepAliveMessage(session, message)) {
nextFilter.messageReceived(session, message);
}
} 
}

public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
Object message = writeRequest.getMessage();
if (!isKeepAliveMessage(session, message)) {
nextFilter.messageSent(session, writeRequest);
}
}

public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
if (status == this.interestedIdleStatus) {
if (!session.containsAttribute(this.WAITING_FOR_RESPONSE)) {
Object pingMessage = this.messageFactory.getRequest(session);
if (pingMessage != null) {
nextFilter.filterWrite(session, (WriteRequest)new DefaultWriteRequest(pingMessage));

if (getRequestTimeoutHandler() != KeepAliveRequestTimeoutHandler.DEAF_SPEAKER) {
markStatus(session);
if (this.interestedIdleStatus == IdleStatus.BOTH_IDLE) {
session.setAttribute(this.IGNORE_READER_IDLE_ONCE);
}
} else {
resetStatus(session);
} 
} 
} else {
handlePingTimeout(session);
} 
} else if (status == IdleStatus.READER_IDLE && 
session.removeAttribute(this.IGNORE_READER_IDLE_ONCE) == null && 
session.containsAttribute(this.WAITING_FOR_RESPONSE)) {
handlePingTimeout(session);
} 

if (this.forwardEvent) {
nextFilter.sessionIdle(session, status);
}
}

private void handlePingTimeout(IoSession session) throws Exception {
resetStatus(session);
KeepAliveRequestTimeoutHandler handler = getRequestTimeoutHandler();
if (handler == KeepAliveRequestTimeoutHandler.DEAF_SPEAKER) {
return;
}

handler.keepAliveRequestTimedOut(this, session);
}

private void markStatus(IoSession session) {
session.getConfig().setIdleTime(this.interestedIdleStatus, 0);
session.getConfig().setReaderIdleTime(getRequestTimeout());
session.setAttribute(this.WAITING_FOR_RESPONSE);
}

private void resetStatus(IoSession session) {
session.getConfig().setReaderIdleTime(0);
session.getConfig().setWriterIdleTime(0);
session.getConfig().setIdleTime(this.interestedIdleStatus, getRequestInterval());
session.removeAttribute(this.WAITING_FOR_RESPONSE);
}

private boolean isKeepAliveMessage(IoSession session, Object message) {
return (this.messageFactory.isRequest(session, message) || this.messageFactory.isResponse(session, message));
}
}

