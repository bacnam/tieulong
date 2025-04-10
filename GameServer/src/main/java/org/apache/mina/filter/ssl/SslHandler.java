package org.apache.mina.filter.ssl;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.filterchain.IoFilterEvent;
import org.apache.mina.core.future.DefaultWriteFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SslHandler
{
private static final Logger LOGGER = LoggerFactory.getLogger(SslHandler.class);

private final SslFilter sslFilter;

private final IoSession session;

private final Queue<IoFilterEvent> preHandshakeEventQueue = new ConcurrentLinkedQueue<IoFilterEvent>();

private final Queue<IoFilterEvent> filterWriteEventQueue = new ConcurrentLinkedQueue<IoFilterEvent>();

private final Queue<IoFilterEvent> messageReceivedEventQueue = new ConcurrentLinkedQueue<IoFilterEvent>();

private SSLEngine sslEngine;

private IoBuffer inNetBuffer;

private IoBuffer outNetBuffer;

private IoBuffer appBuffer;

private final IoBuffer emptyBuffer = IoBuffer.allocate(0);

private SSLEngineResult.HandshakeStatus handshakeStatus;

private boolean firstSSLNegociation;

private boolean handshakeComplete;

private boolean writingEncryptedData;

private Lock sslLock = new ReentrantLock();

SslHandler(SslFilter sslFilter, IoSession session) throws SSLException {
this.sslFilter = sslFilter;
this.session = session;
}

void init() throws SSLException {
if (this.sslEngine != null) {
return;
}

LOGGER.debug("{} Initializing the SSL Handler", this.sslFilter.getSessionInfo(this.session));

InetSocketAddress peer = (InetSocketAddress)this.session.getAttribute(SslFilter.PEER_ADDRESS);

if (peer == null) {
this.sslEngine = this.sslFilter.sslContext.createSSLEngine();
} else {
this.sslEngine = this.sslFilter.sslContext.createSSLEngine(peer.getHostName(), peer.getPort());
} 

this.sslEngine.setUseClientMode(this.sslFilter.isUseClientMode());

if (!this.sslEngine.getUseClientMode()) {

if (this.sslFilter.isWantClientAuth()) {
this.sslEngine.setWantClientAuth(true);
}

if (this.sslFilter.isNeedClientAuth()) {
this.sslEngine.setNeedClientAuth(true);
}
} 

if (this.sslFilter.getEnabledCipherSuites() != null) {
this.sslEngine.setEnabledCipherSuites(this.sslFilter.getEnabledCipherSuites());
}

if (this.sslFilter.getEnabledProtocols() != null) {
this.sslEngine.setEnabledProtocols(this.sslFilter.getEnabledProtocols());
}

this.sslEngine.beginHandshake();

this.handshakeStatus = this.sslEngine.getHandshakeStatus();

this.writingEncryptedData = false;

this.firstSSLNegociation = true;
this.handshakeComplete = false;

if (LOGGER.isDebugEnabled()) {
LOGGER.debug("{} SSL Handler Initialization done.", this.sslFilter.getSessionInfo(this.session));
}
}

void destroy() {
if (this.sslEngine == null) {
return;
}

try {
this.sslEngine.closeInbound();
} catch (SSLException e) {
LOGGER.debug("Unexpected exception from SSLEngine.closeInbound().", e);
} 

if (this.outNetBuffer != null) {
this.outNetBuffer.capacity(this.sslEngine.getSession().getPacketBufferSize());
} else {
createOutNetBuffer(0);
} 
try {
do {
this.outNetBuffer.clear();
} while (this.sslEngine.wrap(this.emptyBuffer.buf(), this.outNetBuffer.buf()).bytesProduced() > 0);
} catch (SSLException e) {

} finally {
this.outNetBuffer.free();
this.outNetBuffer = null;
} 

this.sslEngine.closeOutbound();
this.sslEngine = null;

this.preHandshakeEventQueue.clear();
}

SslFilter getSslFilter() {
return this.sslFilter;
}

IoSession getSession() {
return this.session;
}

boolean isWritingEncryptedData() {
return this.writingEncryptedData;
}

boolean isHandshakeComplete() {
return this.handshakeComplete;
}

boolean isInboundDone() {
return (this.sslEngine == null || this.sslEngine.isInboundDone());
}

boolean isOutboundDone() {
return (this.sslEngine == null || this.sslEngine.isOutboundDone());
}

boolean needToCompleteHandshake() {
return (this.handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_WRAP && !isInboundDone());
}

void schedulePreHandshakeWriteRequest(IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
this.preHandshakeEventQueue.add(new IoFilterEvent(nextFilter, IoEventType.WRITE, this.session, writeRequest));
}

void flushPreHandshakeEvents() throws SSLException {
IoFilterEvent scheduledWrite;
while ((scheduledWrite = this.preHandshakeEventQueue.poll()) != null) {
this.sslFilter.filterWrite(scheduledWrite.getNextFilter(), this.session, (WriteRequest)scheduledWrite.getParameter());
}
}

void scheduleFilterWrite(IoFilter.NextFilter nextFilter, WriteRequest writeRequest) {
this.filterWriteEventQueue.add(new IoFilterEvent(nextFilter, IoEventType.WRITE, this.session, writeRequest));
}

void scheduleMessageReceived(IoFilter.NextFilter nextFilter, Object message) {
this.messageReceivedEventQueue.add(new IoFilterEvent(nextFilter, IoEventType.MESSAGE_RECEIVED, this.session, message));
}

void flushScheduledEvents() {
if (Thread.holdsLock(this)) {
return;
}

this.sslLock.lock();
try {
IoFilterEvent event;
while ((event = this.filterWriteEventQueue.poll()) != null) {
IoFilter.NextFilter nextFilter = event.getNextFilter();
nextFilter.filterWrite(this.session, (WriteRequest)event.getParameter());
} 

while ((event = this.messageReceivedEventQueue.poll()) != null) {
IoFilter.NextFilter nextFilter = event.getNextFilter();
nextFilter.messageReceived(this.session, event.getParameter());
} 
} finally {
this.sslLock.unlock();
} 
}

void messageReceived(IoFilter.NextFilter nextFilter, ByteBuffer buf) throws SSLException {
if (LOGGER.isDebugEnabled()) {
if (!isOutboundDone()) {
LOGGER.debug("{} Processing the received message", this.sslFilter.getSessionInfo(this.session));
} else {
LOGGER.debug("{} Processing the received message", this.sslFilter.getSessionInfo(this.session));
} 
}

if (this.inNetBuffer == null) {
this.inNetBuffer = IoBuffer.allocate(buf.remaining()).setAutoExpand(true);
}

this.inNetBuffer.put(buf);

if (!this.handshakeComplete) {
handshake(nextFilter);
} else {

this.inNetBuffer.flip();

if (!this.inNetBuffer.hasRemaining()) {
return;
}

SSLEngineResult res = unwrap();

if (this.inNetBuffer.hasRemaining()) {
this.inNetBuffer.compact();
} else {
this.inNetBuffer.free();
this.inNetBuffer = null;
} 

checkStatus(res);

renegotiateIfNeeded(nextFilter, res);
} 

if (isInboundDone()) {

int inNetBufferPosition = (this.inNetBuffer == null) ? 0 : this.inNetBuffer.position();
buf.position(buf.position() - inNetBufferPosition);

if (this.inNetBuffer != null) {
this.inNetBuffer.free();
this.inNetBuffer = null;
} 
} 
}

IoBuffer fetchAppBuffer() {
if (this.appBuffer == null) {
return IoBuffer.allocate(0);
}
IoBuffer appBuffer = this.appBuffer.flip();
this.appBuffer = null;

return appBuffer;
}

IoBuffer fetchOutNetBuffer() {
IoBuffer answer = this.outNetBuffer;
if (answer == null) {
return this.emptyBuffer;
}

this.outNetBuffer = null;
return answer.shrink();
}

void encrypt(ByteBuffer src) throws SSLException {
if (!this.handshakeComplete) {
throw new IllegalStateException();
}

if (!src.hasRemaining()) {
if (this.outNetBuffer == null) {
this.outNetBuffer = this.emptyBuffer;
}

return;
} 
createOutNetBuffer(src.remaining());

while (src.hasRemaining()) {

SSLEngineResult result = this.sslEngine.wrap(src, this.outNetBuffer.buf());
if (result.getStatus() == SSLEngineResult.Status.OK) {
if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK)
doTasks();  continue;
} 
if (result.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
this.outNetBuffer.capacity(this.outNetBuffer.capacity() << 1);
this.outNetBuffer.limit(this.outNetBuffer.capacity()); continue;
} 
throw new SSLException("SSLEngine error during encrypt: " + result.getStatus() + " src: " + src + "outNetBuffer: " + this.outNetBuffer);
} 

this.outNetBuffer.flip();
}

