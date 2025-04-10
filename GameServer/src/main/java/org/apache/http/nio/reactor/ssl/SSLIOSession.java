package org.apache.http.nio.reactor.ssl;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import org.apache.http.HttpHost;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.nio.reactor.SessionBufferStatus;
import org.apache.http.nio.reactor.SocketAccessor;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
public class SSLIOSession
implements IOSession, SessionBufferStatus, SocketAccessor
{
public static final String SESSION_KEY = "http.session.ssl";
private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);

private final IOSession session;

private final SSLEngine sslEngine;

private final SSLBuffer inEncrypted;

private final SSLBuffer outEncrypted;

private final SSLBuffer inPlain;

private final SSLBuffer outPlain;

private final InternalByteChannel channel;

private final SSLSetupHandler handler;

private int appEventMask;

private SessionBufferStatus appBufferStatus;

private boolean endOfStream;

private volatile SSLMode sslMode;

private volatile int status;

private volatile boolean initialized;

public SSLIOSession(IOSession session, SSLMode sslMode, HttpHost host, SSLContext sslContext, SSLSetupHandler handler) {
this(session, sslMode, host, sslContext, handler, new PermanentSSLBufferManagementStrategy());
}

public SSLIOSession(IOSession session, SSLMode sslMode, HttpHost host, SSLContext sslContext, SSLSetupHandler handler, SSLBufferManagementStrategy bufferManagementStrategy) {
Args.notNull(session, "IO session");
Args.notNull(sslContext, "SSL context");
Args.notNull(bufferManagementStrategy, "Buffer management strategy");
this.session = session;
this.sslMode = sslMode;
this.appEventMask = session.getEventMask();
this.channel = new InternalByteChannel();
this.handler = handler;

this.session.setBufferStatus(this);

if (this.sslMode == SSLMode.CLIENT && host != null) {
this.sslEngine = sslContext.createSSLEngine(host.getHostName(), host.getPort());
} else {
this.sslEngine = sslContext.createSSLEngine();
} 

int netBuffersize = this.sslEngine.getSession().getPacketBufferSize();
this.inEncrypted = bufferManagementStrategy.constructBuffer(netBuffersize);
this.outEncrypted = bufferManagementStrategy.constructBuffer(netBuffersize);

int appBuffersize = this.sslEngine.getSession().getApplicationBufferSize();
this.inPlain = bufferManagementStrategy.constructBuffer(appBuffersize);
this.outPlain = bufferManagementStrategy.constructBuffer(appBuffersize);
}

public SSLIOSession(IOSession session, SSLMode sslMode, SSLContext sslContext, SSLSetupHandler handler) {
this(session, sslMode, null, sslContext, handler);
}

protected SSLSetupHandler getSSLSetupHandler() {
return this.handler;
}

public boolean isInitialized() {
return this.initialized;
}

@Deprecated
public synchronized void initialize(SSLMode sslMode) throws SSLException {
this.sslMode = sslMode;
initialize();
}

public synchronized void initialize() throws SSLException {
Asserts.check(!this.initialized, "SSL I/O session already initialized");
if (this.status >= 1) {
return;
}
switch (this.sslMode) {
case NEED_WRAP:
this.sslEngine.setUseClientMode(true);
break;
case NEED_UNWRAP:
this.sslEngine.setUseClientMode(false);
break;
} 
if (this.handler != null) {
this.handler.initalize(this.sslEngine);
}
this.initialized = true;
this.sslEngine.beginHandshake();

this.inEncrypted.release();
this.outEncrypted.release();
this.inPlain.release();
this.outPlain.release();

doHandshake();
}

public synchronized SSLSession getSSLSession() {
return this.sslEngine.getSession();
}

private SSLException convert(RuntimeException ex) {
Throwable cause = ex.getCause();
if (cause == null) {
cause = ex;
}
return new SSLException(cause);
}

private SSLEngineResult doWrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
try {
return this.sslEngine.wrap(src, dst);
} catch (RuntimeException ex) {
throw convert(ex);
} 
}

