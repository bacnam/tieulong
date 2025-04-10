package core.network.client2game;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.BaseSession;
import com.zhonglian.server.websocket.IMessageDispatcher;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.def.MessageType;
import com.zhonglian.server.websocket.def.TerminalType;
import com.zhonglian.server.websocket.handler.MessageHeader;
import com.zhonglian.server.websocket.handler.requset.RequestHandler;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.BaseHandler;
import core.network.client2game.handler.WorldForwardHandler;
import core.network.client2game.handler.ZoneForwardHandler;
import core.server.ServerConfig;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandlerDispatcher
implements IMessageDispatcher<ClientSession>
{
protected final Map<String, RequestHandler> _handlers = new ConcurrentHashMap<>();

public void init() {
addHandlerByPath(BaseHandler.class.getPackage().getName());
ZoneForwardHandler zfh = new ZoneForwardHandler();
ProtoForward.ZoneForwardList.forEach(hander -> { 
}); WorldForwardHandler wfh = new WorldForwardHandler();
ProtoForward.WorldForwardList.forEach(hander -> {

});
}

private void addHandlerByPath(String path) {
List<Class<?>> dealers = CommClass.getAllClassByInterface(RequestHandler.class, path);
for (Class<?> cs : dealers) {
RequestHandler dealer = null;
try {
dealer = CommClass.forName(cs.getName()).newInstance();
} catch (Exception e) {
CommLog.error(e.getMessage(), e);
} 

if (dealer == null) {
continue;
}
this._handlers.put(dealer.getEvent().toLowerCase(), dealer);
} 
}

public int handleRawMessage(ClientSession session, ByteBuffer stream) {
MessageHeader header = new MessageHeader();
short eventlength = 0;
try {
header.messageType = MessageType.values()[stream.get()];
header.srcType = (byte)TerminalType.Client.ordinal();
header.srcId = (session.getPlayer() == null) ? 0L : session.getPlayer().getPid();
header.descType = (byte)TerminalType.GameServer.ordinal();
header.descId = ServerConfig.ServerID();
header.sequence = stream.getShort();

byte[] event = new byte[eventlength = stream.getShort()];
stream.get(event);
header.event = new String(event, "UTF-8");

RequestHandler handler = this._handlers.get(header.event.toLowerCase());
if (handler == null) {
session.sendError(header, ErrorCode.Request_NotFoundHandler, "没有相关handler:" + header.event);
CommLog.error("[WSBaseSocketListener] 没有相关handler:{}", header.event);
return -1;
} 

byte[] msg = new byte[stream.getShort()];
stream.get(msg);
SyncTaskManager.task(() -> {
try {
paramRequestHandler.handleMessage(new WebSocketRequest(paramClientSession, paramMessageHeader), new String(paramArrayOfbyte, "UTF-8"));
} catch (Exception e) {
CommLog.error("[WSBaseSocketListener] 协议处理协议信息处理时错误:", e);
} 
});
return 0;
} catch (Exception e) {
CommLog.error("错误的消息头:{},event长度:{}", (new Gson()).toJson(header), Short.valueOf(eventlength));

return -2;
} 
}
}