boolean closeOutbound() throws SSLException {
SSLEngineResult result;
if (this.sslEngine == null || this.sslEngine.isOutboundDone()) {
return false;
}

this.sslEngine.closeOutbound();

createOutNetBuffer(0);

while (true) {
result = this.sslEngine.wrap(this.emptyBuffer.buf(), this.outNetBuffer.buf());
if (result.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
this.outNetBuffer.capacity(this.outNetBuffer.capacity() << 1);
this.outNetBuffer.limit(this.outNetBuffer.capacity());

continue;
} 
break;
} 
if (result.getStatus() != SSLEngineResult.Status.CLOSED) {
throw new SSLException("Improper close state: " + result);
}

this.outNetBuffer.flip();

return true;
}

private void checkStatus(SSLEngineResult res) throws SSLException {
SSLEngineResult.Status status = res.getStatus();

if (status == SSLEngineResult.Status.BUFFER_OVERFLOW) {
throw new SSLException("SSLEngine error during decrypt: " + status + " inNetBuffer: " + this.inNetBuffer + "appBuffer: " + this.appBuffer);
}
}

void handshake(IoFilter.NextFilter nextFilter) throws SSLException {
while (true) {
SSLEngineResult.Status status;
SSLEngineResult result;
switch (this.handshakeStatus) {
case FINISHED:
case NOT_HANDSHAKING:
if (LOGGER.isDebugEnabled()) {
LOGGER.debug("{} processing the FINISHED state", this.sslFilter.getSessionInfo(this.session));
}

this.session.setAttribute(SslFilter.SSL_SESSION, this.sslEngine.getSession());
this.handshakeComplete = true;

if (this.firstSSLNegociation && this.session.containsAttribute(SslFilter.USE_NOTIFICATION)) {

this.firstSSLNegociation = false;
scheduleMessageReceived(nextFilter, SslFilter.SESSION_SECURED);
} 

if (LOGGER.isDebugEnabled()) {
if (!isOutboundDone()) {
LOGGER.debug("{} is now secured", this.sslFilter.getSessionInfo(this.session));
} else {
LOGGER.debug("{} is not secured yet", this.sslFilter.getSessionInfo(this.session));
} 
}
return;

case NEED_TASK:
if (LOGGER.isDebugEnabled()) {
LOGGER.debug("{} processing the NEED_TASK state", this.sslFilter.getSessionInfo(this.session));
}

this.handshakeStatus = doTasks();
continue;

case NEED_UNWRAP:
if (LOGGER.isDebugEnabled()) {
LOGGER.debug("{} processing the NEED_UNWRAP state", this.sslFilter.getSessionInfo(this.session));
}

status = unwrapHandshake(nextFilter);

if ((status == SSLEngineResult.Status.BUFFER_UNDERFLOW && this.handshakeStatus != SSLEngineResult.HandshakeStatus.FINISHED) || isInboundDone()) {
return;
}
continue;

case NEED_WRAP:
if (LOGGER.isDebugEnabled()) {
LOGGER.debug("{} processing the NEED_WRAP state", this.sslFilter.getSessionInfo(this.session));
}

if (this.outNetBuffer != null && this.outNetBuffer.hasRemaining()) {
return;
}

createOutNetBuffer(0);

while (true) {
result = this.sslEngine.wrap(this.emptyBuffer.buf(), this.outNetBuffer.buf());
if (result.getStatus() == SSLEngineResult.Status.BUFFER_OVERFLOW) {
this.outNetBuffer.capacity(this.outNetBuffer.capacity() << 1);
this.outNetBuffer.limit(this.outNetBuffer.capacity());

continue;
} 
break;
} 
this.outNetBuffer.flip();
this.handshakeStatus = result.getHandshakeStatus();
writeNetBuffer(nextFilter); continue;
} 
break;
} 
String msg = "Invalid Handshaking State" + this.handshakeStatus + " while processing the Handshake for session " + this.session.getId();

LOGGER.error(msg);
throw new IllegalStateException(msg);
}

