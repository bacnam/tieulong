package core.network.game2world;

import BaseTask.SyncTask.SyncTaskManager;
import business.global.chat.ChatMgr;
import com.zhonglian.server.websocket.BaseConnector;
import com.zhonglian.server.websocket.BaseIoHandler;
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

public class WorldConnector extends BaseConnector<WorldSession> {
    private static WorldConnector instance = new WorldConnector();
    private boolean logined = false;

    public static WorldConnector getInstance() {
        return instance;
    }

    private WorldConnector() {
        super(new WorldConnector.WorldConnectorIoHandler(), new WebEncoder(), new ServerDecoder());
        WorldMessageDispatcher dispatcher = new WorldMessageDispatcher();
        dispatcher.init();
        this.handler.setMessageDispatcher(dispatcher);
    }

    public boolean isLogined() {
        return this.logined;
    }

    public void setLogined(boolean logined) {
        this.logined = logined;
    }

    public static void request(String event, Object protocol, ResponseHandler handler) {
        if (instance.logined && instance._session != null) {
            instance._session.request(event, protocol, handler);
        } else {
            instance.onSendFailed(event, protocol, handler);
        }
    }

    public static void request(int descServer, String event, Object protocol, ResponseHandler handler) {
        if (instance.logined && instance._session != null) {
            instance._session.request(TerminalType.GameServer, descServer, event, protocol, handler);
        } else {
            instance.onSendFailed(event, protocol, handler);
        }
    }

    public static void notifyMessage(String event, Object protocol) {
        if (instance._session != null) {
            instance._session.notifyMessage(event, protocol);
        }
    }

    public void notifyMessage(int descServer, String event, Object protocol) {
        if (this._session != null) {
            this._session.notifyMessage(TerminalType.GameServer, descServer, event, protocol);
        }
    }

    private void onSendFailed(String event, Object protocol, ResponseHandler handler) {
        SyncTaskManager.task(() -> {
            MessageHeader header = new MessageHeader();
            header.messageType = MessageType.Request;
            header.srcType = (byte)TerminalType.GameServer.ordinal();
            header.srcId = ServerConfig.ServerID();
            header.descType = (byte)TerminalType.WorldServer.ordinal();
            header.descId = ServerConfig.ServerID();
            header.event = event;
            header.descId = -1L;
            header.descType = (byte)TerminalType.WorldServer.ordinal();
            header.sequence = 0;
            WebSocketResponse response = new WebSocketResponse(null, header);
            handler.handleError(response, ErrorCode.Server_NotConnected.value(), "未连接WorldServer");
        });
    }

    public void onConnect() {
        ChatMgr.getInstance().init();
    }

    public static class WorldConnectorIoHandler extends BaseIoHandler<WorldSession> {
        public WorldSession createSession(IoSession session, long sessionID) {
            return new WorldSession(session, sessionID);
        }
    }
}
