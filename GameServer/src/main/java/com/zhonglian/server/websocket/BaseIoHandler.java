package com.zhonglian.server.websocket;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.util.Base64;

public abstract class BaseIoHandler<Session extends BaseSession>
implements IoHandler
{
public static final Object SESSION_ID = new Object();
private static final AtomicLong _IDFactory = new AtomicLong(1L);
protected final Map<Long, Session> connections = Maps.newConcurrentHashMap();
protected IMessageDispatcher<Session> messageDispatcher;

public Session getSession(long sessionId) {
return this.connections.get(Long.valueOf(sessionId));
}

public void setMessageDispatcher(IMessageDispatcher<Session> messageDispatcher) {
this.messageDispatcher = messageDispatcher;
}

public void messageSent(IoSession session, Object message) throws Exception {
if (!(message instanceof String)) {
return;
}
long sessionId = ((Long)session.getAttribute(SESSION_ID)).longValue();
BaseSession client = (BaseSession)this.connections.get(Long.valueOf(sessionId));
if (client == null) {
return;
}
SyncTaskManager.task(() -> paramBaseSession.onSent((String)paramObject));
}

public void messageReceived(IoSession session, Object message) throws Exception {
long sessionId = ((Long)session.getAttribute(SESSION_ID)).longValue();
if (this.messageDispatcher == null) {
CommLog.error("协议派发器未初始化");
return;
} 
BaseSession baseSession = (BaseSession)this.connections.get(Long.valueOf(sessionId));
if (baseSession == null) {
return;
}

if (message instanceof String) {
session.write(getSecWebSocketAccept((String)message));
} else if (message instanceof ByteBuffer) {
this.messageDispatcher.handleRawMessage((Session)baseSession, (ByteBuffer)message);
} 
}

public String getSecWebSocketAccept(String msg) throws UnsupportedEncodingException {
String secKey = getSecWebSocketKey(msg);

secKey = String.valueOf(secKey) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
try {
MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update(secKey.getBytes("iso-8859-1"), 0, secKey.length());
byte[] sha1Hash = md.digest();
secKey = new String(Base64.encodeBase64(sha1Hash));
} catch (Exception e) {
e.printStackTrace();
} 

return "HTTP/1.1 101 Switching Protocols\r\nUpgrade: websocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept: " + 

secKey + "\r\n\r\n";
}

private String getSecWebSocketKey(String req) {
Pattern p = Pattern.compile("^(Sec-WebSocket-Key:).+", 10);
Matcher m = p.matcher(req);
if (m.find()) {
String foundstring = m.group();
return foundstring.split(":")[1].trim();
} 
return null;
}

public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
StringBuilder s = new StringBuilder();
s.append(Calendar.getInstance().getTime().toString());
s.append(":\n session status:\n");
IoServiceStatistics stat = session.getService().getStatistics();
s.append("Read Bytes:").append(stat.getReadBytesThroughput()).append("B/S\n");
s.append("Write Bytes:").append(stat.getWrittenBytesThroughput()).append("B/S\n");
s.append("Read Message:").append(stat.getReadMessagesThroughput()).append("pkt/S\n");
s.append("Write Message:").append(stat.getWrittenMessagesThroughput()).append("pkt/S\n");
CommLog.info(s.toString());
}

public void sessionCreated(IoSession session) throws Exception {
if (session.getRemoteAddress() == null) {
CommLog.error("session {} created, but no remote address!", session.getRemoteAddress());
return;
} 
long sessionId = _IDFactory.incrementAndGet();
session.setAttribute(SESSION_ID, Long.valueOf(sessionId));
Session client = createSession(session, sessionId);
this.connections.put(Long.valueOf(sessionId), client);
if (client != null) {
SyncTaskManager.task(() -> {
if (paramBaseSession != null) {
paramBaseSession.onCreated();
}
});
}
CommLog.debug("create session:{}, id:{}!!!", client.getClass().getName(), Long.valueOf(sessionId));
}

public void sessionOpened(IoSession session) throws Exception {
CommLog.debug("session {} opened at {}", SESSION_ID, Integer.valueOf(CommTime.nowSecond()));
}

public void sessionClosed(IoSession session) throws Exception {
long sessionId = ((Long)session.getAttribute(SESSION_ID)).longValue();
BaseSession client = (BaseSession)this.connections.remove(Long.valueOf(sessionId));
if (client == null) {
CommLog.error("close session:{}, id:{}!!!", session.getClass().getName(), Long.valueOf(sessionId));
return;
} 
CommLog.debug("close client:{}, id:{}!!!", client.getClass().getName(), Long.valueOf(sessionId));
SyncTaskManager.task(() -> paramBaseSession.onClosed());
}

public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
session.close(true);
CommLog.info("client exceptionCaughted closed sessionid:{}", session.getAttribute(SESSION_ID));
}

public void inputClosed(IoSession session) throws Exception {
session.close(true);
CommLog.info("client inputClosed sessionid:{}", session.getAttribute(SESSION_ID));
}

public abstract Session createSession(IoSession paramIoSession, long paramLong);
}