private SSLEngineResult doUnwrap(ByteBuffer src, ByteBuffer dst) throws SSLException {
try {
return this.sslEngine.unwrap(src, dst);
} catch (RuntimeException ex) {
throw convert(ex);
} 
}

private void doRunTask() throws SSLException {
try {
Runnable r = this.sslEngine.getDelegatedTask();
if (r != null) {
r.run();
}
} catch (RuntimeException ex) {
throw convert(ex);
} 
}

private void doHandshake() throws SSLException {
boolean handshaking = true;

SSLEngineResult result = null;
while (handshaking) {
ByteBuffer outPlainBuf; ByteBuffer outEncryptedBuf; ByteBuffer inEncryptedBuf; ByteBuffer inPlainBuf; switch (this.sslEngine.getHandshakeStatus()) {

case NEED_WRAP:
outPlainBuf = this.outPlain.acquire();
outEncryptedBuf = this.outEncrypted.acquire();

outPlainBuf.flip();
result = doWrap(outPlainBuf, outEncryptedBuf);
outPlainBuf.compact();

if (outPlainBuf.position() == 0) {
this.outPlain.release();
outPlainBuf = null;
} 

if (result.getStatus() != SSLEngineResult.Status.OK) {
handshaking = false;
}

case NEED_UNWRAP:
inEncryptedBuf = this.inEncrypted.acquire();
inPlainBuf = this.inPlain.acquire();

inEncryptedBuf.flip();
result = doUnwrap(inEncryptedBuf, inPlainBuf);
inEncryptedBuf.compact();

try {
if (!inEncryptedBuf.hasRemaining() && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
throw new SSLException("Input buffer is full");
}
} finally {

if (inEncryptedBuf.position() == 0) {
this.inEncrypted.release();
inEncryptedBuf = null;
} 
} 

if (this.status >= 1) {
this.inPlain.release();
inPlainBuf = null;
} 
if (result.getStatus() != SSLEngineResult.Status.OK) {
handshaking = false;
}

case NEED_TASK:
doRunTask();

case NOT_HANDSHAKING:
handshaking = false;
} 

} 
if (result != null && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED && 
this.handler != null) {
this.handler.verify(this.session, this.sslEngine.getSession());
}
}

