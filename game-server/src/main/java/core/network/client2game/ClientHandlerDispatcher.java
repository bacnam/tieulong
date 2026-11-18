package core.network.client2game;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import com.google.gson.Gson;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandlerDispatcher implements IMessageDispatcher<ClientSession> {
    protected final Map<String, RequestHandler> _handlers = new ConcurrentHashMap<>();

    @Override
    public void init() {
        this.addHandlerByPath(BaseHandler.class.getPackage().getName());
        ZoneForwardHandler zfh = new ZoneForwardHandler();
        ProtoForward.ZoneForwardList.forEach(hander -> this._handlers.put(hander, zfh));
        WorldForwardHandler wfh = new WorldForwardHandler();
        ProtoForward.WorldForwardList.forEach(hander -> this._handlers.put(hander, wfh));
    }

    private void addHandlerByPath(String path) {
        for (Class<?> cs : CommClass.getAllClassByInterface(RequestHandler.class, path)) {
            RequestHandler dealer = null;

            try {
                dealer = (RequestHandler)CommClass.forName(cs.getName()).newInstance();
            } catch (Exception var7) {
                CommLog.error(var7.getMessage(), (Throwable)var7);
            }

            if (dealer != null) {
                this._handlers.put(dealer.getEvent().toLowerCase(), dealer);
            }
        }
    }

    public int handleRawMessage(ClientSession session, ByteBuffer stream) {
        MessageHeader header = new MessageHeader();
        short eventlength = 0;

        try {
            header.messageType = MessageType.values()[stream.get()];
            header.srcType = (byte)TerminalType.Client.ordinal();
            header.srcId = session.getPlayer() == null ? 0L : session.getPlayer().getPid();
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
            } else {
                byte[] msg = new byte[stream.getShort()];
                stream.get(msg);
                SyncTaskManager.task(() -> {
                    try {
                        handler.handleMessage(new WebSocketRequest(session, header), new String(msg, "UTF-8"));
                    } catch (Exception var5x) {
                        CommLog.error("[WSBaseSocketListener] 协议处理协议信息处理时错误:", (Throwable)var5x);
                    }
                });
                return 0;
            }
        } catch (Exception var8) {
            CommLog.error("错误的消息头:{},event长度:{}", new Gson().toJson(header), eventlength);
            return -2;
        }
    }
}