private void createOutNetBuffer(int expectedRemaining) {
int capacity = Math.max(expectedRemaining, this.sslEngine.getSession().getPacketBufferSize());

if (this.outNetBuffer != null) {
this.outNetBuffer.capacity(capacity);
} else {
this.outNetBuffer = IoBuffer.allocate(capacity).minimumCapacity(0);
} 
}

WriteFuture writeNetBuffer(IoFilter.NextFilter nextFilter) throws SSLException {
DefaultWriteFuture defaultWriteFuture;
if (this.outNetBuffer == null || !this.outNetBuffer.hasRemaining())
{
return null;
}

this.writingEncryptedData = true;

WriteFuture writeFuture = null;

try {
IoBuffer writeBuffer = fetchOutNetBuffer();
defaultWriteFuture = new DefaultWriteFuture(this.session);
this.sslFilter.filterWrite(nextFilter, this.session, (WriteRequest)new DefaultWriteRequest(writeBuffer, (WriteFuture)defaultWriteFuture));

while (needToCompleteHandshake()) {
try {
handshake(nextFilter);
} catch (SSLException ssle) {
SSLException newSsle = new SSLHandshakeException("SSL handshake failed.");
newSsle.initCause(ssle);
throw newSsle;
} 

IoBuffer outNetBuffer = fetchOutNetBuffer();
if (outNetBuffer != null && outNetBuffer.hasRemaining()) {
defaultWriteFuture = new DefaultWriteFuture(this.session);
this.sslFilter.filterWrite(nextFilter, this.session, (WriteRequest)new DefaultWriteRequest(outNetBuffer, (WriteFuture)defaultWriteFuture));
} 
} 
} finally {
this.writingEncryptedData = false;
} 

return (WriteFuture)defaultWriteFuture;
}