private void updateEventMask() {
if (this.status == 1 && this.sslEngine.isOutboundDone() && (this.endOfStream || this.sslEngine.isInboundDone()))
{
this.status = Integer.MAX_VALUE;
}

if (this.status == 0 && this.endOfStream && this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
{
this.status = Integer.MAX_VALUE;
}
if (this.status == Integer.MAX_VALUE) {
this.session.close();

return;
} 
int oldMask = this.session.getEventMask();
int newMask = oldMask;
switch (this.sslEngine.getHandshakeStatus()) {
case NEED_WRAP:
newMask = 5;
break;
case NEED_UNWRAP:
newMask = 1;
break;
case NOT_HANDSHAKING:
newMask = this.appEventMask;
break;
} 

if (this.outEncrypted.hasData()) {
newMask |= 0x4;
}

if (oldMask != newMask) {
this.session.setEventMask(newMask);
}
}

private int sendEncryptedData() throws IOException {
if (!this.outEncrypted.hasData())
{

return this.session.channel().write(EMPTY_BUFFER);
}

ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();

outEncryptedBuf.flip();
int bytesWritten = this.session.channel().write(outEncryptedBuf);
outEncryptedBuf.compact();

if (outEncryptedBuf.position() == 0) {
this.outEncrypted.release();
}
return bytesWritten;
}

private int receiveEncryptedData() throws IOException {
if (this.endOfStream) {
return -1;
}

ByteBuffer inEncryptedBuf = this.inEncrypted.acquire();

int ret = this.session.channel().read(inEncryptedBuf);

if (inEncryptedBuf.position() == 0) {
this.inEncrypted.release();
}
return ret;
}

private boolean decryptData() throws SSLException {
boolean decrypted = false;
while (this.inEncrypted.hasData()) {

ByteBuffer inEncryptedBuf = this.inEncrypted.acquire();
ByteBuffer inPlainBuf = this.inPlain.acquire();

inEncryptedBuf.flip();
SSLEngineResult result = doUnwrap(inEncryptedBuf, inPlainBuf);
inEncryptedBuf.compact();

try { if (!inEncryptedBuf.hasRemaining() && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP) {
throw new SSLException("Input buffer is full");
}
if (result.getStatus() == SSLEngineResult.Status.OK)
{ decrypted = true;

}

else

{ 

if (this.inEncrypted.acquire().position() == 0)
this.inEncrypted.release();  break; }  if (result.getHandshakeStatus() != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING) { if (this.inEncrypted.acquire().position() == 0) this.inEncrypted.release();  break; }  if (this.endOfStream) { if (this.inEncrypted.acquire().position() == 0) this.inEncrypted.release();  break; }  } finally { if (this.inEncrypted.acquire().position() == 0) this.inEncrypted.release();
}

} 
return decrypted;
}

public synchronized boolean isAppInputReady() throws IOException {
do {
int bytesRead = receiveEncryptedData();
if (bytesRead == -1) {
this.endOfStream = true;
}
doHandshake();
SSLEngineResult.HandshakeStatus status = this.sslEngine.getHandshakeStatus();
if (status != SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING && status != SSLEngineResult.HandshakeStatus.FINISHED)
continue;  decryptData();
}
while (this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_TASK);

return ((this.appEventMask & 0x1) > 0 && (this.inPlain.hasData() || (this.appBufferStatus != null && this.appBufferStatus.hasBufferedInput()) || (this.endOfStream && this.status == 0)));
}

public synchronized boolean isAppOutputReady() throws IOException {
return ((this.appEventMask & 0x4) > 0 && this.status == 0 && this.sslEngine.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING);
}

public synchronized void inboundTransport() throws IOException {
updateEventMask();
}

public synchronized void outboundTransport() throws IOException {
sendEncryptedData();
doHandshake();
updateEventMask();
}

public synchronized boolean isInboundDone() {
return this.sslEngine.isInboundDone();
}

public synchronized boolean isOutboundDone() {
return this.sslEngine.isOutboundDone();
}

private synchronized int writePlain(ByteBuffer src) throws SSLException {
Args.notNull(src, "Byte buffer");
if (this.status != 0) {
return -1;
}
if (this.outPlain.hasData()) {

ByteBuffer outPlainBuf = this.outPlain.acquire();
ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();

outPlainBuf.flip();
doWrap(outPlainBuf, outEncryptedBuf);
outPlainBuf.compact();

if (outPlainBuf.position() == 0) {
this.outPlain.release();
outPlainBuf = null;
} 
} 
if (!this.outPlain.hasData()) {
ByteBuffer outEncryptedBuf = this.outEncrypted.acquire();
SSLEngineResult result = doWrap(src, outEncryptedBuf);
if (result.getStatus() == SSLEngineResult.Status.CLOSED) {
this.status = Integer.MAX_VALUE;
}
return result.bytesConsumed();
} 
return 0;
}

private synchronized int readPlain(ByteBuffer dst) {
Args.notNull(dst, "Byte buffer");
if (this.inPlain.hasData()) {

ByteBuffer inPlainBuf = this.inPlain.acquire();

inPlainBuf.flip();
int n = Math.min(inPlainBuf.remaining(), dst.remaining());
for (int i = 0; i < n; i++) {
dst.put(inPlainBuf.get());
}
inPlainBuf.compact();

if (inPlainBuf.position() == 0) {
this.inPlain.release();
inPlainBuf = null;
} 
return n;
} 
if (this.endOfStream) {
return -1;
}
return 0;
}

public synchronized void close() {
if (this.status >= 1) {
return;
}

this.status = 1;
this.sslEngine.closeOutbound();
updateEventMask();
}

public synchronized void shutdown() {
if (this.status == Integer.MAX_VALUE) {
return;
}

this.inEncrypted.release();
this.outEncrypted.release();
this.inPlain.release();
this.outPlain.release();

this.status = Integer.MAX_VALUE;
this.session.shutdown();
}

public int getStatus() {
return this.status;
}

public boolean isClosed() {
return (this.status >= 1 || this.session.isClosed());
}

public ByteChannel channel() {
return this.channel;
}

public SocketAddress getLocalAddress() {
return this.session.getLocalAddress();
}

public SocketAddress getRemoteAddress() {
return this.session.getRemoteAddress();
}

public synchronized int getEventMask() {
return this.appEventMask;
}

public synchronized void setEventMask(int ops) {
this.appEventMask = ops;
updateEventMask();
}

public synchronized void setEvent(int op) {
this.appEventMask |= op;
updateEventMask();
}

public synchronized void clearEvent(int op) {
this.appEventMask &= op ^ 0xFFFFFFFF;
updateEventMask();
}

public int getSocketTimeout() {
return this.session.getSocketTimeout();
}

public void setSocketTimeout(int timeout) {
this.session.setSocketTimeout(timeout);
}

public synchronized boolean hasBufferedInput() {
return ((this.appBufferStatus != null && this.appBufferStatus.hasBufferedInput()) || this.inEncrypted.hasData() || this.inPlain.hasData());
}

public synchronized boolean hasBufferedOutput() {
return ((this.appBufferStatus != null && this.appBufferStatus.hasBufferedOutput()) || this.outEncrypted.hasData() || this.outPlain.hasData());
}

public synchronized void setBufferStatus(SessionBufferStatus status) {
this.appBufferStatus = status;
}

public Object getAttribute(String name) {
return this.session.getAttribute(name);
}

public Object removeAttribute(String name) {
return this.session.removeAttribute(name);
}

public void setAttribute(String name, Object obj) {
this.session.setAttribute(name, obj);
}

private static void formatOps(StringBuilder buffer, int ops) {
if ((ops & 0x1) > 0) {
buffer.append('r');
}
if ((ops & 0x4) > 0) {
buffer.append('w');
}
}

public String toString() {
StringBuilder buffer = new StringBuilder();
buffer.append(this.session);
buffer.append("[");
switch (this.status) {
case 0:
buffer.append("ACTIVE");
break;
case 1:
buffer.append("CLOSING");
break;
case 2147483647:
buffer.append("CLOSED");
break;
} 
buffer.append("][");
formatOps(buffer, this.appEventMask);
buffer.append("][");
buffer.append(this.sslEngine.getHandshakeStatus());
if (this.sslEngine.isInboundDone()) {
buffer.append("][inbound done][");
}
if (this.sslEngine.isOutboundDone()) {
buffer.append("][outbound done][");
}
if (this.endOfStream) {
buffer.append("][EOF][");
}
buffer.append("][");
buffer.append(!this.inEncrypted.hasData() ? 0 : this.inEncrypted.acquire().position());
buffer.append("][");
buffer.append(!this.inPlain.hasData() ? 0 : this.inPlain.acquire().position());
buffer.append("][");
buffer.append(!this.outEncrypted.hasData() ? 0 : this.outEncrypted.acquire().position());
buffer.append("][");
buffer.append(!this.outPlain.hasData() ? 0 : this.outPlain.acquire().position());
buffer.append("]");
return buffer.toString();
}

public Socket getSocket() {
if (this.session instanceof SocketAccessor) {
return ((SocketAccessor)this.session).getSocket();
}
return null;
}

private class InternalByteChannel
implements ByteChannel {
private InternalByteChannel() {}

public int write(ByteBuffer src) throws IOException {
return SSLIOSession.this.writePlain(src);
}

public int read(ByteBuffer dst) throws IOException {
return SSLIOSession.this.readPlain(dst);
}

public void close() throws IOException {
SSLIOSession.this.close();
}

public boolean isOpen() {
return !SSLIOSession.this.isClosed();
}
}
}

