package core.network.game2zone;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.websocket.BaseConnector;
import com.zhonglian.server.websocket.BaseIoHandler;
import com.zhonglian.server.websocket.BaseSession;
import com.zhonglian.server.websocket.IMessageDispatcher;
import com.zhonglian.server.websocket.codecfactory.ServerDecoder;
import com.zhonglian.server.websocket.codecfactory.WebEncoder;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.server.ServerConfig;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ZoneConnector extends BaseConnector<ZoneSession> {
private static ZoneConnector instance = new ZoneConnector();
private boolean logined = false;

public static ZoneConnector getInstance() {
return instance;
}

public static class ZoneConnectorIoHandler
extends BaseIoHandler<ZoneSession> {
public ZoneSession createSession(IoSession session, long sessionID) {
return new ZoneSession(session, sessionID);
}
}

private ZoneConnector() {
super(new ZoneConnectorIoHandler(), (ProtocolEncoder)new WebEncoder(), (ProtocolDecoder)new ServerDecoder());
ZoneMessageDispatcher dispatcher = new ZoneMessageDispatcher();
dispatcher.init();
this.handler.setMessageDispatcher((IMessageDispatcher)dispatcher);
}

public boolean isLogined() {
return this.logined;
}

public void setLogined(boolean logined) {
this.logined = logined;
}

public static void request(String event, Object protocol, ResponseHandler handler) {
if (!instance.logined || instance._session == null) {
instance.onSendFailed(event, protocol, handler);
} else {
((ZoneSession)instance._session).request(event, protocol, handler);
} 
}

public static void request(int descServer, String event, Object protocol, ResponseHandler handler) {
if (!instance.logined || instance._session == null) {
instance.onSendFailed(event, protocol, handler);
} else {
((ZoneSession)instance._session).request(TerminalType.GameServer, descServer, event, protocol, handler);
} 
}

public void notifyMessage(String event, Object protocol) {
if (this._session != null)
((ZoneSession)this._session).notifyMessage(event, protocol); 
}

public void notifyMessage(int descServer, String event, Object protocol) {
if (this._session != null) {
((ZoneSession)this._session).notifyMessage(TerminalType.GameServer, descServer, event, protocol);
}
}

private void onSendFailed(String event, Object protocol, ResponseHandler handler) {
SyncTaskManager.task(() -> {
MessageHeader header = new MessageHeader();
header.messageType = MessageType.Request;
header.srcType = (byte)TerminalType.GameServer.ordinal();
header.srcId = ServerConfig.ServerID();
header.descType = (byte)TerminalType.ZoneServer.ordinal();
header.descId = ServerConfig.ServerID();
header.event = paramString;
header.descId = -1L;
header.descType = (byte)TerminalType.ZoneServer.ordinal();
header.sequence = 0;
WebSocketResponse response = new WebSocketResponse(null, header);
paramResponseHandler.handleError(response, ErrorCode.Server_NotConnected.value(), "未连接ZoneServer");
});
}
}