private SSLEngineResult.Status unwrapHandshake(IoFilter.NextFilter nextFilter) throws SSLException {
if (this.inNetBuffer != null) {
this.inNetBuffer.flip();
}

if (this.inNetBuffer == null || !this.inNetBuffer.hasRemaining())
{
return SSLEngineResult.Status.BUFFER_UNDERFLOW;
}

SSLEngineResult res = unwrap();
this.handshakeStatus = res.getHandshakeStatus();

checkStatus(res);

if (this.handshakeStatus == SSLEngineResult.HandshakeStatus.FINISHED && res.getStatus() == SSLEngineResult.Status.OK && this.inNetBuffer.hasRemaining()) {

res = unwrap();

if (this.inNetBuffer.hasRemaining()) {
this.inNetBuffer.compact();
} else {
this.inNetBuffer.free();
this.inNetBuffer = null;
} 

renegotiateIfNeeded(nextFilter, res);

}
else if (this.inNetBuffer.hasRemaining()) {
this.inNetBuffer.compact();
} else {
this.inNetBuffer.free();
this.inNetBuffer = null;
} 

return res.getStatus();
}

private void renegotiateIfNeeded(IoFilter.NextFilter nextFilter, SSLEngineResult res) throws SSLException {
if (res.getStatus() != SSLEngineResult.Status.CLOSED && res.getStatus() != SSLEngineResult.Status.BUFFER_UNDERFLOW && res.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) {

this.handshakeComplete = false;
this.handshakeStatus = res.getHandshakeStatus();
handshake(nextFilter);
} 
}

private SSLEngineResult unwrap() throws SSLException {
SSLEngineResult res;
if (this.appBuffer == null) {
this.appBuffer = IoBuffer.allocate(this.inNetBuffer.remaining());
} else {

this.appBuffer.expand(this.inNetBuffer.remaining());
} 

SSLEngineResult.Status status = null;
SSLEngineResult.HandshakeStatus handshakeStatus = null;

do {
res = this.sslEngine.unwrap(this.inNetBuffer.buf(), this.appBuffer.buf());
status = res.getStatus();

handshakeStatus = res.getHandshakeStatus();

if (status != SSLEngineResult.Status.BUFFER_OVERFLOW) {
continue;
}
this.appBuffer.capacity(this.appBuffer.capacity() << 1);
this.appBuffer.limit(this.appBuffer.capacity());

}
while ((status == SSLEngineResult.Status.OK || status == SSLEngineResult.Status.BUFFER_OVERFLOW) && (handshakeStatus == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING || handshakeStatus == SSLEngineResult.HandshakeStatus.NEED_UNWRAP));

return res;
}

private SSLEngineResult.HandshakeStatus doTasks() {
Runnable runnable;
while ((runnable = this.sslEngine.getDelegatedTask()) != null)
{

runnable.run();
}
return this.sslEngine.getHandshakeStatus();
}

static IoBuffer copy(ByteBuffer src) {
IoBuffer copy = IoBuffer.allocate(src.remaining());
copy.put(src);
copy.flip();
return copy;
}

public String toString() {
StringBuilder sb = new StringBuilder();

sb.append("SSLStatus <");

if (this.handshakeComplete) {
sb.append("SSL established");
} else {
sb.append("Processing Handshake").append("; ");
sb.append("Status : ").append(this.handshakeStatus).append("; ");
} 

sb.append(", ");
sb.append("HandshakeComplete :").append(this.handshakeComplete).append(", ");
sb.append(">");

return sb.toString();
}

void release() {
if (this.inNetBuffer != null) {
this.inNetBuffer.free();
this.inNetBuffer = null;
} 

if (this.outNetBuffer != null) {
this.outNetBuffer.free();
this.outNetBuffer = null;
} 
}
}

